package com.awashwinter.tifuapp.experimental.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.awashwinter.tifuapp.R;
import com.awashwinter.tifuapp.adapters.PostsAdapter;
import com.awashwinter.tifuapp.adapters.PostsDiffUtilCallback;
import com.awashwinter.tifuapp.data.model.TifuPost;
import com.awashwinter.tifuapp.experimental.HomeMainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private HomeMainViewModel homeMainViewModel;

    @BindView(R.id.floatBtnAdd)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.rvPosts)
    RecyclerView recyclerViewPosts;

    @BindView(R.id.progressList)
    ProgressBar progressBar;


    private PostsAdapter mPostsAdapter;

    AlertDialog dialogBuilder;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        homeMainViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(HomeMainViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setupRecyclerView();
        homeMainViewModel.getActionSortMutableLiveData().observe(this, sortType -> {
            homeViewModel.applySort(sortType);
        });
        homeViewModel.getListMutableLiveData().observe(this, this::computeDiff);

        homeViewModel.getNetStateMutableLiveData().observe(this, netState -> {
            progressBar.setVisibility(netState == HomeViewModel.NetState.START? View.VISIBLE:View.GONE);
        });
        floatingActionButton.setOnClickListener(viewF -> {
            //mainViewModel.addTifu(String.format("Title %d", new Random().nextInt()), "Lorem ips");
            dialogBuilder.show();
        });

        setupDialog();
    }

    private void setupRecyclerView(){
        mPostsAdapter = new PostsAdapter();
        mPostsAdapter.setOnLikeDiskileClickListener(new PostsAdapter.OnLikeDiskileClickListener() {
            @Override
            public void onLikeClick(TifuPost post) {
                homeViewModel.setLike(post);
            }

            @Override
            public void onDisLikeClick(TifuPost post) {
                homeViewModel.setDisLike(post);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
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
        dialogBuilder = new AlertDialog.Builder(getContext()).create();
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
            homeViewModel.addTifu(title, body);
            dialogBuilder.dismiss();
        });
        dialogBuilder.setView(dialogView);
    }
}