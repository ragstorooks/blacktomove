package com.ragstorooks.blacktomove.chess.pieces;

class PieceMoveDetails {
    private int originRank;
    private char originFile;
    private int destinationRank;
    private int destinationFile;
    private int numberOfMovingRanks;
    private int numberOfMovingFiles;
    private boolean isCapture;

    PieceMoveDetails(int originRank, char originFile, int destinationRank, int destinationFile, int
            numberOfMovingRanks, int numberOfMovingFiles, boolean isCapture) {
        this.originRank = originRank;
        this.originFile = originFile;
        this.destinationRank = destinationRank;
        this.destinationFile = destinationFile;
        this.numberOfMovingRanks = numberOfMovingRanks;
        this.numberOfMovingFiles = numberOfMovingFiles;
        this.isCapture = isCapture;
    }

    int getOriginRank() {
        return originRank;
    }

    char getOriginFile() {
        return originFile;
    }

    int getDestinationRank() {
        return destinationRank;
    }

    int getDestinationFile() {
        return destinationFile;
    }

    int getNumberOfMovingRanks() {
        return numberOfMovingRanks;
    }

    int getNumberOfMovingFiles() {
        return numberOfMovingFiles;
    }

    boolean isCapture() {
        return isCapture;
    }
}
