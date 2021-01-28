package com.wangzai.bullet

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.wangzai.bullet.adapter.BulletAdapter
import com.wangzai.bullet.dialog.InputDialog
import com.wangzai.bullet.entity.BULLET_TYPE_NORMAL
import com.wangzai.bullet.entity.Bullet
import com.wangzai.bullet.view.BulletView
import com.wangzai.bullet.view.LiveCardView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var viewBullet: BulletView
    private lateinit var tvBullet: TextView
    private lateinit var ivGoods: ImageView
    private lateinit var viewCard: LiveCardView
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
        setContentView(R.layout.activity_main)

        viewBullet = findViewById(R.id.view_bullet)
        viewCard = findViewById(R.id.view_card)
        tvBullet = findViewById(R.id.tv_bullet)
        ivGoods = findViewById(R.id.iv_goods)

        tvBullet.setOnClickListener { showInputDialog() }
        ivGoods.setOnClickListener { addCard() }
        viewBullet.setAdapter(BulletAdapter())
        viewCard.setOnVisibilityListener {
            if (it == View.VISIBLE) {
                viewBullet.liveCardChange(viewCard.getCardHeight())
            } else {
                viewBullet.liveCardChange(0)
            }
        }
    }

    private fun addCard() {
        if (viewCard.visibility == View.VISIBLE) {
            viewCard.dismissCard()
            job?.cancel()
        } else {
            viewCard.showCard()
            job = lifecycleScope.launch {
                delay(5000)
                viewCard.dismissCard()
            }
        }
    }

    private fun showInputDialog() {
        InputDialog().apply {
            setKeyboardListener { _, margin ->
                viewBullet.keyboardChange(margin)
            }
            setOnTextSendListener {
                viewBullet.addMessage(Bullet("wangzai", it, BULLET_TYPE_NORMAL))
            }
        }.show(supportFragmentManager, "InputDialog")
    }
}