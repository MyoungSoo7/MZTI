package com.mzc.mzti.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mzc.mzti.R
import com.mzc.mzti.databinding.ItemUserProfileAppVersionBinding
import com.mzc.mzti.databinding.ItemUserProfileEditBinding
import com.mzc.mzti.databinding.ItemUserProfileLogoutBinding
import com.mzc.mzti.databinding.ItemUserProfileMbtiBadgeBinding
import com.mzc.mzti.databinding.ItemUserProfileMbtiCardBinding
import com.mzc.mzti.databinding.ItemUserProfileMbtiTestBinding
import com.mzc.mzti.profile.viewholder.UserProfileAppVersionViewHolder
import com.mzc.mzti.profile.viewholder.UserProfileEditViewHolder
import com.mzc.mzti.profile.viewholder.UserProfileLogoutViewHolder
import com.mzc.mzti.profile.viewholder.UserProfileMbtiBadgeViewHolder
import com.mzc.mzti.profile.viewholder.UserProfileMbtiCardViewHolder
import com.mzc.mzti.profile.viewholder.UserProfileMbtiTestViewHolder

private const val TAG: String = "UserProfileAdapter"

class UserProfileAdapter(
    private val viewHolderList: List<UserProfileLayoutType> = listOf(
        UserProfileLayoutType.MBTI_CARD,
        UserProfileLayoutType.EDIT_PROFILE,
        UserProfileLayoutType.MBTI_TEST,
        UserProfileLayoutType.MBTI_BADGE,
        UserProfileLayoutType.APP_VERSION
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

            // 로그아웃
            R.layout.item_user_profile_logout -> {
                UserProfileLogoutViewHolder(
                    ItemUserProfileLogoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                ) {
                    userProfileListener?.logout()
                }
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

            // 로그아웃
            R.layout.item_user_profile_logout -> {

            }

        }
    }

    override fun getItemCount(): Int = viewHolderList.size

    override fun getItemViewType(position: Int): Int = when (viewHolderList[position]) {
        UserProfileLayoutType.MBTI_CARD -> R.layout.item_user_profile_mbti_card
        UserProfileLayoutType.EDIT_PROFILE -> R.layout.item_user_profile_edit
        UserProfileLayoutType.MBTI_TEST -> R.layout.item_user_profile_mbti_test
        UserProfileLayoutType.MBTI_BADGE -> R.layout.item_user_profile_mbti_badge
        UserProfileLayoutType.APP_VERSION -> R.layout.item_user_profile_app_version
        UserProfileLayoutType.LOGOUT -> R.layout.item_user_profile_logout
        else -> R.layout.item_user_profile_app_version
    }

    enum class UserProfileLayoutType {
        MBTI_CARD,

        EDIT_PROFILE,

        MBTI_TEST,

        MBTI_BADGE,

        APP_VERSION,

        LOGOUT
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

        /**
         * 로그아웃
         */
        fun logout()
    }

}