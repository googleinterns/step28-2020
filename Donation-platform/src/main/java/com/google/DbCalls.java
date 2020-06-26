package com.google;

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
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;


/**
 * Class handling the calls to the datastore database.
 */
public final class DbCalls {

  private DatastoreService datastore;
  // Constructor initializes the datastore variable when called.
  public DbCalls(DatastoreService ds) {
    this.datastore = ds;
  }
  // Function returns all charity objects in the database as a list.
  public Collection<Charity> getAllCharities() throws Exception {
    ArrayList< Charity > charityDataStore = new ArrayList < Charity > ();
    Query query = new Query("Charity");
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable())
        {
            Charity charityData = setCharityClass(entity);
            charityDataStore.add(charityData);
    }
    return charityDataStore;
  }
  // Function returns all the tag objects in the database.
  public Collection<Tag> getAllTags() throws Exception {
    ArrayList< Tag > tagDataStore = new ArrayList < Tag > ();
    Query query = new Query("Tag");
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable())
        {
            Tag tagData = setTagClass(entity);
            tagDataStore.add(tagData);
    }
    return tagDataStore;
  }
  // Function returns all the users in the database.
  public Collection<Users> getAllUsers() throws Exception {
    ArrayList< Users > userDataStore = new ArrayList < Users > ();
    Query query = new Query("Users");
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable())
        {
            Users userData = setUsersClass(entity);
            userDataStore.add(userData);
    }
    return userDataStore;
  }
  // Function adds charity to database.
  // Could be used in conjunction with user charity add form.
  public void addCharity(String name, String link, Collection<Key>  categories, String description) throws Exception{
    Entity charityEntity = new Entity("Charity");
    charityEntity.setProperty("name", name);
    charityEntity.setProperty("link", link);
    charityEntity.setProperty("categories", categories);
    charityEntity.setProperty("description", description);
    charityEntity.setProperty("trendingScore", 0.0);
    datastore.put(charityEntity);
  }
  // Function adds tag to the database.
  public void addTag(String name, Double trendingScore) throws Exception{
    Entity tagEntity = new Entity("Tag");
    tagEntity.setProperty("name", name);
    tagEntity.setProperty("trendingScore", trendingScore);
    datastore.put(tagEntity);
  }
  // Function adds user to the database.
  public void addUser(String userName, String email,
              Collection<Key> userInterests, Collection<Key>  charitiesDonatedTo) throws Exception{
    Entity userEntity = new Entity("Users");
    userEntity.setProperty("userName", userName);
    userEntity.setProperty("email", email);
    userEntity.setProperty("userInterests", userInterests);
    userEntity.setProperty("charitiesDonatedTo", charitiesDonatedTo);
    datastore.put(userEntity);
  }
  // Function gets one charity by name. Returns error if multipe charities
  // have the same name.
  public Charity getCharityByName(String name) throws Exception {
    Query query =
    new Query("Charity")
        .setFilter(new FilterPredicate("name", FilterOperator.EQUAL, name));
    PreparedQuery pq = datastore.prepare(query);
    Entity result = pq.asSingleEntity();
    Charity charityResult = setCharityClass(result);
    return charityResult;
  }
  // Function returns all charities under a category tag.
  public Collection<Charity> getCharitiesByTag(String name) throws Exception {
    ArrayList< Charity > charityDataStore = new ArrayList < Charity > ();
    Query query = new Query("Charity");
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable())
        {
            Charity charityData = setCharityClass(entity);
            // Read the categories assigned to the charity
            Collection<Key> categoryKeys = charityData.getCategories();
            if (categoryKeys != null){
              for (Key key : categoryKeys ){
                Entity tagEntity = datastore.get(key);
                Tag tagClass = setTagClass(tagEntity);
                String tagName = tagClass.getName();
                // Check to see if category contains tag searched for.
                if (tagName.equals(name)){
                  charityDataStore.add(charityData);
                }
              } 
            } 
    }
    // Return all charity objects containing tag.
    return charityDataStore;
  }
  // Function returns tag object matching name passed in.
  public Tag getTagByName(String name) throws Exception{
    Query query =
    new Query("Tag")
        .setFilter(new FilterPredicate("name", FilterOperator.EQUAL, name));
    PreparedQuery pq = datastore.prepare(query);
    Entity result = pq.asSingleEntity();
    Tag tagResult = setTagClass(result);
    return tagResult;
  }
  //Function that returns user from user name.
  public Users getUserByUserName(String userName) throws Exception{
    Query query =
    new Query("Users")
        .setFilter(new FilterPredicate("userName", FilterOperator.EQUAL, userName));
    PreparedQuery pq = datastore.prepare(query);
    Entity result = pq.asSingleEntity();
    Users userResult = setUsersClass(result);
    return userResult;
  }
  // Function that returns user from email.
  public Users getUserByEmail(String email) throws Exception{
    Query query =
    new Query("Users")
        .setFilter(new FilterPredicate("email", FilterOperator.EQUAL, email));
    PreparedQuery pq = datastore.prepare(query);
    Entity result = pq.asSingleEntity();
    Users userResult = setUsersClass(result);
    return userResult;
  }

  // Function turns entity into class.
  public Charity setCharityClass(Entity entity){
    Key id = (Key) entity.getKey();
    String name = (String) entity.getProperty("name");
    String link = (String) entity.getProperty("link");
    List<Key> categories = (List<Key>) entity.getProperty("categories");
    String description = (String) entity.getProperty("description");
    Double trendingScore = (Double) entity.getProperty("trendingScore");
    // Converts data to charity object.
    Charity charityData = new Charity(id, name, link, categories, description, trendingScore);
    return charityData;
  }
  // Function turns entity into class.
  public Tag setTagClass(Entity entity){
    Key id = (Key) entity.getKey();
    String name = (String) entity.getProperty("name");
    Double trendingScore = (Double) entity.getProperty("trendingScore");
    // Converts data to tag object.
    Tag tagData = new Tag(id, name, trendingScore);
    return tagData;
  }
  // Function turns entity into class.
  public Users setUsersClass(Entity entity){
    Key id = (Key) entity.getKey();
    String userName = (String) entity.getProperty("userName");
    String email = (String) entity.getProperty("email");
    List<Key>  userInterests = (List<Key> ) entity.getProperty("userInterests");
    List<Key>  charitiesDonatedTo = (List<Key> ) entity.getProperty("charitiesDonatedTo");
    // Converts data to users object.
    Users userData = new Users(id, userName, email, userInterests, charitiesDonatedTo);
    return userData;
  }
  // Function takes modified class and updates database.
  public void updateCharity(Charity charity) throws Exception{
    Key id = charity.getId();
    Entity charityEntity = datastore.get(id);
    charityEntity.setProperty("name", charity.getName());
    charityEntity.setProperty("link", charity.getLink());
    charityEntity.setProperty("categories", charity.getCategories());
    charityEntity.setProperty("description", charity.getDescription());
    charityEntity.setProperty("trendingScore", charity.getTrendingScoreCharity());
    datastore.put(charityEntity);
  }
  // Function takes modified class and updates database.
  public void updateTag(Tag tag) throws Exception{
    Key id = tag.getId();
    Entity tagEntity = datastore.get(id);
    tagEntity.setProperty("name", tag.getName());
    tagEntity.setProperty("trendingScore", tag.getTrendingScoreTag());
    datastore.put(tagEntity);
  }
  // Function takes modified class and updates database.
  public void updateUser(Users user) throws Exception{
    Key id = user.getId();
    Entity userEntity = datastore.get(id);
    userEntity.setProperty("userName", user.getUserName());
    userEntity.setProperty("email", user.getEmail());
    userEntity.setProperty("userInterests", user.getUserInterests());
    userEntity.setProperty("charitiesDonatedTo", user.getCharitiesDonatedTo());
    datastore.put(userEntity);
  }
}