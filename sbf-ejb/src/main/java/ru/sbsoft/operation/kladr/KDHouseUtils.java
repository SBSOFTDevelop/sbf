package ru.sbsoft.operation.kladr;

import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.common.Strings;

/**
 * @author balandin
 * @since Mar 15, 2013 12:59:33 PM
 */
public class KDHouseUtils {

    public static List<KDHouseItem> getElements(final String listHouses) {
        if (Strings.isEmpty(listHouses)) {
            return null;
        }
        List<KDHouseItem> list = new ArrayList<KDHouseItem>();
        int b = 0;
        for (int i = 0; i < listHouses.length(); i++) {
            if (',' == listHouses.charAt(i)) {
                list.add(new KDHouseItem(listHouses.substring(b, i)));
                b = i + 1;
            }
        }
        if (b < listHouses.length() && listHouses.charAt(listHouses.length() - 1) != ',') {
            list.add(new KDHouseItem(listHouses.substring(b)));
        }
        return list;
    }

    public static int getClearHouse(final String house) {
        if (Strings.isEmpty(house)) {
            return -1;
        }
        String numHouse = house;
        for (int i = 0; i < house.length(); i++) {
            char ch = house.charAt(i);
            if (ch < '0' || ch > '9') {
                if (0 == i) {
                    // неверный формат дома - выходим
                    return -1;
                }
                numHouse = house.substring(0, i);
            }
        }
        try {
            return Integer.parseInt(numHouse);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private static int findHouse(final List<KDHouseItem> list, final String house) {
        if (Strings.isEmpty(house)) {
            return -1;
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getType() == KDHouseItem.HouseType.SINGLE && house.equals(list.get(i).getText())) {
                return i;
            }
        }
        return -1;
    }

    public static boolean checkEqHouse(final String listHouses, final String house, final String block) {
        List<KDHouseItem> listHouse = getElements(listHouses);
        String upHouse = house.trim().toUpperCase();
        String fnHouse;
        if (!Strings.isEmpty(block)) {
            String upBlock = block.trim().toUpperCase();
            fnHouse = upHouse + 'К' + upBlock;
            if (findHouse(listHouse, fnHouse) >= 0) {
                return true;
            }
            fnHouse = upHouse + "СТР" + upBlock;
            if (findHouse(listHouse, fnHouse) >= 0) {
                return true;
            }
        }
        return findHouse(listHouse, upHouse) >= 0;
    }

    public static boolean checkEnHouse(final String listHouses, final String house) {
        int numHouse = getClearHouse(house);
        if (-1 == numHouse) {
            return false;
        }
        List<KDHouseItem> listHouse = getElements(listHouses);
        for (KDHouseItem item : listHouse) {
            if (item.getType() == KDHouseItem.HouseType.MULTY || item.getType() == KDHouseItem.HouseType.EVEN || item.getType() == KDHouseItem.HouseType.ODD) {
                if (numHouse >= item.getBegNumber() && numHouse <= item.getEndNumber()) {
                    if (item.getType() == KDHouseItem.HouseType.EVEN) {
                        if (numHouse % 2 == 0) {
                            return true;
                        }
                    } else if (item.getType() == KDHouseItem.HouseType.ODD) {
                        if (numHouse % 2 != 0) {
                            return true;
                        }
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
