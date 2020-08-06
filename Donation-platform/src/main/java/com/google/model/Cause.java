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

import java.io.Serializable;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Index;
import java.lang.Long;

// In Objectify classes must be registered as entities for the service to associate
// the class object with datastore and store it. 
@Entity
// This annotation tells the objectify service to cache the class objects 
// in memcache whenever possible.
@Cache
/** Represents a Cause : id, name, trending score */
public final class Cause {

  // Long id from datastore uniquely identifiying each category cause.
  @Id private Long id;
  // Category cause name.
  @Index private String name;
  // Trending score calculated based on trending score algorithim for cause.
  private Double trendingScore;

  private Cause() {}
  // Initialize all fields of a Cause
  public Cause(Long id, String name, Double trendingScore) {
    this.id = id;
    this.name = name;
    this.trendingScore = trendingScore;
  }
  // [END fs_class_definition]

  public Cause(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Double getCauseTrendingScore() {
    return trendingScore;
  }

  public void setCauseTrendingScore(Double trendingScore) {
    this.trendingScore = trendingScore;
  }

}
