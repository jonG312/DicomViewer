package com.bit.dicomdw.dcm.colorstrategy;


import com.bit.dicomdw.dcm.color.MyColor;
import com.bit.dicomdw.dcm.color.MyColorHandler;


/**
 * MyColorStrategy
 * 提供由MyDcmColor映射到MyColor的接口方法
 * Dicom图像的像素信息和普通图像的有所不同
 * 为了能够在屏幕上正常显示,应该转换为1或3或4通道,用256深度表示的普通计算机图像
 * 这里考虑到通用性,我选用3通道rgb的MyColor
 *
 * implements这个interface的类
 * 应该实现这个映射,即map方法
 * 以实现dicom图像的调窗
 */
public abstract class MyColorStrategy {
    protected MyColor defaultColor=MyColorHandler.black;

    //映射
    public abstract void map(MyColor fromColor, MyColor toColor);

    public void setDefaultColor(int r,int g,int b){
        defaultColor.set(r,g,b);
    }

    protected void getDefaultColor(MyColor color){
        color.set(defaultColor);
    }
}
