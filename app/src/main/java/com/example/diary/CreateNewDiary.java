package com.example.diary;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.icu.text.CaseMap;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.diary.dbhelper.AppDatabase;
import com.example.diary.entities.DiaryEntity;
import com.example.diary.utilities.DiaryListAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateNewDiary extends AppCompatActivity {
    Context context;
    EditText diarytitle, edtdiary;
    TextView createddate;

    LinearLayout linearLayout;
    String DiaryID,dateTime;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_diary);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        linearLayout = findViewById(R.id.linearLayout);

        createddate = findViewById(R.id.tvwCreatedDate);
        diarytitle = findViewById(R.id.edttitle);
        edtdiary = findViewById(R.id.edtdiary);

        String date_n = new SimpleDateFormat("EEE,MMM-dd-yyyy, HH:mm a", Locale.getDefault()).format(new Date());
        createddate.setText(date_n);


        context = this;
        DiaryID = getIntent().getStringExtra("DiaryID");
        if (DiaryID != null && !DiaryID.equals("")) {
            new AgentAsyncTask().execute("Load", DiaryID);

        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.createnewdiary_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Handle Item Selection
        switch (item.getItemId()) {
            case R.id.app_bar_save:
                if (diarytitle.getText().toString().trim().equals("") && edtdiary.getText().toString().trim().equals("")) {
                    Snackbar.make(linearLayout, "Please write you diary.", Snackbar.LENGTH_LONG).show();
                } else{
                    if(DiaryID != null && !DiaryID.equals("")){
                        new AgentAsyncTask().execute("Update", DiaryID, diarytitle.getText().toString(),edtdiary.getText().toString());
                    }else {
                        new AgentAsyncTask().execute("Add", diarytitle.getText().toString(), edtdiary.getText().toString());
                    }
                    Intent intent = new Intent(context, ListOfDiaryActivity.class);
                    startActivity(intent);
                }


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class AgentAsyncTask extends AsyncTask<String, Void, Integer> {

        DiaryEntity diaryEntity = new DiaryEntity();

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Integer doInBackground(String... strings) {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "DiaryDB").build();

            switch (strings[0]) {
                case "Add":

                    diaryEntity.CreatedDate = new Date();
                    diaryEntity.Title = strings[1];
                    diaryEntity.Content = strings[2];

                    db.diaryDAO().InsertDiary(diaryEntity);
                    diaryEntity = null;
                    break;
                case "Load":
                    diaryEntity = db.diaryDAO().GetSingleDiary(Integer.parseInt(strings[1]));
                    break;
                case "Update":
                    diaryEntity.Id = Integer.parseInt(strings[1]);
                    diaryEntity.CreatedDate = new Date();
                    diaryEntity.Title = strings[2];
                    diaryEntity.Content = strings[3];

                    db.diaryDAO().UpdateDiary(diaryEntity);
                    diaryEntity = null;
                    break;
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer agentsCount) {
            if (diaryEntity != null) {
                diarytitle.setText(diaryEntity.Title);
                edtdiary.setText(diaryEntity.Content);
            }

        }
    }
}