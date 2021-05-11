package com.maid.worktodo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
     ImageButton imgButton;
     ArrayList<Notes> notes;
     RecyclerView recyclerView;
     NoteAdapter noteAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imgButton= findViewById(R.id.img_add);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater)MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewInput = inflater.inflate(R.layout.note_input,null,false);

                EditText edtTitle= viewInput.findViewById(R.id.edt_title);
                EditText edtDescription= viewInput.findViewById(R.id.edt_descripton);



                new AlertDialog.Builder(MainActivity.this)
                        .setView(viewInput)
                        .setTitle("Add Note").setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String title = edtTitle.getText().toString();
                        String description = edtDescription.getText().toString();


                        Notes notes = new Notes(title,description);

                        boolean isInserted = new NoteHandler(MainActivity.this).create(notes);
                        
                        if(isInserted){
                            Toast.makeText(MainActivity.this,"Note Saved",Toast.LENGTH_SHORT).show();
                            loadNotes();
                        }else{
                            Toast.makeText(MainActivity.this,"Unable to save note",Toast.LENGTH_SHORT).show();
                        }
                        
                        dialogInterface.cancel();
                        
                    }
                }).show();



            }
        });


        
        recyclerView= findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        ItemTouchHelper.SimpleCallback itemTouchCallBack= new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT| ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                 new NoteHandler(MainActivity.this).delete(notes.get(viewHolder.getAdapterPosition()).getId());
                 notes.remove(viewHolder.getAdapterPosition());
                 noteAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallBack);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        loadNotes();
    }

    public ArrayList<Notes> readNotes(){
        ArrayList<Notes> notes = new NoteHandler(this).readNotes();
        return notes;
    }

    public void loadNotes(){
        notes = readNotes();

        noteAdapter = new NoteAdapter(notes, this, new NoteAdapter.ItemClicked() {
            @Override
            public void onClick(int position, View view) {
                editNote(notes.get(position).getId(),view);

            }
        });
        
        recyclerView.setAdapter(noteAdapter);
    }

 private void editNote(int noteId,View view){
        NoteHandler noteHandler = new NoteHandler(this);
        Notes note = noteHandler.readSingleNote(noteId);
     Intent intent = new Intent(this,EditNote.class);
     intent.putExtra("title",note.getTitle());
     intent.putExtra("description",note.getDescription());
     intent.putExtra("id",note.getId());


     ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,view, ViewCompat.getTransitionName(view));
     startActivityForResult(intent,1, optionsCompat.toBundle());

 }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1)
            loadNotes();
    }
}