package com.mobile.cloudkitchen.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.cloudkitchen.R
import com.mobile.cloudkitchen.data.model.Addresses


class AddressAdapter(private val mContext: Context, private var userAddreses: ArrayList<Addresses>, private var mCallBack: AdapterCallback) : RecyclerView.Adapter<AddressAdapter.ViewHolder>(){

    var callback: AdapterCallback? = mCallBack

    interface AdapterCallback {
        fun onItemClicked(position: Int)
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
      //  var amount: TextView
   //   lateinit var mealsImage : ImageView
      lateinit var addressTxt : TextView
     /*   var transactionId: TextView
        var orderId: TextView
        var paymentRef : TextView*/
        init {
         addressTxt = view.findViewById(R.id.address_txt)
            /*amount = view.findViewById(R.id.amount)
            statusIcon =view.findViewById(R.id.statusIcon)
            transactionId = view.findViewById(R.id.transactionId)
            orderId = view.findViewById(R.id.orderId)
            paymentRef = view.findViewById(R.id.paymentRef)*/
        }
    }


    override fun getItemCount(): Int {
        return userAddreses.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stringBuilder = StringBuilder()
        stringBuilder.append(userAddreses[position].houseNo)
        stringBuilder.append("\n")
        stringBuilder.append(userAddreses[position].addressLine1)
        stringBuilder.append("\n")
        stringBuilder.append(userAddreses[position].addressLine2)
        stringBuilder.append("\n")
        stringBuilder.append(userAddreses[position].city)
        stringBuilder.append("\n")
        stringBuilder.append(userAddreses[position].state)
        stringBuilder.append("\n")
        stringBuilder.append(userAddreses[position].pincode)
        holder.addressTxt.text = stringBuilder.toString()
        holder.itemView.setOnClickListener {
            callback?.onItemClicked(position)
        }
    }
}