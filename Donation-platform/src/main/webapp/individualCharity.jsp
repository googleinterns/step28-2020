<html>
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <link rel="stylesheet" href="browse.css" />
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" />
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
  <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
  <title>Echoing HTML Request Parameters</title>
</head>
<body>
  <!-- NAVIGATION BAR -->
  <nav class="navbar navbar-expand-sm bg-dark navbar-dark fixed-top">
    <a class="navbar-brand" href="index.html">Charities4U</a>
    <ul class="navbar-nav">
      <li class="nav-item">
        <a class="nav-link" href="trending.html">Trending Charities</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="addNewCharity.html">Add Charities</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="browse.html">Browse</a>
      </li>
    </ul>
  </nav>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>${charity}</h1>
<c:if test="${not empty message}">
    <h1>${charity}</h1>
</c:if>
  
<script src="https://code.jquery.com/jquery-3.1.1.min.js" integrity="sha256-hVVnYaiADRTO2PzUGmuLJr8BLUSjGIZsDYGmIJLv2b8=" crossorigin="anonymous"></script> 
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
</body>
</html>