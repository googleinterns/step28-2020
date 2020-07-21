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

/**
 * Displays charity that was just added to the website.
 */
function updateSubmittedCharityOnPage(stringObj)
{   
    var charity = JSON.parse(stringObj);
    document.addEventListener("DOMContentLoaded", function(event) { 
    const cards = document.getElementById('charities-display');

        cards.innerHTML = '';
        var temp = '';
        cards.innerHTML += temp;
        temp = '';
        temp += 
        '<div class="card">' +
        '<img class="card-img-top" src="'+charity.imgSrc+'" alt="Card image cap">' +
        '<div class="card-body">'+
        '<h4 class="card-title"><a>'+charity.name+'</a></h4>' +
        '<p class="card-text">' + displayTags(charity.categories) + '</p>' +
        '<a href=' + charity.link + ' target=_blank class="btn btn-primary" >Donate</a>' +
        '</div>' + '</div>' ;
        cards.innerHTML += temp;

    });
}
/**
 * Displays the tags of a charity in bootstrap badges.
 */
function displayTags(tags)
{
    out = "";
    out += '<h5>'
    for (const tag of tags)
    {
        out += '<span class="center-tags badge badge-info">' + tag.name + '</span>';
    }
    out += '</h5>'
    return out;
}
