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

package com.google.sps;

import java.util.Comparator;

public final class Charity implements Comparator<Charity>{

    private final String name;
    private String description;
    private Collection<Tag> tags;
    private double userRating;
    private double charityNavRating;
    private double trendingScore;

    /** 
     * Constructor takes in details from the database about the charity
     * including name, description, tags, ratings
     */
    public Charity(String name, String description, Collection<Tag> tags, double userRating, double charityNavRating) {
        this.name = name;
        this.description = description;
        this.tags = tags;
        this.userRating = userRating;
        this.charityNavRating = charityNavRating;
    }

    public Charity(String name) {
        this.name = name;
        /*
        this.description = GET request to db based on the charity name;
        this.tags = GET request to db based on the charity name;
        this.userRating = GET request to db based on the charity name;
        this.charityNavRating = GET request to db based on the charity name;
        */
    }

    /**
     * setter for trending score
     */
    public void setTrendingScore(double score) {
        this.trendingScore = score;
    }

    /**
     * A comparator for sorting charities by their trending score in descending order.
     */
    @Override
    public int compareTo(Charity b) {
      return Double.compare(b.trendingScore, this.trendingScore);
    }



}