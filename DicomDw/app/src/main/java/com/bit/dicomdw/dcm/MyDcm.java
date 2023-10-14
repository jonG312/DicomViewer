package com.bit.dicomdw.dcm;

import com.imebra.*;
import com.bit.dicomdw.dcm.colorstrategy.MyColorStrategy;
import com.bit.dicomdw.dcm.colorstrategy.MyNormalColorStrategy;
import com.bit.dicomdw.dcm.colorstrategy.MyNullColorStrategy;
import com.bit.dicomdw.dcm.exception.MyDcmImgLoadException;
import com.bit.dicomdw.dcm.img.MyBitmapImg;
import com.bit.dicomdw.dcm.img.MyDcmImg;
import com.bit.dicomdw.dcm.img.MyImg;
import com.bit.dicomdw.dcm.img.MyImgWrapper;
import com.bit.dicomdw.dcm.img.MyRealImg;

import java.io.File;

/**
 * MyDcm是最重要的一个类
 * 连接了我们的项目与imebra库
 * 提供了获取制定帧的Dcm图图像以及获取图像以外部分信息的功能
 * (暂时不提供获取实际帧数量的方法,因为这个接口我还没找到)
 * <p>
 * 主要接口如下:
 * <p>
 * public MyDcm(String path)
 * 根据文件路径构造,在内部调用了该类的load方法
 * 传入文件路径,加载dcm文件
 * (实际上真正加载到内存的只有文件的一部分)
 * <p>
 * public MyDcm()
 * 这个构造器不传入参数,具体文件可以之后载入
 * <p>
 * public void load(String path)
 * 根据传入路径加载dcm文件
 * 如果不是.dcm后缀的文件,相关参数会设置为null
 * <p>
 * public MyImgWrapper getMyImgWrapper(long frameNum)
 * 根据帧的编号(表示第几几帧,第一帧对应0,第二帧对应1,以此类推)获取图像
 * 获取的是 MyImgWrapper类,它implements了MyImg的接口
 * (之所以取这个名字是因为我以前写的MyDcmImg与MyImg不兼容,所以使用过适配器模式)
 * <p>
 * public String getInfo(MyDcmTag tag)
 * 根据传来的tag获取有关的dicom信息
 * 如病人名字,拍摄日期等
 * 传入参数为MyDcmImg枚举类型
 * 这样做是为了限定输入,防止不可预测的输入造成的实现困难
 * 设计上只会返回String类型,如果要类型转换,请在外部根据实际情况转换
 * <p>
 * public int getImgWidth()
 * public int getImgHeight()
 * dicom图像各帧的宽和高是统一的(根据imebra提供的接口推测),所以我只存了一组宽和高
 * 单位是像素
 */
public class MyDcm {
    static {
        //首先要加载imebra
        System.loadLibrary("imebra_lib");
    }

    private DataSet ds;
    //    private final static short MAX_LOAD_SIZE=2048;
    private final static short MAX_LOAD_SIZE = (short) (4096);

    private int imgWidth;
    private int imgHeight;
    private int frameCnt;

    private MyColorStrategy colorStrategy;
    //如果不是DCM图像，则应该用这个img，用来表示普通的图像
    private MyImg img;

    public MyDcm(String path) {
        this.load(path);
    }

    public MyDcm() {
        ds = null;
        imgWidth = 0;
        imgHeight = 0;
        frameCnt = 0;
        img = null;
        colorStrategy = new MyNullColorStrategy();
    }

    //加载path所指示的dcm文件
    public void load(String path) throws MyDcmImgLoadException {
        if (checkDcmName(path)) {
            img = null;
            ds = CodecFactory.load(path, MAX_LOAD_SIZE);
            imgWidth = Integer.parseInt(getInfo(MyDcmTag.ImageColumns));
            imgHeight = Integer.parseInt(getInfo(MyDcmTag.ImageRows));
            //读取帧数信息
            try {
                frameCnt = Integer.parseInt(getInfo(MyDcmTag.NumberOfFrames));
            } catch (RuntimeException e) {
                frameCnt = 1;
                //C++的异常,会返回一大串字符(串),有点难处理
//                if(e.getMessage().equals("The requested tag is missing")){
//                    frameCnt=1;
//                }else{
//                    Log.d("error_________________",e.getMessage());
//                    throw new MyDcmTagReadException("标签对应值因未知原因无法读取");
//                }
            }
            //读取调窗信息
            try {
                double c = Double.parseDouble(getInfo(MyDcmTag.WindowCenter));
                double w = Double.parseDouble(getInfo(MyDcmTag.WindowWidth));
//                long _min=(long)(c - 0.5 - (w-1) /2);
//                long _max=(long)(c - 0.5 + (w-1) /2);
                long _min = (long) (c - w / 2.0 - 0.5);
                long _max = (long) (c + w / 2.0 + 0.5);
                this.colorStrategy = new MyNormalColorStrategy(_min, _max);
            } catch (RuntimeException e) {
                colorStrategy = new MyNullColorStrategy();
            }
        } else {
            //throw new MyDcmImgLoadException("打开的不是 dicom 图像");
            ds = null;
            try {
                img = new MyBitmapImg(path);
                imgWidth = img.getWidth();
                imgHeight = img.getHeight();
                frameCnt = 1;
            } catch (Exception e) {
                //这里偷懒了，没写具体的异常类型
                //如果没有读取到图像
                img = null;
                imgWidth = imgHeight = 0;
                frameCnt = 0;
                throw new MyDcmImgLoadException("不是可读取的文件类型");
            }
        }
    }

    public void save(String filePath, MyDcmSavingType savingType) {
        codecType_t codecType = savingType.getType();
        CodecFactory.save(ds, filePath, codecType);
    }

    public MyColorStrategy getColorStrategy() {
        return this.colorStrategy;
    }


    //旧版代码
//    public MyDcmImg getMyDcmImg(long frameNum){
//        if(null==ds)return null;
//        Image img=ds.getImage(frameNum);
//        MyDcmImg myDcmImg =new MyDcmImg(img);
//        return myDcmImg;
//    }

    //获取frameNum(帧数)对应的MyImgWarpper
    public MyImgWrapper getMyImgWrapper(long frameNum) {
        if (null == ds) {
            if (null == this.img)
                return null;
            return new MyImgWrapper(this.img);
        }
        //Image img=ds.getImage(frameNum);
        /**注意这一步,它帮我处理了很多麻烦的转换过程*/
        Image img = ds.getImageApplyModalityTransform(frameNum);
        MyImg myDcmImg = new MyDcmImg(img);
        myDcmImg = new MyRealImg(myDcmImg);
        return new MyImgWrapper(myDcmImg, this.colorStrategy);
    }


    //根据tag获取图像之外的信息
    public String getInfo(MyDcmTag tag) throws RuntimeException {
        if (null == this.ds)
            return null;
        return ds.getString(new TagId(tag.getGroup(), tag.getElement()), 0);
    }

    public int getImgWidth() {
        return imgWidth;
    }

    public int getImgHeight() {
        return imgHeight;
    }

    public int getFrameCnt() {
        return frameCnt;
    }


    //私有
    //用于检查是否是dcm后缀的图像名
    //但是不够严格,如果真的要检查图像合法性,仅这一点是不够的
    private boolean checkDcmName(String path) {
        if (path.length() > 2048 || path.length() <= 4)
            return false;
        File file = new File(path);
        String fileName = file.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        //注意,字符串比较要用equals
        return suffix.equals("dcm");
    }

}