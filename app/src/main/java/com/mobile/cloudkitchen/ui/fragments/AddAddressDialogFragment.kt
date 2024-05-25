package com.mobile.cloudkitchen.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.android.volley.VolleyError
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mobile.cloudkitchen.data.model.Addresses
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mobile.cloudkitchen.R
import com.mobile.cloudkitchen.databinding.AddAddressBottomsheetBinding
import com.mobile.cloudkitchen.service.APIService
import com.mobile.cloudkitchen.service.ServiceResponse
import com.mobile.cloudkitchen.utils.AppUtils
import com.mobile.cloudkitchen.utils.UserUtils
import org.json.JSONObject


class AddAddressDialogFragment(fragment: LocationFragment, mAddress: Addresses?) :
    BottomSheetDialogFragment(), ServiceResponse {
    private var _binding: AddAddressBottomsheetBinding? = null
    private val binding get() = _binding!!
    private val mFragment: LocationFragment = fragment
    private val address: Addresses? = mAddress
    private val behavior by lazy { (dialog as BottomSheetDialog).behavior }

    override fun onStart() {
        super.onStart()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        inflater.inflate(
            R.layout.add_address_bottomsheet,
            container, false
        )
        _binding = AddAddressBottomsheetBinding.inflate(inflater, container, false)
        val root: View = binding.root
        if (address != null) {
            editAddress(address)
        }
        _binding!!.homeTxt.setOnClickListener {
           selectHomeType()
        }
        binding.workTxt.setOnClickListener {
           selectWorkType()
        }
        binding.hotelTxt.setOnClickListener {
            selectHotelType()
        }
        binding.otherTxt.setOnClickListener {
            selectOtherType()
        }
        binding.addAddressBtn.setOnClickListener {
            // APIService.addUserAddress()
            if (binding.fullNameTxt.text.toString().isEmpty()) {
                Toast.makeText(requireActivity(), "Enter full name", Toast.LENGTH_LONG)
                    .show()
            } else if (binding.phNumEt.text.toString().isEmpty()) {
                Toast.makeText(requireActivity(), "Enter phone number", Toast.LENGTH_LONG)
                    .show()
            } else if (binding.buildingNameTxt.text.toString().isEmpty()) {
                Toast.makeText(requireActivity(), "Enter building name/ No.", Toast.LENGTH_LONG)
                    .show()
            } else if (binding.areaTxt.text.toString().isEmpty()) {
                Toast.makeText(requireActivity(), "Enter area!", Toast.LENGTH_LONG)
                    .show()
            } else if (binding.landmarkTxt.text.toString().isEmpty()) {
                Toast.makeText(requireActivity(), "Enter Landmark", Toast.LENGTH_LONG)
                    .show()
            } else if (binding.zipcodeTxt.text.toString().isEmpty()) {
                Toast.makeText(requireActivity(), "Enter zipcode!", Toast.LENGTH_LONG)
                    .show()
            } else if (binding.cityTxt.text.toString().isEmpty()) {
                Toast.makeText(requireActivity(), "Enter city!", Toast.LENGTH_LONG)
                    .show()
            } else {
                if (address != null && UserUtils.getUserInfo().addresses.isNotEmpty())
                    UserUtils.getUserInfo().addresses.remove(address)
                val address = Addresses(
                    binding.typeOfAddressTxt.tag.toString(),
                    binding.fullNameTxt.text.toString(),
                    binding.phNumEt.text.toString(),
                    binding.buildingNameTxt.text.toString(),
                    binding.areaTxt.text.toString(),
                    binding.landmarkTxt.text.toString(),
                    binding.cityTxt.text.toString(),
                    "Telangana",
                    binding.zipcodeTxt.text.toString(),
                    "India"
                )
                APIService.addEditDeleteUserAddress(
                    requireActivity(),
                    "ADD_ADDRESS",
                    this,
                    address,
                    false
                )
            }
        }
        return root;
    }

    private fun selectOtherType() {
        binding.othersLayout.visibility = View.VISIBLE
        binding.typeOfAddressTxt.tag = _binding!!.othersEt.text
        binding.otherTxt.setBackground(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.plan_btn_bg_shape
            )
        );
        binding.otherTxt.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white));
        binding.workTxt.setBackground(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.address_type_bg
            )
        );
        binding.workTxt.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black));
        binding.homeTxt.setBackground(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.address_type_bg
            )
        );
        binding.homeTxt.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black));
        binding.hotelTxt.setBackground(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.address_type_bg
            )
        );
        binding.hotelTxt.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black));
    }

    private fun selectHotelType() {
        binding.othersLayout.visibility = View.GONE
        binding.typeOfAddressTxt.tag = _binding!!.hotelTxt.text
        binding.hotelTxt.setBackground(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.plan_btn_bg_shape
            )
        );
        binding.hotelTxt.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white));
        binding.workTxt.setBackground(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.address_type_bg
            )
        );
        binding.workTxt.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black));
        binding.homeTxt.setBackground(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.address_type_bg
            )
        );
        binding.homeTxt.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black));
        binding.otherTxt.setBackground(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.address_type_bg
            )
        );
        binding.otherTxt.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black));
    }

    private fun selectWorkType() {
        binding.othersLayout.visibility = View.GONE
        binding.typeOfAddressTxt.tag = _binding!!.workTxt.text
        binding.workTxt.setBackground(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.plan_btn_bg_shape
            )
        );
        binding.workTxt.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white));
        binding.homeTxt.setBackground(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.address_type_bg
            )
        );
        binding.homeTxt.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black));
        binding.hotelTxt.setBackground(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.address_type_bg
            )
        );
        binding.hotelTxt.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black));
        binding.otherTxt.setBackground(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.address_type_bg
            )
        );
        binding.otherTxt.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black));
    }

    private fun selectHomeType() {
        binding.othersLayout.visibility = View.GONE
        binding.typeOfAddressTxt.tag = _binding!!.homeTxt.text
        binding.homeTxt.setBackground(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.plan_btn_bg_shape
            )
        );
        binding.homeTxt.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white));
        binding.workTxt.setBackground(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.address_type_bg
            )
        );
        binding.workTxt.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black));
        binding.hotelTxt.setBackground(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.address_type_bg
            )
        );
        binding.hotelTxt.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black));
        binding.otherTxt.setBackground(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.address_type_bg
            )
        );
        binding.otherTxt.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black));
    }

    override fun onSuccessResponse(response: Any?, tag: Any?) {
        val json = response as JSONObject
        /*Toast.makeText(requireActivity(), json.get("message").toString(), Toast.LENGTH_LONG)
            .show()*/
        mFragment.refreshAddress()
        dismiss()
    }

    override fun onFailureResponse(error: VolleyError, tag: Any?) {
        AppUtils.showErrorMsg(error, tag.toString(), requireActivity())
    }

    private fun editAddress(address: Addresses) {
        if (address.type?.contains("Home") == true) {
            selectHomeType()
        } else if (address.type?.contains("Work") == true) {
            selectWorkType()
        } else if (address.type?.contains("Hotel") == true) {
            selectHotelType()
        } else {
            _binding!!.othersEt.setText(address.type)
            selectOtherType()
        }
        binding.fullNameTxt.setText(address.fullName)
        binding.phNumEt.setText(address.phoneNumber)
        binding.buildingNameTxt.setText(address.houseNo)
        binding.areaTxt.setText(address.addressLine1)
        binding.landmarkTxt.setText(address.addressLine2)
        binding.zipcodeTxt.setText(address.pincode)
        binding.cityTxt.setText(address.city)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //   behavior.isFitToContents = false
        //   behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
}