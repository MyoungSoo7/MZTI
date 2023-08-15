package com.mzc.mzti.friends.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.mzc.mzti.R
import com.mzc.mzti.databinding.ItemFriendsOtherProfileBinding
import com.mzc.mzti.model.data.friends.FriendsOtherProfileData

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
            // 프로필 사진
            Glide.with(ivOtherProfileItemImg.context)
                .load(pData.profileImg)
                .transform(CircleCrop())
                .placeholder(R.color.border)
                .fallback(R.color.border)
                .error(R.color.border)
                .into(ivOtherProfileItemImg)
            // 닉네임
            tvOtherProfileItemNickname.text = pData.nickname
            // MBTI
            tvOtherProfileItemMbti.text = pData.mbti.name
        }
    }

}