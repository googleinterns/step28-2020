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

package com.google.servlets;

import com.google.model.Charity;
import com.google.PersonalizedRecommendations;
import com.google.gson.Gson;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

/* Servlet that returns personalized charities according to the user's selected causes.*/
@WebServlet("/personalize")
public class PersonalizationServlet extends HttpServlet {

  private final int NUMBER_OF_TAG_SELECTION_FIELDS = 3;

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the current user's session
    HttpSession session = request.getSession(false);

    // Parse tags JSON from request into a list of tags
    List<String> tags = new ArrayList<>();
    // requestData example: ["hunger","education","children"]
    String requestData = request.getReader().lines().collect(Collectors.joining());

    // If no tags were selected, set the tags list to an empty list.
    if(requestData.equals("[]")) {
      tags = Arrays.asList();
    // Otherwise, set the tags list to the processed list of tags.
    } else {
      // For ["hunger","education","children"], removes [" at beginning and "] at end
      // and splits the remaining string according the the "," between the tag names.
      tags = Arrays.asList((requestData.substring(2, requestData.length() - 2)).split("\",\""));
    }

    // If a user session already exists and new tags have not been selected
    // (which is signified by the tags list being empty), then use the tags previously
    // selected in this session.
    if (session != null && tags.isEmpty()) {
      tags = (List<String>) session.getAttribute("selected-tags");
    // Otherwise, keep using the recently-selected tags processed from requestData.
    } else {
      // Create a user session and save the tag selection with it
      session = request.getSession();
      session.setAttribute("selected-tags", tags);
    }

    // Get the best-matching charities from the Recommendation System
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    PersonalizedRecommendations recommendation = new PersonalizedRecommendations(ds);
    List<Charity> bestMatches = recommendation.getBestMatches(tags);

    // Display the recommended charities as a JSON sorted in order of best to worst match
    Gson gson = new Gson();
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(bestMatches));
  }
}
