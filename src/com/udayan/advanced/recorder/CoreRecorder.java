package com.udayan.advanced.recorder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Intent;

/* File: CoreRecorder.java
 * Contains the core recording class singleton
 * The UI thread uses just this class.
 */

public class CoreRecorder {

    // Output stream to put the final file
    private FileOutputStream os;
    // Input stream to read the team file created
    private FileInputStream bufferReadStream;

    // Singleton business:
    private static CoreRecorder instance = null;

    private CoreRecorder() {
    }

    public static CoreRecorder getInstance() {
        if (instance == null) {
            instance = new CoreRecorder();
        }
        return instance;
    }

    // Directly corresponds to pressing the record button
    public void begin() {
        // Return if no recording is currently active
        // This should not happen
        if (Utility.isRecordingActive == true) {
            return;
        }

        // Set the start of recording
        Utility.isRecordingActive = true;

        // Begin the recording session:
        beginRecording();

        if (RecordingConfig.timedRecording == true) {
            TimerThread timer = new TimerThread();
            timer.setStopTimer(RecordingConfig.recordingTimeInSeconds);
            timer.start();
        }
    }

    public void stop() {

        if (Utility.isRecordingActive == false) {
            return;
        }

        Utility.isRecordingActive = false;

        stopRecording();

        if (RecordingConfig.recordingFormat == RecordingConfig.FORMAT_WAV) {
            // Slap a RAW file header on top of it:
            try {
                Utility.writeWaveFileHeader(os, bufferReadStream.getChannel()
                        .size(), bufferReadStream.getChannel().size() + 36,
                        AudioConfig.currentSampleRate, 2,
                        AudioConfig.currentSampleRate * 4);
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] bData = new byte[AudioConfig.READ_BUFFER_SIZE];

            // Dump the rest of the bytes:
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

    private void beginRecording() {
        // Set the current stream to the value set in Home Activity
        this.os = AppGlobals.currentOutputStream;
        // Start the recording service. Let's go:
        AppGlobals.homeContext.startService(new Intent(AppGlobals.homeContext,
                RawRecorderService.class));
    }

    private void stopRecording() {
        // Stop service. Let the engines cool down:
        AppGlobals.homeContext.stopService(new Intent(AppGlobals.homeContext,
                RawRecorderService.class));

        // Prepare the final output file:
        bufferReadStream = null;
        try {
            bufferReadStream = AppGlobals.homeContext
                    .openFileInput(AppGlobals.currentTempFileName1);
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
    }

}
