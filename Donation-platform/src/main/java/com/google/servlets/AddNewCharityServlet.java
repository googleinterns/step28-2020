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

/* Servlet that returns tag names to be added to charity and
*  sends add charity form into database.
*/
@WebServlet("/addNewCharity")
public class AddNewCharityServlet extends HttpServlet
{

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        DbCalls dbCalls = new DbCalls(datastore);
        try
        {
            Collection<Tag> tagCollection = dbCalls.getAllTags();
            List<String> tagNames = new ArrayList<String>();
            for (Tag tagClass : tagCollection)
            {
                tagNames.add(tagClass.getName());
            }
            Gson gson = new Gson();
            String json = gson.toJson(tagNames);
            response.setContentType("application/json;");
            response.getWriter().println(json);
        }
        catch (Exception e) {}
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        DbCalls dbCalls = new DbCalls(datastore);
        try
        {
            Collection<Tag> categoryTags = new ArrayList<Tag>();
            String[] categoryNames = request.getParameterValues("categories");
            for (String name : categoryNames)
            {
                categoryTags.add(dbCalls.getTagByName(name));
            }
            dbCalls.addCharity(request.getParameter("name"), request.getParameter("link"), request.getParameter("imgsrc"), categoryTags, request.getParameter("description"));
            response.sendRedirect("/addNewCharity.html");
        }
        catch (Exception e) {}
    }
}
