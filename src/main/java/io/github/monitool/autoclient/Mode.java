package io.github.monitool.autoclient;

/**
 * Created by Bartosz Głowacki on 2015-04-20.
 */
public enum Mode {
    CPU("c"),
    MEM("m"),
    HDD("d");

    private String mode;

    Mode(String mode){
        this.mode=mode;
    }

    public static Mode fromString(String text) {
        if (text != null) {
            for (Mode m : Mode.values()) {
                if (text.equalsIgnoreCase(m.mode)) {
                    return m;
                }
            }
        }
        return null;
    }

    public String getMode(){
        return mode;
    }

}
