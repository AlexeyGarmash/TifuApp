package com.awashwinter.tifuapp.ui.login;

import android.net.Uri;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {

    private Uri avatartUri;
    private String displayName;
    private String uid;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName) {
        this.displayName = displayName;
    }

    LoggedInUserView(String displayName, String uid, Uri avatar){
        this.displayName = displayName;
        this.uid = uid;
        this.avatartUri = avatar;
    }

    String getDisplayName() {
        return displayName;
    }

    public Uri getAvatartUri() {
        return avatartUri;
    }

    public String getUid() {
        return uid;
    }
}
