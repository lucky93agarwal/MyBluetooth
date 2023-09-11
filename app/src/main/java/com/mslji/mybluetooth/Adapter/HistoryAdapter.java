package com.mslji.mybluetooth.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mslji.mybluetooth.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mslji.mybluetooth.Model.GetHistoryData;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ChatsViewHolder> {

    private Context context;
    private ArrayList<GetHistoryData> chats;

    public HistoryAdapter(Context context, ArrayList<GetHistoryData> chats) {
        this.context = context;
        this.chats = chats;
    }

    public void filterList(ArrayList<GetHistoryData> filteredList) {
        chats = filteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ChatsViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_row_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsViewHolder chatsViewHolder, int i) {

        GetHistoryData chat = chats.get(i);




        String[] arrayString = chat.getNewtime().split("-");
        String[] arrayStringtwo = arrayString[3].split(" ");
        chatsViewHolder.tvtime.setText(arrayString[1].toString()+" "+arrayString[2].toString()+" "+arrayStringtwo[0].toString());
        chatsViewHolder.tvone.setText(chat.getOnetv());
        chatsViewHolder.tvtwo.setText(chat.getTwotv());
        chatsViewHolder.tvthree.setText(chat.getThreetv());
        chatsViewHolder.ididtv.setText(chat.getId());


    }

    @Override
    public int getItemCount() {
        if (chats != null) {
            return chats.size();
        }
        return 0;
    }

    class ChatsViewHolder extends RecyclerView.ViewHolder {

        TextView tvone;
        TextView tvtime;
        TextView tvtwo;
        TextView tvthree;
        TextView ididtv;
        RelativeLayout chatLayoutContainer;

        ChatsViewHolder(@NonNull View itemView) {
            super(itemView);

            ididtv = (TextView)itemView.findViewById(R.id.idtv);

            tvtime = (TextView)itemView.findViewById(R.id.timeet);
            tvone = itemView.findViewById(R.id.onethet);
            tvtwo = itemView.findViewById(R.id.twothet);
            tvthree = itemView.findViewById(R.id.threethet);





        }
    }
}


