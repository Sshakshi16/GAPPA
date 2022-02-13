package com.example.gappa.Adapters;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gappa.Models.MessageModel;
import com.example.gappa.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;


public class ChatAdapter extends RecyclerView.Adapter {

    ArrayList<MessageModel>messageModel;
    Context context;
    String recId;

    int SENDER = 1;
    int RECEIVER = 2;

    public ChatAdapter(ArrayList<MessageModel> messageModel, Context context)
    {
        this.messageModel = messageModel;
        this.context = context;
    }

    public ChatAdapter(ArrayList<MessageModel> messageModel, Context context, String recId) {
        this.messageModel = messageModel;
        this.context = context;
        this.recId = recId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if(viewType == SENDER)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new SendViewHolder(view);
        }

        else
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_reciever,parent,false);
            return new RecieverViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        MessageModel messageModel1 = messageModel.get(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are You Sure to delete this message?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String senderRoom = FirebaseAuth.getInstance().getUid() + recId;
                                database.getReference().child("Chats").child(senderRoom)
                                        .child(messageModel1.getMessageId())
                                        .setValue(null);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                return false;
            }
        });

        if(holder.getClass()==SendViewHolder.class)
        {
            ((SendViewHolder)holder).senderMessageTextView.setText(messageModel1.getMsg());
        }

        else
        {
            ((RecieverViewHolder)holder).recieverMessageTextView.setText(messageModel1.getMsg());
        }

    }



    @Override
    public int getItemViewType(int position) {

        if(messageModel.get(position).getuId().equals(FirebaseAuth.getInstance().getUid()))
        {
            return SENDER;
        }

        else
        {
            return RECEIVER;
        }
    }



    @Override
    public int getItemCount() {
        return messageModel.size();
    }


    public class SendViewHolder extends RecyclerView.ViewHolder
    {
        TextView senderMessageTextView, senderTimeTextView;

        public SendViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMessageTextView = itemView.findViewById(R.id.senderMessageTextView);
            senderTimeTextView = itemView.findViewById(R.id.senderTimeTextView);

        }
    }

    public class RecieverViewHolder extends RecyclerView.ViewHolder
    {
        TextView recieverMessageTextView, recieverTimeTextView;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);

            recieverMessageTextView = itemView.findViewById(R.id.recieverMessageTextView);
            recieverTimeTextView = itemView.findViewById(R.id.recieverTimeTextView);

        }
    }
}
