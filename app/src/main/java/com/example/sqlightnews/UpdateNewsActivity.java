package com.example.sqlightnews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class UpdateNewsActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    EditText txtContent, txtTitle;
    Bundle bundle;
    Calendar date;
    private int IDUser;
    private int IdNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_news);
        initialize();
        setData();
    }

    private void setData() {
        Cursor res = databaseHelper.getNewsData(IdNews);
        while (res.moveToNext()) {
            txtContent.setText(res.getString(2));
            txtTitle.setText(res.getString(1));
        }
    }

    private void initialize() {
        bundle = getIntent().getExtras();
        txtContent = findViewById(R.id.txtContent);
        txtTitle = findViewById(R.id.txtTitle);
        databaseHelper = new DatabaseHelper(this);
        date = Calendar.getInstance();
        IDUser = bundle.getInt("Id");
        IdNews = bundle.getInt("IdNews");
    }

    public void updateNewsClick(View view) {
        if (TextUtils.isEmpty(txtContent.getText()) || TextUtils.isEmpty(txtTitle.getText())) {
            Toast.makeText(this, "Вы заполнили не все поля", Toast.LENGTH_SHORT).show();
            return;
        }
        databaseHelper
                .updateNews(IdNews, txtTitle.getText().toString().trim(), txtContent.getText().toString().trim(),
                        DateUtils.formatDateTime(getApplicationContext(), date.getTimeInMillis(),
                                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_TIME), IDUser);
        startActivity(new Intent(this, AllNewsActivityAdministrator.class).putExtra("Id", IDUser));
        finish();
    }

    public void exitClick(View view) {
        startActivity(new Intent(this, AllNewsActivityAdministrator.class).putExtra("Id", IDUser));
        finish();
    }
}