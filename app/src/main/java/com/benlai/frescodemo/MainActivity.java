package com.benlai.frescodemo;

import android.app.Activity;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/3/24.
 */
public class MainActivity extends Activity {
    @Bind(R.id.my_image_view)
    SimpleDraweeView myImageView;
    @Bind(R.id.zhanwei_image_view)
    SimpleDraweeView zhanweiImageView;
    @Bind(R.id.reload_image_view)
    SimpleDraweeView reloadImageView;
    @Bind(R.id.failure_image_view)
    SimpleDraweeView failureImageView;
    @Bind(R.id.progress_image_view)
    SimpleDraweeView progressImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.bind(this);

        Uri uri = Uri.parse("https://raw.githubusercontent.com/facebook/fresco/gh-pages/static/fresco-logo.png");
        myImageView.setImageURI(uri);

        Uri uri1 = Uri.parse("http://tupian.enterdesk.com/2014/lxy/2014/05/24/2/11.jpg");
        Uri uri2 = Uri.parse("http://images.ali213.net/picfile/pic/2010-01-04/1645325308.jpg");
        Uri uri3 = Uri.parse("https://raw.githubusercontent.com/fresco/gh-pages/static/fresco-logo.png");

        zhanweiImageView.setImageURI(uri1);
//        reloadImageView.setImageURI(uri2);
        failureImageView.setImageURI(uri3);

        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(
                    String id,
                    @Nullable ImageInfo imageInfo,
                    @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }
                QualityInfo qualityInfo = imageInfo.getQualityInfo();
                FLog.d("Final image received! " +
                                "Size %d x %d",
                        "Quality level %d, good enough: %s, full quality: %s",
                        imageInfo.getWidth(),
                        imageInfo.getHeight(),
                        qualityInfo.getQuality(),
                        qualityInfo.isOfGoodEnoughQuality(),
                        qualityInfo.isOfFullQuality());
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                FLog.d("reload", "Intermediate image received");
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                FLog.e(getClass(), throwable, "Error loading %s", id);
            }
        };
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri3)
                .setTapToRetryEnabled(true)
                .setOldController(reloadImageView.getController())
                .setControllerListener(controllerListener)
                .build();
        reloadImageView.setController(controller);

        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(300).setProgressBarImage(new ProgressBarDrawable())
                .build();
        progressImageView.setHierarchy(hierarchy);

        progressImageView.setImageURI(uri2);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
