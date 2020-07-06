function getUser()
{
    fetch('/userauth').then(response => response.json()).then((quote) =>
    {   
        console.log(quote)
        if (quote["loggedIn"] == "true"){
            document.getElementById('index-container').innerHTML = quote["logoutUrl"];
            document.getElementById('index-container').appendChild(document.createTextNode("Welcome: " + quote["username"]));
        } else {
            document.getElementById('index-container').innerHTML = quote["loginUrl"];
        }   
    }
    );
}

function onSignIn(googleUser) {
  var profile = googleUser.getBasicProfile();
  console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
  console.log('Name: ' + profile.getName());
  console.log('Image URL: ' + profile.getImageUrl());
  console.log('Email: ' + profile.getEmail()); // This is null if the 'email' scope is not present.
  var id_token = googleUser.getAuthResponse().id_token;
  var xhr = new XMLHttpRequest();
  xhr.open('POST', '/username');
  xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
  xhr.onload = function() {
    console.log('Signed in as: ' + xhr.responseText);
  };
  xhr.send('idtoken=' + id_token);
}

function signOut() {
    var auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(function () {
      console.log('User signed out.');
    });
  }

function onLoad() {
    gapi.load('auth2', function() {

  gapi.auth2.init({

    client_id: "223187457231-nspsjgjtsnpgjub4q12p37cdu134d6kk.apps.googleusercontent.com",

  }).then(function(){

    auth2 = gapi.auth2.getAuthInstance();
    console.log(auth2.isSignedIn.get()); //now this always returns correctly        

  });
});
}

