package com.sasi.gwmaker.timer;

import javax.management.InvalidAttributeValueException;
import java.util.Timer;
import java.util.TimerTask;

public class TaskTimer {

    /**
     * Amount of seconds the timer must run for before calling timerUp.
     */
    private long timerDelay;

    /**
     * Used to provide time up callbacks to registered listeners.
     */
    private ITaskTimer timerInterface;

    /**
     * Create a timer task that in turn calls the timeUp callback.
     */
    private TimerTask task;

    /**
     * The timer object that executes the timer task after the specified
     * timerDelay.
     */
    private Timer timer;

    /**
     * Constructor.
     *
     * Initializes the timer object and sets the private timerDelay variable.
     *
     * @param timerDelay
     */
    public TaskTimer(long timerDelay) {
        this.timerDelay = timerDelay;
    }

    /**
     * Method used to set the timer interface that will later be called
     * to deliver the callbacks.
     *
     * @param timerInterface
     */
    public void setTimerInterface(ITaskTimer timerInterface) {
        this.timerInterface = timerInterface;
    }

    /**
     * Method used to set up the timer task and start the timer.
     *
     * @throws InvalidAttributeValueException when the timerDelay is not configured or
     * configured to 0 Seconds.
     */
    public void startTimer() throws InvalidAttributeValueException {
        if(timerDelay == 0){
            throw new InvalidAttributeValueException("Timer Delay must be greater than 0 Seconds!");
        }
        timer = new Timer("Keep Alive Timer");
        task = new TimerTask() {
            public void run() {
                if(timerInterface != null){
                    timerInterface.onTimeUp();
                }
            }
        };
        timer.schedule(task, timerDelay);
    }

    /**
     * Reset the Timer.
     */
    public boolean resetTimer(){
        if(task == null){
            return false;
        }else {
            try {
                endTimer();
                startTimer();
                return true;
            } catch (InvalidAttributeValueException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * Cancel the timer.
     */
    public void endTimer(){
        timer.cancel();
        timer.purge();
        task = null;
        timer = null;
    }

}
