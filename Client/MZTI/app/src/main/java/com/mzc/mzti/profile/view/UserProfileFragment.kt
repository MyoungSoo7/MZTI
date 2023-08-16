package com.mzc.mzti.profile.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mzc.mzti.R
import com.mzc.mzti.base.BaseFragment
import com.mzc.mzti.common.custom.WrapContentLinearLayoutManager
import com.mzc.mzti.common.dialog.MessageDialog
import com.mzc.mzti.databinding.FragmentUserProfileBinding
import com.mzc.mzti.main.viewmodel.MainViewModel
import com.mzc.mzti.model.data.router.MztiTabRouter
import com.mzc.mzti.profile.adapter.UserProfileAdapter
import com.mzc.mzti.profileedit.view.UserProfileEditActivity
import java.lang.Exception

private const val TAG: String = "UserProfileFragment"

/**
 * 사용자 프로필 화면
 */
class UserProfileFragment : BaseFragment() {

    private val model: MainViewModel by activityViewModels()
    private var _binding: FragmentUserProfileBinding? = null
    private val binding: FragmentUserProfileBinding get() = _binding!!

    private lateinit var mAdapter: UserProfileAdapter
    private lateinit var userProfileEditIntentForResult: ActivityResultLauncher<Intent>

    // region Fragment LifeCycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

        userProfileEditIntentForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        model.clearUserProfileData()
                        model.requestUserProfile()
                    }
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)

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
        model.userProfileData.observe(viewLifecycleOwner, Observer { userProfileData ->
            if (userProfileData != null) {
                model.setProgressFlag(false)
                mAdapter.update(userProfileData)
            }
        })
    }

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
                    val profileEditIntent =
                        Intent(requireContext(), UserProfileEditActivity::class.java)
                    userProfileEditIntentForResult.launch(profileEditIntent)
                }

                override fun connectMbtiTestSite() {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse("https://www.16personalities.com/ko")
                        requireContext().startActivity(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun letsGoMztiTest() {
                    model.setTabRouter(MztiTabRouter.TAB_LEARNING)
                }

                override fun logout() {
                    showLogoutDialog()
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

        model.requestUserProfile()
    }

    private fun showLogoutDialog() {
        for (fragment in childFragmentManager.fragments) {
            if (fragment is MessageDialog) {
                return
            }
        }

        MessageDialog(
            pMsg = getString(R.string.logout_dialog),
            pLeftBtnText = getString(R.string.logout_title),
            pLeftBtnClickListener = {
                model.setLogoutFlag(true)
            },
            pRightBtnText = getString(R.string.cancel_btn),
            pRightBtnClickListener = {
            }
        ).show(childFragmentManager, null)
    }

}