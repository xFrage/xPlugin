package com.xfrage.challenges;

import com.xfrage.Main;

public class DamageDealtDamageTakenChallenge extends Challenge{

    private boolean enabled = false;

    public DamageDealtDamageTakenChallenge(String title) {
        super(title);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (enabled) startChallenge();
        else stopChallenge();

    }

    public void startChallenge() {
        System.out.println();
    }

    public void stopChallenge() {

    }

}
