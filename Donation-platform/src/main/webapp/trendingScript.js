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
//<button id="submit" onclick="sendTrendingRequest()">Get Trending</button>
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
        // For simplicity, this method will (for now) only output the charity names to update the display.
        // TODO: update this method to extract all necessary details of the charity to update the display.
        const out = [];
        charities.forEach((charity) => {
          out.push(charity.name);
          out.push(charity.tags);
        });
        return out;
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
  for (index = 0; index < charities.length - 1; index += 2) {
    resultsContainer.innerHTML += '<li>' + charities[index] + ': ' + charities[index + 1] + '</li>';

  }
}