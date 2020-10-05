package com.zanhd.torgon_notes.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.zanhd.torgon_notes.R;
import com.zanhd.torgon_notes.data.DatabaseHandler;
import com.zanhd.torgon_notes.model.Note;

import org.w3c.dom.Node;
import org.w3c.dom.Text;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Note> noteList;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, int position) {
        Note note = noteList.get(position);
        viewHolder.titleView.setText(note.getTitle());
        viewHolder.detailsView.setText(note.getDetails());
        viewHolder.dateNoteAdded.setText(note.getDateNoteAdded());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView titleView;
        public TextView detailsView;
        public TextView dateNoteAdded;
        public Button editButton;
        public Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleView = itemView.findViewById(R.id.title_textview);
            detailsView = itemView.findViewById(R.id.details_textview);
            dateNoteAdded = itemView.findViewById(R.id.date_note_added);

            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            Note note = noteList.get(position);

            switch(view.getId()) {
                case R.id.editButton :
                    editNote(note);
                    break;
                case R.id.deleteButton:
                    deleteNote(note.getId());
                    break;
            }
        }

        private void deleteNote(final int id) {
            builder  = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_popup,null);

            Button noButton = view.findViewById(R.id.noButton);
            Button yesButton = view.findViewById(R.id.yesButton);

            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatabaseHandler db = new DatabaseHandler(context);
                    db.deleteNote(id);  // calling delete for database
                    //till now we deleted Note from data base
                    //Now delete the user interface i.e card view of that Note Object which was deleted
                    noteList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    dialog.dismiss();
                }
            });

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        private void editNote(final Note note) {
            builder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view  =inflater.inflate(R.layout.popup,null);

            TextView popUpTitle;
            final TextView titleText;
            final TextView detailsText;
            Button saveButton;

            popUpTitle = view.findViewById(R.id.create_note_text);
            popUpTitle.setText(R.string.update_note);

            titleText = view.findViewById(R.id.title_editview);
            detailsText = view.findViewById(R.id.details_editview);

            saveButton = view.findViewById(R.id.saveButton);
            saveButton.setText(R.string.update);

            titleText.setText(note.getTitle());
            detailsText.setText(note.getDetails());

            //now showing previous note on popup edittext (so that user can modify our current Object of Note)
            titleText.setText(note.getTitle());
            detailsText.setText(note.getDetails());

            builder.setView(view);
            dialog = builder.create();
            dialog.show();


            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseHandler databaseHandler = new DatabaseHandler(context);

                    //update Note
                    note.setTitle(titleText.getText().toString().trim());
                    note.setDetails(detailsText.getText().toString().trim());

                    if(!titleText.getText().toString().trim().isEmpty()) {

                        //update the Note in database
                        databaseHandler.updateNote(note);
                        notifyItemChanged(getAdapterPosition(),note);

                        Snackbar.make(view,"Notes Updated",Snackbar.LENGTH_SHORT).show();

                        //some kalakaaari
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        },800);
                    } else {
                        //do not update the Note in database
                        Snackbar.make(view,"Title can't be empty",Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
