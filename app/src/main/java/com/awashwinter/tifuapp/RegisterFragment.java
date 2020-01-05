package com.awashwinter.tifuapp;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.awashwinter.tifuapp.ui.login.LoggedInUserView;
import com.awashwinter.tifuapp.ui.login.LoginFormState;
import com.awashwinter.tifuapp.ui.login.LoginResult;
import com.awashwinter.tifuapp.ui.login.LoginViewModel;
import com.awashwinter.tifuapp.ui.login.LoginViewModelFactory;
import com.awashwinter.tifuapp.ui.register.RegisterFormState;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    @BindView(R.id.btnGoLogin)
    Button mButtonGoLogin;

    @BindView(R.id.editUsername)
    EditText mEditTextEmail;

    @BindView(R.id.editPassword)
    EditText mEditTextPassword;

    @BindView(R.id.editPasswordConfirm)
    EditText mEditTextPassConfirm;

    @BindView(R.id.editDisplayName)
    EditText mEditTextDisplayName;

    @BindView(R.id.btnRegister)
    Button mButtonRegister;

    @BindView(R.id.loading)
    ProgressBar mProgressBarLoading;


    private LoginViewModel loginViewModel;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), new LoginViewModelFactory())
                .get(LoginViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mButtonGoLogin.setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment);
        });
        init();
    }

    private void init(){

        registerFormStateInit();

        loginResultInit();

        setupTextWatchers(textWatcherInit());

        mEditTextDisplayName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                register();
            }
            return false;
        });

        mButtonRegister.setOnClickListener(v -> {
            mProgressBarLoading.setVisibility(View.VISIBLE);
            register();
        });

    }

    private void setupTextWatchers(TextWatcher afterTextChangedListener){
        mEditTextEmail.addTextChangedListener(afterTextChangedListener);
        mEditTextPassword.addTextChangedListener(afterTextChangedListener);
        mEditTextPassConfirm.addTextChangedListener(afterTextChangedListener);
        mEditTextDisplayName.addTextChangedListener(afterTextChangedListener);
    }

    private TextWatcher textWatcherInit(){
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.registerDataChanged(
                        mEditTextEmail.getText().toString(),
                        mEditTextPassword.getText().toString(),
                        mEditTextPassConfirm.getText().toString(),
                        mEditTextDisplayName.getText().toString());
            }
        };
        return afterTextChangedListener;
    }

    private void loginResultInit() {
        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                mProgressBarLoading.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                //setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                //finish();
            }
        });
    }

    private void registerFormStateInit(){
        loginViewModel.getRegisterFormState().observe(this, new Observer<RegisterFormState>() {
            @Override
            public void onChanged(@Nullable RegisterFormState registerFormState) {
                if (registerFormState == null) {
                    return;
                }
                mButtonRegister.setEnabled(registerFormState.isDataValid());
                if (registerFormState.getUsernameError() != null) {
                    mEditTextEmail.setError(getString(registerFormState.getUsernameError()));
                }
                if (registerFormState.getPasswordError() != null) {
                    mEditTextPassword.setError(getString(registerFormState.getPasswordError()));
                }
                if (registerFormState.getConfirmPasswordError() != null) {
                    mEditTextPassConfirm.setError(getString(registerFormState.getConfirmPasswordError()));
                }
                if (registerFormState.getDisplayNameError() != null) {
                    mEditTextDisplayName.setError(getString(registerFormState.getDisplayNameError()));
                }
            }
        });
    }

    private void register(){
        loginViewModel.register(mEditTextEmail.getText().toString(),
                mEditTextPassword.getText().toString(), mEditTextDisplayName.getText().toString());
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getContext(), welcome, Toast.LENGTH_LONG).show();
        //goToMainAct();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void goToMainAct(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }
}
