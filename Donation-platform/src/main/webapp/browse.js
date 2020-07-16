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

document.addEventListener("DOMContentLoaded", getCharitiesForDisplay());

/**
 * Creates dictionary with tag name as key and array of charity objects as values.
 */
function tagToCharityDictConverter(charityCollection)
{
    var tagCharityDict = {};
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
    return orderedDict
}

// Populates browse page with charities and categories.
function populateBrowsePage(charityCollection)
{
    var tagCharityDict = tagToCharityDictConverter(charityCollection);
    var orderedTagCharityDict = sortDict(tagCharityDict);
    // Clears previous inner HTML data to prevent duplicate data
    // from being shown on the webpage.
    document.getElementById('collection').innerHTML = '';
    // Injects charity and tag data into the webpage.
    let ul = document.createElement('ul');
    ul.setAttribute("class", "collapsible");
    document.getElementById('collection').appendChild(ul);
    $(document).ready(function ()
    {
        $('.collapsible').collapsible();
    }
    );
    for (let tagName in orderedTagCharityDict)
    {
        let li = document.createElement('li');
        ul.appendChild(li);
        li.innerHTML += '<div class="collapsible-header"><i class="material-icons">menu</i>' + tagName.charAt(0).toUpperCase() + tagName.slice(1) + '</div>';
        var arr = orderedTagCharityDict[tagName].sort();
        for (var i = 0, len = arr.length; i < len; i++)
        {
            li.innerHTML += '<div class="collapsible-body"><span><a target="_blank" rel="noopener noreferrer" href="' + arr[i]["link"] + '"class="secondary-content"><i class="material-icons">send</i></a>' + arr[i]["name"] + '</span></div>'
        }
    };
}

/**
 * Gets charities for display on the browse page from the Java servlet.
 */
function getCharitiesForDisplay()
{
    fetch('/browseCharities').then(response => response.json()).then((charityCollection) =>
    {
        populateBrowsePage(charityCollection);
    }
    );
}
