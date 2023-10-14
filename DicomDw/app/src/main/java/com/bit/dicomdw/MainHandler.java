package com.bit.dicomdw;

import android.graphics.Bitmap;

import com.bit.dicomdw.dcm.MyCanvas;
import com.bit.dicomdw.dcm.MyDcm;
import com.bit.dicomdw.dcm.MyDcmSavingType;
import com.bit.dicomdw.dcm.MyMatrix;
import com.bit.dicomdw.dcm.MyPair;
import com.bit.dicomdw.dcm.colorstrategy.MyColorStrategy;
import com.bit.dicomdw.dcm.colorstrategy.MyNullColorStrategy;
import com.bit.dicomdw.dcm.img.MyImgWrapper;
import com.bit.dicomdw.dcm.mask.MyMask;
import com.bit.dicomdw.dcm.transform.MyRotateTransform;
import com.bit.dicomdw.dcm.transform.MyScaleTransform;
import com.bit.dicomdw.dcm.transform.MyTransform;
import com.bit.dicomdw.detector.MyDetector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;

public class MainHandler {
    //软件读入的DCM图像
    public MyDcm DCM;
    public MyCanvas canvas;
    private MyImgWrapper img=null;
    public MyColorStrategy colorStrategy=null;
    private int currentFrameIndex=0;
    private MyDetector detector=new MyDetector();


//    //canvas窗口大小
//    private static final int WIDTH=256;

    //canvas窗口大小
//    private static final int WIDTH=400;
    private static final int WIDTH=400;
    private static int HEIGHT;


    public MainHandler(int view_width, int view_height, Bitmap.Config config){
        double h_w_ratio=(double)view_height/view_width;
        int canvas_width=WIDTH;
        int canvas_height=(int)(h_w_ratio * WIDTH);
        HEIGHT=canvas_height;
        this.canvas=new MyCanvas(canvas_width,canvas_height,config);
        this.DCM=new MyDcm();
//        colorStrategy=new MyNormalColorStrategy(0,255);
        colorStrategy=new MyNullColorStrategy();
    }

    public void read_DICOM(String path){
        DCM.load(path);
        this.detector.setDCM(DCM);
        setFrame(0);
    }

    public int getCanvasWidth(){return WIDTH;}
    public int getCanvasHeight(){return HEIGHT;}
    public int getImgWidth(){return canvas.getImgWidth();}
    public int getImgHeight(){return canvas.getImgHeight();}
    public int getCurrentFrameIndex(){return this.currentFrameIndex;}

    public boolean checkCanvasImgNotNull(){
        if(null==canvas)
            return false;
        if(null==img)
            return false;
        if(null==canvas.getImg())
            return false;
        return true;
    }

    public boolean setFrame(int frameNum){
        if(frameNum<0 || frameNum>=DCM.getFrameCnt()){
            return false;
        }else{
            this.currentFrameIndex=frameNum;
            img=DCM.getMyImgWrapper(frameNum);
            canvas.setImg(img);
            this.setMask(frameNum);

//            //换成双线性插值
//            img.setInterpolationStrategy(new MyBilinearInterpolation());
//            canvas.setInterpolationStrategy(new MyBilinearInterpolation());
            return true;
        }
    }

    public void detect() throws SocketTimeoutException,IOException {
        this.detector.detect(this.currentFrameIndex);
    }
    public boolean checkOnDetecting(int frameNum){
        return detector.checkWaiting(frameNum);
    }

    /**
     * 为当前canvas内的img设置mask，如果不是当前帧，什么也不做并返回false
     * @param frameNum
     * @return
     */
    public boolean setMask(int frameNum){
        if(frameNum==this.currentFrameIndex){
            return setMask();
        }
        return false;
    }
    public boolean setMask(){
        int frameNum=this.currentFrameIndex;
        MyMask mask=this.detector.getMask(frameNum);
        if(null!=mask){
            canvas.setMask(this.detector.getMask(frameNum));
        }else{
            return false;
        }
        return true;
    }

//    public boolean resetImg(){
//        if(!checkCanvasImgNotNull()){
//            return false;
//        }else{
//            canvas.setImg(img);
//            return true;
//        }
//    }

    public void setColorStrategy(MyColorStrategy colorStrategy){
        this.colorStrategy=colorStrategy;
        if(null!=img)
            this.img.setColorStrategy(colorStrategy);
    }

    public Bitmap getBitmap(){
        return canvas.getBitmap();
    }

    public void imgRotate(){
        MyTransform trans=new MyRotateTransform();
        transform(trans);
    }

    public void imgScale(double scale_rate){
        MyScaleTransform t=new MyScaleTransform(scale_rate);
        img.change(t);
        //其实下面这个没必要
        canvas.setImg(img);
    }

    public void setCanvasMatrix(MyMatrix mat){
        this.canvas.setMatrix(mat);
    }

    public MyMatrix getCanvasMatrix(){
        MyMatrix mat=new MyMatrix(this.canvas.getMatrix());
        return mat;
    }

    public int getFrameCnt(){
        return this.DCM.getFrameCnt();
    }

    public void posMap(MyPair coordinate){
        coordinate.x=WIDTH * coordinate.x;
        coordinate.y=HEIGHT * coordinate.y;
    }
    //image transform
    public void transform(MyTransform transform){
        this.img.change(transform);
        this.canvas.setImg(this.img);
    }

    //给第一个赋值
    public void assignment(MyPair first,MyPair second){
        first.x=second.x;
        first.y=second.y;
    }
    //计算哈弗曼距离
    public double HuffmanDist(MyPair one,MyPair two){
        return Math.abs(one.x-two.x)+Math.abs(one.y-two.y);
    }

    @Deprecated // TODO 现在改为只存当前图片为jpg图片，暂时还没处理文件已存在的情况
    public boolean save(String filePath, MyDcmSavingType savingType){
        //this.DCM.save(filePath,savingType);
        Bitmap currentBitmap=this.canvas.getImgBitmap();
        filePath=filePath+".jpg"; // 加后缀
        File file=new File(filePath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        currentBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        return true;
    }

}
