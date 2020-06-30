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


/* (1) Fetches personalized charities from PersonalizationServlet.java and
   (2) displays them on personalized.html */
function loadPersonalizedCharities() {
  getPersonalizedCharitiesFromServlet().then((charities) => {
    updatePersonalizedList(charities);
  });
}

/* (1) Fetches personalized charities from PersonalizationServlet.java */
function getPersonalizedCharitiesFromServlet() {
  // Organize selected tags into a JSON-transferable format
  const tag_1 = document.getElementById("tag-1").value;
  const tag_2 = document.getElementById("tag-2").value;
  const tag_3 = document.getElementById("tag-3").value;
  const tags = { tag1: tag_1, tag2: tag_2, tag3: tag_3 };

  return fetch('/personalize', {
      // Send POST request to PersonalizationServlet.java with tag selections as a JSON
      method: 'POST',
      headers: {
          'Content-Type': 'application/json',
      },
      body: JSON.stringify(tags),
    })
    .then((response) => {
      // Receive response from PersonalizationServlet.java
      return response.json();
    })
    .then((charities) => {
      // Organize the charity attributes into an array of strings
      // Note: only saving each charity's name and tags for now
      const out = [];
      charities.forEach((charity) => {
          out.push(charity.name);
          out.push(charity.tags);
      });
      return out;
    });
}

/* (2) Displays the list of personalized charities them on personalized.html */
function updatePersonalizedList(charities) {
  const personalizedListElement = document.getElementById('personalized-list');

  // Remove old personalized results
  personalizedListElement.innerHTML = "";
  
  // Displays charity listings in the format: "[Charity Name] ([tag 1], [tag2], ...)"
  for(i = 0; i < charities.length - 1; i += 2) {
    var charityString = "<li><strong>" + charities[i] + "</strong>  (";
    for(j = 0; j < charities[i+1].length; j++) {
      if(j == charities[i+1].length - 1) {
        charityString += charities[i + 1][j] + ")</li>";;
      } else {
        charityString += charities[i + 1][j] + ", ";
      }
    }
    personalizedListElement.innerHTML += charityString;
  }
}

// Runs loadPersonalizedCharities() when the submit button is pressed
window.onload=function() {
  document.getElementById('submit').addEventListener("click", function(event) {
    loadPersonalizedCharities();
    event.preventDefault();
  });
}
