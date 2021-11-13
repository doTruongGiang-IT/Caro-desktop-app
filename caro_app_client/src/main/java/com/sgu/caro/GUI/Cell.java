package com.sgu.caro.GUI;

public class Cell {
    private int x;
    private int y;
    private int w;
    private int h;
    private String value;
    
    public static final String X_VALUE = "X";
    public static final String O_VALUE = "O";
    public static final String EMPTY_VALUE = "";
    
    public Cell() {
        value = EMPTY_VALUE;
    }

    public Cell(int x, int y, int w, int h, String value) {
//        this();
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.value = this.value;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public String getValue() {
        return value;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setW(int w) {
        this.w = w;
    }

    public void setH(int h) {
        this.h = h;
    }

    public void setValue(String value) {
        this.value = value;
    }
}