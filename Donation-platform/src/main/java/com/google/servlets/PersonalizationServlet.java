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

import com.google.Charity;
import com.google.PersonalizedRecommendations;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/* Servlet that returns personalized charities according to the user's selected causes.*/
@WebServlet("/personalize")
public class PersonalizationServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    List<String> tags = new ArrayList<>();

    // Get the user's tag input from the checkbox form
    String tag1 = request.getParameter("tag-1");
    String tag2 = request.getParameter("tag-2");
    String tag3 = request.getParameter("tag-3");
    tags.add(tag1);
    tags.add(tag2);
    tags.add(tag3);

    // Get the best-matching charities from the Recommendation System
    PersonalizedRecommendations recommendation = new PersonalizedRecommendations();
    List<Charity> bestMatches = recommendation.getBestMatches(tags);

    // Convert bestMatches list to a list of strings where we only display basic information for now
    List<String> bestMatchesNames = new ArrayList<String>();
    for(Charity charity : bestMatches) {
        bestMatchesNames.add(charity.toString());
    }

    // Display the recommended charities as a JSON sorted in order of best to worst match
    Gson gson = new Gson();
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(bestMatchesNames));
  }

}
