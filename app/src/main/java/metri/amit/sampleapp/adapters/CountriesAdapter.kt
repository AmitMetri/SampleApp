package metri.amit.sampleapp.adapters

import android.app.Activity
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import metri.amit.sampleapp.R
import metri.amit.sampleapp.databinding.ItemCountryBinding
import metri.amit.sampleapp.model.Country
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by amitmetri on 28,April,2021
 * Implements filterable to provide search feature
 */
class CountriesAdapter(private val countries: MutableList<Country>, spanCount: Int, activity: FragmentActivity, countrySelection: CountrySelection) : RecyclerView.Adapter<CountriesAdapter.ViewHolder>(), Filterable {
    private var countriesFiltered: MutableList<Country>
    private val countrySelection: CountrySelection
    private val spanCount: Int
    private val activity: Activity

    /*
    * Update the countries list once available at observer
    * */
    fun updateList(countries: List<Country?>?) {
        this.countries.clear()
        countriesFiltered.clear()
        if (countries != null) {
            for(province in countries){
                province?.let { this.countries.add(it) }
            }
        }
        if (countries != null) {
            for(province in countries){
                province?.let { this.countriesFiltered.add(it) }
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemCountryBinding = ItemCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        /* To support landscape mode the width of grid
        * will be calculated based on span count */
        val displayMetrics = DisplayMetrics()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val display = activity.display
            display?.getRealMetrics(displayMetrics)
        } else {
            @Suppress("DEPRECATION")
            val display = activity.windowManager.defaultDisplay
            @Suppress("DEPRECATION")
            display.getMetrics(displayMetrics)
        }

        itemCountryBinding.root.layoutParams.width = displayMetrics.widthPixels / spanCount
        return ViewHolder(itemCountryBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val country = countriesFiltered[position]
        holder.binding.countryName.text = country.Name
        Glide.with(holder.itemView)
                .load(activity.getString(R.string.base_url_flags) + country.Code?.toLowerCase() + ".png")
                .centerCrop()
                .placeholder(android.R.drawable.gallery_thumb)
                .into(holder.binding.countryFlag)
        holder.itemView.setOnClickListener { v: View? -> countrySelection.onCountrySelected(country, holder.itemView, position) }

        /*
        * Set unique transition names for transition animation
        * */holder.binding.countryFlag.transitionName = "countryFlag$position"
        holder.binding.countryName.transitionName = "countryName$position"
    }

    override fun getItemCount(): Int {
        return countriesFiltered.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charString = constraint.toString()
                countriesFiltered = if (charString.isEmpty()) {
                    CopyOnWriteArrayList(countries)
                } else {
                    val filteredList: MutableList<Country> = ArrayList()
                    for (row in CopyOnWriteArrayList(countries)) {
                        /*
                        * Match the char
                        * */
                        if (row.Name!!.toLowerCase().contains(charString.toLowerCase())
                                || row.Name!!.contains(constraint)) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = countriesFiltered
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                countriesFiltered = results.values as MutableList<Country>
                notifyDataSetChanged()
            }
        }
    }

    interface CountrySelection {
        fun onCountrySelected(country: Country?, itemView: View?, position: Int)
    }

    class ViewHolder(val binding: ItemCountryBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val TAG = "CountriesAdapter"
    }

    /*
    * Initialize the properties while setting the adapter
    * */
    init {
        countriesFiltered = CopyOnWriteArrayList(countries)
        this.spanCount = spanCount
        this.activity = activity
        this.countrySelection = countrySelection
    }
}