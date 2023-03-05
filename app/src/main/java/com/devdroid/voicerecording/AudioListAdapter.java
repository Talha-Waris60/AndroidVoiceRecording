package com.devdroid.voicerecording;

import android.icu.text.Transliterator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.AudioViewHolder> {

    private File[] allAudioFiles;
    // Declare Time class
    private TimeAgo timeAgo;
    private onItemListClick onItemListClick;
    public AudioListAdapter(File[] allAudioFiles, onItemListClick onItemListClick)
    {
        this.allAudioFiles = allAudioFiles;
        this.onItemListClick = onItemListClick;
    }
    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item,parent,false);
        timeAgo = new TimeAgo();
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        // Set the file title
        holder.list_title.setText(allAudioFiles[position].getName());
        holder.list_date.setText(timeAgo.getTimeAgo(allAudioFiles[position].lastModified()));

    }

    @Override
    public int getItemCount() {
        return allAudioFiles.length;
    }

    public class AudioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView list_image;
        private TextView list_title;
        private TextView list_date;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);
            list_image = itemView.findViewById(R.id.play_circle_button);
            list_title = itemView.findViewById(R.id.list_title);
            list_date = itemView.findViewById(R.id.list_date);

            // Set onClick listener
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemListClick.onClickListener(allAudioFiles[getAdapterPosition()], getAdapterPosition());
        }
    }

    public interface onItemListClick {
        void onClickListener(File file, int position);
    }
}
