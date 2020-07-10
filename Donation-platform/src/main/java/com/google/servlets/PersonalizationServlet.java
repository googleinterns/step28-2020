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
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/* Servlet that returns personalized charities according to the user's selected causes.*/
@WebServlet("/personalize")
public class PersonalizationServlet extends HttpServlet {

  private final int NUMBER_OF_TAG_SELECTION_FIELDS = 3;

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the current user's session
    HttpSession session = request.getSession(false);
    
    // Parse tags JSON from request into a list of tags
    List<String> tags = new ArrayList<>();
    // requestData example: {"tag1":"hunger","tag2":"education","tag3":"children"}
    String requestData = request.getReader().lines().collect(Collectors.joining());
    // Convert requestData into a mapping of tag1, tag2,... to their respective selected tag values
    ObjectMapper mapper = new ObjectMapper();
    Map<String, String> tagMap = new HashMap<String, String>();
    try {
      tagMap = mapper.readValue(requestData, Map.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    // Iterate over the mapping and add the tag values to the tags list
    int numberOfBlankTagSelections = 0;
    for (Map.Entry<String, String> entry : tagMap.entrySet()) {
      String tag = entry.getValue();
      if(tag.length() == 0) {
        numberOfBlankTagSelections++;
      }
      tags.add(tag);
    }

    // If a user session already exists and new tags have not been selected
    // (which is signified by the number of blank selected tags equalling the
    // total number of tag selection fields), then use the tags previously 
    // selected in this session.
    if (session != null && numberOfBlankTagSelections == NUMBER_OF_TAG_SELECTION_FIELDS) {
      tags = (ArrayList<String>) session.getAttribute("selected-tags");
    // Otherwise, keep using the recently-selected tags processed from requestData.
    } else {
      // Create a user session and save the tag selection with it
      session = request.getSession();
      session.setAttribute("selected-tags", tags);
    }

    // Get the best-matching charities from the Recommendation System
    PersonalizedRecommendations recommendation = new PersonalizedRecommendations();
    List<Charity> bestMatches = recommendation.getBestMatches(tags);

    // Display the recommended charities as a JSON sorted in order of best to worst match
    Gson gson = new Gson();
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(bestMatches));
  }
}
