package com.awashwinter.tifuapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.awashwinter.tifuapp.adapters.PostsAdapter;
import com.awashwinter.tifuapp.adapters.PostsDiffUtilCallback;
import com.awashwinter.tifuapp.data.model.TifuPost;
import com.awashwinter.tifuapp.ui.login.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    MainViewModel mainViewModel;

    @BindView(R.id.logoutBtn)
    Button buttonLogout;

    @BindView(R.id.floatBtnAdd)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.rvPosts)
    RecyclerView recyclerViewPosts;

    @BindView(R.id.progressList)
    ProgressBar progressBar;


    private PostsAdapter mPostsAdapter;

    AlertDialog dialogBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupRecyclerView();
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getUserLoggedIn().observe(this, isLoggedIn -> {
            if(!isLoggedIn){
                finish();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

        mainViewModel.getListMutableLiveData().observe(this, this::computeDiff);

        mainViewModel.getNetStateMutableLiveData().observe(this, netState -> {
            progressBar.setVisibility(netState == MainViewModel.NetState.START? View.VISIBLE:View.GONE);
        });


        buttonLogout.setOnClickListener(view -> {
            mainViewModel.logout();
        });

        floatingActionButton.setOnClickListener(view -> {
            //mainViewModel.addTifu(String.format("Title %d", new Random().nextInt()), "Lorem ips");
            dialogBuilder.show();
        });

        mainViewModel.checkUserIsLoggedIn();
        setupDialog();
    }

    private void setupRecyclerView(){
        mPostsAdapter = new PostsAdapter();
        mPostsAdapter.setOnLikeDiskileClickListener(new PostsAdapter.OnLikeDiskileClickListener() {
            @Override
            public void onLikeClick(TifuPost post) {
                mainViewModel.setLike(post);
            }

            @Override
            public void onDisLikeClick(TifuPost post) {
                mainViewModel.setDisLike(post);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewPosts.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewPosts.getContext(),
                linearLayoutManager.getOrientation());
        recyclerViewPosts.addItemDecoration(dividerItemDecoration);
        recyclerViewPosts.setAdapter(mPostsAdapter);
    }

    private void computeDiff(List<TifuPost> newPosts) {
        PostsDiffUtilCallback postsDiffUtilCallback = new PostsDiffUtilCallback(mPostsAdapter.getTifuPosts(), newPosts);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(postsDiffUtilCallback, false);
        mPostsAdapter.setTifuPosts(newPosts);
        diffResult.dispatchUpdatesTo(mPostsAdapter);
    }

    private void setupDialog(){
        dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);

        EditText editTextTitle = dialogView.findViewById(R.id.cdTitle);
        EditText editTextBody = dialogView.findViewById(R.id.cdBody);
        Button buttonCancel = dialogView.findViewById(R.id.cdCancel);
        Button buttonSubmit = dialogView.findViewById(R.id.cdSubmit);

        buttonCancel.setOnClickListener(view -> dialogBuilder.dismiss());
        buttonSubmit.setOnClickListener(view -> {
            String title = editTextTitle.getText().toString();
            String body = editTextBody.getText().toString();
            mainViewModel.addTifu(title, body);
            dialogBuilder.dismiss();
        });
        dialogBuilder.setView(dialogView);
    }
}
