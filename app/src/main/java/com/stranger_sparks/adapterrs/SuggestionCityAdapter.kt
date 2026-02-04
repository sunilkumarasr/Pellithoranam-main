package com.stranger_sparks.adapterrs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.stranger_sparks.data_model.SuggestionCityResponse
import java.util.Locale
import java.util.Locale.getDefault

class SuggestionCityAdapter(mContext: Context, @LayoutRes private val layoutResource: Int,
businessArray: List<SuggestionCityResponse.Data>?
) : ArrayAdapter<SuggestionCityResponse.Data>(mContext, layoutResource, businessArray!!.toList()),Filterable{


    private var mList: List<SuggestionCityResponse.Data> = businessArray!!

    override fun getCount(): Int = mList.size

    override fun getItem(position: Int): SuggestionCityResponse.Data = mList[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context).inflate(layoutResource, parent, false) as TextView

//        val view = convertView ?: LayoutInflater.from(parent.context).inflate(
//            layoutResource,
//            parent,
//            false
//        ) var text_title = view.findViewById(R.id.tv_title) as TextView

        view.text = mList[position].location ?: "location"

//        view.tv_title.text = filteredMovies[position].name
//        view.tvMovieYear.text = filteredMovies[position].year.toString()

        return view

    }

    override fun getFilter(): Filter {

        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                @Suppress("UNCHECKED_CAST")
                mList = filterResults.values as List<SuggestionCityResponse.Data>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val queryString = charSequence?.toString()?.lowercase(getDefault())

                val filterResults = FilterResults()
                filterResults.values = if (queryString == null || queryString.isEmpty())
                    mList
                else
                    mList.filter {
                        it.location.lowercase(getDefault()).contains(queryString)
                    }
                return filterResults
            }
        }
    }
}