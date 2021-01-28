package com.wangzai.bullet.adapter

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wangzai.bullet.R
import com.wangzai.bullet.entity.BULLET_TYPE_NOTICE
import com.wangzai.bullet.entity.Bullet

class BulletAdapter : RecyclerView.Adapter<BulletAdapter.ViewHolder>() {

    val listData = mutableListOf<Bullet>()

    fun addData(item: Bullet) {
        listData.add(item)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bullet, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bullet = listData[position]
        val builder = SpannableStringBuilder()
        if (bullet.msgType == BULLET_TYPE_NOTICE) {
            builder.append(bullet.content)
            val colorSpan = ForegroundColorSpan(Color.parseColor("#FFC323"))
            builder.setSpan(colorSpan, 0, bullet.content.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else {
            val userName = "${bullet.userName} : "
            builder.append(userName)
            builder.append(bullet.content)
            val colorSpan = ForegroundColorSpan(Color.parseColor("#FFC323"))
            builder.setSpan(colorSpan, 0, userName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        holder.contentView.text = builder
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contentView: TextView = itemView.findViewById(R.id.tv_bullet)
    }

}