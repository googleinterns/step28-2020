<<<<<<< HEAD
package com.google.servlets;
=======
package com.google;
>>>>>>> database-testing

import com.google.model.Charity;
import com.google.model.Tag;
import com.google.model.Users; 
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Collection;
import java.util.Collections;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;


/**
 * Class handling the calls to the datastore database.
 */
public final class DbCalls {

  private DatastoreService datastore;
<<<<<<< HEAD
  // Function initializes the datastore variable when called.
  public void initializeDatastore(){
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  }
  // Function returns all charity objects in the database as a list.
  public Collection<Charity> getAllCharities() throws Exception {
    initializeDatastore();
=======
  // Constructor initializes the datastore variable when called.
  public DbCalls(DatastoreService ds) {
    this.datastore = ds;
  }
  // Function returns all charity objects in the database as a list.
  public Collection<Charity> getAllCharities() throws Exception {
>>>>>>> database-testing
    ArrayList< Charity > charityDataStore = new ArrayList < Charity > ();
    Query query = new Query("Charity");
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable())
        {
            Key id = (Key) entity.getKey();
            String name = (String) entity.getProperty("name");
            String link = (String) entity.getProperty("link");
            List<Key> categories = (List<Key>) entity.getProperty("categories");
            String description = (String) entity.getProperty("description");
            Double trendingScore = (Double) entity.getProperty("trendingScore");
            // Convertes data to charity object.
            Charity charityData = new Charity(id, name, link, categories, description, trendingScore);
            charityDataStore.add(charityData);
    }
    return charityDataStore;
  }
  // Function returns all the tag objects in the database.
  public Collection<Tag> getAllTags() throws Exception {
<<<<<<< HEAD
    initializeDatastore();
=======
>>>>>>> database-testing
    ArrayList< Tag > tagDataStore = new ArrayList < Tag > ();
    Query query = new Query("Tag");
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable())
        {
            Key id = (Key) entity.getKey();
            String name = (String) entity.getProperty("name");
            Double trendingScore = (Double) entity.getProperty("trendingScore");
            // Convertes data to tag object.
            Tag tagData = new Tag(id, name, trendingScore);
            tagDataStore.add(tagData);
    }
    return tagDataStore;
  }
  // Function returns all the users in the database.
  public Collection<Users> getAllUsers() throws Exception {
<<<<<<< HEAD
    initializeDatastore();
=======
>>>>>>> database-testing
    ArrayList< Users > userDataStore = new ArrayList < Users > ();
    Query query = new Query("Users");
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable())
        {
            Key id = (Key) entity.getKey();
            String userName = (String) entity.getProperty("userName");
            String email = (String) entity.getProperty("email");
            List<Key>  userInterests = (List<Key> ) entity.getProperty("userInterests");
            List<Key>  charitiesDonatedTo = (List<Key> ) entity.getProperty("charitiesDonatedTo");
            // Convertes data to users object.
            Users userData = new Users(id, userName, email, userInterests, charitiesDonatedTo);
            userDataStore.add(userData);
    }
    return userDataStore;
  }
  // Function adds charity to database.
  // Could be used in conjunction with user charity add form.
<<<<<<< HEAD
  public void addCharity(String name, String link,
              Collection<Key>  categories, String description){
    initializeDatastore();
=======
<<<<<<<< HEAD:Donation-platform/src/main/java/com/google/DbCalls.java
  public void addCharity(String name, String link, Collection<Key>  categories, String description) throws Exception{
========
  public void addCharity(String name, String link,
              Collection<Key>  categories, String description){
    initializeDatastore();
>>>>>>>> database-testing:Donation-platform/src/main/java/com/google/servlets/DbCalls.java
>>>>>>> database-testing
    Entity charityEntity = new Entity("Charity");
    charityEntity.setProperty("name", name);
    charityEntity.setProperty("link", link);
    charityEntity.setProperty("categories", categories);
    charityEntity.setProperty("description", description);
<<<<<<< HEAD
    charityEntity.setProperty("trendingScore", 0);
    datastore.put(charityEntity);
  }
  // Function adds tag to the database.
  public void addTag(String name, Key trendingScore){
    initializeDatastore();
    Entity tagEntity = new Entity("Tag");
    tagEntity.setProperty("name", name);
    tagEntity.setProperty("trendingScore", 0);
=======
    charityEntity.setProperty("trendingScore", 0.0);
    datastore.put(charityEntity);
  }
  // Function adds tag to the database.
<<<<<<<< HEAD:Donation-platform/src/main/java/com/google/DbCalls.java
  public void addTag(String name, Double trendingScore) throws Exception{
========
  public void addTag(String name, Key trendingScore){
    initializeDatastore();
>>>>>>>> database-testing:Donation-platform/src/main/java/com/google/servlets/DbCalls.java
    Entity tagEntity = new Entity("Tag");
    tagEntity.setProperty("name", name);
    tagEntity.setProperty("trendingScore", trendingScore);
>>>>>>> database-testing
    datastore.put(tagEntity);
  }
  // Function adds user to the database.
  public void addUser(String userName, String email,
<<<<<<< HEAD
              List<Key> userInterests, List<Key>  charitiesDonatedTo){
    initializeDatastore();
=======
<<<<<<<< HEAD:Donation-platform/src/main/java/com/google/DbCalls.java
              Collection<Key> userInterests, Collection<Key>  charitiesDonatedTo) throws Exception{
========
              List<Key> userInterests, List<Key>  charitiesDonatedTo){
    initializeDatastore();
>>>>>>>> database-testing:Donation-platform/src/main/java/com/google/servlets/DbCalls.java
>>>>>>> database-testing
    Entity userEntity = new Entity("Users");
    userEntity.setProperty("userName", userName);
    userEntity.setProperty("email", email);
    userEntity.setProperty("userInterests", userInterests);
    userEntity.setProperty("charitiesDonatedTo", charitiesDonatedTo);
    datastore.put(userEntity);
  }
  // Function updates trending score of a charity in the database.
  public void updateCharityTrendingScore(Key id, Double trendingScore) throws Exception{
<<<<<<< HEAD
    initializeDatastore();
=======
>>>>>>> database-testing
    Entity charityEntity = datastore.get(id);
    charityEntity.setProperty("trendingScore", trendingScore);
    datastore.put(charityEntity);
  }
  // Function updates trending score of a tag in the database.
  public void updateTagTrendingScore(Key id, Double trendingScore) throws Exception{
<<<<<<< HEAD
    initializeDatastore();
=======
>>>>>>> database-testing
    Entity charityEntity = datastore.get(id);
    charityEntity.setProperty("trendingScore", trendingScore);
    datastore.put(charityEntity);
  }

}