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
import com.awashwinter.tifuapp.ui.login.LoginActivity;
import com.awashwinter.tifuapp.ui.login.LoginFormState;
import com.awashwinter.tifuapp.ui.login.LoginResult;
import com.awashwinter.tifuapp.ui.login.LoginViewModel;
import com.awashwinter.tifuapp.ui.login.LoginViewModelFactory;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {



    @BindView(R.id.editUsername)
    EditText mEditTextEmail;

    @BindView(R.id.editPassword)
    EditText mEditTextPassword;

    @BindView(R.id.btnLogin)
    Button mButtonLogin;

    @BindView(R.id.loading)
    ProgressBar mProgressBarLoading;

    @BindView(R.id.btnGoRegister)
    Button mButtonGoRegister;

    private LoginViewModel loginViewModel;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
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

        mButtonGoRegister.setOnClickListener(view1 -> {
            Navigation.findNavController(view1).navigate(R.id.action_loginFragment_to_registerFragment);
        });

        init();
    }

    private void init(){
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                mButtonLogin.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    mEditTextEmail.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    mEditTextPassword.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

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
                loginViewModel.loginDataChanged(mEditTextEmail.getText().toString(),
                        mEditTextPassword.getText().toString());
            }
        };
        mEditTextEmail.addTextChangedListener(afterTextChangedListener);
        mEditTextPassword.addTextChangedListener(afterTextChangedListener);
        mEditTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(mEditTextEmail.getText().toString(),
                            mEditTextPassword.getText().toString());
                }
                return false;
            }
        });

        mButtonLogin.setOnClickListener(v -> {
            mProgressBarLoading.setVisibility(View.VISIBLE);
            loginViewModel.login(mEditTextEmail.getText().toString(),
                    mEditTextPassword.getText().toString());
        });

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
