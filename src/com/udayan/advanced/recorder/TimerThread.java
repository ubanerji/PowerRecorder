package com.udayan.advanced.recorder;

public class TimerThread extends Thread {
	
	private long timerValue = 0;
	private int stopAction = 0;
	
    public void run() {
    	while (timerValue > 0) {
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		timerValue--;
    	}
    	
    	doStopAction();
    }
    
    private void doStopAction() {
    	if (stopAction == 0) {
    		CoreRecorder.getInstance().stop();
    	}
	}

	//Set the timer to stop a recording session when expiring
    public void setStopTimer(long timeInSec) {
    	timerValue = timeInSec;
    	stopAction = 0;
    }
}
