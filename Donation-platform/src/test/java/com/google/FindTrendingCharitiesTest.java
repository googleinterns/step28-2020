// // Copyright 2019 Google LLC
// //
// // Licensed under the Apache License, Version 2.0 (the "License");
// // you may not use this file except in compliance with the License.
// // You may obtain a copy of the License at
// //
// //     https://www.apache.org/licenses/LICENSE-2.0
// //
// // Unless required by applicable law or agreed to in writing, software
// // distributed under the License is distributed on an "AS IS" BASIS,
// // WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// // See the License for the specific language governing permissions and
// // limitations under the License.

// package com.google;

// import java.util.Arrays;
// import java.util.Collection;
// import org.junit.Assert;
// import org.junit.Before;
// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.junit.runners.JUnit4;
// import java.util.HashMap;
// import java.util.Map;
// import com.google.appengine.api.datastore.DatastoreService;
// import com.google.appengine.api.datastore.DatastoreServiceFactory;
// import com.google.appengine.api.datastore.Entity;
// import com.google.appengine.api.datastore.Query;
// import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
// import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
// import org.junit.After;
// import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
// import static org.junit.Assert.assertEquals;
// import com.google.appengine.api.datastore.Key;
// import java.util.Collections;
// import java.util.List;

// import com.google.DbSetUpUtils;
// import com.google.model.Charity;
// import com.google.model.Tag;

// /** */
// @RunWith(JUnit4.class)
// public final class FindTrendingCharitiesTest {

//   private Charity FA;
//   private Charity RC;
//   private Charity SJ;
//   private Charity NC;
//   private Charity YMCA;
//   private Charity ACLU;
//   private Charity AHA;

//   private FindTrendingCharities query;

//   private final LocalServiceTestHelper helper =
//        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

//    private DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

//    private DbCalls dbCalls;

//    private static final String CHARITY_A = "Charity A";
//   private static final String CHARITY_B = "Charity B";
//   private static final String CHARITY_C = "Charity C";
//   private static final String TAG_A = "Tag A";
//   private static final String TAG_B = "Tag B";
//   private static final String EMAIL_A = "Email A";
//   private static final String EMAIL_B = "Email B";
//   private static final String USERNAME_A = "Username A";
//   private static final String USERNAME_B = "Username B";
//   private static final String LINK_A = "www.test.com";
//   private static final String LINK_B = "www.testb.com";
//   private static final String IMGSRC_A = "www.testimageb.com";
//   private static final String IMGSRC_B = "www.testimagea.com";
//   private static final String USER_ID = "userid";
//   private static final Collection<Tag> CATEGORIES_A = Collections.emptyList();
//   private static final Collection<Tag> CATEGORIES_B = Collections.emptyList();
//   private static final Collection<Tag> CATEGORIES_C = Collections.emptyList();
//   private static final Collection<Tag> USER_INTRESTS_A = Collections.emptyList();
//   private static final Collection<Tag> USER_INTRESTS_B = Collections.emptyList();
//   private static final Collection<Charity> CHARITIES_DONATED_TO_A = Collections.emptyList();
//   private static final Collection<Charity> CHARITIES_DONATED_TO_B = Collections.emptyList();
//   private static final String DESCRIPTION_A = "Very testy description.";
//   private static final String DESCRIPTION_B = "Not a Very testy description.";
//   private static final double TRENDINGSCORE_A = 0.0;
//   private static final double TRENDINGSCORE_B = 0.0;
//   private static final double USER_RATING_A = 0.0;
//   private static final String PLACEHOLDER_STRING = "placeholder";
//   // Some variables for the query strings.
//   private static final String CHARITY = "Charity";
//   private static final String TRENDING_SCORE = "trendingScore";
//   private static final String TAG = "Tag";
//   private static final String USERS = "Users";
//   private static final String NAME = "name";
//   private static final String LINK = "link";
//   private static final String IMGSRC = "imgrc";
//   private static final String CATEGORIES = "categories";
//   private static final String DESCRIPTION = "description";
//   private static final String USERNAME = "userName";
//   private static final String USER_RATING = "userRating";
//   private static final String EMAIL = "email";
//   private static final String USER_INTERESTS = "userInterests";
//   private static final String CHARITIES_DONATED_TO = "charitiesDonatedTo";

//   @Before
//   public void setUp() {
//     helper.setUp();
//     dbCalls = new DbCalls(ds);
//     // DbSetUpUtils dbSetUp = new DbSetUpUtils(ds, db);
//     // dbSetUp.populateDatabase();
//     // query = new FindTrendingCharities();
//   }

//   @After
//   public void tearDown() {
//      helper.tearDown();
//   }

//   @Test
//   public void temp() {
//     //   try {
//     //     FA = db.getCharityByName("Feeding America");
//     //     RC = db.getCharityByName("Red Cross");
//     //     SJ = db.getCharityByName("St. Jude's");
//     //     NC = db.getCharityByName("Nature Conservancy");
//     //     YMCA = db.getCharityByName("YMCA");
//     //     ACLU = db.getCharityByName("ACLU");
//     //     AHA = db.getCharityByName("American Heart Association");
//     // }
//     // catch (Exception e) {
//     //     System.out.println("charities not found in db during testing");
//     // }
//       Assert.assertEquals(1, 1);
//   }

//   @Test
//   public void addCharityTest() throws Exception {
//     dbCalls.addCharity(CHARITY_A, LINK_A, IMGSRC_A, CATEGORIES_A, DESCRIPTION_A);
//     assertEquals(1, ds.prepare(new Query(CHARITY)).countEntities(withLimit(10)));
//   }

// //   @Test
// //   public void testTrendingOrderIsCorrect() {
// //     Collection<Charity> actual = query.query();
// //     Collection<Charity> expected = Arrays.asList(ACLU, AHA, RC, SJ, FA, YMCA, NC);

// //     Assert.assertEquals(expected, actual);
// //   }

// //   @Test
// //   public void testTrendingScoresIsCorrect() {
// //     Collection<Charity> trendingCharities = query.query();

// //     Map<Charity, Double> expected =
// //         new HashMap<Charity, Double>() {
// //           {
// //             put(ACLU, 38.75);
// //             put(AHA, 30.75);
// //             put(RC, 27.375);
// //             put(SJ, 23.25);
// //             put(FA, 19.75);
// //             put(YMCA, 15.5);
// //             put(NC, 8.75);
// //           }
// //         };

// //     Map<Charity, Double> actual =
// //         new HashMap<Charity, Double>() {
// //           {
// //             put(ACLU, ACLU.getTrendingScoreCharity());
// //             put(AHA, AHA.getTrendingScoreCharity());
// //             put(RC, RC.getTrendingScoreCharity());
// //             put(SJ, SJ.getTrendingScoreCharity());
// //             put(FA, FA.getTrendingScoreCharity());
// //             put(YMCA, YMCA.getTrendingScoreCharity());
// //             put(NC, NC.getTrendingScoreCharity());
// //           }
// //         };

// //     Assert.assertEquals(expected, actual);
// //   }
// }
