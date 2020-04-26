package com.sss.foody.box;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

public class Messagefragment extends Fragment {
    private Toast backToast;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView mRecyclerView;
    private FirebaseAuth mAuth;
    private shop_Profile shop;
    private DatabaseReference myRef;
    String imageUrl;
    private static final String TAG = "Message Fragment";
    private  View mContentView;
    int[] sample_images = {R.drawable.super_foodie_plan,R.drawable.morattufoodieplan,R.drawable.protein_foodie_plan,R.drawable.pullingo_foodie_plan};
    List<String> arrayList;
    private ProgressBar progressBar;
    private  FirebaseRecyclerAdapter<shop_Profile,RequestsViewHolder > adapter;
    private FirebaseRecyclerOptions<shop_Profile> options;
    CarouselView    carouselView;
    //  private List<shop_Profile> shoplist = new ArrayList<shop_Profile>();
    List<cardData> myShopList = new ArrayList<>();
    cardData mCardData;
    //private MyAdapter myAdapter;
    private  ImageView imgview;
    private ShopFragment wvf;
    private  ImageView imgviewRegister;
    String currentURL;

    public void init(String url) {
        currentURL = url;
    }

    ListView mListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_message, container, false);
        swipeRefreshLayout = v.findViewById(R.id.messageSwipeRefresh);
        //Database work from firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance ();
        myRef = firebaseDatabase.getReference();
        myRef.keepSynced(true);
        final String userKey = mAuth.getUid();
        progressBar = (ProgressBar) v.findViewById(R.id.progressbarhelp);

        Log.v(TAG,"Userkey: "+ userKey);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        progressBar.setVisibility(View.VISIBLE);
        options = new FirebaseRecyclerOptions.Builder<shop_Profile>()
                .setQuery( myRef.child("customer").child("seller"),shop_Profile.class).build();
        adapter = new FirebaseRecyclerAdapter<shop_Profile, RequestsViewHolder>(options) {

            @NonNull
            @Override
            public RequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View mView = LayoutInflater.from(getContext()).inflate(R.layout.recycler_row_item, parent,
                        false);
                return new RequestsViewHolder(mView);
            }

            @Override
            protected void onBindViewHolder(@NonNull RequestsViewHolder holder, int position, @NonNull final shop_Profile model) {
                Log.v(TAG,"Userkey: "+ model.getShopimageurl());

                Picasso.with(getContext())
                        .load(model.getShopimageurl())
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .error(R.drawable.foodyboxkarapakkam)
                        .into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wvf = new ShopFragment();
                        wvf.init(model.getShoplink());
                        FragmentManager fm =  getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.fragment_container, wvf,"SHOPPAGEFRAGMENT").addToBackStack("ShopFragment");
                        ft.commit();
                        //  .getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        //          new HomewebpageFragment(),"HOMEWEBPAGEFRAGMENT").addToBackStack("HomeFragment").commit();

                    }
                });
                progressBar.setVisibility(View.GONE);

            }

        };
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),1,GridLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        adapter.startListening();
        mRecyclerView.setAdapter(adapter);



        final Appstatus AppStatus = new Appstatus();
        carouselView = (CarouselView) v.findViewById(R.id.carouselView);
        carouselView.setPageCount(sample_images.length);
        carouselView.setImageListener(imageListener);
        carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SubscriptionFragment(),"SUBSCRIPTIONFRAGMENT").addToBackStack("subscription fragment").commit();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressBar.setVisibility(View.VISIBLE);
                myShopList.clear();
                adapter.notifyDataSetChanged();
                myShopList.add(mCardData);
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
            }
        });


        imgview = (ImageView) v.findViewById(R.id.foodyboxImage);
        imgview.setClickable(true);

        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomewebpageFragment(),"HOMEWEBPAGEFRAGMENT").addToBackStack("HomeFragment").commit();
            }
        });

        imgviewRegister = (ImageView) v.findViewById(R.id.foodyboxRegister);
        imgviewRegister.setClickable(true);
        imgviewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SignupFragment(),"SIGNUPFRAGMENT").addToBackStack("signupfragment").commit();

            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter!= null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.startListening();
    }


    public static class RequestsViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        // TextView mTitle, mDescription;
        CardView mCardView;

        public RequestsViewHolder(@NonNull View itemView)
        {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.shopimage);
            mCardView = itemView.findViewById(R.id.myCardView);

        }
    }
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sample_images[position]);
        }
    };

}