package com.talshavit.groupbuyproject.Models;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

public class CityManager {
    private ArrayList<City> cities;

    public CityManager() {
        cities = new ArrayList<>();
        initCities();
    }

    private void initCities() {
        addCity("תל אביב", new LatLng(32.0853, 34.7818), createPoints(
                "אוניברסיטת תל אביב", new LatLng(32.1093, 34.8554),
                "דיזינגוף סנטר", new LatLng(32.0753, 34.7747),
                "שוק הכרמל", new LatLng(32.0686, 34.7699),
                "התחנה המרכזית החדשה", new LatLng(32.0564, 34.7798),
                "כיכר המדינה", new LatLng(32.0840, 34.7873),
                "נמל תל אביב", new LatLng(32.0965, 34.7748),
                "רוטשילד 12", new LatLng(32.0622, 34.7726),
                "סוזן דלל", new LatLng(32.0568, 34.7655),
                "פארק הירקון", new LatLng(32.1009, 34.8148)
        ));

        addCity("ירושלים", new LatLng(31.7683, 35.2137), createPoints(
                "גשר המיתרים", new LatLng(31.7858, 35.2007),
                "גן סאקר", new LatLng(31.7814, 35.2050),
                "מוזיאון ישראל", new LatLng(31.7735, 35.2041),
                "שוק מחנה יהודה", new LatLng(31.7850, 35.2135),
                "מגדל דוד", new LatLng(31.7767, 35.2296),
                "עיר דוד", new LatLng(31.7741, 35.2335),
                "קניון מלחה", new LatLng(31.7514, 35.1883),
                "תחנת הרכבת הראשונה", new LatLng(31.7683, 35.2231),
                "הגן הבוטני בירושלים", new LatLng(31.7720, 35.1959)
        ));

        addCity("באר שבע", new LatLng(31.2520, 34.7915), createPoints(
                "אוניברסיטת בן-גוריון בנגב", new LatLng(31.2622, 34.8013),
                "העיר העתיקה", new LatLng(31.2439, 34.7931),
                "המרכז הרפואי סורוקה", new LatLng(31.2558, 34.7935),
                "פארק קרסו למדע", new LatLng(31.2523, 34.7910),
                "גרנד קניון באר שבע", new LatLng(31.2456, 34.7876),
                "תחנת הרכבת באר שבע מרכז", new LatLng(31.2513, 34.8007),
                "גן החיות נגב זואולוגי", new LatLng(31.2315, 34.7557),
                "המוזיאון לתרבות האסלאם ועמי המזרח", new LatLng(31.2454, 34.7912),
                "פארק נחל באר שבע", new LatLng(31.2367, 34.7955),
                "מרכז קניות ONE Plaza", new LatLng(31.2434, 34.8068)
        ));

        addCity("חיפה", new LatLng(32.7940, 34.9896), createPoints(
                "הגנים הבהאיים", new LatLng(32.8151, 34.9896),
                "המושבה הגרמנית", new LatLng(32.8230, 34.9891),
                "הטכניון", new LatLng(32.7775, 35.0217),
                "מדעטק - המוזיאון הלאומי למדע", new LatLng(32.8072, 34.9851),
                "קניון חיפה", new LatLng(32.7773, 34.9571),
                "פארק הכרמל", new LatLng(32.7433, 35.0466),
                "שוק תלפיות", new LatLng(32.8087, 34.9952),
                "מוזיאון חיפה לאמנות", new LatLng(32.8149, 34.9896)
        ));

        addCity("נתניה", new LatLng(32.3215, 34.8532), createPoints(
                "כיכר העצמאות", new LatLng(32.3281, 34.8563),
                "קניון השרון", new LatLng(32.3321, 34.8546),
                "פארק אגם החורף", new LatLng(32.3163, 34.8677),
                "היכל התרבות נתניה", new LatLng(32.3213, 34.8591),
                "מוזיאון נתניה", new LatLng(32.3300, 34.8551),
                "איצטדיון נתניה", new LatLng(32.2866, 34.8690),
                "קניון עיר ימים", new LatLng(32.2832, 34.8378),
                "שמורת האירוסים", new LatLng(32.28183357227295, 34.8401975676341)
        ));

        addCity("ראשון לציון", new LatLng(31.9730, 34.7925), createPoints(
                "קניון הזהב", new LatLng(31.9903, 34.7795),
                "חוף הים ראשון לציון", new LatLng(31.9812, 34.7488),
                "אצטדיון הברפלד", new LatLng(31.9703, 34.7986),
                "פארק נאות שקמה", new LatLng(31.9756, 34.7704),
                "המוזיאון העירוני ראשון לציון", new LatLng(31.9645, 34.8042),
                "היכל התרבות ראשון לציון", new LatLng(31.9696, 34.8087),
                "הגן העירוני", new LatLng(31.9655, 34.8060),
                "תחנת רכבת משה דיין", new LatLng(31.9762, 34.7707),
                "קניון גן העיר", new LatLng(31.9662, 34.8083),
                "בית העירייה", new LatLng(31.9648, 34.8047)
        ));

        addCity("אשדוד", new LatLng(31.8014, 34.6434), createPoints(
                "המרינה", new LatLng(31.8087, 34.6232),
                "קניון הסיטי", new LatLng(31.8015, 34.6502),
                "פארק אשדוד ים", new LatLng(31.7941, 34.6347),
                "אצטדיון הי״א", new LatLng(31.8061, 34.6576),
                "היכל התרבות אשדוד", new LatLng(31.8031, 34.6524),
                "הגן העירוני אשדוד", new LatLng(31.8057, 34.6559),
                "תחנת רכבת עד הלום", new LatLng(31.7697, 34.6451),
                "בית העירייה אשדוד", new LatLng(31.8038, 34.6498),
                "המוזיאון לתרבות הפלשתים", new LatLng(31.7998, 34.6563)
        ));

        addCity("פתח תקווה", new LatLng(32.0840, 34.8878), createPoints(
                "פארק יד לבנים", new LatLng(32.0876, 34.8897),
                "קניון אבנת", new LatLng(32.0900, 34.8872),
                "היכל התרבות פתח תקווה", new LatLng(32.0871, 34.8868),
                "בית חולים בלינסון", new LatLng(32.0916, 34.8650),
                "בית יד לבנים", new LatLng(32.0855, 34.8910),
                "מוזיאון פתח תקווה לאמנות", new LatLng(32.0846, 34.8900),
                "פארק עופר", new LatLng(32.0895, 34.8934),
                "שוק פתח תקווה", new LatLng(32.0845, 34.8828),
                "תחנת רכבת קרית אריה", new LatLng(32.1048, 34.8636),
                "בית העירייה פתח תקווה", new LatLng(32.0840, 34.8880)
        ));

        addCity("אילת", new LatLng(29.5581, 34.9482), createPoints(
                "ריף הדולפינים", new LatLng(29.5010, 34.9167),
                "מצפה תת-ימי", new LatLng(29.5014, 34.9210),
                "קניון מול הים", new LatLng(29.5559, 34.9519),
                "המרינה", new LatLng(29.5542, 34.9516),
                "מוזיאון אילת", new LatLng(29.5572, 34.9531),
                "פארק תמנע", new LatLng(29.7870, 34.9630),
                "שדה התעופה רמון", new LatLng(29.7281, 35.0125),
                "בית העירייה אילת", new LatLng(29.5583, 34.9533),
                "פארק הצפרות", new LatLng(29.5737, 34.9748)
        ));

        addCity("מודיעין", new LatLng(31.890166726302127, 35.009355084964255), createPoints(
                "קניון עזריאלי מודיעין", new LatLng(31.90016075367011, 35.00888806891325),
                "פארק ענבה", new LatLng(31.896936349736382, 35.004888282961744),
                "היכל התרבות מודיעין", new LatLng(31.89934205940945, 35.014683009948975),
                "תחנת רכבת מודיעין מרכז", new LatLng(31.901336357755827, 35.005669911797476),
                "הספרייה העירונית מודיעין", new LatLng(31.89880927941366, 35.01528153642388),
                "מרכז הספורט העירוני", new LatLng(31.902240314412538, 35.00205995784806),
                "עיריית מודיעין", new LatLng(31.907876351232776, 35.00753750810103),
                "טרם מודיעין", new LatLng(31.905529569164823, 35.00734534858153)
        ));
    }

    private void addCity(String name, LatLng location, HashMap<String, LatLng> points) {
        City city = new City(name, location);
        city.points.putAll(points);
        cities.add(city);
    }

    private HashMap<String, LatLng> createPoints(Object... points) {
        HashMap<String, LatLng> pointsMap = new HashMap<>();
        for (int i = 0; i < points.length; i += 2) {
            pointsMap.put((String) points[i], (LatLng) points[i + 1]);
        }
        return pointsMap;
    }

    public ArrayList<City> getCities() {
        return cities;
    }
}
