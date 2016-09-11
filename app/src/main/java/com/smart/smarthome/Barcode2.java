package com.smart.smarthome;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Barcode2 extends AppCompatActivity {

    static final int GET_BAR_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode2);

        Intent intent = new Intent(Barcode2.this, BarcodeCaptureActivity.class);
        startActivityForResult(intent, GET_BAR_CODE);

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

                        if(dataSnapshot.getChildrenCount() == 0){
                            Toast.makeText(Barcode2.this, "Barcode not Found", Toast.LENGTH_LONG).show();
                            finish();
                        }else {
                            for (final com.google.firebase.database.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                final String key2 = postSnapshot.getKey();


                                AlertDialog.Builder builder = new AlertDialog.Builder(Barcode2.this);
                                builder.setTitle("OK ?");
                                builder.setMessage(barcodeValue2);
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(Barcode2.this, ShowBarcode.class);
                                        i.putExtra("key", postSnapshot.getKey());
                                        finish();
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
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




            } else {
                Toast.makeText(Barcode2.this, "Barcode not Found", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
