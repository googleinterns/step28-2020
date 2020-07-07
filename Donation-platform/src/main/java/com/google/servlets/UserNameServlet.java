package com.google.servlets;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.util.Collection;
import java.util.Collections;
import java.io.IOException;
import com.google.model.Charity;
import com.google.model.Tag;
import com.google.model.Users; 
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.api.client.auth.openidconnect.IdToken;
import com.google.api.client.auth.openidconnect.IdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.security.GeneralSecurityException;


@WebServlet("/username")
public class UserNameServlet extends HttpServlet {

  // @Override
  // public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  //   HashMap<String, String> loginInfo = new HashMap<String, String>();
  //   response.setContentType("application/json");

  //   // If user is not logged in, show a login form (could also redirect to a login page)
  //   if (!userService.isUserLoggedIn()) {
  //     String loginUrl = userService.createLoginURL("/");
  //     loginInfo.put("loginUrl", "<p>Login <a href=\"" + loginUrl + "\">here</a>.</p>");
  //     loginInfo.put("loggedIn", Boolean.toString(userService.isUserLoggedIn()));
  //     loginInfo.put("redirect", "false");
  //     Gson gson = new Gson();
  //     String json = gson.toJson(loginInfo);
  //     response.getWriter().println(json);
  //     return;
  //   }
  //   Gson gson = new Gson();
  //   String json = gson.toJson(loginInfo);
  //   response.getWriter().println(json);
  // }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String CLIENT_ID = "223187457231-nspsjgjtsnpgjub4q12p37cdu134d6kk.apps.googleusercontent.com";
    GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
    // Specify the CLIENT_ID of the app that accesses the backend:
    .setAudience(Collections.singletonList(CLIENT_ID))
    // Or, if multiple clients access the backend:
    //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
    .build();

    String idTokenString = request.getParameter("idtoken");
    try {
    GoogleIdToken idToken = verifier.verify(idTokenString);
    if (idToken != null) {
      Payload payload = idToken.getPayload();

      // Print user identifier
      String userId = payload.getSubject();
      System.out.println("User ID: " + userId);

      // Get profile information from payload
      String email = payload.getEmail();
      boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
      String name = (String) payload.get("name");
      String pictureUrl = (String) payload.get("picture");
      String locale = (String) payload.get("locale");
      String familyName = (String) payload.get("family_name");
      String givenName = (String) payload.get("given_name");
      System.out.println("email: " + email);
      System.out.println("emailVerified: " + emailVerified);
      System.out.println("name: " + name);
      // Use or store profile information
      // ..
      // DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      // if (datastore.get(userId) == null) {
      //   Entity entity = new Entity("Users", id);
      //   entity.setProperty("id", id);
      //   entity.setProperty("userName", userName);
      //   entity.setProperty("email", email);
      //   // The put() function automatically inserts new data or updates existing data based on ID
      //   datastore.put(entity);
      // }


    } else {
      System.out.println("Invalid ID token.");
    }
  }catch  (GeneralSecurityException e) {
            System.out.println("security error");
    }
    
  }

  /**
   * Returns the userName of the user with id, or empty String if the user has not set a nickname.
   */
  private String getUserUsername(String id) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query =
        new Query("Users")
            .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    if (entity == null) {
      return "";
    }
    String username = (String) entity.getProperty("username");
    return username;
  }
}
