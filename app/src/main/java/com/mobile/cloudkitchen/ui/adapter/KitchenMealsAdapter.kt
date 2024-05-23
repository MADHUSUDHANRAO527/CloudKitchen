package com.mobile.cloudkitchen.ui.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.example.Kitchen
import com.mobile.cloudkitchen.R
import com.mobile.cloudkitchen.utils.UserUtils
import org.greenrobot.eventbus.EventBus

class KitchenMealsAdapter(private val mContext: Context, private var kitchen: Kitchen) :
    RecyclerView.Adapter<KitchenMealsAdapter.ViewHolder>() {
    private var selectedPos: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.kitchen_meals_list_row,
                parent,
                false
            )
        return ViewHolder(itemView)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //  var amount: TextView
        var mealsImage: ImageView
        var kitchenTitle: TextView
        var kitchenDesc: TextView
        var rating: RatingBar
        var wkly_sub_price_txt: TextView
        var monthly_sub_price_txt: TextView
        var kitchenCardView: CardView
        var addedTxt: TextView

        init {
            mealsImage = view.findViewById(R.id.meals_image)
            kitchenTitle = view.findViewById(R.id.meal_title_txt)
            kitchenDesc = view.findViewById(R.id.meal_title_desc)
            rating = view.findViewById(R.id.meal_rating)
            wkly_sub_price_txt = view.findViewById(R.id.wkly_sub_price_txt)
            monthly_sub_price_txt = view.findViewById(R.id.monthly_sub_price_txt)
            kitchenCardView = view.findViewById(R.id.kitchen_card_view)
            addedTxt = view.findViewById(R.id.added_btn)
           /* if (adapterPosition == 0) {
                addedTxt.visibility = View.VISIBLE
            }*/
        }
    }


    override fun getItemCount(): Int {
        return kitchen.meals.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.kitchenTitle.text = kitchen.meals[position].name
        holder.kitchenDesc.text = kitchen.meals[position].description
        Glide.with(mContext)
            .load(kitchen.meals[position].images[0])
            .into(holder.mealsImage)
        try {
            holder.rating.rating = kitchen.meals[position].rating?.avgRating?.toFloat()!!
        } catch (e: Exception) {

            println(e.message)
        }
        holder.wkly_sub_price_txt.text =
            kitchen.meals[position].weeklySubscriptionCost.toString() + " " + mContext.resources.getString(
                R.string.Rs
            ) + "/Meal"
        holder.monthly_sub_price_txt.text =
            kitchen.meals[position].monthlySubscriptionCost.toString() + " " + mContext.resources.getString(
                R.string.Rs
            ) + "/Meal"
        if (position == selectedPos) {
            holder.addedTxt.visibility = View.VISIBLE
            UserUtils.mealID = kitchen.meals[position].Id!!
        } else
            holder.addedTxt.visibility = View.GONE

        holder.kitchenCardView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("tag", "KitchenDetailsFragment")
            UserUtils.setKitchen(kitchen)
            UserUtils.mealID = kitchen.meals[position].Id!!
            EventBus.getDefault().postSticky(kitchen.meals[position])
            selectedPos = position
            notifyDataSetChanged()
        }
    }
}
