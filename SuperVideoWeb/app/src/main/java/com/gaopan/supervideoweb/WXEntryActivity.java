package com.gaopan.supervideoweb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Utils.WECHAT_APPID, false);
        api.handleIntent(getIntent(), this);
        Log.i("wechat", "onCreate");
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }
    public void onReq(BaseReq arg0) {
        Log.i("wechat", "BaseReq:" + arg0.getType());
        switch (arg0.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                Log.i("wechat", "ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX");
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                Log.i("wechat", "ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX");
                break;
            default:
                break;
        }
        finish();
    }
    public void onResp(BaseResp arg0) {
        Log.i("wechat", "BaseResp:" + arg0.errCode);
        String result = "";
        switch (arg0.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "分享成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "取消分享";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "认证失败";
                break;
            default:
                result = "errcode_unknown";
                break;
        }
        Toast.makeText(this,"wechat resp result="+result,Toast.LENGTH_SHORT).show();
        finish();
    }
}
