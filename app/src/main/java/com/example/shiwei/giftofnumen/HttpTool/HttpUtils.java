package com.example.shiwei.giftofnumen.HttpTool;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by shiwei on 2016/8/11.
 */
public class HttpUtils {
    private static ExecutorService executorService;

    //定义一个线程池
    public static void getHttp(String path, HttpCallBack httpcallback, int requestCode) {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(3);
            executorService.execute(new HttpRunable(path, httpcallback, requestCode, false, null));
        } else {
            executorService.execute(new HttpRunable(path, httpcallback, requestCode, false, null));
        }
    }

    public static void postHttp(String path, HttpCallBack httpcallback, int requestCode, Map<String, Object> map) {
        Set<String> strings = map.keySet();
        String next = strings.iterator().next();
        Object s = null;
        s = map.get(next);
        StringBuilder builder = new StringBuilder();
        builder.append(next).append("=").append(s);
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(3);
            executorService.execute(new HttpRunable(path, httpcallback, requestCode, true, builder.toString()));
        } else {
            executorService.execute(new HttpRunable(path, httpcallback, requestCode, true, builder.toString()));
        }
    }

    public static class HttpRunable implements Runnable {
        private HttpCallBack httpcallback;
        private String path;
        private int requestcode;
        private InputStream inputStream;
        private boolean isPost;
        private String params;
        private Handler handle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //主线程接收来自子线程的数据,获取网络请求的数据；
                String s = msg.obj.toString();
                if (httpcallback != null) {
                    httpcallback.onsuccess(s, requestcode);
                }
            }
        };


        public HttpRunable(String path, HttpCallBack httpcallback, int requestcode, boolean isPost, String params) {
            this.path = path;
            this.requestcode = requestcode;
            this.httpcallback = httpcallback;
            this.isPost = isPost;
            this.params = params;
        }

        @Override
        public void run() {
            try {
                //打开网络，获取网络流
                URL url = new URL(path);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                if (isPost) {
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.getOutputStream().write(params.getBytes());
                    httpURLConnection.getOutputStream().flush();
                }
                httpURLConnection.connect();
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                    byte[] bytes = new byte[1024];
                    int len = 0;
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((len = inputStream.read(bytes)) != -1) {
                        stringBuilder.append(new String(bytes, 0, len));
                    }
                    String result = new String(stringBuilder.toString().getBytes("utf-8"));
                    //子线程向主线程发送消息
                    Message message = handle.obtainMessage();
                    message.obj = result;
                    message.sendToTarget();//此Target就是handle对像；
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
