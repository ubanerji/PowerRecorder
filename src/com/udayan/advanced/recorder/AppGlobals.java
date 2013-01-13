package com.udayan.advanced.recorder;

import java.io.FileOutputStream;

import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioRecord;

public class AppGlobals {
	public static NotificationManager notifManager;
    public static Context homeContext;

    public static FileOutputStream currentOutputStream;
    public static String currentTempFileName1;
    public static String currentTempFileName2;
    

    public static AudioRecord aRecorder;
}
