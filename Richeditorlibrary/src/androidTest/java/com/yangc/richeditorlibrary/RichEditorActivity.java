package com.yangc.richeditorlibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.yangc.richeditorlibrary.view.editor.SEditorData;
import com.yangc.richeditorlibrary.view.editor.SortRichEditor;
import java.util.ArrayList;


/**
 * Created by yangc on 2017/4/4.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated: 编辑介绍
 */
public class RichEditorActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_PICK_IMAGE = 1023;
    public static final int REQUEST_CODE_CAPTURE_CAMEIA = 1022;
    private SortRichEditor editor;


    public static void startActivity(Activity activity, ArrayList<SEditorData> list) {
        Intent intent = new Intent(activity, RichEditorActivity.class);
        intent.putParcelableArrayListExtra("list", list);
        activity.startActivityForResult(intent, 10);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.menu_sort, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (editor.sort()) {
            //     item.setTitle(R.string.finish);
        } else {
            //    item.setTitle(R.string.sort);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //    setContentView(R.layout.rich_editor_activity);
        //   editor = (SortRichEditor) findViewById(R.id.richEditor);
        //    AppCompatImageView ivGallery = (AppCompatImageView) findViewById(R.id.iv_gallery);
        //   AppCompatImageView ivCamera = (AppCompatImageView) findViewById(R.id.iv_camera);
        //   Button btnPosts = (Button) findViewById(R.id.btn_posts);
        //   ivGallery.setOnClickListener(this);
     //   ivCamera.setOnClickListener(this);
     //   btnPosts.setOnClickListener(this);

        intiView();
    }

    private void intiView() {
        ArrayList<SEditorData> list = getIntent().getParcelableArrayListExtra("list");
        if (list != null) {
            for (SEditorData sEditorData : list) {
             //   sEditorData.setImagerPath(OSSService.getInstance().getImageUrl(sEditorData.getBriefText()));
            }
            editor.addDataList(list);
        }
//        mOnHanlderResultCallback = new GalleryFinal.OnMediaResultCallback() {
//            @Override
//            public void onHandlerSuccess(int requestCode, List<PhotoInfo> resultList) {
//                for (PhotoInfo info : resultList) {
//                    editor.addImage(info.getPhotoPath());
//                }
//
//            }
//
//            @Override
//            public void onHandlerFailure(int requestCode, String errorMsg) {
//
//            }
//        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
       //     case R.id.iv_gallery:
                //     GalleryFinal.openMultiSelect(REQUEST_CODE_PICK_IMAGE, 6, mOnHanlderResultCallback, false);
      //          break;
     //       case R.id.iv_camera:
                //     GalleryFinal.openCamera(REQUEST_CODE_CAPTURE_CAMEIA, mOnHanlderResultCallback);
       //         break;
     //       case R.id.btn_posts:
//                if (editor.buildEditData() == null || editor.buildEditData().isEmpty()) {
//                    //         YUtils.Toast(getString(R.string.Please_add_content));
//                    return;
//                }
//                ArrayList<SEditorData> editList = new ArrayList<>(editor.buildEditData());
//                // 下面的代码可以上传、或者保存，请自行实现
//                Intent intent = new Intent();
//                intent.putParcelableArrayListExtra("data", editList);
//                setResult(RESULT_OK, intent);
//                finish();
   //             break;
        }
    }

    @Override
    public void onBackPressed() {
        editor.clear();
        super.onBackPressed();
    }
}
