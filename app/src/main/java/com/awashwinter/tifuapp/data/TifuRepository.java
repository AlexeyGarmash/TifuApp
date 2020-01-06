package com.awashwinter.tifuapp.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.awashwinter.tifuapp.base.TifuApp;
import com.awashwinter.tifuapp.data.model.LoggedInUser;
import com.awashwinter.tifuapp.data.model.TifuPost;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class TifuRepository {


    public interface OnDataChangedListener{
        void onDataChangeStarted();
        void onDataChanged(List<TifuPost> tifuPosts);
    }


    private DatabaseReference databaseReference;
    private OnDataChangedListener onDataChangedListener;

    List<TifuPost> tifuPosts;

    public TifuRepository(){
        databaseReference = TifuApp.getDatabase().getReference("posts");
    }

    public void getDataOnce(){
        tifuPosts = new ArrayList<>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getListTifus(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getData(){
        tifuPosts = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getListTifus(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getListTifus(DataSnapshot dataSnapshot){
        onDataChangedListener.onDataChangeStarted();
        tifuPosts.clear();
        for(DataSnapshot post: dataSnapshot.getChildren()){
            TifuPost tifuPost = post.getValue(TifuPost.class);
            if (tifuPost != null && tifuPost.isAccessed())
                tifuPosts.add(tifuPost);
        }
        //Collections.sort(tifuPosts);
        onDataChangedListener.onDataChanged(tifuPosts);
    }

    public void createPost(String title, String content){
        String key = databaseReference.push().getKey();
        TifuPost tifuPost = new TifuPost(key, title, content, true, false);
        FirebaseUser firebaseUser = TifuApp.getFirebaseAuth().getCurrentUser();
        tifuPost.setUser(new LoggedInUser(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getPhotoUrl()));
        databaseReference.child(key).setValue(tifuPost);
    }

    public void setLikeOrDis(LoggedInUser user, TifuPost tifuPost, String key){
        DatabaseReference likesChild = databaseReference.child(tifuPost.getPostId()).child(key);

        DatabaseReference userLikeChild = likesChild.child(user.getUserId());
        userLikeChild.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    userLikeChild.setValue(user)
                            .addOnSuccessListener(aVoid -> Log.d("TRANSACTION_LIKES", "LIKE SUCCESS"))
                            .addOnFailureListener(e -> { Log.d("TRANSACTION_LIKES", "LIKE FAIL" + e.getMessage()); });
                }
                else {
                    userLikeChild.removeValue()
                            .addOnSuccessListener(aVoid -> Log.d("TRANSACTION_LIKES", "LIKE REMOVE SUCCESS"))
                            .addOnFailureListener(e -> { Log.d("TRANSACTION_LIKES", "LIKE REMOVE FAIL" + e.getMessage()); });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("USER_LIKE", "DATABASE_ERROR " + databaseError.getMessage());
            }
        });


    }
    public void setOnDataChangedListener(OnDataChangedListener onDataChangedListener) {
        this.onDataChangedListener = onDataChangedListener;
    }
}
