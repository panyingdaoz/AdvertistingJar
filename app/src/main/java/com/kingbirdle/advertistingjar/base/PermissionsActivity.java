package com.kingbirdle.advertistingjar.base;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kingbirdle.advertistingjar.utils.Const;
import com.kingbirdle.advertistingjar.utils.PermissionsUtils;
import com.kingbirdle.advertistingjar.utils.Plog;
import com.kingbirdle.advertistingjar.utils.SpUtil;
import com.kingbirdle.advertistingjar.view.AdvertistingView;

import static android.os.Build.VERSION_CODES.M;

/**
 * @ClassName: PermissionsActivity
 * @Description: java类作用描述
 * @Author: Pan
 * @CreateDate: 2019/11/25 10:51
 */
public class PermissionsActivity extends AppCompatActivity {

    private Context context;
    String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        if (Build.VERSION.SDK_INT >= M) {
            Plog.e("开始申请权限");
            PermissionsUtils.getInstance().chekPermissions(this, permissions, permissionsResult);
        } else {
            Plog.e("不需要申请");
            SpUtil.writeBoolean(Const.PERMISSONS, true);
            Base.intentActivity("5");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsUtils.getInstance().onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * 创建监听权限的接口对象
     */
    PermissionsUtils.IPermissionsResult permissionsResult = new PermissionsUtils.IPermissionsResult() {
        @Override
        public void passPermissons() {
            Plog.e("权限通过");
            SpUtil.writeBoolean(Const.PERMISSONS, true);
            AdvertistingView advertistingView = new AdvertistingView(context);
            advertistingView.startAdvertisting();
            PackageManager packageManager = getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(SpUtil.readString(Const.MAIN_APP_NAME));
            if (intent != null) {
                startActivity(intent);
            }
        }

        @Override
        public void forbitPermissons() {
            PermissionsUtils.getInstance().chekPermissions(PermissionsActivity.this, permissions, permissionsResult);
        }
    };
}
