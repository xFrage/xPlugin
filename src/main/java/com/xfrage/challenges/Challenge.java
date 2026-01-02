package com.xfrage.challenges;

import com.xfrage.Main;

public abstract class Challenge {

    protected boolean enabled = false;
    private final String title;

    protected Challenge(String title) {
        this.title = title;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (Main.getInstance().getTimer().isRunning()) { // challenge wird während /timer resume aktiviert
            if (enabled) startChallenge();
        }

        else stopChallenge(); //
    }

    public String getTitle() {
        return title;
    }

    public abstract void startChallenge();
    public abstract void stopChallenge();
}
