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


public class Tab5 extends AppCompatActivity {

    private RecyclerView mList;
    private ImageButton Additem;
    private ImageButton Increaseitem;
    private ImageButton Decreaseitem;
    private ImageButton Removeitem;
    private Query qType;
    private Query q2Type;


    private ImageView imageView;


    private boolean additem ;
    private boolean decreaseitem;
    private boolean removeitem;

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    static final int GET_BAR_CODE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab1);

        Additem = (ImageButton) findViewById(R.id.Additem2);
        Increaseitem = (ImageButton) findViewById(R.id.IncreaseItem2);
        Decreaseitem = (ImageButton) findViewById(R.id.DecreaseItem2);
        Removeitem = (ImageButton) findViewById(R.id.RemoveItem2);

        imageView = (ImageView) findViewById(R.id.imageItem);
        firebaseAuth = FirebaseAuth.getInstance();
        additem = false;
        decreaseitem = false;
        removeitem = false;

        FirebaseUser user = firebaseAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("items");
        qType = mDatabase.orderByChild("Type").equalTo("Etc");


        mList = (RecyclerView) findViewById(R.id.item_list2);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(this, R.drawable.list_divider), true));

        Removeitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean r = !removeitem;
                removeitem = r;
                if(r == true){
                    Removeitem.setImageResource(R.mipmap.ic_clear_white_24dp);
                    additem = false;
                    Increaseitem.setImageResource(R.mipmap.ic_arrow_upward_black_24dp);
                    decreaseitem = false;
                    Decreaseitem.setImageResource(R.mipmap.ic_arrow_downward_black_24dp);

                }else{
                    Removeitem.setImageResource(R.mipmap.ic_clear_black_24dp);
                }
            }
        });
        Additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeitem = false;
                Removeitem.setImageResource(R.mipmap.ic_clear_black_24dp);
                additem = false;
                Increaseitem.setImageResource(R.mipmap.ic_arrow_upward_black_24dp);
                decreaseitem = false;
                Decreaseitem.setImageResource(R.mipmap.ic_arrow_downward_black_24dp);

                CharSequence colors[] = new CharSequence[] {"Barcode", "Manual"};
                AlertDialog.Builder builder = new AlertDialog.Builder(Tab5.this);
                builder.setTitle("Choose One");
                builder.setItems(colors, new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){

                            startActivity(new Intent(Tab5.this, ShowBarcode.class));


                        }else if(which == 1){
                            startActivity(new Intent(Tab5.this, AddItemActivity.class));
                        }
                    }
                });
                builder.show();


            }
        });


        Decreaseitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean a = !decreaseitem;
                decreaseitem = a;
                if(a == true){
                    Decreaseitem.setImageResource(R.mipmap.ic_arrow_downward_white_24dp);
                    additem = false;
                    Increaseitem.setImageResource(R.mipmap.ic_arrow_upward_black_24dp);
                    removeitem = false;
                    Removeitem.setImageResource(R.mipmap.ic_clear_black_24dp);

                }else{
                    Decreaseitem.setImageResource(R.mipmap.ic_arrow_downward_black_24dp);
                }

            }
        });

        Increaseitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b = !additem ;
                additem = b;

                if(b == true){

                    Increaseitem.setImageResource(R.mipmap.ic_arrow_upward_white_24dp);
                    decreaseitem = false;
                    Decreaseitem.setImageResource(R.mipmap.ic_arrow_downward_black_24dp);
                    removeitem = false;
                    Removeitem.setImageResource(R.mipmap.ic_clear_black_24dp);

                }else{
                    Increaseitem.setImageResource(R.mipmap.ic_arrow_upward_black_24dp);
                }

            }
        });

    }


    @Override
    protected void onStart() {

        super.onStart();


        final FirebaseRecyclerAdapter<item, ItemViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<item, ItemViewHolder>(

                item.class,
                R.layout.itemlist,
                ItemViewHolder.class,
                qType
        ) {
            @Override
            protected void populateViewHolder(ItemViewHolder viewHolder, item model, int position) {

                viewHolder.setName(model.getName());
                viewHolder.setVolumn(model.getUnit());
                viewHolder.setImage(getApplicationContext(), model.getImage());
            }
        };

        mList.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), mList ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public boolean onItemClick(View view, int position) {

                        final DatabaseReference s = firebaseRecyclerAdapter.getRef(position);

                        if(decreaseitem == true) {

                            s.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    item item = dataSnapshot.getValue(item.class);
                                    int i = Integer.parseInt(item.getUnit());
                                    i = i - 1;
                                    s.child("Unit").setValue(Integer.toString(i));

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }else  if (additem == true){

                            s.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    item item = dataSnapshot.getValue(item.class);
                                    int i = Integer.parseInt(item.getUnit());
                                    i = i + 1;
                                    s.child("Unit").setValue(Integer.toString(i));

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }else if (removeitem == true){
                            AlertDialog.Builder builder = new AlertDialog.Builder(Tab5.this);
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

                            Intent i = new Intent(Tab5.this, ShowInformationItem.class);
                            i.putExtra("key",s.getKey());

                            startActivity(i);

                        }
                        return true;

                    }
                    @Override public void onLongItemClick(View view, int position) {


                    }


                })


        );

        mList.setAdapter(firebaseRecyclerAdapter);

    }
    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            mView = itemView ;
        }
        public void setName(String Name){
            TextView name = (TextView) mView.findViewById(R.id.nameItem);
            name.setText(Name);
        }
        public void setVolumn(String Volumn){
            TextView volumn = (TextView) mView.findViewById(R.id.volumnItem);
            volumn.setText(Volumn);
        }
        public void setImage(Context ctx , String image){
            ImageView imageView = (ImageView) mView.findViewById(R.id.imageItem);
            Picasso.with(ctx).load(image).fit().placeholder(R.mipmap.ic_launcher).into(imageView);

        }


    }

}
