package com.bit.dicomdw.dcm.mask;

import com.bit.dicomdw.dcm.mask.MyMaskColorTools.MyDetecFrameMaskColorSet;

public class MyDFMaskGroup {
    private MyDetecFrameMaskColorSet colorSet;
    private MyDetecFrameMask[] masks;
    private int width,height;
    private final int defaultLintWidth=Math.max(3, this.width / 256);

    public MyDFMaskGroup(int width,int height,int frameCnt){
        init(width,height,frameCnt);
    }

    public void init(int width,int height,int frameCnt){
        this.masks=new MyDetecFrameMask[frameCnt];
        this.width=width;
        this.height=height;
        initColorSet();
    }

    public void initColorSet(){
        this.colorSet=new MyDetecFrameMaskColorSet();
    }
    public void initFrame(int index){
        if(index<0||index>=this.masks.length||null==this.masks[index])
            return;
        this.masks[index].clearMask();
    }

    public MyMask getMask(int index){
        if(index<0||index>=this.masks.length){
            return new MyNullMask();
        }
        else{
            return this.masks[index];
        }
    }

    public void drawFrame(int frameIndex,String tag, int startX, int startY,
                          int width, int height, int lineWidth){
        if(frameIndex<0||frameIndex>=this.masks.length)
            return;
        else{
            if(null==this.masks[frameIndex]){
                this.masks[frameIndex]=new MyDetecFrameMask(this.width,this.height,this.colorSet);
            }
            this.masks[frameIndex].drawFrame(tag, startX, startY, width, height, lineWidth);
        }
    }
    public void drawFrame(int frameIndex,String tag, int startX, int startY,
                          int width, int height){
        drawFrame(frameIndex,tag,startX,startY,width,height,defaultLintWidth);
    }

}
