package com.konradb.gameCalendar

import android.content.Intent
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment

abstract class BaseFragment: Fragment() {

    protected fun startApp(){
        val intent : Intent = Intent(requireContext(), MainActivity::class.java).apply{
            flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)
    }
}