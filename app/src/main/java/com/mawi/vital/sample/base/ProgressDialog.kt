package com.mawi.vital.sample.base

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.mawi.vital.sample.R

class ProgressDialog : DialogFragment() {

    companion object {
        @JvmStatic
        fun show(fragmentManager: FragmentManager): ProgressDialog {
            val progressDialog = ProgressDialog()
            progressDialog.show(fragmentManager, "ProgressDialog")
            return progressDialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val containerView = View.inflate(requireActivity(), R.layout.dialog_fragment_progress, null)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(containerView)
        return builder.create()
    }

    override fun isCancelable(): Boolean {
        return false
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        containerView = View.inflate(activity, layoutId, null)
//
//        val builder = AlertDialog.Builder(activity!!, style())
//        builder.setView(containerView)
//
//        val dialog = builder.create()
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCanceledOnTouchOutside(handleCancel())
//
//        dialog.setOnKeyListener { _, keyCode, _ ->
//
//            if (keyCode == KeyEvent.KEYCODE_BACK)
//                handleBack()
//
//            true
//        }
//
//        setupWindow(dialog.window)
//
//        onViewBound(containerView!!)
//
//        globalLayoutListeners.forEach {
//            it.onGlobalLayout()
//            containerView?.viewTreeObserver?.addOnGlobalLayoutListener(it)
//        }
//
//        return dialog
//    }
}