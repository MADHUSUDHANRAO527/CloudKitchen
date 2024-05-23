package com.mobile.cloudkitchen.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobile.cloudkitchen.R
import com.mobile.cloudkitchen.data.model.MonthlyMenu
import com.mobile.cloudkitchen.data.model.WeeklyMenu

class WkMonthMealsMenuAdapter(
    private val mContext: Context,
    private var planType: String,
    private var menu: Any,
) :
    RecyclerView.Adapter<WkMonthMealsMenuAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.wk_month_menu_meals_list_row,
                parent,
                false
            )
        return ViewHolder(itemView)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //  var amount: TextView
        lateinit var mealsImage: ImageView
        lateinit var kitchenTitle: TextView
        lateinit var mealDay: TextView
        lateinit var kitchenDesc: TextView
        lateinit var rating: RatingBar
        lateinit var wkly_sub_price_txt: TextView
        lateinit var monthly_sub_price_txt: TextView
      lateinit  var kitchenCardView : CardView
        init {
            mealsImage = view.findViewById(R.id.meals_image)
        //    kitchenTitle = view.findViewById(R.id.meal_title_txt)
            mealDay = view.findViewById(R.id.day_count_txt)
            kitchenDesc = view.findViewById(R.id.meals_title_desc)
          //  kitchenCardView = view.findViewById(R.id.kitchen_card_view)
        }
    }


    override fun getItemCount(): Int {
        return if(planType.contains("M")) (menu as MonthlyMenu).wklyMenu.size else 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (planType.contains("M")) {
            holder.mealDay.visibility = View.VISIBLE
            holder.mealDay.text =
                "Day-" + (menu as MonthlyMenu).wklyMenu[position].dayCount.toString()
        }
        holder.kitchenDesc.text = if (planType.contains("M")) (menu as MonthlyMenu).wklyMenu[position].menuItems.toString() else getMenuItems((menu as WeeklyMenu).menuItems)
        Glide.with(mContext)
            .load(if (planType.contains("M")) (menu as MonthlyMenu).wklyMenu[position].images else (menu as WeeklyMenu).images)
            .into(holder.mealsImage)
      /*  holder.rating.rating = mealsList[position].noOfRatings.toFloat()
        holder.wkly_sub_price_txt.text =mealsList[position].wklySubscriptionPrice.toString()+" "+mContext.resources.getString(R.string.Rs)+"/Meal"
        holder.monthly_sub_price_txt.text =mealsList[position].monthlySubscriptionPrice.toString()+" "+mContext.resources.getString(R.string.Rs)+"/Meal"
        holder.kitchenCardView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("tag", "KitchenDetailsFragment")
            EventBus.getDefault().postSticky(mealsList[position]._id)
          //  (mContext as HomeActivity?)?.loadFragment(KitchenDetailsFragment(), bundle)
        }*/
    }

    private fun getMenuItems(list: ArrayList<String>): String? {
        val strBuilder = StringBuilder()
        for (item in list) {
            strBuilder.appendLine(item)
        }
    return strBuilder.toString()
    }
}