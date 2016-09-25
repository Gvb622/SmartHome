package com.smart.smarthome;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;

public class ShoppinglistActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageButton AdditemShoppingList;
    private ImageButton IncreaseitemShoppingList;
    private ImageButton RemoveitemShoppingList;

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;

    private RecyclerView mList;
    private String m_Text;

    public boolean remove = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppinglist);
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

        AdditemShoppingList = (ImageButton) findViewById(R.id.AdditemShoppingList);
        IncreaseitemShoppingList = (ImageButton) findViewById(R.id.IncreaseItemShoppingList);
        RemoveitemShoppingList   = (ImageButton) findViewById(R.id.RemoveItemShoppingList);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist");

        mList = (RecyclerView) findViewById(R.id.item_Shoppinglist);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(this, R.drawable.list_divider), true));

        IncreaseitemShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        AdditemShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove = false;
                RemoveitemShoppingList.setImageResource(R.mipmap.ic_clear_black_24dp);

                AlertDialog.Builder builder = new AlertDialog.Builder(ShoppinglistActivity.this);
                builder.setTitle("Pleses Name of List");
                final EditText input = new EditText(ShoppinglistActivity.this);
                input.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                builder.setView(input);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        m_Text = input.getText().toString().trim();
                        DatabaseReference shopList = mDatabase.push();
                        shopList.child("Name").setValue(m_Text);
                        shopList.child("Price").setValue("0");
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

        RemoveitemShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean b = !remove ;
                remove = b;

                if(b == true){
                    RemoveitemShoppingList.setImageResource(R.mipmap.ic_clear_white_24dp);
                } else{
                    RemoveitemShoppingList.setImageResource(R.mipmap.ic_clear_black_24dp);
            }
            }
        });


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
    protected void onStart() {
        super.onStart();

        final FirebaseRecyclerAdapter<ShopList, ItemViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ShopList, ItemViewHolder>(

                ShopList.class,
                R.layout.shoplist,
                ItemViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(ItemViewHolder viewHolder, ShopList model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setPrice(model.getPrice());
            }
        };

        mList.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), mList, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position) {

                        final DatabaseReference s = firebaseRecyclerAdapter.getRef(position);

                        if(remove == true){
                            AlertDialog.Builder builder = new AlertDialog.Builder(ShoppinglistActivity.this);
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
                            Intent i = new Intent(ShoppinglistActivity.this, ShoppinglistShowlistActivity.class);
                            i.putExtra("key", s.getKey());
                            startActivity(i);
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

            mView = itemView ;
        }
        public void setName(String name){
            TextView Name = (TextView) mView.findViewById(R.id.nameItemShoppinglist);
            Name.setText(name);
        }
        public void setPrice(String price){
            TextView Price = (TextView) mView.findViewById(R.id.priceItemShoppintlist);
            Price.setText(price);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shoppinglist, menu);
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
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inventory) {
            // Handle the camera action
            Intent bar = new Intent(ShoppinglistActivity.this,MainActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_shoppinglist) {
            Intent bar = new Intent(ShoppinglistActivity.this,ShoppinglistActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_calculator) {
            Intent bar = new Intent(ShoppinglistActivity.this,CalculatorActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_locationbase) {
            Intent bar = new Intent(ShoppinglistActivity.this,LocationbaseActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_summary) {
            Intent bar = new Intent(ShoppinglistActivity.this,SummaryreportActivity.class);
            startActivity(bar);
        } else if (id == R.id.nav_setting) {
            Intent bar = new Intent(ShoppinglistActivity.this,SettingActivity.class);
            startActivity(bar);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
