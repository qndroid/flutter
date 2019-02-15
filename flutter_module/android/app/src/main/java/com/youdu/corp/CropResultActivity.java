package com.youdu.corp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import com.youdu.R;
import java.io.File;

public class CropResultActivity extends Activity {

  CropView resultView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_crop_result);
    resultView = (CropView) findViewById(R.id.corp_view);

    final String fileName = "user_avatar.png";
    resultView.extensions().load(new File(getCacheDir(), fileName));
  }
}
