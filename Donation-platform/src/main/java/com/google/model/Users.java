// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.model;

import java.util.Collection;
import java.io.Serializable;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Index;

// In Objectify classes must be registered as entities for the service to associate
// the class object with datastore and store it. 
@Entity
// This annotation tells the objectify service to cache the class objects 
// in memcache whenever possible.
@Cache
/** Represents a Users : id, userName, email, userInterests, charitiesDonatedTo. */
public final class Users implements Serializable {

  // Datastore String uniquely identifying user.
  @Id private String id;
  // Username of user
  @Index private String userName;
  // Email address of user
  @Index private String email;
  // Category tags the user selected that they were interested in.
  private Collection<Tag> userInterests;
  // Charities the user donated to.
  private Collection<Charity> charitiesDonatedTo;

  private Users() {}
  // Initialize all fields of Users
  public Users(
      String id,
      String userName,
      String email,
      Collection<Tag> userInterests,
      Collection<Charity> charitiesDonatedTo) {
    this.id = id;
    this.userName = userName;
    this.email = email;
    this.userInterests = userInterests;
    this.charitiesDonatedTo = charitiesDonatedTo;
  }

  public Users(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
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

  public Collection<Tag> getUserInterests() {
    return userInterests;
  }

  public void setUserInterests(Collection<Tag> userInterests) {
    this.userInterests = userInterests;
  }

  public Collection<Charity> getCharitiesDonatedTo() {
    return charitiesDonatedTo;
  }

  public void setCharitiesDonatedTo(Collection<Charity> charitiesDonatedTo) {
    this.charitiesDonatedTo = charitiesDonatedTo;
  }
}
