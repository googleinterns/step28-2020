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

public final class FindTrendingCharities {

    //datastore set up
    private DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    private DbCalls db = new DbCalls(ds);

    //number of trending charities to be returned
    final int trendingNum = 7;


    final double charityNavToScale = 1.25;

    //should sum to 1
    final double userRatingWeight = 0.75;
    final double scaledCharityNavWeight = 0.25;

    //should sum to 1
    final double charityTagsWeight = 0.75;
    final double avgReviewWeight = 0.25;


    //returns the collection of top trending charities
    public Collection<Charity> query() {
        //TODO: Change to getAllCharities() when integrating db
        Collection<Charity> charities = getAllHardCodedCharities();
        for (Charity cur: charities) {
            double curScore = calcCharityTrendingScore(cur);
            cur.setTrendingScore(curScore);
        }
        ArrayList<Charity> charitiesList = new ArrayList<>(charities);
        Collections.sort(charitiesList, new SortByTrending());
        List<Charity> topTrending = charitiesList.subList(0, trendingNum);
        return topTrending;
    }

    //returns a list of all hardcoded charities
    private Collection<Charity> getAllHardCodedCharities(){
        Collection<Charity> allCharities = new ArrayList<Charity>(Arrays.asList(HardCodedCharitiesAndTags.charities));
        return allCharities;
    }

    //returns a list of all charities in the database
    private Collection<Charity> getAllCharities(){
        Collection<Charity> charities = db.getAllCharities();
        return Charities;
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
            avgReview = userRatingWeight * userRating + scaledCharityNavWeight * charityNavRating;
        } else {
            avgReview = userRating;
        }
        //TODO: Integrate db with correct method to retreive tags for one charity
        Collection<String> tags = charity.getTags();
        double charityTagsScore = getTagTrendingScore(tags);
        double charityTrendingScore = charityTagsWeight * charityTagsScore + avgReviewWeight * avgReview;
        return charityTrendingScore;
    }

    //TODO: Integrate db with correct method to retreive navRating for one charity
    private double calcCharityNavRating(Charity charity) {
        double charityNavRating = charity.getCharityNavRating();
        return charityNavRating * charityNavToScale;
    }

    private boolean hasCharityNavRating(Charity charity) {
        return true;
    }

    //return the average trending score of a collection of tags
    private double getTagTrendingScore(Collection<String> tags) {
        double sumScores = 0;
        int numTags = tags.size();
        for (String tag: tags) {
            double tagScore = HardCodedCharitiesAndTags.tagScores.get(tag);
            //TODO: Integrate db with correct method to retreive trending score of specific tag
            //double tagScore = tag.getTrendingScore();
            
            //TODO: Use GoogleTrends API to update tag trending score
            //tagScore = use Google Trend API to get trending score
            sumScores += tagScore;
        }
        //TODO: Update db with new trendingScore for charity

        return (sumScores / numTags);
    }
}
