package com.bit.dicomdw.dcm.color;

public class MyColor {
    public int r,g,b;

    public MyColor(){}

    public MyColor(int r,int g,int b){
        this.r=r;
        this.g=g;
        this.b=b;
    }
    public MyColor(float r,float g,float b){
        this.r=(int)r;
        this.g=(int)g;
        this.b=(int)b;
    }
    public MyColor(double r,double g,double b){
        this.r=(int)r;
        this.g=(int)g;
        this.b=(int)b;
    }
    public MyColor(MyColor color){
        this.r=color.r;
        this.g=color.g;
        this.b=color.b;
    }
    public void set(int r,int g,int b){
        this.r=r;
        this.g=g;
        this.b=b;
    }
    public void set(float r,float g,float b){
        this.r=(int)r;
        this.g=(int)g;
        this.b=(int)b;
    }
    public void set(double r,double g,double b){
        this.r=(int)r;
        this.g=(int)g;
        this.b=(int)b;
    }
    public void set(MyColor color){
        this.r=color.r;
        this.g=color.g;
        this.b=color.b;
    }
}
