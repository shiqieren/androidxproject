package com.liyiwei.baselibrary.parallax.transform;

import android.graphics.Canvas;
import android.view.View;

import com.liyiwei.baselibrary.parallax.widget.ParallaxBackLayout;


import  com.liyiwei.baselibrary.parallax.ViewDragHelper;
/**
 * ParallaxBackLayout
 *
 * @author An Zewei (anzewei88[at]gmail[dot]com)
 * @since ${VERSION}
 */

public class CoverTransform implements ITransform {
    @Override
    public void transform(Canvas canvas, ParallaxBackLayout parallaxBackLayout, View child) {
        int edge = parallaxBackLayout.getEdgeFlag();
        if (edge == ViewDragHelper.EDGE_LEFT) {
            canvas.clipRect(0, 0, child.getLeft(), child.getBottom());
        } else if (edge == ViewDragHelper.EDGE_TOP) {
            canvas.clipRect(0, 0, child.getRight(), child.getTop() + parallaxBackLayout.getSystemTop());
        } else if (edge == ViewDragHelper.EDGE_RIGHT) {
            canvas.clipRect(child.getRight(), 0, parallaxBackLayout.getWidth(), child.getBottom());
        } else if (edge == ViewDragHelper.EDGE_BOTTOM) {
            canvas.clipRect(0, child.getBottom(), child.getRight(), parallaxBackLayout.getHeight());
        }
    }
}
