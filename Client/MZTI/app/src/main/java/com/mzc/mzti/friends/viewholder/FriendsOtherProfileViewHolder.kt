package com.mzc.mzti.friends.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.mzc.mzti.R
import com.mzc.mzti.databinding.ItemFriendsOtherProfileBinding
import com.mzc.mzti.model.data.friends.FriendsOtherProfileData
import com.mzc.mzti.model.data.mbti.getProfileImgResId

class FriendsOtherProfileViewHolder(
    private val binding: ItemFriendsOtherProfileBinding,
    private val onFriendsItemClicked: (pos: Int) -> Unit,
    private val onFriendsItemLongClicked: (pos: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.apply {
            clOtherProfileItem.setOnClickListener {
                val pos = bindingAdapterPosition
                onFriendsItemClicked(pos)
            }

            clOtherProfileItem.setOnLongClickListener {
                val pos = bindingAdapterPosition
                onFriendsItemLongClicked(pos)
                true
            }
        }
    }

    fun bindData(pData: FriendsOtherProfileData) {
        val pos = bindingAdapterPosition

        binding.apply {
            val defaultProfileImgRes = getProfileImgResId(pData.mbti)
            // 프로필 사진
            Glide.with(ivOtherProfileItemImg.context)
                .load(pData.profileImg)
                .transform(CircleCrop())
                .placeholder(defaultProfileImgRes)
                .fallback(defaultProfileImgRes)
                .error(defaultProfileImgRes)
                .into(ivOtherProfileItemImg)
            // 닉네임
            tvOtherProfileItemNickname.text = pData.nickname
            // MBTI
            tvOtherProfileItemMbti.text = pData.mbti.name
        }
    }

}