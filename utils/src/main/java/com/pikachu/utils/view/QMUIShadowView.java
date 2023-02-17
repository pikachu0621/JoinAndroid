/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pikachu.utils.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.LinearLayout;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;

import com.pikachu.utils.R;


/**
 * @author cginechen
 * @date 2017-03-10
 * <p>
 * 提取在一起（2021-08-02） 2.0.0版本的QMUI
 */
public class QMUIShadowView extends LinearLayout {


    public static final int RADIUS_OF_HALF_VIEW_HEIGHT = -1;
    public static final int RADIUS_OF_HALF_VIEW_WIDTH = -2;
    private Context mContext;
    // size
    private int mWidthLimit = 0;
    private int mHeightLimit = 0;
    private int mWidthMini = 0;
    private int mHeightMini = 0;


    // divider
    private int mTopDividerHeight = 0;
    private int mTopDividerInsetLeft = 0;
    private int mTopDividerInsetRight = 0;
    private int mTopDividerColor;
    private int mTopDividerAlpha = 255;

    private int mBottomDividerHeight = 0;
    private int mBottomDividerInsetLeft = 0;
    private int mBottomDividerInsetRight = 0;
    private int mBottomDividerColor;
    private int mBottomDividerAlpha = 255;

    private int mLeftDividerWidth = 0;
    private int mLeftDividerInsetTop = 0;
    private int mLeftDividerInsetBottom = 0;
    private int mLeftDividerColor;
    private int mLeftDividerAlpha = 255;

    private int mRightDividerWidth = 0;
    private int mRightDividerInsetTop = 0;
    private int mRightDividerInsetBottom = 0;
    private int mRightDividerColor;
    private int mRightDividerAlpha = 255;
    private Paint mDividerPaint;

    // round
    private Paint mClipPaint;
    private PorterDuffXfermode mMode;
    private int mRadius;
    private @HideRadiusSide
    int mHideRadiusSide = HIDE_RADIUS_SIDE_NONE;
    private float[] mRadiusArray;
    private boolean mShouldUseRadiusArray;
    private RectF mBorderRect;
    private int mBorderColor = 0;
    private int mBorderWidth = 1;
    private int mOuterNormalColor = 0;
    private WeakReference<View> mOwner;
    private boolean mIsOutlineExcludePadding = false;
    private Path mPath = new Path();

    // shadow
    private boolean mIsShowBorderOnlyBeforeL = true;
    private int mShadowElevation = 0;
    private float mShadowAlpha = .5f;
    private int mShadowColor = Color.BLACK; // >=9.0才有效果

    // outline inset
    private int mOutlineInsetLeft = 0;
    private int mOutlineInsetRight = 0;
    private int mOutlineInsetTop = 0;
    private int mOutlineInsetBottom = 0;


    /**
     * 设置是否要在 press 时改变透明度
     */
    private boolean mChangeAlphaWhenPress = true;
    /**
     * 设置是否要在 disabled 时改变透明度
     */
    private boolean mChangeAlphaWhenDisable = true;
    private float mNormalAlpha = 1f;
    private float mPressedAlpha = 0.5f;
    private float mDisabledAlpha = 0.5f;


    public final static int HIDE_RADIUS_SIDE_NONE = 0,
            HIDE_RADIUS_SIDE_TOP = 1,
            HIDE_RADIUS_SIDE_RIGHT = 2,
            HIDE_RADIUS_SIDE_BOTTOM = 3,
            HIDE_RADIUS_SIDE_LEFT = 4;
    private WeakReference<View> mTarget;


    @IntDef(value = {
            HIDE_RADIUS_SIDE_NONE,
            HIDE_RADIUS_SIDE_TOP,
            HIDE_RADIUS_SIDE_RIGHT,
            HIDE_RADIUS_SIDE_BOTTOM,
            HIDE_RADIUS_SIDE_LEFT})
    @Retention(RetentionPolicy.SOURCE)
    @interface HideRadiusSide {
    }


    public QMUIShadowView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public QMUIShadowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public QMUIShadowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }


    public QMUIShadowView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }


    private void init(Context context, AttributeSet attrs, int defAttr, int defStyleRes) {
        mContext = context;
        mOwner = new WeakReference<>(this);
        mBottomDividerColor = mTopDividerColor = Color.parseColor("#DEE0E2");
        mMode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        mClipPaint = new Paint();
        mClipPaint.setAntiAlias(true);
        mBorderRect = new RectF();
        int radius = 0, shadow = 0;
        boolean useThemeGeneralShadowElevation = false;
        if (null != attrs || defAttr != 0 || defStyleRes != 0) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.QMUIShadowView, 0, defStyleRes);
            int count = ta.getIndexCount();
            for (int i = 0; i < count; ++i) {
                int index = ta.getIndex(i);
                if (index == R.styleable.QMUIShadowView_qmui_maxWidth) {
                    mWidthLimit = ta.getDimensionPixelSize(index, mWidthLimit);
                } else if (index == R.styleable.QMUIShadowView_qmui_maxHeight) {
                    mHeightLimit = ta.getDimensionPixelSize(index, mHeightLimit);
                } else if (index == R.styleable.QMUIShadowView_qmui_minWidth) {
                    mWidthMini = ta.getDimensionPixelSize(index, mWidthMini);
                } else if (index == R.styleable.QMUIShadowView_qmui_minHeight) {
                    mHeightMini = ta.getDimensionPixelSize(index, mHeightMini);
                } else if (index == R.styleable.QMUIShadowView_qmui_topDividerColor) {
                    mTopDividerColor = ta.getColor(index, mTopDividerColor);
                } else if (index == R.styleable.QMUIShadowView_qmui_topDividerHeight) {
                    mTopDividerHeight = ta.getDimensionPixelSize(index, mTopDividerHeight);
                } else if (index == R.styleable.QMUIShadowView_qmui_topDividerInsetLeft) {
                    mTopDividerInsetLeft = ta.getDimensionPixelSize(index, mTopDividerInsetLeft);
                } else if (index == R.styleable.QMUIShadowView_qmui_topDividerInsetRight) {
                    mTopDividerInsetRight = ta.getDimensionPixelSize(index, mTopDividerInsetRight);
                } else if (index == R.styleable.QMUIShadowView_qmui_bottomDividerColor) {
                    mBottomDividerColor = ta.getColor(index, mBottomDividerColor);
                } else if (index == R.styleable.QMUIShadowView_qmui_bottomDividerHeight) {
                    mBottomDividerHeight = ta.getDimensionPixelSize(index, mBottomDividerHeight);
                } else if (index == R.styleable.QMUIShadowView_qmui_bottomDividerInsetLeft) {
                    mBottomDividerInsetLeft = ta.getDimensionPixelSize(index, mBottomDividerInsetLeft);
                } else if (index == R.styleable.QMUIShadowView_qmui_bottomDividerInsetRight) {
                    mBottomDividerInsetRight = ta.getDimensionPixelSize(index, mBottomDividerInsetRight);
                } else if (index == R.styleable.QMUIShadowView_qmui_leftDividerColor) {
                    mLeftDividerColor = ta.getColor(index, mLeftDividerColor);
                } else if (index == R.styleable.QMUIShadowView_qmui_leftDividerWidth) {
                    mLeftDividerWidth = ta.getDimensionPixelSize(index, mLeftDividerWidth);
                } else if (index == R.styleable.QMUIShadowView_qmui_leftDividerInsetTop) {
                    mLeftDividerInsetTop = ta.getDimensionPixelSize(index, mLeftDividerInsetTop);
                } else if (index == R.styleable.QMUIShadowView_qmui_leftDividerInsetBottom) {
                    mLeftDividerInsetBottom = ta.getDimensionPixelSize(index, mLeftDividerInsetBottom);
                } else if (index == R.styleable.QMUIShadowView_qmui_rightDividerColor) {
                    mRightDividerColor = ta.getColor(index, mRightDividerColor);
                } else if (index == R.styleable.QMUIShadowView_qmui_rightDividerWidth) {
                    mRightDividerWidth = ta.getDimensionPixelSize(index, mRightDividerWidth);
                } else if (index == R.styleable.QMUIShadowView_qmui_rightDividerInsetTop) {
                    mRightDividerInsetTop = ta.getDimensionPixelSize(index, mRightDividerInsetTop);
                } else if (index == R.styleable.QMUIShadowView_qmui_rightDividerInsetBottom) {
                    mRightDividerInsetBottom = ta.getDimensionPixelSize(index, mRightDividerInsetBottom);
                } else if (index == R.styleable.QMUIShadowView_qmui_borderColor) {
                    mBorderColor = ta.getColor(index, mBorderColor);
                } else if (index == R.styleable.QMUIShadowView_qmui_borderWidth) {
                    mBorderWidth = ta.getDimensionPixelSize(index, mBorderWidth);
                } else if (index == R.styleable.QMUIShadowView_qmui_radius) {
                    radius = ta.getDimensionPixelSize(index, 0);
                } else if (index == R.styleable.QMUIShadowView_qmui_outerNormalColor) {
                    mOuterNormalColor = ta.getColor(index, mOuterNormalColor);
                } else if (index == R.styleable.QMUIShadowView_qmui_hideRadiusSide) {
                    mHideRadiusSide = ta.getInt(index, mHideRadiusSide);
                } else if (index == R.styleable.QMUIShadowView_qmui_showBorderOnlyBeforeL) {
                    mIsShowBorderOnlyBeforeL = ta.getBoolean(index, mIsShowBorderOnlyBeforeL);
                } else if (index == R.styleable.QMUIShadowView_qmui_shadowElevation) {
                    shadow = ta.getDimensionPixelSize(index, shadow);
                } else if (index == R.styleable.QMUIShadowView_qmui_shadowAlpha) {
                    mShadowAlpha = ta.getFloat(index, mShadowAlpha);
                } else if (index == R.styleable.QMUIShadowView_qmui_useThemeGeneralShadowElevation) {
                    useThemeGeneralShadowElevation = ta.getBoolean(index, false);
                } else if (index == R.styleable.QMUIShadowView_qmui_outlineInsetLeft) {
                    mOutlineInsetLeft = ta.getDimensionPixelSize(index, 0);
                } else if (index == R.styleable.QMUIShadowView_qmui_outlineInsetRight) {
                    mOutlineInsetRight = ta.getDimensionPixelSize(index, 0);
                } else if (index == R.styleable.QMUIShadowView_qmui_outlineInsetTop) {
                    mOutlineInsetTop = ta.getDimensionPixelSize(index, 0);
                } else if (index == R.styleable.QMUIShadowView_qmui_outlineInsetBottom) {
                    mOutlineInsetBottom = ta.getDimensionPixelSize(index, 0);
                } else if (index == R.styleable.QMUIShadowView_qmui_outlineExcludePadding) {
                    mIsOutlineExcludePadding = ta.getBoolean(index, false);
                } else if (index == R.styleable.QMUIShadowView_qmui_shadowColor) {
                    mShadowColor = ta.getColor(index, mShadowColor);
                }
            }
            ta.recycle();
        }
        if (shadow == 0 && useThemeGeneralShadowElevation) {
            shadow = 16;

        }
        setRadiusAndShadow(radius, mHideRadiusSide, shadow, mShadowAlpha);
        setChangeAlphaWhenPress(false);
        setChangeAlphaWhenDisable(false);
    }


    private void getAlphaView() {
        if (mTarget == null) {
            mTarget = new WeakReference<>(this);
        }
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        onPressedChanged(this, pressed);
    }


    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        onEnabledChanged(this, enabled);
    }


    /**
     * 在 {@link View#setPressed(boolean)} 中调用，通知 helper 更新
     *
     * @param current the view to be handled, maybe not equal to target view
     * @param pressed {@link View#setPressed(boolean)} 中接收到的参数
     */
    public void onPressedChanged(View current, boolean pressed) {
        getAlphaView();
        View target = mTarget.get();
        if (target == null) {
            return;
        }
        if (current.isEnabled()) {
            target.setAlpha(mChangeAlphaWhenPress && pressed && current.isClickable() ? mPressedAlpha : mNormalAlpha);
        } else {
            if (mChangeAlphaWhenDisable) {
                target.setAlpha(mDisabledAlpha);
            }
        }
    }

    /**
     * 在 {@link View#setEnabled(boolean)} 中调用，通知 helper 更新
     *
     * @param current the view to be handled, maybe not  equal to target view
     * @param enabled {@link View#setEnabled(boolean)} 中接收到的参数
     */
    public void onEnabledChanged(View current, boolean enabled) {
        getAlphaView();
        View target = mTarget.get();
        if (target == null) {
            return;
        }
        float alphaForIsEnable;
        if (mChangeAlphaWhenDisable) {
            alphaForIsEnable = enabled ? mNormalAlpha : mDisabledAlpha;
        } else {
            alphaForIsEnable = mNormalAlpha;
        }
        if (current != target && target.isEnabled() != enabled) {
            target.setEnabled(enabled);
        }
        target.setAlpha(alphaForIsEnable);
    }


    /**
     * 设置是否要在 press 时改变透明度
     *
     * @param changeAlphaWhenPress 是否要在 press 时改变透明度
     */
    public void setChangeAlphaWhenPress(boolean changeAlphaWhenPress) {
        getAlphaView();
        mChangeAlphaWhenPress = changeAlphaWhenPress;
    }

    /**
     * 设置是否要在 disabled 时改变透明度
     *
     * @param changeAlphaWhenDisable 是否要在 disabled 时改变透明度
     */
    public void setChangeAlphaWhenDisable(boolean changeAlphaWhenDisable) {
        getAlphaView();
        mChangeAlphaWhenDisable = changeAlphaWhenDisable;
        View target = mTarget.get();
        if (target != null) {
            onEnabledChanged(target, target.isEnabled());
        }

    }


    public void setUseThemeGeneralShadowElevation() {
        mShadowElevation = 16;
        setRadiusAndShadow(mRadius, mHideRadiusSide, mShadowElevation, mShadowAlpha);
    }


    public void setOutlineExcludePadding(boolean outlineExcludePadding) {
        if (useFeature()) {
            View owner = mOwner.get();
            if (owner == null) {
                return;
            }
            mIsOutlineExcludePadding = outlineExcludePadding;
            owner.invalidateOutline();
        }

    }


    public boolean setWidthLimit(int widthLimit) {

        if (mWidthLimit != widthLimit) {
            mWidthLimit = widthLimit;
            requestLayout();
            invalidatePk();
        }
        return true;
    }


    public boolean setHeightLimit(int heightLimit) {
        if (mHeightLimit != heightLimit) {
            mHeightLimit = heightLimit;
            requestLayout();
            invalidatePk();
        }
        return true;
    }


    public void updateLeftSeparatorColor(int color) {
        if (mLeftDividerColor != color) {
            mLeftDividerColor = color;
            invalidatePk();
        }
    }


    public void updateBottomSeparatorColor(int color) {
        if (mBottomDividerColor != color) {
            mBottomDividerColor = color;
            invalidatePk();
        }
    }


    public void updateTopSeparatorColor(int color) {
        if (mTopDividerColor != color) {
            mTopDividerColor = color;
            invalidatePk();
        }
    }


    public void updateRightSeparatorColor(int color) {
        if (mRightDividerColor != color) {
            mRightDividerColor = color;
            invalidatePk();
        }
    }


    public int getShadowElevation() {
        return mShadowElevation;
    }


    public float getShadowAlpha() {
        return mShadowAlpha;
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawDividers(canvas, getWidth(), getHeight());
        dispatchRoundBorderDraw(canvas);
    }


    public int getShadowColor() {
        return mShadowColor;
    }


    public void setOutlineInset(int left, int top, int right, int bottom) {
        if (useFeature()) {
            View owner = mOwner.get();
            if (owner == null) {
                return;
            }
            mOutlineInsetLeft = left;
            mOutlineInsetRight = right;
            mOutlineInsetTop = top;
            mOutlineInsetBottom = bottom;
            owner.invalidateOutline();
        }
    }


    public void setShowBorderOnlyBeforeL(boolean showBorderOnlyBeforeL) {
        mIsShowBorderOnlyBeforeL = showBorderOnlyBeforeL;
        invalidatePk();
    }


    public void setShadowElevation(int elevation) {
        if (mShadowElevation == elevation) {
            return;
        }
        mShadowElevation = elevation;
        invalidateOutlinePk();
    }


    public void setShadowAlpha(float shadowAlpha) {
        if (mShadowAlpha == shadowAlpha) {
            return;
        }
        mShadowAlpha = shadowAlpha;
        invalidateOutlinePk();
    }


    public void setShadowColor(int shadowColor) {
        if (mShadowColor == shadowColor) {
            return;
        }
        mShadowColor = shadowColor;
        setShadowColorInner(mShadowColor);
    }

    private void setShadowColorInner(int shadowColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            View owner = mOwner.get();
            if (owner == null) {
                return;
            }
            owner.setOutlineAmbientShadowColor(shadowColor);
            owner.setOutlineSpotShadowColor(shadowColor);
        }
    }

    public void invalidateOutlinePk() {
        if (useFeature()) {
            if (mOwner == null)
                mOwner = new WeakReference<>(this);
            View owner = mOwner.get();
            if (owner == null) {
                return;
            }
            if (mShadowElevation == 0) {
                owner.setElevation(0);
            } else {
                owner.setElevation(mShadowElevation);
            }
            owner.invalidateOutline();
        }
    }


    public void invalidatePk() {
        View owner = mOwner.get();
        if (owner == null) {
            return;
        }
        owner.invalidate();
    }


    public void setHideRadiusSide(@HideRadiusSide int hideRadiusSide) {
        if (mHideRadiusSide == hideRadiusSide) {
            return;
        }
        setRadiusAndShadow(mRadius, hideRadiusSide, mShadowElevation, mShadowAlpha);
    }


    public int getHideRadiusSide() {
        return mHideRadiusSide;
    }


    public void setRadius(int radius) {
        if (mRadius != radius) {
            setRadiusAndShadow(radius, mShadowElevation, mShadowAlpha);
        }
    }


    public void setRadius(int radius, @HideRadiusSide int hideRadiusSide) {
        if (mRadius == radius && hideRadiusSide == mHideRadiusSide) {
            return;
        }
        setRadiusAndShadow(radius, hideRadiusSide, mShadowElevation, mShadowAlpha);
    }


    public int getRadius() {
        return mRadius;
    }


    public void setRadiusAndShadow(int radius, int shadowElevation, float shadowAlpha) {
        setRadiusAndShadow(radius, mHideRadiusSide, shadowElevation, shadowAlpha);
    }


    public void setRadiusAndShadow(int radius, @HideRadiusSide int hideRadiusSide, int shadowElevation, float shadowAlpha) {
        setRadiusAndShadow(radius, hideRadiusSide, shadowElevation, mShadowColor, shadowAlpha);
    }


    public void setRadiusAndShadow(int radius, int hideRadiusSide, int shadowElevation, int shadowColor, float shadowAlpha) {
        final View owner = mOwner.get();
        if (owner == null) {
            return;
        }

        mRadius = radius;
        mHideRadiusSide = hideRadiusSide;

        mShouldUseRadiusArray = isRadiusWithSideHidden();
        mShadowElevation = shadowElevation;
        mShadowAlpha = shadowAlpha;
        mShadowColor = shadowColor;
        if (useFeature()) {
            if (mShadowElevation == 0 || mShouldUseRadiusArray) {
                owner.setElevation(0);
            } else {
                owner.setElevation(mShadowElevation);
            }

            setShadowColorInner(mShadowColor);

            owner.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                @TargetApi(21)
                public void getOutline(View view, Outline outline) {
                    int w = view.getWidth(), h = view.getHeight();
                    if (w == 0 || h == 0) {
                        return;
                    }
                    float radius = getRealRadius();
                    int min = Math.min(w, h);
                    if (radius * 2 > min) {
                        // 解决 OnePlus 3T 8.0 上显示变形
                        radius = min / 2F;
                    }
                    if (mShouldUseRadiusArray) {
                        int left = 0, top = 0, right = w, bottom = h;
                        if (mHideRadiusSide == HIDE_RADIUS_SIDE_LEFT) {
                            left -= radius;
                        } else if (mHideRadiusSide == HIDE_RADIUS_SIDE_TOP) {
                            top -= radius;
                        } else if (mHideRadiusSide == HIDE_RADIUS_SIDE_RIGHT) {
                            right += radius;
                        } else if (mHideRadiusSide == HIDE_RADIUS_SIDE_BOTTOM) {
                            bottom += radius;
                        }
                        outline.setRoundRect(left, top,
                                right, bottom, radius);
                        return;
                    }

                    int top = mOutlineInsetTop, bottom = Math.max(top + 1, h - mOutlineInsetBottom),
                            left = mOutlineInsetLeft, right = w - mOutlineInsetRight;
                    if (mIsOutlineExcludePadding) {
                        left += view.getPaddingLeft();
                        top += view.getPaddingTop();
                        right = Math.max(left + 1, right - view.getPaddingRight());
                        bottom = Math.max(top + 1, bottom - view.getPaddingBottom());
                    }

                    float shadowAlpha = mShadowAlpha;
                    if (mShadowElevation == 0) {
                        // outline.setAlpha will work even if shadowElevation == 0
                        shadowAlpha = 1f;
                    }

                    outline.setAlpha(shadowAlpha);

                    if (radius <= 0) {
                        outline.setRect(left, top,
                                right, bottom);
                    } else {
                        outline.setRoundRect(left, top,
                                right, bottom, radius);
                    }
                }
            });
            owner.setClipToOutline(mRadius == RADIUS_OF_HALF_VIEW_WIDTH || mRadius == RADIUS_OF_HALF_VIEW_HEIGHT || mRadius > 0);

        }
        owner.invalidate();
    }

    /**
     * 有radius, 但是有一边不显示radius。
     *
     * @return
     */
    public boolean isRadiusWithSideHidden() {
        return (mRadius == RADIUS_OF_HALF_VIEW_HEIGHT ||
                mRadius == RADIUS_OF_HALF_VIEW_WIDTH ||
                mRadius > 0) && mHideRadiusSide != HIDE_RADIUS_SIDE_NONE;
    }


    public void updateTopDivider(int topInsetLeft, int topInsetRight, int topDividerHeight, int topDividerColor) {
        mTopDividerInsetLeft = topInsetLeft;
        mTopDividerInsetRight = topInsetRight;
        mTopDividerHeight = topDividerHeight;
        mTopDividerColor = topDividerColor;
        invalidatePk();
    }


    public void updateBottomDivider(int bottomInsetLeft, int bottomInsetRight, int bottomDividerHeight, int bottomDividerColor) {
        mBottomDividerInsetLeft = bottomInsetLeft;
        mBottomDividerInsetRight = bottomInsetRight;
        mBottomDividerColor = bottomDividerColor;
        mBottomDividerHeight = bottomDividerHeight;
        invalidatePk();
    }


    public void updateLeftDivider(int leftInsetTop, int leftInsetBottom, int leftDividerWidth, int leftDividerColor) {
        mLeftDividerInsetTop = leftInsetTop;
        mLeftDividerInsetBottom = leftInsetBottom;
        mLeftDividerWidth = leftDividerWidth;
        mLeftDividerColor = leftDividerColor;
        invalidatePk();
    }


    public void updateRightDivider(int rightInsetTop, int rightInsetBottom, int rightDividerWidth, int rightDividerColor) {
        mRightDividerInsetTop = rightInsetTop;
        mRightDividerInsetBottom = rightInsetBottom;
        mRightDividerWidth = rightDividerWidth;
        mRightDividerColor = rightDividerColor;
        invalidatePk();
    }


    public void onlyShowTopDivider(int topInsetLeft, int topInsetRight,
                                   int topDividerHeight, int topDividerColor) {
        updateTopDivider(topInsetLeft, topInsetRight, topDividerHeight, topDividerColor);
        mLeftDividerWidth = 0;
        mRightDividerWidth = 0;
        mBottomDividerHeight = 0;
        invalidatePk();
    }


    public void onlyShowBottomDivider(int bottomInsetLeft, int bottomInsetRight,
                                      int bottomDividerHeight, int bottomDividerColor) {
        updateBottomDivider(bottomInsetLeft, bottomInsetRight, bottomDividerHeight, bottomDividerColor);
        mLeftDividerWidth = 0;
        mRightDividerWidth = 0;
        mTopDividerHeight = 0;
        invalidatePk();
    }


    public void onlyShowLeftDivider(int leftInsetTop, int leftInsetBottom, int leftDividerWidth, int leftDividerColor) {
        updateLeftDivider(leftInsetTop, leftInsetBottom, leftDividerWidth, leftDividerColor);
        mRightDividerWidth = 0;
        mTopDividerHeight = 0;
        mBottomDividerHeight = 0;
        invalidatePk();
    }


    public void onlyShowRightDivider(int rightInsetTop, int rightInsetBottom, int rightDividerWidth, int rightDividerColor) {
        updateRightDivider(rightInsetTop, rightInsetBottom, rightDividerWidth, rightDividerColor);
        mLeftDividerWidth = 0;
        mTopDividerHeight = 0;
        mBottomDividerHeight = 0;
        invalidatePk();
    }


    public void setTopDividerAlpha(int dividerAlpha) {
        mTopDividerAlpha = dividerAlpha;
        invalidatePk();
    }


    public void setBottomDividerAlpha(int dividerAlpha) {
        mBottomDividerAlpha = dividerAlpha;
        invalidatePk();
    }


    public void setLeftDividerAlpha(int dividerAlpha) {
        mLeftDividerAlpha = dividerAlpha;
        invalidatePk();
    }


    public void setRightDividerAlpha(int dividerAlpha) {
        mRightDividerAlpha = dividerAlpha;
        invalidatePk();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = getMeasuredWidthSpec(widthMeasureSpec);
        heightMeasureSpec = getMeasuredHeightSpec(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int minW = handleMiniWidth(widthMeasureSpec, getMeasuredWidth());
        int minH = handleMiniHeight(heightMeasureSpec, getMeasuredHeight());
        if (widthMeasureSpec != minW || heightMeasureSpec != minH) {
            super.onMeasure(minW, minH);
        }

    }


    public int handleMiniWidth(int widthMeasureSpec, int measuredWidth) {
        if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY
                && measuredWidth < mWidthMini) {
            return MeasureSpec.makeMeasureSpec(mWidthMini, MeasureSpec.EXACTLY);
        }
        return widthMeasureSpec;
    }

    public int handleMiniHeight(int heightMeasureSpec, int measuredHeight) {
        if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY
                && measuredHeight < mHeightMini) {
            return MeasureSpec.makeMeasureSpec(mHeightMini, MeasureSpec.EXACTLY);
        }
        return heightMeasureSpec;
    }

    public int getMeasuredWidthSpec(int widthMeasureSpec) {
        if (mWidthLimit > 0) {
            int size = MeasureSpec.getSize(widthMeasureSpec);
            if (size > mWidthLimit) {
                int mode = MeasureSpec.getMode(widthMeasureSpec);
                if (mode == MeasureSpec.AT_MOST) {
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidthLimit, MeasureSpec.AT_MOST);
                } else {
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidthLimit, MeasureSpec.EXACTLY);
                }

            }
        }
        return widthMeasureSpec;
    }

    public int getMeasuredHeightSpec(int heightMeasureSpec) {
        if (mHeightLimit > 0) {
            int size = MeasureSpec.getSize(heightMeasureSpec);
            if (size > mHeightLimit) {
                int mode = MeasureSpec.getMode(heightMeasureSpec);
                if (mode == MeasureSpec.AT_MOST) {
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(mWidthLimit, MeasureSpec.AT_MOST);
                } else {
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(mWidthLimit, MeasureSpec.EXACTLY);
                }
            }
        }
        return heightMeasureSpec;
    }


    public void setBorderColor(@ColorInt int borderColor) {
        mBorderColor = borderColor;
        invalidatePk();
    }


    public void setBorderWidth(int borderWidth) {
        mBorderWidth = borderWidth;
        invalidatePk();
    }


    public void setOuterNormalColor(int color) {
        mOuterNormalColor = color;
        View owner = mOwner.get();
        if (owner != null) {
            owner.invalidate();
        }
    }


    public boolean hasTopSeparator() {
        return mTopDividerHeight > 0;
    }


    public boolean hasRightSeparator() {
        return mRightDividerWidth > 0;
    }


    public boolean hasBottomSeparator() {
        return mBottomDividerHeight > 0;
    }


    public boolean hasLeftSeparator() {
        return mLeftDividerWidth > 0;
    }


    public boolean hasBorder() {
        return mBorderWidth > 0;
    }


    public void drawDividers(Canvas canvas, int w, int h) {
        View owner = mOwner.get();
        if (owner == null) {
            return;
        }
        if (mDividerPaint == null &&
                (mTopDividerHeight > 0 || mBottomDividerHeight > 0 || mLeftDividerWidth > 0 || mRightDividerWidth > 0)) {
            mDividerPaint = new Paint();
        }
        canvas.save();
        canvas.translate(owner.getScrollX(), owner.getScrollY());
        if (mTopDividerHeight > 0) {
            mDividerPaint.setStrokeWidth(mTopDividerHeight);
            mDividerPaint.setColor(mTopDividerColor);
            if (mTopDividerAlpha < 255) {
                mDividerPaint.setAlpha(mTopDividerAlpha);
            }
            float y = mTopDividerHeight / 2f;
            canvas.drawLine(mTopDividerInsetLeft, y, w - mTopDividerInsetRight, y, mDividerPaint);
        }

        if (mBottomDividerHeight > 0) {
            mDividerPaint.setStrokeWidth(mBottomDividerHeight);
            mDividerPaint.setColor(mBottomDividerColor);
            if (mBottomDividerAlpha < 255) {
                mDividerPaint.setAlpha(mBottomDividerAlpha);
            }
            float y = (float) Math.floor(h - mBottomDividerHeight / 2f);
            canvas.drawLine(mBottomDividerInsetLeft, y, w - mBottomDividerInsetRight, y, mDividerPaint);
        }

        if (mLeftDividerWidth > 0) {
            mDividerPaint.setStrokeWidth(mLeftDividerWidth);
            mDividerPaint.setColor(mLeftDividerColor);
            if (mLeftDividerAlpha < 255) {
                mDividerPaint.setAlpha(mLeftDividerAlpha);
            }
            float x = mLeftDividerWidth / 2f;
            canvas.drawLine(x, mLeftDividerInsetTop, x, h - mLeftDividerInsetBottom, mDividerPaint);
        }

        if (mRightDividerWidth > 0) {
            mDividerPaint.setStrokeWidth(mRightDividerWidth);
            mDividerPaint.setColor(mRightDividerColor);
            if (mRightDividerAlpha < 255) {
                mDividerPaint.setAlpha(mRightDividerAlpha);
            }
            float x = (float) Math.floor(w - mRightDividerWidth / 2f);
            canvas.drawLine(x, mRightDividerInsetTop, x, h - mRightDividerInsetBottom, mDividerPaint);
        }
        canvas.restore();
    }


    private int getRealRadius() {
        View owner = mOwner.get();
        if (owner == null) {
            return mRadius;
        }
        int radius;
        if (mRadius == RADIUS_OF_HALF_VIEW_HEIGHT) {
            radius = owner.getHeight() / 2;
        } else if (mRadius == RADIUS_OF_HALF_VIEW_WIDTH) {
            radius = owner.getWidth() / 2;
        } else {
            radius = mRadius;
        }
        return radius;
    }

    public void dispatchRoundBorderDraw(Canvas canvas) {
        View owner = mOwner.get();
        if (owner == null) {
            return;
        }

        int radius = getRealRadius();
        boolean needCheckFakeOuterNormalDraw = radius > 0 && !useFeature() && mOuterNormalColor != 0;
        boolean needDrawBorder = mBorderWidth > 0 && mBorderColor != 0;
        if (!needCheckFakeOuterNormalDraw && !needDrawBorder) {
            return;
        }

        if (mIsShowBorderOnlyBeforeL && useFeature() && mShadowElevation != 0) {
            return;
        }

        int width = canvas.getWidth(), height = canvas.getHeight();
        canvas.save();
        canvas.translate(owner.getScrollX(), owner.getScrollY());

        // react
        float halfBorderWith = mBorderWidth / 2f;
        if (mIsOutlineExcludePadding) {
            mBorderRect.set(
                    owner.getPaddingLeft() + halfBorderWith,
                    owner.getPaddingTop() + halfBorderWith,
                    width - owner.getPaddingRight() - halfBorderWith,
                    height - owner.getPaddingBottom() - halfBorderWith);
        } else {
            mBorderRect.set(halfBorderWith, halfBorderWith,
                    width - halfBorderWith, height - halfBorderWith);
        }

        if (mShouldUseRadiusArray) {
            if (mRadiusArray == null) {
                mRadiusArray = new float[8];
            }
            if (mHideRadiusSide == HIDE_RADIUS_SIDE_TOP) {
                mRadiusArray[4] = radius;
                mRadiusArray[5] = radius;
                mRadiusArray[6] = radius;
                mRadiusArray[7] = radius;
            } else if (mHideRadiusSide == HIDE_RADIUS_SIDE_RIGHT) {
                mRadiusArray[0] = radius;
                mRadiusArray[1] = radius;
                mRadiusArray[6] = radius;
                mRadiusArray[7] = radius;
            } else if (mHideRadiusSide == HIDE_RADIUS_SIDE_BOTTOM) {
                mRadiusArray[0] = radius;
                mRadiusArray[1] = radius;
                mRadiusArray[2] = radius;
                mRadiusArray[3] = radius;
            } else if (mHideRadiusSide == HIDE_RADIUS_SIDE_LEFT) {
                mRadiusArray[2] = radius;
                mRadiusArray[3] = radius;
                mRadiusArray[4] = radius;
                mRadiusArray[5] = radius;
            }
        }

        if (needCheckFakeOuterNormalDraw) {
            int layerId = canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);
            canvas.drawColor(mOuterNormalColor);
            mClipPaint.setColor(mOuterNormalColor);
            mClipPaint.setStyle(Paint.Style.FILL);
            mClipPaint.setXfermode(mMode);
            if (!mShouldUseRadiusArray) {
                canvas.drawRoundRect(mBorderRect, radius, radius, mClipPaint);
            } else {
                drawRoundRect(canvas, mBorderRect, mRadiusArray, mClipPaint);
            }
            mClipPaint.setXfermode(null);
            canvas.restoreToCount(layerId);
        }

        if (needDrawBorder) {
            mClipPaint.setColor(mBorderColor);
            mClipPaint.setStrokeWidth(mBorderWidth);
            mClipPaint.setStyle(Paint.Style.STROKE);
            if (mShouldUseRadiusArray) {
                drawRoundRect(canvas, mBorderRect, mRadiusArray, mClipPaint);
            } else if (radius <= 0) {
                canvas.drawRect(mBorderRect, mClipPaint);
            } else {
                canvas.drawRoundRect(mBorderRect, radius, radius, mClipPaint);
            }
        }
        canvas.restore();
    }

    private void drawRoundRect(Canvas canvas, RectF rect, float[] radiusArray, Paint paint) {
        mPath.reset();
        mPath.addRoundRect(rect, radiusArray, Path.Direction.CW);
        canvas.drawPath(mPath, paint);

    }

    public static boolean useFeature() {
        return Build.VERSION.SDK_INT >= 21;
    }
}
