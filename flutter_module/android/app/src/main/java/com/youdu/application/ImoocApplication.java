package com.youdu.application;

import android.app.Application;

import com.youdu.core.AdSDKManager;
import com.youdu.share.ShareManager;

import cn.jpush.android.api.JPushInterface;
import io.flutter.view.FlutterMain;

/**
 * *******************************************************
 *
 * @文件名称：CommonApplication.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年11月19日 下午10:38:25
 * @文件描述：App容器
 * @修改历史：2015年11月19日创建初始版本 ********************************************************
 */
public class ImoocApplication extends Application {

  private static ImoocApplication mApplication = null;

  @Override public void onCreate() {
    super.onCreate();
    mApplication = this;
    initShareSDK();
    initJPush();
    initAdSDK();

    FlutterMain.startInitialization(this);
  }

  public static ImoocApplication getInstance() {
    return mApplication;
  }

  public void initShareSDK() {
    ShareManager.initSDK(this);
  }

  private void initJPush() {
    JPushInterface.setDebugMode(true);
    JPushInterface.init(this);
  }

  private void initAdSDK() {
    AdSDKManager.init(this);
  }
}