package com.example.tapnotes;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class Utility {


    //To show toast message
    static void showToastMessage(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

//    ------------------------------------------------------------------------------

    static CollectionReference getCollectionReferenceForNotes(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        //from this currentuser we will save note to data base
        return FirebaseFirestore.getInstance().collection("notes").document(currentUser.getUid()).collection("my_notes");
        //inside note we have unique ID(getUid), in that we have my notes collection

    }

    static String timeStampToString(Timestamp timestamp){
       return new SimpleDateFormat("dd/MM/yyyy").format(timestamp.toDate());
    }
}
