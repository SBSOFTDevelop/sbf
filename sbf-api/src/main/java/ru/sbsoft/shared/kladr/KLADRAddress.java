package ru.sbsoft.shared.kladr;

import java.util.ArrayList;
import ru.sbsoft.common.Strings;

/**
 * @author balandin
 * @since Mar 15, 2013 12:43:27 PM
 */
public class KLADRAddress extends KLADRAddressDict {

    public static final String RUSSIA = "643";
    //
    private String postcode;
    private String house;
    private String building;
    private String flat;
    //
    private String regionCode;
    private int addressID;
    private int state;

    public KLADRAddress() {
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @Override
    public String getPostcode() {
        if (postcode != null) {
            return postcode;
        }
        return super.getPostcode();
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return toString(false);
    }

    public String toString(boolean fullFormat) {
        String[] tmpArray = {
            Strings.coalesce(RUSSIA),
            Strings.coalesce(postcode),
            itemAsString(getRegion()),
            itemAsString(getArea()),
            itemAsString(getCity()),
            itemAsString(getSettlement()),
            itemAsString(getStreet()),
            asString("дом", house),
            asString("стр", building),
            asString("кв", flat)
        };
        return Strings.join(fullFormat ? tmpArray : clean(tmpArray), null, ", ");
    }

    private String asString(String prefix, String object) {
        return object == null ? Strings.EMPTY : prefix + " " + object;
    }

    private String[] clean(String[] buf) {
        ArrayList<String> tmp = new ArrayList<String>();
        for (String s : buf) {
            if (Strings.clean(s) != null) {
                tmp.add(s);
            }
        }
        return tmp.toArray(new String[tmp.size()]);
    }
}
