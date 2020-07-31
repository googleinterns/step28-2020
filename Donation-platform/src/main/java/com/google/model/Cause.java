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

package com.google.model;

import com.google.appengine.api.datastore.Key;

/** Represents a Cause : id, name, trending score */
public final class Cause {

  // Key id from datastore uniquely identifiying each category cause.
  private Key id;
  // Category cause name.
  private String name;
  // Trending score calculated based on trending score algorithim for cause.
  private Double trendingScore;

  // Initialize all fields of a Cause
  public Cause(Key id, String name, Double trendingScore) {
    this.id = id;
    this.name = name;
    this.trendingScore = trendingScore;
  }
  // [END fs_class_definition]

  public Cause(Key id) {
    this.id = id;
  }

  public Key getId() {
    return id;
  }

  public void setId(Key id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Double getTrendingScoreCause() {
    return trendingScore;
  }

  public void setTrendingScoreCause(Double trendingScore) {
    this.trendingScore = trendingScore;
  }

}
