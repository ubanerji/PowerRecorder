package com.udayan.advanced.recorder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class CoreRecorderService extends IntentService {


    private FileOutputStream bufferDumpStream;
    
    private byte[] bData = new byte[40000];
	
	public CoreRecorderService() {
		super("AudioRecordService");
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Utility.isRecordingActive = false;
		
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
	        	    	                
	                if (bytesRead < 0)
	                	break;
	                
	    	    	try {
	    	            // writes the data to file from buffer
	    	            // // stores the voice buffer
	    	            bufferDumpStream.write(bData, 0, bytesRead);
	    	        } catch (IOException e) { 
	    	            e.printStackTrace();
	    	        }
	    	    	
	    	    	//offset += bytesRead;
	            } while (Utility.isRecordingActive);
	        	
	        	try {
	    			Toast.makeText(Constants.homeContext, "Stop Recording with " + bufferDumpStream.getChannel().size() + " bytes", Toast.LENGTH_SHORT).show();
	    		} catch (IOException e2) {
	    			e2.printStackTrace();
	    		}
	        	Constants.aRecorder.stop();
    		}
    	}).start();
	}
}
