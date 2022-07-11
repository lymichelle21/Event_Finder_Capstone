package com.capstone.event_finder.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.capstone.event_finder.activities.EventDetailsActivity;
import com.capstone.event_finder.activities.MainActivity;
import com.capstone.event_finder.activities.PhotoAlbumActivity;
import com.capstone.event_finder.fragments.FeedFragment;
import com.capstone.event_finder.fragments.PhotoDetailsFragment;
import com.capstone.event_finder.models.Event;
import com.capstone.event_finder.models.Photo;
import com.capstone.event_finder.R;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private final Context context;
    private final List<Photo> photos;

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

        private final ImageView ivAlbumPhotoItem;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAlbumPhotoItem = itemView.findViewById(R.id.ivAlbumPhotoItem);
            ivAlbumPhotoItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context.getApplicationContext(), "here", Toast.LENGTH_LONG).show();
                    // TODO: Use interface to call fragment when photo clicked
                }
            });
        }

        public void bind(Photo post) {
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivAlbumPhotoItem);
            }
        }
    }
}
