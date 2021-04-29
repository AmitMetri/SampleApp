package metri.amit.sampleapp.adapters

import android.app.Activity
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import metri.amit.sampleapp.databinding.ItemProvinceBinding
import metri.amit.sampleapp.model.Province
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by amitmetri on 28,April,2021
 */
class ProvinceAdapter(private val provinceList: ArrayList<Province>, private val spanCount: Int, activity: Activity) : RecyclerView.Adapter<ProvinceAdapter.ViewHolder>() {
    private val activity: Activity
    fun updateList(provinceList: List<Province?>?) {
        this.provinceList.clear()
        if (provinceList != null) {
            for(province in provinceList){
                province?.let { this.provinceList.add(it) }
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemProvinceBinding = ItemProvinceBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        /* To support landscape mode the width of grid
         * will be calculated based on span count */
        val displaymetrics = DisplayMetrics()
        try {
            Objects.requireNonNull(activity).windowManager.defaultDisplay.getMetrics(displaymetrics)
        } catch (e: Exception) {
            Log.e(TAG, "ErrorData$e", e)
        }
        itemProvinceBinding.root.layoutParams.width = displaymetrics.widthPixels / spanCount
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

    class ViewHolder(val binding: ItemProvinceBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val TAG = "ProvinceAdapter"
    }

    init {
        this.activity = activity
    }
}

