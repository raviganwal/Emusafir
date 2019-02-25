package com.emusafir.utility;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.emusafir.R;


public class MyProgressDialog extends Dialog {
    Context mContext;
    int myDraw;

    public MyProgressDialog(Context context, int myDraw) {
        super(context);
        this.mContext = context;
        this.myDraw = myDraw;

    }

    @Override
    public void show() {
        super.show();
        setContentView(R.layout.custom_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //...initialize the imageView form infalted layout
        final ImageView gifImageView = findViewById(R.id.custom_loading_imageView);


        GlideApp.with(mContext)
                .load(myDraw)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(new DrawableImageViewTarget(gifImageView));
    }
}