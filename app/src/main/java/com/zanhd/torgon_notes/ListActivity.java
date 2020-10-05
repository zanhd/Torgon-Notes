package com.zanhd.torgon_notes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.zanhd.torgon_notes.data.DatabaseHandler;
import com.zanhd.torgon_notes.model.Note;
import com.zanhd.torgon_notes.ui.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Note> noteList;
    private DatabaseHandler databaseHandler;

    private FloatingActionButton fab;

    //copy pasted from MainActivity because of popup
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private EditText title;
    private EditText details;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        fab = findViewById(R.id.fab2);

        recyclerView = findViewById(R.id.recycler_view);
        databaseHandler = new DatabaseHandler(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        noteList  = new ArrayList<>();
        noteList = databaseHandler.getAllNotes();

        recyclerViewAdapter = new RecyclerViewAdapter(this,noteList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged(); //refresh tha all the data hence notify something is changed

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopup();
            }
        });
    }

    //NOTE : we are MOSTLY copy paste the below code but the best practice to create its class
    //Important Point :  Avoid dublicate code
    private void createPopup() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();

        title = view.findViewById(R.id.title_editview);
        details = view.findViewById(R.id.details_editview);
        saveButton = view.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!title.getText().toString().trim().isEmpty()) {
                    saveNote(view);
                } else {
                    Snackbar.make(view, "Title can't be empty", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveNote(View view) {
        //save data to the db
        Note note = new Note();
        note.setTitle(title.getText().toString().trim());
        note.setDetails(details.getText().toString().trim());
        databaseHandler.addNote(note);
        Snackbar.make(view,"Note saved",Snackbar.LENGTH_SHORT).show();

        // move to next screen(Activity)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                startActivity(new Intent(ListActivity.this,ListActivity.class));
            }
        },500);
    }
}