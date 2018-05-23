/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.example.windowheadtoast.RomUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

/**
 * Description:
 *
 * @author zhaozp
 * @since 2016-10-17
 */

public class FloatWindowManager {
    private static final String TAG = "FloatWindowManager";

    private static volatile FloatWindowManager instance;

    public static FloatWindowManager getInstance() {
        if (instance == null) {
            synchronized (FloatWindowManager.class) {
                if (instance == null) {
                    instance = new FloatWindowManager();
                }
            }
        }
        return instance;
    }

    public void applyOrShowFloatWindow(Context context) {
        if (!checkPermission(context)) {
            showConfirmDialog(context);
        }
    }

    private boolean checkPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //6.0 版本之后由于 google 增加了对悬浮窗权限的管理，所以方式就统一了
            return Settings.canDrawOverlays(context);
        } else if (RomUtils.checkIsMiuiRom()) {
            return MiuiUtils.checkFloatWindowPermission(context);
        } else if (RomUtils.checkIsMeizuRom()) {
            return MeizuUtils.checkFloatWindowPermission(context);
        } else if (RomUtils.checkIsHuaweiRom()) {
            return HuaweiUtils.checkFloatWindowPermission(context);
        } else if (RomUtils.checkIs360Rom()) {
            return QikuUtils.checkFloatWindowPermission(context);
        } else {
            return true;
        }
    }

    private void showConfirmDialog(final Context context) {
        new AlertDialog.Builder(context).setCancelable(true).setTitle("")
                .setMessage("您的手机没有授予悬浮窗权限，请开启后再试")
                .setPositiveButton("现在去开启",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                confirmResult(context, true);
                                dialog.dismiss();
                            }
                        }).setNegativeButton("暂不开启",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmResult(context, false);
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    private void confirmResult(Context context, boolean bool) {
        if (!bool) {
            //do something
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        } else if (RomUtils.checkIsMiuiRom()) {
            MiuiUtils.applyMiuiPermission(context);
        } else if (RomUtils.checkIsMeizuRom()) {
            MeizuUtils.applyPermission(context);
        } else if (RomUtils.checkIsHuaweiRom()) {
            HuaweiUtils.applyPermission(context);
        } else if (RomUtils.checkIs360Rom()) {
            QikuUtils.applyPermission(context);
        }
    }

}
