package com.google.objectify;

import com.google.model.Charity;
import com.google.model.Tag;
import com.google.model.Users;
import com.googlecode.objectify.ObjectifyService;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ObjectifyBootstrapper implements ServletContextListener {
  public void contextInitialized(ServletContextEvent event) {
    ObjectifyService.register(Charity.class);
    ObjectifyService.register(Tag.class);
    ObjectifyService.register(Users.class);
  }
}
