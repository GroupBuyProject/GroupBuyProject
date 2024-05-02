package com.talshavit.groupbuyproject.models;

import java.util.ArrayList;

public class ArrayListCategory {

    public ArrayList<CategoriesModel> setArrayList(){
        ArrayList<CategoriesModel> arrayList = new ArrayList<>();

        arrayList.add(new CategoriesModel("פירות וירקות"));
        arrayList.add(new CategoriesModel("מוצרי חלב וביצים"));
        arrayList.add(new CategoriesModel("בשר עוף ודגים"));
        arrayList.add(new CategoriesModel("לחמים"));
        arrayList.add(new CategoriesModel("משקאות, יין אלכוהול וטבק"));
        arrayList.add(new CategoriesModel("סלטים ונקניקים"));
        arrayList.add(new CategoriesModel("מוצרי אפיה ושימורים"));
        arrayList.add(new CategoriesModel("חטיפים, דגני בוקר, עוגות ועוגיות"));
        arrayList.add(new CategoriesModel("פיצוחים, תבלינים ופירות יבשים"));
        arrayList.add(new CategoriesModel("מוצרי ניקיון וחד פעמי"));

        return arrayList;
    }
}
