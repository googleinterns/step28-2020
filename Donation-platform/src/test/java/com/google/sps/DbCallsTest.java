
package com.google.sps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DbCallsTest {

  private DbCalls databaseQuery;

  @Before
  public void setUp() {
    databaseQuery = new DbCalls();
  }

  @Test
  public void testQuickStart() throws Exception {
    String expected = databaseQuery.getAllDocuments("actual");
    Assert.assertEquals(expected, "actual");
  }

}