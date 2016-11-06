package com.smart.smarthome;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Tab3 extends AppCompatActivity {

    private RecyclerView mList;
    private ImageButton Additem;
    static public ImageButton Increaseitem;
    static public ImageButton Decreaseitem;
    static public ImageButton Removeitem;
    private Query qType;
    private Query q2Type;
    private String m_Text;
    FirebaseUser user;
    DatabaseReference s;




    FirebaseRecyclerAdapter<item, ItemViewHolder> firebaseRecyclerAdapter;


    private ImageView imageView;

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    static final int GET_BAR_CODE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab1);

        Additem = (ImageButton) findViewById(R.id.Additem2);

        Removeitem = (ImageButton) findViewById(R.id.RemoveItem2);


        imageView = (ImageView) findViewById(R.id.imageItem);
        firebaseAuth = FirebaseAuth.getInstance();


        user = firebaseAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("items");
        qType = mDatabase.child("Health and Beauty").orderByChild("Name");


        mList = (RecyclerView) findViewById(R.id.item_list2);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(this, R.drawable.list_divider), true));

        Removeitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean r = ! MainActivity.removeitem;
                MainActivity.removeitem = r;
                if(r == true){
                    Removeitem.setImageResource(R.mipmap.ic_clear_white_24dp);
                    attachRecyclerViewAdapter();
                    Toast.makeText(Tab3.this, "Plese click on item that you want to delete",Toast.LENGTH_LONG).show();



                }else{
                    Removeitem.setImageResource(R.mipmap.ic_clear_black_24dp);
                }
            }
        });


        Additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.removeitem = false;
                Removeitem.setImageResource(R.mipmap.ic_clear_black_24dp);


                CharSequence colors[] = new CharSequence[] {"Barcode", "Manual"};
                AlertDialog.Builder builder = new AlertDialog.Builder(Tab3.this);
                builder.setTitle("Add by");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            startActivity(new Intent(Tab3.this, ShowBarcode.class));
                        }else if(which == 1){
                            startActivity(new Intent(Tab3.this, AddItemActivity.class));
                        }
                    }
                });
                builder.show();

            }

        });




    }


    @Override
    protected void onStart() {

        super.onStart();
        attachRecyclerViewAdapter();
    }

    public void attachRecyclerViewAdapter(){
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<item, ItemViewHolder>(

                item.class,
                R.layout.itemlist,
                ItemViewHolder.class,
                qType
        ) {
            @Override
            protected void populateViewHolder(ItemViewHolder viewHolder, final item model, final int position) {


                try{

                    double value = Double.parseDouble(model.getUnit());
                    value =Double.parseDouble(new DecimalFormat("##.##").format(value));

                    double TotalVolume = Double.parseDouble(model.getTotalVolume());
                    TotalVolume =Double.parseDouble(new DecimalFormat("##.##").format(TotalVolume));




                    if(model.getLowBy().equals("Quantity")) {
                        viewHolder.setVolumn(value + "  " + model.getClassifier());
                    }else if(model.getLowBy().equals("Volume")){
                        viewHolder.setVolumn(TotalVolume + "  " + model.getQuantity()
                                + "\n" + value + "  " + model.getClassifier() );
                    }
                }catch (Exception e){

                }
                viewHolder.setName(model.getName());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        s = firebaseRecyclerAdapter.getRef(position);

                        if (MainActivity.removeitem == true){
                            AlertDialog.Builder builder = new AlertDialog.Builder(Tab3.this);
                            builder.setTitle("Remove item");
                            builder.setMessage("Are you sure to remove this item ?");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    s.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(model.getAlreadyAddtoShoplist().equals("true")) {
                                                DatabaseReference shopall = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist")
                                                        .child("all").child(model.getShopAllkey());
                                                DatabaseReference shopl = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist")
                                                        .child(s.getParent().getKey()).child(model.getShopkey());
                                                shopall.removeValue();
                                                shopl.removeValue();
                                            }
                                            s.removeValue();
                                            attachRecyclerViewAdapter();
                                            MainActivity.removeitem = false;
                                            Removeitem.setImageResource(R.mipmap.ic_clear_black_24dp);

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
                        }else{
                            Intent i = new Intent(Tab3.this, ShowInformationItem.class);
                            i.putExtra("key",s.getKey());
                            i.putExtra("Type",s.getParent().getKey());
                            startActivity(i);
                        }
                    }
                });

                viewHolder.increaseImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         s = firebaseRecyclerAdapter.getRef(position);
                        System.out.println("AAA : " + s);
                        AlertDialog.Builder builder = new AlertDialog.Builder(Tab3.this);
                        builder.setTitle("How many quantity ?");
                        final EditText input = new EditText(Tab3.this);
                        input.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                        builder.setView(input);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                m_Text = input.getText().toString();
                                final String Type = s.getParent().getKey();
                                s.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        item item = dataSnapshot.getValue(item.class);
                                        if(model.getAlreadyAddtoShoplist().equals("true")){

                                            DatabaseReference shopall = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist")
                                                    .child("all").child(model.getShopAllkey());
                                            if (m_Text.equals("")) {
                                                m_Text = "0";
                                            }
                                            Double ItemV = Double.parseDouble(model.getVolumeForAdd()) + Double.parseDouble(m_Text);
                                            shopall.child("ItemVolumn").setValue(ItemV);

                                            DatabaseReference shopl = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist")
                                                    .child(Type).child(model.getShopkey());
                                            shopl.child("ItemVolumn").setValue(ItemV);
                                            s.child("VolumeForAdd").setValue(ItemV+"");

                                        }else {
                                            DatabaseReference TotalPrice = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist")
                                                    .child("all");
                                            DatabaseReference Total = TotalPrice.push();
                                            Total.child("ItemPrice").setValue(item.getRetailPrice());
                                            Total.child("ItemTopsPrice").setValue(item.getSalePriceTops());
                                            Total.child("ItemLotusPrice").setValue(item.getSalePriceLotus());
                                            Total.child("ItemBigCPrice").setValue(item.getSalePriceBigC());
                                            Total.child("ItemFoodLandPrice").setValue(item.getSalePriceFoodland());
                                            Total.child("ItemHomeFreshMartPrice").setValue(item.getSalePriceHomeFreshMart());
                                            Total.child("ItemMaxValuePrice").setValue(item.getSalePriceMaxValue());
                                            Total.child("ItemMakroPrice").setValue(item.getSalePriceMakro());

                                            if (m_Text.equals("")) {
                                                m_Text = "1";
                                            }
                                            Total.child("ItemVolumn").setValue(Double.parseDouble(m_Text));


                                            DatabaseReference Shoplist2 = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist")
                                                    .child(Type);
                                            DatabaseReference Shoplist = Shoplist2.push();
                                            Shoplist.child("ItemImage").setValue(item.getImage());
                                            Shoplist.child("ItemName").setValue(item.getName());
                                            Shoplist.child("ItemPrice").setValue(item.getRetailPrice());
                                            Shoplist.child("Key").setValue(s.getKey());
                                            Shoplist.child("KeyAll").setValue(Total.getKey());
                                            Shoplist.child("ItemClassifier").setValue(item.getClassifier());
                                            Shoplist.child("ItemVolumn").setValue(Double.parseDouble(m_Text));
                                            Shoplist.child("ItemTopsPrice").setValue(item.getSalePriceTops());
                                            Shoplist.child("ItemLotusPrice").setValue(item.getSalePriceLotus());
                                            Shoplist.child("ItemBigCPrice").setValue(item.getSalePriceBigC());
                                            Shoplist.child("ItemFoodLandPrice").setValue(item.getSalePriceFoodland());
                                            Shoplist.child("ItemHomeFreshMartPrice").setValue(item.getSalePriceHomeFreshMart());
                                            Shoplist.child("ItemMaxValuePrice").setValue(item.getSalePriceMaxValue());
                                            Shoplist.child("ItemMakroPrice").setValue(item.getSalePriceMakro());

                                            DatabaseReference User = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("items")
                                                    .child(Type).child(s.getKey());
                                            User.child("VolumeForAdd").setValue(m_Text+"");
                                            User.child("AlreadyAddtoShoplist").setValue("true");
                                            User.child("Shopkey").setValue(Shoplist.getKey());
                                            User.child("ShopAllkey").setValue(Total.getKey());

                                            double retail = Double.parseDouble(item.getRetailPrice());
                                            double tops = Double.parseDouble(item.getSalePriceTops());
                                            double lotus = Double.parseDouble(item.getSalePriceLotus());
                                            double bigC = Double.parseDouble(item.getSalePriceBigC());
                                            double foodland = Double.parseDouble(item.getSalePriceFoodland());
                                            double homefreshmart = Double.parseDouble(item.getSalePriceHomeFreshMart());
                                            double maxValue = Double.parseDouble(item.getSalePriceMaxValue());
                                            double makro = Double.parseDouble(item.getSalePriceMakro());

                                            double min = (Math.min(Math.min(Math.min(Math.min(Math.min(Math.min(Math.min(tops, retail),lotus), bigC), foodland), homefreshmart), maxValue), makro));
                                            List<ItemCheaper> cheaper =new ArrayList<ItemCheaper>();
                                            cheaper.add(new ItemCheaper("Tops", tops));
                                            cheaper.add(new ItemCheaper("Lotus",lotus));
                                            cheaper.add(new ItemCheaper("BigC",bigC));
                                            cheaper.add(new ItemCheaper("Foodland",foodland));
                                            cheaper.add(new ItemCheaper("HomeFreshMart",homefreshmart));
                                            cheaper.add(new ItemCheaper("MaxValue",maxValue));
                                            cheaper.add(new ItemCheaper("Makro",makro));
                                            Collections.sort(cheaper);

                                            String ShopCheaper = "";

                                            for(ItemCheaper ic : cheaper){
                                                if(min == ic.getPrice()){
                                                    ShopCheaper = ShopCheaper + ic.getName();
                                                }
                                            }
                                            Shoplist.child("ShopCheapest").setValue(ShopCheaper);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }

                                });
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
                });

                viewHolder.decreaseImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         s = firebaseRecyclerAdapter.getRef(position);
                        s.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                item item = dataSnapshot.getValue(item.class);

                                double i = Double.parseDouble(item.getTotalVolume());
                                double j ;

                                if(item.getLowBy().equals("Volume")){
                                    j = Double.parseDouble(item.getDecreasePerClick());
                                }else{
                                    j = Double.parseDouble(item.getDecreasePerClick()) * Double.parseDouble(item.getVolume());
                                }

                                i = i - j ;
                                if(i < 0){
                                    i = 0;
                                }

                                double k = i / Double.parseDouble(item.getVolume());

                                if(item.getLowBy().equals("Volume")){
                                    s.child("TotalVolume").setValue(Double.toString(i));
                                    s.child("Unit").setValue(Double.toString(k));

                                }else {
                                    s.child("TotalVolume").setValue(Double.toString(i));
                                    s.child("Unit").setValue(Double.toString(k));

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });


                viewHolder.setImage(getApplicationContext(), model.getImage());

                try{
                    double softline = Double.parseDouble(model.getSoftline());
                    double deadline = Double.parseDouble(model.getDeadline());
                    if(model.getLowBy().equals("Quantity")){
                        double unit = Double.parseDouble(model.getUnit());
                        if (unit <= deadline){
                            viewHolder.setBackground(3);
                        }else if (unit <= softline){
                            viewHolder.setBackground(2);
                        }else{
                            viewHolder.setBackground(1);
                        }
                    }else if(model.getLowBy().equals("Volume")){
                        double totalunit = Double.parseDouble(model.getTotalVolume());

                        if (totalunit <= deadline){
                            viewHolder.setBackground(3);
                        }else if (totalunit <= softline){
                            viewHolder.setBackground(2);
                        }else{
                            viewHolder.setBackground(1);
                        }
                    }

                    if(model.getAlreadyAddtoShoplist().equals("true")){
                        viewHolder.setIncreaseButton(1);
                    }else{
                        viewHolder.setIncreaseButton(0);
                    }
                }catch (Exception e){

                }


            }
        };

            /*
            mList.addOnItemTouchListener(
                    new RecyclerItemClickListener(getApplicationContext(), mList ,new RecyclerItemClickListener.OnItemClickListener() {
                        @Override public boolean onItemClick(View view, int position) {

                             s = firebaseRecyclerAdapter.getRef(position);

                            if(MainActivity.decreaseitem == true) {

                                s.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        item item = dataSnapshot.getValue(item.class);

                                        double i = Double.parseDouble(item.getTotalVolume());
                                        double j ;

                                        if(item.getLowBy().equals("Volume")){
                                            j = Double.parseDouble(item.getDecreasePerClick());
                                        }else{
                                            j = Double.parseDouble(item.getDecreasePerClick()) * Double.parseDouble(item.getVolume());
                                        }

                                        i = i - j ;
                                        if(i < 0){
                                            i = 0;
                                        }

                                        double k = i / Double.parseDouble(item.getVolume());

                                        if(item.getLowBy().equals("Volume")){
                                            s.child("TotalVolume").setValue(Double.toString(i));
                                            s.child("Unit").setValue(Double.toString(k));

                                        }else {
                                            s.child("TotalVolume").setValue(Double.toString(i));
                                            s.child("Unit").setValue(Double.toString(k));

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            } else  if (MainActivity.additem == true){

                                s.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        item item = dataSnapshot.getValue(item.class);

                                        double i = Double.parseDouble(item.getTotalVolume());
                                        double j ;

                                        if(item.getLowBy().equals("Volume")){
                                            j = Double.parseDouble(item.getDecreasePerClick());
                                        }else{
                                            j = Double.parseDouble(item.getDecreasePerClick()) * Double.parseDouble(item.getVolume());
                                        }

                                        i = i + j ;
                                        if(i < 0){
                                            i = 0;
                                        }

                                        double k = i / Double.parseDouble(item.getVolume());

                                        if(item.getLowBy().equals("Volume")){
                                            s.child("TotalVolume").setValue(Double.toString(i));
                                            s.child("Unit").setValue(Double.toString(k));

                                        }else {
                                            s.child("TotalVolume").setValue(Double.toString(i));
                                            s.child("Unit").setValue(Double.toString(k));

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                            } else if (MainActivity.removeitem == true){
                                AlertDialog.Builder builder = new AlertDialog.Builder(Tab3.this);
                                builder.setTitle("Are you sure ?");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        s.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
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
                            }else{

                                Intent i = new Intent(Tab3.this, ShowInformationItem.class);
                                i.putExtra("key",s.getKey());
                                i.putExtra("Type",s.getParent().getKey());

                                startActivity(i);

                            }
                            return true;

                        }
                        @Override public void onLongItemClick(View view, int position) {


                        }


                    })


            );
            */


        mList.setAdapter(firebaseRecyclerAdapter);

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        View mView;
        ImageButton increaseImageButton;
        ImageButton decreaseImageButton;


        public ItemViewHolder(View itemView) {
            super(itemView);

            mView = itemView ;

            increaseImageButton = (ImageButton) mView.findViewById(R.id.IncreaseImageButton);
            decreaseImageButton = (ImageButton) mView.findViewById(R.id.DecreaseImageButton);

        }
        public void setName(String Name){
            TextView name = (TextView) mView.findViewById(R.id.nameItem);
            name.setText(Name);
        }
        public void setVolumn(String Volumn){
            TextView volumn = (TextView) mView.findViewById(R.id.volumnItem);
            volumn.setText(Volumn);
            volumn.setLines(2);

        }
        public void setImage(Context ctx , String image){
            ImageView imageV = (ImageView) mView.findViewById(R.id.imageItem);
            Picasso.with(ctx).load(image).fit().placeholder(R.mipmap.ic_launcher).into(imageV);

        }
        public void setBackground (int i){
            ImageView imageB = (ImageView) mView.findViewById(R.id.sign);
            if(i == 1) {
                imageB.setBackgroundColor(Color.GREEN);
                imageB.invalidate();
            }else if(i == 2){
                imageB.setBackgroundColor(Color.YELLOW);
                imageB.invalidate();
            }else{
                imageB.setBackgroundColor(Color.RED);
                imageB.invalidate();
            }

        }
        public void setIncreaseButton(int i){
            if(i == 1) {
                increaseImageButton.setImageResource(R.mipmap.ic_increaseimagebuttonalready);
            }else{
                increaseImageButton.setImageResource(R.mipmap.ic_imageincreasebutton);

            }
        }


    }

}
