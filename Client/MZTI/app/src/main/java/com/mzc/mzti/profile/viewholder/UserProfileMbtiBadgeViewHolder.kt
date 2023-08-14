package com.mzc.mzti.profile.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.mzc.mzti.databinding.ItemUserProfileMbtiBadgeBinding

class UserProfileMbtiBadgeViewHolder(
    private val binding: ItemUserProfileMbtiBadgeBinding,
    private val onLetsGoTestBtnClicked: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.clMbtiBadgeLetsGoTest.setOnClickListener {
            onLetsGoTestBtnClicked()
        }
    }

    fun bindData() {

    }

}