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

public final class FindTrendingCharities extends CharityUtils {

    // returns the collection of top trending charities pre-computer offline
    public Collection<Charity> queryDb() {
        ArrayList<Charity> charitiesList = new ArrayList<>(charities);
        sortTrendingCharities(charitiesList);
        return topTrendingCharities;
    }

    //sort charities to get topTrendingCharities
    public void sortTrendingCharities(ArrayList<Charity> charitiesList) {
        Collections.sort(charitiesList);
        if (charitiesList.size() > MAX_NUM_OF_CHARITIES_TO_RETURN) {
            topTrendingCharities = charitiesList.subList(0, MAX_NUM_OF_CHARITIES_TO_RETURN);
        } else {
            topTrendingCharities = charitiesList;
        }
    }
}
