package com.mayunfeng.join.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import com.mayunfeng.join.R

class ProgressBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint = Paint()
    private var max = 100f
    private var progress = 50f
    private var progressPx = 0f
    private var numberFormat = 0
    private var textSize = 15f
    private var textColor = -0xbca631
    private var textStyle = Typeface.NORMAL
    private var text: String? = "%d%"
    private var progressForwardColor = -0xbca631
    private var progressAfterColor = -0xf0f10
    private var progressHeight = 20f
    private var progressToTextHeight = 10f
    private var viewHeight = 0f
    private var textHeight = 0f
    private var progressIsRound = false
    private var isShowText = false

    init {
        val typeArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.ProgressBarView, 0, 0)
        textSize = typeArray.getDimension(R.styleable.ProgressBarView_android_textSize, textSize)
        textColor = typeArray.getColor(R.styleable.ProgressBarView_android_textColor, textColor)
        textStyle = typeArray.getInt(R.styleable.ProgressBarView_android_textStyle, 0)
        text = typeArray.getString(R.styleable.ProgressBarView_android_text)
        progressForwardColor = typeArray.getColor(
            R.styleable.ProgressBarView_progressForwardColor,
            progressForwardColor
        )
        progressAfterColor =
            typeArray.getColor(R.styleable.ProgressBarView_progressAfterColor, progressAfterColor)
        progressHeight =
            typeArray.getDimension(R.styleable.ProgressBarView_progressHeight, progressHeight)
        progressToTextHeight = typeArray.getDimension(
            R.styleable.ProgressBarView_progressToTextHeight,
            progressToTextHeight
        )
        max = typeArray.getFloat(R.styleable.ProgressBarView_progressMax, max)
        progress = typeArray.getFloat(R.styleable.ProgressBarView_progressNow, progress)
        numberFormat = typeArray.getInt(R.styleable.ProgressBarView_numberFormat, numberFormat)
        progressIsRound =
            typeArray.getBoolean(R.styleable.ProgressBarView_progressIsRound, progressIsRound)
        isShowText = typeArray.getBoolean(R.styleable.ProgressBarView_isShowText, isShowText)
        paint.isAntiAlias = true
        if (isShowText) {
            paint.typeface = Typeface.defaultFromStyle(textStyle)
            paint.color = textColor
            paint.textSize = textSize
        }
        if (progressIsRound) {
            paint.strokeCap = Paint.Cap.ROUND
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        textHeight = getTextHeight(paint)
        viewHeight = if (isShowText) {
            textHeight + progressToTextHeight + progressHeight
        } else {
            progressHeight
        }
        setMeasuredDimension(measuredWidth, viewHeight.toInt())
        setMax(max)
    }

    private fun getTextHeight(paint: Paint): Float {
        val fm = paint.fontMetrics
        return fm.descent - fm.ascent
        // return fm.bottom - fm.top + fm.leading
    }

    public override fun onDraw(canvas: Canvas) {
        val fl = progressPx * progress
        if (isShowText) {
            paint.strokeWidth = 0f
            paint.color = textColor
            val format = String.format("%.${numberFormat}f", progress / max * 100f)
            val str = text!!.replace("%d", format)
            val measureText = paint.measureText(str)
            val text2 = measureText / 2
            val startText = fl - text2
            var wf = 0f
            wf = if (startText < 0) {
                0f
            } else Math.min(startText, width - measureText)
            canvas.drawText(str, wf, textHeight, paint)
        }
        paint.strokeWidth = progressHeight
        paint.color = progressAfterColor
        var fl1 = 0f
        fl1 = if (isShowText) {
            textHeight + progressToTextHeight + progressHeight / 2f
        } else {
            progressHeight / 2f
        }
        canvas.drawLine(0f, fl1, width.toFloat(), fl1, paint)
        paint.color = progressForwardColor
        canvas.drawLine(0f, fl1, fl, fl1, paint)
    }

    fun setMax(max: Float) {
        this.max = max
        progressPx = measuredWidth / max
        postInvalidate()
    }

    fun setProgress(progress: Float) {
        this.progress = progress
        postInvalidate()
    }
}