package com.example.outgamble_android.util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object ClearFocusHelper {
    fun onEditText(context: Context, vararg edtText: EditText) {
        for (edt in edtText) {
            edt.clearFocus()
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(edt.windowToken, 0)
        }
    }
}