package com.example.tapnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.ims.ImsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

//import java.sql.Timestamp;

public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleEditText, contentEditText;
    ImageButton saveNoteButton;

    TextView pageTitleTextView;

    String title,content,docId;
    boolean isEditMode=false;
    ImageButton deleteNoteButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        titleEditText= findViewById(R.id.notes_title_text);
        contentEditText= findViewById(R.id.notes_content_text);
        saveNoteButton = findViewById(R.id.doneButton);

        pageTitleTextView= findViewById(R.id.page_title);
        deleteNoteButton=findViewById(R.id.deleteButton);

        //Receive data from intent
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");


        if(docId!=null && !docId.isEmpty()){
            isEditMode=true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);

        if(isEditMode){
            pageTitleTextView.setText("Edit your note");
            deleteNoteButton.setVisibility(View.VISIBLE);
        }



        saveNoteButton.setOnClickListener((v)-> saveNote());

        deleteNoteButton.setOnClickListener( (v)-> deleteNoteFromFirebase());



    }

     void deleteNoteFromFirebase() {


         DocumentReference documentReference;
         documentReference= Utility.getCollectionReferenceForNotes().document(docId);


         //Inside my note collection we are creating one document

         documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
             @Override
             public void onComplete(@NonNull Task<Void> task) {
                 if(task.isSuccessful()){
                     //note is deleted

                     Utility.showToastMessage(NoteDetailsActivity.this,"Note deleted successfully");
                     startActivity(new Intent(NoteDetailsActivity.this, MainActivity.class)) ;

                 }
                 else{
                     //note wont added
                     Utility.showToastMessage(NoteDetailsActivity.this,"Failed while deleting notes");

                 }
             }
         });


     }


    void saveNote() {

        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();

        if(noteTitle==null || noteTitle.isEmpty()){
            titleEditText.setError("Title is required");
            return;
        }


        Note note= new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());


        saveNotetoFirebase(note);
    }
//We are not adding all the notes in one single collection
    //We will create one collection
    //Inside collection we will be having document
    //document: document will some ID's(Which is user ID).Each user have different ID
    void saveNotetoFirebase(Note note){

        //saving the note as document

        DocumentReference documentReference;

        if(isEditMode){
            // If they are doing edit update is with docId
            documentReference= Utility.getCollectionReferenceForNotes().document(docId);
        }
        else {
            //For creating new one
            documentReference= Utility.getCollectionReferenceForNotes().document();
        }

        //Inside my note collection we are creating one document

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is added
                    Utility.showToastMessage(NoteDetailsActivity.this,"Note added successfully");
                    startActivity(new Intent(NoteDetailsActivity.this, MainActivity.class)) ;
                }
                else{
                    //note wont added
                    Utility.showToastMessage(NoteDetailsActivity.this,"Failed while adding notes");

                }
            }
        });

    }
}
