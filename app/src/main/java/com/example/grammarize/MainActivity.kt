package com.example.grammarize

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.grammarize.R.layout.activity_main
import com.example.grammarize.R.string.gpt3

class MainActivity : AppCompatActivity() {
	val gpt3APIKey = getString(gpt3)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(activity_main)
	}
}
