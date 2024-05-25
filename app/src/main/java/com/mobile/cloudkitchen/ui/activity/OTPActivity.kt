package com.mobile.cloudkitchen.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.android.volley.AuthFailureError
import com.android.volley.ClientError
import com.android.volley.NetworkError
import com.android.volley.NoConnectionError
import com.android.volley.ParseError
import com.android.volley.ServerError
import com.android.volley.TimeoutError
import com.android.volley.VolleyError
import com.google.gson.Gson
import com.mobile.cloudkitchen.R
import com.mobile.cloudkitchen.data.model.UserInfo
import com.mobile.cloudkitchen.databinding.ActivityOtpBinding
import com.mobile.cloudkitchen.service.APIService
import com.mobile.cloudkitchen.service.ServiceResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class OTPActivity : AppCompatActivity(), ServiceResponse {
    lateinit var binding: ActivityOtpBinding
    private val SPLASH_TIME_OUT: Long = 100
    var listener: ServiceResponse? = this
    var phNumber: String = ""
    lateinit var sp: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var countTime : CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sp = getSharedPreferences("SP",
            Context.MODE_PRIVATE)
        editor = sp.edit()
        if (intent.extras?.containsKey("phnum") != null)
            phNumber = intent.extras?.getString("phnum").toString()
        binding.phNumEt.setText(phNumber)
        countDown()
        binding.otpFirstDigit.setOnKeyListener OnKeyListener@{ view, i, keyEvent ->
            if (i === KeyEvent.KEYCODE_DEL) {
                binding.otpFirstDigit.setText("")
                return@OnKeyListener true
            } else {
                binding.otpFirstDigit.doAfterTextChanged {
                    if (binding.otpFirstDigit.text?.isEmpty() == true) {
                        binding.otpFirstDigit.requestFocus()
                    } else {
                        binding.otpSecondDigit.requestFocus()
                    }
                }
            }
            false
        }
        binding.otpFirstDigit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (binding.otpFirstDigit.text?.isEmpty() == true) {
                    binding.otpFirstDigit.requestFocus()
                } else {
                    binding.otpSecondDigit.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        binding.otpSecondDigit.setOnKeyListener OnKeyListener@{ view, i, keyEvent ->
            if (i === KeyEvent.KEYCODE_DEL) {
                binding.otpSecondDigit.setText("")
                binding.otpFirstDigit.requestFocus()
                return@OnKeyListener true
            } else {
                binding.otpSecondDigit.doAfterTextChanged {
                    if (binding.otpSecondDigit.text?.isEmpty() == true) {
                        binding.otpSecondDigit.requestFocus()
                    } else {
                        binding.otpThirdDigit.requestFocus()
                    }
                }
            }
            false
        }
        binding.otpSecondDigit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (binding.otpSecondDigit.text?.isEmpty() == true) {
                    binding.otpSecondDigit.requestFocus()
                } else {
                    binding.otpThirdDigit.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        binding.otpThirdDigit.setOnKeyListener OnKeyListener@{ view, i, keyEvent ->
            if (i === KeyEvent.KEYCODE_DEL) {
                binding.otpThirdDigit.setText("")
                binding.otpSecondDigit.requestFocus()
                return@OnKeyListener true
            } else {
                binding.otpThirdDigit.doAfterTextChanged {
                    if (binding.otpThirdDigit.text?.isEmpty() == true) {
                        binding.otpThirdDigit.requestFocus()
                    } else {
                        binding.otpFourthDigit.requestFocus()
                    }
                }
            }
            false
        }
        binding.otpThirdDigit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (binding.otpThirdDigit.text?.isEmpty() == true) {
                    binding.otpThirdDigit.requestFocus()
                } else {
                    binding.otpFourthDigit.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

       /* binding.otpFourthDigit.setOnKeyListener OnKeyListener@{ view, i, keyEvent ->
            if (i === KeyEvent.KEYCODE_DEL) {
                binding.otpFourthDigit.setText("")
                binding.otpThirdDigit.requestFocus()
                return@OnKeyListener true
            } else {
                binding.otpFourthDigit.doAfterTextChanged {
                    if (binding.otpFourthDigit.text?.isEmpty() == false) {
                        binding.otpFourthDigit.requestFocus()
                        Handler().postDelayed({
                            binding.otpPBar.visibility =View.VISIBLE
                            APIService.makeVerifyOTPRequest(
                                this,
                                "VERIFYOTP",
                                listener,
                                phNumber,
                                binding.otpFirstDigit.text.toString() + binding.otpSecondDigit.text.toString() + binding.otpThirdDigit.text.toString() + binding.otpFourthDigit.text.toString()
                            )
                        }, SPLASH_TIME_OUT)
                    }
                }
            }
            false
        }*/
        binding.otpFourthDigit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Handler().postDelayed({
                    if (!binding.otpFirstDigit.text.toString()
                            .isBlank() && !binding.otpSecondDigit.text.toString().isBlank() &&
                        !binding.otpThirdDigit.text.toString()
                            .isBlank() && !binding.otpFourthDigit.text.toString().isBlank()
                    ) {
                       // binding.otpPBar.visibility =View.VISIBLE
                        /*APIService.makeVerifyOTPRequest(
                            applicationContext,
                            "VERIFYOTP",
                            listener,
                            phNumber,
                            binding.otpFirstDigit.text.toString() + binding.otpSecondDigit.text.toString() + binding.otpThirdDigit.text.toString() + binding.otpFourthDigit.text.toString()
                        )*/
                    }
                }, SPLASH_TIME_OUT)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        binding.verifyBtn.setOnClickListener {
            val otp = binding.otpFirstDigit.text.toString() + binding.otpSecondDigit.text.toString() + binding.otpThirdDigit.text.toString() + binding.otpFourthDigit.text.toString()
            if (otp.length < 4)
                Toast.makeText(this, "Incorrect OTP " + otp, Toast.LENGTH_SHORT).show()
            else {
                binding.otpPBar.visibility =View.VISIBLE
                APIService.makeVerifyOTPRequest(
                    applicationContext,
                    "VERIFYOTP",
                    listener,
                    phNumber, otp
                )
            }
        }
        binding.resendOtp.setOnClickListener {
            countDown()
            binding.otpPBar.visibility =View.VISIBLE
            APIService.makeLoginOTPRequest(
                this,
                "LOGIN",
                listener,
                binding.phNumEt.text.toString()
            )
        }
    }
    private fun countDown(){
        countTime = object : CountDownTimer(30000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                binding.resendOtp.setText("Resend Otp in " + millisUntilFinished / 1000)
                binding.resendOtp.isClickable = false
                binding.resendOtp.setTextColor(ContextCompat.getColor(this@OTPActivity, R.color.seek_bar_background))
            }

            override fun onFinish() {
                binding.resendOtp.isClickable = true
                binding.resendOtp.setText("Resend Otp")
                binding.resendOtp.setTextColor(ContextCompat.getColor(this@OTPActivity, R.color.colorLightColor))
            }
        }.start()
    }
    override fun onSuccessResponse(response: Any?, tag: Any?) {
        binding.otpPBar.visibility =View.GONE
        if(tag.toString().contentEquals("VERIFYOTP")) {
            if (response is JSONObject) {
                val json = response
                val dataJson = json.getJSONObject("data")
                editor.putString("USER_INFO",dataJson.toString())
                editor.putString("MOBILE_NUMBER", dataJson.getString("mobileNumber"));
                editor.putString("USERID", dataJson.getString("_id"));
                editor.putBoolean("IS_APP_ADMIN", dataJson.getBoolean("isAppAdmin"));
                editor.putString("USER_TYPE", dataJson.getString("userType"));
                editor.putString("NAME", dataJson.getString("name"));
                editor.putString(
                    "TOKEN",
                    dataJson.getJSONObject("jwtToken").getString("accessToken")
                );
                editor.putString(
                    "EXPIRES_IN",
                    dataJson.getJSONObject("jwtToken").getString("expiresIn")
                );
                editor.apply()
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(applicationContext, json.getString("message"), Toast.LENGTH_LONG)
                        .show()
                    val intent = Intent(applicationContext, HomeActivity::class.java)
                    startActivity(intent)
                }
            }
        }else{
            GlobalScope.launch(Dispatchers.Main) {
                var jsonObject = response as JSONObject
                Toast.makeText(applicationContext, "Sent OTP! ${jsonObject.getString("data")}", Toast.LENGTH_LONG)
                    .show()
            }
        }

        // this.mtag = tag
        // binding.otpLayout.visibility = View.VISIBLE
    }

    override fun onFailureResponse(error: VolleyError, tag: Any?) {
        binding.otpPBar.visibility =View.GONE
        if (error is ClientError) {
            var msg = (JSONObject(
                String(
                    error.networkResponse.data,
                    charset("UTF-8")
                )
            ).getString("error"))
            Toast.makeText(
                applicationContext,
                msg,
                Toast.LENGTH_LONG
            ).show();
        } else if (error is NetworkError) {
            Toast.makeText(
                applicationContext,
                "NetworkError!",
                Toast.LENGTH_LONG
            ).show();
        } else if (error is ServerError) {
            Toast.makeText(
                applicationContext,
                "ServerError!",
                Toast.LENGTH_LONG
            ).show();
        } else if (error is AuthFailureError) {
            Toast.makeText(
                applicationContext,
                "AuthFailureError!",
                Toast.LENGTH_LONG
            ).show();
        } else if (error is ParseError) {
            Toast.makeText(
                applicationContext,
                "ParseError!",
                Toast.LENGTH_LONG
            ).show();
        } else if (error is NoConnectionError) {
            Toast.makeText(
                applicationContext,
                "NoConnectionError!",
                Toast.LENGTH_LONG
            ).show();
        } else if (error is TimeoutError) {
            Toast.makeText(
                applicationContext,
                "Oops. Timeout error!",
                Toast.LENGTH_LONG
            ).show();
        } else if (error is ClientError) {
            Toast.makeText(
                applicationContext,
                "Oops. ClientError",
                Toast.LENGTH_LONG
            ).show();
        }
        /* var json = (JSONObject(String(error.networkResponse.data, charset("UTF-8"))).getJSONObject("error"))
         json.get("status")*/
        /* if (error.networkResponse!=null && error.networkResponse.statusCode!=null && error.networkResponse.statusCode == 400) {
            var msg = JSONArray(String(error.networkResponse.data, charset("UTF-8"))).getJSONObject(0)
                .getString("message")
            Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
        }*/
    }
}