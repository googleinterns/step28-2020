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
<<<<<<< HEAD:Donation-platform/src/main/java/com/google/charities/AddCharitiesFromJSON.java

  // TAG IMAGES
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
=======
>>>>>>> new-charities:Donation-platform/src/main/java/com/google/AddCharitiesFromJSON.java
  
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
          put(ADV, 15);
          put(MED, 50);
          put(JEWISH, 15);
          put(WILD, 10);
          put(FAM, 30);
          put(DISEASE, 50);
          put(UNITED, 10);
          put(RELI_ACT, 15);
          put(ZOO, 20);
          put(TREAT, 40);
          put(COMMUNITY, 25);
          put(ANIMAL_RIGHTS, 20);
          put(RELIEF, 30);
          put(RELI_MEDIA, 15);
          put(ADULT_EDU, 20);
          put(SPECIAL_EDU, 25);
          put(CHILD_PROG, 30);
          put(MEDIA, 20);
          put(DEV, 40);
          put(SOCIAL, 40);
          put(PERF, 10);
          put(PLANTS, 20);
          put(EDU_REFORM, 40);
          put(HOMELESS, 50);
          put(RESCUE, 30);
          put(HUM_SERVICE, 35);
          put(YOUTH, 35);
          put(PEACE, 25);
          put(LANDMARK, 10);
          put(FOOD, 40);
          put(POLICY, 30);
          put(SCHLRSHP, 30);
          put(CHILD, 25);
          put(YOUTH_PROG, 25);
          put(SCI_RESEARCH, 35);
          put(HOUSING, 40);
          put(ENVIRON, 20);
          put(MUSEUMS, 20);
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
<<<<<<< HEAD:Donation-platform/src/main/java/com/google/charities/AddCharitiesFromJSON.java
            clone.getName(),
            clone.getLink(),
            clone.getImage(),
            Arrays.asList(db.getTagByName(clone.getCategory())),
            db.getCauseByName(clone.getCause()),
            clone.getDescription(),
            clone.getRating());
=======
            ((String) charityMap.get("name")),
            ((String) charityMap.get("link")),
            ((String) charityMap.get("image")),
            Arrays.asList(db.getTagByName((String) charityMap.get("category"))),
            ((String) charityMap.get("description")));
>>>>>>> new-charities:Donation-platform/src/main/java/com/google/AddCharitiesFromJSON.java
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