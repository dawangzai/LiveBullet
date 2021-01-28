package com.wangzai.bullet.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import com.wangzai.bullet.R

class InputDialog : DialogFragment() {

    private lateinit var imm: InputMethodManager
    private lateinit var viewContainer: FrameLayout
    private lateinit var etContent: EditText
    private lateinit var tvSend: TextView
    private val maxNumber = 30
    private var textSendListener: ((String) -> Unit)? = null

    // 窗口的默认可见高度
    private var windowDefaultVisibleHeight: Int = 0
    private val outRect = Rect()
    private var keyboardListener: ((Boolean, Int) -> Unit)? = null

    // 用于跳出回调事件
    private var isKeyboardShow = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.InputDialog)
        imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        restoreWindow()
        return inflater.inflate(R.layout.fragment_input_dialog, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        showSoftInput()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        keyboardListener = null
    }

    fun setOnTextSendListener(listener: (String) -> Unit) {
        this.textSendListener = listener
    }

    fun setKeyboardListener(listener: (Boolean, Int) -> Unit) {
        this.keyboardListener = listener
    }

    private fun initView(view: View) {
        viewContainer = view.findViewById(R.id.view_container)
        etContent = view.findViewById(R.id.et_content)
        tvSend = view.findViewById(R.id.tv_send)
        //修改下划线颜色
        etContent.background.colorFilter =
            PorterDuffColorFilter(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        etContent.doOnTextChanged { text, _, _, _ ->
            text?.let {
                if (it.length > maxNumber) {
                    etContent.setText(it.subSequence(0, maxNumber - 1))
                    etContent.setSelection(etContent.text.length)
                }
            }
        }
        etContent.doAfterTextChanged {
            it?.let { text ->
                tvSend.isEnabled = text.isNotEmpty()
            }
        }
        tvSend.setOnClickListener {
            textSendListener?.invoke(etContent.text.toString())
            etContent.setText("")
            keyboardListener?.invoke(false, 0)
            dismiss()
        }
        viewContainer.viewTreeObserver.addOnGlobalLayoutListener {
            if (windowDefaultVisibleHeight == 0) {
                windowDefaultVisibleHeight = getWindowVisibleHeight()
            }
            // 键盘弹起
            if (isKeyboardShow() && !isKeyboardShow) {
                isKeyboardShow = true
                keyboardListener?.invoke(true, getBottomMargin())
            }

            // 键盘消失
            if (!isKeyboardShow() && isKeyboardShow) {
                isKeyboardShow = false
                keyboardListener?.invoke(false, 0)
                dialog?.let {
                    if (it.isShowing) {
                        dismiss()
                    }
                }
            }
        }
        viewContainer.setOnClickListener {
            isKeyboardShow = false
            keyboardListener?.invoke(false, 0)
            dismiss()
        }
    }

    private fun restoreWindow() {
        dialog?.window?.let {
            it.decorView.setPadding(0, 0, 0, 0)
            it.attributes = it.attributes.apply {
                gravity = Gravity.BOTTOM
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.MATCH_PARENT
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                it.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                it.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
                it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                it.statusBarColor = Color.TRANSPARENT
            }
        }
    }

    private fun showSoftInput() {
        etContent.post {
            etContent.requestFocus()
            imm.showSoftInput(etContent, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    // 获取输入框距离屏幕底部的距离
    private fun getBottomMargin(): Int {
        return if (isKeyboardShow()) (getKeyboardHeight() + etContent.height) else 0
    }

    // 键盘的高度
    private fun getKeyboardHeight(): Int {
        return windowDefaultVisibleHeight - getWindowVisibleHeight()
    }

    // 键盘是否弹起
    private fun isKeyboardShow(): Boolean {
        return windowDefaultVisibleHeight - getWindowVisibleHeight() > 0
    }

    private fun getWindowVisibleHeight(): Int {
        dialog?.window?.decorView?.getWindowVisibleDisplayFrame(outRect)
        return outRect.bottom
    }
}