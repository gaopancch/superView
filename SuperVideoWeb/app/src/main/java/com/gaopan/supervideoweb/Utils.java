package com.gaopan.supervideoweb;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by gaopan on 2017/6/27.
 */

public class Utils {

    public static String BAIDU_APPID="9818665";
    public static String GT_APPID="mWifWhJV3U84tFpFp5Elf3";

    //weixin  242978649c31f48711143325e8cb285d
    public static String WECHAT_APPID="wx9997793cd0f4ab62";

    public static String getAPPVersionCode(Context ctx) {
        int currentVersionCode = 0;
        String appVersionName="";
        PackageManager manager = ctx.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            appVersionName = info.versionName; // 版本名
            currentVersionCode = info.versionCode; // 版本号
            System.out.println(currentVersionCode + " " + appVersionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersionName;
    }
}
