package com.awashwinter.tifuapp.adapters;

import androidx.recyclerview.widget.DiffUtil;

import com.awashwinter.tifuapp.data.model.TifuPost;

import java.util.List;

public class PostsDiffUtilCallback extends DiffUtil.Callback {

    private final List<TifuPost> oldList;
    private final List<TifuPost> newList;


    public PostsDiffUtilCallback(List<TifuPost> oldList, List<TifuPost> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {

        TifuPost oldPost = oldList.get(oldItemPosition);
        TifuPost newPost = newList.get(newItemPosition);

        return oldPost.getPostId().equals(newPost.getPostId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        TifuPost oldPost = oldList.get(oldItemPosition);
        TifuPost newPost = newList.get(newItemPosition);

        return oldPost.getTitle().equals(newPost.getTitle())
                && oldPost.getContent().equals(newPost.getContent())
                && oldPost.getTimestampLong() == newPost.getTimestampLong()
                && oldPost.isAccessed() == newPost.isAccessed()
                && oldPost.isModerated() == newPost.isModerated();
    }
}
