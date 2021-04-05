package metri.amit.sampleapp.repository;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.test.platform.app.InstrumentationRegistry;
import metri.amit.sampleapp.model.Country;
import metri.amit.sampleapp.model.Province;

import static com.google.common.truth.Truth.assertThat;


/**
 * Created by amitmetri on 05,April,2021
 */
public class CountryRepoTest {

    private Context appContext;

    /*
     * Instrumentation tests need application context
     * */
    @Before
    public void setUp() {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    /*
     * Test the return type when device is connected/NOT connected to network.
     * When device is connected to network return value expected is true.
     * When device is NOT connected to network return value expected is false.
     * */
    @Test
    public void isConnectedToNetwork() {
        boolean value = CountryRepo.getInstance().isConnectedToNetwork(appContext);
        if(CountryRepo.getInstance().isConnectedToNetwork(appContext))
            assertThat(value).isTrue();
        else
            assertThat(value).isFalse();
    }

    /*
     * Test the return type of getCountries()
     * */
    @Test
    public void getCountries() {
        LiveData<List<Country>> listLiveData = CountryRepo.getInstance().getCountries(appContext);
        assertThat(listLiveData).isInstanceOf(LiveData.class);
    }

    /*
     * Test the return type of getProvinceList()
     * */
    @Test
    public void getProvinceList() {
        LiveData<List<Province>> provinceList = CountryRepo.getInstance().getProvinceList("12", appContext);
        assertThat(provinceList).isInstanceOf(LiveData.class);
    }


    @After
    public void tearDown() {
        appContext = null;
    }
}