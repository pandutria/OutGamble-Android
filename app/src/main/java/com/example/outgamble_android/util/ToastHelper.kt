package com.example.outgamble_android.util

import android.content.Context
import com.mhadikz.toaster.Toaster

object ToastHelper {
    fun success(context: Context, title: String, desc: String) {
        Toaster.Builder(context)
            .setTitle(title)
            .setDescription(desc)
            .setDuration(Toaster.LENGTH_SHORT)
            .setStatus(Toaster.Status.SUCCESS)
            .show()
    }
    fun warning(context: Context) {
        Toaster.Builder(context)
            .setTitle("Semua input wajib di isi!")
            .setDescription(" Lengkapi data terlebih dahulu agar proses dapat dilanjutkan.")
            .setDuration(Toaster.LENGTH_SHORT)
            .setStatus(Toaster.Status.WARNING)
            .show()
    }
    fun info(context: Context, title: String, desc: String) {
        Toaster.Builder(context)
            .setTitle(title)
            .setDescription(desc)
            .setDuration(Toaster.LENGTH_SHORT)
            .setStatus(Toaster.Status.INFO)
            .show()
    }
    fun error(context: Context, title: String, desc: String) {
        Toaster.Builder(context)
            .setTitle(title)
            .setDescription(desc)
            .setDuration(Toaster.LENGTH_SHORT)
            .setStatus(Toaster.Status.ERROR)
            .show()
    }
}