package com.loginmodule;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

/**
 * Created by yangc on 2017/4/5.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated: 默认加载页
 */
public abstract class LoadActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION_OTHER = 101;
    private static final int REQUEST_CODE_SETTING = 300;

    //    public String[] pre = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        cameraTask();

    }

    /****
     * 已经获取权限可以开始业务
     ***/
    protected abstract void startApp();

    /***
     * 重写该方法赋予想要权限
     ***/
    protected abstract String[] permission();

    /****
     * 权限适配
     ***/
    public void cameraTask() {
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermission();
        } else {
            //实现业务处理
            startApp();
        }
    }

    @PermissionYes(REQUEST_CODE_PERMISSION_OTHER)
    private void getMultiYes(List<String> grantedPermissions) {

        //已经获取权限可以业务
        startApp();
    }

    @PermissionNo(REQUEST_CODE_PERMISSION_OTHER)
    private void getMultiNo(List<String> deniedPermissions) {
        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING)
                    .setTitle(R.string.title_dialog)
                    .setMessage(R.string.rationale_ask_again)
                    .setPositiveButton(R.string.title_settings_dialog)
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
            // 更多自定dialog，请看上面。
        } else {
            // 用户否不勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            requestPermission();
        }

    }
//----------------------------------权限回调处理----------------------------------//

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /**
         * 转给AndPermission分析结果。
         *
         * @param object     要接受结果的Activity、Fragment。
         * @param requestCode  请求码。
         * @param permissions  权限数组，一个或者多个。
         * @param grantResults 请求结果。
         */
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode2, int resultCode, Intent data) {
        switch (requestCode2) {
            case REQUEST_CODE_SETTING: {
                requestPermission();
                break;
            }
        }
    }

    /**
     * 请求权限
     ***/
    private void requestPermission() {
        AndPermission.with(this)
                .requestCode(REQUEST_CODE_PERMISSION_OTHER)
                .permission(permission())
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        AndPermission.rationaleDialog(LoadActivity.this, rationale).show();
                    }
                })
                .send();
    }

}
