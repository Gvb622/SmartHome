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
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

public class Tab9 extends AppCompatActivity {

    private String value;
    private RecyclerView mList;
    private Query qType;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private ImageButton AddItem2;

    public boolean remove = false;
    private ImageButton RemoveItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab6);

        Bundle extras = getIntent().getExtras();
        value = extras.getString("key");
        System.out.println(value);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist").child(value).child("item");
        qType = mDatabase.orderByChild("Type").equalTo("Household Product");

        mList = (RecyclerView) findViewById(R.id.item_listShopping);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(this, R.drawable.list_divider), true));

        AddItem2 = (ImageButton) findViewById(R.id.Additem2) ;

        AddItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent5 = new Intent(Tab9.this, SelectItemShop.class);
                intent5.putExtra("key",value);
                startActivity(intent5);
            }
        });

        RemoveItem = (ImageButton) findViewById(R.id.RemoveItem2);
        RemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean b = !remove ;
                remove = b;

                if(b == true){
                    RemoveItem.setImageResource(R.mipmap.ic_clear_white_24dp);
                } else{
                    RemoveItem.setImageResource(R.mipmap.ic_clear_black_24dp);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        final FirebaseRecyclerAdapter<Shoplistitem, ItemViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Shoplistitem, ItemViewHolder>(

                Shoplistitem.class,
                R.layout.shoppinglistitem,
                ItemViewHolder.class,
                qType
        ) {
            @Override
            protected void populateViewHolder(ItemViewHolder viewHolder, Shoplistitem model, int position) {

                viewHolder.setName2(model.getItemName());
                viewHolder.setPrice2(model.getItemPrice());
                viewHolder.setVolumn2(model.getItemVolumn());
                viewHolder.setImage2(getApplicationContext(), model.getItemImage());
            }
        };

        mList.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), mList, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position) {

                        final DatabaseReference s = firebaseRecyclerAdapter.getRef(position);

                        if(remove == true){

                            AlertDialog.Builder builder = new AlertDialog.Builder(Tab9.this);
                            builder.setTitle("Are you sure to remove ?");
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

    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }
        public void setName2(String name){
            TextView Name = (TextView) mView.findViewById(R.id.nameShopItem);
            Name.setText(name);
        }
        public void setVolumn2(String volumn){
            TextView Volumn = (TextView) mView.findViewById(R.id.volumnShopItem);
            Volumn.setText(volumn);
        }
        public void setPrice2(String price){
            TextView Price = (TextView) mView.findViewById(R.id.priceShopItem2);
            Price.setText(price);
        }
        public void setImage2(Context ctx , String image){
            ImageView imageView = (ImageView) mView.findViewById(R.id.imageShopItem);
            Picasso.with(ctx).load(image).fit().placeholder(R.mipmap.ic_launcher).into(imageView);
        }
    }
}
