package com.sogeum0310.javamemo;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.Holder> {
    private Cursor cursor;
    private Context context;

    public MemoAdapter(Context context, Cursor cursor){
        this.context = context;
        this.cursor = cursor;
    }
    @NonNull
    @Override
    public MemoAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memo, parent,false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MemoAdapter.Holder holder, int position) {
        if(!cursor.moveToPosition(position))
            return;
        String content = cursor.getString(cursor.getColumnIndex(MemoData.Memolist.content));
        String feel = cursor.getString(cursor.getColumnIndex(MemoData.Memolist.feel));

        System.out.println(content);
        holder.content.setText(content);

    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView content;
        ImageView feel;

        public Holder(@NonNull View itemView) {
            super(itemView);
            this.content = (TextView) itemView.findViewById(R.id.content);
            this.feel = (ImageView) itemView.findViewById(R.id.feel);
        }
    }
}
