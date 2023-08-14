package com.mzc.mzti.profile.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.mzc.mzti.BuildConfig
import com.mzc.mzti.R
import com.mzc.mzti.databinding.ItemUserProfileAppVersionBinding

class UserProfileAppVersionViewHolder(
    private val binding: ItemUserProfileAppVersionBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bindData() {
        binding.apply {
            val context = tvAppVersionVersionName.context
            tvAppVersionVersionName.text =
                context.getString(R.string.appVersion_versionName, BuildConfig.VERSION_NAME)
        }
    }

}