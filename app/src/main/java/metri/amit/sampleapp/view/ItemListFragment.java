package metri.amit.sampleapp.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import metri.amit.sampleapp.R;
import metri.amit.sampleapp.adapters.CountriesAdapter;
import metri.amit.sampleapp.databinding.ItemListFragmentBinding;
import metri.amit.sampleapp.model.Country;
import metri.amit.sampleapp.model.ErrorData;
import metri.amit.sampleapp.viewmodel.ItemListViewModel;

public class ItemListFragment extends Fragment {

    private final List<Country> countries = new ArrayList<>();
    private String TAG = "ItemListFragment";
    private ItemListViewModel mViewModel;
    private ItemListFragmentBinding mBinding;
    private CountriesAdapter countriesAdapter;

    public ItemListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        /*
         * View binding is enabled
         * Inflate the view here
         * */
        mBinding = ItemListFragmentBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* ViewModel initialization with fragment lifecycle scope */
        mViewModel = new ViewModelProvider(this).get(ItemListViewModel.class);

        /* orientation is required for landscape support */
        int orientation = this.getResources().getConfiguration().orientation;



        setRecyclerView(orientation);

        /*
         * Search feature is available.
         * User can search for country name
         * and get desired results with filter implemented in the CountriesAdapter
         * */
        mBinding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                countriesAdapter.getFilter().filter(newText);
                return false;
            }
        });

        subscribeForCountryList();

        /*
         * Subscribe for error data.
         * Error data can be emitted in case of
         * network failures, timeouts, data validation failures or
         * any other exceptions.
         * */
        mBinding.retryButton.setVisibility(View.INVISIBLE);
        mViewModel.getErrorDataMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ErrorData>() {
            @Override
            public void onChanged(ErrorData errorData) {
                /*
                 * Make retry button visible in case of error
                 * and make progress invisible in case of error.
                 * */
                mBinding.retryButton.setVisibility(View.VISIBLE);
                mBinding.progressCircular.setVisibility(View.INVISIBLE);
                mBinding.searchView.setVisibility(View.INVISIBLE);
                mBinding.communicationText.setText(errorData.getErrorMessage());
                /*
                 * Retry button click will initiate network call again
                 * to get the countries list
                 * */
                mBinding.retryButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBinding.retryButton.setVisibility(View.INVISIBLE);
                        subscribeForCountryList();
                    }
                });
            }
        });
    }

    /*
     * Subscribe for countries list,
     * which will be available post successful network call.
     * Update the CountriesAdapter once the countries list is available
     * */
    private void subscribeForCountryList() {
        mBinding.searchView.setVisibility(View.VISIBLE);
        mBinding.progressCircular.setVisibility(View.VISIBLE);
        mBinding.communicationText.setText("");
        mViewModel.getCountryList().observe(getViewLifecycleOwner(), new Observer<List<Country>>() {
            @Override
            public void onChanged(List<Country> countries) {
                mBinding.progressCircular.setVisibility(View.INVISIBLE);
                countriesAdapter.updateList(countries);
            }
        });
    }

    /*
     * Set the recyclerView for every screen configuration change
     * */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRecyclerView(newConfig.orientation);
    }

    /*
     * Set recyclerView using GridlayoutManager
     * */
    private void setRecyclerView(int orientation) {
        mBinding.recyclerView.addItemDecoration(new DividerItemDecoration(mBinding.recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        GridLayoutManager gridLayoutManager;
        /*
         * Use the orientation to set the GridlayoutManager span items count.
         * */
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            countriesAdapter = new CountriesAdapter(countries, 1, getActivity(), new CountriesAdapter.CountrySelection() {
                @Override
                public void onCountrySelected(Country country, View itemView, int position) {
                    navigateToDetails(country, itemView, position);
                }
            });
            gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        } else {
            countriesAdapter = new CountriesAdapter(countries, 2, getActivity(), new CountriesAdapter.CountrySelection() {
                @Override
                public void onCountrySelected(Country country, View itemView, int position) {
                    navigateToDetails(country, itemView, position);
                }
            });
            gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        }
        mBinding.recyclerView.setLayoutManager(gridLayoutManager);
        mBinding.recyclerView.setAdapter(countriesAdapter);
    }

    /*
     * JetPack's navigation component is used for navigation,
     * for passing data
     * and for transition animation
     * */
    private void navigateToDetails(Country country, View itemView, int position) {
        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                .addSharedElement(itemView.findViewById(R.id.countryFlag), "countryFlag" + position)
                .addSharedElement(itemView.findViewById(R.id.countryName), "countryName" + position)
                .build();

        Bundle bundle = new Bundle();
        bundle.putSerializable("CountryDetails", country);
        bundle.putInt("position", position);
        NavHostFragment.findNavController(ItemListFragment.this)
                .navigate(R.id.itemDetailsFragment,
                        bundle, null, extras);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}