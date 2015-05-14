package com.utcs.mad.umad.views;

/**
 * Created by tomasrodriguez on 2/3/15.
 */
import android.content.Context;
import android.util.AttributeSet;


public class ScrollViewWithListener extends StickyScrollView {

    private OnScrollViewListener mOnScrollViewListener;

    public ScrollViewWithListener(Context context) {
        super(context);
    }
    public ScrollViewWithListener(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ScrollViewWithListener(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public interface OnScrollViewListener {
        void onScrollChanged( ScrollViewWithListener v, int l, int t, int oldl, int oldt );
    }

    public void setOnScrollViewListener(OnScrollViewListener l) {
        this.mOnScrollViewListener = l;
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        mOnScrollViewListener.onScrollChanged( this, l, t, oldl, oldt );
        super.onScrollChanged( l, t, oldl, oldt );
    }
}
