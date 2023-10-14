package com.bit.dicomdw.detector;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class MyConnector {
    //安卓模拟器 10.0.2.2 表示宿主机，而 127.0.0.1 是指它自己
    //private String urlString = "https://10.0.2.2:10010/";
    private String urlString = "http://10.0.2.2:10010/";

    /**
     * @param file -- 图片
     * @return 接口返回的json数据
     * 原理：模拟form表单提交：把请求头部信息和和img 信息 写入到输出流中，
     * 通过流把img写入到服务器临时目录里，然后服务器再把img移到指定的位置
     * 最后通过写入流来获取post的响应信息。
     * @description 模拟form表单，上传图片
     */
    public JSONObject uploadImg(File file) throws SocketTimeoutException {
        if (!file.exists()) return null;
        DataInputStream in = null;
        OutputStream out = null;
        JSONObject jsonObject = null; // 返回值
        try {
            // 换行符
            final String newLine = "\r\n";
            // 服务器的域名
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // 设置为POST情
            conn.setRequestMethod("POST");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求头参数
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", "image/jpeg");
            conn.setRequestProperty("Content-Disposition",
                    "form-data;name=\"upload_item\";filename=\"" + file.getName() + "\"");

            out = new DataOutputStream(conn.getOutputStream());

            // 数据输入流,用于读取文件数据
            in = new DataInputStream(new FileInputStream(file));
            byte[] bufferOut = new byte[1024];
            int bytes = 0;
            // 每次读1KB数据,并且将文件数据写入到输出流中
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            // 最后添加换行
            out.write(newLine.getBytes());
            in.close();
            out.flush();
            out.close();


            // 定义BufferedReader输入流来读取URL的响应 ----读取返回的结果
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line = null;
            StringBuilder resultS = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                resultS.append(line);
            }
            jsonObject = new JSONObject(resultS.toString());//创建jsonObjec对象

        } catch (SocketTimeoutException e) {
            //如果网络超时则抛出该错误
            throw e;
        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
                if (null != out) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }
    }


//    private final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
//        public boolean verify(String hostname, SSLSession session) {
//            return true;
//        }
//    };

//    /**
//     * @param file -- 图片
//     * @return 接口返回的json数据
//     * 原理：模拟form表单提交：把请求头部信息和和img 信息 写入到输出流中，
//     * 通过流把img写入到服务器临时目录里，然后服务器再把img移到指定的位置
//     * 最后通过写入流来获取post的响应信息。
//     * @description 模拟form表单，上传图片
//     */
//    public JSONObject uploadImg(File file) throws SocketTimeoutException {
//        if (!file.exists()) return null;
//        DataInputStream in = null;
//        OutputStream out = null;
//        JSONObject jsonObject=null; // 返回值
//        try {
//            // 换行符
//            final String newLine = "\r\n";
//            final String boundaryPrefix = "--";
//            // 定义数据分隔线
//            String BOUNDARY = "+=======7d4a6d158c9";
//            // 服务器的域名
//            URL url = new URL(urlString);
//
//            //HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
//            //conn.setHostnameVerifier(DO_NOT_VERIFY);
//            //conn.connect();
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//            // 设置为POST情
//            conn.setRequestMethod("POST");
//            // 发送POST请求必须设置如下两行
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setUseCaches(false);
//            // 设置请求头参数
//            conn.setRequestProperty("connection", "Keep-Alive");
//            conn.setRequestProperty("Charsert", "UTF-8");
//            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY+";");
//
//            out = new DataOutputStream(conn.getOutputStream());
//
//            // 上传文件
//            StringBuilder sb = new StringBuilder();
//            sb.append(boundaryPrefix);
//            sb.append(BOUNDARY);
//            sb.append(newLine);
//            // 文件参数,photo参数名可以随意修改
//            sb.append("Content-Disposition: form-data;name=\"upload_item\";filename=\"" + file.getName()
//                    + "\"" + newLine);
//            sb.append("Content-Type:image/jpeg");
//            // 参数头设置完以后需要两个换行，然后才是参数内容
//            sb.append(newLine);
//            sb.append(newLine);
//
//            // 将参数头的数据写入到输出流中
//            out.write(sb.toString().getBytes());
//
//            // 数据输入流,用于读取文件数据
//            in = new DataInputStream(new FileInputStream(file));
//            byte[] bufferOut = new byte[1024];
//            int bytes = 0;
//            // 每次读1KB数据,并且将文件数据写入到输出流中
//            while ((bytes = in.read(bufferOut)) != -1) {
//                out.write(bufferOut, 0, bytes);
//            }
//            // 最后添加换行
//            out.write(newLine.getBytes());
//            in.close();
//
//            // 定义最后数据分隔线，即--加上BOUNDARY再加上--。
//            byte[] end_data = (newLine + boundaryPrefix + BOUNDARY + boundaryPrefix + newLine)
//                    .getBytes();
//            // 写上结尾标识
//            out.write(end_data);
//            out.flush();
//            out.close();
//
//
//            // 定义BufferedReader输入流来读取URL的响应 ----读取返回的结果
//            BufferedReader reader = new BufferedReader(new InputStreamReader(
//                    conn.getInputStream()));
//            String line = null;
//            StringBuilder resultS = new StringBuilder();
//            while ((line = reader.readLine()) != null) {
//                resultS.append(line);
////                JSONObject jsonObject = new JSONObject(line);//创建jsonObjec对象
////                String json = jsonObject.toString();//josn格式的字符串
////                System.out.println(json);
//            }
//            jsonObject = new JSONObject(resultS.toString());//创建jsonObjec对象
//            //String json = jsonObject.toString();//josn格式的字符串
//            //System.out.println(json);
//
//        } catch (SocketTimeoutException e) {
//            //如果网络超时则抛出该错误
//            throw e;
//        } catch (Exception e) {
//            System.out.println("发送POST请求出现异常！" + e);
//            e.printStackTrace();
//        } finally {
//            try {
//                if (null != in) {
//                    in.close();
//                }
//                if (null != out) {
//                    out.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return jsonObject;
//        }
//    }
}
