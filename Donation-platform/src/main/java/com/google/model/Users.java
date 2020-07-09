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

import java.util.List;
import com.google.appengine.api.datastore.Key;

/** Represents a Users : id, userName, email, userInterests, charitiesDonatedTo. */
public final class Users {

  // Datastore key uniquely identifying user.
  private Key id;
  // Username of user
  private String userName;
  // Email address of user
  private String email;
  // Tag IDs representing category tags the user selected that they were interested in.
  private List<Key> userInterests;
  // Charity IDs representing charities the user donated to.
  private List<Key> charitiesDonatedTo;

  // Initialize all fields of Users
  public Users(
      Key id,
      String userName,
      String email,
      List<Key> userInterests,
      List<Key> charitiesDonatedTo) {
    this.id = id;
    this.userName = userName;
    this.email = email;
    this.userInterests = userInterests;
    this.charitiesDonatedTo = charitiesDonatedTo;
  }

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

  public List<Key> getUserInterests() {
    return userInterests;
  }

  public void setUserInterests(List<Key> userInterests) {
    this.userInterests = userInterests;
  }

  public List<Key> getCharitiesDonatedTo() {
    return charitiesDonatedTo;
  }

  public void setCharitiesDonatedTo(List<Key> charitiesDonatedTo) {
    this.charitiesDonatedTo = charitiesDonatedTo;
  }
}
