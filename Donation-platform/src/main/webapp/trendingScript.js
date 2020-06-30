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
    updateResultsOnPage(charities);
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
        // Convert the charity details from a json representation to strings.
        // Currently extracts the name and the corresponding tags of the charities for display.
        // TODO: update this method to extract all necessary details of the charity to update the display.
    
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

  //resultsContainer.innerHTML += '<li>' + 'name' + '</li>';
  // add results to the page
  //for (const name of charities) {
  charities.forEach(charity => {
    resultsContainer.innerHTML += '<li>' + charity.name + ': ' + charity.tags + '</li>';
  });
}

/**
 * Updates the display with bootstrap cards. (IN PROGRESS)
 */
function updateCardsOnPage(charities) {
    const cards = document.getElementById('cards');

    cards.innerHTML = '';

    for (index = 0; index < charities.length - 1; index += 2) {
        cards.innerHTML += '<div class="card bg-primary">' + '<div class="card-body text-center">' + '<p class="card-text">' + charities[index] + '</p>';
        cards.innerHTML += '</div>' + '</div>';
    }
}