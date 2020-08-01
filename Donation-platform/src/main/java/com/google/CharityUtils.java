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

// Base class for charity utils including finding trending charities
// and updating charity trending scores
public final class CharityUtils {

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

    public static Collection<Charity> charities;
    public List<Charity> topTrendingCharities;

    //constructor to do set up
    public CharityUtils(DatastoreService ds) {
        this.ds = ds;
        db = new DbCalls(ds);
        DbSetUpUtils setUp = new DbSetUpUtils(ds, db);
        if (charities == null) {
            this.charities = getAllCharities();
            updateCharityScores();
        }
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

    //return Collection of all Tags in db
    public Collection<Tag> getTagsDb() throws Exception{
        Collection<Tag> tags= db.getAllTags();
        return tags;
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

}