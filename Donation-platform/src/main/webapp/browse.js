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
    fetch('/browseCharities').then(response => response.json()).then((tagCollection) =>
    {
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
        localStorage.setItem('charities', JSON.stringify(charities))
        updatePageWithCharities(tagName, charities);
    }
    );
}
/**
 * Populate browse page with category cards.
 */
function populateBrowsePage(tagCollection)
{
    document.getElementById('pagination').innerHTML = '';
    // Clears previous inner HTML data to prevent duplicate data
    // from being shown on the webpage.
    document.getElementById('collection').innerHTML = "";
    const cards = document.getElementById('collection');
    var tagDivElement;
    Object.keys(tagCollection.sort(compare)).forEach(function (tag)
    {
        tagDivElement = document.createElement( "div" );
        tagDivElement.setAttribute("class", "charity-card");
        var tagDivInternalElement = document.createElement( "div" );
        tagDivInternalElement.setAttribute("class", "card charity-card-internal");
        var tagImgElement = document.createElement( "img" );
        tagImgElement.setAttribute("id", "card-img");
        tagImgElement.setAttribute("class", "card-img-top");
        tagImgElement.setAttribute("src", tagCollection[tag].imgSrc);
        tagImgElement.setAttribute("alt", "Card image" );
        var tagDivBodyElement = document.createElement( "div" );
        tagDivBodyElement.setAttribute("class", "card-body d-flex flex-column");
        var tagHeaderElement = document.createElement( "h4" );
        tagHeaderElement.setAttribute("class", "card-title");
        var headerText = document.createTextNode(tagCollection[tag].name.charAt(0).toUpperCase() + tagCollection[tag].name.slice(1));
        tagHeaderElement.appendChild(headerText);
        var seeTagButtonElement = document.createElement( "button" );
        seeTagButtonElement.setAttribute("class", "mt-auto btn btn-primary");
        seeTagButtonElement.setAttribute("id", "charity-btn");
        var seeTagText = document.createTextNode("See Charities");
        seeTagButtonElement.appendChild(seeTagText);
        seeTagButtonElement.addEventListener("click", function() {
            getCharitiesForDisplay(tagCollection[tag].name);
        });
        tagDivElement.appendChild(tagDivInternalElement);
        tagDivInternalElement.appendChild(tagImgElement);
        tagDivInternalElement.appendChild(tagDivBodyElement);
        tagDivBodyElement.appendChild(tagHeaderElement);
        tagDivBodyElement.appendChild(seeTagButtonElement);
        cards.appendChild(tagDivElement);
    });
    document.getElementById('browse').innerHTML = '';
    var browseHeaderElement = document.createElement( "h1" );
    var browseHeaderText = document.createTextNode("Browse Charities");
    browseHeaderElement.appendChild(browseHeaderText);
    document.getElementById('browse').appendChild(browseHeaderElement);
}
/**
 * Updates browse page with thje charities in the selected category card.
 */
function updatePageWithCharities(tagName, charities)
{
    const pageList = new Array();
    const currentPage = 1;
    const numberPerPage = 9;
    let pagination = new Pagination(charities, tagName, pageList, currentPage, numberPerPage);
    pagination.load();
}
/**
 * Sorts objects by name.
 */
function compare( a, b ) {
  if ( a.name < b.name ){
    return -1;
  }
  if ( a.name > b.name ){
    return 1;
  }
  return 0;
}