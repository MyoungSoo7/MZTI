package com.mzc.mzti.friends.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mzc.mzti.R
import com.mzc.mzti.base.BaseFragment
import com.mzc.mzti.databinding.FragmentFriendsBinding
import com.mzc.mzti.main.viewmodel.MainViewModel

private const val TAG: String = "FriendsFragment"

/**
 * 친구 리스트 화면
 */
class FriendsFragment : BaseFragment() {

    private val model: MainViewModel by activityViewModels()
    private var _binding: FragmentFriendsBinding? = null
    private val binding: FragmentFriendsBinding get() = _binding!!

    // region Fragment LifeCycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendsBinding.inflate(inflater, container, false)

        setObserver()
        init()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    // endregion Fragment LifeCycle

    private fun setObserver() {

    }

    private fun init() {

    }

}