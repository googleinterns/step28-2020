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

import java.util.Collection;
import com.googlecode.objectify.Key;
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
/** Represents a Charity : id, name, link, categories, description, trendingScore. */
public final class Charity implements Comparable<Charity>, Serializable {

  // Key id from datastore uniquely identifiying each charity.
  @Id private Long id;
  // Name of charity
  @Index private String name;
  // link directing users to charity.
  private String link;
  // Image source of charity logo.
  private String imgSrc;
  // Charity's cause.
  @Index private Cause cause;
  // Collection storing tags in the form of datastore keys.
  @Index private Collection<Tag> categories;
  // Description of charity.
  private String description;
  // Trending score calculated based on trending score algorithim for charity.
  private double trendingScore;
  // Trending score for the charity's name.
  private double nameTrendingScore;
  // User rating of charity determined by users.
  private double rating;

  private Charity() {}
  // Initialize all fields of a Charity
  public Charity(
      Long id,
      String name,
      String link,
      String imgSrc,
      Collection<Tag> categories,
      Cause cause,
      String description,
      double trendingScore,
      double rating) {
    this.id = id;
    this.name = name;
    this.link = link;
    this.imgSrc = imgSrc;
    this.categories = categories;
    this.cause = cause;
    this.description = description;
    this.trendingScore = trendingScore;
    this.nameTrendingScore = 0.0;
    this.rating = rating;
  }

  public Charity(Long id) {
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

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getImgSrc() {
    return imgSrc;
  }

  public void setImgSrc(String imgSrc) {
    this.imgSrc = imgSrc;
  }

  public Collection<Tag> getCategories() {
    return categories;
  }

  public void setCategories(Collection<Tag> categories) {
    this.categories = categories;
  }

  public Cause getCause() {
    return cause;
  }

  public void setCause(Cause cause) {
    this.cause = cause;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public double getTrendingScoreCharity() {
    return trendingScore;
  }

  public void setTrendingScoreCharity(double trendingScore) {
    this.trendingScore = trendingScore;
  }

  public double getNameTrendingScore() {
    return trendingScore;
  }

  public void setNameTrendingScore(double trendingScore) {
    this.trendingScore = trendingScore;
  }

  public double getRating() {
    return rating;
  }

  public void setRating(double rating) {
    this.rating = rating;
  }

  @Override
  public int compareTo(Charity b) {
    Double bScore = new Double(b.getTrendingScoreCharity());
    Double thisScore = new Double(this.getTrendingScoreCharity());
    return bScore.compareTo(thisScore);
  }
}
