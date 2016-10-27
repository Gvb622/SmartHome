package com.smart.smarthome;

import android.*;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
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
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.squareup.picasso.Picasso;

import java.sql.SQLOutput;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Tab16 extends AppCompatActivity {

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
    String Volum;
    String key;
    String type;
    String Poskey;
    String g  ="";
    String locate ="";
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
        setContentView(R.layout.activity_tab6);

        mGoogleApiClient = new GoogleApiClient.Builder(Tab16.this)
                .addApi(Awareness.API)
                .addApi(AppIndex.API).build();
        mGoogleApiClient.connect();

        name = (TextView) findViewById(R.id.TextviewShop) ;

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

            if (ContextCompat.checkSelfPermission(
                    Tab16.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        Tab16.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        12345
                );
            } else {
                Awareness.SnapshotApi.getPlaces(mGoogleApiClient)
                        .setResultCallback(new ResultCallback<PlacesResult>() {
                            public static final String TAG = "Hi";

                            @Override
                            public void onResult(@NonNull PlacesResult placesResult) {
                                if (!placesResult.getStatus().isSuccess()) {
                                    Log.e(TAG, "Could not get places.");
                                    return;
                                }

                                List<PlaceLikelihood> placeLikelihoodList = placesResult.getPlaceLikelihoods();

                                if (placeLikelihoodList != null) {
                                    for (int i = 0; i < placeLikelihoodList.size(); i++) {
                                        PlaceLikelihood p = placeLikelihoodList.get(i);

                                        if (p.getPlace().getPlaceTypes().contains(43)) {
                                            g = p.getPlace().getName().toString();
                                            System.out.println("Location "+locate);
                                            System.out.println("G "+ g);
                                            if(g.contains("Tesco")){
                                                locate = "Lotus";
                                                AlertDialog.Builder builder = new AlertDialog.Builder(Tab16.this);
                                                builder.setTitle("Now you are near : ");
                                                builder.setMessage(locate);
                                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("location").child(locate);
                                                        qType = mDatabase.child("Food and Ingredients").orderByChild("Name");
                                                        attachRecyclerViewAdapter();
                                                        name.setText("Now you are at :  " + locate);

                                                    }
                                                });
                                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }
                                                });
                                                builder.show();

                                            }
                                        }
                                    }
                                } else {
                                    Log.e(TAG, "Place is null.");
                                }
                            }
                        });
            }


        System.out.println(mDatabase);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist");
        qType = mDatabase.child("Food and Ingredients").orderByChild("Name");
        pType = mDatabase.child("all").orderByKey();

        FoodType = mDatabase.child("Food and Ingredients").orderByKey();

        mList = (RecyclerView) findViewById(R.id.item_listShopping);

        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(this, R.drawable.list_divider), true));


        DecreaseItem = (ImageButton) findViewById(R.id.DecreaseItem2);
        DecreaseItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShoppinglistShowlistActivity.remove = false;
                RemoveItem.setImageResource(R.mipmap.ic_clear_black_24dp);
                ShoppinglistShowlistActivity.increase = false;
                IncreaseItem.setImageResource(R.mipmap.ic_arrow_upward_black_24dp);
                DecreaseItem.setImageResource(R.mipmap.ic_arrow_downward_black_24dp);


                pType.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Tab16.this);
                        builder.setTitle("The Shop that sale in Cheapest Price");
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                            Shoplistitem shopitem = postSnapshot.getValue(Shoplistitem.class);
                            double retail = Double.parseDouble(shopitem.getItemPrice());
                            double tops = Double.parseDouble(shopitem.getItemTopsPrice());
                            double lotus = Double.parseDouble(shopitem.getItemLotusPrice());

                            TotalRetailPrice += retail * Double.parseDouble(shopitem.getItemVolumn());
                            TotalTopsPrice += tops * Double.parseDouble(shopitem.getItemVolumn());
                            TotalLotusPrice += lotus * Double.parseDouble(shopitem.getItemVolumn());


                        }

                        String text;
                        String text3;
                        double DifferentPriceTops = TotalRetailPrice - TotalTopsPrice;
                        double DifferentPriceLotus = TotalRetailPrice - TotalLotusPrice;

                        DifferentPriceTops = Double.parseDouble(new DecimalFormat("##.##").format(DifferentPriceTops));
                        DifferentPriceLotus = Double.parseDouble(new DecimalFormat("##.##").format(DifferentPriceLotus));


                        if (TotalLotusPrice > TotalTopsPrice) {
                            text = "Tops Price is    " + TotalTopsPrice + " THB" + " : " + " You save  " + DifferentPriceTops;
                            text3 = "Lotus Price is " + TotalLotusPrice + " THB" + " : " + " You save  " + DifferentPriceLotus;
                        } else {
                            text = "Lotus Price is " + TotalLotusPrice + " THB" + " : " + " You save  " + DifferentPriceLotus;
                            text3 = "Tops Price is    " + TotalTopsPrice + " THB" + " : " + " You save  " + DifferentPriceTops;
                        }

                        String text2 = "Retail Price is     " + TotalRetailPrice;

                        String color = "#FF0000";
                        String input2 = "<font color=" + color + ">" + text + " THB" + "</font>";
                        String input3 = text2 + " THB";
                        String input4 = text3 + " THB";


                        builder.setMessage(Html.fromHtml(input3 + "<br><br>" + input2 + "<br><br>" + input4));
                        builder.show();
                        TotalRetailPrice = 0;
                        TotalTopsPrice = 0;
                        TotalLotusPrice = 0;
                        System.out.println(mDatabase);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

        AddItem2 = (ImageButton) findViewById(R.id.Additem2);

        AddItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShoppinglistShowlistActivity.remove = false;
                RemoveItem.setImageResource(R.mipmap.ic_clear_black_24dp);
                ShoppinglistShowlistActivity.increase = false;
                IncreaseItem.setImageResource(R.mipmap.ic_arrow_upward_black_24dp);
                DecreaseItem.setImageResource(R.mipmap.ic_arrow_downward_black_24dp);

                Intent intent5 = new Intent(Tab16.this, SelectItemShop.class);
                startActivity(intent5);
            }
        });

        RemoveItem = (ImageButton) findViewById(R.id.RemoveItem2);
        RemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean r = !ShoppinglistShowlistActivity.remove;
                ShoppinglistShowlistActivity.remove = r;
                if (r == true) {
                    RemoveItem.setImageResource(R.mipmap.ic_clear_white_24dp);
                    ShoppinglistShowlistActivity.increase = false;
                    IncreaseItem.setImageResource(R.mipmap.ic_arrow_upward_black_24dp);
                    DecreaseItem.setImageResource(R.mipmap.ic_arrow_downward_black_24dp);

                } else {
                    RemoveItem.setImageResource(R.mipmap.ic_clear_black_24dp);
                }
            }
        });

        IncreaseItem = (ImageButton) findViewById(R.id.IncreaseItem2);
        IncreaseItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b = !ShoppinglistShowlistActivity.increase;
                ShoppinglistShowlistActivity.increase = b;

                if (b == true) {

                    IncreaseItem.setImageResource(R.mipmap.ic_arrow_upward_white_24dp);
                    ShoppinglistShowlistActivity.remove = false;
                    DecreaseItem.setImageResource(R.mipmap.ic_arrow_downward_black_24dp);
                    RemoveItem.setImageResource(R.mipmap.ic_clear_black_24dp);

                } else {
                    IncreaseItem.setImageResource(R.mipmap.ic_arrow_upward_black_24dp);
                }

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
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

    private void initSnapshots() {

        if (ContextCompat.checkSelfPermission(
                Tab16.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    Tab16.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    12345
            );
        } else {

            Awareness.SnapshotApi.getLocation(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<LocationResult>() {

                        public String TAG = "Hiio";

                        @Override
                        public void onResult(@NonNull LocationResult locationResult) {
                            if (!locationResult.getStatus().isSuccess()) {
                                Log.e(TAG, "Could not get location.");
                                return;
                            }
                            Location location = locationResult.getLocation();
                            Log.i(TAG, "Lat: " + location.getLatitude() + ", Lon: " + location.getLongitude());
                        }
                    });


            Awareness.SnapshotApi.getPlaces(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<PlacesResult>() {
                        public static final String TAG = "Hi";

                        @Override
                        public void onResult(@NonNull PlacesResult placesResult) {
                            if (!placesResult.getStatus().isSuccess()) {
                                Log.e(TAG, "Could not get places.");
                                return;
                            }
                            List<PlaceLikelihood> placeLikelihoodList = placesResult.getPlaceLikelihoods();
                            String g  = "";

                            // Show the top 5 possible location results.
                            if (placeLikelihoodList != null) {
                                for (int i = 0; i < placeLikelihoodList.size(); i++) {
                                    PlaceLikelihood p = placeLikelihoodList.get(i);
                                   // Log.i(TAG, p.getPlace().getName().toString() + ", likelihood: " + p.getLikelihood());
                                   // Log.i(TAG, p.getPlace().getPlaceTypes().toString());

                                    if (p.getPlace().getPlaceTypes().contains(43)) {
                                        g = p.getPlace().getName().toString();
                                    }

                                }


                                /*
                                if(!g.equals("")){
                                    Toast.makeText(Tab16.this, g + "near you", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(Tab16.this, "No supermarket nearby", Toast.LENGTH_SHORT).show();
                                }
                            g = "";
                            */

                            } else {
                                Log.e(TAG, "Place is null.");
                            }
                        }
                    });
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){

                Place place = PlacePicker.getPlace(data, this);
                // if(place.getPlaceTypes() == )
                String name = String.format("Place : %s", place.getPlaceTypes());
            }
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
            protected void populateViewHolder(ItemViewHolder viewHolder, Shoplistitem model, int position) {

                viewHolder.setName2(model.getItemName());
                viewHolder.setPrice2(model.getItemPrice() + " /" + model.getItemClassifier());

                double Shop = 0;
                double Retail = 0;
                try {
                    Shop   = Double.parseDouble(model.getItemShopsPrice());
                    Retail = Double.parseDouble(model.getItemPrice());
                } catch (Exception e) {

                }

                viewHolder.setPrice3(locate+  " : " + model.getItemShopsPrice());
                viewHolder.setVolumn2(model.getItemVolumn() + " " + model.getItemClassifier());
                viewHolder.setImage2(getApplicationContext(), model.getItemImage());


            }
        };

        mList.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), mList, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position) {

                        final DatabaseReference s = firebaseRecyclerAdapter.getRef(position);

                        if (ShoppinglistShowlistActivity.remove == true) {

                            initSnapshots();


                        } else if (ShoppinglistShowlistActivity.increase == true) {


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
                                            volumn = Double.parseDouble(new DecimalFormat("##.##").format(volumn));

                                            volumnadd = Double.parseDouble(Volum);
                                            volumnFin = volumn + volumnadd;

                                            volumnFin = Double.parseDouble(new DecimalFormat("##.##").format(volumnFin));

                                            String price = null;
                                            String price2 = null;


                                            //if (Tops > Lotus){
                                            price = "Lotus : " + i.getSalePriceLotus();
                                            //}else if (Tops < Lotus){
                                            price2 = "Tops : " + i.getSalePriceTops();
                                            // }

                                            CharSequence priceItem[] = new CharSequence[]{"Retailprice : " + i.getRetailPrice(), price, price2, "Update All"};
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Tab16.this);
                                            builder.setTitle("Choose Shop and Price that You buy");

                                            builder.setItems(priceItem, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    Calendar c = Calendar.getInstance();
                                                    long mill = c.getTimeInMillis();
                                                    SimpleDateFormat sfd = new SimpleDateFormat("yyyyMMdd");
                                                    SimpleDateFormat sfd2 = new SimpleDateFormat("MM");
                                                    String date2 = sfd.format(mill);
                                                    String month = sfd2.format(mill);

                                                    if (which == 0) {

                                                        DatabaseReference report = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("report").child(String.valueOf(mill));
                                                        DatabaseReference addReport = report;
                                                        addReport.child("Time").setValue(date2);
                                                        addReport.child("Month").setValue(month);
                                                        addReport.child("keyValue").setValue(key);
                                                        addReport.child("Name").setValue(i.getName());
                                                        addReport.child("Type").setValue(type);
                                                        addReport.child("Barcode").setValue(i.getBarcode());
                                                        addReport.child("Unit").setValue(Volum);
                                                        addReport.child("NormalPrice").setValue(i.getRetailPrice());
                                                        addReport.child("BuyPrice").setValue(i.getRetailPrice());
                                                        addReport.child("Classifier").setValue(i.getClassifier());
                                                        double TotalPrice = Double.parseDouble(i.getRetailPrice()) * volumnadd;
                                                        addReport.child("TotalPrice").setValue(String.valueOf(TotalPrice));
                                                        addReport.child("TotalBuyPrice").setValue(TotalPrice);
                                                        addReport.child("Image").setValue(i.getImage());
                                                        Toast.makeText(Tab16.this, i.getName() + " Already add to inventory ", Toast.LENGTH_LONG).show();
                                                        User.child("Unit").setValue(Double.toString(volumnFin));

                                                        double totalVol = volumnFin * Double.parseDouble(i.getVolume());
                                                        User.child("TotalVolume").setValue(totalVol + "");
                                                        s.removeValue();
                                                        DatabaseReference Price = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("all").child(Shopitem.getKeyAll());
                                                        Price.removeValue();


                                                    } else if (which == 1) {

                                                        DatabaseReference report = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("report").child(String.valueOf(mill));
                                                        DatabaseReference addReport = report;
                                                        addReport.child("Time").setValue(date2);
                                                        addReport.child("Month").setValue(month);
                                                        addReport.child("keyValue").setValue(key);
                                                        addReport.child("Name").setValue(i.getName());
                                                        addReport.child("Type").setValue(type);
                                                        addReport.child("Barcode").setValue(i.getBarcode());
                                                        addReport.child("Unit").setValue(Volum);
                                                        addReport.child("NormalPrice").setValue(i.getRetailPrice());
                                                        addReport.child("BuyPrice").setValue(i.getSalePriceLotus());
                                                        addReport.child("Classifier").setValue(i.getClassifier());
                                                        double TotalPrice = Double.parseDouble(i.getRetailPrice()) * volumnadd;
                                                        addReport.child("TotalPrice").setValue(String.valueOf(TotalPrice));
                                                        double TotalBuyPrice = Double.parseDouble(i.getSalePriceLotus()) * volumnadd;
                                                        addReport.child("TotalBuyPrice").setValue(TotalBuyPrice);
                                                        addReport.child("Image").setValue(i.getImage());
                                                        Toast.makeText(Tab16.this, i.getName() + " Already add to inventory ", Toast.LENGTH_LONG).show();
                                                        User.child("Unit").setValue(Double.toString(volumnFin));

                                                        double totalVol = volumnFin * Double.parseDouble(i.getVolume());

                                                        User.child("TotalVolume").setValue(totalVol + "");
                                                        s.removeValue();
                                                        DatabaseReference Price = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("all").child(Shopitem.getKeyAll());
                                                        Price.removeValue();


                                                    } else if (which == 2) {

                                                        DatabaseReference report = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("report").child(String.valueOf(mill));
                                                        DatabaseReference addReport = report;
                                                        addReport.child("Time").setValue(date2);
                                                        addReport.child("Month").setValue(month);
                                                        addReport.child("keyValue").setValue(key);
                                                        addReport.child("Name").setValue(i.getName());
                                                        addReport.child("Type").setValue(type);
                                                        addReport.child("Barcode").setValue(i.getBarcode());
                                                        addReport.child("Unit").setValue(Volum);
                                                        addReport.child("NormalPrice").setValue(i.getRetailPrice());
                                                        addReport.child("BuyPrice").setValue(i.getSalePriceTops());
                                                        addReport.child("Classifier").setValue(i.getClassifier());
                                                        double TotalPrice = Double.parseDouble(i.getRetailPrice()) * volumnadd;
                                                        addReport.child("TotalPrice").setValue(String.valueOf(TotalPrice));
                                                        double TotalBuyPrice = Double.parseDouble(i.getSalePriceTops()) * volumnadd;
                                                        addReport.child("TotalBuyPrice").setValue(TotalBuyPrice);
                                                        addReport.child("Image").setValue(i.getImage());
                                                        Toast.makeText(Tab16.this, i.getName() + " Already add to inventory ", Toast.LENGTH_LONG).show();
                                                        User.child("Unit").setValue(Double.toString(volumnFin));

                                                        double totalVol = volumnFin * Double.parseDouble(i.getVolume());

                                                        User.child("TotalVolume").setValue(totalVol + "");
                                                        s.removeValue();
                                                        DatabaseReference Price = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("all").child(Shopitem.getKeyAll());
                                                        Price.removeValue();


                                                    } else if (which == 3) {

                                                    }
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




                        } else {
                            type = s.getParent().getKey();


                            s.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Shoplistitem Shopitem = dataSnapshot.getValue(Shoplistitem.class);
                                    final AlertDialog.Builder builder2 = new AlertDialog.Builder(Tab16.this);
                                    builder2.setTitle(Shopitem.getItemName());
                                    String alert1 = "RetailPrice : " + Shopitem.getItemPrice() + " THB";
                                    String alert2 = "Tops  :  " + Shopitem.getItemTopsPrice() + " THB";
                                    String alert3 = "Lotus :  " + Shopitem.getItemLotusPrice() + " THB";


                                    double Tops = Double.parseDouble(Shopitem.getItemTopsPrice());
                                    double Lotus = Double.parseDouble(Shopitem.getItemLotusPrice());
                                    double Retail = Double.parseDouble(Shopitem.getItemPrice());

                                    String color = "#FF0000";

                                    if (Retail < Lotus && Retail < Tops) {
                                        builder2.setMessage(alert1 + "\n\n" + alert2 + "\n\n" + alert3);

                                    } else if (Tops > Lotus) {
                                        String text = Shopitem.getItemLotusPrice();
                                        String input = "<font color=" + color + ">" + text + " THB" + "</font>";

                                        builder2.setMessage(Html.fromHtml(alert1 + "<br><br>" + alert2 + "<br><br>" + "<font color='#FF0000'><b> Lotus  : </b></font>" + input));
                                    } else if (Lotus > Tops) {
                                        String text = Shopitem.getItemTopsPrice();
                                        String input = "<font color=" + color + ">" + text + " THB" + "</font>";
                                        builder2.setMessage(Html.fromHtml(alert1 + "<br><br>" + "<font color='#FF0000'><b> Tops </b></font>" + " : " + input + "<br><br>" + alert3)
                                        );
                                    } else {
                                        builder2.setMessage(alert1 + "\n\n" + alert2 + "\n\n" + alert3);

                                    }
                                    builder2.show();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }

                        return true;
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

        mList.setAdapter(firebaseRecyclerAdapter);
    }

}