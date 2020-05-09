package com.qtimes.utils;

/**
 * Author: JackHou
 * Date: 2019/10/11.
 */
public class UtilsConst {
    /*gpio相关*/
    public interface GPIONum {
        String GPIO41 = "41";//out: control the door, low open the door ,high close the door
        String GPIO42 = "42";//in: when to start detect
        String GPIO76 = "76";//flash light ctl
    }

    public interface GPIOPath {
        String GPIO41 = "/sys/class/gpio/gpio41/value";
        String GPIO42 = "/sys/class/gpio/gpio42/value";
        String GPIO76 = "/sys/class/gpio/gpio76/value";
    }

    public interface GPIOKey {
        String VALUE = "value";
        String DIRECTION = "direction";
        String EDGE = "edge";
    }

    public interface GPIOEdge {
        String BOTH = "both";
        String RISING = "rising";
        String FALLING = "falling";
    }

    public interface GPIODirection {
        String OUT = "out";
        String IN = "in";
    }

    public interface GPIOValue {
        String HIGH = "1";
        String LOW = "0";
    }
}
