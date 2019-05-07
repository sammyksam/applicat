package com.example.applicat.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.applicat.GetTimeAgo;
import com.example.applicat.R;
import com.example.applicat.models.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    List<Message> msg_list;
    Context applicationContext;
    public MessageAdapter(List<Message> msg_list, Context applicationContext) {
        this.msg_list = msg_list;
        this.applicationContext = applicationContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(applicationContext).inflate(R.layout.activity_messaging__adapter, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Message messages = msg_list.get(i);
        String chats = messages.getMessage();
        long time = messages.getTimestamp();
        viewHolder.msgView.setText(chats);
        GetTimeAgo getTimeAgo = new GetTimeAgo();
        String disp = getTimeAgo.getTimeAgo(time, applicationContext);

        viewHolder.dateView.setText(disp);

    }

    @Override
    public int getItemCount() {
        return msg_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView msgView;
        TextView dateView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            msgView = mView.findViewById(R.id.txt_Content);
            dateView = mView.findViewById(R.id.txt_time);
        }
    }
}
