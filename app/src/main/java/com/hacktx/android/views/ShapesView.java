package com.hacktx.android.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.hacktx.android.R;

import java.util.ArrayList;
import java.util.Random;

public class ShapesView extends ImageView {

    private ArrayList<Shape> shapes = new ArrayList<>();
    private int width, height;

    public ShapesView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(isInEditMode()) {
            return;
        }

        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        Random r = new Random();
        int drawables[] = {R.drawable.circle, R.drawable.dot, R.drawable.line, R.drawable.square};

        for (int x = 0; x < 25; x++) {
            shapes.add(new Shape(drawables[r.nextInt(drawables.length)], r.nextInt(width), height + r.nextInt(height), r.nextInt(height)));
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);

        for (Shape s : shapes) {
            Drawable d = s.getDrawable();
            d.setBounds(s.getX(), s.getY(), 100 + s.getX(), 100 + s.getY());
            d.setLevel(s.getRotation());
            d.draw(canvas);
        }
    }

    class Shape implements ValueAnimator.AnimatorUpdateListener {

        private final int BASE_DURATION = 20000;
        private final int DISTANCE_ABOVE_STATUS_BAR = 100;
        private final int ROTATION_FACTOR = 3;

        private ValueAnimator mValueAnimator;
        private Drawable mDrawable;
        private int mX, mY, mRotation, mRotationOffset;

        public Shape(@DrawableRes int drawable, int x, int y, int rotation) {
            mDrawable = ContextCompat.getDrawable(getContext(), drawable);
            mX = x;
            mY = y;
            mRotationOffset = rotation;

            mValueAnimator = ValueAnimator.ofInt(mY, -DISTANCE_ABOVE_STATUS_BAR);
            mValueAnimator.setDuration(getDuration());
            mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
            mValueAnimator.setInterpolator(new LinearInterpolator());
            mValueAnimator.addUpdateListener(this);
            mValueAnimator.start();
        }

        public Drawable getDrawable() {
            return mDrawable;
        }

        public int getX() {
            return mX;
        }

        public int getY() {
            return mY;
        }

        public int getRotation() {
            return mRotation;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            mY = (int) valueAnimator.getAnimatedValue();
            mRotation = mY * ROTATION_FACTOR + mRotationOffset;
            invalidate();
        }

        private int getDuration() {
            return ((getY() + DISTANCE_ABOVE_STATUS_BAR) * BASE_DURATION) / height + DISTANCE_ABOVE_STATUS_BAR;
        }
    }
}
