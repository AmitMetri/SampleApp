package metri.amit.sampleapp.repository

import com.google.common.truth.Truth
import metri.amit.sampleapp.SingleMutableLiveData
import metri.amit.sampleapp.repository.CountryRepo
import org.junit.Test

/**
 * Created by amitmetri on 05,April,2021
 */
class CountryRepoUnitTest {
    /*
     * Test the return type of getInstance()
     * */
    @Test
    fun testGetInstance() {
        val countryRepo = CountryRepo.getInstance()
        Truth.assertThat(countryRepo).isInstanceOf(CountryRepo::class.java)
    }

    /*
     * Test the return type of getErrorDataForItemListFragment()
     * */
    @Test
    fun testGetErrorDataForItemListFragment() {
        val errorDataSingleMutableLiveData = CountryRepo.getInstance().errorDataForItemListFragment
        Truth.assertThat(errorDataSingleMutableLiveData).isInstanceOf(SingleMutableLiveData::class.java)
    }

    /*
     * Test the return type of getErrorDataForItemDetailsFragment()
     * */
    @Test
    fun testGetErrorDataForItemDetailsFragment() {
        val errorDataForItemDetailsFragment = CountryRepo.getInstance().errorDataForItemDetailsFragment
        Truth.assertThat(errorDataForItemDetailsFragment).isInstanceOf(SingleMutableLiveData::class.java)
    }
}