package com.sehatq.testapplication.feature.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment

open class BaseHomeFragment : Fragment() {
    lateinit var activity: HomeActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity = getActivity() as HomeActivity
    }
}