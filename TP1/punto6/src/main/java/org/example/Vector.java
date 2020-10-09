package org.example;

import java.io.Serializable;

public class Vector implements Serializable {

    int[] v = new int[10];

    public int[] getV() {
        return v;
    }

    public void setV(int[] v) {
        this.v = v;

    }
}
