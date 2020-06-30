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
        new Charity("Feeding America", "", Arrays.asList("hunger"), 4, 4.5),
        new Charity("Red Cross", "", Arrays.asList("health", "education"), 4.5, 4),
        new Charity("St. Judes", "", Arrays.asList("health", "children"), 3, 3),
        new Charity("Nature Conservancy", "", Arrays.asList("environment"), 5, 2),
        new Charity("YMCA", "", Arrays.asList("children"), 2, 3),
        new Charity("ACLU", "", Arrays.asList("racial equality"), 5, 5), 
        new Charity("American Heart Association", "", Arrays.asList("health"), 3, 3)
    };

    public static final Map<String, Integer> tagScores = new HashMap<String, Integer>() {
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
