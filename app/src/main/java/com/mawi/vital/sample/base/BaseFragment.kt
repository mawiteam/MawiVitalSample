package com.mawi.vital.sample.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

open class BaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId) {

    private var progressDialog: ProgressDialog? = null

    protected fun showProgressDialog() {
        if (progressDialog != null)
            progressDialog?.dismissAllowingStateLoss()
        progressDialog = ProgressDialog.show(parentFragmentManager)
    }

    protected fun hideProgressDialog() {
        progressDialog?.dismissAllowingStateLoss()
        progressDialog = null
    }
}