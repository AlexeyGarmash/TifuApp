package com.awashwinter.tifuapp.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;
import android.util.Patterns;

import com.awashwinter.tifuapp.base.TifuApp;
import com.awashwinter.tifuapp.base.Utils;
import com.awashwinter.tifuapp.data.LoginRepository;
import com.awashwinter.tifuapp.data.OnLoginResultListener;
import com.awashwinter.tifuapp.data.Result;
import com.awashwinter.tifuapp.data.model.LoggedInUser;
import com.awashwinter.tifuapp.R;
import com.awashwinter.tifuapp.ui.register.RegisterFormState;

import javax.inject.Inject;

public class LoginViewModel extends ViewModel {

    private static final String TAG = LoginViewModel.class.getCanonicalName();
    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private MutableLiveData<Boolean> userLoggedIn = new MutableLiveData<>();
    @Inject
    LoginRepository loginRepository;

    public LoginViewModel() {
        TifuApp.getAppComponent().injectLoginRepository(this);
        loginRepository.setOnLoginResultListener(this::checkLoginResult);
    }

    private void checkLoginResult(Result<LoggedInUser> loggedInUserResult) {
        if (loggedInUserResult instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) loggedInUserResult).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName(), data.getUserId(), Utils.getAvatarUriFromUrl(data.getAvatar()))));
            Log.d(TAG, "LOGIN SUCCESS");
        } else {
            Log.d(TAG, ((Result.Error) loggedInUserResult).getError().getMessage());
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        loginRepository.login(username, password);


    }

    public void register(String email, String password, String displayName){
        loginRepository.register(email, password, displayName);
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    public void registerDataChanged(String email, String password, String confirm, String displayName){
        if(!isUserNameValid(email)){
            registerFormState.setValue(new RegisterFormState(R.string.invalid_username, null, null, null));
        } else if (!isPasswordValid(password)){
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_password, null, null));
        } else if (!isPasswordConfirmValid(password, confirm)) {
            registerFormState.setValue(new RegisterFormState(null, null, R.string.invalid_confirm, null));
        } else if(!isDisplayNameValid(displayName)){
            registerFormState.setValue(new RegisterFormState(null, null, null, R.string.invalid_displayname));
        }
        else {
            registerFormState.setValue(new RegisterFormState(true));
        }
    }

    private boolean isDisplayNameValid(String displayName) {
        return displayName != null && displayName.trim().length() >= 4 && displayName.trim().length() <= 10;
    }

    private boolean isPasswordConfirmValid(String password, String confirm) {
        if(password == null || confirm == null) {
            return  false;
        }
        return password.equals(confirm);
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }




    public LiveData<Boolean> getUserLoggedIn() {
        return loginRepository.getIsLoggedIn();
    }

    public LiveData<RegisterFormState> getRegisterFormState() {
        return registerFormState;
    }
}
