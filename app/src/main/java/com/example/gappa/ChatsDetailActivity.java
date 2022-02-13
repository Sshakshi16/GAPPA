package com.example.gappa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Toast;

import com.example.gappa.Adapters.ChatAdapter;
import com.example.gappa.Models.MessageModel;
import com.example.gappa.databinding.ActivityChatsDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatsDetailActivity extends AppCompatActivity {

    ActivityChatsDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatsDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        String senderId = auth.getUid();
        String recieveId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");

        binding.userNameChats.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.o2).into(binding.profileImage);

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatsDetailActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });



        final ArrayList<MessageModel>messageModels = new ArrayList<>();

        final ChatAdapter chatAdapter = new ChatAdapter(messageModels,this,recieveId);
        binding.chatsRecyclerView.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatsRecyclerView.setLayoutManager(layoutManager);



        final String senderRoom = senderId + recieveId;
        final String receiverRoom = recieveId + senderId;

        database.getReference().child("Chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageModels.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren())
                        {
                            MessageModel model = snapshot1.getValue(MessageModel.class);
                            model.setMessageId(snapshot1.getKey());
                            messageModels.add(model);
                        }

                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        binding.sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.sendMessageEditText.getText().toString().isEmpty())
                {
                    Toast.makeText(ChatsDetailActivity.this,"Write Something",Toast.LENGTH_SHORT).show();
                }

                else {

                    String message = binding.sendMessageEditText.getText().toString();
                    final MessageModel model = new MessageModel(senderId, message);
                    model.setTime(new Date().getTime());
                    binding.sendMessageEditText.setText("");

                    database.getReference().child("Chats")
                            .child(senderRoom)
                            .push()
                            .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });

                    database.getReference().child("Chats")
                            .child(receiverRoom)
                            .push()
                            .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                }
            }
        });
    }
}