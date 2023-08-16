package com.mzc.mzti.friends.view

import android.os.Build
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.mzc.mzti.R
import com.mzc.mzti.base.BaseActivity
import com.mzc.mzti.base.BaseViewModel
import com.mzc.mzti.databinding.ActivityFriendMbtiBinding
import com.mzc.mzti.friends.viewmodel.FriendMbtiViewModel
import com.mzc.mzti.model.data.compare.CompareMbtiData
import com.mzc.mzti.model.data.compare.CompareMbtiType
import com.mzc.mzti.model.data.friends.FriendsOtherProfileData
import com.mzc.mzti.model.data.mbti.MBTI
import com.mzc.mzti.model.data.mbti.getProfileImgResId
import com.mzc.mzti.model.data.user.UserInfoData

private const val TAG: String = "FriendMbtiActivity"

const val KEY_MBTI: String = "mbti"
const val KEY_FRIEND_INFO: String = "friend_info"

class FriendMbtiActivity : BaseActivity() {

    private val model: FriendMbtiViewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModel.Factory(application)
        )[FriendMbtiViewModel::class.java]
    }
    private val binding: ActivityFriendMbtiBinding by lazy {
        ActivityFriendMbtiBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.enter_from_right_200, R.anim.exit_to_left_200)

        setContentView(binding.root)

        val mbti: MBTI
        val friendInfoData: FriendsOtherProfileData
        // 백그라운드 종료되었다가 다시 실행된 경우
        if (savedInstanceState != null) {
            savedInstanceState.apply {
                mbti = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    getSerializable(KEY_MBTI, MBTI::class.java)!!
                } else {
                    getSerializable(KEY_MBTI) as MBTI
                }

                friendInfoData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    getSerializable(KEY_FRIEND_INFO, FriendsOtherProfileData::class.java)!!
                } else {
                    getSerializable(KEY_FRIEND_INFO) as FriendsOtherProfileData
                }
            }
        }
        // 그 외의 일반적인 경우
        else {
            intent.apply {
                mbti = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    getSerializableExtra(KEY_MBTI, MBTI::class.java)!!
                } else {
                    getSerializableExtra(KEY_MBTI) as MBTI
                }

                friendInfoData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    getSerializableExtra(KEY_FRIEND_INFO, FriendsOtherProfileData::class.java)!!
                } else {
                    getSerializableExtra(KEY_FRIEND_INFO) as FriendsOtherProfileData
                }
            }
        }

        model.init(mbti, friendInfoData)
        setObserver()
        init()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.enter_from_left_200, R.anim.exit_to_right_200)
    }

    private fun setObserver() {
        model.progressFlag.observe(this, Observer { flag ->
            if (flag) {
                showProgress()
            } else {
                dismissProgress()
            }
        })

        model.exceptionData.observe(this, Observer { exception ->
            exception.message?.let { msg ->
                model.setProgressFlag(false)
                showErrorMsg()
            }
        })

        model.apiFailMsg.observe(this, Observer { msg ->
            model.setProgressFlag(false)
            showToastMsg(msg)
        })

        model.toastMsg.observe(this, Observer { msg ->
            showToastMsg(msg)
        })

        model.mbtiInfoResult.observe(this, Observer { mbtiInfoResult ->
            model.setProgressFlag(false)
            updateLayout(mbtiInfoResult)
        })
    }

    private fun init() {
        binding.apply {
            ibFriendMbtiBack.setOnClickListener {
                finish()
            }
            val mbti = model.friendInfo.mbti
            val defaultProfileImgResId = getProfileImgResId(mbti)
            Glide.with(ivFriendMbtiProfileImg)
                .load(model.friendInfo.profileImg)
                .transform(CircleCrop())
                .fallback(defaultProfileImgResId)
                .error(defaultProfileImgResId)
                .into(ivFriendMbtiProfileImg)
            tvFriendMbtiTitle.text = getString(R.string.friendMbti_title, model.friendInfo.nickname)
            tvFriendMbtiUserName.text = model.friendInfo.nickname
            tvFriendMbtiUserMbti.text = model.friendInfo.mbti.name
        }

        model.requestMbtiInfo()
    }

    private fun updateLayout(pMbtiInfoResult: List<CompareMbtiData>) {
        for (mbtiInfo in pMbtiInfoResult) {
            when (mbtiInfo.type) {
                CompareMbtiType.KEYWORD -> {
                    binding.cvFriendMbtiKeyword.setKeywordList(mbtiInfo.content, model.mbti)
                }

                CompareMbtiType.DESCRIPTION -> {
                    binding.cvFriendMbtiDescription.setCompareMbtiData(mbtiInfo, model.mbti)
                }

                else -> {

                }
            }
        }
    }

}