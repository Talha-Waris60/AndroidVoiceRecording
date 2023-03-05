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
    public AudioListAdapter(File[] allAudioFiles)
    {
        this.allAudioFiles = allAudioFiles;
    }
    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item,parent,false);
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        // Set the file title
        holder.list_title.setText(allAudioFiles[position].getName());
        holder.list_date.setText(allAudioFiles[position].lastModified() + "");

    }

    @Override
    public int getItemCount() {
        return allAudioFiles.length;
    }

    public class AudioViewHolder extends RecyclerView.ViewHolder{

        private ImageView list_image;
        private TextView list_title;
        private TextView list_date;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);
            list_image = itemView.findViewById(R.id.play_circle_button);
            list_title = itemView.findViewById(R.id.list_title);
            list_date = itemView.findViewById(R.id.list_date);
        }
    }
}
