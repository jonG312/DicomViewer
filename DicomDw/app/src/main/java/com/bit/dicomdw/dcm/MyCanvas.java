package com.bit.dicomdw.dcm;


import android.graphics.Bitmap;
import android.graphics.Color;

import com.bit.dicomdw.dcm.color.MyColor;
import com.bit.dicomdw.dcm.img.MyBitmapImg;
import com.bit.dicomdw.dcm.img.MyImg;
import com.bit.dicomdw.dcm.interpolation.MyInterpolation;
import com.bit.dicomdw.dcm.interpolation.MyNearestNeighborInterpolation;
import com.bit.dicomdw.dcm.mask.MyMask;

/**
 * MyCanvas
 * 这个类负责提供一个浏览图像的窗口
 * 这个窗口可以单独控制(移动,旋转,放大视野等),以实现漫游功能
 *
 * 主要对外接口为:
 *
 * public MyCanvas()
 *      负责构造MyCanvas
 *      装换矩阵(或其逆矩阵)初始为单位矩阵
 *      默认是最近邻插值
 *
 * public void getPixelColor(int x,int y,int[] color)
 *      传入坐标获取颜色
 *      其中坐标原点为图像左上角,横轴为横向向右,纵轴为纵向向下
 *      实际上就是C语言二维数组的风格
 *
 * public void setMatrixInverse(MyMatrix mat)
 *      设置变换矩阵的逆矩阵
 *      需要注意的是,由于存储的是mat的逆矩阵
 *      所以传入的时候要保证这个mat的正确性(如可逆性)
 *
 * public boolean setMatrix(MyMatrix mat)
 *      设置转换矩阵
 *      这个矩阵回在函数内进行求逆
 *      如果矩阵不可逆,什么都不会做,并返回false
 *      反之存储mat的逆矩,并返回true
 *
 * public boolean setInterpolationStrategy(MyInterpolation interpolationStrategy)
 *      用于更换插值策略
 *      如果传入的参数为null,更换不成功,返回false
 *      反之返回true
 *
 * 剩余方法是getter与setter方法,由函数名就可以推测出含义
 *
 */
public final class MyCanvas {
    private MyImg img=null;
    private MyInterpolation interpolationStrategy=null;
    //用于临时计算矩阵的posMul
    private double[] _tmp_pos=new double[2];

    //变化矩阵的逆矩阵
    //变化矩阵指的是由原图像状态变换到新图像状态的矩阵
    //现在由于要反推回去,又想不每次都求逆矩阵,所以我就存逆矩阵
    //下面注释中简单记为 Mi
    private MyMatrix mat_inverse =new MyMatrix();

    private Bitmap bm_display=null;

    //构造函数
    //Mi初始化为单位矩阵
    public MyCanvas(int width,int height,Bitmap.Config config){
        this.bm_display=Bitmap.createBitmap(width,height,config);
        mat_inverse.setIdentity();
        interpolationStrategy=new MyNearestNeighborInterpolation();
    }

    //获取img
    public MyImg getImg() {
        return img;
    }

    //设置img
    public void setImg(MyImg img) {
        this.img = img;
        this.interpolationStrategy.setImg(this.img);
    }

    //加mask，改变img
    public void setMask(MyMask mask){
        int w=this.img.getWidth();
        int h=this.img.getHeight();
        MyColor c=new MyColor();
        Bitmap bm=Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        for(int y=0;y<h;y++){
            for(int x=0;x<w;x++){
                // 如果该像素可以由mask决定，就使用mask的颜色，否则使用img的颜色
                if(!mask.getPixelColor(x,y,c)){
                    this.img.getPixelColor(x,y,c);
                }
                bm.setPixel(x,y,Color.rgb(c.r,c.g,c.b));
            }
        }
        this.img=new MyBitmapImg(bm);
        this.interpolationStrategy.setImg(this.img);
    }

    //获取this.img的bitmap，主要是可以用来save成png或者jpg等格式的文件
    public Bitmap getImgBitmap(){
        int w=this.img.getWidth();
        int h=this.img.getHeight();
        MyColor c=new MyColor();
        Bitmap bm=Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        for(int y=0;y<h;y++){
            for(int x=0;x<w;x++){
                this.img.getPixelColor(x,y,c);
                bm.setPixel(x,y,Color.rgb(c.r,c.g,c.b));
            }
        }
        return bm;
    }

    //获取用于显示的bitmap
    public Bitmap getBitmap(){
        int w=bm_display.getWidth();
        int h=bm_display.getHeight();
        MyColor c=new MyColor();
        for(int y=0;y<h;y++){
            for(int x=0;x<w;x++){
                getPixelColor(x,y,c);
                bm_display.setPixel(x,y,Color.rgb(c.r,c.g,c.b));
            }
        }
        return bm_display;
    }



    //获取像素值
    public void getPixelColor(int x,int y,MyColor color){
        mat_inverse.posMul(x,y,_tmp_pos);
        interpolationStrategy.getPixelColor(_tmp_pos[0],_tmp_pos[1],color);
    }


    //获取矩阵逆
    public MyMatrix getMatrixInverse(){
        return this.mat_inverse;
    }

    //获取矩阵
    public MyMatrix getMatrix(){
        return this.mat_inverse.inverse();
    }

    //设置矩阵逆
    public void setMatrixInverse(MyMatrix mat){
        this.mat_inverse.setMatrix(mat);
    }

    //设置矩阵
    public boolean setMatrix(MyMatrix mat){
        MyMatrix _mat=mat.inverse();
        if(null==_mat)
            return false;
        this.mat_inverse.setMatrix(_mat);
        return true;
    }

    public int getImgWidth(){
        if(null==img)
            return 0;
        else
            return img.getWidth();
    }

    public int getImgHeight(){
        if(null==img)
            return 0;
        else
            return img.getHeight();
    }

    //用于更换插值策略
    //如果传入的参数为null,更换不成功,返回false
    //反之返回true
    public boolean setInterpolationStrategy(MyInterpolation interpolationStrategy){
        if(null==interpolationStrategy)
            return false;
        else{
            this.interpolationStrategy=interpolationStrategy;
            this.interpolationStrategy.setImg(img);
            return true;
        }
    }
}
