package com.mobile.cloudkitchen.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.mobile.cloudkitchen.R
import com.mobile.cloudkitchen.data.model.DashBoardBannerModel
import com.smarteist.autoimageslider.SliderViewAdapter


open class HomeMainSliderAdapter(private val mContext: Context) : SliderViewAdapter<HomeMainSliderAdapter.VH>() {
    private var bannersList = ArrayList<DashBoardBannerModel>()
    fun renewItems(sliderItems: ArrayList<DashBoardBannerModel>) {
        bannersList = sliderItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup): VH {
        val inflate: View = LayoutInflater.from(parent.context).inflate(R.layout.slider_items, null)
        return VH(inflate)
    }

    override fun onBindViewHolder(viewHolder: VH, position: Int) {
        Glide.with(mContext)
            .load(bannersList[0].bannerImage)
            .into(viewHolder.imageView)
        /*if (bannersList[position].bannerName == "null") {
            viewHolder.imageView.setOnClickListener {

            }
            viewHolder.freetag.visibility = View.INVISIBLE
            viewHolder.play.visibility = View.INVISIBLE
        } else {
            viewHolder.freetag.visibility = View.VISIBLE
            viewHolder.play.visibility = View.VISIBLE
            viewHolder.imageView.setOnClickListener {
               *//* val intent = Intent(mContext, MovieDescriptionActivity::class.java)
                intent.putExtra("id", bannersList[position].bannerid)
                mContext.startActivity(intent)*//*
            }
        }*/
    }

    override fun getCount(): Int {
        return bannersList.size
    }

    inner class VH(itemView: View) : ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.bannerIv)
        var freetag: RelativeLayout = itemView.findViewById(R.id.free_tag)
        var play: ImageView = itemView.findViewById(R.id.movie_play)

    }
}