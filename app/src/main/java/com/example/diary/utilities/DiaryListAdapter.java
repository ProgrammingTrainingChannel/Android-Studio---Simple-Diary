package com.example.diary.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.diary.CreateNewDiary;
import com.example.diary.ListOfDiaryActivity;
import com.example.diary.R;
import com.example.diary.dbhelper.AppDatabase;
import com.example.diary.entities.DiaryEntity;

import java.util.List;

public class DiaryListAdapter extends ArrayAdapter<DiaryEntity> {
    private Context innerContext;
    private static AppDatabase db;



    public DiaryListAdapter(Context context, List<DiaryEntity> users) {
        super(context, 0, users);
        innerContext = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        //Get the Diary item for this position
        final DiaryEntity diaryEntity = getItem(position);

        //Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.diarylist, parent, false);



        }


        final TextView tvwCreatedDate = (TextView) convertView.findViewById(R.id.tvwDiaryCreatedDate);
        final TextView tvwTitle = (TextView) convertView.findViewById(R.id.tvwDiaryTitle);
        final TextView tvwContent = (TextView) convertView.findViewById(R.id.tvwDiaryContent);

        if (diaryEntity != null) {
            // Populate the data into the template view using the data object
            tvwCreatedDate.setText(diaryEntity.CreatedDate.toString());
            tvwCreatedDate.setTag(diaryEntity.Id);
            tvwTitle.setText(diaryEntity.Title);
            tvwContent.setText(diaryEntity.Content);
        }


        ImageButton btnEdit = convertView.findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
                String DiaryID = tvwCreatedDate.getTag().toString();

                Intent intent = new Intent(innerContext, CreateNewDiary.class);
                intent.putExtra("DiaryID", DiaryID);
                innerContext.startActivity(intent);
            }
        });



        ImageButton btnDelete = convertView.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String diaryID = tvwCreatedDate.getTag().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(innerContext);
                builder.setMessage("Are you sure you want to delete?")
                        .setCancelable(false)
                        .setTitle("Delete Diary")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        DeleteDiary(Integer.parseInt(diaryID));
                                        dialog.cancel();

                                        Intent intent = new Intent(innerContext, ListOfDiaryActivity.class);
                                        innerContext.startActivity(intent);
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // cancel the dialog box
                                        dialog.cancel();
                                    }
                                });
                builder.show();
            }
        });

        return convertView;
    }

    public void DeleteDiary(Integer DiaryId){
        db = Room.databaseBuilder(innerContext,
                AppDatabase.class, "DiaryDB").build();

        new AgentAsyncTask().execute(DiaryId.toString());
    }

    public static class AgentAsyncTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            DiaryEntity diaryEntity = new DiaryEntity();
            diaryEntity = db.diaryDAO().GetSingleDiary(Integer.parseInt(params[0]));

            db.diaryDAO().DeleteDiary(diaryEntity);

            return 0;
        }

        @Override
        protected void onPostExecute(Integer agentsCount) {

        }

    }


}
