package com.google.model;

import java.util.List;
import java.util.Objects;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;


/** Represents a Users : id, userName, email, userInterests, charitiesDonatedTo. */
public final class Users {

  private Key id;
  private String userName;
  private String email;
  private List<Integer> userInterests;
  private List<Integer>  charitiesDonatedTo;

  // Initialize all fields of Users
  public Users(Key id, String userName, String email,
              List<Integer> userInterests, List<Integer>  charitiesDonatedTo) {
    this.id = id;
    this.userName = userName;
    this.email = email;
    this.userInterests = userInterests;
    this.charitiesDonatedTo = charitiesDonatedTo;
  }
  // [END fs_class_definition]

  public Users(Key id) {
    this.id = id;
  }

  public Key getId() {
    return id;
  }

  public void setId(Key id) {
    this.id = id;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<Integer> getUserInterests() {
    return userInterests;
  }

  public void setUserInterests(List<Integer> userInterests) {
    this.userInterests = userInterests;
  }

  public List<Integer> getCharitiesDonatedTo() {
    return charitiesDonatedTo;
  }

  public void setCharitiesDonatedTo(List<Integer> charitiesDonatedTo) {
    this.charitiesDonatedTo = charitiesDonatedTo;
  }
}