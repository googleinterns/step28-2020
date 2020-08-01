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

package com.google.charities;

import java.util.Collection;

/** Clone of a Charity to store data from charity JSONs: name, link, category, description, rating. */
public final class CharityClone {

  // Name of charity
  private String name;
  // link directing users to charity.
  private String link;
  // Image source of charity logo.
  private String imgSrc;
  // Charity's category.
  private String category;
  // Charity's cause.
  private Cause cause;
  // Description of charity.
  private String description;
  // Trending score calculated based on trending score algorithim for charity.
  private double trendingScore;
  // User rating of charity from Charity Navigator API
  private double rating;

  // Initialize all fields of a CharityClone
  public CharityClone(
      String name,
      String link,
      String image,
      String category,
      String cause,
      String description,
      double rating) {
    this.name = name;
    this.link = link;
    this.image = image;
    this.category = category;
    this.cause = cause;
    this.description = description;
    this.rating = rating;
  }

  public String getName() {
    return name;
  }

  public String getLink() {
    return link;
  }

  public String getImage() {
    return image;
  }

  public String getCategory() {
    return category;
  }

  public String getCause() {
    return cause;
  }

  public String getDescription() {
    return description;
  }

  public double getRating() {
    return rating;
  }

  public String toString() {
    return this.name + "\n" + this.link + "\n" + this.image + "\n" + this.category + "\n" + this.description + "\n" + this.rating + "\n";
  }
}
