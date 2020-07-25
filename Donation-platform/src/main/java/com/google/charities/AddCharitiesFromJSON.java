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
 
import java.util.Arrays;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.model.Charity;
import com.google.model.Tag;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
 
import com.google.DbCalls;
import com.google.charities.CharityClone;
import com.google.gson.Gson;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
 
public final class AddCharitiesFromJSON {
 
  private DatastoreService ds;
  private DbCalls db;
  
  private static final String ANIMAL = "Animals";
  private static final String ARTS = "Arts, Culture, Humanities";
  private static final String EDU = "Education";
  private static final String ENV = "Environment";
  private static final String HEALTH = "Health";
  private static final String HUMAN = "Human Services";
  private static final String INTL = "International";
  private static final String RIGHTS = "Human and Civil Rights";
  private static final String RELI = "Religion";
  private static final String COMM = "Community Development";
  private static final String RESEARCH = "Research and Public Policy";
 
  private static final String ANIMAL_IMAGE = "https://i.pinimg.com/originals/51/31/0d/51310de57fc8f015e4020ed258c763ae.jpg";
  private static final String ARTS_IMAGE = "https://tradinginsider.fr/wp-content/uploads/2018/06/culture-768x473.png";
  private static final String EDU_IMAGE = "https://blogs.edweek.org/edweek/inside-school-research/Hand-Raised-Young-Boy-Classroom-GETTY-Blog-560x292.jpg";
  private static final String ENV_IMAGE = "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/nature-quotes-1557340276.jpg";
  private static final String HEALTH_IMAGE = "https://www.debt.com/wp-content/uploads/2018/06/Doctor-helps-patient.jpg";
  private static final String HUMAN_IMAGE = "https://arc.losrios.edu/arc/main/img/instruction/ARC-BSS-Division/ARC-Human-Services/ARC-human-services-940x529.jpg";
  private static final String INTL_IMAGE = "http://opiniojuris.org/wp-content/uploads/63.jpg";
  private static final String RIGHTS_IMAGE = "https://media2.s-nbcnews.com/i/newscms/2020_23/3382051/200606-washington-demonstration-al-1549_e1c57b283c615f899ba66a0168460c8b.jpg";
  private static final String RELI_IMAGE = "https://www.sbs.com.au/topics/sites/sbs.com.au.topics/files/styles/full/public/gettyimages-496701302.jpg";
  private static final String COMM_IMAGE = "https://www.wxriskglobal.com/wp-content/uploads/2018/03/shutterstock_660487873.png";
  private static final String RESEARCH_IMAGE = "https://www.apa.org/images/title-research-publishing_tcm7-251846.jpg";
 
  private static final List<String> tags = new ArrayList<String>(Arrays.asList(ANIMAL, ARTS, EDU, ENV, HEALTH, HUMAN, INTL, RIGHTS, RELI, COMM, RESEARCH)); 
 
 
  public static final Map<String, Integer> tagScores =
    new HashMap<String, Integer>() {
        {
          put(ANIMAL, 15);
          put(ARTS, 10);
          put(EDU, 30);
          put(ENV, 30);
          put(HEALTH, 50);
          put(HUMAN, 40);
          put(INTL, 30);
          put(RIGHTS, 50);
          put(RELI, 20);
          put(COMM, 40);
          put(RESEARCH, 20);
        }
    };
 
  public static final Map<String, String> tagImages =
    new HashMap<String, String>() {
        {
          put(ANIMAL, ANIMAL_IMAGE);
          put(ARTS, ANIMAL_IMAGE);
          put(EDU, ANIMAL_IMAGE);
          put(ENV, ANIMAL_IMAGE);
          put(HEALTH, ANIMAL_IMAGE);
          put(HUMAN, ANIMAL_IMAGE);
          put(INTL, ANIMAL_IMAGE);
          put(RIGHTS, ANIMAL_IMAGE);
          put(RELI, ANIMAL_IMAGE);
          put(COMM, ANIMAL_IMAGE);
          put(RESEARCH, ANIMAL_IMAGE);
        }
    };
 
  public AddCharitiesFromJSON(DatastoreService ds, DbCalls db) {
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
 
    addCharities();
 
    //updateWithUserRatings();
  }
 
  // add hardcoded tags to db with scores
  public void addTags() {
    for (String tag: tags) {
        try {
            db.addTag(tag, tagScores.get(tag), tagImages.get(tag));
        } catch (Exception e) {
            System.out.println("Failure adding tags: " + e);
        } 
    }
  }
 
  // add hardcoded charities to db without userRating
  public void addCharities() {

    Gson gson = new Gson();

    try (Reader reader = new FileReader("/home/ryandraper/step28-2020/Donation-platform/target/donation-platform-1/../../src/main/java/com/google/charities/charities.json")) {

      CharityClone[] clones = gson.fromJson(reader, CharityClone[].class);

      int i = 0;
      int j = 0;
      for(CharityClone clone : clones) {
        try {
          db.addCharity(
            clone.getName(),
            clone.getLink(),
            clone.getImage(),
            Arrays.asList(db.getTagByName(clone.getCategory())),
            clone.getDescription());
        } catch (Exception e) {
          i++;
          System.out.println("Failure in adding charity: " + clone + "Error: " + e + "\n");
        }
        j++;
      }
      System.out.println("Failure adding " + i + " charities out of " + j);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
 
//   // update charities in db with userRating
//   private void updateWithUserRatings() {
//     // try {
//     //   Charity char1 = db.getCharityByName("Feeding America");
//     //   char1.setUserRating(4);
//     //   db.updateCharity(char1);
//     //   Charity char2 = db.getCharityByName("Red Cross");
//     //   char2.setUserRating(4.5);
//     //   db.updateCharity(char2);
//     //   Charity char3 = db.getCharityByName("St. Jude's");
//     //   char3.setUserRating(3);
//     //   db.updateCharity(char3);
//     //   Charity char4 = db.getCharityByName("Nature Conservancy");
//     //   char4.setUserRating(5);
//     //   db.updateCharity(char4);
//     //   Charity char5 = db.getCharityByName("YMCA");
//     //   char5.setUserRating(2);
//     //   db.updateCharity(char5);
//     //   Charity char6 = db.getCharityByName("ACLU");
//     //   char6.setUserRating(5);
//     //   db.updateCharity(char6);
//     //   Charity char7 = db.getCharityByName("American Heart Association");
//     //   char7.setUserRating(3);
//     //   db.updateCharity(char7);
//     // } catch (Exception e) {
//     //   System.out.println("Failure updating UserRatings: " + e);
//     // }
//   }
}
 
 
 
