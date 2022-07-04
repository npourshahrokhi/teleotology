package com.doctor.ashanet.ashanet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

public class ZoomableImageView extends ImageView {
    static final int CLICK = 3;
    static final int DRAG = 1;
    static final int NONE = 0;
    static final int ZOOM = 2;
    float bmHeight;
    float bmWidth;
    float bottom;
    Context context;
    float height;
    PointF last = new PointF();
    float[] m;
    ScaleGestureDetector mScaleDetector;
    Matrix matrix = new Matrix();
    float maxScale = 4.0f;
    float minScale = 1.0f;
    int mode = 0;
    float origHeight;
    float origWidth;
    float redundantXSpace;
    float redundantYSpace;
    float right;
    float saveScale = 1.0f;
    PointF start = new PointF();
    float width;

    public ZoomableImageView(Context context2, AttributeSet attr) {
        super(context2, attr);
        super.setClickable(true);
        this.context = context2;
        this.mScaleDetector = new ScaleGestureDetector(context2, new ScaleListener());
        this.matrix.setTranslate(1.0f, 1.0f);
        this.m = new float[9];
        setImageMatrix(this.matrix);
        setScaleType(ImageView.ScaleType.MATRIX);
        setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                ZoomableImageView.this.mScaleDetector.onTouchEvent(event);
                ZoomableImageView.this.matrix.getValues(ZoomableImageView.this.m);
                float x = ZoomableImageView.this.m[2];
                float y = ZoomableImageView.this.m[5];
                PointF curr = new PointF(event.getX(), event.getY());
                switch (event.getAction()) {
                    case 0:
                        ZoomableImageView.this.last.set(event.getX(), event.getY());
                        ZoomableImageView.this.start.set(ZoomableImageView.this.last);
                        ZoomableImageView.this.mode = 1;
                        break;
                    case 1:
                        ZoomableImageView.this.mode = 0;
                        int xDiff = (int) Math.abs(curr.x - ZoomableImageView.this.start.x);
                        int yDiff = (int) Math.abs(curr.y - ZoomableImageView.this.start.y);
                        if (xDiff < 3 && yDiff < 3) {
                            ZoomableImageView.this.performClick();
                            break;
                        }
                    case 2:
                        if (ZoomableImageView.this.mode == 2 || (ZoomableImageView.this.mode == 1 && ZoomableImageView.this.saveScale > ZoomableImageView.this.minScale)) {
                            float deltaX = curr.x - ZoomableImageView.this.last.x;
                            float deltaY = curr.y - ZoomableImageView.this.last.y;
                            float scaleWidth = (float) Math.round(ZoomableImageView.this.origWidth * ZoomableImageView.this.saveScale);
                            float scaleHeight = (float) Math.round(ZoomableImageView.this.origHeight * ZoomableImageView.this.saveScale);
                            if (scaleWidth < ZoomableImageView.this.width) {
                                deltaX = 0.0f;
                                if (y + deltaY > 0.0f) {
                                    deltaY = -y;
                                } else if (y + deltaY < (-ZoomableImageView.this.bottom)) {
                                    deltaY = -(ZoomableImageView.this.bottom + y);
                                }
                            } else if (scaleHeight < ZoomableImageView.this.height) {
                                deltaY = 0.0f;
                                if (x + deltaX > 0.0f) {
                                    deltaX = -x;
                                } else if (x + deltaX < (-ZoomableImageView.this.right)) {
                                    deltaX = -(ZoomableImageView.this.right + x);
                                }
                            } else {
                                if (x + deltaX > 0.0f) {
                                    deltaX = -x;
                                } else if (x + deltaX < (-ZoomableImageView.this.right)) {
                                    deltaX = -(ZoomableImageView.this.right + x);
                                }
                                if (y + deltaY > 0.0f) {
                                    deltaY = -y;
                                } else if (y + deltaY < (-ZoomableImageView.this.bottom)) {
                                    deltaY = -(ZoomableImageView.this.bottom + y);
                                }
                            }
                            ZoomableImageView.this.matrix.postTranslate(deltaX, deltaY);
                            ZoomableImageView.this.last.set(curr.x, curr.y);
                            break;
                        }
                    case 5:
                        ZoomableImageView.this.last.set(event.getX(), event.getY());
                        ZoomableImageView.this.start.set(ZoomableImageView.this.last);
                        ZoomableImageView.this.mode = 2;
                        break;
                    case 6:
                        ZoomableImageView.this.mode = 0;
                        break;
                }
                ZoomableImageView.this.setImageMatrix(ZoomableImageView.this.matrix);
                ZoomableImageView.this.invalidate();
                return true;
            }
        });
    }

    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        this.bmWidth = (float) bm.getWidth();
        this.bmHeight = (float) bm.getHeight();
    }

    public void setMaxZoom(float x) {
        this.maxScale = x;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private ScaleListener() {
        }

        public boolean onScaleBegin(ScaleGestureDetector detector) {
            ZoomableImageView.this.mode = 2;
            return true;
        }

        public boolean onScale(ScaleGestureDetector detector) {
            float mScaleFactor = detector.getScaleFactor();
            float origScale = ZoomableImageView.this.saveScale;
            ZoomableImageView.this.saveScale *= mScaleFactor;
            if (ZoomableImageView.this.saveScale > ZoomableImageView.this.maxScale) {
                ZoomableImageView.this.saveScale = ZoomableImageView.this.maxScale;
                mScaleFactor = ZoomableImageView.this.maxScale / origScale;
            } else if (ZoomableImageView.this.saveScale < ZoomableImageView.this.minScale) {
                ZoomableImageView.this.saveScale = ZoomableImageView.this.minScale;
                mScaleFactor = ZoomableImageView.this.minScale / origScale;
            }
            ZoomableImageView.this.right = ((ZoomableImageView.this.width * ZoomableImageView.this.saveScale) - ZoomableImageView.this.width) - ((ZoomableImageView.this.redundantXSpace * 2.0f) * ZoomableImageView.this.saveScale);
            ZoomableImageView.this.bottom = ((ZoomableImageView.this.height * ZoomableImageView.this.saveScale) - ZoomableImageView.this.height) - ((ZoomableImageView.this.redundantYSpace * 2.0f) * ZoomableImageView.this.saveScale);
            if (ZoomableImageView.this.origWidth * ZoomableImageView.this.saveScale <= ZoomableImageView.this.width || ZoomableImageView.this.origHeight * ZoomableImageView.this.saveScale <= ZoomableImageView.this.height) {
                ZoomableImageView.this.matrix.postScale(mScaleFactor, mScaleFactor, ZoomableImageView.this.width / 2.0f, ZoomableImageView.this.height / 2.0f);
                if (mScaleFactor >= 1.0f) {
                    return true;
                }
                ZoomableImageView.this.matrix.getValues(ZoomableImageView.this.m);
                float x = ZoomableImageView.this.m[2];
                float y = ZoomableImageView.this.m[5];
                if (mScaleFactor >= 1.0f) {
                    return true;
                }
                if (((float) Math.round(ZoomableImageView.this.origWidth * ZoomableImageView.this.saveScale)) < ZoomableImageView.this.width) {
                    if (y < (-ZoomableImageView.this.bottom)) {
                        ZoomableImageView.this.matrix.postTranslate(0.0f, -(ZoomableImageView.this.bottom + y));
                        return true;
                    } else if (y <= 0.0f) {
                        return true;
                    } else {
                        ZoomableImageView.this.matrix.postTranslate(0.0f, -y);
                        return true;
                    }
                } else if (x < (-ZoomableImageView.this.right)) {
                    ZoomableImageView.this.matrix.postTranslate(-(ZoomableImageView.this.right + x), 0.0f);
                    return true;
                } else if (x <= 0.0f) {
                    return true;
                } else {
                    ZoomableImageView.this.matrix.postTranslate(-x, 0.0f);
                    return true;
                }
            } else {
                ZoomableImageView.this.matrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());
                ZoomableImageView.this.matrix.getValues(ZoomableImageView.this.m);
                float x2 = ZoomableImageView.this.m[2];
                float y2 = ZoomableImageView.this.m[5];
                if (mScaleFactor >= 1.0f) {
                    return true;
                }
                if (x2 < (-ZoomableImageView.this.right)) {
                    ZoomableImageView.this.matrix.postTranslate(-(ZoomableImageView.this.right + x2), 0.0f);
                } else if (x2 > 0.0f) {
                    ZoomableImageView.this.matrix.postTranslate(-x2, 0.0f);
                }
                if (y2 < (-ZoomableImageView.this.bottom)) {
                    ZoomableImageView.this.matrix.postTranslate(0.0f, -(ZoomableImageView.this.bottom + y2));
                    return true;
                } else if (y2 <= 0.0f) {
                    return true;
                } else {
                    ZoomableImageView.this.matrix.postTranslate(0.0f, -y2);
                    return true;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.width = (float) View.MeasureSpec.getSize(widthMeasureSpec);
        this.height = (float) View.MeasureSpec.getSize(heightMeasureSpec);
        float scale = Math.min(this.width / this.bmWidth, this.height / this.bmHeight);
        this.matrix.setScale(scale, scale);
        setImageMatrix(this.matrix);
        this.saveScale = 1.0f;
        this.redundantYSpace = this.height - (this.bmHeight * scale);
        this.redundantXSpace = this.width - (this.bmWidth * scale);
        this.redundantYSpace /= 2.0f;
        this.redundantXSpace /= 2.0f;
        this.matrix.postTranslate(this.redundantXSpace, this.redundantYSpace);
        this.origWidth = this.width - (this.redundantXSpace * 2.0f);
        this.origHeight = this.height - (this.redundantYSpace * 2.0f);
        this.right = ((this.width * this.saveScale) - this.width) - ((this.redundantXSpace * 2.0f) * this.saveScale);
        this.bottom = ((this.height * this.saveScale) - this.height) - ((this.redundantYSpace * 2.0f) * this.saveScale);
        setImageMatrix(this.matrix);
    }
}
