package com.capstone.event_finder.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.capstone.event_finder.R;
import com.capstone.event_finder.activities.PhotoDetailsActivity;
import com.capstone.event_finder.models.Photo;
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
            ivAlbumPhotoItem.setOnClickListener(this::goToPhotoDetailsActivity);
        }

        public void bind(Photo post) {
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivAlbumPhotoItem);
            }
        }

        private void goToPhotoDetailsActivity(View v) {
            Photo photo = photos.get(getLayoutPosition());
            Intent intent = new Intent(context, PhotoDetailsActivity.class);
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation((Activity) context, ivAlbumPhotoItem, "photo");
            intent.putExtra(Photo.class.getSimpleName(), Parcels.wrap(photo));
            context.startActivity(intent, options.toBundle());
        }
    }
}
