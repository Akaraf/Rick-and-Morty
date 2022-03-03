package com.raaf.rickandmorty.ui.utils

import android.view.View
import androidx.appcompat.widget.Toolbar

fun setToolbarTitle(toolbar: Toolbar, title: String) {
    toolbar.title = title
    toolbar.visibility = View.VISIBLE
}