package com.example.activityresult

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.activityresult.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_result)
        binding.apply {
            text = intent.getStringExtra(DATA)
            clickListener = this@ResultActivity.clickListener
        }
    }

    private val clickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.buttonFinish -> {
                setResult(RESULT_OK, Intent().apply { putExtra(DATA, view.tag as String) })
                finish()
            }
        }
    }
}