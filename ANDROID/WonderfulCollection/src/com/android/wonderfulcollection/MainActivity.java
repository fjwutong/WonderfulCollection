package com.android.wonderfulcollection;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.android.wonderfulcollection.WBAuthActivity;;

public class MainActivity extends Activity {
	
	private Button code_auth_button;
	
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init_ui();
        code_auth_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WBAuthActivity.class));
            }
        });
        
    }
    
    
    private void init_ui()
    {
    	code_auth_button = (Button)findViewById(R.id.code_auth_button);
    	
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
