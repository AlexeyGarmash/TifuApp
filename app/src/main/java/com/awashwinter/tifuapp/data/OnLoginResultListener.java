package com.awashwinter.tifuapp.data;

import com.awashwinter.tifuapp.data.model.LoggedInUser;

public interface OnLoginResultListener {
    void loginResult(Result<LoggedInUser> loggedInUserResult);
}
