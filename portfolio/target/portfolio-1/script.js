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
 * Fetches a Message quote from the server and adds it to the DOM.
 */
function getMessage()
{
    console.log('Fetching a message.');

    // The fetch() function returns a Promise because the request is asynchronous.
    const responsePromise = fetch('/data');

    // When the request is complete, pass the response into handleResponse().
    responsePromise.then(handleResponse);
};

/**
 * Handles response by converting it to text and passing the result to
 * addQuoteToDom().
 */
function handleResponse(response)
{
    console.log('Handling the response.');

    // response.text() returns a Promise, because the response is a stream of
    // content and not a simple variable.
    const textPromise = response.text();

    // When the response is converted to text, pass the result into the
    // addQuoteToDom() function.
    textPromise.then(addQuoteToDom);
};

/** Adds a random quote to the DOM. */
function addQuoteToDom(quote)
{
    console.log('Adding quote to dom: ' + quote);

    const quoteContainer = document.getElementById('quote-container');
    quoteContainer.innerText = quote;
};

function makeList(list) {
    // Clears comment box before listing comments
    document.getElementById('quote-container').innerHTML = '';

    // Defines unordered lists with li
    let ul = document.createElement('ul');

    document.getElementById('quote-container').appendChild(ul);

    // Iterate through 2D arrayList and list out user email and comment
    list.forEach(function (item) {
        let li_1 = document.createElement('li');
        let li_2 = document.createElement('li');
        ul.appendChild(li_1);
        ul.appendChild(li_2);

        li_1.innerHTML += '<b>' + item[0] + '</b>';
        li_2.innerHTML += item[1];
    });
}

/**
 * The above code is organized to show each individual step, but we can use an
 * ES6 feature called arrow functions to shorten the code. This function
 * combines all of the above code into a single Promise chain. You can use
 * whichever syntax makes the most sense to you.
 */
function getMessageUsingArrowFunctions(quantity)
{
    console.log(quantity)
    // Limit maximum commens shown
    fetch('/data?max=' + quantity).then(response => response.json()).then((quote) =>
    {
        makeList(quote);
    });
};

/**
 * Another way to use fetch is by using the async and await keywords. This
 * allows you to use the return values directly instead of going through
 * Promises.
 */
async function getMessageUsingAsyncAwait()
{
    const response = await fetch('/data');
    const quote = await response.text();
    document.getElementById('quote-container').innerText = quote;
};

/**
 * This function fetches the user email, the login/logout url and the
 * boolean status of whether or not the user is logged in. The user email is 
 * displayed in the HTML and the login/logout url is displayed in the nav bar.
 * We use the boolean status to determine if we display the comments in the HTML.
 */
function getUser()
{
    fetch('/login').then(response => response.json()).then((quote) =>
    {   
        // quote[0] contains the users email.
        document.getElementById('user-container').innerHTML = quote[0];
        // quote[1] contains the login/logout link url.
        document.getElementById('link-container').innerHTML = quote[1];
        // quote[2] contains boolean indicating if user is logged in.
        var x = document.getElementById("comment-container");
        if (quote[2] === 'false')
        {
             x.style.display = "none";
        }
    }
    );
}



var TxtType = function (el, toRotate, period)
{
    this.toRotate = toRotate;
    this.el = el;
    this.loopNum = 0;
    this.period = parseInt(period, 10) || 2000;
    this.txt = '';
    this.tick();
    this.isDeleting = false;
};

TxtType.prototype.tick = function ()
{
    // Grabs index of current text word.
    var i = this.loopNum % this.toRotate.length;
    // This variable will store the text option passed in the HTML.
    var fullTxt = this.toRotate[i];
    // Deletes text if deleting else adds to it.
    if (this.isDeleting)
    {
        this.txt = fullTxt.substring(0, this.txt.length - 1);
    }
    else
    {
        this.txt = fullTxt.substring(0, this.txt.length + 1);
    }

    this.el.innerHTML = '<span class="wrap">' + this.txt + '</span>';

    var that = this;
    var delta = 200 - Math.random() * 100;
    // Causes deletion to move faster
    if (this.isDeleting)
    {
        delta /= 2;
    }

    if (!this.isDeleting && this.txt === fullTxt)
    {
        delta = this.period;
        this.isDeleting = true;
    }
    else if (this.isDeleting && this.txt === '')
    {
        this.isDeleting = false;
        this.loopNum++;
        delta = 500;
    }
    // Allows users to read text before function is re-run.
    setTimeout(function ()
    {
        that.tick();
    }, delta);
};

window.onload = function ()
{
    var elements = document.getElementsByClassName('typewrite');
    for (var i = 0; i < elements.length; i++)
    {
        var toRotate = elements[i].getAttribute('data-type');
        var period = elements[i].getAttribute('data-period');
        if (toRotate)
        {
            new TxtType(elements[i], JSON.parse(toRotate), period);
        }
    }
    // INJECT CSS
    var css = document.createElement("style");
    css.type = "text/css";
    css.innerHTML = ".typewrite > .wrap { border-right: 0.08em solid #fff}";
    document.body.appendChild(css);
};

// Source: https://css-tricks.com/snippets/css/typewriter-effect/
