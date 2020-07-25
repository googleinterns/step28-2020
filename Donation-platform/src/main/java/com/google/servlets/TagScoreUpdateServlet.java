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

/** Servlet that handles requests for updating the tag scores */
@WebServlet("/tag-query")
public class TagScoreUpdateServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        System.out.println("started doGet");
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        FindTrendingCharities findTrending = new FindTrendingCharities(ds);
        Collection<Tag> tags = new ArrayList<Tag>();
        try {
            tags = findTrending.getTagsDb(); 
        } catch (Exception e) {
            System.out.println("Was not able to get all tags with exception: " + e);
        }
        System.out.println(tags);

        String jsonResponse = gson.toJson(tags);
        System.out.println(tags);
        response.setContentType("application/json");
        response.getWriter().println(jsonResponse);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        DbCalls dbCalls = new DbCalls(ds);
        FindTrendingCharities findTrending = new FindTrendingCharities(ds);

        String outputmsg;

        BufferedReader reader = request.getReader();
        Gson gson = new Gson();

        Tag tag = gson.fromJson(reader, Tag.class);
        Collection<Tag> tagsToUpdate = new ArrayList<Tag>();
        tagsToUpdate.add(tag);
        try {
            findTrending.updateTagScores(tagsToUpdate);
            outputmsg = "ok";
        } catch (Exception e) {
            outputmsg = "not ok";
            System.out.println("Was not able to update all tag scores with exception: " + e);
        }

        response.setContentType("text/html");
        response.getWriter().println(outputmsg);
    
        // Collection<Tag> tagsToUpdate = new ArrayList<>();
        // Enumeration attributeNames = request.getAttributeNames();
        // while (attributeNames.hasMoreElements()) {
        //     String current = (String) attributeNames.nextElement();
        //     //tagsToUpdate.add((Tag) attributeNames.nextElement());
        //     //System.out.println(request.getAttribute(current));
        //     tagsToUpdate.add((Tag) request.getAttribute(current));
        // }

        // try {
        //     System.out.println(tagsToUpdate);
        //     findTrending.updateTagScores(tagsToUpdate);
        //     outputmsg = "ok";
        // } catch (Exception e) {
        //     outputmsg = "not ok";
        //     System.out.println("Was not able to update all tag scores with exception: " + e);
        // }

    }
}
