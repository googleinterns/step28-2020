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

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import com.google.model.Charity;
import com.google.model.Tag;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.PreparedQuery.TooManyResultsException;

public final class FindTrendingCharities {

  private DbCalls db;
  private DatastoreService ds;

  // number of trending charities to be returned
  final int MAX_NUM_OF_CHARITIES_TO_RETURN = 7;

  final double CHARITY_NAV_SCALE_FACTOR = 1.25;

  // should sum to 1
  final double USER_RATING_WEIGHT = 0.75;
  final double CHARITY_NAV_WEIGHT = 0.25;

  // should sum to 1
  final double TAGS_SCORE_WEIGHT = 0.75;
  final double AVG_REVIEW_WEIGHT = 0.25;

  // collections that hold the tags and charities from the db
  private Collection<Charity> charities;

  //constructor to do set up
  public FindTrendingCharities(DatastoreService ds) {
    //ds = DatastoreServiceFactory.getDatastoreService();
    this.ds = ds;
    db = new DbCalls(ds);
    charities = getAllCharities();
  }

  // returns the collection of top trending charities
  public Collection<Charity> queryDb() {
    for (Charity charity : charities) {
      double charityScore = calcCharityTrendingScore(charity);
      charity.setTrendingScoreCharity(charityScore);
      try {
          db.updateCharity(charity);
      }
      catch (Exception e) {
          System.out.println("unable to update charity: " + e);
      }
    }
    ArrayList<Charity> charitiesList = new ArrayList<>(charities);
    Collections.sort(charitiesList);
    List<Charity> topTrending;
    if (charitiesList.size() > MAX_NUM_OF_CHARITIES_TO_RETURN) {
      topTrending = charitiesList.subList(0, MAX_NUM_OF_CHARITIES_TO_RETURN);
    } else {
      topTrending = charitiesList;
    }
    return topTrending;
  }

  // returns a list of all charities in the database
  private Collection<Charity> getAllCharities() {
    Collection<Charity> charities = new ArrayList<>();
    try {
      charities = db.getAllCharities();
    } catch (EntityNotFoundException e) {
      System.out.println("charity entities not found: " + e);
      return null;
    } catch (Exception e) {
      System.out.println("unexpected exception: " + e);
      return null;
    }
    return charities;
  }

  // returns the trending score of inputted charity calculated
  // as a weighted average of the tagScore and the avgReview where
  // tagScore represents the average trending score of the associated tags
  // and avgReview is a weighted average of the userRating and the charityNavigatory API rating
  // Note: weights for the averages are stored as class constants
  private double calcCharityTrendingScore(Charity charity) {
    boolean hasCharityNavRating = hasCharityNavRating(charity);
    double charityNavRating = 0;
    if (hasCharityNavRating) {
      charityNavRating = calcCharityNavRating(charity);
    }
    double userRating = charity.getUserRating();
    double avgReview;
    if (hasCharityNavRating) {
      avgReview = USER_RATING_WEIGHT * userRating + CHARITY_NAV_WEIGHT * charityNavRating;
    } else {
      avgReview = userRating;
    }
    Collection<Tag> tags = new ArrayList<>();
    tags = charity.getCategories();
    double charityTagsScore = 0;
    try {
      charityTagsScore = getTagTrendingScore(tags);
    } catch (EntityNotFoundException e) {
      System.out.println("tag entity not found: " + e);
    } catch (TooManyResultsException e) {
      System.out.println("duplicate tags exist in db: " + e);
    } catch (Exception e) {
      System.out.println("unexpected exception: e");
    }
    double charityTrendingScore =
        TAGS_SCORE_WEIGHT * charityTagsScore + AVG_REVIEW_WEIGHT * avgReview;
    return charityTrendingScore;
  }

  // TODO: Decide whether we will be using charityNavRating and change accordingly
  private double calcCharityNavRating(Charity charity) {
    // double charityNavRating;
    // return charityNavRating * CHARITY_NAV_SCALE_FACTOR;
    return 0;
  }

  private boolean hasCharityNavRating(Charity charity) {
    return false;
  }

  // return the average trending score of a collection of tags
  private double getTagTrendingScore(Collection<Tag> tags) throws Exception {
    double sumScores = 0;
    int numTags = tags.size();
    for (Tag tag : tags) {
      double tagScore = tag.getTrendingScoreTag();

      // TODO: Use GoogleTrends API to update tag trending score

      sumScores += tagScore;
    }
    return (sumScores / numTags);
  }

  public Collection<Tag> getTagsDb() throws Exception{
    Collection<Tag> tags= db.getAllTags();
    return tags;
  }

  public void updateTagScores(Collection<Tag> tagsToUpdate) throws Exception {
    for (Tag tag: tagsToUpdate) {
      db.updateTag(tag);
    }
  }
}
