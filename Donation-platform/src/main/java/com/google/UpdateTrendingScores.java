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

// File that includes DB updates for trends scores
// Note: Only runs as a result of the offline script
public final class UpdateTrendingScores extends CharityUtils {

    public UpdateTrendingScores(DatastoreService ds) {
        super(ds);
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

    //update all charity trending scores
    public void updateCharityScores() {
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
        for (Tag tag : tags) {
            sumScores += tag.getTrendingScoreTag();
        }
        return (sumScores / tags.size());
    }
    
    //updates one tag in db with a new trendingScore
    public void updateTagScore(Tag tagToUpdate) throws Exception {
        db.updateTag(tagToUpdate);
    }

    //updates one charity's name trending score
    public void updateCharityNameScore(Charity charityToUpdate) throws Exception {
        db.updateCharity(charityToUpdate);
        updateCharityScores(charityToUpdate);
    }

    // recalculate and update one charity's trending score
    public void updateCharityScores(Charity charityToUpdate) {
        charityToUpdate.setTrendingScoreCharity(calcCharityTrendingScore(charityToUpdate));
        try {
            db.updateCharity(charity);
        }
        catch (Exception e) {
            System.out.println("unable to update charity: " + e);
        }
    }

}