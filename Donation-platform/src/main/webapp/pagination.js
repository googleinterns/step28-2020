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
    constructor(charities, tagName, pageList, currentPage, numberPerPage, pageName)
    {   
        // Refers to the charity data to be displayed.
        this.charities = charities;
        // Refers to the tag type of charities to be displayed on browse page.
        this.tagName = tagName;
        // Refers to block of data to display based on page.
        this.pageList = pageList;
        // Refers to current page number.
        this.currentPage = currentPage;
        // Refers to total number of charities/tags to display per page.
        this.numberPerPage = numberPerPage;
        // Refers to total number of pages the pagination algorithim holds. 
        this.numberOfPages;
        // Refers to name of page to be displayed.
        this.pageName = pageName;
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
        if (this.getNumberOfPages() > 1) {
            document.getElementById("next charity-btn").disabled = this.currentPage == this.numberOfPages ? true : false;
            document.getElementById("previous charity-btn").disabled = this.currentPage == 1 ? true : false;
            document.getElementById("first charity-btn").disabled = this.currentPage == 1 ? true : false;
            document.getElementById("last charity-btn").disabled = this.currentPage == this.numberOfPages ? true : false;
        }
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
        this.populatePage();
        this.checkThenDisableButtons();
    }
    populatePage()
    {
        if (this.pageName == "browseTags")
        {
            let card = new Card(this.pageName, this.pageList, this.tagName);
            card.populatePageWithTags();
            if (this.getNumberOfPages() > 1) {
                this.addPaginationToPage();
            }
            
        }
        else
        {
            let card =  new Card(this.pageName, this.pageList, this.tagName);
            card.populatePageWithCharities();
            if (this.getNumberOfPages() > 1) {
                this.addPaginationToPage();
            }
        }
    }
    /**
     * Populates the page with the html showing the charities.
     */
    addPaginationToPage()
    {
        document.getElementById('pagination').innerHTML = '';
        var navElement = document.createElement("nav");
        navElement.setAttribute("class", "center");
        navElement.setAttribute("aria-label", "Page navigation example");
        var ulElement = document.createElement("ul");
        ulElement.setAttribute("class", "pagination pg-blue");
       
        var firstBtnElement = this.buttonMaker("first charity-btn", "First", this.firstPage.bind(this));
        var previousBtnElement = this.buttonMaker("previous charity-btn", "Previous", this.previousPage.bind(this));
        ulElement.appendChild(firstBtnElement);
        ulElement.appendChild(previousBtnElement);
        // Builds list of numbers showing how many pages can be shown.
        this.numberOfPages = this.getNumberOfPages()
            var num = this.pageRange();
        for (let i = num["start"]; i < num["end"] + 1; i++)
        {
            var pageLink = this.pageNumberMaker(i);
            ulElement.appendChild(pageLink);
        }
        var nextBtnElement = this.buttonMaker("next charity-btn", "Next", this.nextPage.bind(this));
        var lastBtnElement = this.buttonMaker("last charity-btn", "Last", this.lastPage.bind(this));
        ulElement.appendChild(nextBtnElement);
        ulElement.appendChild(lastBtnElement);

        navElement.appendChild(ulElement);
        document.getElementById('pagination').appendChild(navElement);
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
    * Creates buttons for pagination bar.
    */
    buttonMaker(id, value, func)
    {
        var liElement = document.createElement("li");
        liElement.setAttribute("class", "page-item");
        var btnElement = document.createElement("input");
        btnElement.setAttribute("class", "page-link");
        btnElement.setAttribute("type", "button");
        btnElement.setAttribute("id", id);
        btnElement.setAttribute("value", value);
        btnElement.addEventListener("click", func);
        liElement.appendChild(btnElement);
        return liElement;
    }
    /** 
    * Creates page numbers for pagination bar.
    */
    pageNumberMaker(i)
    {
        var liElement = document.createElement("li");
        var aElement = document.createElement("a");
        var linkText = document.createTextNode(i);
        aElement.addEventListener("click", this.updatePage.bind(this, i));
        liElement.setAttribute("class", "page-item");
        liElement.setAttribute("id", "page-" + i);
        aElement.setAttribute("class", "page-link");
        aElement.appendChild(linkText);
        liElement.appendChild(aElement);
        return liElement;
    }
}
