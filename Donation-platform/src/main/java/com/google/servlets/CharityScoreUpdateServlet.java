// // Copyright 2019 Google LLC
// //
// // Licensed under the Apache License, Version 2.0 (the "License");
// // you may not use this file except in compliance with the License.
// // You may obtain a copy of the License at
// //
// //     https://www.apache.org/licenses/LICENSE-2.0
// //
// // Unless required by applicable law or agreed to in writing, software
// // distributed under the License is distributed on an "AS IS" BASIS,
// // WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// // See the License for the specific language governing permissions and
// // limitations under the License.

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
import com.google.model.Tag;
import com.google.DbCalls;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Enumeration;
import java.io.*;

/** Servlet that handles requests for updating the charity trending scores */
@WebServlet("/charity-update-query")
public class CharityScoreUpdateServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        FindTrendingCharities findTrending = new FindTrendingCharities(ds);
        String outputMsg;
        List<Charity> trendingCharities = new ArrayList<Charity>();
        try {
            trendingCharities = findTrending.updateCharityScores();
            outputMsg = "updated charity";
        } catch (Exception e) {
            System.out.println("Was not able to update all charities with exception: " + e);
            outputMsg = "did not update charity";
        }

        String jsonResponse = gson.toJson(findTrending.topTrendingCharities);
        response.setContentType("application/json");
        response.getWriter().println(jsonResponse);

        // response.setContentType("text/html");
        // response.getWriter().println(outputMsg);
    }
}