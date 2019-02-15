package com.youdu.view.fragment.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youdu.R;
import com.youdu.activity.LoginActivity;
import com.youdu.activity.SettingActivity;
import com.youdu.constant.Constant;
import com.youdu.manager.UserManager;
import com.youdu.module.update.UpdateModel;
import com.youdu.network.http.RequestCenter;
import com.youdu.okhttp.listener.DisposeDataListener;
import com.youdu.service.update.UpdateService;
import com.youdu.share.ShareDialog;
import com.youdu.adutil.ImageLoaderUtil;
import com.youdu.util.Util;
import com.youdu.view.CommonDialog;
import com.youdu.view.MyQrCodeDialog;
import com.youdu.view.fragment.BaseFragment;

import cn.sharesdk.framework.Platform;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author: vision
 * @function: 个人信息fragment
 * @date: 16/7/14
 */
public class MineFragment extends BaseFragment implements OnClickListener {

  /**
   * UI
   */
  private View mContentView;
  private RelativeLayout mLoginLayout;
  private CircleImageView mPhotoView;
  private TextView mLoginInfoView;
  private TextView mLoginView;
  private RelativeLayout mLoginedLayout;
  private TextView mUserNameView;
  private TextView mTickView;
  private TextView mVideoPlayerView;
  private TextView mShareView;
  private TextView mQrCodeView;
  private TextView mUpdateView;

  //自定义了一个广播接收器
  private LoginBroadcastReceiver receiver = new LoginBroadcastReceiver();

  public MineFragment() {
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mContext = getActivity();
    registerBroadcast();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mContentView = inflater.inflate(R.layout.fragment_mine_layout, null, false);
    initView();
    return mContentView;
  }

  private static final int TOTAL_COUNT = 6;
  private static final int TIME_SPAN = 1000;

  //连续点击六次则出现mapping
  private int count = 0;
  private long lastTime;
  private long now;

  /**
   * 处理mapping事件
   */
  private void handleMappingClick() {
    now = System.currentTimeMillis();
    //第一次点击
    if (count == 0) {
      lastTime = now;
    }

    if (now - lastTime < TIME_SPAN) {
      //间隔小间小于一秒，
      count++;
      lastTime = now;
    } else {
      //间隔小间大于一秒，清0，重计
      count = 0;
    }
    Log.e("renzhiqiang", "current count: " + count);
    //超过指定次数，出现mapping window
    if (count >= TOTAL_COUNT) {
      Log.e("renzhiqiang", "click enough");
      count = 0;
    }
  }

  private void initView() {
    mLoginLayout = (RelativeLayout) mContentView.findViewById(R.id.login_layout);
    mLoginLayout.setOnClickListener(this);
    mLoginedLayout = (RelativeLayout) mContentView.findViewById(R.id.logined_layout);
    mLoginedLayout.setOnClickListener(this);

    mPhotoView = (CircleImageView) mContentView.findViewById(R.id.photo_view);
    mPhotoView.setOnClickListener(this);
    mLoginView = (TextView) mContentView.findViewById(R.id.login_view);
    mLoginView.setOnClickListener(this);
    mVideoPlayerView = (TextView) mContentView.findViewById(R.id.video_setting_view);
    mVideoPlayerView.setOnClickListener(this);
    mShareView = (TextView) mContentView.findViewById(R.id.share_imooc_view);
    mShareView.setOnClickListener(this);
    mQrCodeView = (TextView) mContentView.findViewById(R.id.my_qrcode_view);
    mQrCodeView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        handleMappingClick();
      }
    });
    mLoginInfoView = (TextView) mContentView.findViewById(R.id.login_info_view);
    mUserNameView = (TextView) mContentView.findViewById(R.id.username_view);
    mTickView = (TextView) mContentView.findViewById(R.id.tick_view);

    mUpdateView = (TextView) mContentView.findViewById(R.id.update_view);
    mUpdateView.setOnClickListener(this);
  }

  @Override public void onResume() {
    super.onResume();
    //根据用户信息更新我们的fragment
    if (UserManager.getInstance().hasLogined()) {
      if (mLoginedLayout.getVisibility() == View.GONE) {
        mLoginLayout.setVisibility(View.GONE);
        mLoginedLayout.setVisibility(View.VISIBLE);
        mUserNameView.setText(UserManager.getInstance().getUser().data.name);
        mTickView.setText(UserManager.getInstance().getUser().data.tick);
      }
    }
  }

  @Override public void onDestroy() {
    super.onDestroy();
    unregisterBroadcast();
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.share_imooc_view:
        //分享Imooc课网址
        shareFriend();
        break;
      case R.id.login_layout:
      case R.id.login_view:
        //未登陆，则跳轉到登陸页面
        if (!UserManager.getInstance().hasLogined()) {
          toLogin();
        }
        break;
      case R.id.my_qrcode_view:
        if (!UserManager.getInstance().hasLogined()) {
          //未登陆，去登陆。
          toLogin();
        } else {
          //已登陆根据用户ID生成二维码显示
          MyQrCodeDialog dialog = new MyQrCodeDialog(mContext);
          dialog.show();
        }
        break;
      case R.id.video_setting_view:
        mContext.startActivity(new Intent(mContext, SettingActivity.class));
        break;
      case R.id.update_view:
        if (hasPermission(Constant.WRITE_READ_EXTERNAL_PERMISSION)) {
          checkVersion();
        } else {
          requestPermission(Constant.WRITE_READ_EXTERNAL_CODE,
              Constant.WRITE_READ_EXTERNAL_PERMISSION);
        }
        break;
    }
  }

  @Override public void doWriteSDCard() {
    checkVersion();
  }

  /**
   * 去登陆页面
   */
  private void toLogin() {
    Intent intent = new Intent(mContext, LoginActivity.class);
    mContext.startActivity(intent);
  }

  /**
   * 分享慕课网给好友
   */
  private void shareFriend() {
    ShareDialog dialog = new ShareDialog(mContext, false);
    dialog.setShareType(Platform.SHARE_IMAGE);
    dialog.setShareTitle("慕课网");
    dialog.setShareTitleUrl("http://www.imooc.com");
    dialog.setShareText("慕课网");
    dialog.setShareSite("imooc");
    dialog.setShareSiteUrl("http://www.imooc.com");
    dialog.setImagePhoto(Environment.getExternalStorageDirectory() + "/test2.jpg");
    dialog.show();
  }

  private void registerBroadcast() {

    IntentFilter filter = new IntentFilter(LoginActivity.LOGIN_ACTION);
    LocalBroadcastManager.getInstance(mContext).registerReceiver(receiver, filter);
  }

  private void unregisterBroadcast() {
    LocalBroadcastManager.getInstance(mContext).unregisterReceiver(receiver);
  }

  //发送版本检查更新请求
  private void checkVersion() {
    RequestCenter.checkVersion(new DisposeDataListener() {
      @Override public void onSuccess(Object responseObj) {
        final UpdateModel updateModel = (UpdateModel) responseObj;
        if (Util.getVersionCode(mContext) < updateModel.data.currentVersion) {
          //说明有新版本,开始下载
          CommonDialog dialog = new CommonDialog(mContext, getString(R.string.update_new_version),
              getString(R.string.update_title), getString(R.string.update_install),
              getString(R.string.cancel), new CommonDialog.DialogClickListener() {
            @Override public void onDialogClick() {
              Intent intent = new Intent(mContext, UpdateService.class);
              mContext.startService(intent);
            }
          });
          dialog.show();
        } else {
          //弹出一个toast提示当前已经是最新版本等处理
        }
      }

      @Override public void onFailure(Object reasonObj) {

      }
    });
  }

  /**
   * 接收mina发送来的消息，并更新UI
   */
  private class LoginBroadcastReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {
      if (UserManager.getInstance().hasLogined()) {
        //更新我们的fragment
        if (mLoginedLayout.getVisibility() == View.GONE) {
          mLoginLayout.setVisibility(View.GONE);
          mLoginedLayout.setVisibility(View.VISIBLE);
          mUserNameView.setText(UserManager.getInstance().getUser().data.name);
          mTickView.setText(UserManager.getInstance().getUser().data.tick);
          ImageLoaderUtil.getInstance(mContext)
              .displayImage(mPhotoView, UserManager.getInstance().getUser().data.photoUrl);
        }
      }
    }
  }
}
