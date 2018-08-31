package com.photogroup.app.ui;

public class ProgressMonitor2 {

    private volatile int progress = 0;

    private volatile boolean done = false;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        System.out.println("setProgress" + progress);
        this.progress = progress;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

}
