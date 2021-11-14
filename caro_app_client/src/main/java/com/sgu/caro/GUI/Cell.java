package com.sgu.caro.GUI;

public class Cell {
    private int posX;
    private int posY;
    private int width;
    private int height;
    private String value;
    
    public static final String X_VALUE = "X";
    public static final String O_VALUE = "O";
    public static final String EMPTY_VALUE = "";
    
    public Cell() {
        value = EMPTY_VALUE;
    }

    public Cell(int posX, int posY, int width, int height, String value) {
//        this();
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.value = this.value;
    }

	public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getWidth() {
    	return width;
    }

    public int getHeight() {
    	return height;
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

    public void setWidth(int width) {
    	this.width = width;
    }

    public void setHeight(int height) {
    	this.height = height;
    }

    public void setValue(String value) {
        this.value = value;
    }
}