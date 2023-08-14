package com.mzc.mzti.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mzc.mzti.R
import com.mzc.mzti.databinding.ItemUserProfileAppVersionBinding
import com.mzc.mzti.databinding.ItemUserProfileEditBinding
import com.mzc.mzti.databinding.ItemUserProfileMbtiBadgeBinding
import com.mzc.mzti.databinding.ItemUserProfileMbtiCardBinding
import com.mzc.mzti.databinding.ItemUserProfileMbtiTestBinding
import com.mzc.mzti.profile.viewholder.UserProfileAppVersionViewHolder
import com.mzc.mzti.profile.viewholder.UserProfileEditViewHolder
import com.mzc.mzti.profile.viewholder.UserProfileMbtiBadgeViewHolder
import com.mzc.mzti.profile.viewholder.UserProfileMbtiCardViewHolder
import com.mzc.mzti.profile.viewholder.UserProfileMbtiTestViewHolder

private const val TAG: String = "UserProfileAdapter"

class UserProfileAdapter(
    private val viewHolderList: List<UserProfileLayout> = listOf(
        UserProfileLayout.MBTI_CARD,
        UserProfileLayout.EDIT_PROFILE,
        UserProfileLayout.MBTI_TEST,
        UserProfileLayout.MBTI_BADGE,
        UserProfileLayout.APP_VERSION
    )
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var userProfileListener: UserProfileListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            // MBTI증
            R.layout.item_user_profile_mbti_card -> {
                UserProfileMbtiCardViewHolder(
                    ItemUserProfileMbtiCardBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                ) { cardView ->
                    userProfileListener?.saveMbtiCardToImg(cardView)
                }
            }

            // 프로필 수정하기
            R.layout.item_user_profile_edit -> {
                UserProfileEditViewHolder(
                    ItemUserProfileEditBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                ) {
                    userProfileListener?.editUserProfile()
                }
            }

            // MBTI 검사하러 가기
            R.layout.item_user_profile_mbti_test -> {
                UserProfileMbtiTestViewHolder(
                    ItemUserProfileMbtiTestBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                ) {
                    userProfileListener?.connectMbtiTestSite()
                }
            }

            // MBTI 뱃지
            R.layout.item_user_profile_mbti_badge -> {
                UserProfileMbtiBadgeViewHolder(
                    ItemUserProfileMbtiBadgeBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                ) {
                    userProfileListener?.letsGoMztiTest()
                }
            }

            // 앱 버전
            R.layout.item_user_profile_app_version -> {
                UserProfileAppVersionViewHolder(
                    ItemUserProfileAppVersionBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> {
                UserProfileAppVersionViewHolder(
                    ItemUserProfileAppVersionBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            // MBTI증
            R.layout.item_user_profile_mbti_card -> {

            }

            // 프로필 수정하기
            R.layout.item_user_profile_edit -> {

            }

            // MBTI 검사하러 가기
            R.layout.item_user_profile_mbti_test -> {

            }

            // MBTI 뱃지
            R.layout.item_user_profile_mbti_badge -> {

            }

            // 앱 버전
            R.layout.item_user_profile_app_version -> {
                if (holder is UserProfileAppVersionViewHolder) {
                    holder.bindData()
                }
            }

        }
    }

    override fun getItemCount(): Int = viewHolderList.size

    override fun getItemViewType(position: Int): Int = when (viewHolderList[position]) {
        UserProfileLayout.MBTI_CARD -> R.layout.item_user_profile_mbti_card
        UserProfileLayout.EDIT_PROFILE -> R.layout.item_user_profile_edit
        UserProfileLayout.MBTI_TEST -> R.layout.item_user_profile_mbti_test
        UserProfileLayout.MBTI_BADGE -> R.layout.item_user_profile_mbti_badge
        UserProfileLayout.APP_VERSION -> R.layout.item_user_profile_app_version
        else -> R.layout.item_user_profile_app_version
    }

    enum class UserProfileLayout {
        MBTI_CARD,

        EDIT_PROFILE,

        MBTI_TEST,

        MBTI_BADGE,

        APP_VERSION
    }

    interface UserProfileListener {
        /**
         * MBTI증을 이미지로 저장하는 함수
         */
        fun saveMbtiCardToImg(cardView: View)

        /**
         * 유저 프로필 수정
         */
        fun editUserProfile()

        /**
         * MBTI 테스트 사이트로 연결
         */
        fun connectMbtiTestSite()

        /**
         * MZTI 테스트 화면으로 연결
         */
        fun letsGoMztiTest()
    }

}