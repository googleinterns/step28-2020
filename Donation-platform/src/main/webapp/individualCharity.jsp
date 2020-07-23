<html>
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <meta name="google-signin-client_id" content="223187457231-nspsjgjtsnpgjub4q12p37cdu134d6kk.apps.googleusercontent.com">
  <script src="https://apis.google.com/js/platform.js" async defer></script>
  <script src="userAuth.js"></script>
  <link rel="stylesheet" href="userAuth.css">
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
    <!-- NAVBAR -->
    <nav class="navbar fixed-top navbar-expand-lg navbar-light scrolling-navbar peach-gradient">
      <!-- Navbar brand -->
      <a class="navbar-brand" href="/">Charities4U</a>
      <!-- Collapse button -->
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#basicExampleNav" aria-controls="basicExampleNav"
          aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
      </button>
      <!-- Collapsible content -->
      <div class="collapse navbar-collapse" id="basicExampleNav">
          <!-- Links -->
          <ul class="navbar-nav mr-auto">
              <li class="nav-item ">
                  <a class="nav-link" href="/trending.html">Trending Charities
                  </a>
              </li>
              <li class="nav-item active">
                  <a class="nav-link" href="/addNewCharity.html">Add a Charity</a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="/browse.html">Browse</a>
            </li>
          </ul>
          <ul class="navbar-nav ml-auto nav-flex-icons">
              <!-- User Profile Dropdown -->
              <li class="nav-item dropdown">
                  <div id="index-container"></div>
                  <a class="nav-link dropdown-toggle" id="navbarDropdownMenuLink-333" data-toggle="dropdown"
                    aria-haspopup="true" aria-expanded="false">
                    <i class="fas fa-user"></i>
                  </a>
                  <div class="dropdown-menu dropdown-menu-right dropdown-default"
                    aria-labelledby="navbarDropdownMenuLink-333">
                    <div class="g-signin2" data-onsuccess="fetchSignInUserInfo"></div>
                    <a class="dropdown-item" a href="#" onclick="signUserOut();">Sign out</a>
                  </div>
              </li>
          </ul>
      </div>
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