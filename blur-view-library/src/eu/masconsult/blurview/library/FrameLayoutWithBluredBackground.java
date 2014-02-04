
package eu.masconsult.blurview.library;

import android.content.Context;
import android.content.res.TypedArray;
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

    private static final int DEFAULT_BLUR_RADIUS = 15;
    private static final int MAX_BLUR_RADIUS = 25;
    private RenderScript renderScript;
    private ScriptIntrinsicBlur blurIntrinsic;

    private Bitmap originalBackground;
    private Bitmap blurredBackground;

    private boolean parentDrawn = false;
    private Canvas blurCanvas;
    private Allocation in;
    private Allocation out;

    private float blurRadius = 0;

    public FrameLayoutWithBluredBackground(Context context) {
        super(context);
        setUpBlurIntrinsic(context);
    }

    public FrameLayoutWithBluredBackground(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setUpStylableAttributes(attrs);
        setUpBlurIntrinsic(context);
    }

    public FrameLayoutWithBluredBackground(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        setUpStylableAttributes(attrs);
    }

    private void setUpStylableAttributes(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.FrameLayoutWithBluredBackground,
                0, 0);

        try {
            blurRadius = a.getFloat(R.styleable.FrameLayoutWithBluredBackground_blurRadius,
                    DEFAULT_BLUR_RADIUS);
            if (blurRadius > MAX_BLUR_RADIUS) {
                throw new RuntimeException("Invalid blur radius must be 0 < blurRadius < 25");
            }
        } finally {
            a.recycle();
        }

        setWillNotDraw(false);
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
        in = Allocation.createFromBitmap(renderScript, originalBackground);
        out = Allocation.createFromBitmap(renderScript, blurredBackground);
        blurCanvas = new Canvas(originalBackground);

    }

    private void blur() {
        blurIntrinsic.setRadius(blurRadius);
        blurIntrinsic.setInput(in);
        blurIntrinsic.forEach(out);
        out.copyTo(blurredBackground);
    }

    @Override
    public void draw(Canvas canvas) {
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

    public void setBlurRadius(float blurRadius) {
        this.blurRadius = blurRadius;
        invalidate();
    }

    public float getBlurRadius() {
        return blurRadius;
    }

}
