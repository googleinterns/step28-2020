package com.google.model;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Collection;
import java.util.Collections;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;


/** Represents a Charity : id, name, link, categories, description, trendingScore. */
public final class Charity implements Comparator<Charity>{ {

  private Key id;
  private String name;
  private String link;
  private Collection<Integer> categories;
  private String description;
  private Double trendingScore;

  // Initialize all fields of a Charity
  public Charity(Key id, String name, String link,
              Collection<Integer>  categories, String description, Double trendingScore) {
    this.id = id;
    this.name = name;
    this.link = link;
    this.categories = categories;
    this.description = description;
    this.trendingScore = trendingScore;
  }
  // [END fs_class_definition]

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

  public Collection<Integer> getCategories() {
    return categories;
  }

  public void setCategories(Collection<Integer> regions) {
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

  @Override
    public int compareTo(Charity b) {
      return Double.compare(b.trendingScore, this.trendingScore);
    }


}