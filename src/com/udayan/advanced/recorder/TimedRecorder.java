package com.udayan.advanced.recorder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;

public class TimedRecorder{
	
	Context context;
	

	private FileOutputStream os;
	
	public void beginRecording() {

		this.os = Constants.currentOutputStream;
		
		Constants.homeContext.startService(new Intent(Constants.homeContext, RawRecorderService.class));
        
	}
	
	public void stopRecording(){
		
		Constants.homeContext.stopService(new Intent(Constants.homeContext, RawRecorderService.class));

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
	
}
