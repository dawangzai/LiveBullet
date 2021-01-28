package com.wangzai.bullet.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wangzai.bullet.R
import com.wangzai.bullet.adapter.BulletAdapter
import com.wangzai.bullet.entity.BULLET_TYPE_NOTICE
import com.wangzai.bullet.entity.Bullet
import com.wangzai.bullet.util.d2p

// 列表最大展示的消息数量
const val MAX_COUNT_DEFAULT = 100

// 弹幕公告
const val DEFAULT_BULLET =
    "欢迎来到直播间～我们提倡绿色的直播，直播内容和评论严禁出现违法违规、低俗色情、涉政、涉恐、涉群体性事件等内容。请文明观看和互动哦，购买商品请勿轻信其他广告信息，以免上当受骗！"

class BulletView(context: Context, attributeSet: AttributeSet?) :
    FrameLayout(context, attributeSet) {

    // 输入框距离底部的默认高度
    private val BULLET_DEFAULT_MARGIN = 68f.d2p().toInt()

    // 输入框距离底部的高度
    private var currentTranslationY: Float = 0f
    private var isKeyboardShow: Boolean = false

    private var recyclerView: BulletRecyclerView
    private var bulletCountView: TextView
    private val layoutManager = LinearLayoutManager(context).also { it.stackFromEnd = true }
    private var listAdapter: BulletAdapter? = null

    // 还未刷新的消息集合
    private val newMessageList = mutableListOf<Bullet>()

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_bullet, this, true)
        recyclerView = findViewById(R.id.bullet_recycler)
        bulletCountView = findViewById(R.id.tv_bullet_count)

        initView()
    }

    private fun initView() {
        recyclerView.layoutManager = layoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 滑动到底部有新的消息执行刷新
                    checkAdapter()
                    if (layoutManager.findLastVisibleItemPosition()
                        == listAdapter!!.listData.size - 1
                        && newMessageList.size > 0
                    ) {
                        fixUnreadMessage()
                    }
                }
            }
        })
        bulletCountView.setOnClickListener {
            fixUnreadMessage()
        }
    }

    fun setAdapter(adapter: BulletAdapter) {
        listAdapter = adapter
        // 添加弹幕公告
        val bullet = Bullet(null, DEFAULT_BULLET, BULLET_TYPE_NOTICE)
        listAdapter!!.addData(bullet)
        recyclerView.adapter = listAdapter
    }

    // 添加新消息
    fun addMessage(bullet: Bullet) {
        checkAdapter()
        if (listAdapter!!.listData.size == 0 ||
            layoutManager.findLastVisibleItemPosition() == listAdapter!!.listData.size - 1
        ) {
            // 如果当前显示最后一条，刷新消息列表
            val listData = listAdapter!!.listData
            val size = listData.size
            if (size >= MAX_COUNT_DEFAULT) {
                listData.add(bullet)
                listData.removeAt(1)
            } else {
                listData.add(bullet)
            }
            listAdapter!!.notifyDataSetChanged()
            recyclerView.scrollToPosition(listData.size - 1)
        } else {
            newMessageList.add(bullet)
            setUnreadMessageCount()
        }
    }

    // 合并未读的消息
    private fun fixUnreadMessage() {
        val listData = listAdapter!!.listData
        listData.addAll(newMessageList)
        if (listData.size > MAX_COUNT_DEFAULT) {
            listData.subList(1, listData.size - MAX_COUNT_DEFAULT).clear()
        }
        listAdapter!!.notifyDataSetChanged();
        recyclerView.scrollToPosition(listAdapter!!.listData.size - 1)
        newMessageList.clear()
        setUnreadMessageCount()
    }

    private fun setUnreadMessageCount() {
        newMessageList.let {
            if (it.size > 0) {
                bulletCountView.visibility = VISIBLE
                bulletCountView.text = if (it.size > 99) "99+" else it.size.toString()
            } else {
                bulletCountView.visibility = GONE
            }
        }
    }

    // 卡片消失传 0
    fun liveCardChange(cardHeight: Int) {
        currentTranslationY = -cardHeight.toFloat()
        if (isKeyboardShow) return
        translation(currentTranslationY)
    }

    // 键盘消失传 0
    fun keyboardChange(height: Int) {
        if (height > 0) {
            isKeyboardShow = true
            val y = (BULLET_DEFAULT_MARGIN - height).toFloat()
            translation(y)
        } else {
            isKeyboardShow = false
            translation(currentTranslationY)
        }
    }

    private fun translation(y: Float) {
        animate().translationY(y)
            .setDuration(200)
            .start()
    }

    private fun checkAdapter() {
        if (listAdapter == null) throw RuntimeException("listAdapter is null")
    }
}