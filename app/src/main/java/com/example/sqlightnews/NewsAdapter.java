package com.example.sqlightnews;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private Context context;
    private Cursor cursor;
    private int IdNews;
    private int IDUser;

    public NewsAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerview_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }
        holder.txtTitle.setText(cursor.getString(1));
        holder.txtDateOfPublication.setText(cursor.getString(3));
        IdNews = cursor.getInt(0);
        holder.itemView.setTag(IdNews);
        IDUser = cursor.getInt(4);
        Cursor res = holder.databaseHelper.getData(IDUser);
        while (res.moveToNext()) {
            holder.txtAuthor.setText(res.getString(5));
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        public TextView txtTitle, txtDateOfPublication, txtAuthor;
        public DatabaseHelper databaseHelper;

        public NewsViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDateOfPublication = itemView.findViewById(R.id.txtDateOfPublication);
            txtAuthor = itemView.findViewById(R.id.txtAuthor);
            databaseHelper = new DatabaseHelper(itemView.getContext());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = itemView.getContext();
                    Intent intent = new Intent(context, NewsInfoActivity.class);
                    intent.putExtra("IdNews", (int) itemView.getTag());
                    context.startActivity(intent);
                }
            });
        }
    }
}