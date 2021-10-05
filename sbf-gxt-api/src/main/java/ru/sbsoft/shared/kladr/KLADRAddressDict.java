package ru.sbsoft.shared.kladr;

import java.io.Serializable;
import ru.sbsoft.common.Strings;

/**
 * @author balandin
 * @since Mar 15, 2013 12:41:57 PM
 */
public class KLADRAddressDict implements Serializable {

    private KLADRItem region;
    private KLADRItem area;
    private KLADRItem city;
    private KLADRItem settlement;
    private KLADRItem street;

    public KLADRAddressDict() {
    }

    public KLADRItem getRegion() {
        return region;
    }

    public void setRegion(KLADRItem region) {
        this.region = region;
    }

    public KLADRItem getArea() {
        return area;
    }

    public void setArea(KLADRItem area) {
        this.area = area;
    }

    public KLADRItem getCity() {
        return city;
    }

    public void setCity(KLADRItem city) {
        this.city = city;
    }

    public KLADRItem getSettlement() {
        return settlement;
    }

    public void setSettlement(KLADRItem settlement) {
        this.settlement = settlement;
    }

    public KLADRItem getStreet() {
        return street;
    }

    public void setStreet(KLADRItem street) {
        this.street = street;
    }

    public String getPostcode() {
        return getPostCode(street, settlement, city, area, region);
    }

    protected String itemAsString(KLADRItem item) {
        return (item == null) ? Strings.EMPTY : item.toString();
    }

    private String getPostCode(KLADRItem... items) {
        for (KLADRItem item : items) {
            if (item != null && item.getPostcode() != null) {
                return item.getPostcode();
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final KLADRAddressDict other = (KLADRAddressDict) obj;
        if (this.region != other.region && (this.region == null || !this.region.equals(other.region))) {
            return false;
        }
        if (this.area != other.area && (this.area == null || !this.area.equals(other.area))) {
            return false;
        }
        if (this.city != other.city && (this.city == null || !this.city.equals(other.city))) {
            return false;
        }
        if (this.settlement != other.settlement && (this.settlement == null || !this.settlement.equals(other.settlement))) {
            return false;
        }
        if (this.street != other.street && (this.street == null || !this.street.equals(other.street))) {
            return false;
        }
        return true;
    }
}
