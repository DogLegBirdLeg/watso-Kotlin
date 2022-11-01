package com.example.saengsaengtalk.fragmentAccount.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.saengsaengtalk.adapterHome.BaedalPreAdapter
import com.example.saengsaengtalk.databinding.LytAdminGroupBinding
import org.json.JSONArray
import org.json.JSONObject


class AdminGroupAdapter(val context: Context, val groups: JSONArray) : RecyclerView.Adapter<AdminGroupAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        println("그룹 어댑터 진입: ${groups}")
        val binding = LytAdminGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val group = groups.getJSONObject(position)
        holder.bind(group)

    }

    override fun getItemCount(): Int {
        return groups.length()
    }

    inner class CustomViewHolder(var binding: LytAdminGroupBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(group: JSONObject) {
            println("그룹 이너 클래스: ${group}")
            binding.tvGroupName.text = group.getString("name")
            binding.tvGroupId.text = group.getString("optionGroupId")
            binding.tvMin.text = group.getString("minOrderableQuantity")
            binding.tvMax.text = group.getString("maxOrderableQuantity")
            binding.rvOption.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.rvOption.setHasFixedSize(true)
            val optionAdapter = AdminOptionAdapter(group.getJSONArray("options"))
            binding.rvOption.adapter = optionAdapter
        }
    }

}