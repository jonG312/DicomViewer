package com.bit.dicomdw.detector;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.bit.dicomdw.dcm.MyDcm;
import com.bit.dicomdw.dcm.color.MyColor;
import com.bit.dicomdw.dcm.img.MyImg;
import com.bit.dicomdw.dcm.mask.MyDFMaskGroup;
import com.bit.dicomdw.dcm.mask.MyMask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;

public class MyDetector {
    private MyDcm DCM = null;
    private MyDFMaskGroup maskGroup = null;
    private boolean[] status_onWaiting = null;
    private com.bit.dicomdw.detector.MyConnector connector = new com.bit.dicomdw.detector.MyConnector();

    /**
     * 用于初始化
     *
     * @param dcm MyDcm对象，其中有1或多张医学图像
     */
    public void setDCM(MyDcm dcm) {
        this.DCM = dcm;
        this.maskGroup = new MyDFMaskGroup(DCM.getImgWidth(), DCM.getImgHeight(), DCM.getFrameCnt());
        this.status_onWaiting = new boolean[dcm.getFrameCnt()];
    }

    /**
     * 检测某一帧是否正在被处理
     *
     * @param frameIndex 帧的索引
     * @return
     */
    public boolean checkWaiting(int frameIndex) {
        if (frameIndex < 0 || frameIndex >= DCM.getFrameCnt()) {
            return true;
        }
        return this.status_onWaiting[frameIndex];
    }

    /**
     * 获取索引对应的mask，用于相应帧图像的显示
     *
     * @param index 帧的索引
     * @return
     */
    public MyMask getMask(int index) {
        if (index < 0 || index >= DCM.getFrameCnt() || status_onWaiting[index]) {
            return null;
        } else {
            return this.maskGroup.getMask(index);
        }
    }

    /**
     * 将index对应图片以jpeg格式发送到服务器
     * 由服务器生成检测框，将结果以json格式传回
     *
     * @param index 帧的索引号
     * @throws IOException            产生临时图片的时候可能抛出该错误
     * @throws SocketTimeoutException 获取json结果的过程中可能出现网络超时
     */
    public void detect(int index) throws IOException, SocketTimeoutException {
        if (index < 0 || index >= DCM.getFrameCnt() || this.status_onWaiting[index]) {
            return;
        }
        //加锁，保证同一个线程里只有一个detect在运行
        synchronized (this.status_onWaiting) {
            this.status_onWaiting[index] = true;
            //+++++++++++++++++++++TODO test
            //this.maskGroup.drawFrame(index, "hhh", 0, 0, 20, 30,2);
            //+++++++++++++++++++++
            File tmpf = null;
            FileOutputStream fos = null;
            try {
                Bitmap bm = getBitmap(index);
                //产生临时图片
                tmpf = File.createTempFile("tmp_bitmap", ".jpg");
                fos = new FileOutputStream(tmpf);
                bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                //送往服务器并获取结果
                JSONObject json = this.connector.uploadImg(tmpf);
                //处理json结果
                this.drawFrames(index, json);

            } catch (SocketTimeoutException e) {
                throw e;
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != fos) {
                    fos.flush();
                    fos.close();
                }
                if (null != tmpf) {
                    tmpf.deleteOnExit();
                }
                this.status_onWaiting[index] = false;
            }

        }

    }

    /**
     * 解析json，画框
     *
     * @param frameIndex 帧号
     * @param json       json格式数据
     * @return 如果失败返回false
     */
    private boolean drawFrames(int frameIndex, JSONObject json) {
        if (frameIndex < 0 || frameIndex >= DCM.getFrameCnt() || null == json) {
            return false;
        }
        /**
         * {
         *      "frames":[
         *          {
         *              "posX":100
         *              "posY":100
         *              "width":100
         *              "height":100
         *              "tag":"XXXX"
         *          }
         *      ]
         * }
         */
        try {
            JSONArray frameInfoArray = json.getJSONArray("frames");
            JSONObject frameInfo = null;
            this.maskGroup.initFrame(frameIndex);
            for (int i = 0; i < frameInfoArray.length(); i++) {
                frameInfo = frameInfoArray.getJSONObject(i);
                int posX = frameInfo.getInt("posX");
                int posY = frameInfo.getInt("posY");
                int width = frameInfo.getInt("width");
                int height = frameInfo.getInt("height");
                String tag = frameInfo.getString("tag");
                this.maskGroup.drawFrame(frameIndex, tag, posX, posY, width, height);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private Bitmap getBitmap(int index) {
        if (index < 0 || index >= DCM.getFrameCnt()) {
            return null;
        }
        MyImg img = DCM.getMyImgWrapper(index);
        int w = img.getWidth();
        int h = img.getHeight();
        MyColor c = new MyColor();
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                img.getPixelColor(x, y, c);
                bm.setPixel(x, y, Color.rgb(c.r, c.g, c.b));
            }
        }
        return bm;
    }

}
