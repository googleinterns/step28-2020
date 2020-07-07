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
 * Gets the user logged in information 
 * which has been reported by the server and displays user name.
 */
function onSignIn(googleUser) {
    var id_token = googleUser.getAuthResponse().id_token;
    fetch('/username?idtoken=' + id_token).then(response => response.json()).then((object) => {
        document.getElementById('index-container').innerHTML = "Welcome: " + object["userName"];
    });
}
/**
 * Signs the user out of the app.
 */
function signOut() {
    var auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(function() {
        document.getElementById('index-container').innerHTML = "";
    });
}
