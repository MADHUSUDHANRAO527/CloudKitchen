package com.mobile.cloudkitchen.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.mobile.cloudkitchen.R
import com.mobile.cloudkitchen.data.model.Addresses
import com.mobile.cloudkitchen.service.APIService


class AddressAdapter(
    private val mContext: Context,
    private var userAddreses: ArrayList<Addresses>,
    private var mCallBack: AdapterCallback
) : RecyclerView.Adapter<AddressAdapter.ViewHolder>() {

    var callback: AdapterCallback? = mCallBack

    interface AdapterCallback {
        fun onItemClicked(position: Int, isDelete: Boolean)
        fun onItemClick(type: String, address: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.address_grid_row,
                parent,
                false
            )
        return ViewHolder(itemView)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var addressTxt: TextView
        var addressTypeTxt: TextView
        var editDeleteLayout: RelativeLayout
        var shareLayout: RelativeLayout
        var addressLayout: LinearLayout

        init {
            addressTxt = view.findViewById(R.id.address_txt)
            addressTypeTxt = view.findViewById(R.id.address_type)
            editDeleteLayout = view.findViewById(R.id.edit_delete_layout)
            shareLayout = view.findViewById(R.id.share_layout)
            addressLayout = view.findViewById(R.id.address_layout)
        }
    }


    override fun getItemCount(): Int {
        return userAddreses.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stringBuilder = StringBuilder()
        val userAddress = userAddreses[position]
        stringBuilder.append(userAddress.houseNo)
        stringBuilder.append(",")
        stringBuilder.append(userAddress.addressLine1)
        stringBuilder.append(",")
        stringBuilder.append(userAddress.addressLine2)
        stringBuilder.append(",")
        stringBuilder.append(userAddress.city)
        stringBuilder.append(",")
        stringBuilder.append(userAddress.state)
        stringBuilder.append(",")
        stringBuilder.append(userAddress.pincode)
        holder.addressTypeTxt.text = userAddress.type
        holder.addressTxt.text = stringBuilder.toString()
        holder.editDeleteLayout.setOnClickListener {
            showPopup(holder.editDeleteLayout, position)
        }
        holder.addressLayout.setOnClickListener {
            callback?.onItemClick(userAddress.type.toString(), stringBuilder.toString())
        }
        holder.addressLayout.setOnClickListener {
            val shareText = Intent(Intent.ACTION_SEND)
            shareText.type = "text/plain"
            val dataToShare = stringBuilder.toString()
            shareText.putExtra(Intent.EXTRA_SUBJECT, "Address:")
            shareText.putExtra(Intent.EXTRA_TEXT, dataToShare)
            mContext.startActivity(Intent.createChooser(shareText, "Share Via"))
        }
    }

    private fun showPopup(view: View, position: Int) {
        val popup = PopupMenu(mContext, view)
        popup.inflate(R.menu.edit_delete_menu)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                R.id.edit -> {
                    callback?.onItemClicked(position, false)
                }

                R.id.delete -> {
                    callback?.onItemClicked(position, true)
                }
            }
            true
        })

        popup.show()
    }
}