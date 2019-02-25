package com.emusafir.utility;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.emusafir.R;

public class CustomProgressDialog {
    Activity activity;
    Dialog dialog;

    //..we need the context else we can not create the dialog so get context in constructor
    public CustomProgressDialog(Activity activity,Drawable myDraw) {
        this.activity = activity;
        createDialog(myDraw);
    }

    private void createDialog(Drawable myDraw) {

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(false);
        //...that's the layout i told you will inflate later
        dialog.setContentView(R.layout.custom_dialog);

        //...initialize the imageView form infalted layout
        final ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);


        GlideApp.with(activity)
                .load(myDraw)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(new DrawableImageViewTarget(gifImageView));
    }

    //..also create a method which will hide the dialog when some work is search
    public void showDialog() {
        if (!dialog.isShowing())
            dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }

}
