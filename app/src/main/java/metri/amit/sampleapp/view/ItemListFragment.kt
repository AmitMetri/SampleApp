package metri.amit.sampleapp.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import metri.amit.sampleapp.R
import metri.amit.sampleapp.adapters.CountriesAdapter
import metri.amit.sampleapp.adapters.CountriesAdapter.CountrySelection
import metri.amit.sampleapp.databinding.ItemListFragmentBinding
import metri.amit.sampleapp.model.Country
import metri.amit.sampleapp.model.ErrorData
import metri.amit.sampleapp.viewmodel.ItemListViewModel

/**
 * Created by amitmetri on 28,April,2021
 * ItemListFragment lists the countries.
 * MVVM architecture is used along with LiveData support.
 * Counties list and Error data are observed in this fragment.
 * Error cases are handled.
 * Network failures are also handled.
 * Network connection check is placed.
 * */
class ItemListFragment : Fragment() {

    private var mBinding: ItemListFragmentBinding? = null
    private val TAG: String = "ItemListFragment";
    private var itemListViewModel: ItemListViewModel? = null
    private var countriesAdapter: CountriesAdapter? = null
    private val countries: List<Country> = java.util.ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        /*
         * View binding is enabled
         * Inflate the view here
         * */
        mBinding = ItemListFragmentBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* ViewModel initialization with fragment lifecycle scope */
        itemListViewModel = ViewModelProvider(this).get(ItemListViewModel::class.java)

        /* orientation is required for landscape support */
        val orientation: Int = this.resources.configuration.orientation

        setRecyclerView(orientation)


        /*
         * Search feature is available.
         * User can search for country name
         * and get desired results with filter implemented in the CountriesAdapter
         * */
        mBinding!!.searchView.setOnQueryTextListener(object : OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                countriesAdapter!!.filter.filter(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })

        /*
         * Subscribe for error data.
         * Error data can be emitted in case of
         * network failures, timeouts, data validation failures or
         * any other exceptions.
         * */
        mBinding!!.retryButton.visibility = View.INVISIBLE
        itemListViewModel!!.errorDataMutableLiveData.observe(viewLifecycleOwner, Observer { errorData: ErrorData ->
            /*
             * Make retry button visible in case of error
             * and make progress invisible in case of error.
             * */
            mBinding!!.retryButton.visibility = View.VISIBLE
            mBinding!!.progressCircular.visibility = View.INVISIBLE
            mBinding!!.searchView.visibility = View.INVISIBLE
            mBinding!!.communicationText.text = errorData.errorMessage
            /*
             * Retry button click will initiate network call again
             * to get the countries list
             * */
            mBinding!!.retryButton.setOnClickListener {
            mBinding!!.retryButton.visibility = View.INVISIBLE
            subscribeForCountryList()
        }
        })

        subscribeForCountryList()

    }

    /*
     * Subscribe for countries list,
     * which will be available post successful network call.
     * Update the CountriesAdapter once the countries list is available
     * */
    private fun subscribeForCountryList() {
        mBinding!!.searchView.visibility = View.VISIBLE
        mBinding!!.progressCircular.visibility = View.VISIBLE
        mBinding!!.communicationText.text = ""
        itemListViewModel?.countryList?.observe(viewLifecycleOwner, Observer {
            mBinding!!.progressCircular.visibility = View.INVISIBLE
            countriesAdapter!!.updateList(it)
        })
    }

    /*
     * Set the recyclerView for every screen configuration change
     * */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setRecyclerView(newConfig.orientation)
    }

    /*
     * Set recyclerView using GridlayoutManager
     * */
    fun setRecyclerView(orientation: Int) {
        mBinding!!.recyclerView.addItemDecoration(DividerItemDecoration(mBinding!!.recyclerView.context, DividerItemDecoration.VERTICAL))
        val gridLayoutManager: GridLayoutManager
        /*
         * Use the orientation to set the GridlayoutManager span items count.
         * */
        /*
         * Use the orientation to set the GridlayoutManager span items count.
         * */
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            countriesAdapter = CountriesAdapter(countries as MutableList<Country>, 1, requireActivity(), object : CountrySelection {
                override fun onCountrySelected(country: Country?, itemView: View?, position: Int) {
                    navigateToDetails(country!!, itemView!!, position)
                }
            })
            gridLayoutManager = GridLayoutManager(activity, 1)
        } else {
            countriesAdapter = CountriesAdapter(countries as MutableList<Country>, 2, requireActivity(), object : CountrySelection {
                override fun onCountrySelected(country: Country?, itemView: View?, position: Int) {
                    navigateToDetails(country!!, itemView!!, position)
                }
            })
            gridLayoutManager = GridLayoutManager(activity, 2)
        }
        mBinding!!.recyclerView.layoutManager = gridLayoutManager
        mBinding!!.recyclerView.adapter = countriesAdapter
    }

    /*
     * JetPack's navigation component is used for navigation,
     * for passing data
     * and for transition animation
     * */
    private fun navigateToDetails(country: Country, itemView: View, position: Int) {
        val extras = FragmentNavigator.Extras.Builder()
                .addSharedElement(itemView.findViewById(R.id.countryFlag), "countryFlag$position")
                .addSharedElement(itemView.findViewById(R.id.countryName), "countryName$position")
                .build()
        val bundle = Bundle()
        bundle.putSerializable("CountryDetails", country)
        bundle.putInt("position", position)
        NavHostFragment.findNavController(this@ItemListFragment)
                .navigate(R.id.itemDetailsFragment,
                        bundle, null, extras)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}