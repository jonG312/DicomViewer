package com.bit.dicomdw.dcm.interpolation;

import com.bit.dicomdw.dcm.color.MyColor;
import com.bit.dicomdw.dcm.img.MyImg;

/**
 * MyBilinearInterpolation 继承自 MyInterpolation
 * 双线性插值策略
 */
public class MyBilinearInterpolation extends MyInterpolation {

    private MyColor c00=new MyColor();
    private MyColor c01=new MyColor();
    private MyColor c10=new MyColor();
    private MyColor c11=new MyColor();

    public MyBilinearInterpolation(){}
    public MyBilinearInterpolation(MyImg img){
        super(img);
    }

    @Override
    public void getPixelColor(double x, double y, MyColor color){
        if(null==img){
            getDefaultColor(color);
            return;
        }
        if(check_outside(x,y)){
            getDefaultColor(color);
            return;
        }

        int xl=(int)x,xu=xl+1;
        int yl=(int)y,yu=yl+1;

        if(xu>=img_w)xu--;
        if(yu>=img_h)yu--;

        img.getPixelColor(xl,yl,c00);
        img.getPixelColor(xu,yl,c01);
        img.getPixelColor(xl,yu,c10);
        img.getPixelColor(xu,yu,c11);

        double pos_kx=(x-xl);
        double pos_ky=(y-yl);
        double r = interpolate(pos_kx,pos_ky,c00.r,c01.r,c10.r,c11.r);
        double g = interpolate(pos_kx,pos_ky,c00.g,c01.g,c10.g,c11.g);
        double b = interpolate(pos_kx,pos_ky,c00.b,c01.b,c10.b,c11.b);
        color.set(r,g,b);
    }

    private double interpolate(double pos_kx,double pos_ky,
                              int x00,int x01,int x10,int x11){
        double v1=x00+(x01-x00)*pos_kx;
        double v2=x10+(x11-x10)*pos_kx;
        v1=v1+(v2-v1)*pos_ky;
        return v1;
    }
}
