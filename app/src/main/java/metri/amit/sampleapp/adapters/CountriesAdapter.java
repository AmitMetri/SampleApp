package metri.amit.sampleapp.adapters;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import metri.amit.sampleapp.R;
import metri.amit.sampleapp.databinding.ItemCountryBinding;
import metri.amit.sampleapp.model.Country;

/**
 * Created by amitmetri on 03,April,2021
 * Implements filterable to provide search feature
 */
public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.ViewHolder> implements Filterable {

    private static final String TAG = "CountriesAdapter";
    private final List<Country> countries;
    private List<Country> countriesFiltered;
    private final CountrySelection countrySelection;
    private final int spanCount;
    private final Activity activity;

    /*
    * Initialize the properties while setting the adapter
    * */
    public CountriesAdapter(List<Country> countries, int spanCount, FragmentActivity activity, CountrySelection countrySelection) {
        this.countries = countries;
        countriesFiltered = new CopyOnWriteArrayList<>(countries);
        this.spanCount = spanCount;
        this.activity = activity;
        this.countrySelection = countrySelection;
    }

    /*
    * Update the countries list once available at observer
    * */
    public void updateList(List<Country> countries) {
        this.countries.clear();
        countriesFiltered.clear();
        this.countries.addAll(countries);
        countriesFiltered.addAll(countries);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CountriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCountryBinding itemCountryBinding = ItemCountryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        /* To support landscape mode the width of grid
        * will be calculated based on span count */
        DisplayMetrics displaymetrics = new DisplayMetrics();
        try {
            Objects.requireNonNull(activity).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        } catch (Exception e) {
            Log.e(TAG, "ErrorData" + e, e);
        }
        itemCountryBinding.getRoot().getLayoutParams().width = displaymetrics.widthPixels / spanCount;

        return new ViewHolder(itemCountryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CountriesAdapter.ViewHolder holder, int position) {
        Country country = countriesFiltered.get(position);

        holder.binding.countryName.setText(country.getName());
        Glide.with(holder.itemView)
                .load(activity.getString(R.string.base_url_flags) + country.getCode().toLowerCase() + ".png")
                .centerCrop()
                .placeholder(android.R.drawable.gallery_thumb)
                .into(holder.binding.countryFlag);
        holder.itemView.setOnClickListener(v -> countrySelection.onCountrySelected(country, holder.itemView, position));

        /*
        * Set unique transition names for transition animation
        * */
        holder.binding.countryFlag.setTransitionName("countryFlag" + position);
        holder.binding.countryName.setTransitionName("countryName" + position);
    }

    @Override
    public int getItemCount() {
        return countriesFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    countriesFiltered = new CopyOnWriteArrayList<>(countries);
                } else {
                    List<Country> filteredList = new ArrayList<>();
                    for (Country row : countries) {
                        /*
                        * Match the char
                        * */
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())
                                || row.getName().contains(constraint)) {
                            filteredList.add(row);
                        }
                    }
                    countriesFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = countriesFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                countriesFiltered = (List<Country>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface CountrySelection {
        void onCountrySelected(Country country, View itemView, int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemCountryBinding binding;
        private ViewHolder(ItemCountryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
