package com.mobile.cloudkitchen.ui.fragments

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.VolleyError
import com.example.example.CreateOrder
import com.google.gson.Gson
import com.mobile.cloudkitchen.R
import com.mobile.cloudkitchen.data.model.ReviewOrder
import com.mobile.cloudkitchen.data.viewmodels.KitchenDetailsVM
import com.mobile.cloudkitchen.databinding.FragmentPlaceOrderBinding
import com.mobile.cloudkitchen.service.APIService
import com.mobile.cloudkitchen.service.ServiceResponse
import com.mobile.cloudkitchen.ui.activity.HomeActivity
import com.mobile.cloudkitchen.utils.AppUtils
import com.mobile.cloudkitchen.utils.UserUtils
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultListener
import com.razorpay.PaymentResultWithDataListener
import okhttp3.internal.Internal.instance
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject


class PlaceOrderFragment : Fragment(), ServiceResponse {
    private var _binding: FragmentPlaceOrderBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    lateinit var sp: SharedPreferences
    private val binding get() = _binding!!
    private lateinit var planType: String
    private var createOrder = CreateOrder()
    private var processOrder = ReviewOrder()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val kitchenDetailsViewModel =
            ViewModelProvider(this).get(KitchenDetailsVM::class.java)

        _binding = FragmentPlaceOrderBinding.inflate(inflater, container, false)
        val root: View = binding.root
        sp = requireActivity().getSharedPreferences("SP", Context.MODE_PRIVATE)
        setAddress()
        binding.deliveryChargesTxt.text =
            requireActivity().resources.getString(R.string.Rs) + "0.00"
        binding.grandTotalTxt.text =
            requireActivity().resources.getString(R.string.Rs) + UserUtils.getReviewOrder().grandTotal.toString()
        binding.mealTitleTxt.text = UserUtils.getMeal().name
        binding.mealTitle.text = UserUtils.getMeal().name
        binding.mealTitleDesc.text = UserUtils.getMeal().description
        //  binding.mealsCostTxt.text = onlyMealCost

        "${UserUtils.fromHumanDate}-${UserUtils.toHumanDate}".also {
            binding.planDurationTxt.text = it
        }
        UserUtils.timeSlot.also { binding.deliverySlotTxt.text = it }
        _binding!!.paynowBtn.setOnClickListener {

          /*  val razorpay = RazorpayClient("[YOUR_KEY_ID]", "[YOUR_KEY_SECRET]")

            val orderRequest = JSONObject()
            orderRequest.put("amount", 50000)
            orderRequest.put("currency", "INR")
            orderRequest.put("receipt", "receipt#1")
            val notes = JSONObject()
            notes.put("notes_key_1", "Tea, Earl Grey, Hot")
            notes.put("notes_key_1", "Tea, Earl Grey, Hot")
            orderRequest.put("notes", notes)

            val order: Order = instance.orders.create(orderRequest)
*/




         //   startPayment1()
            startPayment()
         //   prepareProcessOrderModel()
         //   AppUtils.showToast(requireActivity(), "will navigate to payment screen!")
        }
        binding.changeDurationTxt.setOnClickListener {
            (requireActivity() as HomeActivity?)?.loadFragment(SelectDurationFragment(), null)
        }
        binding.changeSubscription.setOnClickListener {
            (requireActivity() as HomeActivity?)?.loadFragment(SelectPlanFragment(), null)
        }
        binding.changeAddressTxt.setOnClickListener {
            AppUtils.isFromHome = false
            (requireActivity() as HomeActivity?)?.loadFragment(LocationFragment(), null)
        }
        _binding?.backIcon?.setOnClickListener {
            (activity as HomeActivity?)?.popBack()
        }
        APIService.processOrder(
            requireActivity(),
            this, "/orders/processOrder"
        )
        return root
    }

    private fun startPayment1() {
     /*   val payloadHelper = PayloadHelper("INR", 100, "order_XXXXXXXXX")
        payloadHelper.name = "Gaurav Kumae"
        payloadHelper.description = "Description"
        payloadHelper.prefillEmail = "gaurav.kumar@example.com"
        payloadHelper.prefillContact = "9000090000"
        payloadHelper.prefillCardNum = "4111111111111111"
        payloadHelper.prefillCardCvv = "111"
        payloadHelper.prefillCardExp = "11/24"
        payloadHelper.prefillMethod = "card"
        payloadHelper.prefillName = "MerchantName"
        payloadHelper.sendSmsHash = true
        payloadHelper.retryMaxCount = 4
        payloadHelper.retryEnabled = true
        payloadHelper.color = "#000000"
        payloadHelper.allowRotation = true
        payloadHelper.rememberCustomer = true
        payloadHelper.timeout = 10
        payloadHelper.redirect = true
        payloadHelper.recurring = "1"
        payloadHelper.subscriptionCardChange = true
        payloadHelper.customerId = "cust_1221"
        payloadHelper.callbackUrl = "https://accepts-posts.request"
        payloadHelper.subscriptionId = "sub_2123"
        payloadHelper.modalConfirmClose = true
        payloadHelper.backDropColor = "#ffffff"
        payloadHelper.hideTopBar = true
        payloadHelper.notes = JSONObject("{\"remarks\":\"Discount to cusomter\"}")
        payloadHelper.readOnlyEmail = true
        payloadHelper.readOnlyContact = true
        payloadHelper.readOnlyName = true
        payloadHelper.image = "https://www.razorpay.com"
        // these values are set mandatorily during object initialization. Those values can be overridden like this
        payloadHelper.amount=100
        payloadHelper.currency="INR"
        payloadHelper.orderId = "21237832"*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    private fun setAddress() {
        try {
            binding.deliverAtTxt.text =
                sp.getString("SELECTED_ADDRESS", "Click here to add address!").toString()
                    .split("*")[0]
            binding.addressTxt.text =
                sp.getString("SELECTED_ADDRESS", "Click here to add address!").toString()
                    .split("*")[1]
        } catch (e: IndexOutOfBoundsException) {
            binding.deliverAtTxt.text = "Add or Select Address!"
            binding.addressTxt.text = sp.getString("SELECTED_ADDRESS", "")
            e.printStackTrace()
        }
    }

    //TODO
    private fun prepareProcessOrderModel() {

        val jsonObject = JSONObject()
        val gson = Gson()

        jsonObject.put("user",UserUtils.getUserID(requireActivity()))
        jsonObject.put("kitchen",UserUtils.getKitchen().Id)
        jsonObject.put("meal",UserUtils.getMeal().Id)
        jsonObject.put("plan",UserUtils.planId)
        jsonObject.put("status","PLACED")
        jsonObject.put("deliveryInstructions","Adding sample data : ")
     //   jsonObject.put("","NA")
        jsonObject.put("paymentType", "UPI")
        jsonObject.put("savedAmount",processOrder.savedAmount)
        jsonObject.put("totalAmount",processOrder.totalAmount)
        jsonObject.put("deliveryCharges",0.0)
        jsonObject.put("grandTotal",processOrder.grandTotal)
        jsonObject.put("deliveryAddress",gson.toJson(UserUtils.getSelectedAddress()))
        jsonObject.put("isPaymentDone",true)
        jsonObject.put("planStartDate",UserUtils.fromDate)
        jsonObject.put("planEndDate",UserUtils.toDate)
        jsonObject.put("deliveryTimeSlot",UserUtils.timeSlot)
        APIService.callCreateOrder(
            requireActivity(),
            "CREATE_ORDER",
            this,jsonObject

        )
    }

    private fun startPayment() {
        /*
        *  You need to pass the current activity to let Razorpay create CheckoutActivity
        * */
        Checkout.preload(requireActivity())
        val activity: Activity = requireActivity()
        val co = Checkout()
        co.setKeyID("rzp_test_CLECjNaCmPM9pH")
        try {
            val options = JSONObject()
            options.put("name", "Razorpay Corp")
            options.put("description", "Demoing Charges")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.pn")
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
         //   options.put("order_id", "order_DBJOWzybf0sJbb");
            options.put(
                "amount",
                UserUtils.getReviewOrder().grandTotal.toString()
            )//pass amount in currency subunits

          /*  val retryObj = JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);*/

            val prefill = JSONObject()
            prefill.put("email", "madhurao527@gmail.com")
            prefill.put("contact", "6300592930")

            options.put("prefill", prefill)
            co.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onSuccessResponse(response: Any?, tag: Any?) {
        //{"totalAmount":0,"plannedDates":[],"discount":"10%","grandTotal":0,"savedAmount":0}
        //{"totalAmount":4257,"plannedDates":["2024-06-05","2024-06-06","2024-06-07","2024-06-10","2024-06-11","2024-06-12","2024-06-13","2024-06-14","2024-06-17","2024-06-18","2024-06-19","2024-06-20","2024-06-21","2024-06-24","2024-06-25","2024-06-26","2024-06-27","2024-06-28","2024-07-01","2024-07-02","2024-07-03","2024-07-04","2024-07-05","2024-07-08","2024-07-09","2024-07-10","2024-07-11","2024-07-12","2024-07-15","2024-07-16","2024-07-17","2024-07-18","2024-07-19","2024-07-22","2024-07-23","2024-07-24","2024-07-25","2024-07-26","2024-07-29","2024-07-30","2024-07-31","2024-08-01","2024-08-02"],
        // "discount":"5%","grandTotal":4044.15,"savedAmount":212.85000000000002}
        var gson = Gson()
        if (tag.toString().contains("CREATE_ORDER")) {
            Log.d("SUCCESS:", "")
            var jsonObject = response as JSONObject
            AppUtils.showToast(requireActivity(),jsonObject.getString("message"))
            (requireActivity() as HomeActivity?)?.loadFragment(HomeFragment(), null)

            //{
            //  "message": "Your order placed successfully"
            //}
        } else if (tag.toString().contains("processOrder")) {
            var processOrder = gson.fromJson(
                response.toString(),
                ReviewOrder::class.java
            )
            UserUtils.setReviewOrder(processOrder)
            val onlyMealCost =
                requireActivity().resources.getString(R.string.Rs) + processOrder.totalAmount.toString()
            val mealsCostTxt = onlyMealCost +
                    "(" + if (UserUtils.planType.contains("W")) UserUtils.getKitchen().availablePlans[0].noOfMeals.toString() else UserUtils.getKitchen().availablePlans[1].noOfMeals.toString()
            val savedAmountTxt =
                requireActivity().resources.getString(R.string.Rs) + processOrder.savedAmount.toString()

            binding.planTypeBtn.text = if (UserUtils.planType.contains("W")) "WEEKLY" else "MONTHLY"
            binding.mealsCostTxt.text = mealsCostTxt
            binding.totalSavingTxt.text = savedAmountTxt
            binding.subscriptionCostTxt.text = onlyMealCost
        }
        _binding?.pBar?.visibility = View.GONE
    }

    override fun onFailureResponse(error: VolleyError, tag: Any?) {
        _binding?.pBar?.visibility = View.GONE
        AppUtils.showErrorMsg(error, tag.toString(), requireActivity())
    }

   /* override fun onPaymentSuccess(p0: String?,p1:PaymentData) {
        Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()

    }*/

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(userAddress: String?) {
        setAddress()
        (activity as HomeActivity?)?.showHideBottomNavigation(false, false)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }


}