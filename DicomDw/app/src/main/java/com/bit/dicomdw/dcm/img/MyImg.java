package com.bit.dicomdw.dcm.img;


import com.bit.dicomdw.dcm.color.MyColor;

public abstract class MyImg {
    protected MyColor rgb=new MyColor();
    //获取图片的高
    abstract public int getWidth();
    //获取图片的高
    abstract public int getHeight();
    //获取像素值
    abstract public void getPixelColor(int x,int y,MyColor color);
}
