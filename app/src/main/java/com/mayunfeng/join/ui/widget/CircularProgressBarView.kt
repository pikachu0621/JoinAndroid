package com.mayunfeng.join.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View


open class CircularProgressBarView : View {
	private var mDuration = 100
	private var mProgress = 100
	private val mPaint: Paint = Paint()
	private val mRectF: RectF = RectF()
	private var mBackgroundColor: Int = Color.LTGRAY
	private var mPrimaryColor: Int = Color.parseColor("#6DCAEC")
	private var mStrokeWidth = 10f

	/**
	 * 进度条改变监听
	 *
	 * [.onChange]
	 */
	interface OnProgressChangeListener {
		/**
		 * 进度改变事件，当进度条进度改变，就会调用该方法
		 * @param duration 总进度
		 * @param progress 当前进度
		 * @param rate 当前进度与总进度的商 即：rate = (float)progress / duration
		 */
		fun onChange(duration: Int, progress: Int, rate: Float)
	}

	private var mOnChangeListener: OnProgressChangeListener? = null

	/**
	 * 设置进度条改变监听
	 * @param l
	 */
	fun setOnProgressChangeListener(l: OnProgressChangeListener?) {
		mOnChangeListener = l
	}

	constructor(context: Context) : super(context) {}
	constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
	/**
	 * 得到进度条的最大值
	 * @return
	 */
	/**
	 * 设置进度条的最大值, 该值要 大于 0
	 * @param max
	 */
	var max: Int
		get() = mDuration
		set(max) {
			var max = max
			if (max < 0) {
				max = 0
			}
			mDuration = max
		}
	/**
	 * 得到进度条当前的值
	 * @return
	 */
	/**
	 * 设置进度条的当前的值
	 * @param progress
	 */
	var progress: Int
		get() = mProgress
		set(progress) {
			var progress = progress
			if (progress > mDuration) {
				progress = mDuration
			}
			mProgress = progress
			if (mOnChangeListener != null) {
				mOnChangeListener!!.onChange(mDuration, progress, rateOfProgress)
			}
			invalidate()
		}

	/**
	 * 设置进度条背景的颜色
	 */
	override fun setBackgroundColor(color: Int) {
		mBackgroundColor = color
		invalidate()
	}

	/**
	 * 设置进度条进度的颜色
	 */
	fun setPrimaryColor(color: Int) {
		mPrimaryColor = color
		invalidate()
	}

	/**
	 * 设置环形的宽度
	 * @param width
	 */
	fun setCircleWidth(width: Float) {
		mStrokeWidth = width
		invalidate()
	}

	@Synchronized
	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)
		val halfWidth = width / 2F
		val halfHeight = height / 2F
		val radius = if (halfWidth < halfHeight) halfWidth else halfHeight
		val halfStrokeWidth = mStrokeWidth / 2F

		// 设置画笔
		mPaint.color = mBackgroundColor
		mPaint.isDither = true
		mPaint.flags = Paint.ANTI_ALIAS_FLAG
		mPaint.isAntiAlias = true
		mPaint.strokeWidth = mStrokeWidth
		mPaint.style = Paint.Style.STROKE //设置图形为空心

		// 画背景
		canvas.drawCircle(halfWidth, halfHeight, radius - halfStrokeWidth, mPaint)

		// 画当前进度的圆环
		mPaint.color = mPrimaryColor // 改变画笔颜色
		mRectF.top = halfHeight - radius + halfStrokeWidth
		mRectF.bottom = halfHeight + radius - halfStrokeWidth
		mRectF.left = halfWidth - radius + halfStrokeWidth
		mRectF.right = halfWidth + radius - halfStrokeWidth
		canvas.drawArc(mRectF, -90F, rateOfProgress * 360, false, mPaint)
		canvas.save()
	}

	/**
	 * 得到当前的进度的比率
	 *
	 *  用进度条当前的值 与 进度条的最大值求商
	 * @return
	 */
	private val rateOfProgress: Float
		get() = mProgress.toFloat() / mDuration
}
