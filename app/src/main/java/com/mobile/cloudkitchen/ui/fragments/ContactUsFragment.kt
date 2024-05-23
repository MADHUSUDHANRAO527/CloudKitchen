package com.mobile.cloudkitchen.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mobile.cloudkitchen.databinding.FragmentContactUsBinding


class ContactUsFragment : Fragment() {

    private var _binding: FragmentContactUsBinding? = null
    private val binding get() = _binding!!
    private var number : String = " "
    private var mail : String = " "

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactUsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }
}