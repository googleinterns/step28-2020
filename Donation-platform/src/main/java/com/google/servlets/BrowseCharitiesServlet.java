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

import com.google.gson.Gson;
import com.google.model.Charity;
import com.google.model.Tag;
import com.google.DbCalls;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* Servlet that returns all charity objects.
*/
@WebServlet("/browseCharities")
public class BrowseCharitiesServlet extends HttpServlet
{

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        DbCalls dbCalls = new DbCalls(datastore);
        String tagName = request.getParameter("tagName");
        boolean isTagName = tagName == null || tagName.length() == 0 ? false : true;
        // Determines whether to get all charities by default or only charities that are part of the tag provided.
        if (!isTagName) {
            try
            {
                Collection<Tag> tagCollection = dbCalls.getAllTags();
                Gson gson = new Gson();
                String json = gson.toJson(tagCollection);
                response.setContentType("application/json;");
                response.getWriter().println(json);
            }
            catch (Exception e) {}
        } else {
            try
            {
                Collection<Charity> charityCollection = dbCalls.getCharitiesByTag(tagName);
                Gson gson = new Gson();
                String json = gson.toJson(charityCollection);
                response.setContentType("application/json;");
                response.getWriter().println(json);
            }
            catch (Exception e) {}
        }
    }
}