package com.google.servlets;

import java.util.HashMap;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;


@WebServlet("/userauth")
public class UserAuthServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HashMap<String, String> loginInfo = new HashMap<String, String>();
    response.setContentType("application/json");
    UserService userService = UserServiceFactory.getUserService();

    // If user is not logged in, show a login form (could also redirect to a login page)
    if (!userService.isUserLoggedIn()) {
      String loginUrl = userService.createLoginURL("/");
      loginInfo.put("loginUrl", "<p>Login <a href=\"" + loginUrl + "\">here</a>.</p>");
      loginInfo.put("loggedIn", Boolean.toString(userService.isUserLoggedIn()));
      loginInfo.put("redirect", "false");
      Gson gson = new Gson();
      String json = gson.toJson(loginInfo);
      response.getWriter().println(json);
      return;
    }

    // If user has not set a username, redirect to username page.
    String userName = getUserUsername(userService.getCurrentUser().getUserId());
    if (userName == null) {
      loginInfo.put("redirect", "true");
      Gson gson = new Gson();
      String json = gson.toJson(loginInfo);
      response.getWriter().println(json);
      return;
    }

    // User is logged in and has a username, so the request can proceed
    String logoutUrl = userService.createLogoutURL("/");
    loginInfo.put("logoutUrl", "<p>Logout <a href=\"" + logoutUrl + "\">here</a>.</p>");
    loginInfo.put("loggedIn", Boolean.toString(userService.isUserLoggedIn()));
    loginInfo.put("username", userName);
    loginInfo.put("redirect", "false");
    Gson gson = new Gson();
    String json = gson.toJson(loginInfo);
    response.getWriter().println(json);
  }

  /** Returns the username of the user with id, or null if the user has not set a Username. */
  private String getUserUsername(String id) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query =
        new Query("Users")
            .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    if (entity == null) {
      return null;
    }
    String userName = (String) entity.getProperty("userName");
    return userName;
  }
}