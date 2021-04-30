package metri.amit.sampleapp.repository

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import metri.amit.sampleapp.ApiClient
import metri.amit.sampleapp.R
import metri.amit.sampleapp.SingleMutableLiveData
import metri.amit.sampleapp.model.Country
import metri.amit.sampleapp.model.ErrorData
import metri.amit.sampleapp.model.Province

/**
 * Created by amitmetri on 28,April,2021
 * Single repository for the data source.
 */
class CountryRepo {
    /*
     * Country list mutable live data
     * is being observed by the ItemListFragment
     * to show the list of countries
     * */
    val countries = MutableLiveData<List<Country>>()

    /*
     * error mutable live data is being observed by the ItemListFragment
     * to update the UI in case of failure.
     * */
    val errorDataForItemListFragment = SingleMutableLiveData<ErrorData>()

    /*
     * error mutable live data is being observed by the ItemDetailsFragment
     * to update the UI in case of failure.
     * */
    val errorDataForItemDetailsFragment = SingleMutableLiveData<ErrorData>()

    /*
     * Helper method to check the connectivity
     * */
    private fun isConnectedToNetwork(context: Context): Boolean {
        val conMgr = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        try {
            val netInfo = conMgr.activeNetworkInfo
            if (netInfo == null || !netInfo.isConnected || !netInfo.isAvailable) {
                return false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error: $e")
        }
        return true
    }

    /*
     * Using Retrofit with RxJava2CallAdapterFactory.
     * Observable item is subscriber on the worker thread (Schedulers.io()).
     * Observable item is validated on the worker thread (Schedulers.computation()).
     * This makes the item available for updating the UI directly.
     * Observer is posting the item on main thread (AndroidSchedulers.mainThread())
     * */
    fun getCountries(applicationContext: Context): LiveData<List<Country>> {
        if (countries.value == null) {
            if (isConnectedToNetwork(applicationContext)) {
                ApiClient.getClient(applicationContext).create(CountryServices::class.java).getCountryList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.computation())
                        .map { countries ->
                            try {
                                if (countries.isEmpty()) {
                                    errorDataForItemListFragment.postValue(
                                            ErrorData("050",
                                                    applicationContext.getString(R.string.no_countries)))
                                } else {
                                    for (country in countries) {
                                        if (country.ID == null
                                                || country.Code.isNullOrEmpty()
                                                || country.Name.isNullOrEmpty()) {
                                            errorDataForItemListFragment.postValue(
                                                    ErrorData("050",
                                                            applicationContext.getString(R.string.generic_error)))
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                Log.e(TAG, "Error: $e")
                                errorDataForItemListFragment.postValue(
                                        ErrorData("050",
                                                applicationContext.getString(R.string.generic_error)))
                            }
                            countries
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<List<Country>> {

                            override fun onError(e: Throwable) {
                                Log.e(TAG, "Error: $e")
                                errorDataForItemListFragment.postValue(
                                        ErrorData("050",
                                                applicationContext.getString(R.string.generic_error)))
                            }

                            override fun onComplete() {
                            }

                            override fun onSubscribe(d: Disposable) {
                            }

                            override fun onNext(t: List<Country>) {
                                countries.postValue(t)
                            }

                        } )
            } else {
                errorDataForItemListFragment.postValue(
                        ErrorData("050",
                                applicationContext.getString(R.string.no_network)))
            }
        }
        return countries
    }




    /*
     * Using Retrofit with RxJava2CallAdapterFactory.
     * Observable item is subscriber on the worker thread (Schedulers.io()).
     * Observable item is validated on the worker thread (Schedulers.computation()).
     * This makes the item available for updating the UI directly.
     * Observer is posting the item on main thread (AndroidSchedulers.mainThread())
     * */
    fun getProvinceList(countryId: String?, applicationContext: Context): LiveData<List<Province>> {
        /*
         * province list mutable live data
         * which is being observed by ItemDetailsFragment
         * */
        val provinceMutableLiveData = MutableLiveData<List<Province>>()
        if (isConnectedToNetwork(applicationContext)) {
            ApiClient.getClient(applicationContext).create(CountryServices::class.java).getProvinceList(countryId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.computation())
                    .map { provinceList: List<Province> ->
                        try {
                            if (provinceList.isEmpty()) {
                                errorDataForItemDetailsFragment.postValue(
                                        ErrorData("050",
                                                applicationContext.getString(R.string.no_provinces)))
                            } else {
                                for (province in provinceList) {
                                    if (province.ID == null
                                            || province.Name.isNullOrEmpty()) {
                                        errorDataForItemDetailsFragment.postValue(
                                                ErrorData("050",
                                                        applicationContext.getString(R.string.generic_error)))
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Error: $e")
                            errorDataForItemDetailsFragment.postValue(
                                    ErrorData("050",
                                            applicationContext.getString(R.string.generic_error)))
                        }
                        provinceList
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<List<Province>> {
                        override fun onComplete() {
                        }

                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(t: List<Province>) {
                            provinceMutableLiveData.postValue(t)
                        }

                        override fun onError(e: Throwable) {
                            Log.e(TAG, "Error: $e")
                            errorDataForItemDetailsFragment.postValue(
                                    ErrorData("050",
                                            applicationContext.getString(R.string.generic_error)))
                        }
                    })
        } else {
            errorDataForItemDetailsFragment.postValue(
                    ErrorData("050",
                            applicationContext.getString(R.string.no_network)))
        }
        return provinceMutableLiveData
    }

    companion object {
        private const val TAG = "CountryRepo"

        /*
        * Singleton method to provide the instance of the repository
        * */
        private var instance: CountryRepo? = null
        @Synchronized
        fun getInstance(): CountryRepo{
            if(instance == null){
                instance = CountryRepo()
            }
            return instance as CountryRepo
        }
    }

}


