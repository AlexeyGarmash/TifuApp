package com.awashwinter.tifuapp.data.model.comparators;

import com.awashwinter.tifuapp.data.model.TifuPost;
import com.awashwinter.tifuapp.usecases.SortType;

import java.util.Comparator;

public class LikeComparator implements Comparator<TifuPost> {

    private SortType sortType;

    public LikeComparator(SortType sortType){
        this.sortType = sortType;
    }

    @Override
    public int compare(TifuPost t1, TifuPost t2) {
        switch (sortType) {
            case LIKE_DESCENDING:
                return likeCountCompare(t1,t2);
            case LIKE_ASCENDING:
                return likeCountCompare(t2,t1);
        }
        return 0;
    }

    private int likeCountCompare(TifuPost t1, TifuPost t2){
        return t2.getLikes().size() - t1.getLikes().size();
    }
}
