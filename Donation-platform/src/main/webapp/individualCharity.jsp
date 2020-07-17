<html>
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <link rel="stylesheet" href="individualCharity.css" />
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" />
  <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css">
  <!-- Google Fonts -->
  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700&display=swap">
  <!-- Bootstrap core CSS -->
  <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.5.0/css/bootstrap.min.css" rel="stylesheet">
  <!-- Material Design Bootstrap -->
  <link href="https://cdnjs.cloudflare.com/ajax/libs/mdbootstrap/4.19.1/css/mdb.min.css" rel="stylesheet">
  <script src="individualCharity.js"></script> 
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
<!-- Card -->
<script>updateSubmittedCharityOnPage('${charity}');</script>
<div class="center" id="charities-display"> </div>
<div class="center" ><a href="/addNewCharity.html" class="btn btn-primary" role="button">Add another Charity</a>
<a href="/browse.html" class="btn btn-primary" role="button">Go to Browse</a></div>   
  <!-- JQuery -->
  <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <!-- Bootstrap tooltips -->
  <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.4/umd/popper.min.js"></script>
  <!-- Bootstrap core JavaScript -->
  <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.5.0/js/bootstrap.min.js"></script>
  <!-- MDB core JavaScript -->
  <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/mdbootstrap/4.19.1/js/mdb.min.js"></script>
  
</body>
</html>