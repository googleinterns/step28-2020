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


/* Runs loadPersonalizedCharities on page load where there are no selected tags. */
window.onload=function() {
  loadPersonalizedCharities([]);
  event.preventDefault();
}

/* (1) Fetches personalized charities from PersonalizationServlet.java and
   (2) displays them on personalized.html */
function loadPersonalizedCharities(unprocessedTags) {
  console.log('unprocessedTags: ' + unprocessedTags + " " + unprocessedTags.length);
  getPersonalizedCharitiesFromServlet(unprocessedTags).then((charities) => {
    updatePersonalizedCardsOnPage(charities);
  });
}

/* (1) Fetches personalized charities from PersonalizationServlet.java */
function getPersonalizedCharitiesFromServlet(unprocessedTags) {
  // Process the top 3 ranked tags
  const tags = processTagsFromRanking(unprocessedTags);
  console.log('processedTags: ' + tags);

  return fetch('/personalize', {
      // Send POST request to PersonalizationServlet.java with tag selections
      // as a JSON
      method: 'POST',
      headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
      },
      body: JSON.stringify(tags),
    })
    .then((response) => {
      // Receive response from PersonalizationServlet.java
      return response.json();
    })
    .then((charities) => {
      return charities;
    });
}

/* (2) Displays the list of personalized charities them on personalized.html */
function updatePersonalizedCardsOnPage(charities) {
    const pageList = new Array();
    const currentPage = 1;
    const numberPerPage = 9;
    localStorage.setItem('charities', JSON.stringify(charities))
    localStorage.setItem('tagName', "");
    let pagination = new Pagination(charities, "", pageList, currentPage, numberPerPage, "personalized");
    pagination.load();
}

/* Converts the list of sortable-compitable tag values (which require underscores) 
   into a list of the top 3 tags with their proper names.

   Example: 
   Input: ["li_health", "li_hunger", "li_education", "li_children", 
           "li_environment", "li_racial equality"];
   Output: ["health", "hunger", "education"]; */
function processTagsFromRanking(unprocessedTags) {
  var tags = [];
  if(unprocessedTags.length > 0) {
    for(var i = 0; i < unprocessedTags.length; i++) {
      tags.push(unprocessedTags[i].substring(3, unprocessedTags[i].length))
    }
    for(var i = unprocessedTags.length; i < 3; i++) {
      tags.push(null);
    }
  }
  return tags;
}

/* Creates and displays the tag element in the rank given the tag's name */
function placeSelectedTagInRank(tag) {
    const tagRank = document.getElementById('sortable');

    let innerTagElements = new Map([
      ['Animals', '<i class="fas fa-paw mr-5"></i>Animals'],
      ['Arts, Culture, Humanities', '<i class="fas fa-palette mr-5"></i>Arts, Culture, Humanities'],
      ['Education', '<i class="fas fa-school mr-5"></i>Education'],
      ['Environment', '<i class="fas fa-leaf mr-5"></i>Environment'],
      ['Health', '<i class="fas fa-heartbeat mr-5"></i>Health'],
      ['Human Services', '<i class="fas fa-people-carry mr-5"></i>Human Services'],
      ['International', '<i class="fas fa-globe-americas mr-5"></i>International'],
      ['Human and Civil Rights', '<i class="fas fa-users mr-5"></i>Human and Civil Rights'],
      ['Religion', '<i class="fas fa-place-of-worship mr-5"></i>Religion'],
      ['Community Development', '<i class="fas fa-hands-helping mr-5"></i>Community Development'],
      ['Research and Public Policy', '<i class="fas fa-gavel mr-5"></i>Research and Public Policy']
    ]);
    
    var tagElement = document.createElement('li');
    tagElement.setAttribute("id", "li_" + tag);
    tagElement.setAttribute("class", "list-group-item");
    var divStyle = document.createElement('div');
    divStyle.setAttribute("class", "md-v-line");
    tagElement.appendChild(divStyle);
    tagElement.innerHTML = innerTagElements.get(tag);
    tagRank.appendChild(tagElement);
}

/* Displays the tags of a charity in bootstrap badges. */
function displayTags(tags) {
    out = "";
    out += '<h5>'
    for (const tag of tags) {
        out += '<span class="badge badge-info">' + tag.name + '</span>';
    }
    out += '</h5>'
    return out;
}

/* Removes all tag elements from the ranking */
function clearTagRank() {
  const tagRank = document.getElementById('sortable');
  tagRank.innerHTML = "";
}

// JQUERY FUNCTIONS:
/* Creates combobox (search bar + selection menu) with autocomplete. */
$( function() {
  $.widget( "custom.combobox", {
    _create: function() {
      this.wrapper = $( "<span>" )
        .addClass( "custom-combobox" )
        .insertAfter( this.element );
 
      this.element.hide();
      this._createAutocomplete();
      this._createShowAllButton();
    },
 
    _createAutocomplete: function() {
      var selected = this.element.children( ":selected" ),
        value = selected.val() ? selected.text() : "";
 
      this.input = $( "<input>" )
        .appendTo( this.wrapper )
        .val( value )
        .attr( "title", "" )
        .addClass( "custom-combobox-input ui-widget ui-widget-content " +
                   "ui-state-default ui-corner-left" )
        .autocomplete({
          delay: 0,
          minLength: 0,
          source: $.proxy( this, "_source" )
        })
        .tooltip({
          classes: {
            "ui-tooltip": "ui-state-highlight"
          }
        });
 
      this._on( this.input, {
        autocompleteselect: function( event, ui ) {
          ui.item.option.selected = true;
          this._trigger( "select", event, {
            item: ui.item.option
          });
        },
 
        autocompletechange: "_removeIfInvalid"
      });
    },
 
    _createShowAllButton: function() {
      var input = this.input,
        wasOpen = false;
 
      $( "<a>" )
        .attr( "tabIndex", -1 )
        .attr( "title", "Show All Items" )
        .tooltip()
        .appendTo( this.wrapper )
        .button({
          icons: {
            primary: "ui-icon-triangle-1-s"
          },
          text: false
        })
        .removeClass( "ui-corner-all" )
        .addClass( "custom-combobox-toggle ui-corner-right" )
        .on( "mousedown", function() {
          wasOpen = input.autocomplete( "widget" ).is( ":visible" );
        })
        .on( "click", function() {
          input.trigger( "focus" );
 
          // Close if already visible
          if ( wasOpen ) {
            return;
          }
 
          // Pass empty string as value to search for, displaying all results
          input.autocomplete( "search", "" );
        });
      },
 
    _source: function( request, response ) {
      var matcher = new RegExp( $.ui.autocomplete.escapeRegex(request.term), "i" );
      response( this.element.children( "option" ).map(function() {
        var text = $( this ).text();
        if ( this.value && ( !request.term || matcher.test(text) ) )
          return {
            label: text,
            value: text,
            option: this
          };
      }) );
    },
 
    _removeIfInvalid: function( event, ui ) {
 
      // Selected an item, nothing to do
      if ( ui.item ) {
        return;
      }
 
      // Search for a match (case-insensitive)
      var value = this.input.val(),
        valueLowerCase = value.toLowerCase(),
        valid = false;
      this.element.children( "option" ).each(function() {
        if ( $( this ).text().toLowerCase() === valueLowerCase ) {
          this.selected = valid = true;
          return false;
        }
      });
 
      // Found a match, nothing to do
      if ( valid ) {
        return;
      }
 
      // Remove invalid value
      this.input
        .val( "" )
        .attr( "title", value + " didn't match any item" )
        .tooltip( "open" );
      this.element.val( "" );
      this._delay(function() {
        this.input.tooltip( "close" ).attr( "title", "" );
      }, 2500 );
      this.input.autocomplete( "instance" ).term = "";
    },
 
    _destroy: function() {
      this.wrapper.remove();
      this.element.show();
    }
  });

  $( "#combobox" ).combobox();
} );

/* Handles adding a new tag to the rank and prevents incorrect selections. */
$(function() {
  $('#enter').click(function() {
    
    var tagOrder = $("#sortable").sortable('toArray');
    var newTag = $("#combobox").val();

    // If there are are fewer than 3 tags in tagOrder and newTag has not 
    // already been added to the rank, then add the newTag to the rank.
    if(tagOrder.length < 3 && !tagOrder.includes("li_" + newTag)) {
      placeSelectedTagInRank(newTag);
    
    // If there are already 3 tags in the rank, then alert the user that 
    // they cannot add another tag.
    } else if(tagOrder.length >= 3) {
      swal("You cannot add more than 3 causes to your ranking.", "If you would like to change your selection, " +
      "please clear the ranking and try again.", "info");
    
    // Otherwise, if newTag is already in the rank, alert the user to select a different tag.
    } else {
      swal("You already selected " + newTag + ".", "Please select a different cause.", "info");
    }
  });
});

/* Handles clearing the tag ranking when the clear button is clicked. */
$(function() {
  $('#clear').click(function() {
    clearTagRank();
  });
});

/* Both reads in the selected tag order as an array and runs 
   loadPersonalizedCharities when the submit button is clicked. */
$(function() {
  $("#sortable").sortable({
    items: 'li'
  });

  $('#submit').click(function() {

    var tagOrder = $("#sortable").sortable('toArray');
    var tags = [];
    for(i = 0; i < tagOrder.length; i++) {
      tags.push(tagOrder[i]);
    }

    // If there are 1-3 tags in the rank, run loadPersonalizedCharities.
    if(tags.length > 0 && tags.length <= 3) {
      console.log('TAGs 1: ' + tags + " " + tags.length);
      loadPersonalizedCharities(tags);
    // Otherwise, if there are no tags in the rank, alert the user to select
    // 3 causes before re-submitting (although 1 or 2 tags is also allowed, 
    // encourage 3 so the user gets the most comprehensive results).
    } else if(tags.length == 0) {
      swal("Please select 3 causes to get personalized charities.", "", "info");
    // Otherwise, if there more than 3 tags in the rank, alert the user to select
    // no more than 3 causes before re-submitting (NOTE: the function that places 
    // tags into the ranking already prevents more than 3 tags from being added; 
    // this is a fail-safe).
    } else if(tags.length > 3) {
      swal("Please select no more than 3 tags to get personalized charities.", "", "info");
    }
  });
});