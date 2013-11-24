package com.android.wonderfulcollection;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.widget.TextView;

import com.android.wonderfulcollection.UserInfo;
import com.android.wonderfulcollection.UserInfoJsonParser;

public class GetUserInfoTask extends AsyncTask<String, Void, Boolean> {
	
	private TextView userinfo_textview;
    private String result = "";

    public GetUserInfoTask(TextView userInfoTextview) {
        this.userinfo_textview = userInfoTextview;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        boolean isGetInfo = false;
        String urlText = params[0];
        try {
            URL url = new URL(urlText);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int len = 0;
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {//若当前连接成功
                isGetInfo = true;
                InputStream inStream = conn.getInputStream();//打开输入流
                while ((len = inStream.read(data)) != -1) {
                    outStream.write(data, 0, len);
                }
                result = new String(outStream.toByteArray());//新建result变量用于获取服务器端传回的字符串
                System.out.println("result = " + result); 
                inStream.close();//关闭数据输入流
            }
            outStream.close();//关闭数据输出流
            conn.disconnect();//关闭远程连接
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isGetInfo;
    }

    @Override
    protected void onPostExecute(Boolean isGetInfo) {
        super.onPostExecute(isGetInfo);
        if (isGetInfo) {
        	UserInfoJsonParser userInfoJsonParser = new UserInfoJsonParser();
        	UserInfo userInfo = userInfoJsonParser.GetUserInfoByJson(result);
        	userinfo_textview.setText(userInfo.toString());
        } else {
        	userinfo_textview.setText("NULL"); 
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}
