package com.gaopan.supervideoweb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

import java.util.Random;

public class MainActivity extends Activity {
    private RelativeLayout relativeLayout;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        relativeLayout=(RelativeLayout)findViewById(R.id.activity_main);
        initCover();
        // com.getui.demo.DemoPushService 为第三⽅方⾃自定义推送服务
//        PushManager.getInstance().initialize(this.getApplicationContext(),
//                com.gaopan.supervideoweb.service.GeTuiPushService.class);
//        // com.getui.demo.DemoIntentService 为第三.方.自定义的推送服务事件接收类
//        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(),
//                com.gaopan.supervideoweb.service.GeTuiIntentService.class);

        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,"CmQooDqhNzpux01wBZdg7FFu");
        button=(Button)findViewById(R.id.go);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this, WebViewActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void initCover(){
        relativeLayout.setBackgroundResource(R.drawable.alert);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

