package com.mobile.cloudkitchen.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.NetworkError
import com.android.volley.NoConnectionError
import com.android.volley.ParseError
import com.android.volley.ServerError
import com.android.volley.TimeoutError
import com.android.volley.VolleyError
import com.mobile.cloudkitchen.databinding.ActivityLoginBinding
import com.mobile.cloudkitchen.service.APIService
import com.mobile.cloudkitchen.service.ServiceResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class LoginActivity : AppCompatActivity(), ServiceResponse {
    lateinit var loginBinding: ActivityLoginBinding
    var listener: ServiceResponse? = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)
        loginBinding.authenticateBtn.setOnClickListener {
            if (loginBinding.phNumEt.text.toString().length < 10) {
                Toast.makeText(applicationContext, "Enter valid mobile number!", Toast.LENGTH_LONG)
                    .show()
            } else {
                loginBinding.loginPbar.visibility = View.VISIBLE
                APIService.makeLoginOTPRequest(
                    this,
                    "LOGIN",
                    listener,
                    loginBinding.phNumEt.text.toString()
                )
            }
        }
        loginBinding.troubleLoginBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val recipients = arrayOf("MealBox987@gmail.com")
            intent.putExtra(Intent.EXTRA_EMAIL, recipients)
            intent.putExtra(Intent.EXTRA_SUBJECT, "Not able to login from "+Build.BRAND+"_${Build.MODEL}")
            intent.putExtra(Intent.EXTRA_TEXT, "Mobile number : "+loginBinding.phNumEt.text.toString())
            intent.putExtra(Intent.EXTRA_CC, "mailcc@gmail.com")
            intent.setType("text/html")
            intent.setPackage("com.google.android.gm")
            startActivity(Intent.createChooser(intent, "Send mail"))
        }
    }

    override fun onSuccessResponse(response: Any?, tag: Any?) {
        loginBinding.loginPbar.visibility = View.GONE
        if (tag.toString().contentEquals("LOGIN") || tag.toString().contentEquals("REGISTER")) {
            GlobalScope.launch(Dispatchers.Main) {
                var jsonObject = response as JSONObject
                Toast.makeText(applicationContext, "Sent OTP! ${jsonObject.getString("data")}", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(applicationContext, OTPActivity::class.java)
                intent.putExtra("phnum", loginBinding.phNumEt.text.toString())
                startActivity(intent)
            }
           // this.mtag = tag
           // binding.otpLayout.visibility = View.VISIBLE
        }
    }

    override fun onFailureResponse(error: VolleyError,tag: Any?) {
        loginBinding.loginPbar.visibility = View.GONE
        if (error is NetworkError) {
            Toast.makeText(applicationContext,
                "NetworkError!",
                Toast.LENGTH_LONG).show();
        } else if (error is ServerError) {
            Toast.makeText(applicationContext,
                "ServerError!",
                Toast.LENGTH_LONG).show();
        } else if (error is AuthFailureError) {
            Toast.makeText(applicationContext,
                "AuthFailureError!",
                Toast.LENGTH_LONG).show();
        } else if (error is ParseError) {
            Toast.makeText(applicationContext,
                "ParseError!",
                Toast.LENGTH_LONG).show();
        } else if (error is NoConnectionError) {
            Toast.makeText(applicationContext,
                "NoConnectionError!",
                Toast.LENGTH_LONG).show();
        } else if (error is TimeoutError) {
            Toast.makeText(applicationContext,
                "Oops. Timeout error!",
                Toast.LENGTH_LONG).show();
        }

        /* if (error.networkResponse!=null && error.networkResponse.statusCode!=null && error.networkResponse.statusCode == 400) {
            var msg = JSONArray(String(error.networkResponse.data, charset("UTF-8"))).getJSONObject(0)
                .getString("message")
            Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
        }*/
    }
}