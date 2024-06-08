package com.mobile.cloudkitchen.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mobile.cloudkitchen.data.viewmodels.KitchenDetailsVM
import com.mobile.cloudkitchen.databinding.ProfileFragmentBinding
import com.mobile.cloudkitchen.ui.activity.HomeActivity

class ProfileFragment : Fragment() {
    private var _binding: ProfileFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    lateinit var sp: SharedPreferences
    private val binding get() = _binding!!
    private lateinit var planType: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val kitchenDetailsViewModel =
            ViewModelProvider(this).get(KitchenDetailsVM::class.java)

        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        sp = requireActivity().getSharedPreferences("SP", Context.MODE_PRIVATE)
        binding.nameTxt.text = sp.getString("NAME","NA")
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as HomeActivity?)?.showHideBottomNavigation(false,false)
    }
    override fun onResume() {
        super.onResume()
        (activity as HomeActivity?)?.showHideBottomNavigation(false,false)
    }

    override fun onStop() {
        super.onStop()
        (activity as HomeActivity?)?.showHideBottomNavigation(true,true)
        (activity as HomeActivity?)?.resumeHomeSelection()
    }
}