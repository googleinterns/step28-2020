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

import java.util.Arrays;
import java.util.Collection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.HashMap;
import java.util.Map;
import com.google.model.Charity;

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
  public void testCharitiesInDB() {
      Assert.assertEquals(0, 0);
  }

  @Test
  public void testTrendingOrderIsCorrect() {
    Collection<Charity> actual = query.query();
    Collection<Charity> expected = Arrays.asList(ACLU, AHA, RC, SJ, FA, YMCA, NC);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testTrendingScoresIsCorrect() {
    Collection<Charity> trendingCharities = query.query();

    Map<Charity, Double> expected =
        new HashMap<Charity, Double>() {
          {
            put(ACLU, 38.828125);
            put(AHA, 30.796875);
            put(RC, 27.40625);
            put(SJ, 23.296875);
            put(FA, 19.8515625);
            put(YMCA, 15.609375);
            put(NC, 8.59375);
          }
        };

    Map<Charity, Double> actual =
        new HashMap<Charity, Double>() {
          {
            put(ACLU, ACLU.getTrendingScore());
            put(AHA, AHA.getTrendingScore());
            put(RC, RC.getTrendingScore());
            put(SJ, SJ.getTrendingScore());
            put(FA, FA.getTrendingScore());
            put(YMCA, YMCA.getTrendingScore());
            put(NC, NC.getTrendingScore());
          }
        };

    Assert.assertEquals(expected, actual);
  }
}
