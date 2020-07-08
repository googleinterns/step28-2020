package com.google.model;

import java.util.Collection;
import com.google.appengine.api.datastore.Key;

/** Represents a Charity : id, name, link, categories, description, trendingScore. */
public final class Charity {

  // Key id from datastore uniquely identifiying each charity.
  private Key id;
  // Name of charity
  private String name;
  // link directing users to charity.
  private String link;
  // Collection storing tag IDs in the form of datastore keys.
  private Collection<Key> categories;
  // Description of charity.
  private String description;
  // Trending score calculated based on trending score algorithim for charity.
  private double trendingScore;
  // User rating of charity determined by users.
  private double userRating;

  // Initialize all fields of a Charity
  public Charity(
      Key id,
      String name,
      String link,
      Collection<Key> categories,
      String description,
      double trendingScore,
      double userRating) {
    this.id = id;
    this.name = name;
    this.link = link;
    this.categories = categories;
    this.description = description;
    this.trendingScore = trendingScore;
    this.userRating = userRating;
  }

  public Charity(Key id) {
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

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public Collection<Key> getCategories() {
    return categories;
  }

  public void setCategories(Collection<Key> categories) {
    this.categories = categories;
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

  public double getUserRating() {
    return userRating;
  }

  public void setUserRating(double userRating) {
    this.userRating = userRating;
  }
}
