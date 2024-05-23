package com.mobile.cloudkitchen.ui.activity


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobile.cloudkitchen.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    lateinit var sp: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sp = getSharedPreferences(
            "SP",
            Context.MODE_PRIVATE
        )
        if (sp.getString("TOKEN", "")?.isNotBlank() == true) {
            finish()
            startActivity(Intent(this, HomeActivity::class.java))
        }

        binding.getStartedBtn.setOnClickListener {
            callIntent()
        }
    }

    private fun callIntent() {
        if (sp.getString("TOKEN", "NA")?.contentEquals("NA") == true)
            startActivity(Intent(this, LoginActivity::class.java))
        else
            startActivity(Intent(this, HomeActivity::class.java))

    }
}