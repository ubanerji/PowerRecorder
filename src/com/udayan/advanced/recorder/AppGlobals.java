package com.udayan.advanced.recorder;

import java.io.FileOutputStream;

import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioRecord;

/*
 * File: AppGlobals.java
 * Contains globals related to applications
 * running state. Most values come from UI
 */

public class AppGlobals {
	//The current notification manager:
	public static NotificationManager notifManager;

	//The home UI screen:
    public static Context homeContext;

    //The current output stream which will generate the output file:
    public static FileOutputStream currentOutputStream;

    //Temporary files used inside the app:
    public static String currentTempFileName1;
    public static String currentTempFileName2;
    
    //The current audio recorder instance:
    public static AudioRecord aRecorder;
}
