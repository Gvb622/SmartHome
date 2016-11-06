package com.smart.smarthome;

import android.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.awareness.snapshot.PlacesResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class Tab18 extends AppCompatActivity {


    private String value;
    private RecyclerView mList;
    private Query qType;
    private Query pType;
    private TextView name;

    private Query FoodType;

    static DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private ImageButton AddItem2;

    static public ImageButton IncreaseItem;
    static public ImageButton DecreaseItem;
    static public ImageButton RemoveItem;
    FirebaseUser user;
    DatabaseReference User;

    String m_Text;
    String p_Text;
    Double Volum;
    String key;
    String type;
    String Poskey;
    public static String g = "";
    String locate = "";
    FirebaseRecyclerAdapter<Shoplistitem, ItemViewHolder> firebaseRecyclerAdapter;


    item i;

    double TotalRetailPrice = 0;
    double TotalTopsPrice = 0;
    double TotalLotusPrice = 0;
    double volumnadd;
    double volumnFin;

    Shoplistitem shopitem2;

    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab8);

        mGoogleApiClient = new GoogleApiClient.Builder(Tab18.this)
                .addApi(Awareness.API)
                .addApi(AppIndex.API).build();
        mGoogleApiClient.connect();

        name = (TextView) findViewById(R.id.TextviewShop);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        locate = Tab16.locate;
        g = Tab16.g;


        final DatabaseReference fac = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("Health and Beauty");
        fac.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Shoplistitem shoplist = postSnapshot.getValue(Shoplistitem.class);

                    if (shoplist.getShopCheapest().equals(Tab16.locate)) {
                        DatabaseReference f = fac.child(postSnapshot.getKey());
                        f.child("ShopCheapestNum").setValue(0);
                    } else {
                        DatabaseReference f = fac.child(postSnapshot.getKey());
                        f.child("ShopCheapestNum").setValue(1);
                    }
                }
                mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("Health and Beauty");
                name.setText("Now you are at :  " + Tab16.g);
                qType = mDatabase.orderByChild("ShopCheapestNum");
                pType = mDatabase.child("all").orderByKey();

                attachRecyclerViewAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mList = (RecyclerView) findViewById(R.id.item_listShopping);

        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(this, R.drawable.list_divider), true));


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageButton increaseImageButton;


        public ItemViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            increaseImageButton = (ImageButton) mView.findViewById(R.id.IncreaseImageButton);

        }

        public void setName2(String name) {
            TextView Name = (TextView) mView.findViewById(R.id.nameShopItem);
            Name.setText(name);
        }

        public void setVolumn2(String volumn) {
            TextView Volumn = (TextView) mView.findViewById(R.id.volumnShopItem);
            Volumn.setText(volumn);
        }

        public void setPrice2(String price) {
            TextView Price = (TextView) mView.findViewById(R.id.priceShopItem2);
            Price.setText(price);
        }

        public void setPrice3(String price) {
            TextView Price2 = (TextView) mView.findViewById(R.id.priceShopItem3);
            Price2.setText(price);
        }

        public void setPrice3Color(int color) {
            TextView Price2 = (TextView) mView.findViewById(R.id.priceShopItem3);
            Price2.setTextColor(color);
        }

        public void setImage2(Context ctx, String image) {
            ImageView imageView = (ImageView) mView.findViewById(R.id.imageShopItem);
            Picasso.with(ctx).load(image).fit().placeholder(R.mipmap.ic_launcher).into(imageView);
        }


    }



    public void attachRecyclerViewAdapter() {

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Shoplistitem, ItemViewHolder>(

                Shoplistitem.class,
                R.layout.shoppinglistitem,
                ItemViewHolder.class,
                qType
        ) {
            @Override
            protected void populateViewHolder(ItemViewHolder viewHolder, final Shoplistitem model, final int position) {

                viewHolder.setName2(model.getItemName());

                viewHolder.setPrice2(model.getItemPrice() + " /" + model.getItemClassifier());

                viewHolder.setVolumn2(model.getItemVolumn() + " " + model.getItemClassifier());
                viewHolder.setImage2(getApplicationContext(), model.getItemImage());

                try {

                    String Cheaper = model.getShopCheapest();
                    if(Cheaper.contains(locate)){

                        if(locate.equals("Tops")){
                            viewHolder.setPrice3(locate+  " : " + model.getItemTopsPrice());
                        }else if(locate.equals("Lotus")){
                            viewHolder.setPrice3(locate+  " : " + model.getItemLotusPrice());
                        }else if(locate.equals("BigC")){
                            viewHolder.setPrice3(locate+  " : " + model.getItemBigCPrice());
                        }else if(locate.equals("Foodland")){
                            viewHolder.setPrice3(locate+  " : " + model.getItemFoodLandPrice());
                        }else if(locate.equals("HomeFreshMart")){
                            viewHolder.setPrice3(locate+  " : " + model.getItemHomeFreshMartPrice());
                        }else if(locate.equals("MaxValue")){
                            viewHolder.setPrice3(locate+  " : " + model.getItemMaxValuePrice());
                        }else if(locate.equals("Makro")){
                            viewHolder.setPrice3(locate+  " : " + model.getItemMakroPrice());
                        }

                    }else{

                        if(locate.equals("Tops")){
                            viewHolder.setPrice3(locate+  " : " + model.getItemTopsPrice());
                            viewHolder.setPrice3Color(Color.BLACK);

                        }else if(locate.equals("Lotus")){
                            viewHolder.setPrice3(locate+  " : " + model.getItemLotusPrice());
                            viewHolder.setPrice3Color(Color.BLACK);

                        }else if(locate.equals("BigC")){
                            viewHolder.setPrice3(locate+  " : " + model.getItemBigCPrice());
                            viewHolder.setPrice3Color(Color.BLACK);

                        }else if(locate.equals("Foodland")){
                            viewHolder.setPrice3(locate+  " : " + model.getItemFoodLandPrice());
                            viewHolder.setPrice3Color(Color.BLACK);

                        }else if(locate.equals("HomeFreshMart")){
                            viewHolder.setPrice3(locate+  " : " + model.getItemHomeFreshMartPrice());
                            viewHolder.setPrice3Color(Color.BLACK);

                        }else if(locate.equals("MaxValue")){
                            viewHolder.setPrice3(locate+  " : " + model.getItemMaxValuePrice());
                            viewHolder.setPrice3Color(Color.BLACK);

                        }else if(locate.equals("Makro")){
                            viewHolder.setPrice3(locate+  " : " + model.getItemMakroPrice());
                            viewHolder.setPrice3Color(Color.BLACK);
                        }
                    }

                } catch (Exception e) {

                }
                viewHolder.increaseImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final DatabaseReference s = firebaseRecyclerAdapter.getRef(position);
                        type = s.getParent().getKey();
                        s.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final Shoplistitem Shopitem = dataSnapshot.getValue(Shoplistitem.class);
                                key = Shopitem.getKey();
                                Volum = Shopitem.getItemVolumn();
                                User = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("items")
                                        .child(type).child(key);
                                User.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        i = dataSnapshot.getValue(item.class);
                                        double volumn = Double.parseDouble(i.getUnit());
                                        volumn= Double.parseDouble(new DecimalFormat("##.##").format(volumn));

                                        volumnadd = Volum;
                                        volumnFin = volumn + volumnadd;

                                        volumnFin = Double.parseDouble(new DecimalFormat("##.##").format(volumnFin));
                                        String Price = null;

                                        if(locate.equals("Tops")){
                                            Price = locate + " : " + model.getItemTopsPrice();
                                        }else if(locate.equals("Lotus")){
                                            Price = locate + " : " + model.getItemLotusPrice();

                                        }else if(locate.equals("BigC")){
                                            Price = locate + " : " + model.getItemBigCPrice();

                                        }else if(locate.equals("Foodland")){
                                            Price = locate + " : " + model.getItemFoodLandPrice();

                                        }else if(locate.equals("HomeFreshMart")){
                                            Price = locate + " : " + model.getItemHomeFreshMartPrice();

                                        }else if(locate.equals("MaxValue")){
                                            Price = locate + " : " + model.getItemMaxValuePrice();

                                        }else if(locate.equals("Makro")){
                                            Price = locate + " : " + model.getItemMakroPrice();

                                        }

                                        CharSequence priceItem[] = new CharSequence[] {Price , "Buy other price"};
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(Tab18.this);
                                        builder.setTitle("Choose Shop and Price that You buy");
                                        builder.setItems(priceItem, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int which) {

                                                Calendar c = Calendar.getInstance();
                                                final long mill = c.getTimeInMillis();
                                                SimpleDateFormat sfd = new SimpleDateFormat("yyyyMMdd");
                                                SimpleDateFormat sfd2 = new SimpleDateFormat("MM");
                                                final String date2 = sfd.format(mill);
                                                final String month = sfd2.format(mill);

                                                if(which == 0){

                                                    DatabaseReference report = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("report").child(String.valueOf(mill));
                                                    DatabaseReference addReport = report;
                                                    addReport.child("Time").setValue(date2);
                                                    addReport.child("Month").setValue(month);
                                                    addReport.child("keyValue").setValue(key);
                                                    addReport.child("Name").setValue(i.getName());
                                                    addReport.child("Type").setValue(type);
                                                    addReport.child("Barcode").setValue(i.getBarcode());
                                                    addReport.child("Unit").setValue(Volum+"");
                                                    addReport.child("NormalPrice").setValue(i.getRetailPrice());

                                                    Double priceitem = null;

                                                    if(locate.equals("Tops")){
                                                        priceitem = Double.parseDouble(model.getItemTopsPrice());
                                                    }else if(locate.equals("Lotus")){
                                                        priceitem = Double.parseDouble(model.getItemLotusPrice());

                                                    }else if(locate.equals("BigC")){
                                                        priceitem = Double.parseDouble(model.getItemBigCPrice());

                                                    }else if(locate.equals("Foodland")){
                                                        priceitem = Double.parseDouble(model.getItemFoodLandPrice());

                                                    }else if(locate.equals("HomeFreshMart")){
                                                        priceitem = Double.parseDouble(model.getItemHomeFreshMartPrice());

                                                    }else if(locate.equals("MaxValue")){
                                                        priceitem = Double.parseDouble(model.getItemMaxValuePrice());

                                                    }else if(locate.equals("Makro")){
                                                        priceitem = Double.parseDouble(model.getItemMakroPrice());
                                                    }

                                                    addReport.child("BuyPrice").setValue(priceitem+"");


                                                    addReport.child("Classifier").setValue(i.getClassifier());
                                                    double TotalPrice = Double.parseDouble(i.getRetailPrice()) * volumnadd;
                                                    addReport.child("TotalPrice").setValue(String.valueOf(TotalPrice));



                                                    double TotalPriceLo = priceitem * volumnadd;
                                                    addReport.child("TotalBuyPrice").setValue(TotalPriceLo);
                                                    addReport.child("Image").setValue(i.getImage());
                                                    addReport.child("BuyAt").setValue(g);

                                                    User.child("Unit").setValue(Double.toString(volumnFin));

                                                    double totalVol = volumnFin * Double.parseDouble(i.getVolume());
                                                    User.child("TotalVolume").setValue(totalVol+"");
                                                    User.child("AlreadyAddtoShoplist").setValue("false");
                                                    User.child("VolumeForAdd").setValue("0");

                                                    DatabaseReference Price = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("all").child(Shopitem.getKeyAll());
                                                    Price.removeValue();
                                                    s.removeValue();

                                                    attachRecyclerViewAdapter();

                                                }else if (which == 1) {

                                                    AlertDialog.Builder builder3 = new AlertDialog.Builder(Tab18.this);
                                                    builder3.setTitle("How much price that you buy ?");
                                                    final EditText input = new EditText(Tab18.this);
                                                    input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                                    input.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                                                    builder3.setView(input);
                                                    builder3.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int which) {
                                                            m_Text = input.getText().toString();
                                                            DatabaseReference report = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("report").child(String.valueOf(mill));
                                                            DatabaseReference addReport = report;
                                                            addReport.child("Time").setValue(date2);
                                                            addReport.child("Month").setValue(month);
                                                            addReport.child("keyValue").setValue(key);
                                                            addReport.child("Name").setValue(i.getName());
                                                            addReport.child("Type").setValue(type);
                                                            addReport.child("Barcode").setValue(i.getBarcode());
                                                            addReport.child("Unit").setValue(Volum + "");
                                                            addReport.child("NormalPrice").setValue(i.getRetailPrice());
                                                            addReport.child("BuyPrice").setValue(m_Text);
                                                            addReport.child("Classifier").setValue(i.getClassifier());
                                                            double TotalPrice = Double.parseDouble(i.getRetailPrice()) * volumnadd;
                                                            addReport.child("TotalPrice").setValue(String.valueOf(TotalPrice));
                                                            double TotalPriceLo = Double.parseDouble(m_Text) * volumnadd;
                                                            addReport.child("TotalBuyPrice").setValue(TotalPriceLo);
                                                            addReport.child("Image").setValue(i.getImage());
                                                            addReport.child("BuyAt").setValue(g);


                                                            User.child("Unit").setValue(Double.toString(volumnFin));
                                                            double totalVol = volumnFin * Double.parseDouble(i.getVolume());
                                                            User.child("TotalVolume").setValue(totalVol + "");
                                                            User.child("AlreadyAddtoShoplist").setValue("false");
                                                            User.child("VolumeForAdd").setValue("0");

                                                            String shop = null;

                                                            if (locate.equals("Tops")) {
                                                                shop = "SalePriceTops";
                                                            } else if (locate.equals("Lotus")) {
                                                                shop = "SalePriceLotus";

                                                            } else if (locate.equals("BigC")) {
                                                                shop = "SalePriceBigC";

                                                            } else if (locate.equals("Foodland")) {
                                                                shop = "SalePriceFoodland";

                                                            } else if (locate.equals("HomeFreshMart")) {
                                                                shop = "SalePriceHomeFreshMart";

                                                            } else if (locate.equals("MaxValue")) {
                                                                shop = "SalePriceMaxValue";

                                                            } else if (locate.equals("Makro")) {
                                                                shop = "SalePriceMakro";
                                                            }


                                                            User.child(shop).setValue(m_Text);
                                                            DatabaseReference Price = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("all").child(Shopitem.getKeyAll());
                                                            Price.removeValue();
                                                            s.removeValue();
                                                            attachRecyclerViewAdapter();
                                                        }

                                                    });

                                                    builder3.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.cancel();
                                                        }
                                                    });

                                                    builder3.show();

                                                }
                                            }
                                        });
                                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        });
                                        builder.show();



                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });





                    }
                });

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        double retail = Double.parseDouble(model.getItemPrice());
                        double tops = Double.parseDouble(model.getItemTopsPrice());
                        double lotus = Double.parseDouble(model.getItemLotusPrice());
                        double bigC = Double.parseDouble(model.getItemBigCPrice());
                        double foodland = Double.parseDouble(model.getItemFoodLandPrice());
                        double homefreshmart = Double.parseDouble(model.getItemHomeFreshMartPrice());
                        double maxValue = Double.parseDouble(model.getItemMaxValuePrice());
                        double makro = Double.parseDouble(model.getItemMakroPrice());

                        List<ItemCheaper> cheaper = new ArrayList<ItemCheaper>();
                        cheaper.add(new ItemCheaper("Tops", tops));
                        cheaper.add(new ItemCheaper("Lotus", lotus));
                        cheaper.add(new ItemCheaper("BigC", bigC));
                        cheaper.add(new ItemCheaper("Foodland", foodland));
                        cheaper.add(new ItemCheaper("HomeFreshMart", homefreshmart));
                        cheaper.add(new ItemCheaper("MaxValue", maxValue));
                        cheaper.add(new ItemCheaper("Makro", makro));
                        Collections.sort(cheaper);

                        String textCheaper[] = new String [7];
                        int inddz = 0;

                        for (ItemCheaper ic : cheaper) {
                            textCheaper[inddz] = ic.getName() + " : " + ic.getPrice() + " THB";
                            inddz++;
                        }



                        AlertDialog.Builder builder5 = new AlertDialog.Builder(Tab18.this);
                        builder5.setTitle(model.getItemName());

                        String color = "#FF0000";

                        String text = textCheaper[0];

                        String input = "<font color=" + color + ">" + text + "</font>";

                        builder5.setMessage(Html.fromHtml( "RetailPrice : " + model.getItemPrice() + " THB" + "<br><br>" + input + "<br><br>" +
                                textCheaper[1] + "<br><br>" + textCheaper[2] + "<br><br>" + textCheaper[3] + "<br><br>" + textCheaper[4] + "<br><br>"
                                + textCheaper[5] + "<br><br>" + textCheaper[6]));

                        builder5.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder5.show();
                    }

                });


            }
        };

        mList.setAdapter(firebaseRecyclerAdapter);
    }

}