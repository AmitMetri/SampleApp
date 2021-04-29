package metri.amit.sampleapp.view

import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import metri.amit.sampleapp.R
import metri.amit.sampleapp.adapters.ProvinceAdapter
import metri.amit.sampleapp.databinding.FragmentItemDetailsBinding
import metri.amit.sampleapp.model.Country
import metri.amit.sampleapp.model.ErrorData
import metri.amit.sampleapp.model.Province
import metri.amit.sampleapp.viewmodel.ItemDetailsViewModel
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by amitmetri on 28,April,2021
 * Details screen when any if the country is selected
 * from the first screen
 * MVVM architecture is used along with LiveData support.
 * Counties list and Error data are observed in this fragment.
 * Error cases are handled.
 * Network failures are also handled.
 * Network connection check is placed.
 */
class ItemDetailsFragment : Fragment() {

    private var mViewModel: ItemDetailsViewModel? = null
    private val provinceList: MutableList<Province> = mutableListOf()
    private var provinceAdapter: ProvinceAdapter? = null
    private var mBinding: FragmentItemDetailsBinding? = null
    private var position: Int? = null
    private var country: Country? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
         * get the arguments passed by ItemListFragment
         * */
        country = requireArguments().getSerializable("CountryDetails") as Country?
        position = requireArguments().getInt("position", 0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        /*
         * View binding is enabled
         * Inflate the view here
         * */
        mBinding = FragmentItemDetailsBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* ViewModel initialization with fragment lifecycle scope */
        mViewModel = ViewModelProvider(this).get(ItemDetailsViewModel::class.java)

        /* Required for shared transition */
        this@ItemDetailsFragment.sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        postponeEnterTransition()

        /* Set the transition names, which are unique.
         * Same transition names are set in CountriesAdapter */
        mBinding!!.countryFlag.transitionName = "countryFlag$position"
        mBinding!!.countryName.transitionName = "countryName$position"

        /* orientation is required for landscape support */
        val orientation = this.resources.configuration.orientation

        setRecyclerView(orientation)

        /*
         * Set the UI elements here
         * */
        if (country != null) {
            Glide.with(this)
                    .load(getString(R.string.base_url_flags)
                            + country!!.Code?.toLowerCase(Locale.getDefault()) + ".png")
                    .centerCrop()
                    .placeholder(android.R.drawable.gallery_thumb)
                    .listener(object : RequestListener<Drawable?> {
                        override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                            //Start transition
                            startPostponedEnterTransition()
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                            //Start transition
                            startPostponedEnterTransition()
                            return false
                        }
                    })
                    .into(mBinding!!.countryFlag)
            mBinding!!.countryName.text = StringBuilder("Name: ").append(country!!.Name)
            mBinding!!.countryCode.text = StringBuilder("Code: ").append(country!!.Code)
            mBinding!!.phoneCode.text = StringBuilder("Phone code: ").append(country!!.PhoneCode)
            subscribeForProvinceList()

            /*
             * Subscribe for error data.
             * Error data can be emitted in case of
             * network failures, timeouts, data validation failures or
             * any other exceptions.
             * */
            if (mBinding!!.retryButton != null) {
                mBinding!!.retryButton!!.visibility = View.INVISIBLE
            }
            mViewModel!!.errorDataMutableLiveData.observe(viewLifecycleOwner, Observer { errorData: ErrorData ->
                /*
                 * Make retry button visible in case of error
                 * and make progress invisible in case of error.
                 * */
                mBinding!!.retryButton!!.visibility = View.VISIBLE
                mBinding!!.progressCircular.visibility = View.INVISIBLE
                if (mBinding!!.communicationText != null) {
                    mBinding!!.communicationText!!.text = errorData.errorMessage
                }
                /*
                 * Retry button click will initiate network call again
                 * to get the province list
                 * */
                mBinding!!.retryButton!!.setOnClickListener { v: View? ->
                    mBinding!!.retryButton!!.visibility = View.INVISIBLE
                    subscribeForProvinceList()
                }
            })
        }
    }

    /*
     * Subscribe for province list,
     * which will be available post successful network call.
     * Update the ProvinceAdapter once the province list is available
     * */
    private fun subscribeForProvinceList() {
        mBinding!!.progressCircular.visibility = View.VISIBLE
        mBinding!!.communicationText!!.text = ""
        mViewModel!!.getProvinceList(country!!.ID.toString()).observe(viewLifecycleOwner, Observer { provinceList: List<Province> ->
            mBinding!!.progressCircular.visibility = View.INVISIBLE
            provinceAdapter!!.updateList(provinceList)
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
    private fun setRecyclerView(orientation: Int) {
        mBinding!!.recyclerView.addItemDecoration(DividerItemDecoration(mBinding!!.recyclerView.context, DividerItemDecoration.VERTICAL))
        val gridLayoutManager: GridLayoutManager
        /*
        * Use the orientation to set the GridlayoutManager span items count.
        * */
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            provinceAdapter = ProvinceAdapter(provinceList , 2, requireActivity())
            gridLayoutManager = GridLayoutManager(activity, 2)
        } else {
            provinceAdapter = ProvinceAdapter(provinceList, 4, requireActivity())
            gridLayoutManager = GridLayoutManager(activity, 4)
        }
        mBinding!!.recyclerView.layoutManager = gridLayoutManager
        mBinding!!.recyclerView.adapter = provinceAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}