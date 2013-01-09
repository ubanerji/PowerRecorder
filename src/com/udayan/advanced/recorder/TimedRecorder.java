package com.udayan.advanced.recorder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class TimedRecorder extends Service{
	
	Context context;
	
    private byte[] bData = new byte[Constants.READ_BUFFER_SIZE];
	
	private FileOutputStream os;
	
    private FileOutputStream bufferDumpStream;
	
    @Override
	public void onCreate() {

		super.onCreate();
		beginRecording();
	}
	
	public void beginRecording() {
		
		this.os = Constants.currentOutputStream;
		
		Constants.aRecorder = new AudioRecord(AudioSource.MIC, Constants.currentSampleRate, Constants.currentChannelConfig, Constants.currentFormat, Constants.coreBufferSize);

        if (Constants.aRecorder.getState() == AudioRecord.STATE_UNINITIALIZED) {
        	Toast.makeText(Constants.homeContext, "Could not initialize audio record", Toast.LENGTH_LONG).show();
        }
        else {
        	Toast.makeText(Constants.homeContext, "Audio successfully initialized", Toast.LENGTH_SHORT).show();
        }
                
        
        new Thread(new Runnable() {
    		public void run() {
    			Constants.aRecorder.startRecording();
	        	
	        	bufferDumpStream = null;
	    		try {
	    			bufferDumpStream = Constants.homeContext.openFileOutput(Constants.currentTempFileName1, Context.MODE_PRIVATE);
	    		} catch (FileNotFoundException e2) {
	    			e2.printStackTrace();
	    		}
	        	
	            int offset = 0;
	            int bytesRead;
	    		    	
	        	do {
	                bytesRead = Constants.aRecorder.read(bData, offset, Constants.coreBufferSize);
	        	    	                
	                if (bytesRead < 0) {
	                	Log.e("Hello", "Breaking bad " + Constants.coreBufferSize);
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
	        	
	        	Constants.aRecorder.stop();
    		}
    	}).start();
        
        
	}
	
	public void stopRecording(){
		
		FileInputStream bufferReadStream = null;
		try {
			bufferReadStream = Constants.homeContext.openFileInput(Constants.currentTempFileName1);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		
    	//Convert file to WAV:
		
	
    	try {
			Utility.writeWaveFileHeader(os, bufferReadStream.getChannel().size(), bufferReadStream.getChannel().size() + 36, 
					Constants.currentSampleRate, 2, Constants.currentSampleRate * 4);
		} catch (IOException e) {
			e.printStackTrace();
		}

    	byte[] bData = new byte[Constants.READ_BUFFER_SIZE];
    	
    	//Dump the rest of the bytes:
    	try {

			int tempByte = bufferReadStream.read(bData);
			while (tempByte != -1) {
				os.write(bData, 0, tempByte);
				tempByte = bufferReadStream.read(bData);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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
