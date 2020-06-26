
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
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyFactory.Builder;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.Filter;
import java.util.Collection;
import java.util.Collections;
import java.util.Arrays;
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
  private static final String CHARITY_C = "Charity C";
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
  private static final Collection<Key> CATEGORIES_C = Collections.emptyList();
  private static final Collection<Key> USER_INTRESTS_A = Collections.emptyList();
  private static final Collection<Key> USER_INTRESTS_B = Collections.emptyList();
  private static final Collection<Key> CHARITIES_DONATED_TO_A = Collections.emptyList();
  private static final Collection<Key> CHARITIES_DONATED_TO_B = Collections.emptyList();
  private static final String DESCRIPTION_A = "Very testy description.";
  private static final String DESCRIPTION_B= "Not a Very testy description.";
  private static final Double TRENDINGSCORE_A = 0.0;
  private static final Double TRENDINGSCORE_B = 0.0;

  // Sets up local datastore service and dbCalls object.
  @Before
  public void setUp() {
    helper.setUp();
    dbCalls = new DbCalls(ds);
  }
  // Cleans up test.
  @After
  public void tearDown() {
    helper.tearDown();
  }
  // Test checks if function adds charity to the database.
  @Test
  public void addCharityTest() throws Exception{
    dbCalls.addCharity(CHARITY_A, LINK_A, CATEGORIES_A, DESCRIPTION_A);
    assertEquals(1, ds.prepare(new Query("Charity")).countEntities(withLimit(10)));
  }
  // Test checks if function adds tag to the database.
  @Test
  public void addTagTest() throws Exception{
    dbCalls.addTag(TAG_A, TRENDINGSCORE_A);
    assertEquals(1, ds.prepare(new Query("Tag")).countEntities(withLimit(10)));
  }
  // Test checks if function adds user to the database.
  @Test
  public void addUserTest() throws Exception{
    dbCalls.addUser(USERNAME_A, EMAIL_A, USER_INTRESTS_A, CHARITIES_DONATED_TO_A);
    assertEquals(1, ds.prepare(new Query("Users")).countEntities(withLimit(10)));
  }
  // Test checks if function returns all charities in database.
  @Test
  public void getAllCharitiesTest() throws Exception{
    dbCalls.addCharity(CHARITY_A, LINK_A, CATEGORIES_A, DESCRIPTION_A);
    dbCalls.addCharity(CHARITY_B, LINK_B, CATEGORIES_B, DESCRIPTION_B);
    Collection<Charity> expected = dbCalls.getAllCharities();
    assertEquals(expected.size(), ds.prepare(new Query("Charity")).countEntities(withLimit(10)));
  }
  // Test checks if function returns all tags in database.
  @Test
  public void getAllTagsTest() throws Exception{
    dbCalls.addTag(TAG_A, TRENDINGSCORE_A);
    dbCalls.addTag(TAG_B, TRENDINGSCORE_B);
    Collection<Tag> expected = dbCalls.getAllTags();
    assertEquals(expected.size(), ds.prepare(new Query("Tag")).countEntities(withLimit(10)));
  }
  // Test checks if function returns all users in database.
  @Test
  public void getAllUsersTest() throws Exception{
    dbCalls.addUser(USERNAME_A, EMAIL_A, USER_INTRESTS_A, CHARITIES_DONATED_TO_A);
    dbCalls.addUser(USERNAME_B, EMAIL_B, USER_INTRESTS_B, CHARITIES_DONATED_TO_B);
    Collection<Users> expected = dbCalls.getAllUsers();
    assertEquals(expected.size(), ds.prepare(new Query("Users")).countEntities(withLimit(10)));
  }
  // Test checks if object returned corresponds to name passed in.
  @Test
  public void getCharityByNameTest() throws Exception{
    dbCalls.addCharity(CHARITY_A, LINK_A, CATEGORIES_A, DESCRIPTION_A);
    dbCalls.addCharity(CHARITY_B, LINK_B, CATEGORIES_B, DESCRIPTION_B);
    Charity expected = dbCalls.getCharityByName(CHARITY_A);
    assertEquals(expected.getName(), CHARITY_A);
  }
  // Test checks if object returned corresponds to name passed in.
  @Test
  public void getTagByNameTest() throws Exception{
    dbCalls.addTag("BLM", 6.0);
    Tag actualTag = dbCalls.getTagByName("BLM");
    String expected = "BLM";
    String actual = actualTag.getName();
    assertEquals(expected, actual);
  }
  // Test checks if function returns all charities that contain certain tag.
  @Test
  public void getCharitiesByTagTest() throws Exception{ 
    dbCalls.addTag("BLM", 6.0);
    Tag blmTag = dbCalls.getTagByName("BLM");
    Key blmKey = blmTag.getId();
    Collection<Key> CATEGORY_A_WITHKEY = Arrays.asList(blmKey);
    Collection<Key> CATEGORY_B_WITHKEY = Arrays.asList(blmKey);
    dbCalls.addCharity(CHARITY_A, LINK_A, CATEGORY_A_WITHKEY, DESCRIPTION_A);
    dbCalls.addCharity(CHARITY_B, LINK_B, CATEGORY_B_WITHKEY, DESCRIPTION_B);
    dbCalls.addCharity(CHARITY_C, LINK_B, CATEGORIES_C, DESCRIPTION_B);
    Collection<Charity> expected = dbCalls.getCharitiesByTag("BLM");
    assertEquals(expected.size(), 2);
  }
  // Test checks if function returns object when queried with username.
  @Test
  public void getUserByUserNameTest() throws Exception{
    dbCalls.addUser(USERNAME_A, EMAIL_A, USER_INTRESTS_A, CHARITIES_DONATED_TO_A);
    Users actualUser = dbCalls.getUserByUserName(USERNAME_A);
    String actual = actualUser.getUserName();
    String expected = USERNAME_A;
    assertEquals(actual, expected);
  }
  // Test checks if function returns object when queried with email.
  @Test
  public void getUserByEmailTest() throws Exception{
    dbCalls.addUser(USERNAME_A, EMAIL_A, USER_INTRESTS_A, CHARITIES_DONATED_TO_A);
    Users actualUser = dbCalls.getUserByEmail(EMAIL_A);
    String actual = actualUser.getUserName();
    String expected = USERNAME_A;
    assertEquals(actual, expected);
  }
  // Test checks if function converts entity into class object.
  @Test
  public void setCharityClassTest() throws Exception{
    Entity charityEntity = new Entity("Charity");
    charityEntity.setProperty("name", CHARITY_A);
    charityEntity.setProperty("link", LINK_A);
    charityEntity.setProperty("categories", CATEGORIES_A);
    charityEntity.setProperty("description", DESCRIPTION_A);
    charityEntity.setProperty("trendingScore", TRENDINGSCORE_A);
    Charity actual = dbCalls.setCharityClass(charityEntity);
    String charityNameActual = actual.getName();
    assertEquals(charityNameActual, CHARITY_A);
  }
  // Test checks if function converts entity into class object.
  @Test
  public void setTagClassTest() throws Exception{
    Entity tagEntity = new Entity("Tag");
    tagEntity.setProperty("name", TAG_A);
    tagEntity.setProperty("trendingScore", TRENDINGSCORE_A);
    Tag actual = dbCalls.setTagClass(tagEntity);
    String tagNameActual = actual.getName();
    assertEquals(tagNameActual, TAG_A);
  }
  // Test checks if function converts entity into class object.
  @Test
  public void setUsersClassTest() throws Exception{
    Entity userEntity = new Entity("Users");
    userEntity.setProperty("userName", USERNAME_A);
    userEntity.setProperty("email", EMAIL_A);
    userEntity.setProperty("userInterests", USER_INTRESTS_A);
    userEntity.setProperty("charitiesDonatedTo", CHARITIES_DONATED_TO_A);
    Users actual = dbCalls.setUsersClass(userEntity);
    String userNameActual = actual.getUserName();
    assertEquals(userNameActual, USERNAME_A);
  }
  // Test checks if function updates database entity properties.
  @Test
  public void updateCharityTest() throws Exception{
    dbCalls.addCharity(CHARITY_A, LINK_A, CATEGORIES_A, DESCRIPTION_A);
    Charity charity = dbCalls.getCharityByName(CHARITY_A);
    charity.setName("New Name Charity");
    dbCalls.updateCharity(charity);
    Charity actualCharity = dbCalls.getCharityByName("New Name Charity");
    String actualCharityName = actualCharity.getName();
    String expectedCharityName = "New Name Charity";
    assertEquals(actualCharityName, expectedCharityName);
  }
  // Test checks if function updates database entity properties.
  @Test
  public void updateTagTest() throws Exception{
    dbCalls.addTag(TAG_A, TRENDINGSCORE_A);
    Tag tag = dbCalls.getTagByName(TAG_A);
    tag.setName("New Tag");
    dbCalls.updateTag(tag);
    Tag actualTag = dbCalls.getTagByName("New Tag");
    String actualTagName = actualTag.getName();
    String expectedTagName = "New Tag";
    assertEquals(actualTagName, expectedTagName);
  }
  // Test checks if function updates database entity properties.
  @Test
  public void updateUserTest() throws Exception{
    dbCalls.addUser(USERNAME_A, EMAIL_A, USER_INTRESTS_A, CHARITIES_DONATED_TO_A);
    Users user = dbCalls.getUserByUserName(USERNAME_A);
    user.setUserName("newUser");
    dbCalls.updateUser(user);
    Users actualUser = dbCalls.getUserByUserName("newUser");
    String actualUserName = actualUser.getUserName();
    String expectedUserName = "newUser";
    assertEquals(actualUserName, expectedUserName);
  }
}