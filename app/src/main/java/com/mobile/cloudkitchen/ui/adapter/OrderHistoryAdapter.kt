package com.mobile.cloudkitchen.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobile.cloudkitchen.R
import com.mobile.cloudkitchen.data.model.Orders
import com.mobile.cloudkitchen.utils.AppUtils

class OrderHistoryAdapter(private val mContext: Context, private var ordersList: List<Orders>) :
    RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.orders_list_row,
                parent,
                false
            )
        return ViewHolder(itemView)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //  var amount: TextView
        lateinit var mealImage: ImageView
        lateinit var mealTitle: TextView
        lateinit var planType: TextView
        lateinit var orderDateTimeTxt: TextView
        lateinit var orderAmountTxt: TextView
        lateinit var statusTxt: TextView
        lateinit var deliveryTime: TextView
      lateinit  var kitchenCardView : CardView
        init {
            mealImage = view.findViewById(R.id.meal_img)
            mealTitle = view.findViewById(R.id.meal_title_txt)
            planType = view.findViewById(R.id.plan_type_txt)
            orderDateTimeTxt = view.findViewById(R.id.order_date_time_txt)
            orderAmountTxt =view.findViewById(R.id.order_amount_txt)
            statusTxt =view.findViewById(R.id.status_txt)

            /*  rating = view.findViewById(R.id.rating_txt)
              startsFromPrice =view.findViewById(R.id.cost_txt)
              deliveryTime = view.findViewById(R.id.delivery_time_txt)
              kitchenCardView = view.findViewById(R.id.kitchen_card_view)*/
        }
    }


    override fun getItemCount(): Int {
        return ordersList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mealTitle.text = ordersList[position].meal!!.name
        Glide.with(mContext)
            .load(ordersList[position].meal?.images!![0])
            .into(holder.mealImage)
        holder.planType.text = ordersList[position].plan!!.name
  //      holder.orderDateTimeTxt.text = AppUtils.getReadableDateFromUTC(ordersList[position].createdAt!!)
        holder.orderDateTimeTxt.text = ordersList[position].createdAt!!
        holder.orderAmountTxt.text =  mContext.resources.getString(
            R.string.Rs)+" "+ ordersList[position].grandTotal.toString()
        holder.statusTxt.text = ordersList[position].status.toString()
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