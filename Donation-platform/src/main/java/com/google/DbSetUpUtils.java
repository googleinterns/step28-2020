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
import com.google.model.Cause;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public final class DbSetUpUtils {

  private DatastoreService ds;
  private DbCalls db;
  
  // constants for Tag names
  private static final String HUNGER = "hunger";
  private static final String EDU = "education";
  private static final String CHILD = "children";
  private static final String ENV = "environment";
  private static final String RACE_EQ = "racial equality";
  private static final String HEALTH = "health";

  // constants for Cause names
  private static final String HUNGER_CAUSE = "hunger";
  private static final String EDU_CAUSE = "education";
  private static final String CHILD_CAUSE = "children";
  private static final String ENV_CAUSE = "environment";
  private static final String RACE_EQ_CAUSE = "racial equality";
  private static final String HEALTH_CAUSE = "health";

  private static final String HUNGER_IMAGE = "https://thumbs.dreamstime.com/b/eating-utensils-icon-trendy-eating-utensils-logo-concept-whi-eating-utensils-icon-trendy-eating-utensils-logo-concept-white-131149320.jpg";
  private static final String EDU_IMAGE = "https://www.pngitem.com/pimgs/m/280-2802271_student-graduation-hat-school-education-icon-png-white.png";
  private static final String CHILD_IMAGE = "https://previews.123rf.com/images/milanpetrovic/milanpetrovic1804/milanpetrovic180400038/99865919-children-icon-holding-hands-vector-on-white-background-.jpg";
  private static final String ENV_IMAGE = "https://thumbs.dreamstime.com/z/environment-icon-vector-isolated-white-background-sign-eco-symbols-transparent-134167444.jpg";
  private static final String RACE_EQ_IMAGE = "https://previews.123rf.com/images/alena08/alena081803/alena08180300068/97706534-people-icon-stick-figure-illustration-people-isolated-on-white-background-vector.jpg";
  private static final String HEALTH_IMAGE = "https://image.shutterstock.com/image-vector/medical-blank-line-icon-high-260nw-756046810.jpg";

  private static final List<String> tags = new ArrayList<String>(Arrays.asList(HUNGER, EDU, CHILD, ENV, RACE_EQ, HEALTH));
  private static final List<String> causes = new ArrayList<String>(Arrays.asList(HUNGER_CAUSE, EDU_CAUSE, CHILD_CAUSE, ENV_CAUSE, RACE_EQ_CAUSE, HEALTH_CAUSE)); 

  public static final Map<String, Integer> tagScores =
    new HashMap<String, Integer>() {
        {
          put(HUNGER, 25);
          put(EDU, 30);
          put(CHILD, 20);
          put(ENV, 10);
          put(RACE_EQ, 50);
          put(HEALTH, 40);
        }
    };

  public static final Map<String, Integer> causeScores =
    new HashMap<String, Integer>() {
        {
          put(HUNGER_CAUSE, 25);
          put(EDU_CAUSE, 30);
          put(CHILD_CAUSE, 20);
          put(ENV_CAUSE, 10);
          put(RACE_EQ_CAUSE, 50);
          put(HEALTH_CAUSE, 40);
        }
    };

  public static final Map<String, String> tagImages =
    new HashMap<String, String>() {
        {
          put(HUNGER, HUNGER_IMAGE);
          put(EDU, EDU_IMAGE);
          put(CHILD, CHILD_IMAGE);
          put(ENV, ENV_IMAGE);
          put(RACE_EQ, RACE_EQ_IMAGE);
          put(HEALTH, HEALTH_IMAGE);
        }
    };

  // constructor for db setup
  public DbSetUpUtils() {
    this.ds = DatastoreServiceFactory.getDatastoreService();
    this.db = new DbCalls(ds);
  }

  public DbSetUpUtils(DatastoreService ds) {
    this.ds = ds;
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
  // NOTE: PersonalizedRecommendationsTest.java relies on populateDatabase
  // only adding the original set of charities (Feeding America, Red Cross,
  // St. Jude's, Nature Conservancy, YMCA, ACLU, and American Heart Association).
  public void populateDatabase() {
    addTags();

    addCauses();

    addCharities();
  }

  // add hardcoded tags to db with scores
  private void addTags() {
    for (String tag: tags) {
        try {
            db.addTag(tag, tagScores.get(tag), tagImages.get(tag));
        } catch (Exception e) {
            System.out.println("Failure adding tags: " + e);
        } 
    }
  }

  // add hardcoded tags to db with scores
  private void addCauses() {
    for (String cause: causes) {
        try {
            db.addCause(cause, causeScores.get(cause));
        } catch (Exception e) {
            System.out.println("Failure adding causes: " + e);
        } 
    }
  }

  // add hardcoded charities to db without userRating
  private void addCharities() {
    try {
      Tag hunger = db.getTagByName(HUNGER);
      Tag education = db.getTagByName(EDU);
      Tag children = db.getTagByName(CHILD);
      Tag environment = db.getTagByName(ENV);
      Tag racialEquality = db.getTagByName(RACE_EQ);
      Tag health = db.getTagByName(HEALTH);

      Cause hungerCause = db.getCauseByName(HUNGER_CAUSE);
      Cause educationCause = db.getCauseByName(EDU_CAUSE);
      Cause childrenCause = db.getCauseByName(CHILD_CAUSE);
      Cause environmentCause = db.getCauseByName(ENV_CAUSE);
      Cause racialEqualityCause = db.getCauseByName(RACE_EQ_CAUSE);
      Cause healthCause = db.getCauseByName(HEALTH_CAUSE);

      db.addCharity(
          "Feeding America",
          "https://secure.feedingamerica.org/site/Donation2",
          "https://www.charities.org/sites/default/files/Feeding-America_logo-web.png",
          Arrays.asList(hunger),
          hungerCause,
          "Feeding America is a United States–based nonprofit organization that is a nationwide network of more than 200 food banks that feed more than 46 million people through food pantries, soup kitchens, shelters, and other community-based agencies. Forbes ranks it as the second largest U.S. charity by revenue.",
          4.0);
      db.addCharity(
          "Red Cross",
          "https://www.redcross.org/donate/donation.html/",
          "https://media.defense.gov/2018/Sep/17/2001966913/-1/-1/0/180917-F-ZZ000-1001.JPG",
          Arrays.asList(health, education),
          healthCause,
          "The American Red Cross, also known as The American National Red Cross, is a humanitarian organization that provides emergency assistance, disaster relief, and disaster preparedness education in the United States",
          4.5);
      db.addCharity(
          "St. Jude's",
          "https://www.stjude.org/donate/pm.html",
          "https://i1.wp.com/engageforgood.com/wp-content/uploads/2018/10/Untitled-design-69.png?fit=700%2C400&ssl=1",
          Arrays.asList(health, children),
          healthCause,
          "Saint Jude Children's Research Hospital, founded in 1962, is a pediatric treatment and research facility focused on children's catastrophic diseases, particularly leukemia and other cancers. The hospital costs about US$2.8 million a day to run, but patients are not charged for their care.",
          3.0);
      db.addCharity(
          "Nature Conservancy",
          "https://support.nature.org/site/Donation2",
          "https://climatepolicyinitiative.org/wp-content/uploads/2018/03/tnc-nature-conservancy-logo.gif",
          Arrays.asList(environment),
          environmentCause,
          "The Nature Conservancy is a charitable environmental organization, headquartered in Arlington, Virginia, United States. Founded in 1951, The Nature Conservancy has over one million members, and has protected more than 119,000,000 acres of land and thousands of miles of rivers worldwide.",
          5.0);
      db.addCharity(
          "YMCA",
          "https://www.ymca.net/be-involved",
          "https://www.tristatehomepage.com/wp-content/uploads/sites/92/2016/05/YMCA20logo20web_1462174680230_8365252_ver1.0-1.jpg?w=720&h=405&crop=1",
          Arrays.asList(children),
          childrenCause,
          "YMCA, sometimes regionally called the Y, is a worldwide youth organization based in Geneva, Switzerland, with more than 64 million beneficiaries in 120 countries.",
          2.0);
      db.addCharity(
          "ACLU",
          "https://action.aclu.org/give/fund-every-fight-ahead?",
          "https://upload.wikimedia.org/wikipedia/commons/thumb/6/65/New_ACLU_Logo_2017.svg/1200px-New_ACLU_Logo_2017.svg.png",
          Arrays.asList(racialEquality),
          racialEqualityCause,
          "The American Civil Liberties Union is a nonprofit organization founded in 1920 \"to defend and preserve the individual rights and liberties guaranteed to every person in this country by the Constitution and laws of the United States\"",
          5.0);
      db.addCharity(
          "American Heart Association",
          "https://www2.heart.org/site/SPageServer?pagename=donatenow_heart",
          "https://upload.wikimedia.org/wikipedia/en/thumb/e/e6/American_Heart_Association_Logo.svg/1200px-American_Heart_Association_Logo.svg.png",
          Arrays.asList(health),
          healthCause,
          "The American Heart Association is a non-profit organization in the United States that funds cardiovascular medical research, educates consumers on healthy living and fosters appropriate cardiac care in an effort to reduce disability and deaths caused by cardiovascular disease and stroke.",
          3.0);
    } catch (Exception e) {
      System.out.println("Failure in adding charities: " + e);
    }
  }

}
