package com.maid.worktodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class EditNote extends AppCompatActivity {
    EditText edtTitle,edtDescription;

    Button btnsave,btncancel;
    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Intent intent = getIntent();
        linearLayout = findViewById(R.id.btn_holder);
        edtTitle= findViewById(R.id.edt_edit_title);
        edtDescription = findViewById(R.id.edt_edit_description);
        btnsave = findViewById(R.id.btnSave);
        btncancel = findViewById(R.id.btnCancel);

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notes note = new Notes(edtTitle.getText().toString(),edtDescription.getText().toString());
                note.setId(intent.getIntExtra("id",1));
               if (new NoteHandler(EditNote.this).update(note)){
                   Toast.makeText(EditNote.this,"Note Updated",Toast.LENGTH_SHORT).show();
               }else{
                   Toast.makeText(EditNote.this,"Failed Updating",Toast.LENGTH_SHORT).show();
               }

               onBackPressed();
            }
        });

        edtTitle.setText(intent.getStringExtra("title"));
        edtDescription.setText(intent.getStringExtra("description"));

    }

    @Override
    public void onBackPressed() {
        btnsave.setVisibility(View.GONE);
        btncancel.setVisibility(View.GONE);
        TransitionManager.beginDelayedTransition(linearLayout);
        super.onBackPressed();
    }
}