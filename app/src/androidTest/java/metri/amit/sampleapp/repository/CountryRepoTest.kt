package metri.amit.sampleapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by amitmetri on 05,April,2021
 */
class CountryRepoTest {
    private var appContext: Context? = null

    /*
     * Instrumentation tests need application context
     * */
    @Before
    fun setUp() {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    /*
     * Test the return type when device is connected/NOT connected to network.
     * When device is connected to network return value expected is true.
     * When device is NOT connected to network return value expected is false.
     * */
    @get:Test
    val isConnectedToNetwork: Unit
        get() {
            val value = CountryRepo.getInstance().isConnectedToNetwork(appContext!!)
            if (CountryRepo.getInstance().isConnectedToNetwork(appContext!!)) Truth.assertThat(value).isTrue() else Truth.assertThat(value).isFalse()
        }

    /*
     * Test the return type of getCountries()
     * */
    @get:Test
    val countries: Unit
        get() {
            val listLiveData = CountryRepo.getInstance().getCountries(appContext!!)
            Truth.assertThat(listLiveData).isInstanceOf(LiveData::class.java)
        }

    /*
     * Test the return type of getProvinceList()
     * */
    @get:Test
    val provinceList: Unit
        get() {
            val provinceList = CountryRepo.getInstance().getProvinceList("12", appContext!!)
            Truth.assertThat(provinceList).isInstanceOf(LiveData::class.java)
        }

    @After
    fun tearDown() {
        appContext = null
    }
}