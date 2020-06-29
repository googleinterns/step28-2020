package com.google.model;

import java.util.List;
import java.util.Objects;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;


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
  private Double trendingScore;
  // User rating of charity determined by users.
  private Double userRating;


  // Initialize all fields of a Charity
  public Charity(Key id, String name, String link,
              Collection<Key>  categories, String description, Double trendingScore, Double userRating) {
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

  public Double getTrendingScoreCharity() {
    return trendingScore;
  }

  public void setTrendingScoreCharity(Double trendingScore) {
    this.trendingScore = trendingScore;
  }

  public Double getUserRating() {
    return userRating;
  }

  public void setUserRating( Double userRating){
    this.userRating = userRating;
  }
}

class SortByTrending implements Comparator<Charity> {
        /**
        * A comparator for sorting charities by their trending score in descending order.
        */
        @Override
        public int compare(Charity a, Charity b) {
            return Double.compare(b.getTrendingScoreCharity(), a.getTrendingScoreCharity());
        }
}