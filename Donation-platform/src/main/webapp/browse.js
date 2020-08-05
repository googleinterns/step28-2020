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

document.addEventListener("DOMContentLoaded", getTagsForDisplay());
/**
 * Gets tags for display on the browse page from the Java servlet.
 */
function getTagsForDisplay()
{
    localStorage.setItem('tagName', "");
    fetch('/browseCharities').then(response => response.json()).then((tagCollection) =>
    {
        localStorage.setItem('charities', JSON.stringify(tagCollection));
        populateBrowsePage(tagCollection);
    }
    );
}
/**
 * Gets charities for display on the browse page from the Java servlet.
 */
function getCharitiesForDisplay(tagName)
{
    localStorage.setItem('tagName', tagName);
    fetch('/browseCharities?tagName=' + tagName).then(response => response.json()).then((charities) =>
    {
        localStorage.setItem('charities', JSON.stringify(charities));
        updatePageWithCharities(tagName, charities);
    }
    );
}
/**
 * Populate browse page with category cards.
 */
function populateBrowsePage(tagCollection)
{
    const pageList = new Array();
    const currentPage = 1;
    const numberPerPage = 9;
    let pagination = new Pagination(tagCollection, "", pageList, currentPage, numberPerPage, "browseTags");
    pagination.load();
}
/**
 * Updates browse page with thje charities in the selected category card.
 */
function updatePageWithCharities(tagName, charities)
{
    const pageList = new Array();
    const currentPage = 1;
    const numberPerPage = 9;
    let pagination = new Pagination(charities, tagName, pageList, currentPage, numberPerPage, "browse");
    pagination.load();
}
