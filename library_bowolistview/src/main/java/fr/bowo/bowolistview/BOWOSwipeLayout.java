/**
 The MIT License (MIT)

 Copyright (c) 2016 Chau Thai

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */

package fr.bowo.bowolistview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("RtlHardcoded")
public class BOWOSwipeLayout extends ViewGroup {
    // These states are used only for ViewBindHelper
    protected static final int STATE_CLOSE     = 0;
    protected static final int STATE_CLOSING   = 1;
    protected static final int STATE_OPEN      = 2;
    protected static final int STATE_OPENING   = 3;
    protected static final int STATE_DRAGGING  = 4;//Glisser

    private static final int DEFAULT_MIN_FLING_VELOCITY = 300; // dp per second
    private static final int DEFAULT_MIN_DIST_REQUEST_DISALLOW_PARENT = 0; // dp

    public static final int DRAG_EDGE_LEFT =   0x1;
    public static final int DRAG_EDGE_RIGHT =  0x1 << 1;//TODO voir pk ce nombre c important

    /**
     * The secondary view will be under the main view.
     */
    public static final int MODE_NORMAL = 0;

    /**
     * The secondary view will stick the edge of the main view.
     */
    public static final int MODE_SAME_LEVEL = 1;

    /**
     * Main view is the view which is shown when the layout is closed.
     */
    private View mMainView;

    /**
     * Secondary view is the view which is shown when the layout is opened.
     */
    private View mSecondaryView;

    /**
     * The rectangle position of the main view when the layout is closed.
     */
    private Rect mRectMainClose = new Rect();

    /**
     * The rectangle position of the main view when the layout is opened.
     */
    private Rect mRectMainOpen  = new Rect();

    /**
     * The rectangle position of the secondary view when the layout is closed.
     */
    private Rect mRectSecClose  = new Rect();

    /**
     * The rectangle position of the secondary view when the layout is opened.
     */
    private Rect mRectSecOpen   = new Rect();

    /**
     * The minimum distance (px) to the closest drag edge that the BowoSwipeLayout
     * will disallow the parent to intercept touch event.
     */
    private int mMinDistRequestDisallowParent = 0;

    private boolean isOpen = false;
    private volatile boolean mAborted = false;
    private volatile boolean mIsScrolling = false;
    private volatile boolean mLockDrag = false;

    private int mMinFlingVelocity = DEFAULT_MIN_FLING_VELOCITY;
    private int mState = STATE_CLOSE;
    private int mMode = MODE_NORMAL;

    private int mLastMainLeft = 0;
    private int mLastMainTop  = 0;

    private int mDragEdge = DRAG_EDGE_RIGHT;//TODO left

    private ViewDragHelper mDragHelper;
    private GestureDetectorCompat mGestureDetector;

    private DragStateChangeListener mDragStateChangeListener; // only used for ViewBindHelper
    private SwipeListener mSwipeListener;

    private int mOnLayoutCount = 0;
    private boolean isSwipeable = true;
    interface DragStateChangeListener {
        void onDragStateChanged(int state);
    }

    /**
     * Listener for monitoring events about swipe layout.
     */
    public interface SwipeListener {
        /**
         * Called when the main view becomes completely closed.
         */
        void onClosed(BOWOSwipeLayout view);

        /**
         * Called when the main view becomes completely opened.
         */
        void onOpened(BOWOSwipeLayout view);

        /**
         * Called when the main view's position changes.
         * @param slideOffset The new offset of the main view within its range, from 0-1
         */
        void onSlide(BOWOSwipeLayout view, float slideOffset);
    }


    public BOWOSwipeLayout(Context context) {
        super(context);
        init(context, null);
    }

    public BOWOSwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    //TODO voir
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        mDragHelper.processTouchEvent(event);
        return true;
    }

    //TODO voir
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mDragHelper.processTouchEvent(ev);
        mGestureDetector.onTouchEvent(ev);

        boolean settling = mDragHelper.getViewDragState() == ViewDragHelper.STATE_SETTLING;
        boolean idleAfterScrolled = mDragHelper.getViewDragState() == ViewDragHelper.STATE_IDLE
                && mIsScrolling;

        return settling || idleAfterScrolled;
    }

    //Inflate la vue principale et les vue secondaire (comme le bouton delete)
    //La vue secounder est toujour en premier dans le xml
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // get views
        if (getChildCount() == 2 && getChildAt(0).getVisibility() == VISIBLE && getChildAt(1).getVisibility() == VISIBLE) {
            mSecondaryView = getChildAt(0);
            mMainView = getChildAt(1);
        }
        else if (getChildCount() == 1) {
            mMainView = getChildAt(0);
        }
        else if (getChildCount() >= 2) {
            mMainView = getChildAt(0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAborted = false;
        //Definis les tailles des vues
        for (int index = 0; index < getChildCount(); index++) {
            final View child = getChildAt(index);

            int left, right, top, bottom;
            left = right = top = bottom = 0;

            final int minLeft = getPaddingLeft();
            final int maxRight = Math.max(r - getPaddingRight() - l, 0);
            final int minTop = getPaddingTop();
            final int maxBottom = Math.max(b - getPaddingBottom() - t, 0);

            int measuredChildHeight = child.getMeasuredHeight();
            int measuredChildWidth = child.getMeasuredWidth();

            // need to take account if child size is match_parent
            final LayoutParams childParams = child.getLayoutParams();
            boolean matchParentHeight = false;
            boolean matchParentWidth = false;

            if (childParams != null) {
                matchParentHeight = (childParams.height == LayoutParams.MATCH_PARENT) ||
                        (childParams.height == LayoutParams.FILL_PARENT);
                matchParentWidth = (childParams.width == LayoutParams.MATCH_PARENT) ||
                        (childParams.width == LayoutParams.FILL_PARENT);
            }

            if (matchParentHeight) {
                measuredChildHeight = maxBottom - minTop;
                childParams.height = measuredChildHeight;
            }

            if (matchParentWidth) {
                measuredChildWidth = maxRight - minLeft;
                childParams.width = measuredChildWidth;
            }

            switch (mDragEdge) {
                case DRAG_EDGE_LEFT:
                    left    = Math.min(getPaddingLeft(), maxRight);
                    top     = Math.min(getPaddingTop(), maxBottom);
                    right   = Math.min(measuredChildWidth + getPaddingLeft(), maxRight);
                    bottom  = Math.min(measuredChildHeight + getPaddingTop(), maxBottom);
                    break;
                case DRAG_EDGE_RIGHT:
                    left    = Math.max(r - measuredChildWidth - getPaddingRight() - l, minLeft);
                    top     = Math.min(getPaddingTop(), maxBottom);
                    right   = Math.max(r - getPaddingRight() - l, minLeft);
                    bottom  = Math.min(measuredChildHeight + getPaddingTop(), maxBottom);
                    break;
            }

            child.layout(left, top, right, bottom);
        }
        if (mSecondaryView == null) {
            return;
        }
        // taking account offset when mode is SAME_LEVEL
        //TODO a voir
        if (mMode == MODE_SAME_LEVEL) {
            switch (mDragEdge) {
                case DRAG_EDGE_LEFT:
                    mSecondaryView.offsetLeftAndRight(-mSecondaryView.getWidth());
                    break;
                case DRAG_EDGE_RIGHT:
                    mSecondaryView.offsetLeftAndRight(mSecondaryView.getWidth());
                    break;
            }
        }

        initRects();

        if (isOpen) {
            open(false);
        } else {
            close(false);
        }

        //TODO a voir
        mLastMainLeft = mMainView.getLeft();
        mLastMainTop = mMainView.getTop();

        mOnLayoutCount++;
    }

    /**
     * {@inheritDoc}
     */
    //TODO a VOIR
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
      if (getChildCount() > 2) {
            throw new RuntimeException("Error Layout : more than 2 child");
        }

        final LayoutParams params = getLayoutParams();

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int desiredWidth = 0;
        int desiredHeight = 0;

        // first find the largest child
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            desiredWidth = Math.max(child.getMeasuredWidth(), desiredWidth);
            desiredHeight = Math.max(child.getMeasuredHeight(), desiredHeight);
        }
        // create new measure spec using the largest child width
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(desiredWidth, widthMode);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(desiredHeight, heightMode);

        final int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            final LayoutParams childParams = child.getLayoutParams();

            if (childParams != null) {
                if (childParams.height == LayoutParams.MATCH_PARENT) {
                    child.setMinimumHeight(measuredHeight);
                }

                if (childParams.width == LayoutParams.MATCH_PARENT) {
                    child.setMinimumWidth(measuredWidth);
                }
            }

            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            desiredWidth = Math.max(child.getMeasuredWidth(), desiredWidth);
            desiredHeight = Math.max(child.getMeasuredHeight(), desiredHeight);
        }

        // taking accounts of padding
        desiredWidth += getPaddingLeft() + getPaddingRight();
        desiredHeight += getPaddingTop() + getPaddingBottom();

        // adjust desired width
        if (widthMode == MeasureSpec.EXACTLY) {
            desiredWidth = measuredWidth;
        } else {
            if (params.width == LayoutParams.MATCH_PARENT) {
                desiredWidth = measuredWidth;
            }

            if (widthMode == MeasureSpec.AT_MOST) {
                desiredWidth = (desiredWidth > measuredWidth)? measuredWidth : desiredWidth;
            }
        }

        // adjust desired height
        if (heightMode == MeasureSpec.EXACTLY) {
            desiredHeight = measuredHeight;
        } else {
            if (params.height == LayoutParams.MATCH_PARENT) {
                desiredHeight = measuredHeight;
            }

            if (heightMode == MeasureSpec.AT_MOST) {
                desiredHeight = (desiredHeight > measuredHeight)? measuredHeight : desiredHeight;
            }
        }
        setMeasuredDimension(desiredWidth, desiredHeight);
    }

    //TODO a voir
    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void open(boolean animation) {
        isOpen = true;
        mAborted = false;
        if (mSecondaryView == null || isSwipeable == false) {
            return;
        }
        if (animation) {
            mState = STATE_OPENING;
            mDragHelper.smoothSlideViewTo(mMainView, mRectMainOpen.left, mRectMainOpen.top);

            if (mDragStateChangeListener != null) {
                mDragStateChangeListener.onDragStateChanged(mState);
            }
        } else {
            mState = STATE_OPEN;
            mDragHelper.abort();

            mMainView.layout(
                    mRectMainOpen.left,
                    mRectMainOpen.top,
                    mRectMainOpen.right,
                    mRectMainOpen.bottom
            );

            mSecondaryView.layout(
                    mRectSecOpen.left,
                    mRectSecOpen.top,
                    mRectSecOpen.right,
                    mRectSecOpen.bottom
            );
        }

        ViewCompat.postInvalidateOnAnimation(BOWOSwipeLayout.this);
    }

    /**
     * Close the panel to hide the secondary view
     * @param animation true to animate the close motion. {@link SwipeListener} won't be
     *                  called if is animation is false.
     */
    public void close(boolean animation) {
        isOpen = false;
        mAborted = false;
        if (mSecondaryView == null || isSwipeable == false) {
            return;
        }

        if (animation) {
            mState = STATE_CLOSING;
            mDragHelper.smoothSlideViewTo(mMainView, mRectMainClose.left, mRectMainClose.top);

            if (mDragStateChangeListener != null) {
                mDragStateChangeListener.onDragStateChanged(mState);
            }

        } else {
            mState = STATE_CLOSE;
            mDragHelper.abort();

            mMainView.layout(
                    mRectMainClose.left,
                    mRectMainClose.top,
                    mRectMainClose.right,
                    mRectMainClose.bottom
            );

            mSecondaryView.layout(
                    mRectSecClose.left,
                    mRectSecClose.top,
                    mRectSecClose.right,
                    mRectSecClose.bottom
            );
        }
        ViewCompat.postInvalidateOnAnimation(BOWOSwipeLayout.this);
    }

    public void setDragEdge(int dragEdge) {
        mDragEdge = dragEdge;
    }

    public int getDragEdge() {
        return mDragEdge;
    }


    /** Only used for {@link ViewBinderHelper} */
/*    void setDragStateChangeListener(DragStateChangeListener listener) {
        mDragStateChangeListener = listener;
    }*/

    /** Abort current motion in progress. Only used for {@link ViewBinderHelper} */
    /*protected void abort() {
        mAborted = true;
        mDragHelper.abort();
    }*/

    /**
     * In RecyclerView/ListView, onLayout should be called 2 times to display children views correctly.
     * This method check if it've already called onLayout two times.
     * @return true if you should call {@link #requestLayout()}.
     */
    protected boolean shouldRequestLayout() {
        return mOnLayoutCount < 2;
    }


    private int getMainOpenLeft() {
        switch (mDragEdge) {
            case DRAG_EDGE_LEFT:
                return mRectMainClose.left + mSecondaryView.getWidth();
            case DRAG_EDGE_RIGHT:
                return mRectMainClose.left - mSecondaryView.getWidth();
            default:
                return 0;
        }
    }

    private int getMainOpenTop() {
        switch (mDragEdge) {
            case DRAG_EDGE_RIGHT:
                return mRectMainClose.top;
            case DRAG_EDGE_LEFT:
                return mRectMainClose.top;
            default:
                return 0;
        }
    }

    private int getSecOpenLeft() {
        if (mMode == MODE_NORMAL) {
            return mRectSecClose.left;
        }
        if (mDragEdge == DRAG_EDGE_LEFT) {
            return mRectSecClose.left + mSecondaryView.getWidth();
        } else {
            return mRectSecClose.left - mSecondaryView.getWidth();
        }
    }

    private int getSecOpenTop() {
        if (mMode == MODE_NORMAL || mDragEdge == DRAG_EDGE_RIGHT || mDragEdge == DRAG_EDGE_LEFT) {
            return mRectSecClose.top;
        }
        return mRectSecClose.top - mSecondaryView.getHeight();
    }

    private void initRects() {
        // close position of main view
        mRectMainClose.set(
                mMainView.getLeft(),
                mMainView.getTop(),
                mMainView.getRight(),
                mMainView.getBottom()
        );

        // close position of secondary view
        mRectSecClose.set(
                mSecondaryView.getLeft(),
                mSecondaryView.getTop(),
                mSecondaryView.getRight(),
                mSecondaryView.getBottom()
        );

        // open position of the main view
        mRectMainOpen.set(
                getMainOpenLeft(),
                getMainOpenTop(),
                getMainOpenLeft() + mMainView.getWidth(),
                getMainOpenTop() + mMainView.getHeight()
        );

        // open position of the secondary view
        mRectSecOpen.set(
                getSecOpenLeft(),
                getSecOpenTop(),
                getSecOpenLeft() + mSecondaryView.getWidth(),
                getSecOpenTop() + mSecondaryView.getHeight()
        );
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null && context != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.BowoSwipeLayout,
                    0, 0
            );

            mDragEdge = a.getInteger(R.styleable.BowoSwipeLayout_dragEdge, DRAG_EDGE_RIGHT);//TODO left
            mMinFlingVelocity = a.getInteger(R.styleable.BowoSwipeLayout_flingVelocity, DEFAULT_MIN_FLING_VELOCITY);
            mMode = a.getInteger(R.styleable.BowoSwipeLayout_mode, MODE_NORMAL);

            mMinDistRequestDisallowParent = a.getDimensionPixelSize(
                    R.styleable.BowoSwipeLayout_minDistRequestDisallowParent,
                    dpToPx(DEFAULT_MIN_DIST_REQUEST_DISALLOW_PARENT)
            );
        }

        mDragHelper = ViewDragHelper.create(this, 1.0f, mDragHelperCallback);
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL);

        mGestureDetector = new GestureDetectorCompat(context, mGestureListener);
    }

    private final GestureDetector.OnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        boolean hasDisallowed = false;

        @Override
        public boolean onDown(MotionEvent e) {
            mIsScrolling = false;
            hasDisallowed = false;
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            mIsScrolling = true;
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mIsScrolling = true;

            if (getParent() != null) {
                boolean shouldDisallow;

                if (!hasDisallowed) {
                    shouldDisallow = getDistToClosestEdge() >= mMinDistRequestDisallowParent;
                    if (shouldDisallow) {
                        hasDisallowed = true;
                    }
                } else {
                    shouldDisallow = true;
                }

                // disallow parent to intercept touch event so that the layout will work
                // properly on RecyclerView or view that handles scroll gesture.
                getParent().requestDisallowInterceptTouchEvent(shouldDisallow);
            }

            return false;
        }
    };

    private int getDistToClosestEdge() {
        if (mSecondaryView == null || isSwipeable == false) {
            return 0;
        }
        switch (mDragEdge) {

            case DRAG_EDGE_RIGHT:
                final int pivotLeft = mRectMainClose.right - mSecondaryView.getWidth();

                return Math.min(
                        mMainView.getRight() - pivotLeft,
                        mRectMainClose.right - mMainView.getRight()
                );
            case DRAG_EDGE_LEFT:
                final int pivotRight = mRectMainClose.left + mSecondaryView.getWidth();

                return Math.min(
                        mMainView.getLeft() - mRectMainClose.left,
                        pivotRight - mMainView.getLeft()
                );
        }
        return 0;
    }

    private int getHalfwayPivotHorizontal() {
        if (mSecondaryView == null || isSwipeable == false) {
            return mRectMainClose.right;
        }
        if (mDragEdge == DRAG_EDGE_LEFT) {
            return mRectMainClose.left + mSecondaryView.getWidth() / 2;
        } else {
            return mRectMainClose.right - mSecondaryView.getWidth() / 2;
        }
    }

    private final ViewDragHelper.Callback mDragHelperCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            mAborted = false;

            if (mLockDrag)
                return false;

            mDragHelper.captureChildView(mMainView, pointerId);
            return false;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            switch (mDragEdge) {
                default:
                    return child.getTop();
            }
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (mSecondaryView == null || isSwipeable == false) {
                return child.getLeft();
            }
            switch (mDragEdge) {
                case DRAG_EDGE_RIGHT:
                    return Math.max(
                            Math.min(left, mRectMainClose.left),
                            mRectMainClose.left - mSecondaryView.getWidth()
                    );
                case DRAG_EDGE_LEFT:
                    return Math.max(
                            Math.min(left, mRectMainClose.left + mSecondaryView.getWidth()),
                            mRectMainClose.left
                    );
                default:
                    return child.getLeft();
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            final boolean velRightExceeded =  pxToDp((int) xvel) >= mMinFlingVelocity;
            final boolean velLeftExceeded =   pxToDp((int) xvel) <= -mMinFlingVelocity;
            final int pivotHorizontal = getHalfwayPivotHorizontal();

            switch (mDragEdge) {
                case DRAG_EDGE_RIGHT:
                    if (velRightExceeded) {
                        close(true);
                    } else if (velLeftExceeded) {
                        open(true);
                    } else {
                        if (mMainView.getRight() < pivotHorizontal) {
                            open(true);
                        } else {
                            close(true);
                        }
                    }
                    break;
                case DRAG_EDGE_LEFT:
                    if (velRightExceeded) {
                        open(true);
                    } else if (velLeftExceeded) {
                        close(true);
                    } else {
                        if (mMainView.getLeft() < pivotHorizontal) {
                            close(true);
                        } else {
                            open(true);
                        }
                    }
                    break;
            }
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            super.onEdgeDragStarted(edgeFlags, pointerId);

            if (mLockDrag) {
                return;
            }
            boolean edgeStartRight = (mDragEdge == DRAG_EDGE_LEFT)
                    && edgeFlags == ViewDragHelper.EDGE_RIGHT;
            boolean edgeStartLeft = (mDragEdge == DRAG_EDGE_RIGHT)
                    && edgeFlags == ViewDragHelper.EDGE_LEFT;
            if (edgeStartLeft || edgeStartRight) {
                mDragHelper.captureChildView(mMainView, pointerId);
            }
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (mSecondaryView == null) {
                return;
            }
            if (mMode == MODE_SAME_LEVEL) {
                if (mDragEdge == DRAG_EDGE_LEFT || mDragEdge == DRAG_EDGE_RIGHT) {
                    mSecondaryView.offsetLeftAndRight(dx);
                } else {
                    mSecondaryView.offsetTopAndBottom(dy);
                }
            }

            boolean isMoved = (mMainView.getLeft() != mLastMainLeft) || (mMainView.getTop() != mLastMainTop);
            if (mSwipeListener != null && isMoved) {
                if (mMainView.getLeft() == mRectMainClose.left && mMainView.getTop() == mRectMainClose.top) {
                    mSwipeListener.onClosed(BOWOSwipeLayout.this);
                }
                else if (mMainView.getLeft() == mRectMainOpen.left && mMainView.getTop() == mRectMainOpen.top) {
                    mSwipeListener.onOpened(BOWOSwipeLayout.this);
                }
                else {
                    mSwipeListener.onSlide(BOWOSwipeLayout.this, getSlideOffset());
                }
            }

            mLastMainLeft = mMainView.getLeft();
            mLastMainTop = mMainView.getTop();
            ViewCompat.postInvalidateOnAnimation(BOWOSwipeLayout.this);
        }

        private float getSlideOffset() {
            if (mSecondaryView == null || isSwipeable == false) {
                return 0;
            }
            switch (mDragEdge) {
                case DRAG_EDGE_LEFT:
                    return (float) (mMainView.getLeft() - mRectMainClose.left) / mSecondaryView.getWidth();
                case DRAG_EDGE_RIGHT:
                    return (float) (mRectMainClose.left - mMainView.getLeft()) / mSecondaryView.getWidth();
                default:
                    return 0;
            }
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            final int prevState = mState;

            switch (state) {
                case ViewDragHelper.STATE_DRAGGING:
                    mState = STATE_DRAGGING;
                    break;

                case ViewDragHelper.STATE_IDLE:

                    // drag edge is left or right
                    if (mDragEdge == DRAG_EDGE_LEFT || mDragEdge == DRAG_EDGE_RIGHT) {
                        if (mMainView.getLeft() == mRectMainClose.left) {
                            mState = STATE_CLOSE;
                        } else {
                            mState = STATE_OPEN;
                        }
                    }
                    break;
            }

            if (mDragStateChangeListener != null && !mAborted && prevState != mState) {
                mDragStateChangeListener.onDragStateChanged(mState);
            }
        }
    };

    private int pxToDp(int px) {
        Resources resources = getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private int dpToPx(int dp) {
        Resources resources = getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public void setSwipeable(boolean swipeable) {
        isSwipeable = swipeable;
    }
}
