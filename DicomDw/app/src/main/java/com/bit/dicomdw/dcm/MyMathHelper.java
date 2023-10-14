package com.bit.dicomdw.dcm;

/**
 * MyMathHelper
 * 仅为本包提供静态方法
 * 用于各种数学计算
 */
public final class MyMathHelper {
    //MyMathHelper 内部使用的精度值
    private static final double EPS=1e-9f;

    //返回整数值的floor函数
    public static int int_floor(double value){
        return (value>=0.0)?(int)(value+EPS):(int)(value-1+EPS);
    }
    //返回整数值的ceil函数
    public static int int_ceil(double value){
        return (value>=0.0)?(int)(value+1-EPS):(int)(value-EPS);
    }
    //返回整数值的round函数
    public static int int_round(double value){
        return (value>=0.0)?(int)(value+0.5+EPS):(int)(value-0.5-EPS);
    }
    //把颜色值限定到[0,255]
    public static int clampColorValue(double color_value){
        int value=(int)color_value;
        return value<0 ? 0 : (value > 255 ? 255 : value);
    }
    //把颜色值限定到[0,255]
    public static int clampColorValue(int value){
        return value<0 ? 0 : (value > 255 ? 255 : value);
    }
    //角度转换到[0,360)(角度制),精度0.01度
    public static double angle_loop(double degree){
//        int tmp_degree=(int)(degree*100);
//        tmp_degree=(tmp_degree % 36000 + 36000) % 36000;
//        return (double)tmp_degree/100.0f;
        if(degree>360)return degree-360;
        else if(degree<0)return degree+360;
        else return degree;
    }
}
