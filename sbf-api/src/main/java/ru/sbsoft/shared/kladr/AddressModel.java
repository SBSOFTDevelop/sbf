package ru.sbsoft.shared.kladr;

import java.io.Serializable;
import ru.sbsoft.shared.SbStringBuilder;
import ru.sbsoft.shared.param.DTO;

/**
 * Класс представляет модель адреса в справочнике КЛАДР.
 * @author balandin
 */
public class AddressModel implements Serializable, DTO {

    public static final String NAME_HOUSE = "дом ";
    public static final String NAME_BLOCK = "корп ";
    public static final String NAME_FLAT = "кв ";
//    public static final String CODE_EMPTY_REG = "00";
//    public static final String CODE_EMPTY_OTHER = "000";
//    public static final String CODE_EMPTY_STRT = "0000";
    public static final int KLADR_SIZE = 15;
    private String postIndex;
    private String regionCode;
    private String regionName;
    private String areaCode;
    private String areaName;
    private String cityCode;
    private String cityName;
    private String villageCode;
    private String villageName;
    private String streetCode;
    private String streetName;
    private String house;
    private String block;
    private String flat;

    public AddressModel() {
    }

    public String getCodeKLADR() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(checkCode(getRegionCode(), "00"));
        strBuilder.append(checkCode(getAreaCode(), "000"));
        strBuilder.append(checkCode(getCityCode(), "000"));
        strBuilder.append(checkCode(getVillageCode(), "000"));
        strBuilder.append(checkCode(getStreetCode(), "0000"));
        return emptyKLADR(strBuilder.toString());
    }

    public void setCodeKLADR(String codeKLADR) {
        StringBuilder strBuilder = new StringBuilder();
        if (null != codeKLADR && !codeKLADR.isEmpty()) {
            strBuilder.append(codeKLADR);
            // убираем решетку, если есть ------------------------------------
            if ('#' == strBuilder.charAt(0)) {
                strBuilder.deleteCharAt(0);
            }
            // режем до 15 символов ------------------------------------------
            if (strBuilder.length() > KLADR_SIZE) {
                strBuilder.delete(KLADR_SIZE, strBuilder.length());
            }
        }
        // распределяем коды -------------------------------------------------
        if (strBuilder.length() >= 2) {
            regionCode = strBuilder.substring(0, 2);
        } else {
            regionCode = null;
        }
        if (strBuilder.length() >= 5) {
            areaCode = strBuilder.substring(2, 5);
        } else {
            areaCode = null;
        }
        if (strBuilder.length() >= 8) {
            cityCode = strBuilder.substring(5, 8);
        } else {
            cityCode = null;
        }
        if (strBuilder.length() >= 11) {
            villageCode = strBuilder.substring(8, 11);
        } else {
            villageCode = null;
        }
        if (strBuilder.length() >= 15) {
            streetCode = strBuilder.substring(11, 15);
        } else {
            streetCode = null;
        }
    }

//    public boolean isValidCodeKLADR() {
//        return !CODE_EMPTY_REG.equals(getRegionCode()) && (!CODE_EMPTY_OTHER.equals(getCityCode()) || !CODE_EMPTY_OTHER.equals(getVillageCode()) || !CODE_EMPTY_STRT.equals(getStreetCode()));
//    }
    public String getPostIndex() {
        return postIndex;
    }

    public void setPostIndex(String postIndex) {
        this.postIndex = postIndex;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public String getRegionNameOnly() {
        return getClearName(regionName);
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public String getAreaNameOnly() {
        return getClearName(areaName);
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCityNameOnly() {
        return getClearName(cityName);
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public String getVillageName() {
        return villageName;
    }

    public String getVillageNameOnly() {
        return getClearName(villageName);
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getStreetCode() {
        return streetCode;
    }

    public void setStreetCode(String streetCode) {
        this.streetCode = streetCode;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getStreetNameOnly() {
        return getClearName(streetName);
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    private String checkCode(String code, String emptyCode) {
        if (code == null || code.equalsIgnoreCase("null")) {
            code = emptyCode;
        }
        return validateCode(code, emptyCode);
    }

    private String validateCode(String code, String emptyCode) {
        boolean valid = true;
        String possibleSymbols = "0123456789";
        char[] symbols = code.toCharArray();
        for (char c : symbols) {
            if (possibleSymbols.indexOf(c) == -1) {
                valid = false;
                break;
            }
        }

        if (valid) {
            return code;
        }

        return emptyCode;
    }

    @Override
    public String toString() {
        return getFullAddress();
    }

    public String getFullAddress() {
        final SbStringBuilder sb = new SbStringBuilder();
        sb.joinNotEmptyValue(streetName);
        sb.joinNotEmptyValue(NAME_HOUSE, house);
        sb.joinNotEmptyValue(NAME_BLOCK, block);
        sb.joinNotEmptyValue(NAME_FLAT, flat);
        sb.joinNotEmptyValue(villageName);
        sb.joinNotEmptyValue(cityName);
        sb.joinNotEmptyValue(areaName);
        sb.joinNotEmptyValue(regionName);
        sb.joinNotEmptyValue(postIndex);
        return sb.toString();
    }

    public String getFullAddressForLetter() {
        final SbStringBuilder sb = new SbStringBuilder();
        sb.joinNotEmptyValue(streetName);
        sb.joinNotEmptyValue(NAME_HOUSE, house);
        sb.joinNotEmptyValue(NAME_BLOCK, block);
        sb.joinNotEmptyValue(NAME_FLAT, flat);
        sb.joinNotEmptyValue(villageName);
        sb.joinNotEmptyValue(cityName);
        sb.joinNotEmptyValue(areaName);
        sb.joinNotEmptyValue(regionName);
        sb.joinNotEmptyValue(postIndex);

        return sb.toString();
    }

    private String getClearName(final String name) {
        if (null == name || name.trim().isEmpty()) {
            return null;
        }
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(name.trim());
        int pos = strBuilder.lastIndexOf(" ");
        if (-1 == pos) {
            return name.trim();
        } else {
            return strBuilder.substring(0, pos);
        }
    }

//    public BigDecimal getIDByCode(final String code) {
//        if (null == code || code.isEmpty()) {
//            return BigDecimal.ZERO;
//        }
//        try {
//            int i = Integer.valueOf(code);
//            if (0 == i) {
//                return BigDecimal.ZERO;
//            }
//            return new BigDecimal(i);
//        } catch (Exception exp) {
//            return BigDecimal.ZERO;
//        }
//    }
//    public void clearCodes() {
//        setRegionCode(null);
//        setAreaCode(null);
//        setCityCode(null);
//        setVillageCode(null);
//        setStreetCode(null);
//    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AddressModel other = (AddressModel) obj;
        if ((this.postIndex == null) ? (other.postIndex != null) : !this.postIndex.equals(other.postIndex)) {
            return false;
        }
        if ((this.regionCode == null) ? (other.regionCode != null) : !this.regionCode.equals(other.regionCode)) {
            return false;
        }
        if ((this.regionName == null) ? (other.regionName != null) : !this.regionName.equals(other.regionName)) {
            return false;
        }
        if ((this.areaCode == null) ? (other.areaCode != null) : !this.areaCode.equals(other.areaCode)) {
            return false;
        }
        if ((this.areaName == null) ? (other.areaName != null) : !this.areaName.equals(other.areaName)) {
            return false;
        }
        if ((this.cityCode == null) ? (other.cityCode != null) : !this.cityCode.equals(other.cityCode)) {
            return false;
        }
        if ((this.cityName == null) ? (other.cityName != null) : !this.cityName.equals(other.cityName)) {
            return false;
        }
        if ((this.villageCode == null) ? (other.villageCode != null) : !this.villageCode.equals(other.villageCode)) {
            return false;
        }
        if ((this.villageName == null) ? (other.villageName != null) : !this.villageName.equals(other.villageName)) {
            return false;
        }
        if ((this.streetCode == null) ? (other.streetCode != null) : !this.streetCode.equals(other.streetCode)) {
            return false;
        }
        if ((this.streetName == null) ? (other.streetName != null) : !this.streetName.equals(other.streetName)) {
            return false;
        }
        if ((this.house == null) ? (other.house != null) : !this.house.equals(other.house)) {
            return false;
        }
        if ((this.block == null) ? (other.block != null) : !this.block.equals(other.block)) {
            return false;
        }
        return !((this.flat == null) ? (other.flat != null) : !this.flat.equals(other.flat));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.postIndex != null ? this.postIndex.hashCode() : 0);
        hash = 97 * hash + (this.regionCode != null ? this.regionCode.hashCode() : 0);
        hash = 97 * hash + (this.regionName != null ? this.regionName.hashCode() : 0);
        hash = 97 * hash + (this.areaCode != null ? this.areaCode.hashCode() : 0);
        hash = 97 * hash + (this.areaName != null ? this.areaName.hashCode() : 0);
        hash = 97 * hash + (this.cityCode != null ? this.cityCode.hashCode() : 0);
        hash = 97 * hash + (this.cityName != null ? this.cityName.hashCode() : 0);
        hash = 97 * hash + (this.villageCode != null ? this.villageCode.hashCode() : 0);
        hash = 97 * hash + (this.villageName != null ? this.villageName.hashCode() : 0);
        hash = 97 * hash + (this.streetCode != null ? this.streetCode.hashCode() : 0);
        hash = 97 * hash + (this.streetName != null ? this.streetName.hashCode() : 0);
        hash = 97 * hash + (this.house != null ? this.house.hashCode() : 0);
        hash = 97 * hash + (this.block != null ? this.block.hashCode() : 0);
        hash = 97 * hash + (this.flat != null ? this.flat.hashCode() : 0);
        return hash;
    }

    private String emptyKLADR(String stringKlader) {
        for (char c : stringKlader.toCharArray()) {
            if (c != '0') {
                return stringKlader;
            }
        }

        return "";
    }
}
