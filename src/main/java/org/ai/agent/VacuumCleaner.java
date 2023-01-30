package org.ai.agent;

public class VacuumCleaner {
    private int rewardPoints = 0;
    private int position = 0;
    private final Square[] squares;

    public VacuumCleaner(Square[] squares) {
        this.squares = squares;
    }

    public void addRewardPoints(int rewardPoints) {
        this.rewardPoints += rewardPoints;
    }

    public int getRewardPoints() {
        return this.rewardPoints;
    }

    public Square getCurrentSquare() {
        return squares[position];
    }

    public void changeSquare() {
        if (position == 0) {
            position = 1;
        } else {
            position = 0;
        }
    }
}