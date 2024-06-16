package com.mobile.cloudkitchen.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.mobile.cloudkitchen.R
import com.mobile.cloudkitchen.data.events.PaymentSuccessEvent
import com.mobile.cloudkitchen.databinding.ActivityHomeBinding
import com.mobile.cloudkitchen.ui.fragments.HomeFragment
import com.mobile.cloudkitchen.ui.fragments.LocationFragment
import com.mobile.cloudkitchen.ui.fragments.ProfileFragment
import com.mobile.cloudkitchen.utils.AppUtils
import com.razorpay.PaymentData
import com.razorpay.PaymentResultListener
import com.razorpay.PaymentResultWithDataListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode



class HomeActivity : BaseActivity(), PaymentResultWithDataListener, PaymentResultListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding
    private var fragmentList = ArrayList<Fragment>()

    //  lateinit var sp: SharedPreferences
    private lateinit var menuItem: MenuItem
    public var listner : (()-> Unit)? = null
    private var locationPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        FirebaseCrashlytics.getInstance().setCustomKey(
            "user_id", sp.getString("user_id", "NA").toString()
        )
        FirebaseCrashlytics.getInstance()
            .setCustomKey("device_model", Build.BRAND + "_${Build.MODEL}")

        if (fragmentList.isEmpty())
            fragmentList.add(HomeFragment())
        else for (fragment in fragmentList) {
            if (!fragment.tag.equals("HomeFragment"))
                fragmentList.add(HomeFragment())
        }
        if (getSelectedAddress()?.contains("*") == true) {
            binding.appBarHome.typeOfAddressTv.text = "Home"
            binding.appBarHome.addressTxt.text = getSelectedAddress()?.split("*")!![1]
        } else
            binding.appBarHome.addressTxt.text = getSelectedAddress()?.split("*")!![0]

        if (binding.appBarHome.addressTxt.text.toString().contains("Click")) {
            val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                getLocation()
            } else {
                AppUtils.enableGPSLocation(this)
            }
        }

        EventBus.getDefault().register(this)

        setSupportActionBar(binding.appBarHome.toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.appBarHome.toolbar.setTitle("")
        binding.appBarHome.toolbar.setSubtitle("")
        binding.appBarHome.toolbar.navigationIcon = null
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        setBottomMarginAsPerDisplay(navController)

        binding.appBarHome.addressLayout.setOnClickListener {
            loadFragment(LocationFragment(), null)
        }
        binding.appBarHome.bottomNavigation.setOnItemSelectedListener {
            this.menuItem = it
            when (it.itemId) {
                R.id.btm_nav_order -> {
                    val bundle = Bundle()
                    bundle.putBoolean("dummy_call", true)
                    title = resources.getString(R.string.menu_home)
                    loadFragment(HomeFragment(), null)
                }

                R.id.btm_nav_profile -> {
                    title = resources.getString(R.string.profile)
                    loadFragment(ProfileFragment(), null)
                }
            }
            return@setOnItemSelectedListener true
        }


    }
    fun resumeHomeSelection(){
        binding.appBarHome.bottomNavigation.selectedItemId = R.id.btm_nav_order
    }

    private fun setBottomMarginAsPerDisplay(navController: NavController) {
        var wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager;
        var display = wm.defaultDisplay as Display;
        var dm = DisplayMetrics();
        display.getMetrics(dm);
        dm.widthPixels;
    }

    fun loadFragment(fragment: Fragment, bundle: Bundle?) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(
            R.id.nav_host_fragment_content_home,
            fragment,
            fragment.javaClass.simpleName
        )
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss()
        fragment.arguments = bundle
        fragmentList.add(fragment)
    }

    fun replaceFragment(fragment: Fragment, bundle: Bundle?) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.nav_host_fragment_content_home,
            fragment,
            fragment.javaClass.simpleName
        )
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss()
        fragment.arguments = bundle
        fragmentList.add(fragment)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun showHideBottomNavigation(makeVisibleBN: Boolean, makeVisibleTB: Boolean) {
        if (makeVisibleBN && makeVisibleTB) {
            binding.appBarHome.bottomAppBar.visibility = View.VISIBLE
            binding.appBarHome.toolbar.visibility = View.VISIBLE
        } else if (makeVisibleBN && !makeVisibleTB) {
            binding.appBarHome.bottomAppBar.visibility = View.VISIBLE
            binding.appBarHome.toolbar.visibility = View.GONE
        } else if (makeVisibleTB) {
            binding.appBarHome.bottomAppBar.visibility = View.GONE
            binding.appBarHome.toolbar.visibility = View.VISIBLE
        } else {
            binding.appBarHome.bottomAppBar.visibility = View.GONE
            binding.appBarHome.toolbar.visibility = View.GONE
        }

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(userAddress: String?) {
        // Do something
        saveAddress(userAddress.toString())
        binding.appBarHome.typeOfAddressTv.text = userAddress?.split("*")!![0]
        binding.appBarHome.addressTxt.text = userAddress?.split("*")!![1]
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val GPS_ON_CALLBACK = 100 //check GPS setting
        // Log.d("onActivityResult()", Integer.toString(resultCode))
        when (requestCode) {
            GPS_ON_CALLBACK -> {
                //  AppUtils.getLocation(this)
                getLocation()
            }

            RESULT_OK -> {
                Toast.makeText(this, "Location enabled by user!", Toast.LENGTH_LONG).show()
            }

            RESULT_CANCELED -> {
                Toast.makeText(this, "Location not enabled, user cancelled.", Toast.LENGTH_LONG)
                    .show()
            }

            else -> {}
        }
    }

    private val permissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value == true
            }
            permissions.entries.forEach {
                Log.e("LOG_TAG", "${it.key} = ${it.value}")
            }

            if (granted) {
                getLocation()
                // your code if permission granted
            } else {
                AppUtils.showToast(this, "You denied permission!")
                // your code if permission denied
            }
        }

    private fun saveAddress(fullAddressLine: String) {
        editor.putString(
            "SELECTED_ADDRESS",
            fullAddressLine
        )
        editor.commit()
        editor.apply()
    }

    private fun getSelectedAddress(): String? {
        return sp.getString("SELECTED_ADDRESS", "Click here to add address!")
    }

    override fun onResume() {
        super.onResume()
    }
    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        Toast.makeText(this,"Payment Successful : Payment ID: $p0\nPayment Data: ${p1?.data}",Toast.LENGTH_LONG).show()

    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Toast.makeText(this,"Payment Failed : Payment Data: ${p2?.data}",Toast.LENGTH_LONG).show()
    }

    override fun onPaymentSuccess(p0: String?) {
        EventBus.getDefault().post(PaymentSuccessEvent(p0!!))
        Toast.makeText(this,"Payment Success : $p0}",Toast.LENGTH_LONG).show()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this,"Payment Failed : Payment Data: ${p1}",Toast.LENGTH_LONG).show()
    }
}

interface PaymentTrigger{
    fun paymentSuccess(id:String )
}
