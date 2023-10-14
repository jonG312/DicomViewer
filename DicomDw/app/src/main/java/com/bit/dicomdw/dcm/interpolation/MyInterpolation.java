package com.bit.dicomdw.dcm.interpolation;


import com.bit.dicomdw.dcm.color.MyColor;
import com.bit.dicomdw.dcm.img.MyImg;

/**
 * MyInterpolation
 * 插值策略抽象类
 * 其中存了MyImg的引用(用于之后插值)
 * 存了"默认颜色",并且可以设置这个"默认颜色"
 * "默认颜色"用于访问无效坐标或者img为null时返回的颜色
 * "默认颜色"默认为 (r=0,g=0,b=0)
 *
 * 抽象接口方法有:
 *
 * abstract public void getPixelColor(double x,double y,int[] color)
 *      用于获取浮点数坐标处的颜色值
 */
public abstract class MyInterpolation {
    protected MyImg img=null;
    protected int img_w=0;
    protected int img_h=0;
    protected MyColor defaultColor = new MyColor(0,0,0);
    protected MyColor rgb=new MyColor();

    public MyInterpolation(){
        img=null;
    }

    public MyInterpolation(MyImg img){
        setImg(img);
    }

    //设置img
    public void setImg(MyImg img){
        this.img=img;
        img_w=this.img.getWidth();
        img_h=this.img.getHeight();
    }

    //设置defaultColor(即"默认颜色")
    public void setDefaultColor(int r,int g,int b){
        defaultColor.set(r,g,b);
    }

    //获取浮点数坐标处的颜色值
    abstract public void getPixelColor(double x, double y, MyColor color);

    public int getWidth(){
        return this.img_w;
    }

    public int getHeight(){
        return this.img_h;
    }

    protected void getDefaultColor(MyColor color){
        color.set(defaultColor);
    }
    protected final boolean check_outside(double x,double y){
        return x<0.0f || y<0.0f || x>img_w-1.0f || y>img_h-1.0f;
    }
}
