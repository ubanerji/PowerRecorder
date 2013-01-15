package com.udayan.advanced.recorder;

import android.media.MediaRecorder.AudioSource;

public class RecordingConfig {
	public static int FORMAT_WAV = 0;
	public static int FORMAT_3GP = 1;
	
	public static boolean timedRecording = false;
	public static int recordingTimeInSeconds = 0;
	
	public static int audioSource = AudioSource.MIC;
	
	public static int recordingFormat = RecordingConfig.FORMAT_WAV;
}
