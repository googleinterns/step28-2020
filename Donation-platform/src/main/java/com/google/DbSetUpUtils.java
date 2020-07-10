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

package com.google;

import java.util.Arrays;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.model.Charity;
import com.google.model.Tag;

public final class DbSetUpUtils {

  private DatastoreService ds;
  private DbCalls db;

  // constructor for db setup
  public DbSetUpUtils() {
    this.ds = DatastoreServiceFactory.getDatastoreService();
    this.db = new DbCalls(ds);
  }

  public DbSetUpUtils(DatastoreService ds, DbCalls db) {
    this.ds = ds;
    this.db = db;
  }

  // getter
  public DbCalls getDbCalls() {
    return this.db;
  }

  // add hardcoded charities into the db
  public void populateDatabase() {
    addTags();

    addCharities();

    updateWithUserRatings();
  }

  // add hardcoded tags to db with scores
  private void addTags() {
    try {
      db.addTag("hunger", 25.0);
      db.addTag("education", 30.0);
      db.addTag("children", 20.0);
      db.addTag("environment", 10.0);
      db.addTag("racial equality", 50.0);
      db.addTag("health", 40.0);
    } catch (Exception e) {
      System.out.println("Failure adding tags: " + e);
    }
  }

  // add hardcoded charities to db without userRating
  private void addCharities() {
    try {
      Tag hunger = db.getTagByName("hunger");
      Tag education = db.getTagByName("education");
      Tag children = db.getTagByName("children");
      Tag environment = db.getTagByName("environment");
      Tag racialEquality = db.getTagByName("racial equality");
      Tag health = db.getTagByName("health");

      db.addCharity(
          "Feeding America",
          "https://secure.feedingamerica.org/site/Donation2",
          "https://www.charities.org/sites/default/files/Feeding-America_logo-web.png",
          Arrays.asList(hunger),
          "");
      db.addCharity(
          "Red Cross",
          "https://www.redcross.org/donate/donation.html/",
          "https://media.defense.gov/2018/Sep/17/2001966913/-1/-1/0/180917-F-ZZ000-1001.JPG",
          Arrays.asList(health, education),
          "");
      db.addCharity(
          "St. Jude's",
          "https://www.stjude.org/donate/pm.html",
          "https://i1.wp.com/engageforgood.com/wp-content/uploads/2018/10/Untitled-design-69.png?fit=700%2C400&ssl=1",
          Arrays.asList(health, children),
          "");
      db.addCharity(
          "Nature Conservancy",
          "https://support.nature.org/site/Donation2",
          "https://climatepolicyinitiative.org/wp-content/uploads/2018/03/tnc-nature-conservancy-logo.gif",
          Arrays.asList(environment),
          "");
      db.addCharity(
          "YMCA",
          "https://www.ymca.net/be-involved",
          "https://www.tristatehomepage.com/wp-content/uploads/sites/92/2016/05/YMCA20logo20web_1462174680230_8365252_ver1.0-1.jpg?w=720&h=405&crop=1",
          Arrays.asList(children),
          "");
      db.addCharity(
          "ACLU",
          "https://action.aclu.org/give/fund-every-fight-ahead?",
          "https://upload.wikimedia.org/wikipedia/commons/thumb/6/65/New_ACLU_Logo_2017.svg/1200px-New_ACLU_Logo_2017.svg.png",
          Arrays.asList(racialEquality),
          "");
      db.addCharity(
          "American Heart Association",
          "https://www2.heart.org/site/SPageServer?pagename=donatenow_heart",
          "https://upload.wikimedia.org/wikipedia/en/thumb/e/e6/American_Heart_Association_Logo.svg/1200px-American_Heart_Association_Logo.svg.png",
          Arrays.asList(health),
          "");
    } catch (Exception e) {
      System.out.println("Failure in adding charities: " + e);
    }
  }

  // update charities in db with userRating
  private void updateWithUserRatings() {
    try {
      Charity char1 = db.getCharityByName("Feeding America");
      char1.setUserRating(4);
      db.updateCharity(char1);
      Charity char2 = db.getCharityByName("Red Cross");
      char2.setUserRating(4.5);
      db.updateCharity(char2);
      Charity char3 = db.getCharityByName("St. Jude's");
      char3.setUserRating(3);
      db.updateCharity(char3);
      Charity char4 = db.getCharityByName("Nature Conservancy");
      char4.setUserRating(5);
      db.updateCharity(char4);
      Charity char5 = db.getCharityByName("YMCA");
      char5.setUserRating(2);
      db.updateCharity(char5);
      Charity char6 = db.getCharityByName("ACLU");
      char6.setUserRating(5);
      db.updateCharity(char6);
      Charity char7 = db.getCharityByName("American Heart Association");
      char7.setUserRating(3);
      db.updateCharity(char7);
    } catch (Exception e) {
      System.out.println("Failure updating UserRatings: " + e);
    }
  }
}
