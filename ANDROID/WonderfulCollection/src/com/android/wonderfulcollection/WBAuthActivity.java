package com.android.wonderfulcollection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.WeiboParameters;
import com.sina.weibo.sdk.constant.WBConstants;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sina.weibo.sdk.utils.UIUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WBAuthActivity extends Activity{

	private static final String TAG = "WBAuthCodeActivity";
	
	/**
     * WeiboSDKDemo 程序的 APP_SECRET。
     * 请注意：请务必妥善保管好自己的 APP_SECRET，不要直接暴露在程序中，此处仅作为一个DEMO来演示。
     */
	private static final String WEIBO_DEMO_APP_SECRET = "caf917169426aecf2f9a469fea42a3d0";
    
    /** 通过 code 获取 Token 的 URL */
    private static final String OAUTH2_ACCESS_TOKEN_URL = "https://open.weibo.cn/oauth2/access_token";
    
    /**获取 user 信息的URL*/
    private static final String WEIBO_GET_USERINFO_URL = "https://api.weibo.com/2/users/show.json";
    
    private static final String WEIBO_GET_USERFAVORITE_URL = "https://api.weibo.com/2/favorites/ids.json";
    
    /** 获取 Token 成功或失败的消息 */
    private static final int MSG_FETCH_TOKEN_SUCCESS = 1;
    private static final int MSG_FETCH_TOKEN_FAILED  = 2;
	
    /** 微博 Web 授权接口类，提供登陆等功能  */
    private WeiboAuth mWeiboAuth;
    /** 获取到的 Code */
    private String mCode;
    /** 获取到的 Token */
    private Oauth2AccessToken mAccessToken;
	
	/**
	 * ui控件
	 */
	private Button get_code_button;
	private Button get_token_button;
	private TextView code_textview;
	private TextView token_textview;
	public	TextView userinfo_textview;
	public	TextView userfavorite_textview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        init_ui();
        
        // 初始化微博对象
        mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        
        get_code_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mWeiboAuth.authorize(new AuthListener(), WeiboAuth.OBTAIN_AUTH_CODE);
				
			}
		});
        
        get_token_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				fetchTokenAsync(mCode, WEIBO_DEMO_APP_SECRET);
				
			}
		});       
        
	}
	
	private void init_ui()
	{
		get_code_button = (Button)findViewById(R.id.get_code_button);
		get_token_button = (Button)findViewById(R.id.get_token_button);
		get_token_button.setEnabled(false);
		code_textview = (TextView)findViewById(R.id.code_text);
		token_textview = (TextView)findViewById(R.id.token_text);	
		userinfo_textview = (TextView)findViewById(R.id.userinfo_text);
		userfavorite_textview = (TextView)findViewById(R.id.userfavorite_text);
	}
	
	 /**
     * 微博认证授权回调类。
     */
    class AuthListener implements WeiboAuthListener {
        
        @Override
        public void onComplete(Bundle values) {
            if (null == values) {
                Toast.makeText(WBAuthActivity.this, 
                        "获取code失败", Toast.LENGTH_SHORT).show();
                return;
            }
            
            String code = values.getString("code");
            if (TextUtils.isEmpty(code)) {
                Toast.makeText(WBAuthActivity.this, 
                		"获取code失败", Toast.LENGTH_SHORT).show();
                return;
            }
            
            mCode = code;
            code_textview.setText(code);
            get_token_button.setEnabled(true);
            token_textview.setText("");
            Toast.makeText(WBAuthActivity.this, 
            		"获取code成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(WBAuthActivity.this, 
            		"获取code取消", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            UIUtils.showToast(WBAuthActivity.this, 
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG);
        }
    }
    
    
    /**
     * 该 Handler 配合 {@link RequestListener} 对应的回调来更新 UI。
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG_FETCH_TOKEN_SUCCESS:
                // 显示 Token
                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                        new java.util.Date(mAccessToken.getExpiresTime()));
                String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
                token_textview.setText(String.format(format, mAccessToken.getToken(), date));
                get_token_button.setEnabled(false);
                               
                GetUserInfoTask getUserInfoTask = new GetUserInfoTask(userinfo_textview);
                String user_url = WEIBO_GET_USERINFO_URL + "?uid=" + mAccessToken.getUid() + 
                							"&access_token=" + mAccessToken.getToken();
                getUserInfoTask.execute(user_url);
                
                
                GetUserFavoritesTask getUserFavoritesTask = new GetUserFavoritesTask(userfavorite_textview);
                String favorites_url = WEIBO_GET_USERFAVORITE_URL + "?access_token=" + mAccessToken.getToken();
                
                getUserFavoritesTask.execute(favorites_url);
                
                
                
                Toast.makeText(WBAuthActivity.this, 
                        "获取token成功", Toast.LENGTH_SHORT).show();
                break;
                
            case MSG_FETCH_TOKEN_FAILED:
                Toast.makeText(WBAuthActivity.this, 
                        "获取token失败", Toast.LENGTH_SHORT).show();
                break;
                
            default:
                break;
            }
        };
    };
    
    /**
     * 异步获取 Token。
     * 
     * @param authCode  授权 Code，该 Code 是一次性的，只能被获取一次 Token
     * @param appSecret 应用程序的 APP_SECRET，请务必妥善保管好自己的 APP_SECRET，
     *                  不要直接暴露在程序中，此处仅作为一个DEMO来演示。
     */
    public void fetchTokenAsync(String authCode, String appSecret) {
        /*
        LinkedHashMap<String, String> requestParams = new LinkedHashMap<String, String>();
        requestParams.put(WBConstants.AUTH_PARAMS_CLIENT_ID,     Constants.APP_KEY);
        requestParams.put(WBConstants.AUTH_PARAMS_CLIENT_SECRET, appSecretConstantS.APP_SECRET);
        requestParams.put(WBConstants.AUTH_PARAMS_GRANT_TYPE,    "authorization_code");
        requestParams.put(WBConstants.AUTH_PARAMS_CODE,          authCode);
        requestParams.put(WBConstants.AUTH_PARAMS_REDIRECT_URL,  Constants.REDIRECT_URL);
        */
        WeiboParameters requestParams = new WeiboParameters();
        requestParams.add(WBConstants.AUTH_PARAMS_CLIENT_ID,     Constants.APP_KEY);
        requestParams.add(WBConstants.AUTH_PARAMS_CLIENT_SECRET, appSecret);
        requestParams.add(WBConstants.AUTH_PARAMS_GRANT_TYPE,    "authorization_code");
        requestParams.add(WBConstants.AUTH_PARAMS_CODE,          authCode);
        requestParams.add(WBConstants.AUTH_PARAMS_REDIRECT_URL,  Constants.REDIRECT_URL);
    
        /**
         * 请注意：
         * {@link RequestListener} 对应的回调是运行在后台线程中的，
         * 因此，需要使用 Handler 来配合更新 UI。
         */
        AsyncWeiboRunner.request(OAUTH2_ACCESS_TOKEN_URL, requestParams, "POST", new RequestListener() {
            @Override
            public void onComplete(String response) {
                LogUtil.d(TAG, "Response: " + response);
                
                // 获取 Token 成功
                Oauth2AccessToken token = Oauth2AccessToken.parseAccessToken(response);
                if (token != null && token.isSessionValid()) {
                    LogUtil.d(TAG, "Success! " + token.toString());
                    
                    mAccessToken = token;
                    mHandler.obtainMessage(MSG_FETCH_TOKEN_SUCCESS).sendToTarget();
                } else {
                    LogUtil.d(TAG, "Failed to receive access token");
                }
            }
    
            @Override
            public void onComplete4binary(ByteArrayOutputStream responseOS) {
                LogUtil.e(TAG, "onComplete4binary...");
                mHandler.obtainMessage(MSG_FETCH_TOKEN_FAILED).sendToTarget();
            }
    
            @Override
            public void onIOException(IOException e) {
                LogUtil.e(TAG, "onIOException： " + e.getMessage());
                mHandler.obtainMessage(MSG_FETCH_TOKEN_FAILED).sendToTarget();
            }
    
            @Override
            public void onError(WeiboException e) {
                LogUtil.e(TAG, "WeiboException： " + e.getMessage());
                mHandler.obtainMessage(MSG_FETCH_TOKEN_FAILED).sendToTarget();
            }
        });
    }
	
	
	
}
