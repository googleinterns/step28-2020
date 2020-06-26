
package com.google;


import com.google.model.Charity;
import com.google.model.Tag;
import com.google.model.Users; 
import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static org.junit.Assert.assertEquals;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import java.util.Collection;
import java.util.Collections;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DbCallsTest {

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  private DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
  
  private DbCalls dbCalls;

  // Some variables that we can use in our tests.
  private static final String CHARITY_A = "Charity A";
  private static final String CHARITY_B = "Charity B";
  private static final String TAG_A = "Tag A";
  private static final String TAG_B = "Tag B";
  private static final String EMAIL_A = "Email A";
  private static final String EMAIL_B = "Email B";
  private static final String USERNAME_A = "Username A";
  private static final String USERNAME_B = "Username B";
  private static final String LINK_A = "www.test.com";
  private static final String LINK_B = "www.testb.com";
  private static final Collection<Key> CATEGORIES_A = Collections.emptyList();
  private static final Collection<Key> CATEGORIES_B = Collections.emptyList();
  private static final Collection<Key> USER_INTRESTS_A = Collections.emptyList();
  private static final Collection<Key> USER_INTRESTS_B = Collections.emptyList();
  private static final Collection<Key> CHARITIES_DONATED_TO_A = Collections.emptyList();
  private static final Collection<Key> CHARITIES_DONATED_TO_B = Collections.emptyList();
  private static final String DESCRIPTION_A = "Very testy description.";
  private static final String DESCRIPTION_B= "Not a Very testy description.";
  private static final Double TRENDINGSCORE_A = 0.0;
  private static final Double TRENDINGSCORE_B = 0.0;

  @Before
  public void setUp() {
    helper.setUp();
    dbCalls = new DbCalls(ds);
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void addCharityTest() throws Exception{
    dbCalls.addCharity(CHARITY_A, LINK_A, CATEGORIES_A, DESCRIPTION_A);
    assertEquals(1, ds.prepare(new Query("Charity")).countEntities(withLimit(10)));
  }

  @Test
  public void addTagTest() throws Exception{
    dbCalls.addTag(TAG_A, TRENDINGSCORE_A);
    assertEquals(1, ds.prepare(new Query("Tag")).countEntities(withLimit(10)));
  }

  @Test
  public void addUserTest() throws Exception{
    dbCalls.addUser(USERNAME_A, EMAIL_A, USER_INTRESTS_A, CHARITIES_DONATED_TO_A);
    assertEquals(1, ds.prepare(new Query("Users")).countEntities(withLimit(10)));
  }
  
  @Test
  public void getAllCharitiesTest() throws Exception{
    dbCalls.addCharity(CHARITY_A, LINK_A, CATEGORIES_A, DESCRIPTION_A);
    dbCalls.addCharity(CHARITY_B, LINK_B, CATEGORIES_B, DESCRIPTION_B);
    Collection<Charity> expected = dbCalls.getAllCharities();
    assertEquals(expected.size(), ds.prepare(new Query("Charity")).countEntities(withLimit(10)));
  }

  @Test
  public void getAllTagsTest() throws Exception{
    dbCalls.addTag(TAG_A, TRENDINGSCORE_A);
    dbCalls.addTag(TAG_B, TRENDINGSCORE_B);
    Collection<Tag> expected = dbCalls.getAllTags();
    assertEquals(expected.size(), ds.prepare(new Query("Tag")).countEntities(withLimit(10)));
  }

  @Test
  public void getAllUsersTest() throws Exception{
    dbCalls.addUser(USERNAME_A, EMAIL_A, USER_INTRESTS_A, CHARITIES_DONATED_TO_A);
    dbCalls.addUser(USERNAME_B, EMAIL_B, USER_INTRESTS_B, CHARITIES_DONATED_TO_B);
    Collection<Users> expected = dbCalls.getAllUsers();
    System.out.println(expected);
    assertEquals(expected.size(), ds.prepare(new Query("Users")).countEntities(withLimit(10)));
  }

  @Test
  public void updateCharityTrendingScoreTest() throws Exception{
    Double expected = 0.0;
    Double actual = 5.0;
    dbCalls.addCharity(CHARITY_A, LINK_A, CATEGORIES_A, DESCRIPTION_A);
    Collection<Charity> allCharities = dbCalls.getAllCharities();
    for (Charity elem : allCharities){
      Key id = elem.getId();
      dbCalls.updateCharityTrendingScore(id, 5.0);
    }
    Collection<Charity> newCharities = dbCalls.getAllCharities();
    for (Charity elem : newCharities){
      expected = elem.getTrendingScoreCharity();
    }
    assertEquals(actual, expected);
  }

  @Test
  public void updateTagTrendingScoreTest() throws Exception{
    Double expected = 0.0;
    Double actual = 5.0;
    dbCalls.addTag(TAG_A, TRENDINGSCORE_A);
    Collection<Tag> allTags = dbCalls.getAllTags();
    for (Tag elem : allTags){
      Key id = elem.getId();
      dbCalls.updateTagTrendingScore(id, 5.0);
    }
    Collection<Tag> newTags = dbCalls.getAllTags();
    for (Tag elem : newTags){
      expected = elem.getTrendingScoreTag();
    }
    assertEquals(actual, expected);
  }
  
}