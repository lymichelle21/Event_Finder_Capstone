package com.capstone.event_finder.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.capstone.event_finder.models.Photo;
import com.capstone.event_finder.R;
import com.parse.ParseFile;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private final Context context;
    private final List<Photo> photos;
    private Animator currentAnimator;
    private int shortAnimationDuration = 300;
    RelativeLayout rlContainer;

    public PhotoAdapter(Context context, List<Photo> photos) {
        this.context = context;
        this.photos = photos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Photo post = photos.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageButton ibAlbumPhotoItem;
        private final ImageView ivExpandedPhoto;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivExpandedPhoto = itemView.findViewById(R.id.ivExpandedPhoto);
            ibAlbumPhotoItem = itemView.findViewById(R.id.ibAlbumPhotoItem);
            rlContainer = itemView.findViewById(R.id.rlContainer);
            ibAlbumPhotoItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context.getApplicationContext(), "hey", Toast.LENGTH_SHORT).show();
                    zoomImage(ibAlbumPhotoItem, ivExpandedPhoto);
                }
            });
        }

        public void bind(Photo post) {
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ibAlbumPhotoItem);
                Glide.with(context).load(image.getUrl()).into(ivExpandedPhoto);
            }
        }
    }

    private void zoomImage(ImageButton ibAlbumPhotoItem, ImageView ivExpandedPhoto) {
        return;
    }
}
