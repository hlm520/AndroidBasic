package com.hlm.basic.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

public class MyPermissionActivity extends MyActionBarActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int SETTING_REQUEST_CODE = 2;
    private static String[] mNeverRequest;
    private static Runnable mRunnable = null;

    /**
     * request any number of permissions disposable
     *
     * @param permissions what permissions want to be requested
     */
    protected void request(String... permissions) {
        synchronized (this) {
            mNeverRequest = new String[permissions.length];
            int i = 0;
            for (String permission : permissions) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    if (!isRequested(permission)) {
                        mNeverRequest[i] = permission;
                        i++;
                    }
                }
            }
            if (mNeverRequest.length > 0) {
                ActivityCompat.requestPermissions(this, mNeverRequest, PERMISSION_REQUEST_CODE);
            }
        }
    }

    /**
     * if permission hasn't requested,to request and if success,invoke the runnable.run();
     * {@link Runnable#run()}
     *
     * @param runnable task to do after request success
     * @param permission which permission want to request
     */
    protected void request(Runnable runnable, String permission) {
        mRunnable = runnable;
        request(permission);
    }

    /**
     * to concert the permission is having permissiveness
     *
     * @param permission which permission want to request
     * @return true:has permissiveness,otherwise,hasn't permissiveness
     */
    protected boolean isRequested(String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    dialog(permissions[i]);
                    break;
                } else {
                    run();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTING_REQUEST_CODE) {
            for (String permission : mNeverRequest) {
                if (permission != null) {
                    if (isRequested(permission)) {
                        run();
                    } else {
                        dialog(permission);
                        break;
                    }
                }
            }
        }
    }

    /**
     * apply by user is wrong
     * whether or not to request in system setting intent by user
     *
     * @param permission the permission request failed
     */
    private void dialog(String permission) {
        AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
        normalDialog.setTitle("权限申请");
        normalDialog.setMessage("应用未授予" + Entity.getName(permission) + "权限,需要在设置界面手动授权！");
        normalDialog.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toSetting();
                    }
                });
        normalDialog.setNegativeButton("退出",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        // show
        normalDialog.show();
    }

    /**
     * open the system setting intent
     */
    private void toSetting() {
        Uri packageURI = Uri.parse("package:" + getPackageName());
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        startActivityForResult(intent, SETTING_REQUEST_CODE);
    }

    private void run() {
        if (mRunnable != null) {
            mRunnable.run();
            mRunnable = null;
        }
    }
}
