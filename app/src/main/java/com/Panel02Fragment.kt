package com

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentPanel02Binding

class Panel02Fragment : Fragment() {
    lateinit var binding: FragmentPanel02Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPanel02Binding.inflate(inflater, container, false)

        return binding.root

    }
}