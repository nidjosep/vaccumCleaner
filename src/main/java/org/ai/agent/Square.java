package org.ai.agent;

public class Square {
    private final String name;
    private boolean isDirty;

    public Square(String name) {
        this.name = name;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public boolean isClean() {
        return !isDirty;
    }

    public void makeDirty() {
        isDirty = true;
    }

    public void suckUpTheDirt() {
        isDirty = false;
    }

    public String getName() {
        return name;
    }
}
