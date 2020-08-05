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
 * Class sets look and function of cards across website.
 */
class Card
{
    constructor(pageName, pageList, tagName)
    {   
        // Refers to name of the page trying to be displayed.
        this.pageName = pageName;
        // Refers to the list of data that will be displayed on the page.
        this.pageList = pageList;
        // Refers to tag of charities to be displayed. Empty if not on browse page.
        this.tagName = tagName;
    }
    /**
    * Populates page with charities in the databsed. There are 
    * decisions the function makes based on the page being displayed.
    */
    populatePageWithCharities()
    {
        // Clears previous inner HTML data to prevent duplicate data
        // from being shown on the webpage.
        document.getElementById('collection').innerHTML = "";
        const cards = document.getElementById('collection');
        var charityDivElement;
        var cur_count = 0;
        this.pageList.forEach(function (charity)
        {
            cur_count++;
            charityDivElement = this.divMaker("charity-card");
            var charityDivInternalElement = this.divMaker("card charity-card-internal");
            var charityImgElement = this.imgMaker("card-img", "card-img-top", charity.imgSrc, "Card image");
            var charityDivBodyElement = this.divMaker("card-body d-flex flex-column");
            var charityHeaderElement = this.headerMaker("h4", "card-title", charity.name);
            var donateButtonElement = this.buttonMaker("mt-auto btn btn-primary", "charity-btn", "Donate");
            donateButtonElement.addEventListener("click", function ()
            {
                window.open(charity.link, "_blank");
            }
                .bind(this));
            var charityTextElement = document.createElement("p");
            charityTextElement.setAttribute("class", "card-text")
            // Adds badges to cards if on trending or personalized page.
            if (this.pageName == "trending" || this.pageName == "personalized")
            {
                var tagHeader = document.createElement("h5");
                for (const tag of charity.categories)
                {
                    var tagSpan = this.spanMaker("badge badge-info", tag);
                    tagHeader.appendChild(tagSpan);
                }
                charityTextElement.appendChild(tagHeader);
            }
            this.truncateDescription(charityTextElement, charity);
            charityDivElement.appendChild(charityDivInternalElement);
            charityDivInternalElement.appendChild(charityImgElement);
            charityDivInternalElement.appendChild(charityDivBodyElement);
            charityDivBodyElement.appendChild(charityHeaderElement);
            charityDivBodyElement.appendChild(charityTextElement);
            charityDivBodyElement.appendChild(donateButtonElement);
            cards.appendChild(charityDivElement);
        }
            .bind(this));

        // Displays back button if user is on browse page.
        if (this.pageName == "browse")
        {
            document.getElementById('browse').innerHTML = '';
            var browseHeaderElement = this.headerMaker("h1", "", this.tagName.charAt(0).toUpperCase() + this.tagName.slice(1));
            document.getElementById('browse').appendChild(browseHeaderElement);

            var backDivButtonElement = document.createElement("div");
            var backButtonElement = this.buttonMaker("back-btn btn btn-primary", "charity-btn", "");
            backButtonElement.addEventListener("click", function ()
            {
                getTagsForDisplay();
            }
                .bind(this));
            var backIconElement = document.createElement("i");
            backIconElement.setAttribute("class", "fas fa-arrow-left");
            backButtonElement.appendChild(backIconElement);
            backDivButtonElement.appendChild(backButtonElement);
            backDivButtonElement.appendChild(charityDivElement);
            cards.appendChild(backDivButtonElement);
        }
    }
    /**
    * Populates browse page with tags in the databse. There are 
    * decisions the function makes based on the page being displayed.
    */
    populatePageWithTags()
    {
        document.getElementById('pagination').innerHTML = '';
        // Clears previous inner HTML data to prevent duplicate data
        // from being shown on the webpage.
        document.getElementById('collection').innerHTML = "";
        const cards = document.getElementById('collection');
        var tagDivElement;
        Object.keys(this.pageList.sort(this.compare)).forEach(function (tag)
        {
            tagDivElement = this.divMaker("charity-card");
            var tagDivInternalElement = this.divMaker("card charity-card-internal");
            var tagImgElement = this.imgMaker("card-img", "card-img-top", this.pageList[tag].imgSrc, "Card image");
            var tagDivBodyElement = this.divMaker("card-body d-flex flex-column");
            var tagHeaderElement = this.headerMaker("h4", "card-title", this.pageList[tag].name.charAt(0).toUpperCase() + this.pageList[tag].name.slice(1));
            var seeTagButtonElement = this.buttonMaker("mt-auto btn btn-primary", "charity-btn", "See Charities");
            seeTagButtonElement.addEventListener("click", function ()
            {
                getCharitiesForDisplay(this.pageList[tag].name);
            }
                .bind(this));

            tagDivElement.appendChild(tagDivInternalElement);
            tagDivInternalElement.appendChild(tagImgElement);
            tagDivInternalElement.appendChild(tagDivBodyElement);
            tagDivBodyElement.appendChild(tagHeaderElement);
            tagDivBodyElement.appendChild(seeTagButtonElement);
            cards.appendChild(tagDivElement);
        }
            .bind(this));
        document.getElementById('browse').innerHTML = '';
        var browseHeaderElement = document.createElement("h1");
        var browseHeaderText = document.createTextNode("Browse Charities");
        browseHeaderElement.appendChild(browseHeaderText);
        document.getElementById('browse').appendChild(browseHeaderElement);
    }
    /**
     * Handles the hiding and display of the full description on each card.
     */
    readMoreLessDescription(charityId) {
      var dots = document.getElementById(charityId+"-dots");
      var moreText = document.getElementById(charityId+"-more");
      var btnText = document.getElementById(charityId+"-expand-btn");
      if (dots.style.display === "none") {
        dots.style.display = "inline";
        btnText.innerHTML = "Read more"; 
        moreText.style.display = "none";
      } else {
        dots.style.display = "none";
        btnText.innerHTML = "Read less"; 
        moreText.style.display = "inline";
      }
    }
    /**
     * Determines whether or not to truncate the description.
     */
    truncateDescription(textElememt, charity) 
    {
    // Detects if description is longer than 100 characters before adding read more link.
    if (charity.description.length > 100) {
        var initialDescription = charity.description.substring(0,100);
        var moreDescription = charity.description.substring(100);
        var initialDescriptionText = document.createTextNode(initialDescription);
        // Adds dots to end of preview description.
        var dotsSpan = document.createElement("span");
        dotsSpan.setAttribute("id", charity.id+"-dots");
        var dotsText = document.createTextNode("...");
        dotsSpan.appendChild(dotsText);
        // Hides the rest of the description.
        var moreDescriptionSpan = document.createElement("span");
        moreDescriptionSpan.setAttribute("class", "more");
        moreDescriptionSpan.setAttribute("id", charity.id+"-more");
        var moreDescriptionText = document.createTextNode(moreDescription);
        moreDescriptionSpan.appendChild(moreDescriptionText);
        // Creates button for read more link.
        var buttonElement = this.buttonMaker("link-btn", charity.id+"-expand-btn", "Read More");
        buttonElement.addEventListener("click", function ()
        {
            this.readMoreLessDescription(charity.id);
        }
            .bind(this, charity.id));
        textElememt.appendChild(initialDescriptionText);
        textElememt.appendChild(dotsSpan);
        textElememt.appendChild(moreDescriptionSpan);
        textElememt.appendChild(buttonElement);
    } else {
        var descriptionText = document.createTextNode(charity.description);
        textElememt.appendChild(descriptionText);
    }
    }
    /**
     * Sorts objects by name.
     */
    compare(a, b)
    {
        if (a.name < b.name)
        {
            return -1;
        }
        if (a.name > b.name)
        {
            return 1;
        }
        return 0;
    }
    /**
     * Creates div elements.
     */
    divMaker(className) {
        var divElement = document.createElement("div");
        divElement.setAttribute("class", className);
        return divElement;
    }
    /**
     * Creates image elements.
     */
    imgMaker(id, className, src, alt) {
        var imgElement = document.createElement("img");
        imgElement.setAttribute("id", className);
        imgElement.setAttribute("class", className);
        imgElement.setAttribute("src", src);
        imgElement.setAttribute("alt", alt);
        return imgElement
    }
    /**
     * Creates header elements.
     */
    headerMaker(header, className, headerName) {
        var headerElement = document.createElement(header);
        headerElement.setAttribute("class", className);
        var headerText = document.createTextNode(headerName);
        headerElement.appendChild(headerText);
        return headerElement;
    }
    /**
     * Creates button elements.
     */
    buttonMaker(className, id, buttonText) {
        var buttonElement = document.createElement("button");
        buttonElement.setAttribute("class", className);
        buttonElement.setAttribute("id", id);
        if ( buttonText != ""){
            var tagText = document.createTextNode(buttonText);
            buttonElement.appendChild(tagText);
        }
        return buttonElement;
    }
    /**
     * Creates span elements.
     */
    spanMaker(className, tag) {
        var tagSpan = document.createElement("span");
        tagSpan.setAttribute("class", className);
        var tagText = document.createTextNode(tag.name);
        tagSpan.appendChild(tagText);
        return tagSpan;
    }
}
