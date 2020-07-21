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
 * Gets charities for display on the browse page from the Java servlet.
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

    fetch('/browseCharities?tagName=' + tagName).then(response => response.json()).then((charities) =>
    {
        updatePageWithCharities(tagName, charities);
    }
    );
}
/**
 * Populate browse page with category cards.
 */
function populateBrowsePage(charityCollection)
{
    var tagCharityDict = tagToCharityDictMapper(charityCollection);
    var orderedTagCharityDict = sortDict(tagCharityDict);
    var tagImageDict = tagToImageSourceMapper(charityCollection);
    // Clears previous inner HTML data to prevent duplicate data
    // from being shown on the webpage.
    document.getElementById('collection').innerHTML = '';
    const cards = document.getElementById('collection');
    cards.innerHTML = '';
    var cur_count = 0;
    var toAdd = '';
    var temp = '';
    Object.keys(orderedTagCharityDict).forEach(function (tag)
    {
        if (cur_count != 0)
        {
            toAdd = '<div >' + temp + '</div>';
            cards.innerHTML += toAdd;
            toAdd = '';
            temp = '';
        }
        cur_count += 1;
        temp += '<div>' +
        '<div class="card h-100">' +
        '<img id="card-img" class="card-img-top" src="' + tagImageDict[tag] + '" alt="Card image cap">' +
        '<div class="card-body">' +
        '<h4 class="card-title"><a>' + tag.charAt(0).toUpperCase() + tag.slice(1) + '</a></h4>' +
        '<button id="charity-btn" class="btn btn-primary" onclick="getCharitiesForDisplay(\'' + tag + '\')">See Charities</button>' +
        '</div>' + '</div>';
    }
    );
    document.getElementById('browse').innerHTML = '';
    document.getElementById('browse').innerHTML = '<h1>Browse Charities</h1>';
    toAdd = '<div>' + temp + '</div>';
    cards.innerHTML += toAdd;
}
/**
 * Updates browse page with thje charities in the selected category card.
 */
function updatePageWithCharities(tagName, charities)
{
    // Clears previous inner HTML data to prevent duplicate data
    // from being shown on the webpage.
    document.getElementById('collection').innerHTML = '';
    const cards = document.getElementById('collection');
    cards.innerHTML = '';
    var cur_count = 0;
    var toAdd = '';
    var temp = '';
    charities.forEach(function (charity)
    {
        if (cur_count != 0)
        {
            toAdd = '<div >' + temp + '</div>';
            cards.innerHTML += toAdd;
            toAdd = '';
            temp = '';
        }
        cur_count += 1;
        temp += '<div>' +
        '<div class="card h-100">' +
        '<img id="card-img" class="card-img-top" src="' + charity.imgSrc + '" alt="Card image cap">' +
        '<div class="card-body">' +
        '<h4 class="card-title"><a>' + charity.name + '</a></h4>' +
        '<p class="card-text">' + charity.description + '</p>' +
        '<button id="charity-btn" class="btn btn-primary" onclick="location.href=\'' + charity.link + '\'">Donate</button>' +
        '</div>' + '</div>';
    }
    );
    document.getElementById('browse').innerHTML = '';
    document.getElementById('browse').innerHTML = '<h1>' + tagName.charAt(0).toUpperCase() + tagName.slice(1) + '</h1>';
    toAdd = '<div><button id="charity-btn" class="back-btn btn btn-primary" onclick="getTagsForDisplay()" type="button">Back</button>' + temp + '</div>';
    cards.innerHTML += toAdd;
}
