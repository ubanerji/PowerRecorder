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

		this.os = AppGlobals.currentOutputStream;
		
		AppGlobals.homeContext.startService(new Intent(AppGlobals.homeContext, RawRecorderService.class));
        
	}
	
	public void stopRecording(){
		
		AppGlobals.homeContext.stopService(new Intent(AppGlobals.homeContext, RawRecorderService.class));

		FileInputStream bufferReadStream = null;
		try {
			bufferReadStream = AppGlobals.homeContext.openFileInput(AppGlobals.currentTempFileName1);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		
    	//Convert file to WAV:
		
	
    	try {
			Utility.writeWaveFileHeader(os, bufferReadStream.getChannel().size(), bufferReadStream.getChannel().size() + 36, 
					AudioConfig.currentSampleRate, 2, AudioConfig.currentSampleRate * 4);
		} catch (IOException e) {
			e.printStackTrace();
		}

    	byte[] bData = new byte[AudioConfig.READ_BUFFER_SIZE];
    	
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
