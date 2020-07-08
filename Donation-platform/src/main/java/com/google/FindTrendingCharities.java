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
import java.util.Arrays;
import java.util.List;
import java.lang.Double;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.model.Charity;
import com.google.model.Tag;

public final class FindTrendingCharities {

    DbCalls db;

    //number of trending charities to be returned
    final int MAX_NUM_OF_CHARITIES_TO_RETURN = 7;

    final double CHARITY_NAV_SCALE_FACTOR = 1.25;

    //should sum to 1
    final double USER_RATING_WEIGHT = 0.75;
    final double CHARITY_NAV_WEIGHT = 0.25;

    //should sum to 1
    final double TAGS_SCORE_WEIGHT = 0.75;
    final double AVG_REVIEW_WEIGHT = 0.25;

    
    //returns the collection of top trending charities
    public Collection<Charity> query() {
        DbSetUpUtils setUp = new DbSetUpUtils();
        db = setUp.getDbCalls();
        //setUp.dbSetUp();                                      //only call once
        //TODO: Change to getAllCharities() when integrating db
        Collection<Charity> charities = getAllCharities();
        for (Charity cur: charities) {
            double curScore = calcCharityTrendingScore(cur);
            cur.setTrendingScoreCharity(curScore);
        }
        ArrayList<Charity> charitiesList = new ArrayList<>(charities);
        Collections.sort(charitiesList);
        List<Charity> topTrending = charitiesList;
        if (charitiesList.size() > MAX_NUM_OF_CHARITIES_TO_RETURN) {
            topTrending = charitiesList.subList(0, MAX_NUM_OF_CHARITIES_TO_RETURN);
        }
        return topTrending;
    }

    //returns a list of all hardcoded charities
    /*private Collection<Charity> getAllHardCodedCharities(){
        Collection<Charity> allCharities = new ArrayList<Charity>(Arrays.asList(HardCodedCharitiesAndTags.charities));
        return allCharities;
    }*/

    //returns a list of all charities in the database
    private Collection<Charity> getAllCharities(){
        Collection<Charity> charities = new ArrayList<>();
        try {
           charities = db.getAllCharities();
        }
        catch (Exception e) {
            System.out.println("Failure in retrieving charities: " + e);
        }
        return charities;
    }


    // returns the trending score of inputted charity calculated 
    // as aweighted average of the tagScore and the avgReview where
    // tagScore represents the average trending score of the associated tags
    // and avgReview is a weighted average of the userRating and the charityNavigatory API rating
    // weights for the averages are stored as class constants 
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
        //TODO: Integrate db with correct method to retreive tags for one charity
        Collection<Tag> tags = new ArrayList<>();
        try {
           tags = db.getTagObjectsByIds(charity.getCategories());
        }
        catch (Exception e) {
            System.out.println("Exception in retreiving tags for charity: " + e);
        }
        double charityTagsScore = 0;
        try {
           charityTagsScore = getTagTrendingScore(tags);
        }
        catch (Exception e) {
            System.out.println("Exception in retreiving tag scores: " + e);
        }
        double charityTrendingScore = TAGS_SCORE_WEIGHT * charityTagsScore + AVG_REVIEW_WEIGHT * avgReview;
        return charityTrendingScore;
    }

    //TODO: Integrate db with correct method to retreive navRating for one charity
    private double calcCharityNavRating(Charity charity) {
        //double charityNavRating;
        //return charityNavRating * CHARITY_NAV_SCALE_FACTOR;
        return 0;
    }

    private boolean hasCharityNavRating(Charity charity) {
        return false;
    }

    //return the average trending score of a collection of tags
    private double getTagTrendingScore(Collection<Tag> tags) throws Exception {
        double sumScores = 0;
        int numTags = tags.size();
        for (Tag tag: tags) {
            //double tagScore = HardCodedCharitiesAndTags.tagScores.get(tag);
            //TODO: Integrate db with correct method to retreive trending score of specific tag
            double tagScore = tag.getTrendingScoreTag();
            
            //TODO: Use GoogleTrends API to update tag trending score
            //tagScore = use Google Trend API to get trending score
            sumScores += tagScore;
        }
        return (sumScores / numTags);
    }

}
