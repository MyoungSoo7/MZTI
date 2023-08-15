package com.mzc.mzti.friends.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mzc.mzti.R
import com.mzc.mzti.base.BaseFragment
import com.mzc.mzti.common.custom.WrapContentLinearLayoutManager
import com.mzc.mzti.common.session.MztiSession
import com.mzc.mzti.databinding.DialogRemoveFriendBinding
import com.mzc.mzti.databinding.FragmentFriendsBinding
import com.mzc.mzti.friends.adapter.FriendsAdapter
import com.mzc.mzti.main.viewmodel.MainViewModel
import com.mzc.mzti.model.data.friends.FriendsDataWrapper
import com.mzc.mzti.model.data.friends.FriendsLayoutType
import com.mzc.mzti.model.data.friends.FriendsMyProfileData
import com.mzc.mzti.model.data.friends.FriendsOtherProfileData
import com.mzc.mzti.model.data.mbti.MBTI

private const val TAG: String = "FriendsFragment"

/**
 * 친구 리스트 화면
 */
class FriendsFragment : BaseFragment() {

    private val model: MainViewModel by activityViewModels()
    private var _binding: FragmentFriendsBinding? = null
    private val binding: FragmentFriendsBinding get() = _binding!!

    private val removeFriendDialog: BottomSheetDialog by lazy {
        val bottomSheetView = layoutInflater.inflate(
            R.layout.dialog_remove_friend,
            null
        )
        DialogRemoveFriendBinding.bind(bottomSheetView).apply {
            tvRemoveFriendConfirm.setOnClickListener {
                model.requestRemoveFriend()
                removeFriendDialog.dismiss()
            }
            tvRemoveFriendCancel.setOnClickListener {
                removeFriendDialog.dismiss()
            }
        }
        BottomSheetDialog(requireContext(), R.style.TransparentBottomSheetDialog).apply {
            setContentView(bottomSheetView)
            window?.setBackgroundDrawableResource(R.color.transparent)
        }
    }

    private lateinit var mAdapter: FriendsAdapter
    private lateinit var addFriendIntentForResult: ActivityResultLauncher<Intent>

    // region Fragment LifeCycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

        addFriendIntentForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        result.data?.let { intent ->
                            val otherProfileData =
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    intent.getSerializableExtra(
                                        KEY_OTHER_PROFILE_DATA,
                                        FriendsOtherProfileData::class.java
                                    )!!
                                } else {
                                    intent.getSerializableExtra(KEY_OTHER_PROFILE_DATA) as FriendsOtherProfileData
                                }

                            model.requestAddFriend(otherProfileData)
                            showToastMsg(
                                getString(
                                    R.string.addFriends_add_success,
                                    otherProfileData.nickname
                                )
                            )
                        }
                    }
                }
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
        model.friendsList.observe(viewLifecycleOwner, Observer { friendsList ->
            if (friendsList.isNotEmpty()) {
                model.setProgressFlag(false)
                mAdapter.update(friendsList)
            }
        })
    }

    private fun init() {
        mAdapter = FriendsAdapter(
            arrayListOf(
                FriendsDataWrapper(
                    FriendsLayoutType.MY_PROFILE,
                    FriendsMyProfileData(
                        nickname = MztiSession.userNickname,
                        mbti = MztiSession.userMbti,
                        profileImg = MztiSession.userProfileImg
                    )
                )
            )
        ).apply {
            friendsListener = object : FriendsAdapter.FriendsListener {
                override fun showMbtiInfo(mbti: MBTI) {

                }

                override fun showRemoveFriendsDialog(friendId: String) {
                    if (!removeFriendDialog.isShowing) {
                        model.setRemoveFriendId(friendId)
                        removeFriendDialog.show()
                    }
                }
            }
        }

        binding.apply {
            ibFriendsAdd.setOnClickListener {
                showAddFriendActivity()
            }

            rvFriends.apply {
                layoutManager =
                    WrapContentLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = mAdapter
                itemAnimator = null
            }
        }

        model.requestFriendsList()
    }

    private fun showAddFriendActivity() {
        val addFriendIntent = Intent(requireContext(), AddFriendActivity::class.java)
        addFriendIntentForResult.launch(addFriendIntent)
    }

}