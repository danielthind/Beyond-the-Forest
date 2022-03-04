package com.coffeepizza.beyondtheforest.sprite.util;

public class UserData {

    public String type;
    public Object obj;
    public int trigger;

    public UserData(String type, Object obj, int trigger) {
        this.type = type;
        this.obj = obj;
        this.trigger = trigger;
    }
}