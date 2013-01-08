package com.udayan.advanced.recorder;

import android.content.Context;
import android.content.Intent;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import android.os.Handler;
import android.widget.Toast;

public class CoreRecorder extends Thread {
    
    Context context;
    Handler handler;
    
    CoreRecorderService recorderService;
    
    
    public CoreRecorder(Context context) {
        this.context = context;
        handler = new Handler();
        
        recorderService  = new CoreRecorderService();
        
        Constants.aRecorder = new AudioRecord(AudioSource.MIC, Constants.currentSampleRate, Constants.currentChannelConfig, Constants.currentFormat, Constants.coreBufferSize);
        
        if (Constants.aRecorder.getState() == AudioRecord.STATE_UNINITIALIZED) {
        	Toast.makeText(context, "Could not initialize audio record", Toast.LENGTH_LONG).show();
        }
        else {
        	Toast.makeText(context, "Audio successfully initialized", Toast.LENGTH_SHORT).show();
        }
        
    }
    
    public void beginRecording() {
    	Utility.isRecordingActive = true;
    	Intent intent = new Intent();
    	recorderService.startService(intent);
        
    	
        
    }
    
    public void stopRecording() {
    	Utility.isRecordingActive = false;
    	
    	return;    	
    }
    
    public void run() {
    	
    	
    }

}
