package com.bit.dicomdw.dcm.colorstrategy;


import com.bit.dicomdw.dcm.color.MyColor;

/**
 * 这个ColorStrategy功能是:
 * 截取lowerBound和upperBound中间段
 * 均匀分配这段的值到[0,255]区间,并取整数
 * (对rgb通道分别进行,lowerBound和upperBound共享)
 */
public class MyNormalColorStrategy extends MyColorStrategy {
    private long lowerBound;
    private long upperBound;

    public MyNormalColorStrategy(long lowerBound,long upperBound){
        if(lowerBound>upperBound){
            this.lowerBound=upperBound;
            this.upperBound=lowerBound;
        }else{
            this.lowerBound=lowerBound;
            this.upperBound=upperBound;
        }
    }

    private static final int samples_per_pixel=255;//取255是为了防止出现256

    @Override
    public void map(MyColor fromColor, MyColor toColor){
        long len=upperBound-lowerBound;
        if(len<=0){
            toColor.set(0,0,0);
            return;
        }else{
            if(toColor.r>upperBound || toColor.r<lowerBound
                    || toColor.g>upperBound || toColor.g<lowerBound
                    || toColor.b>upperBound || toColor.b<lowerBound ){
                toColor.set(0,0,0);
                return;
            }
            toColor.r=(int)((double)(fromColor.r-lowerBound)/len * samples_per_pixel);
            toColor.g=(int)((double)(fromColor.g-lowerBound)/len * samples_per_pixel);
            toColor.b=(int)((double)(fromColor.b-lowerBound)/len * samples_per_pixel);
        }
    }

    public long getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(long lowerBound) {
        this.lowerBound = lowerBound;
    }

    public long getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(long upperBound) {
        this.upperBound = upperBound;
    }

}
