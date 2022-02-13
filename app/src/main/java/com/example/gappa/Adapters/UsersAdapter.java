package com.example.gappa.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gappa.ChatsDetailActivity;
import com.example.gappa.Models.MessageModel;
import com.example.gappa.Models.Users;
import com.example.gappa.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{

    ArrayList<Users> list;
    Context context;

    public UsersAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.sampler_view_pager,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users users = list.get(position);

        Picasso.get().load(users.getProfilepic()).placeholder(R.drawable.o2).into(holder.profilePic);
        holder.userName.setText(users.getUserName());

        FirebaseDatabase.getInstance().getReference().child("Chats").
                child(FirebaseAuth.getInstance().getUid() + users.getUserId())
                .orderByChild("time")
                .limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren())
                        {
                            for(DataSnapshot snapshot1 : snapshot.getChildren())
                            {
//                                MessageModel m = new MessageModel(snapshot1.child("msg").getValue(String.class));
//                                holder.lastMessage.setText(m.getMsg());
                                holder.lastMessage.setText(snapshot1.child("msg").getValue(String.class));
                                //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@" + snapshot1.child("msg").getValue(String.class));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatsDetailActivity.class);
                intent.putExtra("userId", users.getUserId());
                intent.putExtra("profilePic", users.getProfilepic());
                intent.putExtra("userName", users.getUserName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView profilePic;
        TextView userName, lastMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profileImage);
            userName = itemView.findViewById(R.id.userName);
            lastMessage = itemView.findViewById(R.id.lastMessage);

        }
    }
}
