// Copyright 2019 Google LLC
//
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
import java.util.HashMap;
import java.util.Map;

public final class HardCodedCharitiesAndTags {

  public static final Charity[] charities = {
    new Charity(
        "Feeding America",
        "",
        Arrays.asList("hunger"),
        4,
        4.5,
        "https://secure.feedingamerica.org/site/Donation2",
        "https://www.charities.org/sites/default/files/Feeding-America_logo-web.png"),
    new Charity(
        "Red Cross",
        "",
        Arrays.asList("health", "education"),
        4.5,
        4,
        "https://www.redcross.org/donate/donation.html/",
        "https://media.defense.gov/2018/Sep/17/2001966913/-1/-1/0/180917-F-ZZ000-1001.JPG"),
    new Charity(
        "St. Jude's",
        "",
        Arrays.asList("health", "children"),
        3,
        3,
        "https://www.stjude.org/donate/pm.html",
        "https://i1.wp.com/engageforgood.com/wp-content/uploads/2018/10/Untitled-design-69.png?fit=700%2C400&ssl=1"),
    new Charity(
        "Nature Conservancy",
        "",
        Arrays.asList("environment"),
        5,
        2,
        "https://support.nature.org/site/Donation2",
        "https://climatepolicyinitiative.org/wp-content/uploads/2018/03/tnc-nature-conservancy-logo.gif"),
    new Charity(
        "YMCA",
        "",
        Arrays.asList("children"),
        2,
        3,
        "https://www.ymca.net/be-involved",
        "https://www.tristatehomepage.com/wp-content/uploads/sites/92/2016/05/YMCA20logo20web_1462174680230_8365252_ver1.0-1.jpg?w=720&h=405&crop=1"),
    new Charity(
        "ACLU",
        "",
        Arrays.asList("racial equality"),
        5,
        5,
        "https://action.aclu.org/give/fund-every-fight-ahead?",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/6/65/New_ACLU_Logo_2017.svg/1200px-New_ACLU_Logo_2017.svg.png"),
    new Charity(
        "American Heart Association",
        "",
        Arrays.asList("health"),
        3,
        3,
        "https://www2.heart.org/site/SPageServer?pagename=donatenow_heart",
        "https://upload.wikimedia.org/wikipedia/en/thumb/e/e6/American_Heart_Association_Logo.svg/1200px-American_Heart_Association_Logo.svg.png")
  };

  public static final Map<String, Integer> tagScores =
      new HashMap<String, Integer>() {
        {
          put("hunger", 25);
          put("education", 30);
          put("children", 20);
          put("environment", 10);
          put("racial equality", 50);
          put("health", 40);
        }
      };
}
