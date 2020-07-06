package com.google.model;

import java.util.List;
import java.util.Objects;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;


/** Represents a Charity : id, name, link, categories, description, trendingScore. */
public final class Charity implements Comparable<Charity> { 

  // Key id from datastore uniquely identifiying each charity.
  private Key id;
  // Name of charity
  private String name;
  // Link directing users to charity.
  private String link;
  // Image source of charity logo.
  private String imgSrc;
  // Collection storing tag IDs in the form of datastore keys.
  private Collection<Key> categories;
  // Description of charity.
  private String description;
  // Trending score calculated based on trending score algorithim for charity.
  private double trendingScore;
  // User rating of charity determined by users.
  private double userRating;


  // Initialize all fields of a Charity
  public Charity(Key id, String name, String link, String imgSrc,
              Collection<Key>  categories, String description, double trendingScore, double userRating) {
    this.id = id;
    this.name = name;
    this.link = link;
    this.imgSrc = imgSrc;
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
  
  public void setimgSrc(String link) {
    this.link = link;
  }

  public String getImgSrc() {
    return imgSrc;
  }
  
  public void setImgSrc(String imgSrc) {
    this.imgSrc = imgSrc;
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

  public void setUserRating( double userRating){
    this.userRating = userRating;
  }

  @Override
  public int compareTo(Charity b) {
    return Double.compare(b.getTrendingScore(), this.getTrendingScore());
  }
}



