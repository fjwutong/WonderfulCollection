package com.android.wonderfulcollection;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.widget.TextView;

public class GetUserFavoritesTask extends AsyncTask<String, Void, Boolean> {
	
	private TextView userfavorite_textview;
    private String result = "";

    public GetUserFavoritesTask(TextView userFavoritesTextview) {
        this.userfavorite_textview = userFavoritesTextview;
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
        	UserFavoritesJsonParser userFavoritesJsonParser = new UserFavoritesJsonParser();
        	UserFavorites userFavorites = userFavoritesJsonParser.GetUserFavoritesByJson(result);
        	userfavorite_textview.setText(userFavorites.toString());
        } else {
        	userfavorite_textview.setText("NULL"); 
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}
