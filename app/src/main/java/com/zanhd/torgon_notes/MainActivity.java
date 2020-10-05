package com.zanhd.torgon_notes;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.zanhd.torgon_notes.data.DatabaseHandler;
import com.zanhd.torgon_notes.model.Note;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import org.w3c.dom.Node;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private EditText title;
    private EditText details;
    private Button saveButton;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHandler = new DatabaseHandler(this);

        //checking database wheather items was saved or not
//        List<Note> noteList = databaseHandler.getAllNotes();
//        for(Note note : noteList ){
//            Log.d("checkmain", "onCreate: "  + note.getId() + " " + note.getTitle() + " " + note.getDetails() + " "+ note.getDateNoteAdded());
//        }

        byPassActivity(); //check wheather there is a need to byPass MainActivity (check fun def for more details)

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopup();
            }
        });
    }

    private void byPassActivity() {
        if(databaseHandler.getNotesCount() > 0) {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish(); //get rid off MainActivity
        }
    }

    private void createPopup() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup,null);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();

        title = view.findViewById(R.id.title_editview);
        details = view.findViewById(R.id.details_editview);
        saveButton = view.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!title.getText().toString().trim().isEmpty()) {
                    saveNote(view);
                } else {
                    Snackbar.make(view,"Title can't be empty",Snackbar.LENGTH_SHORT).show();
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
                startActivity(new Intent(MainActivity.this,ListActivity.class));
            }
        },500);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}