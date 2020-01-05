package com.awashwinter.tifuapp.base;

import android.net.Uri;
import android.widget.ImageView;

import com.awashwinter.tifuapp.data.model.LoggedInUser;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Utils {

    public static String getTime(long timeStamp){
        try{
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timeStamp);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
            Date date = (Date) calendar.getTime();
            return sdf.format(date);
        }catch (Exception e) {
        }
        return "";
    }

    public static Uri getAvatarUriFromUrl(String name){
        String fin = String.format("https://api.adorable.io/avatars/100/%s.png", name);
        return Uri.parse(fin);
    }

    public static void setImage(ImageView imageView, String uri){
        Glide
                .with(imageView.getContext())
                .load(uri)
                .centerCrop()
                .into(imageView);
    }

    public static LoggedInUser getUser(FirebaseUser firebaseUser){
        return new LoggedInUser(firebaseUser.getUid(), firebaseUser.getDisplayName(), Objects.requireNonNull(firebaseUser.getPhotoUrl()));
    }
}
