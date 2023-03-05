package com.devdroid.voicerecording;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;


public class AudioListFragment extends Fragment {

    private RelativeLayout playerSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView audioRecyclerView;
    private File[] allAudioFiles;
    private AudioListAdapter audioListAdapter;



    public AudioListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_audio_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playerSheet = view.findViewById(R.id.player_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(playerSheet);
        audioRecyclerView = view.findViewById(R.id.audio_recycle_view);

        // Give the path where we store out video
        String path = getActivity().getExternalFilesDir("/").getAbsolutePath();
        // File Directory
        File directory = new File(path);
        // List all the files of the directory
        allAudioFiles = directory.listFiles();

        audioListAdapter = new AudioListAdapter(allAudioFiles);
        audioRecyclerView.setHasFixedSize(true);
        audioRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        audioRecyclerView.setAdapter(audioListAdapter);

        // Add bottom Sheet call back method
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // Set the state of the sheet
                if (newState == BottomSheetBehavior.STATE_HIDDEN)
                {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // We can do anything here for this app
            }
        });
    }
}