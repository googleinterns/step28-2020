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

import com.google.Charity;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Arrays;

/* Class that takes user-selected tags as input and finds the according best-matching charities.*/
public class RecommendationSystem {

    // Determine how much the charity's tags matching matters compared to how trending the charity is
    private final double TAG_MATCHING_WEIGHT = 0.7;
    private final double CHARITY_TRENDING_WEIGHT = 0.3;

    // Increment constants to charity's tagValue for having the given user-selected tags (#1, #2, or #3)
    private final double TAGSCORE_INCREMENT_FOR_HAVING_TAG_1 = 0.8;
    private final double TAGSCORE_INCREMENT_FOR_HAVING_TAG_2 = 0.5;
    private final double TAGSCORE_INCREMENT_FOR_HAVING_TAG_3 = 0.3;
    private final double TAGSCORE_INCREMENT_FOR_HAVING_TAG_1_AND_2 = 0.2;
    private final double TAGSCORE_INCREMENT_FOR_HAVING_TAG_1_AND_3 = 0.15;
    private final double TAGSCORE_INCREMENT_FOR_HAVING_TAG_2_AND_3 = 0.1;

    // Sorts charities according to the scores calculated by getCharityScores
    public List<Charity> getBestMatches(List<String> selectedTags) {
        List<Charity> bestMatches = new ArrayList<Charity>();
        HashMap<Charity, Double> sortedCharityScores = sortByValue(getCharityScores(selectedTags));
        for(Map.Entry charity : sortedCharityScores.entrySet()) {
            bestMatches.add((Charity) charity.getKey());
        }
        return bestMatches;
    }

    // Returns mapping of charities to the scores that determine their rank on the personalzied page (higher score = better match)
    public HashMap<Charity, Double> getCharityScores(List<String> selectedTags) {
        Collection<Charity> charities = getAllCharities();
        HashMap<Charity, Double> charityScores = new HashMap<Charity, Double>();

        for(Charity charity : charities) {
            // If the charity contains any of the user-selected tags, it will appear on the personalized page
            if((charity.getTags()).contains(selectedTags.get(0)) | 
            (charity.getTags()).contains(selectedTags.get(1)) | 
            (charity.getTags()).contains(selectedTags.get(2))) {
                double tagScore = 0.0;
                // If the charity has the user's #1 ranked tag
                if((charity.getTags()).contains(selectedTags.get(0))) {
                    tagScore += TAGSCORE_INCREMENT_FOR_HAVING_TAG_1;
                    if((charity.getTags()).contains(selectedTags.get(1))) {
                        tagScore += TAGSCORE_INCREMENT_FOR_HAVING_TAG_1_AND_2;
                    } else if((charity.getTags()).contains(selectedTags.get(2))) {
                        tagScore += TAGSCORE_INCREMENT_FOR_HAVING_TAG_1_AND_3;
                    }
                // If the charity has the user's #2 ranked tag
                } else if((charity.getTags()).contains(selectedTags.get(1))) {
                    tagScore += TAGSCORE_INCREMENT_FOR_HAVING_TAG_2;
                    if((charity.getTags()).contains(selectedTags.get(2))) {
                        tagScore += TAGSCORE_INCREMENT_FOR_HAVING_TAG_2_AND_3;
                    }
                // If the charity has the user's #3 ranked tag
                } else if((charity.getTags()).contains(selectedTags.get(2))) {
                    tagScore += TAGSCORE_INCREMENT_FOR_HAVING_TAG_3;
                }
                double charityScore = TAG_MATCHING_WEIGHT*tagScore + CHARITY_TRENDING_WEIGHT*charity.getTrendingScore();
                charityScores.put(charity, charityScore);
            }
        }
        return charityScores;
    }

    // Gets charities from the HardCodedCharitiesAndTags class
    private Collection<Charity> getAllCharities() {
        return Arrays.asList(HardCodedCharitiesAndTags.charities);
    }

    // Sorts HashMap in decreasing order of values
    public LinkedHashMap<Charity, Double> sortByValue(HashMap<Charity, Double> map) {
        List<Entry<Charity, Double>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, Entry.comparingByValue());
        Collections.reverse(list);
        LinkedHashMap<Charity, Double> result = new LinkedHashMap<>();
        for (Entry<Charity, Double> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

}