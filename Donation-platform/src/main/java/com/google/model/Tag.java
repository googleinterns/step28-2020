package com.google.model;

import java.util.List;
import java.util.Objects;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;


/** Represents a Tag : id, name, trending score */
public final class Tag {

  private Key id;
  private String name;
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