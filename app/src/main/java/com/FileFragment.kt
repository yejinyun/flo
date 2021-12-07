package com

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentFileBinding
import com.example.flo.databinding.FragmentLockerBinding

class FileFragment: Fragment() {
    lateinit var binding: FragmentFileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFileBinding.inflate(inflater, container, false)

        return binding.root

    }
}