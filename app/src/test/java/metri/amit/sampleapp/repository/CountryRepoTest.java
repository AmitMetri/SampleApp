package metri.amit.sampleapp.repository;

import org.junit.Test;

import metri.amit.sampleapp.SingleMutableLiveData;
import metri.amit.sampleapp.model.ErrorData;

import static com.google.common.truth.Truth.assertThat;


/**
 * Created by amitmetri on 05,April,2021
 */
public class CountryRepoTest {

    /*
     * Test the return type of getInstance()
     * */
    @Test
    public void testGetInstance() {
        CountryRepo countryRepo = CountryRepo.getInstance();
        assertThat(countryRepo).isInstanceOf(CountryRepo.class);
    }

    /*
     * Test the return type of getErrorDataForItemListFragment()
     * */
    @Test
    public void testGetErrorDataForItemListFragment() {
        SingleMutableLiveData<ErrorData> errorDataSingleMutableLiveData
                = CountryRepo.getInstance().getErrorDataForItemListFragment();
        assertThat(errorDataSingleMutableLiveData).isInstanceOf(SingleMutableLiveData.class);
    }

    /*
     * Test the return type of getErrorDataForItemDetailsFragment()
     * */
    @Test
    public void testGetErrorDataForItemDetailsFragment() {
        SingleMutableLiveData<ErrorData> errorDataForItemDetailsFragment
                = CountryRepo.getInstance().getErrorDataForItemDetailsFragment();
        assertThat(errorDataForItemDetailsFragment).isInstanceOf(SingleMutableLiveData.class);
    }


}