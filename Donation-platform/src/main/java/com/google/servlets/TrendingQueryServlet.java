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

import com.google.FindTrendingCharities;

import java.io.IOException;
import java.util.Collection;
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.model.Charity;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

/** Servlet that handles requests for the trending page* */
@WebServlet("/trending-query")
public class TrendingQueryServlet extends HttpServlet {

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Gson gson = new Gson();

    // Find the trending charities.
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    FindTrendingCharities findTrending = new FindTrendingCharities(ds);
    Collection<Charity> answer = findTrending.queryDb();

    System.out.println("printing trending charities in tag query: " + findTrending.topTrendingCharities);
    // Convert the collection of charities to JSON
    String jsonResponse = gson.toJson(findTrending.topTrendingCharities);

    // Send the JSON back as the response
    response.setContentType("application/json");
    response.getWriter().println(jsonResponse);
  }
}
