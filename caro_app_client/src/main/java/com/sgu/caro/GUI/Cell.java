package com.sgu.caro.GUI;

public class Cell {
    private int posX;
    private int posY;
    private int w;
    private int h;
    private String value;
    
    public static final String X_VALUE = "X";
    public static final String O_VALUE = "O";
    public static final String EMPTY_VALUE = "";
    
    public Cell() {
        value = EMPTY_VALUE;
    }

    public Cell(int posX, int posY, int w, int h, String value) {
//        this();
        this.posX = posX;
        this.posY = posY;
        this.w = w;
        this.h = h;
        this.value = this.value;
    }

	public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
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

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
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