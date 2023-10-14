package com.bit.dicomdw.dcm.transform;


import com.mip.dcm.MyMatrix;
import com.mip.dcm.color.MyColor;
import com.mip.dcm.img.MyImg;
import com.mip.dcm.img.MyRealImg;
import com.mip.dcm.interpolation.MyInterpolation;
import com.mip.dcm.interpolation.MyNearestNeighborInterpolation;

public class MyScaleTransform implements MyTransform {
    private double scaleRate;
    private MyMatrix mat_inverse=new MyMatrix();
    private MyInterpolation interpolationStrategy=null;
    //用于临时计算矩阵的posMul
    private double[] _tmp_pos=new double[2];

    public MyScaleTransform(double scaleRate){
        this(scaleRate,new MyNearestNeighborInterpolation());
    }

    public MyScaleTransform(double scaleRate, MyInterpolation interpolationStrategy){
        setScaleRate(scaleRate);
        setInterpolationStrategy(interpolationStrategy);
    }

    public void setInterpolationStrategy(MyInterpolation interpolationStrategy) {
        this.interpolationStrategy = interpolationStrategy;
    }

    public void setScaleRate(double scaleRate){
        this.scaleRate=scaleRate;
        this.mat_inverse.setScale(scaleRate);
        this.mat_inverse=this.mat_inverse.inverse();
    }

    @Override
    public MyImg work(MyImg img){
        if(null==this.mat_inverse)
            return img;
        this.interpolationStrategy.setImg(img);
        int w=(int)(img.getWidth()*scaleRate);
        int h=(int)(img.getHeight()*scaleRate);
        MyRealImg _img=new MyRealImg(w,h);
        MyColor c=new MyColor();
        int cnt=0;
        for(int y=0;y<h;y++){
            for(int x=0;x<w;x++){
                mat_inverse.posMul(x,y,_tmp_pos);
                interpolationStrategy.getPixelColor(_tmp_pos[0],_tmp_pos[1],c);
                _img.setColor(x,y,c);
            }
        }
        return _img;
    }
}
