package com.exz.wenzhoupeople.utils

import android.content.Context
import android.view.Gravity
import android.view.View
import com.common.controls.dialog.CoreDialog
import com.exz.wenzhoupeople.R
import kotlinx.android.synthetic.main.dialog_hint.view.*

/**
 * Created by 史忠文
 * on 2017/9/11.
 */
object KtDialog {
    /***
     *提示
     */
    fun hint(context: Context, msg: String, listener: (v: View) -> Unit): KtDialog {
        val inflate = View.inflate(context, R.layout.dialog_hint, null)
        val dlg = CoreDialog(context, com.common.alertpop.R.style.dialog, inflate, true)
        dlg.setPosition(Gravity.CENTER, 0, 0)
        dlg.setCanceledOnTouchOutside(true)
        inflate.msg.text = msg
        inflate.hint_cancel.setOnClickListener {
            dlg.dismiss()
        }
        inflate.hint_confirm.setOnClickListener {
            dlg.dismiss()
            listener.invoke(it)
        }
        dlg.show()
        return this
    }
}