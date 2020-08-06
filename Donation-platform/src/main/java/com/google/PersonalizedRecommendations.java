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

package com.google;

import com.google.model.Charity;
import com.google.model.Tag;
import com.google.AddCharitiesFromJSON;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Arrays;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;

/* Class that takes user-selected tags as input and finds the according best-matching charities.*/
public class PersonalizedRecommendations {

  private DbCalls db;
  private DatastoreService ds;

  // collections that hold the tags and charities from the db
  private Collection<Charity> charities;
  private Collection<Tag> tags;

  // Determine how much the charity's tags matching matters compared to how trending the charity is
  private final double TAG_MATCHING_WEIGHT = 1.0;
  private final double CHARITY_TRENDING_WEIGHT = 0.0; //TODO: make >0 once trending scores accurate

  // Increment constants to charity's tagValue for having the given user-selected tags (#1, #2, or
  // #3)
  private final double TAGSCORE_INCREMENT_FOR_HAVING_TAG_1 = 0.8;
  private final double TAGSCORE_INCREMENT_FOR_HAVING_TAG_2 = 0.5;
  private final double TAGSCORE_INCREMENT_FOR_HAVING_TAG_3 = 0.3;
  private final double TAGSCORE_INCREMENT_FOR_HAVING_TAG_1_AND_2 = 0.2;
  private final double TAGSCORE_INCREMENT_FOR_HAVING_TAG_1_AND_3 = 0.15;
  private final double TAGSCORE_INCREMENT_FOR_HAVING_TAG_2_AND_3 = 0.1;

  // Constructor to setup db
  public PersonalizedRecommendations(DatastoreService ds) {
    this.ds = ds;
    db = new DbCalls(ds);
    AddCharitiesFromJSON setup = new AddCharitiesFromJSON(ds, db);
    // TODO: REPLACE getAllCharities WITH A MORE EFFICIENT METHOD FROM DB CALLS
    // or TODO: make adding charities a separate script and delete this check altogether
    charities = getAllCharities();
    tags = getAllTags();
    // only populate database with tags if there are none there already
    if(tags.isEmpty()) {
      setup.addTags();
    }
    // only populate database with charities if there are none there already
    if(charities.isEmpty()) {
      setup.addCharities();
    }
  }

  // Returns the best-matching charities sorted according to the scores calculated by
  // getCharityScores
  public List<Charity> getBestMatches(List<String> selectedTags) {
    List<Charity> bestMatches = new ArrayList<Charity>();
    HashMap<Charity, Double> sortedCharityScores = sortByValue(getCharityScores(selectedTags));
    for (Map.Entry charity : sortedCharityScores.entrySet()) {
      bestMatches.add((Charity) charity.getKey());
    }
    return bestMatches;
  }

  // Returns mapping of charities to the scores that determine their rank on the personalized page
  // (higher score = better match)
  public HashMap<Charity, Double> getCharityScores(List<String> selectedTags) {
    HashMap<Charity, Double> charityScores = new HashMap<Charity, Double>();
    
    for (Charity charity : charities) {
      // If the charity contains any of the user-selected tags, it will appear on the personalized
      // page
      Collection<Tag> charityTagObjects = charity.getCategories();
      Collection<String> charityTagNames = new ArrayList<String>();
      for(Tag tag : charityTagObjects) {
        charityTagNames.add(tag.getName());
      }
      if (charityTagNames.contains(selectedTags.get(0))
          | charityTagNames.contains(selectedTags.get(1))
          | charityTagNames.contains(selectedTags.get(2))) {
        double tagScore = 0.0;
        // If the charity has the user's #1 ranked tag
        if (charityTagNames.contains(selectedTags.get(0))) {
          tagScore += TAGSCORE_INCREMENT_FOR_HAVING_TAG_1;
          if (charityTagNames.contains(selectedTags.get(1))) {
            tagScore += TAGSCORE_INCREMENT_FOR_HAVING_TAG_1_AND_2;
          } else if (charityTagNames.contains(selectedTags.get(2))) {
            tagScore += TAGSCORE_INCREMENT_FOR_HAVING_TAG_1_AND_3;
          }
          // If the charity has the user's #2 ranked tag
        } else if (charityTagNames.contains(selectedTags.get(1))) {
          tagScore += TAGSCORE_INCREMENT_FOR_HAVING_TAG_2;
          if (charityTagNames.contains(selectedTags.get(2))) {
            tagScore += TAGSCORE_INCREMENT_FOR_HAVING_TAG_2_AND_3;
          }
          // If the charity has the user's #3 ranked tag
        } else if (charityTagNames.contains(selectedTags.get(2))) {
          tagScore += TAGSCORE_INCREMENT_FOR_HAVING_TAG_3;
        }
        double charityScore =
            TAG_MATCHING_WEIGHT * tagScore + CHARITY_TRENDING_WEIGHT * charity.getTrendingScoreCharity();
        charityScores.put(charity, charityScore);
      }
    }
    return charityScores;
  }

  // Gets charities from the database
  private Collection<Charity> getAllCharities() {
    Collection<Charity> charities = new ArrayList<>();
    try {
      charities = db.getAllCharities();
    } catch (EntityNotFoundException e) {
      System.out.println("Charity entities not found: " + e);
      return null;
    } catch (Exception e) {
      System.out.println("Unexpected exception: " + e);
      return null;
    }
    return charities;
  }

  // Gets tags from the database
  private Collection<Tag> getAllTags() {
    Collection<Tag> tags = new ArrayList<>();
    try {
      tags = db.getAllTags();
    } catch (EntityNotFoundException e) {
      System.out.println("Tag entities not found: " + e);
      return null;
    } catch (Exception e) {
      System.out.println("Unexpected exception: " + e);
      return null;
    }
    return tags;
  }

  // Gets charities from the database that have the specified tag
  private Collection<Charity> getCollectionOfPersonalizedCharities(String tag1, String tag2, String tag3) {
    Collection<Charity> charities = new ArrayList<>();
    try {
      charities = db.getCollectionOfPersonalizedCharities(tag1, tag2, tag3);
    } catch (EntityNotFoundException e) {
      System.out.println("Charity entities not found: " + e);
      return null;
    } catch (Exception e) {
      System.out.println("Unexpected exception: " + e);
      return null;
    }
    return charities;
  }

  // Sorts HashMap in decreasing order of values
  public LinkedHashMap<Charity, Double> sortByValue(HashMap<Charity, Double> map) {
    List<Entry<Charity, Double>> list = new ArrayList<>(map.entrySet());
    Collections.sort(list, Entry.comparingByValue());
    Collections.reverse(list);
    LinkedHashMap<Charity, Double> result = new LinkedHashMap<>();
    for (Entry<Charity, Double> entry : list) {
      result.put(entry.getKey(), entry.getValue());
    }
    return result;
  }
}
