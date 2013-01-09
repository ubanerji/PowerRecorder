package com.udayan.advanced.recorder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Home extends Activity {

	TimedRecorder timedRecorder;
	
    String mFileName;
    
    boolean isRecordingButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        
        Constants.homeContext = this;
        
        isRecordingButton = true;
        
        String state = Environment.getExternalStorageState();

        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            Toast.makeText(this, "Cannot access external directory", Toast.LENGTH_LONG).show();
            this.finish();
        }
        
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        
        if (!path.exists()) {
        	Toast.makeText(this, "External path does not exist!", Toast.LENGTH_LONG).show();
        }
        
        File finalFilePath = new File(path, "myAudio.wav");
        FileOutputStream finalOutputStream = null;
		try {
			finalOutputStream = new FileOutputStream(finalFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Constants.currentOutputStream = finalOutputStream;
		
        Utility.setRecordingParams();

        Constants.currentTempFileName1 = "bufferTemp";

        setContentView(R.layout.activity_home);

        Button recordButton = (Button) findViewById(R.id.buttonRec);
        recordButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (isRecordingButton) {
					((Button) v).setText("Stop");
			    	Utility.isRecordingActive = true;
			    	startService(new Intent(v.getContext(), TimedRecorder.class));
					isRecordingButton = false;
				}
				else {
					((Button) v).setText("Record");
					((Button) v).setEnabled(false);
			    	Utility.isRecordingActive = false;
					stopService(new Intent(v.getContext(), TimedRecorder.class));
					isRecordingButton = false;
				}
				
			}
		});        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }
}
