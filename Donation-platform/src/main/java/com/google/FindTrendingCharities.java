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

    //datastore set up
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    DbCalls db = new DbCalls(ds);

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
        //Only run once
        //dbSetUp();
        //TODO: Change to getAllCharities() when integrating db
        Collection<Charity> charities = getAllCharities();
        for (Charity cur: charities) {
            double curScore = calcCharityTrendingScore(cur);
            cur.setTrendingScoreCharity(curScore);
        }
        ArrayList<Charity> charitiesList = new ArrayList<>(charities);
        Collections.sort(charitiesList);
        List<Charity> topTrending = charitiesList.subList(0, trendingNum);
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
            avgReview = userRatingWeight * userRating + scaledCharityNavWeight * charityNavRating;
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
        double charityTrendingScore = charityTagsWeight * charityTagsScore + avgReviewWeight * avgReview;
        return charityTrendingScore;
    }

    //TODO: Integrate db with correct method to retreive navRating for one charity
    private double calcCharityNavRating(Charity charity) {
        //double charityNavRating;
        //return charityNavRating * charityNavToScale;
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

    //add hardcoded charities into the db
    private void dbSetUp() {
        //addTags();
        try {
            db.addTag("hunger", 25.0);
            db.addTag("education", 30.0);
            db.addTag("children", 20.0);
            db.addTag("environment", 10.0);
            db.addTag("racial equality", 50.0);
            db.addTag("health", 40.0);
        }
        catch (Exception e) {
            System.out.println("Failure adding tags: " + e);
        }

        //addCharities();
        try {
            //translate tags to keys
            Key hunger = db.getTagByName("hunger").getId();
            Key education = db.getTagByName("education").getId();
            Key children = db.getTagByName("children").getId();
            Key environment = db.getTagByName("environment").getId();
            Key racialEquality = db.getTagByName("racial equality").getId();
            Key health = db.getTagByName("health").getId();

            // //addCharities
            db.addCharity("Feeding America", "https://secure.feedingamerica.org/site/Donation2", "https://www.charities.org/sites/default/files/Feeding-America_logo-web.png", Arrays.asList(hunger), "");
            db.addCharity("Red Cross", "https://www.redcross.org/donate/donation.html/", "https://media.defense.gov/2018/Sep/17/2001966913/-1/-1/0/180917-F-ZZ000-1001.JPG", Arrays.asList(health, education), "");
            db.addCharity("St. Jude's", "https://www.stjude.org/donate/pm.html", "https://i1.wp.com/engageforgood.com/wp-content/uploads/2018/10/Untitled-design-69.png?fit=700%2C400&ssl=1", Arrays.asList(health, children), "");
            db.addCharity("Nature Conservancy", "https://support.nature.org/site/Donation2", "https://climatepolicyinitiative.org/wp-content/uploads/2018/03/tnc-nature-conservancy-logo.gif", Arrays.asList(environment), "");
            db.addCharity("YMCA", "https://www.ymca.net/be-involved", "https://www.tristatehomepage.com/wp-content/uploads/sites/92/2016/05/YMCA20logo20web_1462174680230_8365252_ver1.0-1.jpg?w=720&h=405&crop=1", Arrays.asList(children), "");
            db.addCharity("ACLU", "https://action.aclu.org/give/fund-every-fight-ahead?", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/65/New_ACLU_Logo_2017.svg/1200px-New_ACLU_Logo_2017.svg.png", Arrays.asList(racialEquality), ""); 
            db.addCharity("American Heart Association", "https://www2.heart.org/site/SPageServer?pagename=donatenow_heart", "https://upload.wikimedia.org/wikipedia/en/thumb/e/e6/American_Heart_Association_Logo.svg/1200px-American_Heart_Association_Logo.svg.png", Arrays.asList(health), "");
        }

        catch (Exception e) {
            System.out.println("Failure in adding charities: " + e);
        }

        //updateWithUserRatings();
        try {
            Charity char1 = db.getCharityByName("Feeding America");
            char1.setUserRating(4);
            db.updateCharity(char1);
            Charity char2 = db.getCharityByName("Red Cross");
            char2.setUserRating(4.5);
            db.updateCharity(char2);
            Charity char3 = db.getCharityByName("St. Jude's");
            char3.setUserRating(3);
            db.updateCharity(char3);
            Charity char4 = db.getCharityByName("Nature Conservancy");
            char4.setUserRating(5);
            db.updateCharity(char4);
            Charity char5 = db.getCharityByName("YMCA");
            char5.setUserRating(2);
            db.updateCharity(char5);
            Charity char6 = db.getCharityByName("ACLU");
            char6.setUserRating(5);
            db.updateCharity(char6);
            Charity char7 = db.getCharityByName("American Heart Association");
            char7.setUserRating(3);
            db.updateCharity(char7);
        }
        catch (Exception e) {
            System.out.println("Failure updating UserRatings: " + e);
        }
    }

    private void addTags() {
        try {
            db.addTag("hunger", 25.0);
            db.addTag("education", 30.0);
            db.addTag("children", 20.0);
            db.addTag("environment", 10.0);
            db.addTag("racial equality", 50.0);
            db.addTag("health", 40.0);
        }
        catch (Exception e) {
            System.out.println("Failure adding tags: " + e);
        }
    }

    private void addCharities() {
        try {
            //translate tags to keys
            Key hunger = db.getTagByName("hunger").getId();
            Key education = db.getTagByName("education").getId();
            Key children = db.getTagByName("children").getId();
            Key environment = db.getTagByName("environment").getId();
            Key racialEquality = db.getTagByName("racial equality").getId();
            Key health = db.getTagByName("health").getId();

            // //addCharities
            db.addCharity("Feeding America", "https://secure.feedingamerica.org/site/Donation2", "https://www.charities.org/sites/default/files/Feeding-America_logo-web.png", Arrays.asList(hunger), "");
            db.addCharity("Red Cross", "https://www.redcross.org/donate/donation.html/", "https://media.defense.gov/2018/Sep/17/2001966913/-1/-1/0/180917-F-ZZ000-1001.JPG", Arrays.asList(health, education), "");
            db.addCharity("St. Jude's", "https://www.stjude.org/donate/pm.html", "https://i1.wp.com/engageforgood.com/wp-content/uploads/2018/10/Untitled-design-69.png?fit=700%2C400&ssl=1", Arrays.asList(health, children), "");
            db.addCharity("Nature Conservancy", "https://support.nature.org/site/Donation2", "https://climatepolicyinitiative.org/wp-content/uploads/2018/03/tnc-nature-conservancy-logo.gif", Arrays.asList(environment), "");
            db.addCharity("YMCA", "https://www.ymca.net/be-involved", "https://www.tristatehomepage.com/wp-content/uploads/sites/92/2016/05/YMCA20logo20web_1462174680230_8365252_ver1.0-1.jpg?w=720&h=405&crop=1", Arrays.asList(children), "");
            db.addCharity("ACLU", "https://action.aclu.org/give/fund-every-fight-ahead?", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/65/New_ACLU_Logo_2017.svg/1200px-New_ACLU_Logo_2017.svg.png", Arrays.asList(racialEquality), ""); 
            db.addCharity("American Heart Association", "https://www2.heart.org/site/SPageServer?pagename=donatenow_heart", "https://upload.wikimedia.org/wikipedia/en/thumb/e/e6/American_Heart_Association_Logo.svg/1200px-American_Heart_Association_Logo.svg.png", Arrays.asList(health), "");
        }

        catch (Exception e) {
            System.out.println("Failure in adding charities: " + e);
        }
    }

    private void updateWithUserRatings() {
        try {
            Charity char1 = db.getCharityByName("Feeding America");
            char1.setUserRating(4);
            db.updateCharity(char1);
            Charity char2 = db.getCharityByName("Red Cross");
            char2.setUserRating(4.5);
            db.updateCharity(char2);
            Charity char3 = db.getCharityByName("St. Jude's");
            char3.setUserRating(3);
            db.updateCharity(char3);
            Charity char4 = db.getCharityByName("Nature Conservancy");
            char4.setUserRating(5);
            db.updateCharity(char4);
            Charity char5 = db.getCharityByName("YMCA");
            char5.setUserRating(2);
            db.updateCharity(char5);
            Charity char6 = db.getCharityByName("ACLU");
            char6.setUserRating(5);
            db.updateCharity(char6);
            Charity char7 = db.getCharityByName("American Heart Association");
            char7.setUserRating(3);
            db.updateCharity(char7);
        }
        catch (Exception e) {
            System.out.println("Failure updating UserRatings: " + e);
        }
    }
}
