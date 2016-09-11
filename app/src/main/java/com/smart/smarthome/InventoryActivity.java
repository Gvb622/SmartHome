package com.smart.smarthome;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

public class InventoryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mList;
    private ImageButton Additem;
    private ImageButton Increaseitem;
    private ImageButton Decreaseitem;
    private ImageButton Removeitem;


    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private ImageView imageView;
    private item i2;
    private boolean additem ;
    private boolean decreaseitem;
    private boolean removeitem;
    private boolean ItemCLick ;
    static final int GET_BAR_CODE = 1;
    public String key;
    public boolean finish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Additem = (ImageButton) findViewById(R.id.Additem);
        Increaseitem = (ImageButton) findViewById(R.id.IncreaseItem);
        Decreaseitem = (ImageButton) findViewById(R.id.DecreaseItem);
        Removeitem = (ImageButton) findViewById(R.id.RemoveItem);

        imageView = (ImageView) findViewById(R.id.imageItem);
        firebaseAuth = FirebaseAuth.getInstance();
        additem = false;
        decreaseitem = false;
        removeitem = false;
        ItemCLick = false;
        finish = false;

        FirebaseUser user = firebaseAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("items");

        mList = (RecyclerView) findViewById(R.id.item_list);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(InventoryActivity.this);
                builder.setTitle("Choose One");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){

                            Intent intent = new Intent(InventoryActivity.this, BarcodeCaptureActivity.class);
                            startActivityForResult(intent, GET_BAR_CODE);

                        }else if(which == 1){
                            startActivity(new Intent(InventoryActivity.this, AddItemActivity.class));
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
                mDatabase
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(InventoryActivity.this);
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

                            Intent i = new Intent(InventoryActivity.this, ShowInformationItem.class);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inventory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inventory) {
            // Handle the camera action
            Intent bar = new Intent(InventoryActivity.this,InventoryActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_shoppinglist) {
            Intent bar = new Intent(InventoryActivity.this,ShoppinglistActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_calculator) {
            Intent bar = new Intent(InventoryActivity.this,CalculatorActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_locationbase) {
            Intent bar = new Intent(InventoryActivity.this,LocationbaseActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_summary) {
            Intent bar = new Intent(InventoryActivity.this,SummaryreportActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_setting) {
            Intent bar = new Intent(InventoryActivity.this,SettingActivity.class);
            startActivity(bar);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_BAR_CODE) {
            if (resultCode == RESULT_OK) {
                final String barcodeValue2 = data.getStringExtra("Barcode");
                DatabaseReference mDatabse = FirebaseDatabase.getInstance().getReference().child("system").child("items");
                System.out.println(barcodeValue2);
                Query queryRef = mDatabse.orderByChild("Barcode").equalTo(barcodeValue2);
                queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        System.out.println(barcodeValue2);
                        for (final com.google.firebase.database.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            final String key2 =postSnapshot.getKey();


                            AlertDialog.Builder builder = new AlertDialog.Builder(InventoryActivity.this);
                            builder.setTitle("OK ?");
                            builder.setMessage(barcodeValue2);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(InventoryActivity.this, ShowBarcode.class);
                                    i.putExtra("key", postSnapshot.getKey());
                                    startActivity(i);
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
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




            } else {
                Toast.makeText(InventoryActivity.this, "Barcode not Found", Toast.LENGTH_LONG).show();
            }
        }
    }
}
