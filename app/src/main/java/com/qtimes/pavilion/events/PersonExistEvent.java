package com.qtimes.pavilion.events;

/**
 * Author: JackHou
 * Date: 2020/4/23.
 */
public class PersonExistEvent {
    private boolean personExist;

    public PersonExistEvent(boolean mPersonExist) {
        personExist = mPersonExist;
    }

    public boolean isPersonExist() {
        return personExist;
    }

    public void setPersonExist(boolean mPersonExist) {
        personExist = mPersonExist;
    }
}
