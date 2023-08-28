package com.example.tapnotes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder > {

    Context context;

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        holder.titleTextView.setText(note.title);
        holder.contentTextView.setText(note.content);
        holder.timeStampView.setText(Utility.timeStampToString(note.timestamp));


        holder.itemView.setOnClickListener((v)-> {

            Intent intent = new Intent(context, NoteDetailsActivity.class);
            intent.putExtra("title",note.title);
            intent.putExtra("content",note.content);
            String docId= this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);

            context.startActivity(intent);

        });

    }


    //Here we have to pass the view which we have created that single item xml
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_single_noteview,parent,false);
        return new NoteViewHolder(view);
    }


//    --------------------------------------------------------------------------------------------------------

    //This will hold the view for recycler note item
    class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView, contentTextView, timeStampView;


        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);


            titleTextView= itemView.findViewById(R.id.note_title_text_viewOnly);
            contentTextView=itemView.findViewById(R.id.note_content_text_viewOnly);
            timeStampView=itemView.findViewById(R.id.note_timestamp_text_viewOnly);




        }
    }



}
