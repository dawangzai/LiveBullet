package com.wangzai.bullet.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.EdgeEffect
import androidx.recyclerview.widget.RecyclerView
import com.wangzai.bullet.R
import com.wangzai.bullet.util.d2p

class BulletRecyclerView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs) {

    private val maxHeight: Int

    init {
        context.obtainStyledAttributes(attrs, R.styleable.BulletRecyclerView).also {
            maxHeight =
                it.getDimensionPixelSize(R.styleable.BulletRecyclerView_maxHeight, 200.d2p())
        }.recycle()

        // android:overScrollMode="never" 样式
        edgeEffectFactory = object : EdgeEffectFactory() {
            override fun createEdgeEffect(view: RecyclerView, direction: Int): EdgeEffect {
                return object : EdgeEffect(context) {
                    override fun draw(canvas: Canvas): Boolean {
                        return false
                    }
                }
            }
        }
    }

    // 去掉底部渐变消失
    override fun getBottomFadingEdgeStrength(): Float {
        return 0f
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        // 设置最大高度
        val heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST)
        super.onMeasure(widthSpec, heightMeasureSpec)
    }
}