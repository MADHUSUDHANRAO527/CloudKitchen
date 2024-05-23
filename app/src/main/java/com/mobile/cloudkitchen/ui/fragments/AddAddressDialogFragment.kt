package com.mobile.cloudkitchen.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.VolleyError
import com.mobile.cloudkitchen.data.model.Addresses
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mobile.cloudkitchen.R
import com.mobile.cloudkitchen.databinding.AddAddressBottomsheetBinding
import com.mobile.cloudkitchen.service.APIService
import com.mobile.cloudkitchen.service.ServiceResponse
import com.mobile.cloudkitchen.utils.UserUtils
import org.json.JSONObject


class AddAddressDialogFragment(fragment: LocationFragment, mAddress: Addresses?) : BottomSheetDialogFragment(), ServiceResponse {
    private var _binding: AddAddressBottomsheetBinding? = null
    private val binding get() = _binding!!
    private val mFragment : LocationFragment = fragment
    private val address : Addresses? = mAddress


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
            } else{
                if (address!=null && UserUtils.getUserInfo().addresses.isNotEmpty())
                    UserUtils.getUserInfo().addresses.remove(address)
                var address = Addresses(binding.buildingNameTxt.text.toString(),binding.areaTxt.text.toString(),
                    binding.landmarkTxt.text.toString(),binding.cityTxt.text.toString(),"Telangana",binding.zipcodeTxt.text.toString(),"India")
                APIService.addUserAddress(requireActivity(),"ADD_ADDRESS",this,address)
            }
        }
        return root;
    }

    override fun onSuccessResponse(response: Any?, tag: Any?) {
        val json = response as JSONObject
        Toast.makeText(requireActivity(), json.get("message").toString(), Toast.LENGTH_LONG)
            .show()
        mFragment.refreshAddress()
        dismiss()
    }

    override fun onFailureResponse(error: VolleyError, tag: Any?) {
        Toast.makeText(requireActivity(), error.message, Toast.LENGTH_LONG)
            .show()
    }

    fun editAddress(address: Addresses) {
        binding.fullNameTxt.setText("")
        binding.phNumEt.setText("")
        binding.buildingNameTxt.setText(address.houseNo)
        binding.areaTxt.setText(address.addressLine1)
        binding.landmarkTxt.setText(address.addressLine2)
        binding.zipcodeTxt.setText(address.pincode)
        binding.cityTxt.setText(address.city)

    }
}