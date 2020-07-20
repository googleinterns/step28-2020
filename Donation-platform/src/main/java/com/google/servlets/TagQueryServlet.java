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
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

/** Servlet that handles requests for the trending page* */
@WebServlet("/tag-query")
public class TagQueryServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("started doGet");
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        FindTrendingCharities findTrending = new FindTrendingCharities(ds);
        Collection<Tag> tags = findTrending.getTagsDb(); 
        System.out.println(tags);

        String jsonResponse = gson.toJson(tags);
        System.out.println(tags);
        response.setContentType("application/json");
        response.getWriter().println(jsonResponse);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        FindTrendingCharities findTrending = new FindTrendingCharities(ds);

        Collection<Tag> tagsToUpdate() = new ArrayList<>();
        Enumeration attributeNames = request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            tagsToUpdate.add(attributeNames.nextElement());
        }
        findTrending.updateTagScores(tagsToUpdate);
    }
}
