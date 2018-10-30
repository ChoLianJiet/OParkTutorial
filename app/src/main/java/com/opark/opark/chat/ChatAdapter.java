package com.opark.opark.chat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.opark.opark.R;
import com.opark.opark.share_parking.MapsMainActivity;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {

    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private String mDisplayName;
    private ArrayList<DataSnapshot> mSnapshotList;
    private String foundUser = MapsMainActivity.foundUser;
    private LayoutInflater inflater;

    private ChildEventListener mListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            mSnapshotList.add(dataSnapshot);
            notifyDataSetChanged();
            Log.d("Chat","Datasnapshot contains " + mSnapshotList);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public ChatAdapter(Activity activity, DatabaseReference ref, String name) {
        mActivity = activity;
        mDisplayName = name;
        mDatabaseReference = ref.child("messages");
        mDatabaseReference.addChildEventListener(mListener);
        mSnapshotList = new ArrayList<>();
    }

    static class ViewHolder{
        TextView authorName;
        TextView body;
        LinearLayout.LayoutParams params;
    }

    @Override
    public int getCount() {
        return mSnapshotList.size();
    }

    @Override
    public ChatMessage getItem(int position) {
        DataSnapshot snapshot = mSnapshotList.get(position);
        return snapshot.getValue(ChatMessage.class);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){

            inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_chat_row_sent,parent,false);
            final ViewHolder holder = new ViewHolder();
            holder.authorName = (TextView) convertView.findViewById(R.id.message_user);
            holder.body = (TextView) convertView.findViewById(R.id.message_text);
            holder.params = (LinearLayout.LayoutParams) holder.authorName.getLayoutParams();
            convertView.setTag(holder);

        }

        final ChatMessage message = getItem(position);
        final ViewHolder holder = (ViewHolder) convertView.getTag();

        boolean isMe = message.getMessageUser().equals(mDisplayName);
        setChatRowAppreance(isMe,convertView);

        String author = message.getMessageUser();
        holder.authorName.setText(author);

        String msg = message.getMessageText();
        holder.body.setText(msg);

        return convertView;
    }

    private void setChatRowAppreance(boolean isItMe, View v){

        if (isItMe){
//            holder.params.gravity = Gravity.END;
//            holder.authorName.setTextColor(Color.GREEN);
//            holder.body.setBackgroundResource(R.drawable.bubble1);
            v = inflater.inflate(R.layout.list_chat_row_sent,null);
        } else {
//            holder.params.gravity = Gravity.START;
//            holder.authorName.setTextColor(Color.BLUE);
//            holder.body.setBackgroundResource(R.drawable.bubble2);
            v = inflater.inflate(R.layout.list_chat_row_recv,null);
        }

//        holder.authorName.setLayoutParams(holder.params);
//        holder.body.setLayoutParams(holder.params);
    }

    public void cleanup(){
        mDatabaseReference.removeEventListener(mListener);
    }
}
