package com.awashwinter.tifuapp.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.awashwinter.tifuapp.MainActivity;
import com.awashwinter.tifuapp.R;
import com.awashwinter.tifuapp.ui.login.LoginViewModel;
import com.awashwinter.tifuapp.ui.login.LoginViewModelFactory;

public class LoginActivity extends AppCompatActivity {


    //NavController navControllerAuth;
    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //navControllerAuth = Navigation.findNavController(this, R.id.nav_host_fragment);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.getUserLoggedIn().observe(this, aBoolean -> {
            if(aBoolean){
                goToMainAct();
            }
        });

    }

    private void goToMainAct(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
