package com.example.popularmoviesstage2.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviesstage2.R;
import com.example.popularmoviesstage2.models.Video;

import java.util.List;

public class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.TrailerItemViewHolder> {
    private List<Video> mDataset;

    public TrailerListAdapter(List<Video> videos) {
        mDataset = videos;
    }

    public static class TrailerItemViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mTrailerItem;
        public TextView mTrailerText;


        public TrailerItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mTrailerItem = (LinearLayout) itemView;
            mTrailerText = itemView.findViewById(R.id.trailer_item_text);
        }
    }

    @NonNull
    @Override
    public TrailerItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_list_item, parent, false);
        return new TrailerItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerItemViewHolder holder, int position) {
        final Video trailer = mDataset.get(position);
        holder.mTrailerText.setText(trailer.getName());

        holder.mTrailerItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri videoUri = Uri.parse("https://youtube.com/watch?v=" + trailer.getKey());
                Intent videoIntent = new Intent(Intent.ACTION_VIEW, videoUri);
                if (videoIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
                    v.getContext().startActivity(videoIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDataset == null) {
            return 0;
        }
        return mDataset.size();
    }

    public void setVideos(List<Video> videos) {
        mDataset.clear();
        mDataset.addAll(videos);
        notifyDataSetChanged();
    }
}
