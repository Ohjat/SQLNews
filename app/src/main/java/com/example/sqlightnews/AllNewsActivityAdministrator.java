package com.example.sqlightnews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AllNewsActivityAdministrator extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    NewsAdapter adapter;
    RecyclerView recyclerNews;
    Bundle bundle;
    private int IDUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_news_administrator);
        initialize();
        setRecyclerView();
    }

    private void setRecyclerView() {
        recyclerNews.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsAdapter(this, databaseHelper.getNewsData());
        recyclerNews.setAdapter(adapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(AllNewsActivityAdministrator.this);
                dialog.setTitle("Удаление новости")
                        .setMessage("Вы действительно хотите удалить эту новость?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                databaseHelper.deleteNews((int) viewHolder.itemView.getTag());
                                adapter.swapCursor(databaseHelper.getNewsData());
                            }
                        })
                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                adapter.swapCursor(databaseHelper.getNewsData());
                                dialogInterface.cancel();
                            }
                        });
                dialog.create().show();
            }
        }).attachToRecyclerView(recyclerNews);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                startActivity(new Intent(AllNewsActivityAdministrator.this, UpdateNewsActivity.class)
                        .putExtra("Id", IDUser)
                        .putExtra("IdNews", (int) viewHolder.itemView.getTag()));
                finish();
            }
        }).attachToRecyclerView(recyclerNews);
    }

    private void initialize() {
        recyclerNews = findViewById(R.id.recyclerNews);
        bundle = getIntent().getExtras();
        IDUser = bundle.getInt("Id");
        databaseHelper = new DatabaseHelper(this);
    }

    public void addNewsClick(View view) {
        startActivity(new Intent(this, AddNewsActivity.class).putExtra("Id", IDUser));
        finish();
    }
}