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
      localStorage.setItem('charities', JSON.stringify(charities))
      updateContOnPage(charities);
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
 * Updates the display with bootstrap cards organized in rows and columns in a grid-like system.
 */
function updateContOnPage(charities) {
    const pageList = new Array();
    const currentPage = 1;
    const numberPerPage = 9;
    localStorage.setItem('tagName', "");
    let pagination = new Pagination(charities, "", pageList, currentPage, numberPerPage, "trending");
    pagination.load();
}