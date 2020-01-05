package com.awashwinter.tifuapp.ui.register;

import androidx.annotation.Nullable;

import com.awashwinter.tifuapp.ui.login.LoginFormState;

public class RegisterFormState extends LoginFormState {

    @Nullable
    private Integer confirmPasswordError;
    @Nullable
    private Integer displayNameError;

    public RegisterFormState(@Nullable Integer usernameError, @Nullable Integer passwordError, @Nullable Integer confirmPasswordError, @Nullable Integer displayNameError){
        super(usernameError, passwordError);
        this.confirmPasswordError = confirmPasswordError;
        this.displayNameError = displayNameError;
    }

    public RegisterFormState(boolean isDataValid){
        super(isDataValid);
    }

    @Nullable
    public Integer getConfirmPasswordError() {
        return confirmPasswordError;
    }

    @Nullable
    public Integer getDisplayNameError() {
        return displayNameError;
    }
}
