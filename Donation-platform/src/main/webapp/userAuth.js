function getUser()
{
    fetch('/userauth').then(response => response.json()).then((quote) =>
    {   
        console.log(quote)
        if (quote["redirect"] == "true"){
            location.href="/username"
        }
        if (quote["loggedIn"] == "true"){
            document.getElementById('index-container').innerHTML = quote["logoutUrl"];
            document.getElementById('index-container').appendChild(document.createTextNode("Welcome: " + quote["username"]));
        } else {
            document.getElementById('index-container').innerHTML = quote["loginUrl"];
        }   
    }
    );
}