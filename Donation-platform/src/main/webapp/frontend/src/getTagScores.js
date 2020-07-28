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

//sample method to demonstrate GET request
function sendQuery() {
    fetch('https://donations-step-2020-new.uc.r.appspot.com/tag-query', {method: 'GET'})
    //fetch('https://8080-5a8baf4b-def3-4b41-83c1-47f3b4af6cdc.us-west1.cloudshell.dev/tag-query', {method: 'GET'})
        .then(function(response) {
            console.log(response);
            return response.json();
        })
        .then(function(response) {
            console.log(response);
        })
        .catch(function(err) {
            console.log("error found", err);
        });
}

//method to send POST request with tagToUpdate in body
function sendPostQuery(tagToUpdate) {
    console.log("in other func: ", tagToUpdate);
    console.log("stringified version: ", JSON.stringify(tagToUpdate));
    fetch('https://donations-step-2020-new.uc.r.appspot.com/tag-query', {method: 'POST', body: JSON.stringify(tagToUpdate)})
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

//complete function to retrieve tags and update their scores in db
function getTagsAndUpdate() {
    fetch('https://donations-step-2020-new.uc.r.appspot.com/tag-query', {method: 'GET'})
        .then(function(response) {
            return response.json();
        })
        .then(function(tagArray) {
            console.log('pre: ', tagArray);
            tagArray.forEach(function(tag) {
                googleTrends.autoComplete({keyword: tag.name})
                .then(function(res) {
                    const obj = JSON.parse(res);
                    const topics = obj.default.topics;
                    console.log(tag.name, ": ", topics);
                    topics.forEach(function(search) {
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
                                    tag.trendingScore = avgTrendingScore;
                                    console.log('tag score after changing for ', tag.name, ": ", tag.trendingScore);
                                } else if (numDataPoints <= 0) {
                                    console.log('average: division by 0 for ', tag.name);
                                    tag.trendingScore = 0;
                                }
                                sendPostQuery(tag);
                            });
                        }
                    })       
                })
                .catch((err) => {
                console.log('got the error', err);
                console.log('error message', err.message);
                console.log('request body',  err.requestBody);
                });
            });
        })
        .then(function(updatedTagArray) {
            console.log("updateTagArray: ", updatedTagArray);
        })
        .catch(function(err) {
            console.error(err);
        });
}

//send request to update charity trending scores
function updateCharityScores() {
    fetch('https://donations-step-2020-new.uc.r.appspot.com/charity-update-query', {method: 'GET'})
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

//encompassing function to update tag scores and then update charity scores
function updateAll() {
    getTagsAndUpdate();
    updateCharityScores();
}
