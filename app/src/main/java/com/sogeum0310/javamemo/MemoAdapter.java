package com.sogeum0310.javamemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.Holder> {
    ArrayList<MemoArray> list;
    private Context context;

    public MemoAdapter(Context context, ArrayList<MemoArray> list) {
        this.context = context;
        this.list = list;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    private OnItemClickListener mListener = null;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }


    @NonNull
    @Override
    public MemoAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memo, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MemoAdapter.Holder holder, int position) {
        String content = list.get(position).content;
        int feel = list.get(position).feel;
//⭕🔺️❌
        holder.content.setText(content);
        if (feel == 1) {
            holder.feel.setText("▲");
        } else if (feel == 2) {
            holder.feel.setText("❌");
        } else {
            holder.feel.setText("⭕");
        }

    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }


    public class Holder extends RecyclerView.ViewHolder {
        TextView content;
        TextView feel;

        public Holder(@NonNull View itemView) {
            super(itemView);
            this.content = (TextView) itemView.findViewById(R.id.content);
            this.feel = (TextView) itemView.findViewById(R.id.feel);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener.onItemClick(view, pos);
                        }
                    }
                }
            });
        }
    }
}
