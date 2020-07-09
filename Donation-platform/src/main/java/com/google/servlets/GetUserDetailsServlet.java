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
import com.google.model.Users;
import com.google.DbCalls;
import com.google.api.client.auth.openidconnect.IdToken;
import com.google.api.client.auth.openidconnect.IdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyFactory.Builder;
import com.google.appengine.api.datastore.EntityNotFoundException;
import java.util.Collection;
import java.util.Collections;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* Servlet that returns user information when logged in.*/
@WebServlet("/userDetails")
public class GetUserDetailsServlet extends HttpServlet
{

    private static final String CLIENT_ID = "223187457231-nspsjgjtsnpgjub4q12p37cdu134d6kk.apps.googleusercontent.com";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        DbCalls dbCalls = new DbCalls(datastore);

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
        .setAudience(Collections.singletonList(CLIENT_ID))
        .build();

        try
        {
            GoogleIdToken idToken = verifier.verify(request.getParameter("idtoken"));
            if (idToken != null)
            {
                // Returns profile information of current user.
                Payload payload = idToken.getPayload();
                Entity entity;
                try
                {
                    // Reads user information from datastore.
                    entity = datastore.get(KeyFactory.createKey("Users", payload.getSubject()));
                }
                // If user not in datastore, add user to datastore.
                catch (EntityNotFoundException e)
                {
                    dbCalls.addUser(payload.getSubject(),(String) payload.get("name"), payload.getEmail(), Collections.emptyList(), Collections.emptyList());
                    entity = datastore.get(KeyFactory.createKey("Users", payload.getSubject()));
                }
                Gson gson = new Gson();
                String json = gson.toJson(dbCalls.setUsersClass(entity));
                response.setContentType("application/json;");
                response.getWriter().println(json);

            }
            else
            {
                System.out.println("Invalid ID token.");
            }
        }
        catch  (GeneralSecurityException e) {}
        catch (Exception e) {}
    }
}
