package com.youdu.module.recommand;

import com.youdu.module.BaseModel;
import com.youdu.module.monitor.Monitor;
import com.youdu.module.monitor.emevent.EMEvent;

import java.util.ArrayList;

/**
 * *******************************************************
 *
 * @文件名称：RecommandValue.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年7月9日 上午9:24:04
 * @文件描述：搜索实体
 * @修改历史：2015年7月9日创建初始版本 ********************************************************
 */
public class RecommandBodyValue extends BaseModel {

    public int type;
    public String logo;
    public String title;
    public String info;
    public String price;
    public String text;
    public String site;
    public String from;
    public String zan;
    public ArrayList<String> url;

    //视频专用
    public String thumb;
    public String resource;
    public String resourceID;
    public String adid;
    public ArrayList<Monitor> startMonitor;
    public ArrayList<Monitor> middleMonitor;
    public ArrayList<Monitor> endMonitor;
    public String clickUrl;
    public ArrayList<Monitor> clickMonitor;
    public EMEvent event;

}
