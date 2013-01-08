package com.udayan.advanced.recorder;

import android.content.Context;
import android.media.AudioRecord;


public class Constants {

    public static String currentTempFileName1;
    public static String currentTempFileName2;
    
    public static int currentSampleRate;
    public static int currentChannelConfig;
    public static int currentNumChannels;
    public static int coreBufferSize;
    public static int currentFormat;
    
    public static int READ_BUFFER_SIZE = 8192;
    
    public static AudioRecord aRecorder;
    
    public static Context homeContext;
}
