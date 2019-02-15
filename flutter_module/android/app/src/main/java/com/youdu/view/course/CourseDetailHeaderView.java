package com.youdu.view.course;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.youdu.R;
import com.youdu.activity.PhotoViewActivity;
import com.youdu.core.video.VideoAdContext;
import com.youdu.module.course.CourseHeaderValue;
import com.youdu.adutil.ImageLoaderUtil;
import com.youdu.adutil.Utils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by renzhiqiang on 16/9/8.
 */
public class CourseDetailHeaderView extends RelativeLayout {

    private Context mContext;
    /**
     * UI
     */
    private RelativeLayout mRootView;
    private CircleImageView mPhotoView;
    private TextView mNameView;
    private TextView mDayView;
    private TextView mOldValueView;
    private TextView mNewValueView;
    private TextView mIntroductView;
    private TextView mFromView;
    private TextView mZanView;
    private TextView mScanView;
    private LinearLayout mContentLayout;
    private RelativeLayout mVideoLayout;
    private TextView mHotCommentView;
    /**
     * data
     */
    private CourseHeaderValue mData;
    private ImageLoaderUtil mImageLoader;

    public CourseDetailHeaderView(Context context, CourseHeaderValue value) {
        this(context, null, value);
    }

    public CourseDetailHeaderView(Context context, AttributeSet attrs, CourseHeaderValue value) {
        super(context, attrs);
        mContext = context;
        mData = value;
        mImageLoader = ImageLoaderUtil.getInstance(mContext);
        initView();
    }

    private void initView() {
        mRootView = (RelativeLayout) LayoutInflater.from(mContext).
                inflate(R.layout.listview_course_comment_head_layout, this);
        mPhotoView = (CircleImageView) mRootView.findViewById(R.id.photo_view);
        mNameView = (TextView) mRootView.findViewById(R.id.name_view);
        mDayView = (TextView) mRootView.findViewById(R.id.day_view);
        mOldValueView = (TextView) mRootView.findViewById(R.id.old_value_view);
        mNewValueView = (TextView) mRootView.findViewById(R.id.new_value_view);
        mIntroductView = (TextView) mRootView.findViewById(R.id.introduct_view);
        mFromView = (TextView) mRootView.findViewById(R.id.from_view);
        mContentLayout = (LinearLayout) mRootView.findViewById(R.id.picture_layout);
        mContentLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PhotoViewActivity.class);
                intent.putStringArrayListExtra(PhotoViewActivity.PHOTO_LIST, mData.photoUrls);
                mContext.startActivity(intent);
            }
        });
        mVideoLayout = (RelativeLayout) mRootView.findViewById(R.id.video_view);
        mZanView = (TextView) mRootView.findViewById(R.id.zan_view);
        mScanView = (TextView) mRootView.findViewById(R.id.scan_view);
        mHotCommentView = (TextView) mRootView.findViewById(R.id.hot_comment_view);

        mImageLoader.displayImage(mPhotoView, mData.logo);
        mNameView.setText(mData.name);
        mDayView.setText(mData.dayTime);
        mOldValueView.setText(mData.oldPrice);
        mOldValueView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mNewValueView.setText(mData.newPrice);
        mIntroductView.setText(mData.text);
        mFromView.setText(mData.from);
        mZanView.setText(mData.zan);
        mScanView.setText(mData.scan);
        mHotCommentView.setText(mData.hotComment);
        for (String url : mData.photoUrls) {
            mContentLayout.addView(createItem(url));
        }
        if (!TextUtils.isEmpty(mData.video.resource)) {
            new VideoAdContext(mVideoLayout,
                    new Gson().toJson(mData.video), null);
        }
    }

    private ImageView createItem(String url) {
        ImageView imageView = new ImageView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.
                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dip2px(mContext, 150));
        params.topMargin = Utils.dip2px(mContext, 10);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        mImageLoader.displayImage(imageView, url);
        return imageView;
    }
}
