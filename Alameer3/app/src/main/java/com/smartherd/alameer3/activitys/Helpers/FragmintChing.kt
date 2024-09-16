package com.smartherd.alameer3.activitys.Helpers

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.smartherd.alameer3.R
import com.smartherd.alameer3.activitys.MainActivity

class FragmintChing(private val fragmentManager: FragmentManager, private val fragmentChangeListener: FragmentChangeListener) {
    fun ching(fragment: Fragment) {
        fragmentChangeListener.onFragmentChange(fragment)
    }
}