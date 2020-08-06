package com.google;

import com.google.model.Charity;
import com.google.model.Tag;
import com.google.model.Users;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import com.google.appengine.api.datastore.DatastoreService;
import com.googlecode.objectify.Key;
import java.util.Collections;
import static com.googlecode.objectify.ObjectifyService.ofy;
import java.lang.Long;

/** Caching
* Caching of the objects is being handled by objectify thorough the
* @cache annotation in the model classes. Objectify will connect to the global 
* memcache when the data is loaded and speed up the app perfromance. 
*/

/** Transactions
* If you operate on the datastore without an explicit transaction,
* each datastore operation is treated like a separate little transaction which is retried separately. 
* This is the current implementation.
*/

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
        List<Charity> charityDataStore = ofy().load().type(Charity.class).list();
        return charityDataStore;
    }

    // Function returns all the tag objects in the database.
    public Collection<Tag> getAllTags() throws Exception
    {
        List<Tag> tagDataStore = ofy().load().type(Tag.class).list();
        return tagDataStore;
    }
    // Function returns all the users in the database.
    public Collection<Users> getAllUsers() throws Exception
    {
        List<Users> usersDataStore = ofy().load().type(Users.class).list();
        return usersDataStore;
    }
    // Function adds charity to database.
    // Could be used in conjunction with user charity add form.
    public void addCharity(
        String name, String link, String imgsrc, Collection<Tag> categories, String description)
    throws Exception
    { 
        Charity newCharity = new Charity(null, name, link, imgsrc, categories, description, 0.0, 0.0);
        ofy().save().entity(newCharity).now();
    }
    // Function adds tag to the database.
    public void addTag(String name, double trendingScore, String imgSrc) throws Exception
    {
        Tag newTag = new Tag(null,  name, trendingScore, imgSrc);
        ofy().save().entity(newTag).now();
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
        Users newUser = new Users(userId, userName, email, userInterests, charitiesDonatedTo);
        ofy().save().entity(newUser).now();
    }
    // Function returns charity object from ID.
    public Charity getCharityById(Long charityId) throws Exception
    {
        return (Charity) ofy().load().type(Charity.class).id(charityId).now();
    }
    // Function gets one charity by name. Returns error if multipe charities
    // have the same name.
    public Charity getCharityByName(String name) throws Exception
    {
        return ofy().load().type(Charity.class).filter("name", name).first().now();
    }
    // Function returns all charities under a category tag.
    public Collection<Charity> getCharitiesByTag(String name) throws Exception
    {
        ArrayList<Charity> charityDataStore = new ArrayList<Charity>();
        for (Charity charity : ofy().load().type(Charity.class).list())
        {
            // Read the categories assigned to the charity
            if (charity.getCategories() != null)
            {
                for (Tag tag : charity.getCategories())
                {
                    // Check to see if category contains tag searched for.
                    if (tag.getName().equals(name))
                    {
                        charityDataStore.add(charity);
                    }
                }
            }
        }
        // Return all charity objects containing tag.
        return charityDataStore;
    }
    // Function returns all charities under one of the three specified category tags.
    public Collection<Charity> getCollectionOfPersonalizedCharities(String name1, String name2, String name3) throws Exception
    {
        ArrayList<Charity> charityDataStore = new ArrayList<Charity>();
        for (Charity charity : ofy().load().type(Charity.class).list())
        {
            // Read the categories assigned to the charity
            if (charity.getCategories() != null)
            {
                for (Tag tag : charity.getCategories())
                {
                    // Check to see if category contains tag searched for.
                    if (tag.getName().equals(name1) | tag.getName().equals(name2) | tag.getName().equals(name3))
                    {
                        charityDataStore.add(charity);
                    }
                }
            }
        }
        // Return all charity objects containing tag.
        return charityDataStore;
    }
    // Function returns tag object from ID.
    public Tag getTagById(Long tagId) throws Exception
    {
        return (Tag) ofy().load().type(Tag.class).id(tagId).now();
    }
    // Function returns tag object matching name passed in.
    public Tag getTagByName(String name) throws Exception
    {
        return ofy().load().type(Tag.class).filter("name", name).first().now();
    }
    // Function that returns user from user name.
    public Users getUserByUserName(String userName) throws Exception
    {
        return ofy().load().type(Users.class).filter("userName", userName).first().now();
    }
    // Function that returns user from email.
    public Users getUserByEmail(String email) throws Exception
    {
        return ofy().load().type(Users.class).filter("email", email).first().now();
    }
    // Function takes modified class and updates database.
    public void updateCharity(Charity charity) throws Exception
    {
        ofy().save().entity(charity).now();
    }

    // Function takes modified class and updates database.
    public void updateTag(Tag tag) throws Exception
    {
        ofy().save().entity(tag).now();
    }
    // Function takes modified class and updates database.
    public void updateUser(Users user) throws Exception
    {
        ofy().save().entity(user).now();
    }
}
