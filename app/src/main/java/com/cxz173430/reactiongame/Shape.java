package com.cxz173430.reactiongame;
/**
 * This is the object class for the shapes of the game
 * @Author:     Churong Zhang
 * @Date:       3/29/2020
 * @Class:      CS 6326
 */

import android.graphics.Color;

import androidx.annotation.Nullable;

public class Shape{
    // define shape attributes
    private boolean circle;
    private int posX;
    private int posY;
    private int color;
    private int width;
    private long birthTime;
    private long deadTime;

    // define getter functions
    public boolean isCircle() {
        return circle;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getColor() {
        return color;
    }

    public int getWidth() {
        return width;
    }

    public long getDeadTime() {
        return deadTime;
    }

    public long getBirthTime() {
        return birthTime;
    }

    // default constructor for dummy object
    public Shape()
    {

    }

    // colors Red, Orange, Yellow, Green, Blue, Purple, and White
    // contructor
    public Shape(boolean circleOrNot, int x, int y, int color, int side, long bTime , long dTime)
    {
        circle = circleOrNot;
        posX = x;
        posY = y;
        this.color = color;
        width = side;
        birthTime = bTime;
        deadTime = dTime;
    }
}
