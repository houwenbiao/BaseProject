package com.qtimes.wonly.events;

/**
 * Author: JackHou
 * Date: 2020/3/28.
 */
public class PersonInOutEvent {
    private int numIn;
    private int numOut;

    public PersonInOutEvent(int numIn, int numOut) {
        this.numIn = numIn;
        this.numOut = numOut;
    }

    public int getNumIn() {
        return numIn;
    }

    public void setNumIn(int numIn) {
        this.numIn = numIn;
    }

    public int getNumOut() {
        return numOut;
    }

    public void setNumOut(int numOut) {
        this.numOut = numOut;
    }

    @Override
    public String toString() {
        return "PersonInOutEvent{" +
                "numIn=" + numIn +
                ", numOut=" + numOut +
                '}';
    }
}
