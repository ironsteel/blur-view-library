
package com.ironsteel.blur_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class FrameLayoutWithBluredBackground extends FrameLayout {

    private RenderScript renderScript;
    private ScriptIntrinsicBlur blurIntrinsic;

    private Bitmap originalBackground;
    private Bitmap blurredBackground;

    private boolean parentDrawn = false;
    private Canvas blurCanvas;
    private Allocation in;
    private Allocation out;
    private boolean blurFinished;

    public FrameLayoutWithBluredBackground(Context context) {
        super(context);
        setUpBlurIntrinsic(context);
    }

    public FrameLayoutWithBluredBackground(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setUpBlurIntrinsic(context);
    }

    public FrameLayoutWithBluredBackground(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private void setUpBlurIntrinsic(Context context) {
        renderScript = RenderScript.create(context);
        blurIntrinsic = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        createBitmaps();
    }

    private void createBitmaps() {
        originalBackground = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        blurredBackground = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        blurCanvas = new Canvas(originalBackground);
        in = Allocation.createFromBitmap(renderScript, originalBackground);
        out = Allocation.createFromBitmap(renderScript,
                blurredBackground);
    }

    private void blur() {
        blurIntrinsic.setRadius(10.f);
        blurIntrinsic.setInput(in);
        blurIntrinsic.forEach(out);
        out.copyTo(blurredBackground);
        blurFinished = true;
    }

    @Override
    public void draw(Canvas canvas) {
        if (blurFinished) {
            canvas.drawBitmap(blurredBackground, 0, 0, null);
            super.draw(canvas);
            return;
        }
        View v = (View) getParent();

        // Avoid recursive explosion
        if (parentDrawn) {
            return;
        }

        // This will trigger a draw again so bump the flag
        // to avoid recursive explosion
        parentDrawn = true;
        drawParentInBitmap(v);

        blur();
        canvas.drawBitmap(blurredBackground, 0, 0, null);

        super.draw(canvas);
        parentDrawn = false;
    }

    private void drawParentInBitmap(View v) {
        blurCanvas.save();
        blurCanvas.translate(-getLeft(), -getTop());
        v.draw(blurCanvas);
        blurCanvas.restore();
    }

}
