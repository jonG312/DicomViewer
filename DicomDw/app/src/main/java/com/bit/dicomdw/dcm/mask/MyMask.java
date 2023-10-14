package com.bit.dicomdw.dcm.mask;

import com.bit.dicomdw.dcm.color.MyColor;

public abstract class MyMask {
    /**如果返回false，表示该像素位置的颜色不会由mask决定*/
    abstract public boolean getPixelColor(int x,int y,MyColor color);
}
