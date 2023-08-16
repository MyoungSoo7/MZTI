package com.mzc.mzti.profile.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.mzc.mzti.R
import com.mzc.mzti.common.session.MztiSession
import com.mzc.mzti.databinding.ItemUserProfileMbtiCardBinding
import com.mzc.mzti.model.data.mbti.getBackgroundProfileDrawableResId
import com.mzc.mzti.model.data.mbti.getProfileImgResId

class UserProfileMbtiCardViewHolder(
    private val binding: ItemUserProfileMbtiCardBinding,
    private val onCardSaveBtnClicked: (cardView: View) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.apply {
            ibMbtiCardDownload.setOnClickListener {
                onCardSaveBtnClicked(clMbtiCard)
            }
        }
    }

    fun bindData() {
        val pos = bindingAdapterPosition

        val mbti = MztiSession.userMbti
        val nickname = MztiSession.userNickname
        val id = MztiSession.userId
        val profileImg = MztiSession.userProfileImg
        binding.apply {
            val defaultProfileImgRes = getProfileImgResId(mbti)
            // 프로필 사진
            Glide.with(ivMbtiCardUserProfile.context)
                .load(profileImg)
                .transform(CircleCrop())
                .placeholder(defaultProfileImgRes)
                .fallback(defaultProfileImgRes)
                .error(defaultProfileImgRes)
                .into(ivMbtiCardUserProfile)
            // 닉네임
            tvMbtiCardUserName.text = nickname
            // MBTI
            tvMbtiCardUserMbti.text = mbti.name
            // ID
            tvMbtiCardUserId.text =
                tvMbtiCardUserId.context.getString(R.string.mbtiCard_id, id)

            clMbtiCard.setBackgroundResource(getBackgroundProfileDrawableResId(mbti))
            cvMbtiCardProfileBorder.updateColor(mbti)
        }
    }

}