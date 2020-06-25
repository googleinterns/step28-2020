
package com.google.sps;

import com.google.sps.servlets.DbCalls;
import com.google.sps.model.Charity;
import com.google.sps.model.Tag;
import com.google.sps.model.Users; 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.sps.model.Charity;
import com.google.sps.model.Tag;
import com.google.sps.model.Users;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static org.junit.Assert.assertEquals;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;



@RunWith(JUnit4.class)
public class DbCallsTest {

  // private final LocalServiceTestHelper helper =
  //     new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  // Some charities that we can use in our tests.
  private static final String CHARITY_A = "Charity A";
  private static final String LINK_A = "www.test.com";
  private static final Collection<Integer> CATEGORIES_A = Collections.emptyList();
  private static final String DESCRIPTION_A = "Very testy description.";
  private static final Double TRENDINGSCORE_A = 0.0;
  private Key KEY_A = KeyFactory.createKey("Charity", 1234);

  // @Before
  // public void setUp() {
  //   helper.setUp();
  // }

  // @After
  // public void tearDown() {
  //   helper.tearDown();
  // }

  @Test
  public void addCharityTest() throws Exception {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity charityEntity = new Entity("Charity");
    charityEntity.setProperty("name", CHARITY_A);
    charityEntity.setProperty("link", LINK_A);
    charityEntity.setProperty("categories", CATEGORIES_A);
    charityEntity.setProperty("description", DESCRIPTION_A);
    charityEntity.setProperty("trendingScore", TRENDINGSCORE_A);
    ds.put(charityEntity);
    // Collection<Charity> actual = databaseQuery.getAllCharities();
    // Collection<Charity> expected = Arrays.asList(new Charity(KEY_A, CHARITY_A, LINK_A, CATEGORIES_A, DESCRIPTION_A, TRENDINGSCORE_A));
    Assert.assertEquals(2, ds.prepare(new Query("Charity")).countEntities(withLimit(10)));
  }

}