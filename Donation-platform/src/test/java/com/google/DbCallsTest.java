package com.google;

import com.google.model.Charity;
import com.google.model.Tag;
import com.google.model.Cause;
import com.google.model.Users;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static org.junit.Assert.assertEquals;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static com.googlecode.objectify.ObjectifyService.ofy;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.cache.AsyncCacheFilter;
import com.googlecode.objectify.util.Closeable;
import java.lang.Long;
import org.junit.BeforeClass;


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
  private static final Cause CAUSE_A = null;
  private static final Cause CAUSE_B = null;
  private static final String EMAIL_A = "Email A";
  private static final String EMAIL_B = "Email B";
  private static final String USERNAME_A = "Username A";
  private static final String USERNAME_B = "Username B";
  private static final String LINK_A = "www.test.com";
  private static final String LINK_B = "www.testb.com";
  private static final String IMGSRC_A = "www.testimageb.com";
  private static final String IMGSRC_B = "www.testimagea.com";
  private static final String USER_ID = "userid";
  private static final Collection<Tag> CATEGORIES_A = Collections.emptyList();
  private static final Collection<Tag> CATEGORIES_B = Collections.emptyList();
  private static final Collection<Tag> CATEGORIES_C = Collections.emptyList();
  private static final Collection<Tag> USER_INTRESTS_A = Collections.emptyList();
  private static final Collection<Tag> USER_INTRESTS_B = Collections.emptyList();
  private static final Collection<Charity> CHARITIES_DONATED_TO_A = Collections.emptyList();
  private static final Collection<Charity> CHARITIES_DONATED_TO_B = Collections.emptyList();
  private static final String DESCRIPTION_A = "Very testy description.";
  private static final String DESCRIPTION_B = "Not a Very testy description.";
  private static final double TRENDINGSCORE_A = 0.0;
  private static final double TRENDINGSCORE_B = 0.0;
  private static final double USER_RATING_A = 0.0;
  private static final String PLACEHOLDER_STRING = "placeholder";
  // Some variables for the query strings.
  private static final String CHARITY = "Charity";
  private static final String TRENDING_SCORE = "trendingScore";
  private static final String TAG = "Tag";
  private static final String CAUSE = "Cause";
  private static final String USERS = "Users";
  private static final String NAME = "name";
  private static final String LINK = "link";
  private static final String IMGSRC = "imgSrc";
  private static final String CATEGORIES = "categories";
  private static final String DESCRIPTION = "description";
  private static final String USERNAME = "userName";
  private static final String USER_RATING = "userRating";
  private static final String EMAIL = "email";
  private static final String USER_INTERESTS = "userInterests";
  private static final String CHARITIES_DONATED_TO = "charitiesDonatedTo";
  
  protected Closeable session;

  // Initializes objectify class entities within its context before tests are run.
  @BeforeClass
    public static void setUpBeforeClass() {
        // Reset the Factory so that all translators work properly.
        ObjectifyService.setFactory(new ObjectifyFactory());
        ObjectifyService.register(Charity.class);
        ObjectifyService.register(Tag.class);
        ObjectifyService.register(Cause.class);
        ObjectifyService.register(Users.class);
    }
  // Sets up local datastore service and dbCalls object.
  @Before
  public void setUp() {
    helper.setUp();
    dbCalls = new DbCalls(ds);
    this.session = ObjectifyService.begin(); 
  }
  // Cleans up test.
  @After
  public void tearDown() {
    AsyncCacheFilter.complete();
    this.session.close();
    helper.tearDown();
  }
  // Test checks if function adds charity to the database.
  @Test
  public void addCharityTest() throws Exception {
    dbCalls.addCharity(CHARITY_A, LINK_A, IMGSRC_A, CATEGORIES_A, CAUSE_A, DESCRIPTION_A, 0.0);
    assertEquals(1, ds.prepare(new Query(CHARITY)).countEntities(withLimit(10)));
  }
  // Test checks if function adds tag to the database.
  @Test
  public void addTagTest() throws Exception {
    dbCalls.addTag(TAG_A, TRENDINGSCORE_A, IMGSRC);
    assertEquals(1, ds.prepare(new Query(TAG)).countEntities(withLimit(10)));
  }
  // Test checks if function adds user to the database.
  @Test
  public void addUserTest() throws Exception {
    dbCalls.addUser(USER_ID, USERNAME_A, EMAIL_A, USER_INTRESTS_A, CHARITIES_DONATED_TO_A);
    assertEquals(1, ds.prepare(new Query(USERS)).countEntities(withLimit(10)));
  }
  // Test checks if function returns all charities in database.
  @Test
  public void getAllCharitiesTest() throws Exception {
    dbCalls.addCharity(CHARITY_A, LINK_A, IMGSRC_A, CATEGORIES_A, CAUSE_A, DESCRIPTION_A,  0.0);
    dbCalls.addCharity(CHARITY_B, LINK_B, IMGSRC_B, CATEGORIES_B, CAUSE_B, DESCRIPTION_B, 0.0);
    Collection<Charity> expected = dbCalls.getAllCharities();
    assertEquals(expected.size(), ds.prepare(new Query(CHARITY)).countEntities(withLimit(10)));
  }
  // Test checks if function returns all tags in database.
  @Test
  public void getAllTagsTest() throws Exception {
    dbCalls.addTag(TAG_A, TRENDINGSCORE_A, IMGSRC);
    dbCalls.addTag(TAG_B, TRENDINGSCORE_B, IMGSRC);
    Collection<Tag> expected = dbCalls.getAllTags();
    assertEquals(expected.size(), ds.prepare(new Query(TAG)).countEntities(withLimit(10)));
  }
  // Test checks if function returns all users in database.
  @Test
  public void getAllUsersTest() throws Exception {
    dbCalls.addUser(USER_ID, USERNAME_A, EMAIL_A, USER_INTRESTS_A, CHARITIES_DONATED_TO_A);
    dbCalls.addUser(USER_ID, USERNAME_B, EMAIL_B, USER_INTRESTS_B, CHARITIES_DONATED_TO_B);
    Collection<Users> expected = dbCalls.getAllUsers();
    assertEquals(expected.size(), ds.prepare(new Query(USERS)).countEntities(withLimit(10)));
  }
  // Test checks if object returned corresponds to name passed in.
  @Test
  public void getCharityByNameTest() throws Exception {
    dbCalls.addCharity(CHARITY_A, LINK_A, IMGSRC_A, CATEGORIES_A, CAUSE_A, DESCRIPTION_A, 0.0);
    dbCalls.addCharity(CHARITY_B, LINK_B, IMGSRC_B, CATEGORIES_B, CAUSE_B, DESCRIPTION_B, 0.0);
    Charity expected = dbCalls.getCharityByName(CHARITY_A);
    assertEquals(expected.getName(), CHARITY_A);
  }
  // Test checks if object returned corresponds to name passed in.
  @Test
  public void getTagByNameTest() throws Exception {
    dbCalls.addTag(PLACEHOLDER_STRING, 6.0, IMGSRC);
    Tag actualTag = dbCalls.getTagByName(PLACEHOLDER_STRING);
    assertEquals(PLACEHOLDER_STRING, actualTag.getName());
  }
  // Test checks if function returns all charities that contain certain tag.
  @Test
  public void getCharitiesByTagTest() throws Exception {
    dbCalls.addTag(PLACEHOLDER_STRING, 6.0, IMGSRC);
    Tag blmTag = dbCalls.getTagByName(PLACEHOLDER_STRING);
    Collection<Tag> CATEGORY_A_WITH_TAG = Arrays.asList(blmTag);
    Collection<Tag> CATEGORY_B_WITH_TAG = Arrays.asList(blmTag);
    dbCalls.addCharity(CHARITY_A, LINK_A, IMGSRC_A, CATEGORY_A_WITH_TAG, CAUSE_A, DESCRIPTION_A, 0.0);
    dbCalls.addCharity(CHARITY_B, LINK_B, IMGSRC_B, CATEGORY_B_WITH_TAG, CAUSE_B, DESCRIPTION_B, 0.0);
    dbCalls.addCharity(CHARITY_C, LINK_B, IMGSRC_B, CATEGORIES_C, CAUSE_A, DESCRIPTION_B, 0.0);
    Collection<Charity> expected = dbCalls.getCharitiesByTag(PLACEHOLDER_STRING);
    assertEquals(expected.size(), 2);
  }
  // Test checks if function returns object when queried with username.
  @Test
  public void getUserByUserNameTest() throws Exception {
    dbCalls.addUser(USER_ID, USERNAME_A, EMAIL_A, USER_INTRESTS_A, CHARITIES_DONATED_TO_A);
    Users actualUser = dbCalls.getUserByUserName(USERNAME_A);
    assertEquals(actualUser.getUserName(), USERNAME_A);
  }
  // Test checks if function returns object when queried with email.
  @Test
  public void getUserByEmailTest() throws Exception {
    dbCalls.addUser(USER_ID, USERNAME_A, EMAIL_A, USER_INTRESTS_A, CHARITIES_DONATED_TO_A);
    Users actualUser = dbCalls.getUserByEmail(EMAIL_A);
    assertEquals(actualUser.getUserName(), USERNAME_A);
  }
  // Test checks if function updates database entity properties.
  @Test
  public void updateCharityTest() throws Exception {
    dbCalls.addCharity(CHARITY_A, LINK_A, IMGSRC_A, CATEGORIES_A, CAUSE_A, DESCRIPTION_A, 0.0);
    Charity charity = dbCalls.getCharityByName(CHARITY_A);
    charity.setName(PLACEHOLDER_STRING);
    dbCalls.updateCharity(charity);
    Charity actualCharity = dbCalls.getCharityByName(PLACEHOLDER_STRING);
    assertEquals(actualCharity.getName(), PLACEHOLDER_STRING);
  }
  // Test checks if function updates database entity properties.
  @Test
  public void updateTagTest() throws Exception {
    dbCalls.addTag(TAG_A, TRENDINGSCORE_A, IMGSRC_A);
    Tag tag = dbCalls.getTagByName(TAG_A);
    tag.setName(PLACEHOLDER_STRING);
    dbCalls.updateTag(tag);
    Tag actualTag = dbCalls.getTagByName(PLACEHOLDER_STRING);
    assertEquals(actualTag.getName(), PLACEHOLDER_STRING);
  }
  // Test checks if function updates database entity properties.
  @Test
  public void updateUserTest() throws Exception {
    dbCalls.addUser(USER_ID, USERNAME_A, EMAIL_A, USER_INTRESTS_A, CHARITIES_DONATED_TO_A);
    Users user = dbCalls.getUserByUserName(USERNAME_A);
    user.setUserName(PLACEHOLDER_STRING);
    dbCalls.updateUser(user);
    Users actualUser = dbCalls.getUserByUserName(PLACEHOLDER_STRING);
    assertEquals(actualUser.getUserName(), PLACEHOLDER_STRING);
  }
  // Test checks if function gets charity by ID from database.
  @Test
  public void getCharityByIdTest() throws Exception {
    dbCalls.addCharity(CHARITY_A, LINK_A, IMGSRC_A, CATEGORIES_A, CAUSE_A, DESCRIPTION_A, 0.0);
    Charity expectedCharity = dbCalls.getCharityByName(CHARITY_A);
    Long expectedCharityId = expectedCharity.getId();
    Charity actualCharity = dbCalls.getCharityById(expectedCharityId);
    assertEquals(expectedCharity.getName(), actualCharity.getName());
  }
  // Test checks if function gets Tag by ID from database.
  @Test
  public void getTagByIdTest() throws Exception {
    dbCalls.addTag(TAG_A, TRENDINGSCORE_A, IMGSRC_A);
    Tag expectedTag = dbCalls.getTagByName(TAG_A);
    Long expectedTagId = expectedTag.getId();
    Tag actualTag = dbCalls.getTagById(expectedTagId);
    assertEquals(expectedTag.getId(), actualTag.getId());
  }
}
