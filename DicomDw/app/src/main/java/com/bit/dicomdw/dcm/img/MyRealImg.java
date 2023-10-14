package com.bit.dicomdw.dcm.img;


import com.bit.dicomdw.dcm.color.MyColor;

public class MyRealImg extends MyImg {
    private int w,h;
    private MyColor[] data=null;

    public MyRealImg(int w,int h){
        this.w=w;
        this.h=h;
        this.data=new MyColor[w*h];
        for(int i=0;i<w*h;i++){
            this.data[i]=new MyColor();
        }
    }

    public MyRealImg(MyImg img){
        w=img.getWidth();
        h=img.getHeight();
        this.data=new MyColor[w*h];
        for(int i=0;i<w*h;i++){
            this.data[i]=new MyColor();
        }
        int index;
        for(int y=0;y<h;y++){
            for(int x=0;x<w;x++){
                index=calcuIndex(x,y);
                img.getPixelColor(x,y,data[index]);
            }
        }
    }

    @Override
    public int getWidth(){
        return w;
    }

    @Override
    public int getHeight(){
        return h;
    }

    @Override
    public void getPixelColor(int x, int y, MyColor color){
        int index=calcuIndex(x,y);
        color.set(data[index]);
    }


    //为了效率没有进行越界检测
    public void setColor(int x,int y,int r,int g,int b){
        int index=calcuIndex(x,y);
        data[index].set(r,g,b);
    }
    public void setColor(int x,int y,MyColor color){
        int index=calcuIndex(x,y);
        data[index].set(color);
    }

    private int calcuIndex(int x,int y){
        return y*w+x;
    }
}
