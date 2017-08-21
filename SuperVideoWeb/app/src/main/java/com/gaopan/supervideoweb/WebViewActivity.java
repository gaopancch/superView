package com.gaopan.supervideoweb;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pgyersdk.feedback.PgyFeedback;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.pgyersdk.update.PgyUpdateManager;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Timer;
import java.util.TimerTask;

public class WebViewActivity extends Activity {
    private String vip1="http://vip.ifkdy.com/?url=";
    private String letv="http://movie.le.com/";
    private String tencent="http://3g.v.qq.com/";
    private String aiyiqi="http://www.iqiyi.com/dianying/";
    private String youku="http://movie.youku.com/";
    private WebView webView;
    private Button buttonVip,goBack;
    private WebSettings webSetting;
    private ProgressBar progressBar;
    private Button searchButtuon;
    private EditText editText;
    private DrawerLayout mDrawerLayout;
    private TextView appVersion;
    private ListView lvLeftMenu;
    private ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.draw_layout);
        api = WXAPIFactory.createWXAPI(this, Utils.WECHAT_APPID, true);
        api.registerApp(Utils.WECHAT_APPID);
        PgyUpdateManager.register(this, "com.pgyersdk.provider");//检查版本更新情况
        initViews();
        initWebView();
    }

    private  void initViews(){
        buttonVip=(Button)findViewById(R.id.vipButton);
        goBack=(Button)findViewById(R.id.goBack);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        editText=(EditText)findViewById(R.id.edit_text);
        searchButtuon=(Button)findViewById(R.id.search);
        appVersion = (TextView) findViewById(R.id.app_version);
        appVersion.setText("Version:" + Utils.getAPPVersionCode(this));
        lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);
        searchButtuon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=editText.getText().toString();
                webView.loadUrl("https://www.baidu.com/s?wd="+text);
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buttonVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vivipUrl= vip1+webView.getUrl();
                Uri uri = Uri.parse(vivipUrl); // url为你要链接的地址
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(Intent.createChooser(intent, "请选择浏览器"));
            }
        });
        setViews();

    }

    private void setViews(){
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
//        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
//            }
//
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                Log.i("addDrawerListener","onDrawerOpened");
//            }
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                Log.i("addDrawerListener","onDrawerClosed");
//            }
//
//            @Override
//            public void onDrawerStateChanged(int newState) {
//            }
//        });
        String []lvs = new String[]{
                "优酷",
                "爱奇艺",
                "乐视视频",
                "腾讯视频",
                "意见反馈",
//                "分享给QQ好友",
                "关闭程序"};
        //设置菜单列表
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs);
        lvLeftMenu.setAdapter(arrayAdapter);
        lvLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                leftMenuItemClick(position);
            }
        });
    }

    private void leftMenuItemClick(int position) {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
        switch (position) {
            case 0:
                webView.loadUrl(youku);
                break;
            case 1:
                webView.loadUrl(aiyiqi);
                break;
            case 2:
                webView.loadUrl(tencent);
                break;
            case 3:
                webView.loadUrl(letv);
                break;
            case 4:
                // 以对话框的形式弹出 反馈
                PgyFeedback.getInstance().showDialog(WebViewActivity.this);
//                shareSerectBoxToWechat();
                break;
            case 5:
                finish();
//                shareSerectBoxToQQ();
                break;
            case 6:
                finish();
                break;
        }
    }

    protected IWXAPI api;
    private void shareSerectBoxToWechat() {
        if (!api.isWXAppInstalled()) {
            ToastUtils.showMessage(this, "您还未安装微信");
            return;
        }
        // 0-分享给朋友  1-分享到朋友圈
        int flag = 0;
        //share to wechat
        WXTextObject wxTextObject = new WXTextObject();
        wxTextObject.text = "直接点击这个链接可以下载免费观看优酷，" +
                "乐视视频等网站会员视频的软件哦!我用的很爽，你也赶紧试试吧~" +
                "\nhttps://www.pgyer.com/Jtpi";
        WXMediaMessage wxMediaMessage = new WXMediaMessage();
        wxMediaMessage.mediaObject = wxTextObject;
        wxMediaMessage.description = wxTextObject.text;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        ////transaction字段用于唯一标识一个请求，这个必须有，否则会出错
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = wxMediaMessage;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }


    private  void initWebView(){
        webView=(WebView)findViewById(R.id.webView);
        webSetting=webView.getSettings();

        webSetting.setDefaultTextEncodingName("utf-8") ;//这句话去掉也没事。。只是设置了编码格式
        webSetting.setJavaScriptEnabled(true);  //这句话必须保留。。不解释
        webSetting.setDomStorageEnabled(true);//这句话必须保留。。否则无法播放优酷视频网页。。其他的可以

        webSetting.setPluginState(WebSettings.PluginState.ON);
        webSetting.setUseWideViewPort(true); // 关键点
        webSetting.setAllowFileAccess(true); // 允许访问文件
        webSetting.setSupportZoom(true); // 支持缩放
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT); // 不加载缓存内容
        //加载网页
        webView.loadUrl(letv);
        //强制在webview打开网页，防止使用系统默认的浏览器打开网页
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                /**当加载的网页需要重定向的时候就会回调这个函数告知我们应用程序是否需
                 * 要接管控制网页加载，如果应用程序接管，并且return true意味着主程序接
                 * 管网页加载，如果返回false让webview自己处理。*/

                /**webview只能识别http://或https://开头的url, 因此如果要识别其他的scheme
                 * (如: alipays、weixin、mailto、tel ... 等等), 你就要自行处理.
                 * 一般其他的scheme都是由原生APP处理, 即用一个Intent去调起能处理此scheme开头的url的APP*/
//                if( url.startsWith("http:") || url.startsWith("https:") ) {
//                    view.loadUrl(url);
//                    return true;
//                }else {
//                    try {
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                        startActivity(intent);
//                        return true;
//                    }catch (Exception e){
//                        return false;
//                    }
//                }
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                //这里进行无网络或错误处理，具体可以根据errorCode的值进行判断，做跟详细的处理。
//                view.loadUrl(file:///android_asset/error.html );
                Log.i("onReceivedError","errorCode="+errorCode+" description="
                        +description+" failingUrl="+failingUrl);
            }

        });
        webView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                WebViewActivity.this.setProgress(newProgress * 1000);
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if(webView!=null&&webView.canGoBack()){
            webView.goBack();
        }else {
            exitBy2Click();
        }
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this,"再按一次，退出程序",Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 1000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // 自定义摇一摇的灵敏度，默认为950，数值越小灵敏度越高。
        PgyFeedbackShakeManager.setShakingThreshold(1000);
        // 以对话框的形式弹出
        PgyFeedbackShakeManager.register(this);
        // 以Activity的形式打开，这种情况下必须在AndroidManifest.xml配置FeedbackActivity
        // 打开沉浸式,默认为false
        // FeedbackActivity.setBarImmersive(true);
        PgyFeedbackShakeManager.register(this, false);
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PgyFeedbackShakeManager.unregister();
        api.unregisterApp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.source_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId()){

            case R.id.menu_youku:
                webView.loadUrl(youku);
                break;
            case R.id.menu_aiqiyi:
                webView.loadUrl(aiyiqi);
                break;
            case R.id.menu_qq:
                webView.loadUrl(tencent);
                break;
            case R.id.menu_le:
                webView.loadUrl(letv);
                break;
            case R.id.close:
                finish();
            default:
                break;
        }
//         Toast.makeText(MainActivity.this, ""+item.getItemId(), Toast.LENGTH_SHORT).show();

        return super.onOptionsItemSelected(item);
    }
}
