// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

///home/anerishah/step-donation/step28-2020/Donation-platform/src/main/webapp/frontend/src
///usr/local/nvm/versions/node/v10.14.2/bin/node

var googleTrends = require('/home/anerishah/step-donation/step28-2020/Donation-platform/src/main/webapp/frontend/node_modules/google-trends-api/lib/google-trends-api.min.js');
const fetch = require("node-fetch");

function sendQuery() {
    fetch('https://donations-step-2020-new.uc.r.appspot.com/tag-query', {method: 'GET'})
    //fetch('https://8080-5a8baf4b-def3-4b41-83c1-47f3b4af6cdc.us-west1.cloudshell.dev/tag-query', {method: 'GET'})
        .then(function(response) {
            console.log(response);
            return response.json();
        })
        .then(function(response) {
            console.log(response);
            //response.forEach(function(tag) {
                //console.log(tag.name, ": ", tag.trendingScore);
                //tag.trendingScore = 0;
                //console.log(tag.name, ": ", tag.trendingScore);
            //});
            //console.log(response);
        })
        .catch(function(err) {
            console.log("error found", err);
        });
}

function sendPostQuery(tagArray) {
    console.log("in other func: ", tagArray);
    console.log("stringified version: ", JSON.stringify(tagArray));
    fetch('https://donations-step-2020-new.uc.r.appspot.com/tag-query', {method: 'POST', body: JSON.stringify(tagArray)})
        .then(function(response) {
            return response.text();
        })
        .then(function(response) {
            console.log("text response: ", response);
        })
        .catch(function(err) {
            console.log(err);
        });
}

//sendQuery();

function getTagsAndUpdate() {
    fetch('https://donations-step-2020-new.uc.r.appspot.com/tag-query', {method: 'GET'})
        .then(function(response) {
            return response.json();
        })
        .then(function(tagArray) {
            console.log('pre: ', tagArray);
            tagArray.forEach(function(tag) {
                //console.log("at the top: total tags: ", numTags, " vs. seen tags: ", numTagsProcessed);
                googleTrends.autoComplete({keyword: tag.name})
                .then(function(res) {
                    const obj = JSON.parse(res);
                    const topics = obj.default.topics;
                    console.log(tag.name, ": ", topics);
                    topics.forEach(function(search) {
                        //numTopicsProcessed += 1;
                        title = search.title.toLowerCase();
                        id = search.mid;
                        if (search.type == 'Topic' && title === tag.name.toLowerCase() && id.includes('/m/')) { 
                            var today = new Date();
                            // startTime currently set to a week before current date
                            var start = new Date(today - (7 * 24 * 60 * 60 * 1000));
                            googleTrends.interestOverTime({keyword: search.mid, startTime: start, endTime: today})
                            .then(function(res) {
                                const obj = JSON.parse(res);
                                const timeData = obj.default.timelineData;
                                var runningTotal = 0;
                                var numDataPoints = Object.keys(timeData).length;
                                timeData.forEach(function(data) {
                                    if (data.hasData == 'true') {
                                        runningTotal += data.value[0];
                                    }
                                });
                                if (numDataPoints > 0) {
                                    var avgTrendingScore = runningTotal / numDataPoints;
                                    //console.log('tag score before changing for ', tag.name, ": ", tag.trendingScore);
                                    //console.log('average for ', tag.name, ': ', avgTrendingScore);
                                    tag.trendingScore = avgTrendingScore;
                                    console.log('tag score after changing for ', tag.name, ": ", tag.trendingScore);
                                    //console.log(tagArray);
                                } else if (numDataPoints <= 0) {
                                    console.log('average: division by 0 for ', tag.name);
                                    tag.trendingScore = 0;
                                }
                                //console.log("trial" , tagArray);
                                sendPostQuery(tagArray);
                                //last point where tagArray updates are in scope
                            });
                        }
                    })
                        //console.log('tag score before changing for ', tag.name, ": ", tag.trendingScore)
                        
                })
                .catch((err) => {
                console.log('got the error', err);
                console.log('error message', err.message);
                console.log('request body',  err.requestBody);
                });
            });
            //console.log('post queries: ', tagArray);
            return tagArray;
        })
        .then(function(updatedTagArray) {
            console.log("updateTagArray: ", updatedTagArray);
        })
        .catch(function(err) {
            console.error(err);
        });
}

getTagsAndUpdate();

function getTagsAndUpdateTrial() {
    fetch('https://donations-step-2020-new.uc.r.appspot.com/tag-query', {method: 'GET'})
        .then(function(response) {
            return response.json();
        })
        .then(function(tagArray) {
            console.log('pre: ', tagArray);
            var numTags = tagArray.size;
            var numTagsProcessed = 0;
            tagArray.forEach(function(tag) {
                numTagsProcessed += 1;
                googleTrends.autoComplete({keyword: tag.name})
                .then(function(res) {
                    const obj = JSON.parse(res);
                    const topics = obj.default.topics;
                    console.log(tag.name, ": ", topics);
                    //var numTopics = topics.size;
                    //var numTopicsProcessed = 0;
                    topics.forEach(function(search) {
                        //numTopicsProcessed = 0;
                        title = search.title.toLowerCase();
                        id = search.mid;
                        var prevTrendingScore = tag.trendingScore;
                        var today = new Date();
                        // startTime currently set to a week before current date
                        var start = new Date(today - (7 * 24 * 60 * 60 * 1000));
                        googleTrends.interestOverTime({keyword: search.mid, startTime: start, endTime: today})
                        .then(function(res) {
                            const obj = JSON.parse(res);
                            const timeData = obj.default.timelineData;
                            var runningTotal = 0;
                            var numDataPoints = Object.keys(timeData).length;
                            timeData.forEach(function(data) {
                                if (data.hasData == 'true') {
                                    runningTotal += data.value[0];
                                }
                            });
                            if (numDataPoints > 0) {
                                var avgTrendingScore = runningTotal / numDataPoints;
                                //console.log('tag score before changing for ', tag.name, ": ", tag.trendingScore);
                                //console.log('average for ', tag.name, ': ', avgTrendingScore);
                                tag.trendingScore = avgTrendingScore;
                                console.log('tag score after changing for ', tag.name, ": ", tag.trendingScore);
                                //console.log(tagArray);
                            } else if (numDataPoints <= 0) {
                                console.log('average: division by 0 for ', tag.name);
                                tag.trendingScore = 0;
                            }
                            //console.log("post queries: ", tagArray);    
                                //last point where tagArray updates are in scope
                        });
                        if (!(search.type === 'Topic' && title === tag.name.toLowerCase() && id.includes('/m/'))) { 
                            tag.trendingScore = prevTrendingScore;
                                console.log("post queries: ", tagArray);    
                                    //last point where tagArray updates are in scope
                        }
                    })
                        //console.log('tag score before changing for ', tag.name, ": ", tag.trendingScore)
                        
                })
                .catch((err) => {
                console.log('got the error', err);
                console.log('error message', err.message);
                console.log('request body',  err.requestBody);
                });
            });
            //console.log('post queries: ', tagArray);
            return tagArray;
        })
        .then(function(updatedTagArray) {
            console.log("updateTagArray: ", updatedTagArray);
        })
        .catch(function(err) {
            console.error(err);
        });
}

/*
googleTrends.autoComplete({keyword: 'ACLU'})
.then(function(res) {
    const obj = JSON.parse(res);
    const topics = obj.default.topics;
    console.log(topics);
    var runningTotal = 0;
    var numDataPoints = 0;
    topics.forEach(function(search) {

        if (search.type == 'Topic') {
            console.log('topic found');
            var today = new Date();
            // startTime currently set to a week before current date
            var start = new Date(today - (7 * 24 * 60 * 60 * 1000));
            googleTrends.interestOverTime({keyword: search.mid, startTime: start, endTime: today})
            .then(function(res){
                return JSON.parse(res);
            })
            .then(function(obj) {
                const timeData = obj.default.timelineData;
                numDataPoints += Object.keys(timeData).length;
                timeData.forEach(function(data) {
                    if (data.hasData == 'true') {
                        runningTotal += data.value[0];
                    }
                });
                console.log(runningTotal);
            })
            .catch(function(err){
                console.error(err);
            });
        }
    });
    console.log(runningTotal);
    console.log(numDataPoints);
    console.log('average: ', runningTotal / numDataPoints);
})
.catch((err) => {
  console.log('got the error', err);
  console.log('error message', err.message);
  console.log('request body',  err.requestBody);
});
*/
