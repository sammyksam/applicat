package com.example.applicat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.applicat.adapters.MessageAdapter;
import com.example.applicat.models.Message;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class message extends AppCompatActivity {

    ListView lv;
    EditText enter_message;
    Button send;
    FirebaseDatabase db;
    DatabaseReference messages_table;
    ArrayList<Message> messages;
    MessageAdapter adapter;
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    Context c;
    private static final int TOTAL_MESSAGES_TO_LOAD = 10;
    private LinearLayoutManager mLinear;
    private int itemPos = 0;
    private String mLastKey = "";
    private String mPrevKey = "";
    private int currentPage = 1;
    private List<Message> msg_list;
    private DatabaseReference rootRef;
    private MessageAdapter messageAdapter;
    private SwipeRefreshLayout swipe;
    private RecyclerView msg_list_view;
    private ImageButton sendMsg;
    private EditText msg;
    String cUid;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(this, Login.class));
                return true;

            case  R.id.profile:
                startActivity(new Intent(this, Profile.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);


        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

       //
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
             cUid = firebaseAuth.getCurrentUser().getUid();
        }

        msg_list = new ArrayList<>();
        sendMsg = findViewById(R.id.imageButton);
        msg_list_view = findViewById(R.id.msg_view_id);
        swipe = findViewById(R.id.swipe_id);
        msg = findViewById(R.id.editText);
        messageAdapter = new MessageAdapter(msg_list, getApplicationContext());
        rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.keepSynced(true);
        mLinear = new LinearLayoutManager(message.this);

        msg_list_view.setLayoutManager(mLinear);
        msg_list_view.setHasFixedSize(true);
        msg_list_view.setAdapter(messageAdapter);

        rootRef.child("Chosen").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                      final String ids = data.child("id").getValue().toString();
                        getMessages(ids);

                        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                currentPage++;
                                itemPos = 0;
                                getMoreMessages(ids);
                            }
                        });

                        sendMsg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String txt = msg.getText().toString().trim();
                                if(!TextUtils.isEmpty(txt)){
                                    DatabaseReference userPush = rootRef.child("Messages").child(cUid).child(ids).push();
                                    String pushId = userPush.getKey();
                                    Map msgMap = new HashMap();
                                    msgMap.put("timestamp", ServerValue.TIMESTAMP);
                                    msgMap.put("message", txt);

                                    Map userMap = new HashMap();
                                    userMap.put("Messages/" + cUid + "/" + ids + "/" + pushId, msgMap);
                                    userMap.put("Messages/" + ids + "/" + cUid + "/" + pushId, msgMap);

                                    msg.setText("");

                                    rootRef.updateChildren(userMap, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                            Toast.makeText(message.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getMoreMessages(String uid) {

        DatabaseReference msgRef = rootRef.child("Messages").child(cUid).child(uid);
        Query query = msgRef.orderByKey().endAt(mLastKey).limitToLast(10);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    Message message = dataSnapshot.getValue(Message.class);
                    String msgKey = dataSnapshot.getKey();

                    if(!mPrevKey.equals(msgKey)){
                       msg_list.add(itemPos++, message);
                    } else{
                        mPrevKey = mLastKey;
                    }
                    messageAdapter.notifyDataSetChanged();
                    swipe.setRefreshing(false);
                    mLinear.scrollToPositionWithOffset(10,0);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getMessages(String id) {
        DatabaseReference msgRef = rootRef.child("Messages").child(cUid).child(id);
        msgRef.keepSynced(true);
        Query query = msgRef.limitToLast(currentPage * TOTAL_MESSAGES_TO_LOAD);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    Message message = dataSnapshot.getValue(Message.class);
                    itemPos++;

                    if(itemPos == 1){
                        String msgKey = dataSnapshot.getKey();
                        mLastKey = msgKey;
                        mPrevKey = msgKey;
                    }
                    msg_list.add(message);
                    messageAdapter.notifyDataSetChanged();
                    msg_list_view.scrollToPosition(msg_list.size()-1);
                    swipe.setRefreshing(false);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
