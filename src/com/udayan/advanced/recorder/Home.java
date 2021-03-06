package com.udayan.advanced.recorder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.NotificationManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Home extends Activity {

	CoreRecorder coreRecorder;
	
    String mFileName;
    
    boolean isRecordingButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        
        /* Set Global state variables: */
        
        //Set the home context:
        AppGlobals.homeContext = this;
        //Set the notification manager:
        AppGlobals.notifManager = 
        		  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        
        isRecordingButton = true;
        
        coreRecorder = CoreRecorder.getInstance();
        
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

		AppGlobals.currentOutputStream = finalOutputStream;
		
        Utility.setRecordingParams();

        AppGlobals.currentTempFileName1 = "bufferTemp";

        setContentView(R.layout.activity_home);

        Button recordButton = (Button) findViewById(R.id.buttonRec);
        recordButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (isRecordingButton) {
					((Button) v).setText("Stop");
                    coreRecorder.begin();
					isRecordingButton = false;
				}
				else {
					((Button) v).setText("Record");
					((Button) v).setEnabled(false);
					coreRecorder.stop();
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
