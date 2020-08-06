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
 
import com.google.DbCalls;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
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


public final class AddCharitiesFromJSON {
 
  private DatastoreService ds;
  private DbCalls db;
  
  // TAG NAMES
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
  
  // LIST OF TAG NAMES
  private static final List<String> tags = new ArrayList<String>(Arrays.asList(ANIMAL, ARTS, EDU, ENV, HEALTH, HUMAN, INTL, RIGHTS, RELI, COMM, RESEARCH));

  // CAUSE NAMES
  private static final String ADV = "Advocacy and Education";
  private static final String MED = "Medical Research";
  private static final String JEWISH = "Jewish Federations";
  private static final String WILD = "Wildlife Conservation";
  private static final String FAM = "Patient and Family Support";
  private static final String DISEASE = "Diseases, Disorders, and Disciplines";
  private static final String UNITED = "United Ways";
  private static final String RELI_ACT = "Religious Activities";
  private static final String ZOO = "Zoos and Aquariums";
  private static final String TREAT = "Treatment and Prevention Services";
  private static final String COMMUNITY = "Community Foundations";
  private static final String ANIMAL_RIGHTS = "Animal Rights, Welfare, and Services";
  private static final String RELIEF = "Humanitarian Relief Supplies";
  private static final String RELI_MEDIA = "Religious Media and Broadcasting";
  private static final String ADULT_EDU = "Adult Education Programs and Services";
  private static final String SPECIAL_EDU = "Special Education";
  private static final String CHILD_PROG = "Early Childhood Programs and Services";
  private static final String MEDIA = "Public Broadcasting and Media";
  private static final String DEV = "Development and Relief Services";
  private static final String SOCIAL = "Social Services";
  private static final String PERF = "Performing Arts";
  private static final String PLANTS = "Botanical Gardens, Parks, and Nature Centers";
  private static final String EDU_REFORM = "Education Policy and Reform";
  private static final String HOMELESS = "Homeless Services";
  private static final String RESCUE = "Rescue Missions";
  private static final String HUM_SERVICE = "Multipurpose Human Service Organizations";
  private static final String YOUTH = "Youth Development, Shelter, and Crisis Services";
  private static final String PEACE = "International Peace, Security, and Affairs";
  private static final String LANDMARK = "Libraries, Historical Societies and Landmark Preservation";
  private static final String FOOD = "Food Banks, Food Pantries, and Food Distribution";
  private static final String POLICY = "Social and Public Policy Research";
  private static final String SCHLRSHP = "Scholarship and Financial Support";
  private static final String CHILD = "Children's and Family Services";
  private static final String YOUTH_PROG = "Youth Education Programs and Services";
  private static final String SCI_RESEARCH = "Non-Medical Science & Technology Research";
  private static final String HOUSING = "Housing and Neighborhood Development";
  private static final String ENVIRON = "Environmental Protection and Conservation";
  private static final String MUSEUMS = "Museums";

  private static final List<String> causes = new ArrayList<String>(Arrays.asList(ADV, MED, JEWISH, WILD, FAM, DISEASE, UNITED, RELI_ACT, ZOO, TREAT, COMMUNITY, ANIMAL_RIGHTS, RELIEF, 
                                                                                 RELI_MEDIA, ADULT_EDU, SPECIAL_EDU, CHILD_PROG, MEDIA, DEV, SOCIAL, PERF, PLANTS, EDU_REFORM, 
                                                                                 HOMELESS, RESCUE, HUM_SERVICE, YOUTH, PEACE, LANDMARK, FOOD, POLICY, SCHLRSHP, CHILD, YOUTH_PROG,
                                                                                 SCI_RESEARCH, HOUSING, ENVIRON, MUSEUMS));
  // HARDCODED MAPPING OF TAGS TO TRENDING SCORES
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

  // HARDCODED MAPPING OF CAUSES TO TRENDING SCORES
  public static final Map<String, Integer> causeScores =
    new HashMap<String, Integer>() {
        {
          put(ADV, 0);
          put(MED, 0);
          put(JEWISH, 0);
          put(WILD, 0);
          put(FAM, 0);
          put(DISEASE, 0);
          put(UNITED, 0);
          put(RELI_ACT, 0);
          put(ZOO, 0);
          put(TREAT, 0);
          put(COMMUNITY, 0);
          put(ANIMAL_RIGHTS, 0);
          put(RELIEF, 0);
          put(RELI_MEDIA, 0);
          put(ADULT_EDU, 0);
          put(SPECIAL_EDU, 0);
          put(CHILD_PROG, 0);
          put(MEDIA, 0);
          put(DEV, 0);
          put(SOCIAL, 0);
          put(PERF, 0);
          put(PLANTS, 0);
          put(EDU_REFORM, 0);
          put(HOMELESS, 0);
          put(RESCUE, 0);
          put(HUM_SERVICE, 0);
          put(YOUTH, 0);
          put(PEACE, 0);
          put(LANDMARK, 0);
          put(FOOD, 0);
          put(POLICY, 0);
          put(SCHLRSHP, 0);
          put(CHILD, 0);
          put(YOUTH_PROG, 0);
          put(SCI_RESEARCH, 0);
          put(HOUSING, 0);
          put(ENVIRON, 0);
          put(MUSEUMS, 0);
        }
    };
 
  // MAPPING OF TAGS TO IMAGES
  public static final Map<String, String> tagImages =
    new HashMap<String, String>() {
        {
          put(ANIMAL, "https://i.pinimg.com/originals/51/31/0d/51310de57fc8f015e4020ed258c763ae.jpg");
          put(ARTS, "https://tradinginsider.fr/wp-content/uploads/2018/06/culture-768x473.png");
          put(EDU, "https://blogs.edweek.org/edweek/inside-school-research/Hand-Raised-Young-Boy-Classroom-GETTY-Blog-560x292.jpg");
          put(ENV, "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/nature-quotes-1557340276.jpg");
          put(HEALTH, "https://www.debt.com/wp-content/uploads/2018/06/Doctor-helps-patient.jpg");
          put(HUMAN, "https://arc.losrios.edu/arc/main/img/instruction/ARC-BSS-Division/ARC-Human-Services/ARC-human-services-940x529.jpg");
          put(INTL, "http://opiniojuris.org/wp-content/uploads/63.jpg");
          put(RIGHTS, "https://media2.s-nbcnews.com/i/newscms/2020_23/3382051/200606-washington-demonstration-al-1549_e1c57b283c615f899ba66a0168460c8b.jpg");
          put(RELI, "https://www.sbs.com.au/topics/sites/sbs.com.au.topics/files/styles/full/public/gettyimages-496701302.jpg");
          put(COMM, "https://www.wxriskglobal.com/wp-content/uploads/2018/03/shutterstock_660487873.png");
          put(RESEARCH, "https://www.apa.org/images/title-research-publishing_tcm7-251846.jpg");
        }
    };

  public AddCharitiesFromJSON(DatastoreService ds, DbCalls db) {
    this.ds = ds;
    this.db = db;

  }
 
  public DbCalls getDbCalls() {
    return this.db;
  }
 
  // Add hardcoded tags to DB with scores
  public void addTags() {
    for (String tag: tags) {
        try {
            db.addTag(tag, tagScores.get(tag), tagImages.get(tag));
        } catch (Exception e) {
            System.out.println("Failure adding tags: " + e);
        } 
    }
  }

  // Add hardcoded causes to DB with scores
  public void addCauses() {
    for (String cause: causes) {
        try {
            db.addCause(cause, causeScores.get(cause));
        } catch (Exception e) {
            System.out.println("Failure adding causes: " + e);
        } 
    }
  }
 
  // Add charities from charities.json to db without rating
  public void addCharities() {

    Gson gson = new Gson();
    
    // Locates file in /resources
    File file = new File(getClass().getClassLoader().getResource("charities.json").getFile());
    System.out.println(file.getAbsolutePath());

    try (Reader reader = new FileReader(file)) {
      
      // Converts charities.json into a list of HashMaps where each HashMap corresponds with one of the charity JSON objects
      HashMap[] charityMaps = gson.fromJson(reader, HashMap[].class);

      int numberOfFailedCharities = 0;
      int totalCharities = 0;
      for(HashMap charityMap : charityMaps) {
        try {
          db.addCharity(
            ((String) charityMap.get("name")),
            ((String) charityMap.get("link")),
            ((String) charityMap.get("image")),
            Arrays.asList(db.getTagByName((String) charityMap.get("category"))),
            ((String) charityMap.get("description")));
        } catch (Exception e) {
          numberOfFailedCharities++;
          System.out.println("Failure in adding charity: " + charityMap + "Error: " + e + "\n");
        }
        totalCharities++;
      }
      System.out.println("Failure adding " + numberOfFailedCharities + " charities out of " + totalCharities);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}