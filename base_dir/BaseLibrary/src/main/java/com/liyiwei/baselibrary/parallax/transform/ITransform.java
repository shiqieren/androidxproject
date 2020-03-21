package com.liyiwei.baselibrary.parallax.transform;

import android.graphics.Canvas;
import android.view.View;

import com.liyiwei.baselibrary.parallax.widget.ParallaxBackLayout;


/**
 * ParallaxBackLayout
 *
 * @author An Zewei (anzewei88[at]gmail[dot]com)
 * @since ${VERSION}
 */

public interface ITransform {
    void transform(Canvas canvas, ParallaxBackLayout parallaxBackLayout, View child);
}
