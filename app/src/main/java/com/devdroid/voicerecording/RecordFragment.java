package com.devdroid.voicerecording;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import android.Manifest;


public class RecordFragment extends Fragment implements View.OnClickListener {


    private NavController navController;

    // listImage view on record fragment
    private ImageButton list_btn;
    private ImageButton record_btn;
    private boolean isRecording = false;
    private static final String recordPermissions = Manifest.permission.RECORD_AUDIO;
    private static final int PERMISSION_CODE = 210;

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

        list_btn.setOnClickListener(this);
        record_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.list_btn:
                navController.navigate(R.id.action_recordFragment_to_audioListFragment);
                break;

            case R.id.record_btn:
                if(isRecording)
                {
                    // stop Recording
                    record_btn.setImageDrawable(getResources().getDrawable(R.drawable.mic_red,null));
                    isRecording = false;
                }
                else
                {
                    // start Recording
                    if (checkPermissions()) {
                        record_btn.setImageDrawable(getResources().getDrawable(R.drawable.mic_re, null));
                        isRecording = true;
                    }
                }
                break;
        }

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted, start recording
                    Log.d("RecordFragment", "mic_re drawable ID: " + R.drawable.mic_red);
                    record_btn.setImageDrawable(getResources().getDrawable(R.drawable.mic_red, null));
                    isRecording = true;
                } else {
                    // permission denied, show message
                    Toast.makeText(getContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
