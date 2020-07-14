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
 
import com.google.model.Charity;
import com.google.model.Tag;
import com.google.DbSetUpUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
 
/* Unit tests for PersonalizedRecommendations.java */
@RunWith(JUnit4.class)
public final class PersonalizedRecommendationsTest {
 
  private Charity FA;
  private Charity RC;
  private Charity SJ;
  private Charity NC;
  private Charity YMCA;
  private Charity ACLU;
  private Charity AHA;
 
  private PersonalizedRecommendations recommendation;
  private final LocalServiceTestHelper helper =
       new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private DatastoreService ds;
  private DbCalls db;
 
  @Before
  public void setUp() {
    helper.setUp();
    ds = DatastoreServiceFactory.getDatastoreService();
    db = new DbCalls(ds);
    DbSetUpUtils dbSetUp = new DbSetUpUtils(ds, db);
    dbSetUp.populateDatabase();

    try {
      FA = db.getCharityByName("Feeding America");
      RC = db.getCharityByName("Red Cross");
      SJ = db.getCharityByName("St. Jude's");
      NC = db.getCharityByName("Nature Conservancy");
      YMCA = db.getCharityByName("YMCA");
      ACLU = db.getCharityByName("ACLU");
      AHA = db.getCharityByName("American Heart Association");
      // Reset each charity's trending score so that only tags are considered
      // in testing the personalized ranking.
      FA.setTrendingScoreCharity(0);
      RC.setTrendingScoreCharity(0);
      SJ.setTrendingScoreCharity(0);
      NC.setTrendingScoreCharity(0);
      YMCA.setTrendingScoreCharity(0);
      ACLU.setTrendingScoreCharity(0);
      AHA.setTrendingScoreCharity(0);
    }
    catch (Exception e) {
        System.out.println("Charities not found in db during testing: " + e);
    }
    recommendation = new PersonalizedRecommendations(ds);
  }
 
  @After
  public void tearDown() {
     helper.tearDown();
  }
 
  @Test
  public void onlyTag1Selected() {
    // If only tag #1 is selected, recommend all charities that have tag #1
    // (order is arbitary since we do not have accurate trending scores yet).
 
    List<String> tags = Arrays.asList("health", null, null);
    Collection<Charity> actual = recommendation.getBestMatches(tags);
    Collection<Charity> expected = Arrays.asList(AHA, SJ, RC);
    Assert.assertTrue(sameSetOfCharities(actual, expected));
  }
 
  @Test
  public void onlyTag2Selected() {
    // If only tag #2 is selected, recommend all charities that have tag #2
    // (order is arbitary since we do not have accurate trending scores yet).
 
    List<String> tags = Arrays.asList(null, "health", null);
    Collection<Charity> actual = recommendation.getBestMatches(tags);
    Collection<Charity> expected = Arrays.asList(AHA, SJ, RC);
    Assert.assertTrue(sameSetOfCharities(actual, expected));
  }
 
  @Test
  public void onlyTag3Selected() {
    // If only tag #3 is selected, recommend all charities that have tag #3
    // (order is arbitary since we do not have accurate trending scores yet).
 
    List<String> tags = Arrays.asList(null, null, "health");
    Collection<Charity> actual = recommendation.getBestMatches(tags);
    Collection<Charity> expected = Arrays.asList(AHA, SJ, RC);
    Assert.assertTrue(sameSetOfCharities(actual, expected));
  }
 
  @Test
  public void tag1BeforeTag2() {
    // A charity with just tag #1 should come before a charity with just tag #2
    // in the recommendation order.
 
    // Example: ACLU (which has matching tag #1 (racial equality)) should
    // come before Nature Conservancy (which has matching tag #2 (environment)).
 
    List<String> tags = Arrays.asList("racial equality", "environment", "hunger");
    List<Charity> actual = recommendation.getBestMatches(tags);
 
    String nameOfCharityWithTag1 = "ACLU";
    String nameOfCharityWithTag2 = "Nature Conservancy";
    int indexOfCharityWithTag1 = 0;
    int indexOfCharityWithTag2 = 0;
 
    // Find the respective indices of the charity with just tag #1 and
    // the charity with just tag #2
    for (Charity charity : actual) {
      if ((charity.getName()).equals(nameOfCharityWithTag1)) {
        indexOfCharityWithTag1 = actual.indexOf(charity);
      } else if ((charity.getName()).equals(nameOfCharityWithTag2)) {
        indexOfCharityWithTag2 = actual.indexOf(charity);
      }
    }
 
    Assert.assertTrue(indexOfCharityWithTag1 < indexOfCharityWithTag2);
  }
 
  @Test
  public void tag2BeforeTag3() {
    // A charity with just tag #2 should come before a charity with just tag #3
    // in the recommendation order.
 
    // Example: Nature Conservancy (which has matching tag #2 (environment)) should
    // come before Feeding America (which has matching tag #3 (hunger)).
 
    List<String> tags = Arrays.asList("racial equality", "environment", "hunger");
    List<Charity> actual = recommendation.getBestMatches(tags);
 
    String nameOfCharityWithTag2 = "Nature Conservancy";
    String nameOfCharityWithTag3 = "Feeding America";
    int indexOfCharityWithTag2 = 0;
    int indexOfCharityWithTag3 = 0;
 
    // Find the respective indices of the charity with just tag #2 and
    // the charity with just tag #3
    for (Charity charity : actual) {
      if ((charity.getName()).equals(nameOfCharityWithTag2)) {
        indexOfCharityWithTag2 = actual.indexOf(charity);
      } else if ((charity.getName()).equals(nameOfCharityWithTag3)) {
        indexOfCharityWithTag3 = actual.indexOf(charity);
      }
    }
 
    Assert.assertTrue(indexOfCharityWithTag2 < indexOfCharityWithTag3);
  }
 
  @Test
  public void tags1And2BeforeJust1() {
    // A charity with tags #1 and #2 should come before a charity with just tag #1
    // in the recommendation order.
 
    // Example: Red Cross (which has matching tags #1 (health) and #2 (education)) should
    // come before St. Jude's (which only has matching tag #1 (health)).
 
    List<String> tags = Arrays.asList("health", "education", "racial equality");
    List<Charity> actual = recommendation.getBestMatches(tags);
 
    String nameOfCharityWithTags1And2 = "Red Cross";
    String nameOfCharityWithJustTag1 = "St. Jude's";
    int indexOfCharityWithTags1And2 = 0;
    int indexOfCharityWithJustTag1 = 0;
 
    // Find the respective indices of the charity with tags #1 and #2 and
    // the charity with just tag #1
    for (Charity charity : actual) {
      if ((charity.getName()).equals(nameOfCharityWithTags1And2)) {
        indexOfCharityWithTags1And2 = actual.indexOf(charity);
      } else if ((charity.getName()).equals(nameOfCharityWithJustTag1)) {
        indexOfCharityWithJustTag1 = actual.indexOf(charity);
      }
    }
 
    Assert.assertTrue(indexOfCharityWithTags1And2 < indexOfCharityWithJustTag1);
  }
 
  @Test
  public void tags2And3BeforeJust2() {
    // A charity with tags #2 and #3 should come before a charity with just tag #2
    // in the recommendation order.
 
    // Example: St. Jude's (which has matching tags #2 (health) and #3 (children)) should
    // come before American Heart Association (which only has matching tag #2 (health)).
 
    List<String> tags = Arrays.asList("hunger", "health", "children");
    List<Charity> actual = recommendation.getBestMatches(tags);
 
    String nameOfCharityWithTags2And3 = "St. Jude's";
    String nameOfCharityWithJustTag2 = "American Heart Association";
    int indexOfCharityWithTags2And3 = 0;
    int indexOfCharityWithJustTag2 = 0;
 
    // Find the respective indices of the charity with tags #2 and #3 and
    // the charity with just tag #2
    for (Charity charity : actual) {
      if ((charity.getName()).equals(nameOfCharityWithTags2And3)) {
        indexOfCharityWithTags2And3 = actual.indexOf(charity);
      } else if ((charity.getName()).equals(nameOfCharityWithJustTag2)) {
        indexOfCharityWithJustTag2 = actual.indexOf(charity);
      }
    }
 
    Assert.assertTrue(indexOfCharityWithTags2And3 < indexOfCharityWithJustTag2);
  }
 
  @Test
  public void onlyTags1and2Selected() {
    // If only tags #1 and #2 are selected, recommend all charities that have tag
    // #1, tag #2, or both #1 and #2. Every charity that has tag #1 should come before
    // every charity that does not have tag #1.
 
    // Example: All charities with the tags "children" and/or "health" should be
    // recommended. Every charity with the tag "children" should come before every
    // charity without the tag "children."
 
    boolean everyCharityPresent = false;
    boolean correctOrdering = false;
 
    List<String> tags = Arrays.asList("children", "health", null);
    List<Charity> actual = recommendation.getBestMatches(tags);
    List<Charity> expected = Arrays.asList(SJ, YMCA, AHA, RC);
    everyCharityPresent = sameSetOfCharities(actual, expected);
 
    // Find the index of the highest-ranking charity without tag #1
    int indexOfHighestRankingCharityWithoutTag1 = actual.size() - 1;
    for (Charity charity : actual) {
      Collection<Tag> charityTagObjects = charity.getCategories();
      Collection<String> charityTagNames = new ArrayList<String>();
      for(Tag tag : charityTagObjects) {
        charityTagNames.add(tag.getName());
      }
      if (!(new HashSet<>(charityTagNames)).contains("children")
          && actual.indexOf(charity) < indexOfHighestRankingCharityWithoutTag1) {
        indexOfHighestRankingCharityWithoutTag1 = actual.indexOf(charity);
      }
    }
    // Find the index of the lowest-ranking charity with tag #1
    int indexOfLowestRankingCharityWithTag1 = 0;
    for (Charity charity : actual) {
      Collection<Tag> charityTagObjects = charity.getCategories();
      Collection<String> charityTagNames = new ArrayList<String>();
      for(Tag tag : charityTagObjects) {
        charityTagNames.add(tag.getName());
      }
      if ((new HashSet<>(charityTagNames)).contains("children")
          && actual.indexOf(charity) > indexOfLowestRankingCharityWithTag1) {
        indexOfLowestRankingCharityWithTag1 = actual.indexOf(charity);
      }
    }
    correctOrdering = indexOfLowestRankingCharityWithTag1 < indexOfHighestRankingCharityWithoutTag1;
 
    Assert.assertTrue(everyCharityPresent && correctOrdering);
  }
 
  @Test
  public void onlyTags2and3Selected() {
    // If only tags #2 and #3 are selected, recommend all charities that have tag
    // #3, tag #3, or both #2 and #3. Every charity that has tag #2 should come before
    // every charity that does not have tag #2.
 
    // Example: All charities with the tags "health" and/or "children" should be
    // recommended. Every charity with the tag "health" should come before every
    // charity without the tag "health."
 
    boolean everyCharityPresent = false;
    boolean correctOrdering = false;
 
    List<String> tags = Arrays.asList(null, "health", "children");
    List<Charity> actual = recommendation.getBestMatches(tags);
    List<Charity> expected = Arrays.asList(SJ, RC, AHA, YMCA);
    everyCharityPresent = sameSetOfCharities(actual, expected);
 
    // Find the index of the highest-ranking charity without tag #2
    int indexOfHighestRankingCharityWithoutTag2 = actual.size() - 1;
    for (Charity charity : actual) {
      Collection<Tag> charityTagObjects = charity.getCategories();
      Collection<String> charityTagNames = new ArrayList<String>();
      for(Tag tag : charityTagObjects) {
        charityTagNames.add(tag.getName());
      }
      if (!(new HashSet<>(charityTagNames)).contains("health")
          && actual.indexOf(charity) < indexOfHighestRankingCharityWithoutTag2) {
        indexOfHighestRankingCharityWithoutTag2 = actual.indexOf(charity);
      }
    }
    // Find the index of the lowest-ranking charity with tag #2
    int indexOfLowestRankingCharityWithTag2 = 0;
    for (Charity charity : actual) {
      Collection<Tag> charityTagObjects = charity.getCategories();
      Collection<String> charityTagNames = new ArrayList<String>();
      for(Tag tag : charityTagObjects) {
        charityTagNames.add(tag.getName());
      }
      if ((new HashSet<>(charityTagNames)).contains("health")
          && actual.indexOf(charity) > indexOfLowestRankingCharityWithTag2) {
        indexOfLowestRankingCharityWithTag2 = actual.indexOf(charity);
      }
    }
    correctOrdering = indexOfLowestRankingCharityWithTag2 < indexOfHighestRankingCharityWithoutTag2;
 
    Assert.assertTrue(everyCharityPresent && correctOrdering);
  }

  /* Checks if two charity collections have identical sets of charities (order does not matter). */
  public static boolean sameSetOfCharities(Collection<Charity> charities1, Collection<Charity> charities2){
    HashSet<String> charity1Names = new HashSet<String>();
    HashSet<String> charity2Names = new HashSet<String>();
    for(Charity charity : charities1) {
      charity1Names.add(charity.getName());
    }
    for(Charity charity : charities2) {
      charity2Names.add(charity.getName());
    }
    return charity1Names.equals(charity2Names);
  }
}