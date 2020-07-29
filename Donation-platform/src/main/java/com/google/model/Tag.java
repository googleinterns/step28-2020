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

@Entity
@Cache
/** Represents a Tag : id, name, trending score */
public final class Tag implements Serializable {

  // Long id from datastore uniquely identifiying each category tag.
  @Id private Long id;
  // Category tag name.
  @Index private String name;
  // Trending score calculated based on trending score algorithim for tag.
  @Index private Double trendingScore;
  // Image for showing each tag category.
  @Index private String imgSrc;

  private Tag() {}
  // Initialize all fields of a Tag
  public Tag(Long id, String name, Double trendingScore, String imgSrc) {
    this.id = id;
    this.name = name;
    this.trendingScore = trendingScore;
    this.imgSrc = imgSrc;
  }
  // [END fs_class_definition]

  public Tag(Long id) {
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

  public Double getTrendingScoreTag() {
    return trendingScore;
  }

  public void setTrendingScoreTag(Double trendingScore) {
    this.trendingScore = trendingScore;
  }

  public String getImgSrc() {
    return imgSrc;
  }

  public void setImgSrc(String imgSrc) {
    this.imgSrc = imgSrc;
  }
}
