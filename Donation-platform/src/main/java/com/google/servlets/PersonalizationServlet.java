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

package com.google.sps.servlets;

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

/** Servlet that returns personalized comments data.*/
@WebServlet("/personalize")
public class PersonalizationServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    List<String> tags = new ArrayList<>();

    // Get the tag input from the checkbox form
    String blm = request.getParameter("blm");
    String education = request.getParameter("education");
    String hungerAndPoverty = request.getParameter("hunger-and-poverty");
    String environment = request.getParameter("environment");

    tags.add(blm);
    tags.add(education);
    tags.add(hungerAndPoverty);
    tags.add(environment);

    Gson gson = new Gson();
    // Write the tag input as a JSON
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(tags));
  }

}
