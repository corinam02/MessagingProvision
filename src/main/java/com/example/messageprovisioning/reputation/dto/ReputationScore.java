package com.example.messageprovisioning.reputation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReputationScore {

    private String phoneNumber;
    private int score;
    private ScoreLevel level;
    private long totalReports;
    private long recentReports;
    private boolean safeToProvision;


    public ReputationScore(String phoneNumber, int score, ScoreLevel level, long totalReports, long recentReports) {

    }

    public ReputationScore(String phoneNumber, int score, ScoreLevel level, boolean safeToProvision) {
        this.phoneNumber = phoneNumber;
        this.score = score;
        this.level = level;
        this.safeToProvision = safeToProvision;
    }
}
