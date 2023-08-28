package com.example.tapnotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addNoteButton;
    RecyclerView recyclerNoteView;
    ImageButton menuButton;

    NoteAdapter noteAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addNoteButton = findViewById(R.id.addNote_btn);
        recyclerNoteView=findViewById(R.id.recycler_note_view);
        menuButton=findViewById(R.id.menu_Button);


        addNoteButton.setOnClickListener((v)-> startActivity(new Intent(MainActivity.this, NoteDetailsActivity
                .class)) );

        menuButton.setOnClickListener((v)-> showMenu());

        setupRecyclerNoteView();

    }

    void showMenu() {
        // To display logout button

        PopupMenu popupMenu = new PopupMenu(MainActivity.this,menuButton);
        popupMenu.getMenu().add("Log Out");
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle()=="Log Out"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                    return true;

                }

                return false;
            }
        });




    }

    void setupRecyclerNoteView() {

        Query query = Utility.getCollectionReferenceForNotes().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>().setQuery(query,Note.class).build();
        recyclerNoteView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter= new NoteAdapter(options,this);
        recyclerNoteView.setAdapter(noteAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }
}