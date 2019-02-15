package com.youdu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;

import com.youdu.R;
import com.youdu.activity.base.BaseActivity;
import com.youdu.core.AdSDKManager;
import com.youdu.core.display.DisplayAdContext;
import com.youdu.db.SPManager;


public class LoadingActivity extends BaseActivity {

    private RelativeLayout mAdLayout;
    private RelativeLayout mCopyLayout;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (SPManager.getInstance().
                    getBoolean(SPManager.IS_SHOW_GUIDE, false)) {
                startActivity(new Intent(LoadingActivity.this,
                        HomeActivity.class));
            } else {
                SPManager.getInstance().putBoolean(SPManager.IS_SHOW_GUIDE, true);
                startActivity(new Intent(LoadingActivity.this,
                        GuideActivity.class));
            }
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hiddenStatusBar();
        setContentView(R.layout.activity_loading_layout);
        initView();
        mHandler.sendEmptyMessageDelayed(0, 3000);
    }

    private void initView() {

        mAdLayout = (RelativeLayout) findViewById(R.id.ad_content_view);
        mCopyLayout = (RelativeLayout) findViewById(R.id.content_layout);

        /**
         * 创建广告位
         */
        AdSDKManager.createDisplayAd(mAdLayout, new DisplayAdContext.DisplayAdAppListener() {
            @Override
            public void onLoadingComplete() {
                mCopyLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed() {
                mCopyLayout.setVisibility(View.GONE);
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
