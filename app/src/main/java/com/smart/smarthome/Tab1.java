package com.smart.smarthome;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.graphics.Color;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.squareup.picasso.Picasso;


public class Tab1 extends AppCompatActivity {

    private RecyclerView mList;
    private ImageButton Additem;
    private ImageButton Increaseitem;
    private ImageButton Decreaseitem;
    private ImageButton Removeitem;
    private Query qType;

    private ImageView imageView;


    private boolean additem ;
    private boolean decreaseitem;
    private boolean removeitem;

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;


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
        qType = mDatabase.orderByChild("Type").equalTo("Box");

        mList = (RecyclerView) findViewById(R.id.item_list2);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(this, R.drawable.list_divider), true));

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
