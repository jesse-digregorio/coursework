package com.jesse.projects.libraries;

/**
 * Created by jesse on 7/7/2017.
 */

public enum Die {
    D2(2), D4(4), D6(6), D8(8), D10(10), D12(12), D20(20);

    private final int value;
    private Die(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        String name;
        switch (value) {
            case  2: name = "D2";  break;
            case  4: name = "D4";  break;
            case  6: name = "D6";  break;
            case  8: name = "D8";  break;
            case 10: name = "D10"; break;
            case 12: name = "D12"; break;
            case 20: name = "D20"; break;
            default: name = "D??"; break;
        }
        return name;
    }
}

