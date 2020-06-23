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
import java.lang.Long;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

public final class FindTrending {

    //number of trending charities to be returned
    private int topTrending = 12;

    //returns the collection of top trending charities
    public Collection<Charity> query() {
        Collection<Charity> charities = getAllCharities();
        for (Charity cur: charities) {
            double curScore = calcCharityTrendingScore(cur);
            cur.setTrendingScore(cur);
        }
        Charity[] charityScores = charities.toArray(new Charity[charities.size()]);
        Arrays.sort(charities, comp);
        List<Charity> topTrending = new ArrayList<>();
        for (int i = 0; i < trendingNum; i++) {
            topTrending.add(charities[i]);
        }
        return topTrending;
    }

    //returns the trending score of inputted charity
    private double calcCharityTrendingScore(Charity charity) {
        //double charityNavRating = GET request to db for CharityNavigatorAPI rating
        //double scaledCharityNavRating = 1.25 * charityNavRating
        //double userRating = GET request to db for charity star rating
    }
}