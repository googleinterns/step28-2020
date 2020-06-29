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

  private static final String CHARITY = "Charity";
  private static final String TRENDING_SCORE = "trendingScore";
  private static final String TAG = "Tag";
  private static final String USERS = "Users";
  private static final String NAME = "name";
  private static final String LINK = "link";
  private static final String CATEGORIES = "categories";
  private static final String DESCRIPTION = "description";
  private static final String USERNAME = "userName";
  private static final String USER_RATING = "userRating";
  private static final String EMAIL = "email";
  private static final String USER_INTERESTS = "userInterests";
  private static final String CHARITIES_DONATED_TO = "charitiesDonatedTo";

  // Function returns all charity objects in the database as a list.
  public Collection<Charity> getAllCharities() throws Exception {
    ArrayList< Charity > charityDataStore = new ArrayList < Charity > ();
    Query query = new Query(CHARITY);
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
    Query query = new Query(TAG);
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
    Query query = new Query(USERS);
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
    Entity charityEntity = new Entity(CHARITY);
    charityEntity.setProperty(NAME, name);
    charityEntity.setProperty(LINK, link);
    charityEntity.setProperty(CATEGORIES, categories);
    charityEntity.setProperty(DESCRIPTION, description);
    charityEntity.setProperty(TRENDING_SCORE, 0.0);
    charityEntity.setProperty(USER_RATING, 0.0);
    datastore.put(charityEntity);
  }
  // Function adds tag to the database.
  public void addTag(String name, Double trendingScore) throws Exception{
    Entity tagEntity = new Entity(TAG);
    tagEntity.setProperty(NAME, name);
    tagEntity.setProperty(TRENDING_SCORE, trendingScore);
    datastore.put(tagEntity);
  }
  // Function adds user to the database.
  public void addUser(String userName, String email,
              Collection<Key> userInterests, Collection<Key>  charitiesDonatedTo) throws Exception{
    Entity userEntity = new Entity(USERS);
    userEntity.setProperty(USERNAME, userName);
    userEntity.setProperty(EMAIL, email);
    userEntity.setProperty(USER_INTERESTS, userInterests);
    userEntity.setProperty(CHARITIES_DONATED_TO, charitiesDonatedTo);
    datastore.put(userEntity);
  }
  // Function returns charity object from ID.
  public Charity getCharityById(Key charityId) throws Exception{
    Entity charityEntity = datastore.get(charityId);
    Charity charityClass = setCharityClass(charityEntity);
    return charityClass;
  }
  // Function gets one charity by name. Returns error if multipe charities
  // have the same name.
  public Charity getCharityByName(String name) throws Exception {
    Query query =
    new Query(CHARITY)
        .setFilter(new FilterPredicate(NAME, FilterOperator.EQUAL, name));
    PreparedQuery pq = datastore.prepare(query);
    Entity result = pq.asSingleEntity();
    Charity charityResult = setCharityClass(result);
    return charityResult;
  }
  // Function returns all charities under a category tag.
  public Collection<Charity> getCharitiesByTag(String name) throws Exception {
    ArrayList< Charity > charityDataStore = new ArrayList < Charity > ();
    Query query = new Query(CHARITY);
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
  // Function returns tag object from ID.
  public Tag getTagById(Key tagId) throws Exception{
    Entity tagEntity = datastore.get(tagId);
    Tag tagClass = setTagClass(tagEntity);
    return tagClass;
  }
  // Function returns tag object matching name passed in.
  public Tag getTagByName(String name) throws Exception{
    Query query =
    new Query(TAG)
        .setFilter(new FilterPredicate(NAME, FilterOperator.EQUAL, name));
    PreparedQuery pq = datastore.prepare(query);
    Entity result = pq.asSingleEntity();
    Tag tagResult = setTagClass(result);
    return tagResult;
  }
  //Function that returns user from user name.
  public Users getUserByUserName(String userName) throws Exception{
    Query query =
    new Query(USERS)
        .setFilter(new FilterPredicate(USERNAME, FilterOperator.EQUAL, userName));
    return setUsersClass(datastore.prepare(query).asSingleEntity());
  }
  // Function that returns user from email.
  public Users getUserByEmail(String email) throws Exception{
    Query query =
    new Query(USERS)
        .setFilter(new FilterPredicate(EMAIL, FilterOperator.EQUAL, email));
    return setUsersClass(datastore.prepare(query).asSingleEntity());
  }

  // Function turns entity into class.
  public Charity setCharityClass(Entity entity){
    Key id = (Key) entity.getKey();
    String name = (String) entity.getProperty(NAME);
    String link = (String) entity.getProperty(LINK);
    List<Key> categories = (List<Key>) entity.getProperty(CATEGORIES);
    String description = (String) entity.getProperty(DESCRIPTION);
    Double trendingScore = (Double) entity.getProperty(TRENDING_SCORE);
    Double userRating = (Double) entity.getProperty(USER_RATING);
    // Converts data to charity object.
    Charity charityData = new Charity(id, name, link, categories, description, trendingScore, userRating);
    return charityData;
  }
  // Function turns entity into class.
  public Tag setTagClass(Entity entity){
    Key id = (Key) entity.getKey();
    String name = (String) entity.getProperty(NAME);
    Double trendingScore = (Double) entity.getProperty(TRENDING_SCORE);
    // Converts data to tag object.
    Tag tagData = new Tag(id, name, trendingScore);
    return tagData;
  }
  // Function turns entity into class.
  public Users setUsersClass(Entity entity){
    Key id = (Key) entity.getKey();
    String userName = (String) entity.getProperty(USERNAME);
    String email = (String) entity.getProperty(EMAIL);
    List<Key>  userInterests = (List<Key> ) entity.getProperty(USER_INTERESTS);
    List<Key>  charitiesDonatedTo = (List<Key> ) entity.getProperty(CHARITIES_DONATED_TO);
    // Converts data to users object.
    Users userData = new Users(id, userName, email, userInterests, charitiesDonatedTo);
    return userData;
  }
  // Function takes modified class and updates database.
  public void updateCharity(Charity charity) throws Exception{
    Key id = charity.getId();
    Entity charityEntity = datastore.get(id);
    charityEntity.setProperty(NAME, charity.getName());
    charityEntity.setProperty(LINK, charity.getLink());
    charityEntity.setProperty(CATEGORIES, charity.getCategories());
    charityEntity.setProperty(DESCRIPTION, charity.getDescription());
    charityEntity.setProperty(TRENDING_SCORE, charity.getTrendingScoreCharity());
    charityEntity.setProperty(USER_RATING, charity.getUserRating());
    datastore.put(charityEntity);
  }
  // Function takes modified class and updates database.
  public void updateTag(Tag tag) throws Exception{
    Key id = tag.getId();
    Entity tagEntity = datastore.get(id);
    tagEntity.setProperty(NAME, tag.getName());
    tagEntity.setProperty(TRENDING_SCORE, tag.getTrendingScoreTag());
    datastore.put(tagEntity);
  }
  // Function takes modified class and updates database.
  public void updateUser(Users user) throws Exception{
    Key id = user.getId();
    Entity userEntity = datastore.get(id);
    userEntity.setProperty(USERNAME, user.getUserName());
    userEntity.setProperty(EMAIL, user.getEmail());
    userEntity.setProperty(USER_INTERESTS, user.getUserInterests());
    userEntity.setProperty(CHARITIES_DONATED_TO, user.getCharitiesDonatedTo());
    datastore.put(userEntity);
  }
}