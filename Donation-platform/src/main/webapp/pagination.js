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
* Class handles pagination and display of charity data
* on the browse page.
*/
class Pagination
{
    constructor(charities, tagName, pageList, currentPage, numberPerPage)
    {
        this.charities = charities;
        this.tagName = tagName;
        this.pageList = pageList;
        this.currentPage = currentPage;
        this.numberPerPage = numberPerPage;
        this.numberOfPages;
    }
    /**
     * Returns the total number of pages each tag will display
     * on the page.
     */
    getNumberOfPages()
    {
        return Math.ceil(this.charities.length / this.numberPerPage);
    }
    /**
     * Updates page with new charities when number is clicked on pagination.
     */
    updatePage(page)
    {
        this.currentPage = page;
        this.determineCharitiesToDisplay();
        document.getElementById('page-' + page).setAttribute("class", "page-item active");
    }
    /**
     * Updates page with new charities when next is clicked on pagination.
     */
    nextPage()
    {
        this.currentPage += 1;
        this.determineCharitiesToDisplay();
        document.getElementById('page-' + this.currentPage).setAttribute("class", "page-item active");
    }
    /**
     * Updates page with new charities when previous is clicked on pagination.
     */
    previousPage()
    {
        this.currentPage -= 1;
        this.determineCharitiesToDisplay();
        document.getElementById('page-' + this.currentPage).setAttribute("class", "page-item active");
    }
    /**
     * Updates page with new charities when first is clicked on pagination.
     */
    firstPage()
    {
        this.currentPage = 1;
        this.determineCharitiesToDisplay();
        document.getElementById('page-' + this.currentPage).setAttribute("class", "page-item active");
    }
    /**
     * Updates page with new charities when last is clicked on pagination.
     */
    lastPage()
    {
        this.currentPage = this.numberOfPages;
        this.determineCharitiesToDisplay();
        document.getElementById('page-' + this.currentPage).setAttribute("class", "page-item active");
    }
    /**
     * Disables buttons in pagination if no more charities can be displayed.
     */
    checkThenDisableButtons()
    {
        document.getElementById("next charity-btn").disabled = this.currentPage == this.numberOfPages ? true : false;
        document.getElementById("previous charity-btn").disabled = this.currentPage == 1 ? true : false;
        document.getElementById("first charity-btn").disabled = this.currentPage == 1 ? true : false;
        document.getElementById("last charity-btn").disabled = this.currentPage == this.numberOfPages ? true : false;
    }
    /**
     * Calculates how many pages to show in pagination bar.
     */
    pageRange()
    {
        var start = this.currentPage - 2;
        var end = this.currentPage + 2;
        if (end > this.numberOfPages) {
            start -= (end - this.numberOfPages);
            end = this.numberOfPages;
        }
        if (start <= 0) {
            end += ((start - 1)*(-1))
            start = 1;
        }
        end = end > this.numberOfPages ? this.numberOfPages : end;
        return {start:start, end:end};
    }
    /**
     * Loads functions to be called for pagination.
     */
    load()
    {
        this.determineCharitiesToDisplay();
        this.numberOfPages = this.getNumberOfPages();
    }
    /**
     * Determines which charities to display.
     */
    determineCharitiesToDisplay()
    {
        var begin = ((this.currentPage - 1) * this.numberPerPage);
        var end = begin + this.numberPerPage;
        this.charities = JSON.parse(localStorage.getItem('charities'));
        this.pageList = this.charities.slice(begin, end);
        this.tagName = localStorage.getItem('tagName');
        this.populateTagPageWithCharities();
        this.checkThenDisableButtons();
    }
    populateTagPageWithCharities()
    {
        // Clears previous inner HTML data to prevent duplicate data
        // from being shown on the webpage.
        document.getElementById('collection').innerHTML = "";
        const cards = document.getElementById('collection');
        var charityDivElement;
        this.pageList.forEach(function (charity)
        {
            charityDivElement = document.createElement( "div" );
            charityDivElement.setAttribute("class", "charity-card");
            var charityDivInternalElement = document.createElement( "div" );
            charityDivInternalElement.setAttribute("class", "card charity-card-internal");
            var charityImgElement = document.createElement( "img" );
            charityImgElement.setAttribute("id", "card-img");
            charityImgElement.setAttribute("class", "card-img-top");
            charityImgElement.setAttribute("src", charity.imgSrc);
            charityImgElement.setAttribute("alt", "Card image" );
            var charityDivBodyElement = document.createElement( "div" );
            charityDivBodyElement.setAttribute("class", "card-body d-flex flex-column");
            var charityHeaderElement = document.createElement( "h4" );
            charityHeaderElement.setAttribute("class", "card-title");
            var headerText = document.createTextNode(charity.name);
            charityHeaderElement.appendChild(headerText);
            var charityTextElement = document.createElement( "p" );
            charityTextElement.setAttribute("class", "card-text")
            var descriptionText = document.createTextNode(charity.description);
            charityTextElement.appendChild(descriptionText);
            var donateButtonElement = document.createElement( "button" );
            donateButtonElement.setAttribute("class", "mt-auto btn btn-primary");
            donateButtonElement.setAttribute("id", "charity-btn");
            var donateText = document.createTextNode("Donate");
            donateButtonElement.appendChild(donateText);
            donateButtonElement.addEventListener("click", function() {
                window.open(charity.link, "_blank");
            });

            charityDivElement.appendChild(charityDivInternalElement);
            charityDivInternalElement.appendChild(charityImgElement);
            charityDivInternalElement.appendChild(charityDivBodyElement);
            charityDivBodyElement.appendChild(charityHeaderElement);
            charityDivBodyElement.appendChild(charityTextElement);
            charityDivBodyElement.appendChild(donateButtonElement);
            cards.appendChild(charityDivElement);
        }
        );
        document.getElementById('browse').innerHTML = '';
        var browseHeaderElement = document.createElement( "h1" );
        var browseHeaderText = document.createTextNode(this.tagName.charAt(0).toUpperCase() + this.tagName.slice(1));
        browseHeaderElement.appendChild(browseHeaderText);
        document.getElementById('browse').appendChild(browseHeaderElement);
        
        var backDivButtonElement = document.createElement( "div" );
        var backButtonElement = document.createElement( "button" );
        backButtonElement.setAttribute("class", "back-btn btn btn-primary");
        backButtonElement.setAttribute("id", "charity-btn");
        var backIconElement = document.createElement( "i" );
        backIconElement.setAttribute("class", "fas fa-arrow-left");
        backButtonElement.appendChild(backIconElement);
        backButtonElement.addEventListener("click", function() {
            getTagsForDisplay();
        });
        backDivButtonElement.appendChild(backButtonElement);
        backDivButtonElement.appendChild(charityDivElement);
        cards.appendChild(backDivButtonElement);
        this.addPaginationToPage();
    }
    /**
     * Populates the page with the html showing the charities.
     */
    addPaginationToPage()
    {
        document.getElementById('pagination').innerHTML = '';
        var navElement = document.createElement( "nav" );
        navElement.setAttribute("class", "center");
        navElement.setAttribute("aria-label", "Page navigation example");
        var ulElement = document.createElement( "ul" );
        ulElement.setAttribute("class", "pagination pg-blue");
        var liElement1 = document.createElement( "li" );
        liElement1.setAttribute("class", "page-item");
        var firstBtnElement = document.createElement( "input" );
        firstBtnElement.setAttribute("class", "page-link");
        firstBtnElement.setAttribute("type", "button");
        firstBtnElement.setAttribute("id", "first charity-btn");
        firstBtnElement.setAttribute("value", "First");
        firstBtnElement.addEventListener("click", this.firstPage.bind(this));
        var liElement2 = document.createElement( "li" );
        liElement2.setAttribute("class", "page-item");
        var previousBtnElement = document.createElement( "input" );
        previousBtnElement.setAttribute("class", "page-link");
        previousBtnElement.setAttribute("type", "button");
        previousBtnElement.setAttribute("id", "previous charity-btn");
        previousBtnElement.setAttribute("value", "Previous");
        previousBtnElement.addEventListener("click", this.previousPage.bind(this));
        liElement1.appendChild(firstBtnElement);
        liElement2.appendChild(previousBtnElement);
        ulElement.appendChild(liElement1);
        ulElement.appendChild(liElement2);
        // Builds list of numbers showing how many pages can be shown.
        this.numberOfPages = this.getNumberOfPages()
        var num = this.pageRange();
        for (let i = num["start"]; i < num["end"] + 1; i++)
        {
            var liElement = document.createElement( "li" );
            var aElement = document.createElement( "a" );
            var linkText = document.createTextNode(i);
            aElement.addEventListener("click", this.updatePage.bind(this, i));
            liElement.setAttribute("class", "page-item");
            liElement.setAttribute("id", "page-" + i );
            aElement.setAttribute("class", "page-link");
            aElement.appendChild(linkText);
            liElement.appendChild(aElement);
            ulElement.appendChild(liElement);
        }
        var liElement3 = document.createElement( "li" );
        liElement3.setAttribute("class", "page-item");
        var nextBtnElement = document.createElement( "input" );
        nextBtnElement.setAttribute("class", "page-link");
        nextBtnElement.setAttribute("type", "button");
        nextBtnElement.setAttribute("id", "next charity-btn");
        nextBtnElement.setAttribute("value", "Next");
        nextBtnElement.addEventListener("click", this.nextPage.bind(this));
        var liElement4 = document.createElement( "li" );
        liElement4.setAttribute("class", "page-item");
        var lastBtnElement = document.createElement( "input" );
        lastBtnElement.setAttribute("class", "page-link");
        lastBtnElement.setAttribute("type", "button");
        lastBtnElement.setAttribute("id", "last charity-btn");
        lastBtnElement.setAttribute("value", "Last");
        lastBtnElement.addEventListener("click", this.lastPage.bind(this));
        liElement3.appendChild(nextBtnElement);
        liElement4.appendChild(lastBtnElement);
        ulElement.appendChild(liElement3);
        ulElement.appendChild(liElement4);

        navElement.appendChild(ulElement);
        document.getElementById('pagination').appendChild(navElement);
    }

}
