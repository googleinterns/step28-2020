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
import com.google.UpdateTrendingScores;
import com.google.CharityUtils;

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

    // old get method
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        UpdateTrendingScores updateTrending = new UpdateTrendingScores(ds);
        String outputMsg;

        try {
            updateTrending.updateCharityScores();
            outputMsg = "success";
        } catch (Exception e) {
            System.out.println("Was not able to update all charities with exception: " + e);
            outputMsg = "unsuccessful computation of charity scores";
        }

        //String jsonResponse = gson.toJson(trendingCharities);
        response.setContentType("text/html");
        response.getWriter().println(outputMsg);
    }

    // new get method
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        System.out.println("started doGet");
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        CharityUtils charityUtils = new CharityUtils(ds);
        Collection<Charity> charities = new ArrayList<Charity>();
        try {
            charities = charityUtils.getAllCharities(); 
        } catch (Exception e) {
            System.out.println("Was not able to get all charities with exception: " + e);
        }
        System.out.println(charities);

        String jsonResponse = gson.toJson(charities);
        System.out.println(charities);
        response.setContentType("application/json");
        response.getWriter().println(jsonResponse);
    }

    // new post method
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        DbCalls dbCalls = new DbCalls(ds);
        UpdateTrendingScores updateTrending = new UpdateTrendingScores(ds);

        String outputMsg;

        BufferedReader reader = request.getReader();
        Gson gson = new Gson();

        Tag charityToUpdate = gson.fromJson(reader, Charity.class);
        try {
            updateTrending.updateCharityNameScore(charityToUpdate);
            outputMsg = "ok, ";
        } catch (Exception e) {
            outputMsg = "did not update charity name score, ";
            System.out.println("Was not able to update charity name score with exception: " + e);
        }

        response.setContentType("text/html");
        response.getWriter().println(outputMsg);

    }
    
}