package com.mzc.mzti.main.view

import android.os.Bundle
import com.mzc.mzti.R
import com.mzc.mzti.base.BaseActivity
import com.mzc.mzti.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

}