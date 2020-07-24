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
 * Stores tag image scource with tag name for easy access.
 */
function tagToImageSourceMapper(charityCollection)
{
    const tagImageDict = {};
    charityCollection.forEach(function (item)
    {
        item["categories"].forEach(function (tag)
        {
            if (tag["name"]in tagImageDict)
            {
                tagImageDict[tag["name"]].push(tag["imgSrc"]);
            }
            else
            {
                tagImageDict[tag["name"]] = [];
                tagImageDict[tag["name"]].push(tag["imgSrc"]);
            }
        }
        );
    }
    );
    return tagImageDict;
}
/**
 * Creates dictionary with tag name as key and array of charity objects as values.
 */
function tagToCharityDictMapper(charityCollection)
{
    const tagCharityDict = {};
    //Associates each tag name with all its charity objects.
    charityCollection.forEach(function (item)
    {
        item["categories"].forEach(function (tag)
        {
            if (tag["name"]in tagCharityDict)
            {
                tagCharityDict[tag["name"]].push(item);
            }
            else
            {
                tagCharityDict[tag["name"]] = [];
                tagCharityDict[tag["name"]].push(item);
            }
        }
        );
    }
    );
    return tagCharityDict;
}
/**
 * Sorts dictionary keys and values alphabetically.
 */
function sortDict(dict)
{
    const orderedDict = {};
    Object.keys(dict).sort().forEach(function (key)
    {
        orderedDict[key] = dict[key];
    }
    );
    return orderedDict;
}
/**
 * Gets tags for display on the browse page from the Java servlet.
 */
function getTagsForDisplay()
{
    fetch('/browseCharities').then(response => response.json()).then((charityCollection) =>
    {
        populateBrowsePage(charityCollection);
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
function populateBrowsePage(charityCollection)
{
    document.getElementById('pagination').innerHTML = '';
    var tagCharityDict = tagToCharityDictMapper(charityCollection);
    var orderedTagCharityDict = sortDict(tagCharityDict);
    var tagImageDict = tagToImageSourceMapper(charityCollection);
    // Clears previous inner HTML data to prevent duplicate data
    // from being shown on the webpage.
    document.getElementById('collection').innerHTML = "";
    const cards = document.getElementById('collection');
    var charityDivElement;
    Object.keys(orderedTagCharityDict).forEach(function (tag)
    {
        charityDivElement = document.createElement( "div" );
        charityDivElement.setAttribute("class", "charity-card");
        var charityDivInternalElement = document.createElement( "div" );
        charityDivInternalElement.setAttribute("class", "card charity-card-internal");
        var charityImgElement = document.createElement( "img" );
        charityImgElement.setAttribute("id", "card-img");
        charityImgElement.setAttribute("class", "card-img-top");
        charityImgElement.setAttribute("src", tagImageDict[tag]);
        charityImgElement.setAttribute("alt", "Card image" );
        var charityDivBodyElement = document.createElement( "div" );
        charityDivBodyElement.setAttribute("class", "card-body d-flex flex-column");
        var charityHeaderElement = document.createElement( "h4" );
        charityHeaderElement.setAttribute("class", "card-title");
        var headerText = document.createTextNode(tag.charAt(0).toUpperCase() + tag.slice(1));
        charityHeaderElement.appendChild(headerText);
        var seeCharityButtonElement = document.createElement( "button" );
        seeCharityButtonElement.setAttribute("class", "mt-auto btn btn-primary");
        seeCharityButtonElement.setAttribute("id", "charity-btn");
        var seeCharityText = document.createTextNode("See Charities");
        seeCharityButtonElement.appendChild(seeCharityText);
        seeCharityButtonElement.addEventListener("click", function() {
            getCharitiesForDisplay(tag);
        });
        charityDivElement.appendChild(charityDivInternalElement);
        charityDivInternalElement.appendChild(charityImgElement);
        charityDivInternalElement.appendChild(charityDivBodyElement);
        charityDivBodyElement.appendChild(charityHeaderElement);
        charityDivBodyElement.appendChild(seeCharityButtonElement);
        cards.appendChild(charityDivElement);
    }
    );
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