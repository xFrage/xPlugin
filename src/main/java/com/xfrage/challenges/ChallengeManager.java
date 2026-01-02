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
            challenge.setEnabled(true);
            activeChallenges.add(challenge);
        }
    }

    public static void disableChallenge(Challenge challenge) {
        challenge.setEnabled(false);
        activeChallenges.remove(challenge);
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

    public static List<Challenge> getChallenges() {
        return challenges;
    }

    public static List<Challenge> getActiveChallenges() {
        return activeChallenges;
    }

    public static Challenge getChallenge(String name) {
        for (int i = 0; i < challenges.size(); i++) {
            if (challenges.get(i).getTitle().equals(name)) {
                return activeChallenges.get(i);
            }
        }
        return null;
    }
}