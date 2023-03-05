package com.devdroid.voicerecording;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class RecordFragment extends Fragment implements View.OnClickListener {


    private NavController navController;

    // listImage view on record fragment
    private ImageButton list_btn;
    private ImageButton record_btn;
    private boolean isRecording = false;
    private static final String recordPermissions = Manifest.permission.RECORD_AUDIO;
    private static final int PERMISSION_CODE = 210;
    // Variable for startRecording
    private MediaRecorder mediaRecorder;
    // Variable to store Files
    private  String recordFile;
    private Chronometer timer;
    private TextView fileNameText;


    public RecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        list_btn = view.findViewById(R.id.list_btn);
        record_btn = view.findViewById(R.id.record_btn);
        timer = view.findViewById(R.id.record_timer);
        fileNameText = view.findViewById(R.id.record_fileName);

        list_btn.setOnClickListener(this);
        record_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.list_btn:
                if (isRecording) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            navController.navigate(R.id.action_recordFragment_to_audioListFragment);
                            isRecording = false;
                        }
                    });
                    alertDialog.setNegativeButton("CANCEL",null);
                    alertDialog.setTitle("Audio Still recording");
                    alertDialog.setMessage("Are you sure, you want to stop the recording");
                    alertDialog.create().show();
                }
                else {
                    navController.navigate(R.id.action_recordFragment_to_audioListFragment);
                }
                break;

            case R.id.record_btn:
                if(isRecording)
                {
                    // stop Recording
                    stopRecording();
                    record_btn.setImageDrawable(getResources().getDrawable(R.drawable.mic_re,null));
                    isRecording = false;
                }
                else
                {
                    // start Recording
                    if (checkPermissions()) {
                        startRecording();
                        record_btn.setImageDrawable(getResources().getDrawable(R.drawable.mic_red, null));
                        isRecording = true;
                    }
                }
                break;
        }

    }

    private void startRecording() {

        // When the user press the start button timer is start
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        String recordPath = getActivity().getExternalFilesDir("/").getAbsolutePath();

        // To make the file name Dynamic We use SimpleDataFormat
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.CANADA);

        // Date Object
        Date date = new Date();

        recordFile = "Recording_" + dateFormat.format(date) +  ".3gp";

        // Set the filename on textView
        fileNameText.setText("Recording, File Name: " + recordFile);

        // Initialize MediaPlayer here
        mediaRecorder = new MediaRecorder();
        // set the Audio Source
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // Output format of MediaRecord
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(recordPath+"/"+recordFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
    }

    private void stopRecording() {

        // Set the filename on textView
        fileNameText.setText("Recording Stopped, File Saved : " + recordFile);


        // When the user press the stop button timer is stop
        timer.stop();

        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    private boolean checkPermissions() {
        // if we have permissions to record
        Log.d("RecordFragment", "checkPermissions() called");
        if (ActivityCompat.checkSelfPermission(getContext(), recordPermissions) == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{recordPermissions}, PERMISSION_CODE);
            return false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isRecording)
        {
            stopRecording();
        }
    }
}
