package com.sss.foody.box;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class LocationFragment extends Fragment {

    WebView webView;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    List<String> convertedlocations;
    List<String> vendorlist = new ArrayList<>();
    List alldelivery = new ArrayList<String>();
    List<String> foodReadylocations = new ArrayList<String >();
    private Spinner spinnervendors;
    private View orderplaced,orderprocessed;
    private static final String TAG = "LocationFragment";
    private Map<String, List> listofDeliverypoints= new HashMap<String, List>();
    private Map<String,List> currentdeliverylocationstatus = new HashMap<String, List>();
    LinearLayout mparent;
    LinearLayout.LayoutParams params;
    private DatabaseReference myRef;
    private Calendar c;
    private Map<String, String > currentLocationStatus;
    private GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};
    private SimpleDateFormat dateformat,dateandtimeformat;
    private String datetoday,locations;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_location, container, false);
        mparent = (LinearLayout)v.findViewById(R.id.container);
        orderplaced = inflater.inflate(R.layout.fragment_orderplaced, null);


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance ();
        myRef = firebaseDatabase.getReference();
        spinnervendors = v.findViewById(R.id.spinnervendors);
        c = Calendar.getInstance();
        dateformat = new SimpleDateFormat("MM-dd-yyyy");
        dateandtimeformat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        datetoday = dateformat.format(c.getTime());
        final String userKey = mAuth.getUid();
        myRef.child("deliverypoints");
        progressBar = (ProgressBar) v.findViewById(R.id.progressbarhelp);


        spinnervendors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] myArray = new String[vendorlist.size()];
                vendorlist.toArray(myArray);
                String currentvendor = myArray[spinnervendors.getSelectedItemPosition ()];
                Log.v(TAG, "key = " + currentvendor + foodReadylocations);
                mparent.removeAllViews();
                View iv_background = orderplaced.findViewById(R.id.viewOrderConfirmed);
                if (foodReadylocations.contains(currentvendor)) {
                    iv_background.setBackgroundResource(R.drawable.shape_status_completed);
                } else {
                    iv_background.setBackgroundResource(R.drawable.shape_status_current);
                }
                mparent.addView(orderplaced);

                if (currentdeliverylocationstatus!= null && (currentdeliverylocationstatus.get(currentvendor) != null)) {
                    if (currentdeliverylocationstatus.get(currentvendor) != null) {
                        List<String> internal = currentdeliverylocationstatus.get(currentvendor);
                        for (int i = 0; (internal != null) && i < internal.size(); i++) {
                            orderprocessed = inflater.inflate(R.layout.order_processed, null);
                            orderprocessed.setPadding(10,100,0,0);
                            View processedbackground = orderprocessed.findViewById(R.id.viewOrderProcessed);
                            TextView textviews = (TextView) orderprocessed.findViewById(R.id.textOrderDelivered);
                            Log.v(TAG, "key = " + internal.get(i));
                            processedbackground.setBackgroundResource(R.drawable.shape_status_completed);
                            textviews.setText( internal.get(i));
                            mparent.addView(orderprocessed);
                        }
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        myRef.child("customer").child("vendors").addListenerForSingleValueEvent(new ValueEventListener() {
            @NonNull
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();
                Log.v(TAG, "Key = " + dataSnapshot.getChildrenCount() );
                HashMap<String,Object> user = null;
                while (items.hasNext()) {
                    DataSnapshot item = items.next();
                    user = (HashMap<String, Object>) item.getValue();
                    vendorlist.add(item.getKey());

                    listofDeliverypoints.put(item.getKey(),(List<String>)user.get("deliverypoints"));
                }
                Log.v(TAG, "key = " + listofDeliverypoints );
                spinnervendors.setAdapter ( new ArrayAdapter<String>( getActivity(), android.R.layout.simple_spinner_dropdown_item, vendorlist));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });


        myRef.child("LocationStatus").child("FoodReady").addValueEventListener(new ValueEventListener() {
            @NonNull
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> items = dataSnapshot.child(datetoday).getChildren().iterator();
                while (items.hasNext()) {
                    DataSnapshot item = items.next();
                    foodReadylocations.add(item.getKey());
                }
                Log.v(TAG, "listofDeliverypoints = " + foodReadylocations );
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

        myRef.child("LocationStatus").child("Delivery").addValueEventListener(new ValueEventListener() {
            @NonNull
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> items = dataSnapshot.child(datetoday).getChildren().iterator();
                Log.v(TAG, "Key = " + dataSnapshot.getChildrenCount() );
                HashMap<String,Object> user = null;
                HashMap<String,Object> insideuser = null;
                Iterator<DataSnapshot> itemlocals;
                while (items.hasNext()) {
                    DataSnapshot item = items.next();
                    itemlocals = item.getChildren().iterator();
                    //delivery = currentdeliverylocationstatus.get(item.getKey());
                    List <String> delivery = new ArrayList<String>();
                    while(itemlocals.hasNext()) {
                        DataSnapshot itemlocal = itemlocals.next();
                        if ((!alldelivery.contains(itemlocal.getKey()))) {
                            Log.v(TAG, "delivery = " +  itemlocal);
                            Log.v(TAG, "delivery = " + itemlocal.getKey() );
                            delivery.add(itemlocal.getKey());
                        }
                    }
                    currentdeliverylocationstatus.put(item.getKey(),delivery);
                    Log.v(TAG, "delivery = " + currentdeliverylocationstatus + delivery );
                }
                Log.v(TAG, "delivery = " + currentdeliverylocationstatus );

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText ( getActivity().getApplicationContext (),"Problem while fetching Delivery location, Please Try again",Toast.LENGTH_LONG).show ();

            }

        });
        return v;
    }


}