package ru.sbsoft.shared.kladr;

/**
 *
 * @author kiselev
 */
public class AddrUtil {

    public static enum AddrType {

        DLV, REG
    };

    //------ Addres prefixes
    private static final String PREF_HOUSE = "дом ";
    private static final String PREF_BLOCK = "корп. ";
    private static final String PREF_FLAT = "кв. ";

    public static String enrichOldAddrStr(final String pindex, final String oldAddr, final String house, final String block, final String flat) {
        String laddr = oldAddr != null ? oldAddr.toLowerCase() : "";
        String[] args = new String[5];
        int i = 0;
        args[i++] = pindex;
        args[i++] = oldAddr;
        args[i++] = getHousePart(PREF_HOUSE, house, laddr);
        args[i++] = getHousePart(PREF_BLOCK, block, laddr);
        args[i++] = getHousePart(PREF_FLAT, flat, laddr);
        return joinAdr(args);
    }

    public static String getAddrStr(final String pindex, final String region, final String area, final String city, final String village, final String street, final String house, final String block, final String flat) {
        String[] args = new String[9];
        int i = 0;
        args[i++] = pindex;
        args[i++] = region;
        args[i++] = area;
        args[i++] = city;
        args[i++] = village;
        args[i++] = street;
        args[i++] = getHousePart(PREF_HOUSE, house);
        args[i++] = getHousePart(PREF_BLOCK, block);
        args[i++] = getHousePart(PREF_FLAT, flat);
        return joinAdr(args);
    }

    public static String getAddrStr(final AddressModel addr) {
        return getAddrStr(addr, false);
    }

    public static String getAddrStr(final AddressModel addr, boolean shortAddr) {
        AddressModel a = cleanup(addr);
        String[] args;
        int i = 0;
        if (shortAddr) {
            args = new String[7];
        } else {
            args = new String[9];
            args[i++] = a.getPostIndex();
            args[i++] = a.getRegionName();
        }
        args[i++] = a.getAreaName();
        args[i++] = a.getCityName();
        args[i++] = a.getVillageName();
        args[i++] = a.getStreetName();
        args[i++] = getHousePart(PREF_HOUSE, a.getHouse());
        args[i++] = getHousePart(PREF_BLOCK, a.getBlock());
        args[i++] = getHousePart(PREF_FLAT, a.getFlat());
        return joinAdr(args);
    }

    public static AddressModel getAddr(final String pindex, final String region, final String area, final String city, final String village, final String street, final String house, final String block, final String flat) {
        AddressModel addr = new AddressModel();
        addr.setPostIndex(pindex);
        addr.setRegionName(region);
        addr.setAreaName(area);
        addr.setCityName(city);
        addr.setVillageName(village);
        addr.setStreetName(street);
        addr.setHouse(house);
        addr.setBlock(block);
        addr.setFlat(flat);
        return cleanup(addr);
    }

    public static boolean isAddrOk(final AddressModel addr) {
        return isAddrOk(addr, false);
    }
    
    public static boolean isAddrOk(final AddressModel addr, boolean asKladdr) {
        AddressModel a = cleanup(addr);
        //fedaral towns
        String reg = a.getRegionName() != null ? a.getRegionName().toLowerCase() : null;
        if (reg != null && (reg.endsWith(" г") || reg.startsWith("г ") || reg.endsWith(" г.") || reg.startsWith("г.")) && a.getStreetName() != null && (asKladdr || a.getHouse() != null)) {
            return true;
        }
        String area = a.getAreaName() != null ? a.getAreaName().toLowerCase() : null;
        if (area != null && (area.endsWith(" п") || area.startsWith("п ") || area.endsWith(" п.") || area.startsWith("п.")) && a.getStreetName() != null && (asKladdr || a.getHouse() != null)) {
            return true;
        }
        return a.getRegionName() != null && (a.getVillageName() != null || (a.getCityName() != null && (asKladdr || a.getHouse() != null)));
    }

    public static boolean isAddrEmpty(final AddressModel addr) {
        return addr.getPostIndex() == null && addr.getRegionName() == null && addr.getAreaName() == null && addr.getCityName() == null && addr.getVillageName() == null && addr.getStreetName() == null && addr.getHouse() == null && addr.getBlock() == null && addr.getFlat() == null;
    }

    private static String getHousePart(final String pref, String val) {
        return getHousePart(pref, val, null);
    }

    private static String getHousePart(final String pref, String val, String adrStr) {
        if (val != null && !(val = val.trim()).isEmpty()) {
            String cleanPref = pref.trim();
            if (cleanPref.endsWith(".")) {
                cleanPref = cleanPref.substring(0, cleanPref.length() - 1);
            }
            if (adrStr == null || !adrStr.contains(cleanPref)) {
                if (val.toLowerCase().contains(cleanPref)) {
                    return val;
                } else {
                    return pref + val;
                }
            }
        }
        return null;
    }

    public static AddressModel cleanup(final AddressModel addr) {
        addr.setRegionName(cleanup(addr.getRegionName()));
        addr.setAreaName(cleanup(addr.getAreaName()));
        addr.setCityName(cleanup(addr.getCityName()));
        addr.setVillageName(cleanup(addr.getVillageName()));
        addr.setStreetName(cleanup(addr.getStreetName()));
        addr.setHouse(cleanup(addr.getHouse()));
        addr.setBlock(cleanup(addr.getBlock()));
        addr.setFlat(cleanup(addr.getFlat()));
        return addr;
    }

    private static String cleanup(String addrStr) {
        if (addrStr != null && (addrStr = addrStr.trim()).length() > 0) {
            //убираем с концов паразитные запятые, если есть, т.к. это разделитель в адресе
            int p0 = 0;
            while (p0 < addrStr.length() && (',' == addrStr.charAt(p0) || ' ' == addrStr.charAt(p0))) {
                p0++;
            }
            if (p0 >= addrStr.length()) {
                return null;
            }
            int p1 = addrStr.length() - 1;
            while (p1 >= 0 && (',' == addrStr.charAt(p1) || ' ' == addrStr.charAt(p1))) {
                p1--;
            }
            if (p1 < p0) {
                return null;
            }
            p1++;
            if (p0 == 0 && p1 == addrStr.length()) {
                return addrStr;
            }
            return addrStr.substring(p0, p1);
        }
        return null;
    }

    private static String joinAdr(final String... strs) {
        return join(", ", true, strs);
    }

    public static String join(String divider, boolean dropEmpty, final String... strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }
        if (divider == null) {
            divider = "";
        }
        int totalLen = 0;
        for (String str : strs) {
            if (str != null) {
                totalLen += str.length();
            }
        }
        StringBuilder buf = new StringBuilder(totalLen + strs.length);
        for (String str : strs) {
            if (!dropEmpty || (str != null && (str = str.trim()).length() > 0)) {
                buf.append(str).append(divider);
            }
        }
        if (buf.length() > 0) {
            buf.setLength(buf.length() - divider.length());
        }
        return buf.toString();
    }

}
