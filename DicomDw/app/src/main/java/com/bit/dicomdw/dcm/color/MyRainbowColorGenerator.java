package com.bit.dicomdw.dcm.color;

import java.util.Random;

public class MyRainbowColorGenerator {
    static private MyColor[] rainbowColor=new MyColor[8];
    //static private MyColor defaultColor=new MyColor(255,0,255);
    static private Random rnd=new Random(System.currentTimeMillis());
    static {
        rainbowColor[0]=new MyColor(255,0,0);
        rainbowColor[1]=new MyColor(255,152,0);
        rainbowColor[2]=new MyColor(255,255,0);
        rainbowColor[3]=new MyColor(0,255,0);
        rainbowColor[4]=new MyColor(0,255,255);
        rainbowColor[5]=new MyColor(0,0,255);
        rainbowColor[6]=new MyColor(150,0,255);
        rainbowColor[7]=new MyColor(255,120,255);
    }

    static public MyColor getRandomColor(){
        double _index=rnd.nextDouble()*6.999;
        int _pre=(int)_index;
        double step=_index-_pre;
        int r=(int)(rainbowColor[_pre].r+step*(rainbowColor[_pre+1].r-rainbowColor[_pre].r));
        int g=(int)(rainbowColor[_pre].g+step*(rainbowColor[_pre+1].g-rainbowColor[_pre].g));
        int b=(int)(rainbowColor[_pre].b+step*(rainbowColor[_pre+1].b-rainbowColor[_pre].b));
        return new MyColor(r,g,b);
    }

}
