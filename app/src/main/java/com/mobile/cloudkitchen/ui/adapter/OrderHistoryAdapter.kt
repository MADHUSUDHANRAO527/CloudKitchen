package com.mobile.cloudkitchen.ui.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobile.cloudkitchen.R
import com.mobile.cloudkitchen.data.model.KitchensModel
import com.mobile.cloudkitchen.data.model.Orders
import com.mobile.cloudkitchen.ui.activity.HomeActivity
import com.mobile.cloudkitchen.ui.fragments.KitchenDetailsFragment

class OrderHistoryAdapter(private val mContext: Context, private var ordersList: ArrayList<Orders>) :
    RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.meals_list_row,
                parent,
                false
            )
        return ViewHolder(itemView)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //  var amount: TextView
        lateinit var mealsImage: ImageView
        lateinit var kitchenTitle: TextView
        lateinit var kitchenDesc: TextView
        lateinit var rating: TextView
        lateinit var startsFromPrice: TextView
        lateinit var deliveryTime: TextView
      lateinit  var kitchenCardView : CardView
        init {
            mealsImage = view.findViewById(R.id.meals_image)
            kitchenTitle = view.findViewById(R.id.kitchen_title_txt)
            kitchenDesc = view.findViewById(R.id.kitchen_title_desc)
            rating = view.findViewById(R.id.rating_txt)
            startsFromPrice =view.findViewById(R.id.cost_txt)
            deliveryTime = view.findViewById(R.id.delivery_time_txt)
            kitchenCardView = view.findViewById(R.id.kitchen_card_view)
        }
    }


    override fun getItemCount(): Int {
        return ordersList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.kitchenTitle.text = ordersList[position].createdAt
       /* holder.kitchenDesc.text = kitchensList[position].description
        Glide.with(mContext)
            .load(kitchensList[position].bannerImage)
            .into(holder.mealsImage)
        holder.rating.text =kitchensList[position].noOfRatings.toString()
        holder.startsFromPrice.text ="Starts from "+kitchensList[position].startingPrice.toString()+" "+mContext.resources.getString(R.string.Rs)
        holder.deliveryTime.text = "Delivery in 21min"
        holder.kitchenCardView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("kitchen_id", kitchensList[position]._id)
            bundle.putString("kitchen_title", kitchensList[position].title)
            bundle.putString("kitchen_desc", kitchensList[position].description)
            bundle.putString("kitchen_type", kitchensList[position].type)
            (mContext as HomeActivity?)?.loadFragment(KitchenDetailsFragment(), bundle)
        }*/
    }
}