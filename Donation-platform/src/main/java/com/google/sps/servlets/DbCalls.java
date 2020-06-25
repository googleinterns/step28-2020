package com.google.sps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
<<<<<<< HEAD

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.auth.oauth2.ComputeEngineCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
=======
>>>>>>> parent of 6740293... test and class objects added

public final class DbCalls {

   public String getAllDocuments(String entity) throws Exception {
<<<<<<< HEAD
    // [START fs_get_all_docs]
    // GoogleCredentials credentials = ComputeEngineCredentials.create();
    // FirebaseOptions options = new FirebaseOptions.Builder()
    //   .setCredentials(credentials)
    //   .setProjectId("donation-step-2020")
    //   .build();
    // FirebaseApp.initializeApp(options);
    // //asynchronously retrieve all documents
    // ApiFuture<QuerySnapshot> future = db.collection(entity).get();
    // // future.get() blocks on response
    // List<QueryDocumentSnapshot> documents = future.get().getDocuments();
    // for (QueryDocumentSnapshot document : documents) {
    //   System.out.println(document.getId());
    // }
    // // [END fs_get_all_docs]
=======
    
>>>>>>> parent of 6740293... test and class objects added
    return entity;
  }
}