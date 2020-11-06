package com.news.goodlife.Models;

public class BubbleSurround {
    int length;
    int top, bottom, start, end;
    int centerX, centerY;
    int spaceTop, spaceBottom, spaceStart, spaceEnd;

    boolean biggerNextToMe = false;
    boolean biggerUnderneathMe = false;

    int biggerOverflowEnd = 0;
    int biggerOverflowBottom = 0;


    CostCatData data;


    public BubbleSurround() {
    }

    public void setPosition(int top, int bottom, int start, int end){
        setTop(top);
        setBottom(bottom);
        setStart(start);
        setEnd(end);
    }

    public void setSurroundingSpace(int spaceTop, int spaceBottom, int spaceStart, int spaceEnd){
        setSpaceTop(spaceTop);
        setSpaceBottom(spaceBottom);
        setSpaceStart(spaceStart);
        setSpaceEnd(spaceEnd);
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getSpaceTop() {
        return spaceTop;
    }

    public void setSpaceTop(int spaceTop) {
        this.spaceTop = spaceTop;
    }

    public int getSpaceBottom() {
        return spaceBottom;
    }

    public void setSpaceBottom(int spaceBottom) {
        this.spaceBottom = spaceBottom;
    }

    public int getSpaceStart() {
        return spaceStart;
    }

    public void setSpaceStart(int spaceStart) {
        this.spaceStart = spaceStart;
    }

    public int getSpaceEnd() {
        return spaceEnd;
    }

    public void setSpaceEnd(int spaceEnd) {
        this.spaceEnd = spaceEnd;
    }

    public boolean isBiggerNextToMe() {
        return biggerNextToMe;
    }

    public void setBiggerNextToMe(boolean biggerNextToMe) {
        this.biggerNextToMe = biggerNextToMe;
    }

    public boolean isBiggerUnderneathMe() {
        return biggerUnderneathMe;
    }

    public void setBiggerUnderneathMe(boolean biggerUnderneathMe) {
        this.biggerUnderneathMe = biggerUnderneathMe;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getBiggerOverflowEnd() {
        return biggerOverflowEnd;
    }

    public void setBiggerOverflowEnd(int biggerOverflowEnd) {
        this.biggerOverflowEnd = biggerOverflowEnd;
    }

    public int getBiggerOverflowBottom() {
        return biggerOverflowBottom;
    }

    public void setBiggerOverflowBottom(int biggerOverflowBottom) {
        this.biggerOverflowBottom = biggerOverflowBottom;
    }

    public CostCatData getData() {
        return data;
    }

    public void setData(CostCatData data) {
        this.data = data;
    }

    public int getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }
}
