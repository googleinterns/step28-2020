package com.google.model;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/** Represents a Charity : id, name, link, categories, description, trendingScore. */
public final class Charity implements Comparator<Charity>{ {

  private Integer id;
  private String name;
  private String link;
  private List<Integer> categories;
  private String description;


  // [START fs_class_definition]
  public Charity() {
    // Must have a public no-argument constructor
  }

  // Initialize all fields of a Charity
  public Charity(Integer id, String name, String link,
              List<Integer>  categories, String description) {
    this.id = id;
    this.name = name;
    this.link = link;
    this.categories = categories;
    this.description = description;
  }
  // [END fs_class_definition]

  public Charity(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
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

  public List<Integer> getCategories() {
    return categories;
  }

  public void setCategories(List<Integer> regions) {
    this.categories = categories;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
    public int compareTo(Charity b) {
      return Double.compare(b.trendingScore, this.trendingScore);
    }



}