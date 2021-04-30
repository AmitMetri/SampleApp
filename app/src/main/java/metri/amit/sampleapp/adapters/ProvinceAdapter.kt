package metri.amit.sampleapp.adapters

import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import metri.amit.sampleapp.databinding.ItemProvinceBinding
import metri.amit.sampleapp.model.Province

/**
 * Created by amitmetri on 28,April,2021
 */
class ProvinceAdapter(private var provinceList: MutableList<Province>, private val spanCount: Int, private val activity: Activity) : RecyclerView.Adapter<ProvinceAdapter.ViewHolder>() {

    /*
    * Update the ProvinceAdapter once updated list is available at observer
    * */
    fun updateList(provinceList: List<Province>) {
        this.provinceList.clear()
        this.provinceList.addAll(provinceList)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemProvinceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemProvinceBinding = ItemProvinceBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        /*
         * To support landscape mode the width of grid
         * will be calculated based on span count
         * */
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
        itemProvinceBinding.root.layoutParams.width = displayMetrics.widthPixels / spanCount
        return ViewHolder(itemProvinceBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val province = provinceList[position]
        holder.binding.textViewIndex.text = (position + 1).toString()
        holder.binding.provinceName.text = province.Name
    }

    override fun getItemCount(): Int {
        return provinceList.size
    }
}

