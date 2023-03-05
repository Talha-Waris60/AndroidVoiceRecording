package com.devdroid.voicerecording;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.IOException;
import java.security.PrivateKey;


public class AudioListFragment extends Fragment implements AudioListAdapter.onItemListClick{

    private RelativeLayout playerSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView audioRecyclerView;
    private File[] allAudioFiles;
    private AudioListAdapter audioListAdapter;
    private MediaPlayer mediaPlayer = null;
    private boolean isPlaying = false;
    private File fileToPlay = null;

    // UI Elements
    private ImageButton playButton;
    private TextView playerHeader;
    private TextView playerFilename;

    // Seekbar
    private SeekBar playerSeekbar;
    private Handler seekbarHandler;
    private Runnable updateSeekbar;





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
        playButton = view.findViewById(R.id.play_button);
        playerHeader = view.findViewById(R.id.player_header_title);
        playerFilename = view.findViewById(R.id.player_fileName);
        playerSeekbar = view.findViewById(R.id.player_seekbar);

        // Give the path where we store out video
        String path = getActivity().getExternalFilesDir("/").getAbsolutePath();
        // File Directory
        File directory = new File(path);
        // List all the files of the directory
        allAudioFiles = directory.listFiles();

        audioListAdapter = new AudioListAdapter(allAudioFiles, this);
        audioRecyclerView.setHasFixedSize(true);
        audioRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        audioRecyclerView.setAdapter(audioListAdapter);
        ;

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

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying) {
                    pauseAudio();
                }
                else {
                    if (fileToPlay != null) {
                        resumeAudio();
                    }
                }
            }
        });

        playerSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pauseAudio();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (fileToPlay != null) {
                    int progress = seekBar.getProgress();
                    mediaPlayer.seekTo(progress);
                    resumeAudio();
                }
            }
        });
    }

    @Override
    public void onClickListener(File file, int position) {
        fileToPlay = file;
        if (isPlaying)
        {
            // Stop the Audio
            stopAudio();
            playAudio(fileToPlay);
        }
        else {
            // play the Audio
            playAudio(fileToPlay);
        }
    }

    private void pauseAudio() {
        mediaPlayer.pause();
        playButton.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.play_button,null));
        isPlaying = false;
        seekbarHandler.removeCallbacks(updateSeekbar);
    }
    private void resumeAudio() {
        mediaPlayer.start();
        playButton.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.player_pause_button,null));
        isPlaying = true;

        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar,0);
    }
    private void stopAudio() {
        // Stop the Audio
        playButton.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.play_button,null));
        playerHeader.setText("Stopped");
        isPlaying = false;
        mediaPlayer.stop();
        seekbarHandler.removeCallbacks(updateSeekbar);

    }

    private void playAudio(File filetoPlay) {

        mediaPlayer = new MediaPlayer();

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        try {
            mediaPlayer.setDataSource(filetoPlay.getAbsolutePath());

            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        playButton.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.player_pause_button,null));
        playerFilename.setText(filetoPlay.getName());
        playerHeader.setText("playing");
        // This method will be used to play the audio
        isPlaying = true;

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopAudio();
                playerHeader.setText("Finished");
            }
        });

        // set the maximum progress
        playerSeekbar.setMax(mediaPlayer.getDuration());
        // Initialize Handler
        seekbarHandler = new Handler();
        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar,0);
    }

    private void updateRunnable() {
        updateSeekbar = new Runnable() {
            @Override
            public void run() {
                playerSeekbar.setProgress(mediaPlayer.getCurrentPosition());
                seekbarHandler.postDelayed(this,500);
            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
        stopAudio();
    }
}