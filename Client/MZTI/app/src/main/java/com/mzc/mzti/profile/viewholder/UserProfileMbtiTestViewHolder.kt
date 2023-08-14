package com.mzc.mzti.profile.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.mzc.mzti.databinding.ItemUserProfileMbtiTestBinding

class UserProfileMbtiTestViewHolder(
    private val binding: ItemUserProfileMbtiTestBinding,
    private val onMbtiTestBtnClicked: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.apply {
            clMbtiTest.setOnClickListener {
                onMbtiTestBtnClicked()
            }
        }
    }

    fun bindData() {

    }

}