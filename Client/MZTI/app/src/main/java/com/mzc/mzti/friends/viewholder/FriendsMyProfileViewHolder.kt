package com.mzc.mzti.friends.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.mzc.mzti.R
import com.mzc.mzti.databinding.ItemFriendsMyProfileBinding
import com.mzc.mzti.model.data.friends.FriendsMyProfileData

class FriendsMyProfileViewHolder(
    private val binding: ItemFriendsMyProfileBinding,
    private val onFriendsItemClicked: (pos: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.apply {
            clMyProfileItem.setOnClickListener {
                val pos = bindingAdapterPosition
                onFriendsItemClicked(pos)
            }
        }
    }

    fun bindData(pData: FriendsMyProfileData) {
        val pos = bindingAdapterPosition

        binding.apply {
            // 프로필 사진
            Glide.with(ivMyProfileItemImg.context)
                .load(pData.profileImg)
                .transform(CircleCrop())
                .placeholder(R.color.border)
                .fallback(R.color.border)
                .error(R.color.border)
                .into(ivMyProfileItemImg)
            // 닉네임
            tvMyProfileItemNickname.text = pData.nickname
            // MBTI
            tvMyProfileItemMbti.text = pData.mbti.name
        }
    }

}