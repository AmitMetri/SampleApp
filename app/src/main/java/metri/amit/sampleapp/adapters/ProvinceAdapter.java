package metri.amit.sampleapp.adapters;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import metri.amit.sampleapp.databinding.ItemProvinceBinding;
import metri.amit.sampleapp.model.Province;

/**
 * Created by amitmetri on 04,April,2021
 */
public class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.ViewHolder> {

    private static final String TAG = "ProvinceAdapter";
    private List<Province> provinceList;
    private int spanCount;
    private Activity activity;

    public ProvinceAdapter(List<Province> provinceList, int spanCount, FragmentActivity activity) {
        this.provinceList = provinceList;
        this.spanCount = spanCount;
        this.activity = activity;
    }

    public void updateList(List<Province> provinceList) {
        this.provinceList.clear();
        this.provinceList.addAll(provinceList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProvinceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProvinceBinding itemProvinceBinding = ItemProvinceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        /* To support landscape mode the width of grid
         * will be calculated based on span count */
        DisplayMetrics displaymetrics = new DisplayMetrics();
        try {
            Objects.requireNonNull(activity).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        } catch (Exception e) {
            Log.e(TAG, "ErrorData" + e, e);
        }
        itemProvinceBinding.getRoot().getLayoutParams().width = displaymetrics.widthPixels / spanCount;

        return new ProvinceAdapter.ViewHolder(itemProvinceBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProvinceAdapter.ViewHolder holder, int position) {
        Province province = provinceList.get(position);
        holder.binding.textViewIndex.setText(String.valueOf(position+1));
        holder.binding.provinceName.setText(province.getName());
    }

    @Override
    public int getItemCount() {
        return provinceList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemProvinceBinding binding;

        private ViewHolder(ItemProvinceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
