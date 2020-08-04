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

package com.google.objectify;

import com.google.model.Charity;
import com.google.model.Tag;
import com.google.model.Cause;
import com.google.model.Users;
import com.googlecode.objectify.ObjectifyService;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Class initializes the objectify service and registers the class
 * entities that will be used to instantiate the class objects in
 * the project. This will run any time the project is built to ensure
 * the objectify service has a context to run within.
 */
@WebListener
public class ObjectifyBootstrapper implements ServletContextListener {
  public void contextInitialized(ServletContextEvent event) {
    ObjectifyService.register(Charity.class);
    ObjectifyService.register(Tag.class);
    ObjectifyService.register(Cause.class);
    ObjectifyService.register(Users.class);
  }
}
