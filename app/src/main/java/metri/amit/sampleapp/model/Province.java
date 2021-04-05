
package metri.amit.sampleapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
* Model class for Province list
* Improvement can be done here with Parcelable implementation
* */
public class Province {

    @SerializedName("ID")
    @Expose
    private Integer id;
    @SerializedName("CountryCode")
    @Expose
    private String countryCode;
    @SerializedName("Code")
    @Expose
    private String code;
    @SerializedName("Name")
    @Expose
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
