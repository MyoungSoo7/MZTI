package com.mzc.mzti.profile.view

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mzc.mzti.R
import com.mzc.mzti.base.BaseFragment
import com.mzc.mzti.common.custom.WrapContentLinearLayoutManager
import com.mzc.mzti.databinding.FragmentUserProfileBinding
import com.mzc.mzti.main.viewmodel.MainViewModel
import com.mzc.mzti.model.data.router.MztiTabRouter
import com.mzc.mzti.profile.adapter.UserProfileAdapter

private const val TAG: String = "UserProfileFragment"

/**
 * 사용자 프로필 화면
 */
class UserProfileFragment : BaseFragment() {

    private val model: MainViewModel by activityViewModels()
    private var _binding: FragmentUserProfileBinding? = null
    private val binding: FragmentUserProfileBinding get() = _binding!!

    private lateinit var mAdapter: UserProfileAdapter

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
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    // endregion Fragment LifeCycle

    private fun init() {
        mAdapter = UserProfileAdapter().apply {
            userProfileListener = object : UserProfileAdapter.UserProfileListener {
                override fun saveMbtiCardToImg(cardView: View) {
                    val bmp = Bitmap.createBitmap(
                        cardView.measuredWidth,
                        cardView.measuredHeight,
                        Bitmap.Config.ARGB_8888
                    )
                    val canvas = Canvas(bmp)
                    cardView.draw(canvas)

                    model.requestSaveBitmap(bmp)
                }

                override fun editUserProfile() {

                }

                override fun connectMbtiTestSite() {

                }

                override fun letsGoMztiTest() {
                    model.setTabRouter(MztiTabRouter.TAB_LEARNING)
                }
            }
        }

        binding.apply {
            rvUserProfile.apply {
                layoutManager =
                    WrapContentLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = mAdapter
                itemAnimator = null
            }
        }
    }

}