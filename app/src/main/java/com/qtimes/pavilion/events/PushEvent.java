package com.qtimes.pavilion.events;

/**
 * Author: JackHou
 * Date: 2019/12/21.
 */
public class PushEvent {
    private String type;
    private String key;
    private String value;

    public PushEvent(String type, String key, String value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }
}
