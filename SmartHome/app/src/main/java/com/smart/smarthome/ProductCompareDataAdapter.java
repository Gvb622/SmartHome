package com.smart.smarthome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Korn on 9/3/2016.
 */
public class ProductCompareDataAdapter extends BaseAdapter {
    ArrayList<ProductCompare> productCompares = new ArrayList<ProductCompare>();
    Context context;
    public ProductCompareDataAdapter(Context context, ArrayList<ProductCompare> productCompares){
        this.context = context;
        this.productCompares = productCompares;
    }

    public int getCount(){
        return productCompares.size();
    }

    public Object getItem(int position){
        return productCompares.get(position);
    }

    public long getItemId(int position){
        return 0;
    }

    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater mInflater =
                (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null)
            view = mInflater.inflate(R.layout.product_compare_data_list, parent, false);

        TextView productName = (TextView) view.findViewById(R.id.textViewProductName);
        TextView productPrice = (TextView) view.findViewById(R.id.textViewProductPrice);
        TextView productVolume = (TextView) view.findViewById(R.id.textViewProductVolume);
        TextView productUnit = (TextView) view.findViewById(R.id.textViewProductUnit);
        TextView productCompare = (TextView) view.findViewById(R.id.textViewProductcompare);
        Double compare;
        compare = productCompares.get(position).getPrice()/(productCompares.get(position).getUnit()*productCompares.get(position).getVolume());

        productCompare.setText(String.format( "%.3f", compare ));
        productUnit.setText(""+productCompares.get(position).getUnit());
        productVolume.setText(""+productCompares.get(position).getVolume());
        productPrice.setText(productCompares.get(position).getPrice().toString());
        productName.setText(productCompares.get(position).getName());
        return view;
    }


}
