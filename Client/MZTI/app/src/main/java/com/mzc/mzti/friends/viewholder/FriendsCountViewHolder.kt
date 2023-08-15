package com.mzc.mzti.friends.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.mzc.mzti.R
import com.mzc.mzti.databinding.ItemFriendsCountBinding

class FriendsCountViewHolder(
    private val binding: ItemFriendsCountBinding
) : RecyclerView.ViewHolder(binding.root) {

    init {

    }

    fun bindData(pFriendsCnt: Int) {
        val pos = bindingAdapterPosition

        binding.apply {
            // 친구 N명
            tvFriendsCount.text =
                tvFriendsCount.context.getString(R.string.friends_count, pFriendsCnt)
        }
    }

}