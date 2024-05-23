package com.mobile.cloudkitchen.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.mobile.cloudkitchen.R
import com.mobile.cloudkitchen.databinding.ActivityHomeBinding
import com.mobile.cloudkitchen.ui.fragments.HomeFragment
import com.mobile.cloudkitchen.ui.fragments.LocationFragment
import com.mobile.cloudkitchen.utils.AppUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding
    private var fragmentList = ArrayList<Fragment>()
    lateinit var sp: SharedPreferences
    private lateinit var menuItem: MenuItem

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sp = this.getSharedPreferences("SP", Context.MODE_PRIVATE)
        FirebaseApp.initializeApp(this);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        FirebaseCrashlytics.getInstance().setCustomKey(
            "user_id", sp.getString("user_id", "NA").toString()
        )
        FirebaseCrashlytics.getInstance()
            .setCustomKey("device_model", Build.BRAND + "_${Build.MODEL}")
        //  FirebaseCrashlytics.getInstance().

        if (fragmentList.isEmpty())
            fragmentList.add(HomeFragment())
        else for (fragment in fragmentList) {
            if (!fragment.tag.equals("HomeFragment"))
                fragmentList.add(HomeFragment())
        }
        EventBus.getDefault().register(this)

        setSupportActionBar(binding.appBarHome.toolbar)
        /*  val window: Window = this.window
          window.addFlags(FLAG_SECURE)*/
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.appBarHome.toolbar.setTitle("")
        binding.appBarHome.toolbar.setSubtitle("")
        binding.appBarHome.toolbar.navigationIcon = null
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        setBottomMarginAsPerDisplay(navController)


        /*var typeface = ResourcesCompat.getFont(this, R.font.quick_sand_font_family)
        TextView.setTypeface(typeface)*/
        /*appBarConfiguration = AppBarConfiguration(
            setOf(

                R.id.nav_home,
                R.id.nav_notifications,
                R.id.nav_contact_us,
                R.id.nav_feedback,
                R.id.nav_about,
                R.id.nav_policy,
                R.id.nav_terms,
              //  R.id.nav_watch_history,
                R.id.nav_help
            ), binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        binding.navView.setNavigationItemSelectedListener(this)
        binding.navView.bringToFront()*/
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
                /* R.id.btm_nav_dining -> {
                     title = resources.getString(R.string.dining)
                    // loadFragment(LocationFragment(), null)
                 }*/
                R.id.btm_nav_profile -> {
                    title = resources.getString(R.string.profile)
                    // loadFragment(LocationFragment(), null)
                }
                /* R.id.btm_nav_profile -> {
                     title = resources.getString(R.string.profile)
                     loadFragment(ProfileFragment(), null)
                 }
                 R.id.btm_nav_collections -> {
                     title = resources.getString(R.string.collection)
                     loadFragment(CollectionFragment(), null)
                 }
                 R.id.btm_nav_settings -> {
                     title = resources.getString(R.string.action_settings)
                     loadFragment(SettingsFragment(), null)

                 }*/
            }
            return@setOnItemSelectedListener true
        }


    }

    private fun setBottomMarginAsPerDisplay(navController: NavController) {
        var wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager;
        var display = wm.defaultDisplay as Display;
        var dm = DisplayMetrics();
        display.getMetrics(dm);
        dm.widthPixels;
        /*if(dm.widthPixels>2000){
           // navController.
            val params: ViewGroup.LayoutParams = navController.getView().getLayoutParams()
            params.height = 900
            navController.navInflater.getView().setLayoutParams(params)
            findNavController(R.id.nav_host_fragment_content_home).
        }*/
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

    /*  override fun onNavigationItemSelected(item: MenuItem): Boolean {
          var bundle: Bundle
          when (item.itemId) {
              R.id.nav_notifications -> {
                  title = "Notifications"
                  loadFragment(ContactUsFragment(), null)
                  binding.drawerLayout.closeDrawer(GravityCompat.START)

              }
             *//* R.id.nav_watch_history -> {
                title = "Watch History"
                loadFragment(ContactUsFragment(), null)
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }*//*
            R.id.nav_contact_us -> {
                title = "Contact Us"
                val bundle = Bundle()
                bundle.putBoolean("isFromLogin", false)
                loadFragment(ContactUsFragment(), bundle)
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            R.id.nav_terms -> {
                title = "Terms and Conditions"
                val bundle = Bundle()
                bundle.putBoolean("isFromLogin", false)
                bundle.putString("text", "TermsandConditions")
                bundle.putString("tag", "Terms and Conditions")
                loadFragment(ContactUsFragment(), bundle)
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            R.id.nav_help -> {
                title = "Help"
                val bundle = Bundle()
                bundle.putBoolean("isFromLogin", false)
                loadFragment(ContactUsFragment(), bundle)
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            R.id.nav_about -> {
                title = "About Us"
                val bundle = Bundle()
                bundle.putString("text", "About_Us")
                bundle.putString("tag", "About Us")
                loadFragment(ContactUsFragment(), bundle)
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            R.id.nav_policy -> {
                title = "Privacy Policy"
                val bundle = Bundle()
                bundle.putString("text", "Privacy_Policy")
                bundle.putString("tag", "Privacy Policy")
                loadFragment(ContactUsFragment(), bundle)
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            R.id.nav_feedback -> {
                loadFragment(ContactUsFragment(), null)
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
        }
        showHideBottomNavigation(true, true)
        return false
    }*/

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
        binding.appBarHome.addressTxt.text = userAddress
    }

    fun popBack() {
        supportFragmentManager?.popBackStack()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val GPS_ON_CALLBACK = 100 //check GPS setting
        // Log.d("onActivityResult()", Integer.toString(resultCode))
        when (requestCode) {
            GPS_ON_CALLBACK -> {
                AppUtils.getLocation(this)
            }

            RESULT_OK -> {

                // All required changes were successfully made
                Toast.makeText(this, "Location enabled by user!", Toast.LENGTH_LONG).show()
                //    ContactFragment.isGPSEnabled = true
                //    ContactFragment.click()
            }

            RESULT_CANCELED -> {

                // The user was asked to change settings, but chose not to
                Toast.makeText(this, "Location not enabled, user cancelled.", Toast.LENGTH_LONG)
                    .show()
                //    ContactFragment.isGPSEnabled = false
            }

            else -> {}
        }
    }
}

