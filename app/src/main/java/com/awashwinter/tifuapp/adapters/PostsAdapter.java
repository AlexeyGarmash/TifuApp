package com.awashwinter.tifuapp.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.awashwinter.tifuapp.R;
import com.awashwinter.tifuapp.base.Utils;
import com.awashwinter.tifuapp.data.model.TifuPost;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder> {

    public interface OnLikeDiskileClickListener{
        void onLikeClick(TifuPost post);
        void onDisLikeClick(TifuPost post);
    }

    private OnLikeDiskileClickListener onLikeDiskileClickListener;
    class PostViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvUsername)
        TextView tvUsername;

        @BindView(R.id.tvTitle)
        TextView tvTitle;

        @BindView(R.id.tvContent)
        TextView tvContent;

        @BindView(R.id.tvTimestamp)
        TextView tvTimestamp;

        @BindView(R.id.tvModerated)
        TextView tvModerated;

        @BindView(R.id.avatar)
        ImageView imageViewAvatar;

        @BindView(R.id.btnLike)
        ImageButton btnLike;

        @BindView(R.id.tvLikesCount)
        TextView tvLikesCount;

        @BindView(R.id.btnDisLike)
        ImageButton btnDisLike;

        @BindView(R.id.tvDisLikesCount)
        TextView tvDisLikesCount;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(TifuPost tifuPost){
            tvUsername.setText(tifuPost.getUser().getDisplayName());
            tvTitle.setText(tifuPost.getTitle());
            tvContent.setText(tifuPost.getContent());
            tvTimestamp.setText(tifuPost.getTimeString());
            tvModerated.animate().alpha(tifuPost.isModerated()? 1f : 0f);
            Utils.setImage(imageViewAvatar, tifuPost.getUser().getAvatar());


            tvLikesCount.setText(String.valueOf(tifuPost.getLikes().size()));
            tvDisLikesCount.setText(String.valueOf(tifuPost.getDislikes().size()));


            btnLike.setOnClickListener(view -> {
                onLikeDiskileClickListener.onLikeClick(tifuPost);
            });

            btnDisLike.setOnClickListener(view -> {
                onLikeDiskileClickListener.onDisLikeClick(tifuPost);
            });

            btnLike.setBackgroundResource(tifuPost.likeIsYour()?R.drawable.ic_like_your_24dp: R.drawable.ic_like_neutral_24dp);

            btnDisLike.setBackgroundResource(tifuPost.dislikeIsYour()?R.drawable.ic_dislike_your_24dp: R.drawable.ic_dislike_neutral_24dp);

        }
    }

    private List<TifuPost> tifuPosts;


    public PostsAdapter(){
        setHasStableIds(true);
        tifuPosts = new ArrayList<>();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.bind(tifuPosts.get(position));
    }

    @Override
    public int getItemCount() {
        return tifuPosts.size();
    }

    public List<TifuPost> getTifuPosts() {
        return tifuPosts;
    }

    public void setTifuPosts(List<TifuPost> tifuPosts) {
        this.tifuPosts.clear();
        this.tifuPosts.addAll(tifuPosts);
    }

    public void setOnLikeDiskileClickListener(OnLikeDiskileClickListener onLikeDiskileClickListener) {
        this.onLikeDiskileClickListener = onLikeDiskileClickListener;
    }

    @Override
    public long getItemId(int position) {
        return (long) tifuPosts.get(position).getPostId().hashCode();
    }
}
