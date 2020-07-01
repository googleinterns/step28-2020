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

document.addEventListener("DOMContentLoaded", sendTrendingRequest());

/**
 * Sends the trending request to the server. Using the response, 
 * it lists the options reported by the server.
 */
function sendTrendingRequest() {
  queryServer().then((charities) => {
    updateCardsOnPage(charities);
  });
}

/**
 * Sends the trending request to the server and gets back the trending charities.
 */
function queryServer() {
  return fetch('/trending-query', {method: 'POST'})
      .then((response) => {
        return response.json();
      })
      .then((charities) => {
        return charities
      });
}

/**
 * Updates the UI to show the results of the query.
 */
function updateResultsOnPage(charities) {
  const resultsContainer = document.getElementById('results');

  // clear out any old results
  resultsContainer.innerHTML = '';

  // add results to the page
  //for (const name of charities) {
  charities.forEach(charity => {
    resultsContainer.innerHTML += '<li>' + charity.name + ': ' + charity.tags + '</li>';
  });
}

/**
 * Updates the display with bootstrap cards.
 */
function updateCardsOnPage(charities) {
    const cards = document.getElementById('cards');

    cards.innerHTML = '';

    charities.forEach(charity => {
        cards.innerHTML += '<div class="card border-dark mb-3">' + '<img class="card-img-top" src=' + charity.imgSrc + ' alt="Card image">' + '<div class="card text-center">' + '<h4 class="card-title">' + charity.name + '</h4>' + 
                            '<p class="card-text">' + displayTags(charity.tags) + '</p>' + '<a href=' + charity.link + ' target=_blank class="btn btn-primary" role="button">Learn More and Donate</a>';
        cards.innerHTML += '</div>' + '</div>';
    });
}

/**
 * Displays the tags of a charity in bootstrap badges.
 */
function displayTags(tags) {
    out = "";
    for (const tag of tags) {
        console.log(tag);
        out += '<h5><span class="badge badge-info">' + tag + '</span></h5>';
    }
    return out;
}