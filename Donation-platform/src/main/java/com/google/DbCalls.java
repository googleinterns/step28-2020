package com.google;

import com.google.model.Charity;
import com.google.model.Tag;
import com.google.model.Users;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;

/** Class handling the calls to the datastore database. */
public final class DbCalls
{
    private DatastoreService datastore;
    // Constructor initializes the datastore variable when called.
    public DbCalls(DatastoreService ds)
    {
        this.datastore = ds;
    }

    private static final String CHARITY = "Charity";
    private static final String TRENDING_SCORE = "trendingScore";
    private static final String TAG = "Tag";
    private static final String USERS = "Users";
    private static final String NAME = "name";
    private static final String LINK = "link";
    private static final String IMGSRC = "imgsrc";
    private static final String CATEGORIES = "categories";
    private static final String DESCRIPTION = "description";
    private static final String USERNAME = "userName";
    private static final String USER_RATING = "userRating";
    private static final String EMAIL = "email";
    private static final String USER_INTERESTS = "userInterests";
    private static final String CHARITIES_DONATED_TO = "charitiesDonatedTo";

    // Function returns all charity objects in the database as a list.
    public Collection<Charity> getAllCharities() throws Exception
    {
        ArrayList<Charity> charityDataStore = new ArrayList<Charity>();
        for (Entity entity : datastore.prepare(new Query(CHARITY)).asIterable())
        {
            charityDataStore.add(setCharityClass(entity));
        }
        return charityDataStore;
    }
    // Function returns all the tag objects in the database.
    public Collection<Tag> getAllTags() throws Exception
    {
        ArrayList<Tag> tagDataStore = new ArrayList<Tag>();
        for (Entity entity : datastore.prepare(new Query(TAG)).asIterable())
        {
            tagDataStore.add(setTagClass(entity));
        }
        return tagDataStore;
    }
    // Function returns all the users in the database.
    public Collection<Users> getAllUsers() throws Exception
    {
        ArrayList<Users> userDataStore = new ArrayList<Users>();
        for (Entity entity : datastore.prepare(new Query(USERS)).asIterable())
        {
            userDataStore.add(setUsersClass(entity));
        }
        return userDataStore;
    }
    // Function adds charity to database.
    // Could be used in conjunction with user charity add form.
    public void addCharity(
        String name, String link, String imgsrc, Collection<Tag> categories, String description)
    throws Exception
    {
        Transaction txn = datastore.beginTransaction();
        try
        {
            Entity charityEntity = new Entity(CHARITY);
            charityEntity.setProperty(NAME, name);
            charityEntity.setProperty(LINK, link);
            charityEntity.setProperty(IMGSRC, imgsrc);
            charityEntity.setProperty(CATEGORIES, getTagIdsByObject(categories));
            charityEntity.setProperty(DESCRIPTION, description);
            charityEntity.setProperty(TRENDING_SCORE, 0.0);
            charityEntity.setProperty(USER_RATING, 0.0);
            datastore.put(txn, charityEntity);
            txn.commit();
        }
        finally
        {
            if (txn.isActive())
            {
                txn.rollback();
            }
        }
    }
    // Function adds tag to the database.
    public void addTag(String name, double trendingScore, String imgSrc) throws Exception
    {
        Transaction txn = datastore.beginTransaction();
        try
        {
            Entity tagEntity = new Entity(TAG);
            tagEntity.setProperty(NAME, name);
            tagEntity.setProperty(TRENDING_SCORE, trendingScore);
            tagEntity.setProperty(IMGSRC, imgSrc);
            datastore.put(txn, tagEntity);
            txn.commit();
        }
        finally
        {
            if (txn.isActive())
            {
                txn.rollback();
            }
        }
    }
    // Function adds user to the database.
    public void addUser(
        String userId,
        String userName,
        String email,
        Collection<Tag> userInterests,
        Collection<Charity> charitiesDonatedTo)
    throws Exception
    {
        Transaction txn = datastore.beginTransaction();
        try
        {
            Entity userEntity = new Entity(USERS, userId);
            userEntity.setProperty(USERNAME, userName);
            userEntity.setProperty(EMAIL, email);
            userEntity.setProperty(USER_INTERESTS, userInterests);
            userEntity.setProperty(CHARITIES_DONATED_TO, charitiesDonatedTo);
            datastore.put(userEntity);
            txn.commit();
        }
        finally
        {
            if (txn.isActive())
            {
                txn.rollback();
            }
        }
    }
    // Function returns charity object from ID.
    public Charity getCharityById(Key charityId) throws Exception
    {
        return setCharityClass(datastore.get(charityId));
    }
    // Function gets one charity by name. Returns error if multipe charities
    // have the same name.
    public Charity getCharityByName(String name) throws Exception
    {
        Query query =
            new Query(CHARITY).setFilter(new FilterPredicate(NAME, FilterOperator.EQUAL, name));
        return setCharityClass(datastore.prepare(query).asSingleEntity());
    }
    // Function returns all charities under a category tag.
    public Collection<Charity> getCharitiesByTag(String name) throws Exception
    {
        ArrayList<Charity> charityDataStore = new ArrayList<Charity>();
        for (Entity entity : datastore.prepare(new Query(CHARITY)).asIterable())
        {
            // Read the categories assigned to the charity
            if (setCharityClass(entity).getCategories() != null)
            {
                for (Tag tag : setCharityClass(entity).getCategories())
                {
                    // Check to see if category contains tag searched for.
                    if (tag.getName().equals(name))
                    {
                        charityDataStore.add(setCharityClass(entity));
                    }
                }
            }
        }
        // Return all charity objects containing tag.
        return charityDataStore;
    }
    // Function returns tag object from ID.
    public Tag getTagById(Key tagId) throws Exception
    {
        return setTagClass(datastore.get(tagId));
    }
    // Function returns tag object matching name passed in.
    public Tag getTagByName(String name) throws Exception
    {
        Query query = new Query(TAG).setFilter(new FilterPredicate(NAME, FilterOperator.EQUAL, name));
        return setTagClass(datastore.prepare(query).asSingleEntity());
    }

    // Function returns Tag class from ID.
    public Collection<Tag> getTagObjectsByIds(List<Key> categories) throws Exception
    {
        ArrayList<Tag> tagDataStore = new ArrayList<Tag>();
        if (categories != null)
        {
            for (Key key : categories)
            {
                tagDataStore.add(setTagClass(datastore.get(key)));
            }
        }
        return tagDataStore;
    }
    // Function returns Tag Ids from classes.
    public Collection<Key> getTagIdsByObject(Collection<Tag> categories) throws Exception
    {
        ArrayList<Key> tagDataStore = new ArrayList<Key>();
        if (categories != null)
        {
            for (Tag tagClass : categories)
            {
                tagDataStore.add(tagClass.getId());
            }
        }
        return tagDataStore;
    }
    // Function returns charity classes from IDs.
    public Collection<Charity> getCharityObjectsByIds(List<Key> charities) throws Exception
    {
        ArrayList<Charity> charityDataStore = new ArrayList<Charity>();
        if (charities != null)
        {
            for (Key key : charities)
            {
                charityDataStore.add(setCharityClass(datastore.get(key)));
            }
        }
        return charityDataStore;
    }
    // Function returns charity Ids from classes.
    public Collection<Key> getCharityIdsByObject(Collection<Charity> charities) throws Exception
    {
        ArrayList<Key> charityDataStore = new ArrayList<Key>();
        if (charities != null)
        {
            for (Charity charityClass : charities)
            {
                charityDataStore.add(charityClass.getId());
            }
        }
        return charityDataStore;
    }
    // Function that returns user from user name.
    public Users getUserByUserName(String userName) throws Exception
    {
        Query query =
            new Query(USERS).setFilter(new FilterPredicate(USERNAME, FilterOperator.EQUAL, userName));
        return setUsersClass(datastore.prepare(query).asSingleEntity());
    }
    // Function that returns user from email.
    public Users getUserByEmail(String email) throws Exception
    {
        Query query =
            new Query(USERS).setFilter(new FilterPredicate(EMAIL, FilterOperator.EQUAL, email));
        return setUsersClass(datastore.prepare(query).asSingleEntity());
    }

    // Function turns entity into class.
    public Charity setCharityClass(Entity entity) throws Exception
    {
        Key id = (Key) entity.getKey();
        String name = (String) entity.getProperty(NAME);
        String link = (String) entity.getProperty(LINK);
        String imgsrc = (String) entity.getProperty(IMGSRC);
        List<Key> categoryKeys = (List<Key>) entity.getProperty(CATEGORIES);
        Collection<Tag> categories = getTagObjectsByIds(categoryKeys);
        String description = (String) entity.getProperty(DESCRIPTION);
        double trendingScore = (double) entity.getProperty(TRENDING_SCORE);
        double userRating = (double) entity.getProperty(USER_RATING);
        // Converts data to charity object.
        return new Charity(id, name, link, imgsrc, categories, description, trendingScore, userRating);
    }
    // Function turns entity into class.
    public Tag setTagClass(Entity entity) throws Exception
    {
        Key id = (Key) entity.getKey();
        String name = (String) entity.getProperty(NAME);
        double trendingScore = (double) entity.getProperty(TRENDING_SCORE);
        String imgSrc = (String) entity.getProperty(IMGSRC);
        // Converts data to tag object.
        return new Tag(id, name, trendingScore, imgSrc);
    }
    // Function turns entity into class.
    public Users setUsersClass(Entity entity) throws Exception
    {
        Key id = (Key) entity.getKey();
        String userName = (String) entity.getProperty(USERNAME);
        String email = (String) entity.getProperty(EMAIL);
        List<Key> userInterestsKeys = (List<Key>) entity.getProperty(USER_INTERESTS);
        Collection<Tag> userInterests = getTagObjectsByIds(userInterestsKeys);
        List<Key> charitiesDonatedToKeys = (List<Key>) entity.getProperty(CHARITIES_DONATED_TO);
        Collection<Charity> charitiesDonatedTo = getCharityObjectsByIds(charitiesDonatedToKeys);
        // Converts data to users object.
        return new Users(id, userName, email, userInterests, charitiesDonatedTo);
    }
    // Function takes modified class and updates database.
    public void updateCharity(Charity charity) throws Exception
    {
        Transaction txn = datastore.beginTransaction();
        try
        {
            Entity charityEntity = datastore.get(charity.getId());
            charityEntity.setProperty(NAME, charity.getName());
            charityEntity.setProperty(LINK, charity.getLink());
            charityEntity.setProperty(IMGSRC, charity.getImgSrc());
            charityEntity.setProperty(CATEGORIES, getTagIdsByObject(charity.getCategories()));
            charityEntity.setProperty(DESCRIPTION, charity.getDescription());
            charityEntity.setProperty(TRENDING_SCORE, charity.getTrendingScoreCharity());
            charityEntity.setProperty(USER_RATING, charity.getUserRating());
            datastore.put(charityEntity);
            txn.commit();
        }
        finally
        {
            if (txn.isActive())
            {
                txn.rollback();
            }
        }
    }

    // Function takes modified class and updates database.
    public void updateTag(Tag tag) throws Exception
    {
        Transaction txn = datastore.beginTransaction();
        try
        {
            Entity tagEntity = datastore.get(tag.getId());
            tagEntity.setProperty(NAME, tag.getName());
            tagEntity.setProperty(TRENDING_SCORE, tag.getTrendingScoreTag());
            tagEntity.setProperty(IMGSRC, tag.getImgSrc());
            datastore.put(tagEntity);
            txn.commit();
        }
        finally
        {
            if (txn.isActive())
            {
                txn.rollback();
            }
        }
    }
    // Function takes modified class and updates database.
    public void updateUser(Users user) throws Exception
    {
        Transaction txn = datastore.beginTransaction();
        try
        {
            Entity userEntity = datastore.get(user.getId());
            userEntity.setProperty(USERNAME, user.getUserName());
            userEntity.setProperty(EMAIL, user.getEmail());
            userEntity.setProperty(USER_INTERESTS, user.getUserInterests());
            userEntity.setProperty(CHARITIES_DONATED_TO, user.getCharitiesDonatedTo());
            datastore.put(userEntity);
            txn.commit();
        }
        finally
        {
            if (txn.isActive())
            {
                txn.rollback();
            }
        }
    }
}
