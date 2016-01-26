package it.ennova.rxadvertise;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ViewFlipper;

public class ViewFlipperUtils {

    public static void initAnimationOn(@NonNull ViewFlipper viewFlipper, @NonNull Context context) {
        viewFlipper.setInAnimation(context, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(context, android.R.anim.slide_out_right);
    }
}
