package com.smart.smarthome;

import java.util.ArrayList;

/**
 * Created by Korn on 9/3/2016.
 */
public class ProductCompareData {
    static ArrayList<ProductCompare> compareProductCompareData = new ArrayList<ProductCompare>();
    public static void add(ProductCompare productCompare){
        compareProductCompareData.add(productCompare);
    }

    public static void remove(int index){
        compareProductCompareData.remove(index);
    }

    public static ProductCompare get(int index){
        return compareProductCompareData.get(index);
    }

    public static ArrayList<ProductCompare> getAll(){
        return compareProductCompareData;
    }
}
