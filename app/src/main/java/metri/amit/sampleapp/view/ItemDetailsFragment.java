package metri.amit.sampleapp.view;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.transition.TransitionInflater;
import metri.amit.sampleapp.R;
import metri.amit.sampleapp.adapters.ProvinceAdapter;
import metri.amit.sampleapp.databinding.FragmentItemDetailsBinding;
import metri.amit.sampleapp.model.Country;
import metri.amit.sampleapp.model.ErrorData;
import metri.amit.sampleapp.model.Province;
import metri.amit.sampleapp.viewmodel.ItemDetailsViewModel;

/**
 * Item Details fragment
 */
public class ItemDetailsFragment extends Fragment {


    private ItemDetailsViewModel mViewModel;
    private FragmentItemDetailsBinding mBinding;
    private List<Province> provinceList = new ArrayList<>();
    private ProvinceAdapter provinceAdapter;
    private Country country;
    private int position;

    public ItemDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         * get the arguments passed by ItemListFragment
         * */
        if (getArguments() != null) {
            if (getArguments().getSerializable("CountryDetails") != null) {
                country = (Country) getArguments().getSerializable("CountryDetails");
            }
            position = getArguments().getInt("position", 0);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*
         * View binding is enabled
         * Inflate the view here
         *  */
        mBinding = FragmentItemDetailsBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* ViewModel initialization with fragment lifecycle scope */
        mViewModel = new ViewModelProvider(this).get(ItemDetailsViewModel.class);

        /* Required for shared transition */
        ItemDetailsFragment.this.setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        postponeEnterTransition();
        /* Set the transition names, which are unique.
         * Same transition names are set in CountriesAdapter */
        mBinding.countryFlag.setTransitionName("countryFlag" + position);
        mBinding.countryName.setTransitionName("countryName" + position);

        /* orientation is required for landscape support */
        int orientation = this.getResources().getConfiguration().orientation;

        setRecyclerView(orientation);

        /*
         * Set the UI elements here
         * */
        if (country != null) {
            Glide.with(this)
                    .load(getString(R.string.base_url_flags) + country.getCode().toLowerCase() + ".png")
                    .centerCrop()
                    .placeholder(android.R.drawable.gallery_thumb)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            //Start transition
                            startPostponedEnterTransition();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            //Start transition
                            startPostponedEnterTransition();
                            return false;
                        }
                    })
                    .into(mBinding.countryFlag);
            mBinding.countryName.setText(new StringBuilder("Name: ").append(country.getName()));
            mBinding.countryCode.setText(new StringBuilder("Code: ").append(country.getCode()));
            mBinding.phoneCode.setText(new StringBuilder("Phone code: ").append(country.getPhoneCode()));

            subscribeForProvinceList();

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
                    mBinding.communicationText.setText(errorData.getErrorMessage());
                    /*
                     * Retry button click will initiate network call again
                     * to get the province list
                     * */
                    mBinding.retryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mBinding.retryButton.setVisibility(View.INVISIBLE);
                            subscribeForProvinceList();
                        }
                    });
                }
            });
        }
    }


    /*
     * Subscribe for province list,
     * which will be available post successful network call.
     * Update the ProvinceAdapter once the province list is available
     * */
    private void subscribeForProvinceList() {
        mBinding.progressCircular.setVisibility(View.VISIBLE);
        mBinding.communicationText.setText("");
        mViewModel.getProvinceList(country.getId().toString()).observe(getViewLifecycleOwner(), new Observer<List<Province>>() {
            @Override
            public void onChanged(List<Province> provinceList) {
                mBinding.progressCircular.setVisibility(View.INVISIBLE);
                provinceAdapter.updateList(provinceList);
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
            provinceAdapter = new ProvinceAdapter(provinceList, 2, getActivity());
            gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        } else {
            provinceAdapter = new ProvinceAdapter(provinceList, 4, getActivity());
            gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        }
        mBinding.recyclerView.setLayoutManager(gridLayoutManager);
        mBinding.recyclerView.setAdapter(provinceAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}