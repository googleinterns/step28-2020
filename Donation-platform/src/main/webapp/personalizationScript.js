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
  getPersonalizedCharitiesFromServlet(unprocessedTags).then((charities) => {
    updatePersonalizedCardsOnPage(charities);
  });
}

/* (1) Fetches personalized charities from PersonalizationServlet.java */
function getPersonalizedCharitiesFromServlet(unprocessedTags) {
  // Process the top 3 ranked tags
  const tags = processTagsFromRanking(unprocessedTags);

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
    const cards = document.getElementById('charities-display');

    cards.innerHTML = '';
    var cur_count = 0;
    var toAdd = '';
    var temp = '';
    charities.forEach(charity => {
        if (cur_count % 4 == 0 && cur_count != 0) {
            toAdd = '<div class="row">' + temp + '</div>';
            cards.innerHTML += toAdd;
            toAdd = '';
            temp = '';
        }
        cur_count += 1;
        temp += '<div class="col-3">' + 
                '<div class="card text-center">' + 
                '<div class="card-header">Match #' + cur_count + '</div>' + 
                '<img class="card-img-top" src=' + charity.imgSrc + ' alt="Card image">' +
                '<div class="card-body">' +
                '<h4 class="card-title">' + charity.name + '</h4>' +
                '<p class="card-text">' + displayTags(charity.categories) + '</p>' + '</div>' +
                '<div class="card-footer"><a href=' + charity.link + 
                ' target=_blank class="btn btn-primary" role="button">Donate</a></div>' +
                '</div>' + '</div>';
    });
    toAdd = '<div class="row">' + temp + '</div>';
    cards.innerHTML += toAdd;
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

    let tagElements = new Map([
      ['children', '<li id="li_children" class="list-group-item"><div class="md-v-line"></div>' +
       '<i class="fas fa-child mr-5"></i> children</li>'],
      ['education', '<li id="li_education" class="list-group-item"><div class="md-v-line"></div>' +
       '<i class="fas fa-school mr-5"></i>education</li>'],
      ['environment', '<li id="li_environment" class="list-group-item"><div class="md-v-line"></div>' +
       '<i class="fas fa-leaf mr-5"></i>environment</li>'],
      ['health', '<li id="li_health" class="list-group-item"><div class="md-v-line"></div>' +
       '<i class="fas fa-heartbeat mr-5"></i> health</li>'],
      ['hunger', '<li id="li_hunger" class="list-group-item"><div class="md-v-line"></div>' +
       '<i class="fas fa-utensils mr-5"></i> hunger</li>'],
      ['racial equality', '<li id="li_racial equality" class="list-group-item"><div class="md-v-line">' +
       '</div><i class="fas fa-users mr-5"></i>racial equality</li>']
    ]);

    tagRank.innerHTML += tagElements.get(tag);
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