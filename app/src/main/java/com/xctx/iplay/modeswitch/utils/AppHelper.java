package com.xctx.iplay.modeswitch.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

/**
 * Created by iplay on 2016/11/30.
 */

public class AppHelper {

    /**
     * 检查系统应用程序，并打开
     */
    public void openApp(Context context) {

        PackageManager mPackageManager;
        List<ResolveInfo> mAllApps;

        boolean flags = false;

        //应用过滤条件
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        mPackageManager = context.getPackageManager();
        mAllApps = mPackageManager.queryIntentActivities(mainIntent, 0);
        //按包名排序
        Collections.sort(mAllApps, new ResolveInfo.DisplayNameComparator(mPackageManager));

        for (ResolveInfo res : mAllApps) {
            //该应用的包名和主Activity
            String pkg = res.activityInfo.packageName;
            String cls = res.activityInfo.name;

            if (pkg.contains("com.xctx.iplay.gamemapapp")) {
                ComponentName componet = new ComponentName(pkg, cls);

                Intent intent = new Intent();
                intent.setComponent(componet);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

                flags = true;
            }
        }

        if (flags == false) {
            Toast.makeText(context, "您未能成功打开gamemapapp,请下载或手动进入gamemapapp进行业务操作", Toast.LENGTH_SHORT).show();
        }
    }

}
