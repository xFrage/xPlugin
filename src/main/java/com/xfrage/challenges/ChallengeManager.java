package com.xfrage.challenges;

import com.xfrage.Main;

import java.util.ArrayList;
import java.util.List;

public class ChallengeManager {

    private static final List<Challenge> challenges = new ArrayList<>();
    private static final List<Challenge> activeChallenges = new ArrayList<>();

    public static void register(Challenge challenge) {
        challenges.add(challenge);
    }

    public static void enableChallenge(Challenge challenge) {
        if (!activeChallenges.contains(challenge)) {
            activeChallenges.add(challenge);
            updateChallenges();
        }
    }

    public static void disableChallenge(Challenge challenge) {
        activeChallenges.remove(challenge);
        updateChallenges();
    }

    public static void resumeAllChallenges() {
        for (Challenge challenge : activeChallenges) {
            challenge.setEnabled(true);
        }
    }

    public static void pauseAllChallenges() {
        for (Challenge challenge : activeChallenges) {
            challenge.setEnabled(false);
        }
    }

    public static void updateChallenges() {
        if (Main.getInstance().getTimer().isRunning()) { // timer is running
            resumeAllChallenges();
        } else { // timer is paused
            pauseAllChallenges();
        }
    }

    public static List<Challenge> getChallenges() {
        return challenges;
    }

    public static List<Challenge> getActiveChallenges() {
        return activeChallenges;
    }

    public static Challenge getChallenge(String name) {
        for (Challenge challenge : challenges) {
            if (challenge.getTitle().equals(name)) {
                return challenge;
            }
        }
        return null;
    }

    public static boolean getChallengeActive(String name) {
        for (Challenge c : activeChallenges) {
            if (c.getTitle().equals(name)) {
                return true;
            }
        }
        return false;
    }
}