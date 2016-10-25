package com.smart.smarthome;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.formats.NativeAd;
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

public class Tab11 extends AppCompatActivity {

    private String value;
    private RecyclerView mList;
    private Query qType;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private Button finishButton;
    private String m_Text;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab7);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();


        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("items");
        qType = mDatabase.child("Food and Ingredients").orderByChild("Name");

        mList = (RecyclerView) findViewById(R.id.item_listShopping2);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(this, R.drawable.list_divider), true));

        finishButton = (Button)findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Toast.makeText(Tab11.this, "     Click on item and add Volume" + " \n " + "After Add all item click finish button" , Toast.LENGTH_LONG).show();




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
                }catch (Exception e){

                }

            }
        };

        mList.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), mList, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position) {
                        final DatabaseReference s = firebaseRecyclerAdapter.getRef(position);

                        AlertDialog.Builder builder = new AlertDialog.Builder(Tab11.this);
                        builder.setTitle("How many quantity ?");
                        final EditText input = new EditText(Tab11.this);
                        input.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
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

                                        DatabaseReference TotalPrice = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist")
                                                .child("all");
                                        DatabaseReference Total = TotalPrice.push();
                                        Total.child("ItemPrice").setValue(item.getRetailPrice());
                                        Total.child("ItemTopsPrice").setValue(item.getSalePriceTops());
                                        Total.child("ItemLotusPrice").setValue(item.getSalePriceLotus());
                                        if(m_Text.equals("")){
                                            m_Text = "0";
                                        }
                                        Total.child("ItemVolumn").setValue(m_Text);


                                        DatabaseReference Shoplist2 = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("shoppinglist")
                                                .child(Type);
                                        DatabaseReference Shoplist = Shoplist2.push();
                                        Shoplist.child("ItemImage").setValue(item.getImage());
                                        Shoplist.child("ItemName").setValue(item.getName());
                                        Shoplist.child("ItemPrice").setValue(item.getRetailPrice());
                                        Shoplist.child("Key").setValue(s.getKey());
                                        Shoplist.child("KeyAll").setValue(Total.getKey());
                                        Shoplist.child("ItemClassifier").setValue(item.getClassifier());
                                        Shoplist.child("ItemVolumn").setValue(m_Text);
                                        Shoplist.child("ItemTopsPrice").setValue(item.getSalePriceTops());
                                        Shoplist.child("ItemLotusPrice").setValue(item.getSalePriceLotus());




                                        Toast.makeText(Tab11.this, item.getName() + " already add to shoppinglist", Toast.LENGTH_LONG).show();

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
    }
}
