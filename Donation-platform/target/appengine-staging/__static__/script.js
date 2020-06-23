function getMessageUsingArrowFunctions()
{
    // Limit maximum commens shown
    fetch('/Quickstart').then(response => response.json()).then((quote) =>
    {
        makeList(quote);
    });
};
