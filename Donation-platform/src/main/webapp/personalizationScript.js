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


/* (1) Fetches personalized charities from PersonalizationServlet.java and
   (2) displays them on personalized.html */
function loadPersonalizedCharities(tagOrder) {
  getPersonalizedCharitiesFromServlet(tagOrder).then((charities) => {
    updatePersonalizedCardsOnPage(charities);
  });
}

/* Converts the list of sortable-compitable tag values (which require underscores) 
   into a list of the top 3 tags with their proper names.

   Example: tagOrder = ["li_health", "li_hunger", "li_education", "li_children", 
                        "li_environment", "li_racial equality"];
            tags = ["health", "hunger", "education"]; */
function processTagsFromRanking(tagOrder) {
  var tags = [];
  if(tagOrder.length > 0) {
    for(var i = 0; i < 3; i++) {
      tags.push(tagOrder[i].substring(3, tagOrder[i].length))
    }
  }
  return tags;
}

/* (1) Fetches personalized charities from PersonalizationServlet.java */
function getPersonalizedCharitiesFromServlet(tagOrder) {
  // Process the top 3 ranked tags
  const tags = processTagsFromRanking(tagOrder);

  return fetch('/personalize', {
      // Send POST request to PersonalizationServlet.java with tag selections
      // as a JSON
      method: 'POST',
      headers: {
          'Content-Type': 'application/json',
      },
      body: JSON.stringify(tags),
    })
    .then((response) => {
      // Receive response from PersonalizationServlet.java
      return response.json();
    })
    .then((charities) => {
      return charities;
    });
}

/* (2) Displays the list of personalized charities them on personalized.html */
function updatePersonalizedCardsOnPage(charities) {
    const cards = document.getElementById('charities-display');

    cards.innerHTML = '';
    var cur_count = 0;
    var toAdd = '';
    var temp = '';
    charities.forEach(charity => {
        if (cur_count % 4 == 0 && cur_count != 0) {
            toAdd = '<div class="row">' + temp + '</div>';
            cards.innerHTML += toAdd;
            toAdd = '';
            temp = '';
        }
        cur_count += 1;
        temp += '<div class="col-3">' + 
                '<div class="card text-center">' + 
                '<div class="card-header">Match #' + cur_count + '</div>' + 
                '<img class="card-img-top" src=' + charity.imgSrc + ' alt="Card image">' +
                '<div class="card-body">' +
                '<h4 class="card-title">' + charity.name + '</h4>' +
                '<p class="card-text">' + displayTags(charity.categories) + '</p>' + '</div>' +
                '<div class="card-footer"><a href=' + charity.link + 
                ' target=_blank class="btn btn-primary" role="button">Donate</a></div>' +
                '</div>' + '</div>';
    });
    toAdd = '<div class="row">' + temp + '</div>';
    cards.innerHTML += toAdd;
}

/* Displays the tags of a charity in bootstrap badges. */
function displayTags(tags) {
    out = "";
    out += '<h5>'
    for (const tag of tags) {
        out += '<span class="badge badge-info">' + tag.name + '</span>';
    }
    out += '</h5>'
    return out;
}

/* Both reads in the selected tag order as an array and runs 
   loadPersonalizedCharities when the submit button is clicked. */
$(function() {
  $("#sortable").sortable();
  $('#submit').click(function() {
    var tagOrder = $("#sortable").sortable('toArray');
    console.log(tagOrder);
    loadPersonalizedCharities(tagOrder);
  });
});

/* Runs loadPersonalizedCharities on page load where there are no selected tags. */
window.onload=function() {
  loadPersonalizedCharities([]);
  event.preventDefault();
}