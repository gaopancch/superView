package com.gaopan.supervideoweb;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShowActivity extends Activity {
    private TextView title,contnet;
    private Button actionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        final String url=getIntent().getStringExtra("url");
        title=(TextView)findViewById(R.id.titleTextView);
        contnet=(TextView)findViewById(R.id.contentTextView);
        actionButton=(Button)findViewById(R.id.actionButton);
        if(!TextUtils.isEmpty(url)){
            actionButton.setVisibility(View.VISIBLE);
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("http://vip.ifkdy.com/?url="+url); // url为你要链接的地址
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(Intent.createChooser(intent, "请选择浏览器"));
                }
            });
        }
        String t=getIntent().getStringExtra("title");
        String c=getIntent().getStringExtra("content");
        if(!TextUtils.isEmpty(t)&&!TextUtils.isEmpty(c)) {
            title.setText(t);
            contnet.setText("   " + c);
        }
    }
}
