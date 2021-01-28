package com.wangzai.bullet.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.wangzai.bullet.R
import com.wangzai.bullet.util.d2p

class LiveCardView(context: Context, attributeSet: AttributeSet) :
    FrameLayout(context, attributeSet) {

    private var listener: ((Int) -> Unit)? = null

    private var cardWidth = 258.d2p()
    private var cardHeight = 0

    fun showCard() {
        removeAllViews()
        addLiveCard()
        startVisibleAnimate {
            visibility = VISIBLE
            listener?.invoke(VISIBLE)
        }
    }

    fun dismissCard() {
        if (childCount > 0)
            startGoneAnimate {
                removeLiveCard()
                visibility = GONE
                listener?.invoke(GONE)
            }
    }

    fun setOnVisibilityListener(listener: (Int) -> Unit) {
        this.listener = listener
    }

    fun getCardHeight(): Int {
        return cardHeight
    }

    private fun addLiveCard() {
        inflate(context, R.layout.layout_goods, null).let {
            measureCardSize(it)
            addView(it)
        }
    }

    private fun removeLiveCard() {
        removeAllViews()
    }

    // 手动测量一下卡片的宽高，做动画需要用到
    private fun measureCardSize(cardView: View) {
        cardView.measure(
            MeasureSpec.makeMeasureSpec(cardWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
        cardWidth = cardView.measuredWidth
        cardHeight = cardView.measuredHeight
    }

    private fun startGoneAnimate(endAction: Runnable) {
        animate()
            .scaleX(0f)
            .scaleY(0f)
            .translationX(-cardWidth / 2.toFloat())
            .translationY(cardHeight / 2.toFloat())
            .setDuration(300)
            .withEndAction(endAction)
            .start()
    }

    private fun startVisibleAnimate(startAction: Runnable) {
        animate()
            .scaleX(1f)
            .scaleY(1f)
            .translationX(0f)
            .translationY(0f)
            .setDuration(300)
            .withStartAction(startAction)
            .start()
    }
}