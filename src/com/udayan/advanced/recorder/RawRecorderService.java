package com.udayan.advanced.recorder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class RawRecorderService extends Service {

    private FileOutputStream bufferDumpStream;

    private byte[] bData = new byte[AudioConfig.READ_BUFFER_SIZE];

    Intent intent = new Intent(AppGlobals.homeContext, Home.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(AppGlobals.homeContext, 0, intent, 0);
    
    Notification.Builder notifBuilder = new Notification.Builder(AppGlobals.homeContext)
    		.setContentTitle("PowerRecorder recording active")
    		.setContentText("Recording since ")
    		.setContentIntent(pendingIntent);
    
    Notification notif = notifBuilder.getNotification();
    
    
    @Override
	public void onCreate() {
		super.onCreate();
		notif.icon = R.drawable.ic_launcher;
		AppGlobals.notifManager.notify(0, notif);
		beginRecording();
	}
	
	public void beginRecording() {

		AppGlobals.aRecorder = new AudioRecord(AudioSource.MIC, AudioConfig.currentSampleRate, AudioConfig.currentChannelConfig, AudioConfig.currentFormat, AudioConfig.coreBufferSize);

        if (AppGlobals.aRecorder.getState() == AudioRecord.STATE_UNINITIALIZED) {
        	Toast.makeText(AppGlobals.homeContext, "Could not initialize audio record", Toast.LENGTH_LONG).show();
        }
        else {
        	Toast.makeText(AppGlobals.homeContext, "Audio successfully initialized", Toast.LENGTH_SHORT).show();
        }


        new Thread(new Runnable() {
    		public void run() {
    			AppGlobals.aRecorder.startRecording();
	        	
	        	bufferDumpStream = null;
	    		try {
	    			bufferDumpStream = AppGlobals.homeContext.openFileOutput(AppGlobals.currentTempFileName1, Context.MODE_PRIVATE);
	    		} catch (FileNotFoundException e2) {
	    			e2.printStackTrace();
	    		}
	        	
	            int offset = 0;
	            int bytesRead;
	    		    	
	        	do {
	                bytesRead = AppGlobals.aRecorder.read(bData, offset, AudioConfig.coreBufferSize);
	        	    	                
	                if (bytesRead < 0) {
	                	Log.e("Hello", "Breaking bad " + AudioConfig.coreBufferSize);
	                	break;
	                }
	                	
	    	    	try {
	    	            // writes the data to file from buffer
	    	            // // stores the voice buffer
	    	            bufferDumpStream.write(bData, 0, bytesRead);
	    	        } catch (IOException e) { 
	    	            e.printStackTrace();
	    	        }
	    	    	
	    	    	//offset += bytesRead;
	            } while (Utility.isRecordingActive);

	        	Log.e("Hello", "Exiting tight loop");
	        	
	        	AppGlobals.aRecorder.stop();
    		}
    	}).start();
        
        
	}
	
	public void stopRecording(){
		AppGlobals.notifManager.cancelAll();
		
	}

	
	@Override
	public void onDestroy() {
		stopRecording();
		super.onDestroy();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
