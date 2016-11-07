package com.smart.smarthome;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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

public class Tab9 extends AppCompatActivity {

    private String value;
    private RecyclerView mList;
    private Query qType;
    private Query pType;

    private Query FoodType;


    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private ImageButton AddItem2;

    static public ImageButton IncreaseItem;
    static public ImageButton DecreaseItem;
    static public ImageButton RemoveItem;
    FirebaseUser user;
    DatabaseReference User;
    DatabaseReference User2;
    DatabaseReference s;


    String m_Text;
    String p_Text;
    Double Volum;
    String key;
    String type;
    String Poskey;

    item i;

    double TotalRetailPrice = 0;
    double TotalTopsPrice = 0;
    double TotalLotusPrice = 0;
    double TotalBigCPrice = 0;
    double TotalFoodlandPrice = 0;
    double TotalHomeFreshMartPrice = 0;
    double TotalMaxValuePrice = 0;
    double TotalMakroPrice = 0;
    double volumnadd;
    double volumnFin;

    int count = 0;
    boolean nFinish = false;

    int numI = 0;
    String[] PostKey;


    Shoplistitem shopitem2;
    FirebaseRecyclerAdapter<Shoplistitem, ItemViewHolder> firebaseRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab6);


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist");
        qType = mDatabase.child("Household Product").orderByChild("Name");
        pType = mDatabase.child("all").orderByKey();

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
                pType.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Tab9.this);
                        builder.setTitle("The Shop that sale in Cheapest Price");
                        TotalRetailPrice = 0;
                        TotalTopsPrice = 0;
                        TotalLotusPrice = 0;
                        TotalBigCPrice = 0;
                        TotalFoodlandPrice = 0;
                        TotalHomeFreshMartPrice = 0;
                        TotalMaxValuePrice = 0;
                        TotalMakroPrice = 0;
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                            Shoplistitem shopitem = postSnapshot.getValue(Shoplistitem.class);
                            double retail = Double.parseDouble(shopitem.getItemPrice());
                            double tops = Double.parseDouble(shopitem.getItemTopsPrice());
                            double lotus = Double.parseDouble(shopitem.getItemLotusPrice());
                            double bigC = Double.parseDouble(shopitem.getItemBigCPrice());
                            double foodland = Double.parseDouble(shopitem.getItemFoodLandPrice());
                            double homefreshmart = Double.parseDouble(shopitem.getItemHomeFreshMartPrice());
                            double maxValue = Double.parseDouble(shopitem.getItemMaxValuePrice());
                            double makro = Double.parseDouble(shopitem.getItemMakroPrice());

                            TotalRetailPrice += retail * shopitem.getItemVolumn();
                            TotalTopsPrice += tops * shopitem.getItemVolumn();
                            TotalLotusPrice += lotus * shopitem.getItemVolumn();
                            TotalBigCPrice += bigC * shopitem.getItemVolumn();
                            TotalFoodlandPrice += foodland * shopitem.getItemVolumn();
                            TotalHomeFreshMartPrice += homefreshmart * shopitem.getItemVolumn();
                            TotalMaxValuePrice += maxValue * shopitem.getItemVolumn();
                            TotalMakroPrice += makro * shopitem.getItemVolumn();


                        }

                        String text;
                        String text3 = null;
                        String textCheap[] = new String[7];
                        String textCheap2[] = new String[7];


                        double DifferentPriceTops = TotalRetailPrice - TotalTopsPrice;
                        double DifferentPriceLotus = TotalRetailPrice - TotalLotusPrice;
                        double DifferentPriceBigC = TotalRetailPrice - TotalBigCPrice;
                        double DifferentPriceFoodland = TotalRetailPrice - TotalFoodlandPrice;
                        double DifferentPriceHomeFreshMart = TotalRetailPrice - TotalHomeFreshMartPrice;
                        double DifferentPriceMaxValue = TotalRetailPrice - TotalMaxValuePrice;
                        double DifferentPriceMakro = TotalRetailPrice - TotalMakroPrice;


                        DifferentPriceTops = Double.parseDouble(new DecimalFormat("##.##").format(DifferentPriceTops));
                        DifferentPriceLotus = Double.parseDouble(new DecimalFormat("##.##").format(DifferentPriceLotus));
                        DifferentPriceBigC = Double.parseDouble(new DecimalFormat("##.##").format(DifferentPriceBigC));
                        DifferentPriceFoodland = Double.parseDouble(new DecimalFormat("##.##").format(DifferentPriceFoodland));
                        DifferentPriceHomeFreshMart = Double.parseDouble(new DecimalFormat("##.##").format(DifferentPriceHomeFreshMart));
                        DifferentPriceMaxValue = Double.parseDouble(new DecimalFormat("##.##").format(DifferentPriceMaxValue));
                        DifferentPriceMakro = Double.parseDouble(new DecimalFormat("##.##").format(DifferentPriceMakro));


                        List<ItemCheapest> cheapests = new ArrayList<ItemCheapest>();
                        cheapests.add(new ItemCheapest("Tops", TotalTopsPrice, DifferentPriceTops));
                        cheapests.add(new ItemCheapest("Lotus", TotalLotusPrice, DifferentPriceLotus));
                        cheapests.add(new ItemCheapest("BigC", TotalBigCPrice, DifferentPriceBigC));
                        cheapests.add(new ItemCheapest("Foodland", TotalFoodlandPrice, DifferentPriceFoodland));
                        cheapests.add(new ItemCheapest("HomeFreshMart", TotalHomeFreshMartPrice, DifferentPriceHomeFreshMart));
                        cheapests.add(new ItemCheapest("MaxValue", TotalMaxValuePrice, DifferentPriceMaxValue));
                        cheapests.add(new ItemCheapest("Makro", TotalMakroPrice, DifferentPriceMakro));
                        Collections.sort(cheapests);

                        int inddd = 0;
                        for (ItemCheapest ic : cheapests) {

                            textCheap[inddd] = ic.getName() + " Price is " + ic.getPrice() + " THB";
                            textCheap2[inddd] = "You save " + ic.getDifferent() + " THB";
                            inddd++;
                        }


                        String text2 = "Retail Price is     " + TotalRetailPrice + " THB";

                        String color = "#FF0000";

                        String input2 = "<font color=" + color + ">" + textCheap[0] + "</font>";
                        String input3 = "<font color=" + color + ">" + textCheap2[0] + "</font>";
                        ;
                        String input4 = text2;


                        builder.setMessage(Html.fromHtml(input4 + "<br><br>" + input2 + "<br>" + input3 + "<br><br>" + textCheap[1] + "<br>" + textCheap2[1] + "<br><br>"
                                + textCheap[2] + "<br>" + textCheap2[2] + "<br><br>" + textCheap[3] + "<br>" + textCheap2[3] + "<br><br>"
                                + textCheap[4] + "<br>" + textCheap2[4] + "<br><br>" + textCheap[5] + "<br>" + textCheap2[5] + "<br><br>"
                                + textCheap[6] + "<br>" + textCheap2[6]));

                        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();
                        TotalRetailPrice = 0;
                        TotalTopsPrice = 0;
                        TotalLotusPrice = 0;
                        TotalBigCPrice = 0;
                        TotalFoodlandPrice = 0;
                        TotalHomeFreshMartPrice = 0;
                        TotalMaxValuePrice = 0;
                        TotalMakroPrice = 0;
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
                Intent intent5 = new Intent(Tab9.this, MainActivity.class);
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
                } else {
                    RemoveItem.setImageResource(R.mipmap.ic_clear_black_24dp);
                }
            }
        });

        IncreaseItem = (ImageButton) findViewById(R.id.IncreaseItem2);
        IncreaseItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShoppinglistShowlistActivity.remove = false;

                RemoveItem.setImageResource(R.mipmap.ic_clear_black_24dp);

                pType.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                            Shoplistitem shopitem = postSnapshot.getValue(Shoplistitem.class);
                            double retail = Double.parseDouble(shopitem.getItemPrice());
                            double tops = Double.parseDouble(shopitem.getItemTopsPrice());
                            double lotus = Double.parseDouble(shopitem.getItemLotusPrice());
                            double bigC = Double.parseDouble(shopitem.getItemBigCPrice());
                            double foodland = Double.parseDouble(shopitem.getItemFoodLandPrice());
                            double homefreshmart = Double.parseDouble(shopitem.getItemHomeFreshMartPrice());
                            double maxValue = Double.parseDouble(shopitem.getItemMaxValuePrice());
                            double makro = Double.parseDouble(shopitem.getItemMakroPrice());

                            TotalRetailPrice += retail * shopitem.getItemVolumn();
                            TotalTopsPrice += tops * shopitem.getItemVolumn();
                            TotalLotusPrice += lotus * shopitem.getItemVolumn();
                            TotalBigCPrice += bigC * shopitem.getItemVolumn();
                            TotalFoodlandPrice += foodland * shopitem.getItemVolumn();
                            TotalHomeFreshMartPrice += homefreshmart * shopitem.getItemVolumn();
                            TotalMaxValuePrice += maxValue * shopitem.getItemVolumn();
                            TotalMakroPrice += makro * shopitem.getItemVolumn();
                        }

                        CharSequence priceItem[] = new CharSequence[] {"Tops" , "Lotus" , "BigC" , "Foodland", "Home Fresh Mart" , "MaxValue", "Makro"};
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(Tab9.this);
                        builder2.setTitle("Choose Shop that You buy");
                        builder2.setItems(priceItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                if(which == 0){
                                    AlertDialog.Builder builder3 = new AlertDialog.Builder(Tab9.this);
                                    builder3.setTitle("You buy item from Tops");
                                    builder3.setMessage("Price is " + TotalTopsPrice);
                                    builder3.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            updateAll("Food and Ingredients","Tops");
                                            updateAll("Beverage and Drink Powder","Tops");
                                            updateAll("Health and Beauty","Tops");
                                            updateAll("Household Product","Tops");
                                            updateAll("Etc","Tops");
                                        }
                                    });
                                    builder3.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                                    builder3.show();

                                }else if(which == 1){
                                    AlertDialog.Builder builder3 = new AlertDialog.Builder(Tab9.this);
                                    builder3.setTitle("You buy item from Lotus");
                                    builder3.setMessage("Price is " + TotalLotusPrice);
                                    builder3.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            updateAll("Food and Ingredients","Lotus");
                                            updateAll("Beverage and Drink Powder","Lotus");
                                            updateAll("Health and Beauty","Lotus");
                                            updateAll("Household Product","Lotus");
                                            updateAll("Etc","Lotus");
                                        }
                                    });
                                    builder3.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                                    builder3.show();

                                }else if(which == 2){
                                    AlertDialog.Builder builder3 = new AlertDialog.Builder(Tab9.this);
                                    builder3.setTitle("You buy item from BigC");
                                    builder3.setMessage("Price is " + TotalBigCPrice);
                                    builder3.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            updateAll("Food and Ingredients","BigC");
                                            updateAll("Beverage and Drink Powder","BigC");
                                            updateAll("Health and Beauty","BigC");
                                            updateAll("Household Product","BigC");
                                            updateAll("Etc","Lotus");
                                        }
                                    });
                                    builder3.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                                    builder3.show();

                                }else if(which == 3){
                                    AlertDialog.Builder builder3 = new AlertDialog.Builder(Tab9.this);
                                    builder3.setTitle("You buy item from Foodland");
                                    builder3.setMessage("Price is " + TotalFoodlandPrice);
                                    builder3.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            updateAll("Food and Ingredients","Foodland");
                                            updateAll("Beverage and Drink Powder","Foodland");
                                            updateAll("Health and Beauty","Foodland");
                                            updateAll("Household Product","Foodland");
                                            updateAll("Etc","Foodland");
                                        }
                                    });
                                    builder3.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                                    builder3.show();

                                }else if(which == 4){
                                    AlertDialog.Builder builder3 = new AlertDialog.Builder(Tab9.this);
                                    builder3.setTitle("You buy item from Home Fresh Mart");
                                    builder3.setMessage("Price is " + TotalHomeFreshMartPrice);
                                    builder3.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            updateAll("Food and Ingredients","HomeFreshMart");
                                            updateAll("Beverage and Drink Powder","HomeFreshMart");
                                            updateAll("Health and Beauty","HomeFreshMart");
                                            updateAll("Household Product","HomeFreshMart");
                                            updateAll("Etc","HomeFreshMart");
                                        }
                                    });
                                    builder3.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                                    builder3.show();

                                }else if(which == 5){
                                    AlertDialog.Builder builder3 = new AlertDialog.Builder(Tab9.this);
                                    builder3.setTitle("You buy item from MaxValue");
                                    builder3.setMessage("Price is " + TotalMaxValuePrice);
                                    builder3.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            updateAll("Food and Ingredients","MaxValue");
                                            updateAll("Beverage and Drink Powder","MaxValue");
                                            updateAll("Health and Beauty","MaxValue");
                                            updateAll("Household Product","MaxValue");
                                            updateAll("Etc","MaxValue");
                                        }
                                    });
                                    builder3.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                                    builder3.show();

                                }else if(which == 6){
                                    AlertDialog.Builder builder3 = new AlertDialog.Builder(Tab9.this);
                                    builder3.setTitle("You buy item from Makro");
                                    builder3.setMessage("Price is " + TotalMakroPrice);
                                    builder3.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            updateAll("Food and Ingredients","Makro");
                                            updateAll("Beverage and Drink Powder","Makro");
                                            updateAll("Health and Beauty","Makro");
                                            updateAll("Household Product","Makro");
                                            updateAll("Etc","Makro");
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
                        builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder2.show();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                TotalRetailPrice = 0;
                TotalTopsPrice = 0;
                TotalLotusPrice = 0;
                TotalBigCPrice = 0;
                TotalFoodlandPrice = 0;
                TotalHomeFreshMartPrice = 0;
                TotalMaxValuePrice = 0;
                TotalMakroPrice = 0;

            }
        });


    }

    public void updateAll(final String TypeItem, final String shop) {


        Query FoodTypeForUpdate = mDatabase.child(TypeItem).orderByKey();
        FoodTypeForUpdate.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Poskey = postSnapshot.getKey();
                    shopitem2 = postSnapshot.getValue(Shoplistitem.class);
                    key = shopitem2.getKey();
                    Volum = shopitem2.getItemVolumn();
                    System.out.println(Poskey);
                    System.out.println(key);

                    DatabaseReference Price = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("all").child(shopitem2.getKeyAll());
                    Price.removeValue();

                    final DatabaseReference UserItem = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("items")
                            .child(TypeItem).child(key);
                    UserItem.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            item iteminfo = dataSnapshot.getValue(item.class);
                            double volumn = Double.parseDouble(iteminfo.getUnit());
                            volumn = Double.parseDouble(new DecimalFormat("##.##").format(volumn));

                            volumnadd = Double.parseDouble(iteminfo.getVolumeForAdd());
                            volumnFin = volumn + volumnadd;
                            volumnFin = Double.parseDouble(new DecimalFormat("##.##").format(volumnFin));
                            Calendar c = Calendar.getInstance();
                            long mill = c.getTimeInMillis();
                            SimpleDateFormat sfd = new SimpleDateFormat("yyyyMMdd");
                            SimpleDateFormat sfd2 = new SimpleDateFormat("MM");
                            String date2 = sfd.format(mill);
                            String month = sfd2.format(mill);

                            DatabaseReference report = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("report").child(String.valueOf(mill));
                            DatabaseReference addReport = report;
                            addReport.child("Time").setValue(date2);
                            addReport.child("Month").setValue(month);
                            addReport.child("keyValue").setValue(key);
                            addReport.child("Name").setValue(iteminfo.getName());
                            addReport.child("Type").setValue(TypeItem);
                            addReport.child("Barcode").setValue(iteminfo.getBarcode());
                            addReport.child("Unit").setValue(iteminfo.getVolumeForAdd() + "");
                            addReport.child("NormalPrice").setValue(iteminfo.getRetailPrice());

                            String Buyprice = "";

                            if (shop.equals("Tops")) {
                                Buyprice = iteminfo.getSalePriceTops();
                            } else if (shop.equals("Lotus")) {
                                Buyprice = iteminfo.getSalePriceLotus();
                            } else if (shop.equals("BigC")) {
                                Buyprice = iteminfo.getSalePriceBigC();
                            } else if (shop.equals("Foodland")) {
                                Buyprice = iteminfo.getSalePriceFoodland();
                            } else if (shop.equals("HomeFreshMart")) {
                                Buyprice = iteminfo.getSalePriceHomeFreshMart();
                            } else if (shop.equals("MaxValue")) {
                                Buyprice = iteminfo.getSalePriceMaxValue();
                            } else if (shop.equals("Makro")) {
                                Buyprice = iteminfo.getSalePriceMakro();
                            }

                            addReport.child("BuyPrice").setValue(Buyprice);


                            addReport.child("Classifier").setValue(iteminfo.getClassifier());

                            double TotalPrice = Double.parseDouble(iteminfo.getRetailPrice()) * volumnadd;

                            addReport.child("TotalPrice").setValue(String.valueOf(TotalPrice));

                            double TotalBuyPrice = Double.parseDouble(Buyprice) * volumnadd;


                            addReport.child("TotalBuyPrice").setValue(TotalBuyPrice);
                            addReport.child("BuyAt").setValue(shop);

                            addReport.child("Image").setValue(iteminfo.getImage());
                            UserItem.child("Unit").setValue(Double.toString(volumnFin));
                            double TotalV = volumnFin * Double.parseDouble(iteminfo.getVolume());
                            UserItem.child("TotalVolume").setValue(TotalV + "");
                            UserItem.child("VolumeForAdd").setValue("0");
                            UserItem.child("AlreadyAddtoShoplist").setValue("false");
                            DatabaseReference This = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child(TypeItem);
                            This.removeValue();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        attachRecyclerViewAdapter();
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

                try {

                    String Cheaper = model.getShopCheapest();
                    if (Cheaper.contains("TopsLotusBigCFoodlandHomeFreshMartMaxValueMakro")) {
                        //viewHolder.setPrice3("Tops : " + model.getItemTopsPrice());
                    } else if (Cheaper.contains("Tops")) {
                        viewHolder.setPrice3("Tops : " + model.getItemTopsPrice());
                    } else if (Cheaper.contains("Lotus")) {
                        viewHolder.setPrice3("Lotus : " + model.getItemLotusPrice());
                    } else if (Cheaper.contains("BigC")) {
                        viewHolder.setPrice3("BigC : " + model.getItemBigCPrice());
                    } else if (Cheaper.contains("Foodland")) {
                        viewHolder.setPrice3("FoodLand : " + model.getItemFoodLandPrice());
                    } else if (Cheaper.contains("HomeFreshMart")) {
                        viewHolder.setPrice3("HomeFresh: " + model.getItemHomeFreshMartPrice());
                    } else if (Cheaper.contains("MaxValue")) {
                        viewHolder.setPrice3("MaxValue : " + model.getItemMaxValuePrice());
                    } else if (Cheaper.contains("Makro")) {
                        viewHolder.setPrice3("Makro : " + model.getItemMakroPrice());
                    }


                } catch (Exception e) {

                }

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        s = firebaseRecyclerAdapter.getRef(position);
                        if (ShoppinglistShowlistActivity.remove == true) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Tab9.this);
                            builder.setTitle("Remove item");
                            builder.setMessage("Are you sure to remove this item ?");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    s.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {


                                            DatabaseReference shopall = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist")
                                                    .child("all").child(model.getKeyAll());
                                            DatabaseReference UserItem = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("items")
                                                    .child(s.getParent().getKey()).child(model.getKey());
                                            UserItem.child("AlreadyAddtoShoplist").setValue("false");
                                            UserItem.child("VolumeForAdd").setValue("0");

                                            shopall.removeValue();
                                            s.removeValue();
                                            attachRecyclerViewAdapter();
                                            ShoppinglistShowlistActivity.remove = false;
                                            RemoveItem.setImageResource(R.mipmap.ic_clear_black_24dp);

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                        } else {


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

                            String textCheaper[] = new String[7];
                            int inddz = 0;

                            for (ItemCheaper ic : cheaper) {
                                textCheaper[inddz] = ic.getName() + " : " + ic.getPrice() + " THB";
                                inddz++;
                            }


                            AlertDialog.Builder builder5 = new AlertDialog.Builder(Tab9.this);
                            builder5.setTitle(model.getItemName());

                            String color = "#FF0000";

                            String text = textCheaper[0];

                            String input = "<font color=" + color + ">" + text + "</font>";

                            builder5.setMessage(Html.fromHtml("RetailPrice : " + model.getItemPrice() + " THB" + "<br><br>" + input + "<br><br>" +
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
                    }
                });

                viewHolder.setVolumn2(model.getItemVolumn() + " " + model.getItemClassifier());
                viewHolder.setImage2(getApplicationContext(), model.getItemImage());

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
                                    public void onDataChange(final DataSnapshot dataSnapshot) {

                                        i = dataSnapshot.getValue(item.class);
                                        double volumn = Double.parseDouble(i.getUnit());
                                        volumn = Double.parseDouble(new DecimalFormat("##.##").format(volumn));

                                        volumnadd = Volum;
                                        volumnFin = volumn + volumnadd;

                                        volumnFin = Double.parseDouble(new DecimalFormat("##.##").format(volumnFin));

                                        String price = null;
                                        String price2 = null;
                                        String price3 = null;
                                        String price4 = null;
                                        String price5 = null;
                                        String price6 = null;
                                        String price7 = null;
                                        String priceRe = null;

                                        priceRe = "Retailprice : " + i.getRetailPrice();
                                        price = "Lotus : " + i.getSalePriceLotus();
                                        price2 = "Tops : " + i.getSalePriceTops();
                                        price3 = "BigC : " + i.getSalePriceBigC();
                                        price4 = "Foodland : " + i.getSalePriceFoodland();
                                        price5 = "Home Fresh Mart : " + i.getSalePriceHomeFreshMart();
                                        price6 = "MaxValue : " + i.getSalePriceMaxValue();
                                        price7 = "Makro : " + i.getSalePriceMakro();


                                        CharSequence priceItem[] = new CharSequence[]{priceRe, price, price2, price3, price4, price5, price6, price7, "Buy other price"};
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(Tab9.this);
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


                                                if (which == 0) {

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
                                                    addReport.child("BuyPrice").setValue(i.getRetailPrice());
                                                    addReport.child("Classifier").setValue(i.getClassifier());
                                                    double TotalPrice = Double.parseDouble(i.getRetailPrice()) * volumnadd;
                                                    addReport.child("TotalPrice").setValue(String.valueOf(TotalPrice));
                                                    addReport.child("TotalBuyPrice").setValue(TotalPrice);
                                                    addReport.child("Image").setValue(i.getImage());
                                                    addReport.child("BuyAt").setValue("RetailPrice");


                                                    Toast.makeText(Tab9.this, i.getName() + " Already add to inventory ", Toast.LENGTH_LONG).show();
                                                    User.child("Unit").setValue(Double.toString(volumnFin));

                                                    double totalVol = volumnFin * Double.parseDouble(i.getVolume());
                                                    User.child("TotalVolume").setValue(totalVol + "");
                                                    User.child("AlreadyAddtoShoplist").setValue("false");
                                                    User.child("VolumeForAdd").setValue("0");

                                                    DatabaseReference Price = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("all").child(Shopitem.getKeyAll());
                                                    Price.removeValue();
                                                    s.removeValue();

                                                    attachRecyclerViewAdapter();

                                                } else if (which == 1) {

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
                                                    addReport.child("BuyPrice").setValue(i.getSalePriceLotus());
                                                    addReport.child("Classifier").setValue(i.getClassifier());
                                                    double TotalPrice = Double.parseDouble(i.getRetailPrice()) * volumnadd;
                                                    addReport.child("TotalPrice").setValue(String.valueOf(TotalPrice));
                                                    double TotalPriceLo = Double.parseDouble(i.getSalePriceLotus()) * volumnadd;
                                                    addReport.child("TotalBuyPrice").setValue(TotalPriceLo);
                                                    addReport.child("Image").setValue(i.getImage());
                                                    addReport.child("BuyAt").setValue("Lotus");


                                                    Toast.makeText(Tab9.this, i.getName() + " Already add to inventory ", Toast.LENGTH_LONG).show();
                                                    User.child("Unit").setValue(Double.toString(volumnFin));

                                                    double totalVol = volumnFin * Double.parseDouble(i.getVolume());
                                                    User.child("TotalVolume").setValue(totalVol + "");
                                                    User.child("AlreadyAddtoShoplist").setValue("false");
                                                    User.child("VolumeForAdd").setValue("0");

                                                    DatabaseReference Price = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("all").child(Shopitem.getKeyAll());
                                                    Price.removeValue();
                                                    s.removeValue();

                                                    attachRecyclerViewAdapter();

                                                } else if (which == 2) {

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
                                                    addReport.child("BuyPrice").setValue(i.getSalePriceTops());
                                                    addReport.child("Classifier").setValue(i.getClassifier());
                                                    double TotalPrice = Double.parseDouble(i.getRetailPrice()) * volumnadd;
                                                    addReport.child("TotalPrice").setValue(String.valueOf(TotalPrice));
                                                    double TotalPriceLo = Double.parseDouble(i.getSalePriceTops()) * volumnadd;
                                                    addReport.child("TotalBuyPrice").setValue(TotalPriceLo);
                                                    addReport.child("Image").setValue(i.getImage());
                                                    addReport.child("BuyAt").setValue("Tops");


                                                    Toast.makeText(Tab9.this, i.getName() + " Already add to inventory ", Toast.LENGTH_LONG).show();
                                                    User.child("Unit").setValue(Double.toString(volumnFin));

                                                    double totalVol = volumnFin * Double.parseDouble(i.getVolume());
                                                    User.child("TotalVolume").setValue(totalVol + "");
                                                    User.child("AlreadyAddtoShoplist").setValue("false");
                                                    User.child("VolumeForAdd").setValue("0");

                                                    DatabaseReference Price = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("all").child(Shopitem.getKeyAll());
                                                    Price.removeValue();
                                                    s.removeValue();

                                                    attachRecyclerViewAdapter();

                                                } else if (which == 3) {

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
                                                    addReport.child("BuyPrice").setValue(i.getSalePriceBigC());
                                                    addReport.child("Classifier").setValue(i.getClassifier());
                                                    double TotalPrice = Double.parseDouble(i.getRetailPrice()) * volumnadd;
                                                    addReport.child("TotalPrice").setValue(String.valueOf(TotalPrice));
                                                    double TotalPriceLo = Double.parseDouble(i.getSalePriceBigC()) * volumnadd;
                                                    addReport.child("TotalBuyPrice").setValue(TotalPriceLo);
                                                    addReport.child("Image").setValue(i.getImage());
                                                    addReport.child("BuyAt").setValue("BigC");


                                                    Toast.makeText(Tab9.this, i.getName() + " Already add to inventory ", Toast.LENGTH_LONG).show();
                                                    User.child("Unit").setValue(Double.toString(volumnFin));

                                                    double totalVol = volumnFin * Double.parseDouble(i.getVolume());
                                                    User.child("TotalVolume").setValue(totalVol + "");
                                                    User.child("AlreadyAddtoShoplist").setValue("false");
                                                    User.child("VolumeForAdd").setValue("0");

                                                    DatabaseReference Price = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("all").child(Shopitem.getKeyAll());
                                                    Price.removeValue();
                                                    s.removeValue();

                                                    attachRecyclerViewAdapter();

                                                } else if (which == 4) {

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
                                                    addReport.child("BuyPrice").setValue(i.getSalePriceFoodland());
                                                    addReport.child("Classifier").setValue(i.getClassifier());
                                                    double TotalPrice = Double.parseDouble(i.getRetailPrice()) * volumnadd;
                                                    addReport.child("TotalPrice").setValue(String.valueOf(TotalPrice));
                                                    double TotalPriceLo = Double.parseDouble(i.getSalePriceFoodland()) * volumnadd;
                                                    addReport.child("TotalBuyPrice").setValue(TotalPriceLo);
                                                    addReport.child("Image").setValue(i.getImage());
                                                    addReport.child("BuyAt").setValue("Foodland");


                                                    Toast.makeText(Tab9.this, i.getName() + " Already add to inventory ", Toast.LENGTH_LONG).show();
                                                    User.child("Unit").setValue(Double.toString(volumnFin));

                                                    double totalVol = volumnFin * Double.parseDouble(i.getVolume());
                                                    User.child("TotalVolume").setValue(totalVol + "");
                                                    User.child("AlreadyAddtoShoplist").setValue("false");
                                                    User.child("VolumeForAdd").setValue("0");

                                                    DatabaseReference Price = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("all").child(Shopitem.getKeyAll());
                                                    Price.removeValue();
                                                    s.removeValue();

                                                    attachRecyclerViewAdapter();

                                                } else if (which == 5) {

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
                                                    addReport.child("BuyPrice").setValue(i.getSalePriceHomeFreshMart());
                                                    addReport.child("Classifier").setValue(i.getClassifier());
                                                    double TotalPrice = Double.parseDouble(i.getRetailPrice()) * volumnadd;
                                                    addReport.child("TotalPrice").setValue(String.valueOf(TotalPrice));
                                                    double TotalPriceLo = Double.parseDouble(i.getSalePriceHomeFreshMart()) * volumnadd;
                                                    addReport.child("TotalBuyPrice").setValue(TotalPriceLo);
                                                    addReport.child("Image").setValue(i.getImage());
                                                    addReport.child("BuyAt").setValue("HomeFreshMart");


                                                    Toast.makeText(Tab9.this, i.getName() + " Already add to inventory ", Toast.LENGTH_LONG).show();
                                                    User.child("Unit").setValue(Double.toString(volumnFin));

                                                    double totalVol = volumnFin * Double.parseDouble(i.getVolume());
                                                    User.child("TotalVolume").setValue(totalVol + "");
                                                    User.child("AlreadyAddtoShoplist").setValue("false");
                                                    User.child("VolumeForAdd").setValue("0");

                                                    DatabaseReference Price = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("all").child(Shopitem.getKeyAll());
                                                    Price.removeValue();
                                                    s.removeValue();

                                                    attachRecyclerViewAdapter();

                                                } else if (which == 6) {

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
                                                    addReport.child("BuyPrice").setValue(i.getSalePriceMaxValue());
                                                    addReport.child("Classifier").setValue(i.getClassifier());
                                                    double TotalPrice = Double.parseDouble(i.getRetailPrice()) * volumnadd;
                                                    addReport.child("TotalPrice").setValue(String.valueOf(TotalPrice));
                                                    double TotalPriceLo = Double.parseDouble(i.getSalePriceMaxValue()) * volumnadd;
                                                    addReport.child("TotalBuyPrice").setValue(TotalPriceLo);
                                                    addReport.child("Image").setValue(i.getImage());
                                                    addReport.child("BuyAt").setValue("MaxValue");


                                                    Toast.makeText(Tab9.this, i.getName() + " Already add to inventory ", Toast.LENGTH_LONG).show();
                                                    User.child("Unit").setValue(Double.toString(volumnFin));

                                                    double totalVol = volumnFin * Double.parseDouble(i.getVolume());
                                                    User.child("TotalVolume").setValue(totalVol + "");
                                                    User.child("AlreadyAddtoShoplist").setValue("false");
                                                    User.child("VolumeForAdd").setValue("0");

                                                    DatabaseReference Price = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("all").child(Shopitem.getKeyAll());
                                                    Price.removeValue();
                                                    s.removeValue();

                                                    attachRecyclerViewAdapter();

                                                } else if (which == 7) {

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
                                                    addReport.child("BuyPrice").setValue(i.getSalePriceMakro());
                                                    addReport.child("Classifier").setValue(i.getClassifier());
                                                    double TotalPrice = Double.parseDouble(i.getRetailPrice()) * volumnadd;
                                                    addReport.child("TotalPrice").setValue(String.valueOf(TotalPrice));
                                                    double TotalPriceLo = Double.parseDouble(i.getSalePriceMakro()) * volumnadd;
                                                    addReport.child("TotalBuyPrice").setValue(TotalPriceLo);
                                                    addReport.child("Image").setValue(i.getImage());
                                                    addReport.child("BuyAt").setValue("Makro");


                                                    Toast.makeText(Tab9.this, i.getName() + " Already add to inventory ", Toast.LENGTH_LONG).show();
                                                    User.child("Unit").setValue(Double.toString(volumnFin));

                                                    double totalVol = volumnFin * Double.parseDouble(i.getVolume());
                                                    User.child("TotalVolume").setValue(totalVol + "");
                                                    User.child("AlreadyAddtoShoplist").setValue("false");
                                                    User.child("VolumeForAdd").setValue("0");

                                                    DatabaseReference Price = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("all").child(Shopitem.getKeyAll());
                                                    Price.removeValue();
                                                    s.removeValue();
                                                    attachRecyclerViewAdapter();

                                                } else if (which == 8) {

                                                    CharSequence priceItem[] = new CharSequence[]{"Tops", "Lotus", "BigC", "Foodland", "Home Fresh Mart", "MaxValue", "Makro"};
                                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(Tab9.this);
                                                    builder2.setTitle("Choose Shop that You buy");
                                                    builder2.setItems(priceItem, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int which2) {
                                                            if (which2 == 0) {
                                                                AlertDialog.Builder builder3 = new AlertDialog.Builder(Tab9.this);
                                                                builder3.setTitle("How much price that you buy ?");
                                                                final EditText input = new EditText(Tab9.this);
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
                                                                        addReport.child("BuyAt").setValue("Tops");


                                                                        Toast.makeText(Tab9.this, i.getName() + " Already add to inventory ", Toast.LENGTH_LONG).show();
                                                                        User.child("Unit").setValue(Double.toString(volumnFin));

                                                                        double totalVol = volumnFin * Double.parseDouble(i.getVolume());
                                                                        User.child("TotalVolume").setValue(totalVol + "");
                                                                        User.child("AlreadyAddtoShoplist").setValue("false");
                                                                        User.child("VolumeForAdd").setValue("0");
                                                                        User.child("SalePriceTops").setValue(m_Text);
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
                                                            } else if (which2 == 1) {
                                                                AlertDialog.Builder builder3 = new AlertDialog.Builder(Tab9.this);
                                                                builder3.setTitle("How much price that you buy ?");
                                                                final EditText input = new EditText(Tab9.this);
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
                                                                        addReport.child("BuyAt").setValue("Lotus");


                                                                        Toast.makeText(Tab9.this, i.getName() + " Already add to inventory ", Toast.LENGTH_LONG).show();
                                                                        User.child("Unit").setValue(Double.toString(volumnFin));

                                                                        double totalVol = volumnFin * Double.parseDouble(i.getVolume());
                                                                        User.child("TotalVolume").setValue(totalVol + "");
                                                                        User.child("AlreadyAddtoShoplist").setValue("false");
                                                                        User.child("VolumeForAdd").setValue("0");
                                                                        User.child("SalePriceLotus").setValue(m_Text);
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
                                                            } else if (which2 == 2) {
                                                                AlertDialog.Builder builder3 = new AlertDialog.Builder(Tab9.this);
                                                                builder3.setTitle("How much price that you buy ?");
                                                                final EditText input = new EditText(Tab9.this);
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
                                                                        addReport.child("BuyAt").setValue("BigC");


                                                                        Toast.makeText(Tab9.this, i.getName() + " Already add to inventory ", Toast.LENGTH_LONG).show();
                                                                        User.child("Unit").setValue(Double.toString(volumnFin));

                                                                        double totalVol = volumnFin * Double.parseDouble(i.getVolume());
                                                                        User.child("TotalVolume").setValue(totalVol + "");
                                                                        User.child("AlreadyAddtoShoplist").setValue("false");
                                                                        User.child("VolumeForAdd").setValue("0");
                                                                        User.child("SalePriceBigC").setValue(m_Text);
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
                                                            } else if (which2 == 3) {
                                                                AlertDialog.Builder builder3 = new AlertDialog.Builder(Tab9.this);
                                                                builder3.setTitle("How much price that you buy ?");
                                                                final EditText input = new EditText(Tab9.this);
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
                                                                        addReport.child("BuyAt").setValue("Foodland");


                                                                        Toast.makeText(Tab9.this, i.getName() + " Already add to inventory ", Toast.LENGTH_LONG).show();
                                                                        User.child("Unit").setValue(Double.toString(volumnFin));

                                                                        double totalVol = volumnFin * Double.parseDouble(i.getVolume());
                                                                        User.child("TotalVolume").setValue(totalVol + "");
                                                                        User.child("AlreadyAddtoShoplist").setValue("false");
                                                                        User.child("VolumeForAdd").setValue("0");
                                                                        User.child("SalePriceFoodland").setValue(m_Text);
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
                                                            } else if (which2 == 4) {
                                                                AlertDialog.Builder builder3 = new AlertDialog.Builder(Tab9.this);
                                                                builder3.setTitle("How much price that you buy ?");
                                                                final EditText input = new EditText(Tab9.this);
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
                                                                        addReport.child("BuyAt").setValue("Home Fresh Mart");


                                                                        Toast.makeText(Tab9.this, i.getName() + " Already add to inventory ", Toast.LENGTH_LONG).show();
                                                                        User.child("Unit").setValue(Double.toString(volumnFin));

                                                                        double totalVol = volumnFin * Double.parseDouble(i.getVolume());
                                                                        User.child("TotalVolume").setValue(totalVol + "");
                                                                        User.child("AlreadyAddtoShoplist").setValue("false");
                                                                        User.child("VolumeForAdd").setValue("0");
                                                                        User.child("SalePriceHomeFreshMart").setValue(m_Text);
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
                                                            } else if (which2 == 5) {
                                                                AlertDialog.Builder builder3 = new AlertDialog.Builder(Tab9.this);
                                                                builder3.setTitle("How much price that you buy ?");
                                                                final EditText input = new EditText(Tab9.this);
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
                                                                        addReport.child("BuyAt").setValue("MaxValue");


                                                                        Toast.makeText(Tab9.this, i.getName() + " Already add to inventory ", Toast.LENGTH_LONG).show();
                                                                        User.child("Unit").setValue(Double.toString(volumnFin));

                                                                        double totalVol = volumnFin * Double.parseDouble(i.getVolume());
                                                                        User.child("TotalVolume").setValue(totalVol + "");
                                                                        User.child("AlreadyAddtoShoplist").setValue("false");
                                                                        User.child("VolumeForAdd").setValue("0");
                                                                        User.child("SalePriceMaxValue").setValue(m_Text);
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
                                                            } else if (which2 == 6) {
                                                                AlertDialog.Builder builder3 = new AlertDialog.Builder(Tab9.this);
                                                                builder3.setTitle("How much price that you buy ?");
                                                                final EditText input = new EditText(Tab9.this);
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
                                                                        addReport.child("BuyAt").setValue("Makro");


                                                                        Toast.makeText(Tab9.this, i.getName() + " Already add to inventory ", Toast.LENGTH_LONG).show();
                                                                        User.child("Unit").setValue(Double.toString(volumnFin));

                                                                        double totalVol = volumnFin * Double.parseDouble(i.getVolume());
                                                                        User.child("TotalVolume").setValue(totalVol + "");
                                                                        User.child("AlreadyAddtoShoplist").setValue("false");
                                                                        User.child("VolumeForAdd").setValue("0");
                                                                        User.child("SalePriceMakro").setValue(m_Text);
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

                                                    builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.cancel();
                                                        }
                                                    });
                                                    builder2.show();


                                                }


                                            }
                                        });
                                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
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
            }

        };
        mList.setAdapter(firebaseRecyclerAdapter);
    }



/*
        mList.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), mList, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position) {

                        final DatabaseReference s = firebaseRecyclerAdapter.getRef(position);

                        if (ShoppinglistShowlistActivity.remove == true) {




                            AlertDialog.Builder builder = new AlertDialog.Builder(Tab9.this);
                            builder.setTitle("Are you sure to remove ?");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    s.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Shoplistitem Shopitem = dataSnapshot.getValue(Shoplistitem.class);
                                            DatabaseReference Price = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("all").child(Shopitem.getKeyAll());
                                            Price.removeValue();
                                            s.removeValue();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();




                        }else if (ShoppinglistShowlistActivity.increase == true) {



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

                                            String price = null;
                                            String price2 = null;


                                            //if (Tops > Lotus){
                                                price = "Lotus : " + i.getSalePriceLotus();
                                            //}else if (Tops < Lotus){
                                                price2 = "Tops : " + i.getSalePriceTops();
                                           // }

                                            CharSequence priceItem[] = new CharSequence[] {"Retailprice : " + i.getRetailPrice() , price , price2, "Update All"};
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Tab9.this);
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

                                                    if(which == 0){

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
                                                        Toast.makeText(Tab9.this, i.getName() + " Already add to inventory ", Toast.LENGTH_LONG).show();
                                                        User.child("Unit").setValue(Double.toString(volumnFin));

                                                        double totalVol = volumnFin * Double.parseDouble(i.getVolume());
                                                        User.child("TotalVolume").setValue(totalVol+"");
                                                        s.removeValue();
                                                        DatabaseReference Price = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("all").child(Shopitem.getKeyAll());
                                                        Price.removeValue();



                                                    }else if(which == 1){

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
                                                        Toast.makeText(Tab9.this, i.getName() + " Already add to inventory ", Toast.LENGTH_LONG).show();
                                                        User.child("Unit").setValue(Double.toString(volumnFin));

                                                        double totalVol = volumnFin * Double.parseDouble(i.getVolume());

                                                        User.child("TotalVolume").setValue(totalVol+"");
                                                        s.removeValue();
                                                        DatabaseReference Price = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("all").child(Shopitem.getKeyAll());
                                                        Price.removeValue();


                                                    }else if(which == 2){

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
                                                        Toast.makeText(Tab9.this, i.getName() + " Already add to inventory ", Toast.LENGTH_LONG).show();
                                                        User.child("Unit").setValue(Double.toString(volumnFin));

                                                        double totalVol = volumnFin * Double.parseDouble(i.getVolume());

                                                        User.child("TotalVolume").setValue(totalVol+"");
                                                        s.removeValue();
                                                        DatabaseReference Price = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("all").child(Shopitem.getKeyAll());
                                                        Price.removeValue();




                                                    }else if(which == 3){

                                                                Query FoodTypeForUpdate = mDatabase.child("Food and Ingredients").orderByKey();
                                                                FoodTypeForUpdate.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                                                            Poskey = postSnapshot.getKey();
                                                                            shopitem2 = postSnapshot.getValue(Shoplistitem.class);
                                                                            key = shopitem2.getKey();
                                                                            Volum = shopitem2.getItemVolumn();
                                                                            System.out.println(Poskey);
                                                                            System.out.println(key);

                                                                            DatabaseReference Price = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("all").child(shopitem2.getKeyAll());
                                                                            Price.removeValue();

                                                                            final DatabaseReference UserItem =  FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("items")
                                                                                    .child("Food and Ingredients").child(key);
                                                                            UserItem.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                    item iteminfo = dataSnapshot.getValue(item.class);
                                                                                    double volumn = Double.parseDouble(iteminfo.getUnit());
                                                                                    volumn = Double.parseDouble(new DecimalFormat("##.##").format(volumn));

                                                                                    volumnadd = Double.parseDouble(iteminfo.getVolumeForAdd());
                                                                                    volumnFin = volumn + volumnadd;
                                                                                    volumnFin = Double.parseDouble(new DecimalFormat("##.##").format(volumnFin));
                                                                                    Calendar c = Calendar.getInstance();
                                                                                    long mill = c.getTimeInMillis();
                                                                                    SimpleDateFormat sfd = new SimpleDateFormat("yyyyMMdd");
                                                                                    SimpleDateFormat sfd2 = new SimpleDateFormat("MM");
                                                                                    String date2 = sfd.format(mill);
                                                                                    String month = sfd2.format(mill);

                                                                                    DatabaseReference report = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("report").child(String.valueOf(mill));
                                                                                    DatabaseReference addReport = report;
                                                                                    addReport.child("Time").setValue(date2);
                                                                                    addReport.child("Month").setValue(month);
                                                                                    addReport.child("keyValue").setValue(key);
                                                                                    addReport.child("Name").setValue(iteminfo.getName());
                                                                                    addReport.child("Type").setValue("Food and Ingredients");
                                                                                    addReport.child("Barcode").setValue(iteminfo.getBarcode());
                                                                                    addReport.child("Unit").setValue(iteminfo.getVolumeForAdd());
                                                                                    addReport.child("NormalPrice").setValue(iteminfo.getRetailPrice());
                                                                                    addReport.child("BuyPrice").setValue(iteminfo.getRetailPrice());
                                                                                    addReport.child("Classifier").setValue(iteminfo.getClassifier());
                                                                                    double TotalPrice = Double.parseDouble(iteminfo.getRetailPrice()) * volumnadd;
                                                                                    addReport.child("TotalPrice").setValue(String.valueOf(TotalPrice));
                                                                                    addReport.child("TotalBuyPrice").setValue(TotalPrice);
                                                                                    addReport.child("Image").setValue(i.getImage());
                                                                                    UserItem.child("Unit").setValue(Double.toString(volumnFin));
                                                                                    double TotalV = volumnFin * Double.parseDouble(iteminfo.getVolume());
                                                                                    UserItem.child("TotalVolume").setValue(TotalV+"");
                                                                                    UserItem.child("VolumeForAdd").setValue("0");


                                                                                    DatabaseReference This = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("Food and Ingredients");
                                                                                    This.removeValue();
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(DatabaseError databaseError) {

                                                                                }
                                                                            });

                                                                        }


                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                });



                                                        Query DrinkTypeForUpdate = mDatabase.child("Beverage and Drink Powder").orderByKey();
                                                        DrinkTypeForUpdate.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                                                    Poskey = postSnapshot.getKey();
                                                                    shopitem2 = postSnapshot.getValue(Shoplistitem.class);
                                                                    key = shopitem2.getKey();
                                                                    Volum = shopitem2.getItemVolumn();
                                                                    System.out.println(Poskey);
                                                                    System.out.println(key);

                                                                    DatabaseReference Price = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("all").child(shopitem2.getKeyAll());
                                                                    Price.removeValue();

                                                                    final DatabaseReference UserItem =  FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("items")
                                                                            .child("Beverage and Drink Powder").child(key);
                                                                    UserItem.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            item iteminfo = dataSnapshot.getValue(item.class);
                                                                            double volumn = Double.parseDouble(iteminfo.getUnit());
                                                                            volumn = Double.parseDouble(new DecimalFormat("##.##").format(volumn));

                                                                            volumnadd = Double.parseDouble(iteminfo.getVolumeForAdd());
                                                                            volumnFin = volumn + volumnadd;
                                                                            volumnFin = Double.parseDouble(new DecimalFormat("##.##").format(volumnFin));
                                                                            Calendar c = Calendar.getInstance();
                                                                            long mill = c.getTimeInMillis();
                                                                            SimpleDateFormat sfd = new SimpleDateFormat("yyyyMMdd");
                                                                            SimpleDateFormat sfd2 = new SimpleDateFormat("MM");
                                                                            String date2 = sfd.format(mill);
                                                                            String month = sfd2.format(mill);

                                                                            DatabaseReference report = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("report").child(String.valueOf(mill));
                                                                            DatabaseReference addReport = report;
                                                                            addReport.child("Time").setValue(date2);
                                                                            addReport.child("Month").setValue(month);
                                                                            addReport.child("keyValue").setValue(key);
                                                                            addReport.child("Name").setValue(iteminfo.getName());
                                                                            addReport.child("Type").setValue("Beverage and Drink Powder");
                                                                            addReport.child("Barcode").setValue(iteminfo.getBarcode());
                                                                            addReport.child("Unit").setValue(iteminfo.getVolumeForAdd());
                                                                            addReport.child("NormalPrice").setValue(iteminfo.getRetailPrice());
                                                                            addReport.child("BuyPrice").setValue(iteminfo.getRetailPrice());
                                                                            addReport.child("Classifier").setValue(iteminfo.getClassifier());
                                                                            double TotalPrice = Double.parseDouble(iteminfo.getRetailPrice()) * volumnadd;
                                                                            addReport.child("TotalPrice").setValue(String.valueOf(TotalPrice));
                                                                            addReport.child("TotalBuyPrice").setValue(TotalPrice);
                                                                            addReport.child("Image").setValue(i.getImage());
                                                                            UserItem.child("Unit").setValue(Double.toString(volumnFin));
                                                                            double TotalV = volumnFin * Double.parseDouble(iteminfo.getVolume());
                                                                            UserItem.child("TotalVolume").setValue(TotalV+"");
                                                                            UserItem.child("VolumeForAdd").setValue("0");


                                                                            DatabaseReference This = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("Beverage and Drink Powder");
                                                                            This.removeValue();
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });

                                                                }


                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });

                                                        Query HealthTypeForUpdate = mDatabase.child("Health and Beauty").orderByKey();
                                                        HealthTypeForUpdate.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                                                    Poskey = postSnapshot.getKey();
                                                                    shopitem2 = postSnapshot.getValue(Shoplistitem.class);
                                                                    key = shopitem2.getKey();
                                                                    Volum = shopitem2.getItemVolumn();
                                                                    System.out.println(Poskey);
                                                                    System.out.println(key);

                                                                    DatabaseReference Price = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("all").child(shopitem2.getKeyAll());
                                                                    Price.removeValue();

                                                                    final DatabaseReference UserItem =  FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("items")
                                                                            .child("Health and Beauty").child(key);
                                                                    UserItem.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            item iteminfo = dataSnapshot.getValue(item.class);
                                                                            double volumn = Double.parseDouble(iteminfo.getUnit());
                                                                            volumn = Double.parseDouble(new DecimalFormat("##.##").format(volumn));

                                                                            volumnadd = Double.parseDouble(iteminfo.getVolumeForAdd());
                                                                            volumnFin = volumn + volumnadd;
                                                                            volumnFin = Double.parseDouble(new DecimalFormat("##.##").format(volumnFin));
                                                                            Calendar c = Calendar.getInstance();
                                                                            long mill = c.getTimeInMillis();
                                                                            SimpleDateFormat sfd = new SimpleDateFormat("yyyyMMdd");
                                                                            SimpleDateFormat sfd2 = new SimpleDateFormat("MM");
                                                                            String date2 = sfd.format(mill);
                                                                            String month = sfd2.format(mill);

                                                                            DatabaseReference report = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("report").child(String.valueOf(mill));
                                                                            DatabaseReference addReport = report;
                                                                            addReport.child("Time").setValue(date2);
                                                                            addReport.child("Month").setValue(month);
                                                                            addReport.child("keyValue").setValue(key);
                                                                            addReport.child("Name").setValue(iteminfo.getName());
                                                                            addReport.child("Type").setValue("Health and Beauty");
                                                                            addReport.child("Barcode").setValue(iteminfo.getBarcode());
                                                                            addReport.child("Unit").setValue(iteminfo.getVolumeForAdd());
                                                                            addReport.child("NormalPrice").setValue(iteminfo.getRetailPrice());
                                                                            addReport.child("BuyPrice").setValue(iteminfo.getRetailPrice());
                                                                            addReport.child("Classifier").setValue(iteminfo.getClassifier());
                                                                            double TotalPrice = Double.parseDouble(iteminfo.getRetailPrice()) * volumnadd;
                                                                            addReport.child("TotalPrice").setValue(String.valueOf(TotalPrice));
                                                                            addReport.child("TotalBuyPrice").setValue(TotalPrice);
                                                                            addReport.child("Image").setValue(i.getImage());
                                                                            UserItem.child("Unit").setValue(Double.toString(volumnFin));
                                                                            double TotalV = volumnFin * Double.parseDouble(iteminfo.getVolume());
                                                                            UserItem.child("TotalVolume").setValue(TotalV+"");
                                                                            UserItem.child("VolumeForAdd").setValue("0");


                                                                            DatabaseReference This = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("Health and Beauty");
                                                                            This.removeValue();
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });

                                                                }


                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });

                                                        Query HouseTypeForUpdate = mDatabase.child("Household Product").orderByKey();
                                                        HouseTypeForUpdate.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                                                    Poskey = postSnapshot.getKey();
                                                                    shopitem2 = postSnapshot.getValue(Shoplistitem.class);
                                                                    key = shopitem2.getKey();
                                                                    Volum = shopitem2.getItemVolumn();
                                                                    System.out.println(Poskey);
                                                                    System.out.println(key);

                                                                    DatabaseReference Price = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("all").child(shopitem2.getKeyAll());
                                                                    Price.removeValue();

                                                                    final DatabaseReference UserItem =  FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("items")
                                                                            .child("Household Product").child(key);
                                                                    UserItem.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            item iteminfo = dataSnapshot.getValue(item.class);
                                                                            double volumn = Double.parseDouble(iteminfo.getUnit());
                                                                            volumn = Double.parseDouble(new DecimalFormat("##.##").format(volumn));

                                                                            volumnadd = Double.parseDouble(iteminfo.getVolumeForAdd());
                                                                            volumnFin = volumn + volumnadd;
                                                                            volumnFin = Double.parseDouble(new DecimalFormat("##.##").format(volumnFin));
                                                                            Calendar c = Calendar.getInstance();
                                                                            long mill = c.getTimeInMillis();
                                                                            SimpleDateFormat sfd = new SimpleDateFormat("yyyyMMdd");
                                                                            SimpleDateFormat sfd2 = new SimpleDateFormat("MM");
                                                                            String date2 = sfd.format(mill);
                                                                            String month = sfd2.format(mill);

                                                                            DatabaseReference report = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("report").child(String.valueOf(mill));
                                                                            DatabaseReference addReport = report;
                                                                            addReport.child("Time").setValue(date2);
                                                                            addReport.child("Month").setValue(month);
                                                                            addReport.child("keyValue").setValue(key);
                                                                            addReport.child("Name").setValue(iteminfo.getName());
                                                                            addReport.child("Type").setValue("Household Product");
                                                                            addReport.child("Barcode").setValue(iteminfo.getBarcode());
                                                                            addReport.child("Unit").setValue(iteminfo.getVolumeForAdd());
                                                                            addReport.child("NormalPrice").setValue(iteminfo.getRetailPrice());
                                                                            addReport.child("BuyPrice").setValue(iteminfo.getRetailPrice());
                                                                            addReport.child("Classifier").setValue(iteminfo.getClassifier());
                                                                            double TotalPrice = Double.parseDouble(iteminfo.getRetailPrice()) * volumnadd;
                                                                            addReport.child("TotalPrice").setValue(String.valueOf(TotalPrice));
                                                                            addReport.child("TotalBuyPrice").setValue(TotalPrice);
                                                                            addReport.child("Image").setValue(i.getImage());
                                                                            UserItem.child("Unit").setValue(Double.toString(volumnFin));
                                                                            UserItem.child("VolumeForAdd").setValue("0");
                                                                            double TotalV = volumnFin * Double.parseDouble(iteminfo.getVolume());
                                                                            UserItem.child("TotalVolume").setValue(TotalV+"");


                                                                            DatabaseReference This = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("Household Product");
                                                                            This.removeValue();
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });

                                                                }


                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });


                                                        Query EtcTypeForUpdate = mDatabase.child("Etc").orderByKey();
                                                        EtcTypeForUpdate.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                                                    Poskey = postSnapshot.getKey();
                                                                    shopitem2 = postSnapshot.getValue(Shoplistitem.class);
                                                                    key = shopitem2.getKey();
                                                                    Volum = shopitem2.getItemVolumn();
                                                                    System.out.println(Poskey);
                                                                    System.out.println(key);

                                                                    DatabaseReference Price = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("all").child(shopitem2.getKeyAll());
                                                                    Price.removeValue();

                                                                    final DatabaseReference UserItem =  FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("items")
                                                                            .child("Etc").child(key);
                                                                    UserItem.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            item iteminfo = dataSnapshot.getValue(item.class);
                                                                            double volumn = Double.parseDouble(iteminfo.getUnit());
                                                                            volumn = Double.parseDouble(new DecimalFormat("##.##").format(volumn));

                                                                            volumnadd = Double.parseDouble(iteminfo.getVolumeForAdd());
                                                                            volumnFin = volumn + volumnadd;
                                                                            volumnFin = Double.parseDouble(new DecimalFormat("##.##").format(volumnFin));
                                                                            Calendar c = Calendar.getInstance();
                                                                            long mill = c.getTimeInMillis();
                                                                            SimpleDateFormat sfd = new SimpleDateFormat("yyyyMMdd");
                                                                            SimpleDateFormat sfd2 = new SimpleDateFormat("MM");
                                                                            String date2 = sfd.format(mill);
                                                                            String month = sfd2.format(mill);

                                                                            DatabaseReference report = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("report").child(String.valueOf(mill));
                                                                            DatabaseReference addReport = report;
                                                                            addReport.child("Time").setValue(date2);
                                                                            addReport.child("Month").setValue(month);
                                                                            addReport.child("keyValue").setValue(key);
                                                                            addReport.child("Name").setValue(iteminfo.getName());
                                                                            addReport.child("Type").setValue("Etc");
                                                                            addReport.child("Barcode").setValue(iteminfo.getBarcode());
                                                                            addReport.child("Unit").setValue(iteminfo.getVolumeForAdd());
                                                                            addReport.child("NormalPrice").setValue(iteminfo.getRetailPrice());
                                                                            addReport.child("BuyPrice").setValue(iteminfo.getRetailPrice());
                                                                            addReport.child("Classifier").setValue(iteminfo.getClassifier());
                                                                            double TotalPrice = Double.parseDouble(iteminfo.getRetailPrice()) * volumnadd;
                                                                            addReport.child("TotalPrice").setValue(String.valueOf(TotalPrice));
                                                                            addReport.child("TotalBuyPrice").setValue(TotalPrice);
                                                                            addReport.child("Image").setValue(iteminfo.getImage());
                                                                            UserItem.child("Unit").setValue(Double.toString(volumnFin));
                                                                            double TotalV = volumnFin * Double.parseDouble(iteminfo.getVolume());
                                                                            UserItem.child("TotalVolume").setValue(TotalV+"");
                                                                            UserItem.child("VolumeForAdd").setValue("0");


                                                                            DatabaseReference This = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child("Etc");
                                                                            This.removeValue();
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });

                                                                }


                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });








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
                                    final AlertDialog.Builder builder2 = new AlertDialog.Builder(Tab9.this);
                                    //TextView title = new TextView(Tab9.this);
                                    //title.setText(Shopitem.getItemName());
                                    //title.setGravity(Gravity.CENTER);
                                    //builder2.setCustomTitle(title);

                                    builder2.setTitle(Shopitem.getItemName());
                                    String alert1 = "RetailPrice : " + Shopitem.getItemPrice() + " THB";
                                    String alert2 = "Tops  :  " + Shopitem.getItemTopsPrice() + " THB";
                                    String alert3 = "Lotus :  " + Shopitem.getItemLotusPrice() + " THB";


                                    double Tops = Double.parseDouble(Shopitem.getItemTopsPrice());
                                    double Lotus = Double.parseDouble(Shopitem.getItemLotusPrice());
                                    double Retail = Double.parseDouble(Shopitem.getItemPrice());

                                    String color = "#FF0000";

                                    if(Retail < Lotus && Retail < Tops) {
                                        builder2.setMessage(alert1 + "\n\n" + alert2 + "\n\n" + alert3);

                                    }else if (Tops > Lotus) {
                                        String text = Shopitem.getItemLotusPrice();
                                        String input = "<font color=" + color + ">" + text + " THB"+ "</font>";

                                        builder2.setMessage(Html.fromHtml(alert1 + "<br><br>" + alert2 + "<br><br>" + "<font color='#FF0000'><b> Lotus  : </b></font>" +  input));
                                    } else if (Lotus > Tops) {
                                        String text = Shopitem.getItemTopsPrice();
                                        String input = "<font color=" + color + ">" + text + " THB" + "</font>";
                                        builder2.setMessage(Html.fromHtml(alert1 + "<br><br>" + "<font color='#FF0000'><b> Tops </b></font>" + " : " + input + "<br><br>" + alert3)
                                        );
                                    }else {
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

*/


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


}