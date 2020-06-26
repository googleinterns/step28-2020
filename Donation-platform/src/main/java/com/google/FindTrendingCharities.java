// // Copyright 2019 Google LLC
// //
// // Licensed under the Apache License, Version 2.0 (the "License");
// // you may not use this file except in compliance with the License.
// // You may obtain a copy of the License at
// //
// //     https://www.apache.org/licenses/LICENSE-2.0
// //
// // Unless required by applicable law or agreed to in writing, software
// // distributed under the License is distributed on an "AS IS" BASIS,
// // WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// // See the License for the specific language governing permissions and
// // limitations under the License.

// package com.google;

// import java.util.Collection;
// import java.util.Collections;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;
// import java.lang.Double;
// import java.util.Set;
// import java.util.HashSet;
// import java.util.HashMap;

// public final class FindTrendingCharities {

//     //number of trending charities to be returned
//     final int topTrending = 12;

//     final double charityNavToScale = 1.25;

//     //should sum to 1
//     final double userRatingWeight = 0.75;
//     final double scaledCharityNavWeight = 0.25;

//     //should sum to 1
//     final double charityTagsWeight = 0.75;
//     final double avgReviewWeight = 0.25;


//     //returns the collection of top trending charities
//     public Collection<Charity> query() {
//         Collection<Charity> charities = getAllCharities();
//         for (Charity cur: charities) {
//             double curScore = calcCharityTrendingScore(cur);
//             cur.setTrendingScore(curScore);
//         }
//         Collections.sort(charities);
//         List<Charity> topTrending = charities.subList(0, trendingNum);
//         return topTrending;
//     }

//     //returns a list of all charities in the database
//     private Collection<Charity> getAllCharities(){
//         List<Charity> charities = new ArrayList<>();
//         // GET request for all charity names from the database
//         // create new Charity object for each of the names, with the relevant info from db
//         // add each new Charity object to the charities collection 
//         return charities;
//     }

//     // returns the trending score of inputted charity calculated 
//     // as aweighted average of the tagScore and the avgReview where
//     // tagScore represents the average trending score of the associated tags
//     // and avgReview is a weighted average of the userRating and the charityNavigatory API rating
//     // weights for the averages are stored as class constants 
//     private double calcCharityTrendingScore(Charity charity) {
//         boolean hasCharityNavRating = false;
//         double charityNavRating, scaledCharityNavRating, userRating, avgReview;
//         if (charity has CharityNavigator rating) {
//             hasCharityNavRating = true;
//             //charityNavRating = GET request to db for CharityNavigatorAPI rating;
//         }
//         scaledCharityNavRating = charityNavToScale * charityNavRating;
//         //userRating = GET request to db for charity star rating;
//         if (hasCharityNavRating) {
//             avgReview = userRatingWeight * userRating + scaledCharityNavWeight * scaledCharityNavRating;
//         } else {
//             avgReview = userRating;
//         }
//         Collection<Tag> tags = GET request to db for charity tags;
//         double charityTagsScore = getTagTrendingScore(Collection<Tag>);
//         double charityTrendingScore = charityTagsWeight * charityTagsScore + avgReviewWeight * avgReview;
//         return charityTrendingScore;
//     }

//     //return the average trending score of a collection of tags
//     private double getTagTrendingScore(Collection<Tag> tags) {
//         double sumScores = 0;
//         int numTags = tags.size();
//         for (Tag tag: tags) {
//             double tagScore = 0;
//             //tagScore = use Google Trend API to get trending score
//             sumScores += tagScore;
//         }
//         return (sumScores / numTags);
//     }
// }