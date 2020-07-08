package com.google.model;

import com.google.appengine.api.datastore.Key;

/** Represents a Tag : id, name, trending score */
public final class Tag {

  // Key id from datastore uniquely identifiying each category tag.
  private Key id;
  // Category tag name.
  private String name;
  // Trending score calculated based on trending score algorithim for tag.
  private Double trendingScore;

  // Initialize all fields of a Tag
  public Tag(Key id, String name, Double trendingScore) {
    this.id = id;
    this.name = name;
    this.trendingScore = trendingScore;
  }
  // [END fs_class_definition]

  public Tag(Key id) {
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

  public Double getTrendingScoreTag() {
    return trendingScore;
  }

  public void setTrendingScoreTag(Double trendingScore) {
    this.trendingScore = trendingScore;
  }
}
