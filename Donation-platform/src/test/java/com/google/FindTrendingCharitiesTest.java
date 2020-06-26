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

package com.google;

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

/** */
@RunWith(JUnit4.class)
public final class FindTrendingCharitiesTest {
  
  private static final Charity FA = HardCodedCharitiesAndTags.charities[0];
  private static final Charity RC = HardCodedCharitiesAndTags.charities[1];
  private static final Charity SJ = HardCodedCharitiesAndTags.charities[2];
  private static final Charity NC = HardCodedCharitiesAndTags.charities[3];
  private static final Charity YMCA = HardCodedCharitiesAndTags.charities[4];
  private static final Charity ACLU = HardCodedCharitiesAndTags.charities[5];
  private static final Charity AHA = HardCodedCharitiesAndTags.charities[6];

  private FindTrendingCharities query;

  @Before
  public void setUp() {
    query = new FindTrendingCharities();
  }

  @Test
  public void correctTrendingOrder() {
      Collection<Charity> actual = query.query();
      Collection<Charity> expected = Arrays.asList(ACLU, AHA, RC, SJ, FA, YMCA, NC);

      Assert.assertEquals(expected, actual);
  }

  @Test
  public void correctTrendingScores() {
      Collection<Charity> trendingCharities = query.query();

      //check that ACLU's trending score is as expected
      double expected = 38.828125;
      double actual = ACLU.getTrendingScore();

      Assert.assertEquals(expected, actual, 0.001);

      //check that AHA's trending score is as expected
      expected = 30.796875;
      actual = AHA.getTrendingScore();

      Assert.assertEquals(expected, actual, 0.001);

      //check that RC's trending score is as expected
      expected = 27.40625;
      actual = RC.getTrendingScore();

      Assert.assertEquals(expected, actual, 0.001);

      //check that SJ's trending score is as expected
      expected = 23.296875;
      actual = SJ.getTrendingScore();

      Assert.assertEquals(expected, actual, 0.001);

      //check that FA's trending score is as expected
      expected = 19.8515625;
      actual = FA.getTrendingScore();

      Assert.assertEquals(expected, actual, 0.001);

      //check that YMCA's trending score is as expected
      expected = 15.609375;
      actual = YMCA.getTrendingScore();

      Assert.assertEquals(expected, actual, 0.001);

      //check that NC's trending score is as expected
      expected = 8.59375;
      actual = NC.getTrendingScore();

      Assert.assertEquals(expected, actual, 0.001);
  }

  
}