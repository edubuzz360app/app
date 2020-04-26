package com.sss.foody.box;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,BottomNavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private long backPressedTime;
    private FirebaseAuth mAuth;


    Fragment fragment = null;


    private Toast backToast;
    //int[] sample_images = {R.drawable.morattu_foodie_plan };

    @Override
    public void onBackPressed() {
        if (backPressedTime + 1200 > System.currentTimeMillis()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else {
                backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
                backToast.show();
            }
        }

        backPressedTime = System.currentTimeMillis();
    }
    private void showHome(){
        fragment = new Messagefragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragment).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        mDrawerLayout = (DrawerLayout) findViewById ( R.id.sidebar);
        NavigationView navigationView = findViewById ( R.id.nav_view );
        BottomNavigationView mbottomNavigationView = findViewById(R.id.bottom_navigation);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SubscriptionFragment(),"SUBSCRIPTIONFRAGMENT").addToBackStack("Subscription Fragment").commit();
            //    Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
             //           .setAction("Action", null).show();
            }
        });
        navigationView.setNavigationItemSelectedListener ( this );

        mbottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

       if (appStatus.isOnline(MainActivity.this)) {
            showHome();
        }else {
           buildDialog(MainActivity.this).show();
        }
        mToggle = new ActionBarDrawerToggle (this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener ( mToggle );
        mToggle.syncState ();
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );

    }



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_left:
                    String contact = "917010288829"; // use country code with your phone number
                    String url = "https://api.whatsapp.com/send?phone=" + contact+"&text=&source=&data=";
                    try {
                        PackageManager pm = MainActivity.this.getPackageManager();
                        pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    } catch (PackageManager.NameNotFoundException e) {
                        Toast.makeText(MainActivity.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    break;

                case R.id.action_right:
                    Uri uri = Uri.parse("https://instagram.com/_u/foodyboxchennai/");
                    Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                    likeIng.setPackage("com.instagram.android");

                    try {
                        startActivity(likeIng);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://instagram.com/foodyboxchennai/")));
                    }
                    break;
                case R.id.trackorder:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new LocationFragment(),"LOCATIONFRAGMENT").addToBackStack("locationfragment").commit();
                    break;
                case R.id.profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new MyAccountFragment(),"MYACCOUNTFRAGMENT").addToBackStack("myaccountfrag").commit();
                    break;
            }
            return false;
        }
    };

    //share button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate ( R.menu.toolbar,menu);
        return super.onCreateOptionsMenu ( menu );
    }

    //SideBar Open-Close Function
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (mToggle.onOptionsItemSelected ( item )) {
            return true ;
        }
        switch (item.getItemId ()){
            case R.id.share:
                Intent sharingIntent = new Intent (Intent.ACTION_SEND);
                sharingIntent.setType ( "text/plain" );
                String shareBody = "Hey Foodie... I care your Health, So i am sharing you about Foody Box app at https://www.foodybox.in Download at https://play.google.com/store/apps/details?id=com.sss.foody.box";
                String shareSubject = "Foody Box";
                sharingIntent.putExtra ( Intent.EXTRA_TEXT,shareBody );
                sharingIntent.putExtra ( Intent.EXTRA_SUBJECT,shareSubject );
                startActivity ( Intent.createChooser ( sharingIntent, "Share Using" ) );
                break;

            case R.id.cart:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CartFragment (),"CARTFRAGMENT").addToBackStack("cart").commit();
                break;


        }
        return super.onOptionsItemSelected ( item );
    }
    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("Switch on Internet");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    MainActivity.this.recreate();

            }
        });

        return builder;
    }
    Appstatus appStatus = new Appstatus();
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment f = this.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            switch (menuItem.getItemId()) {
                case R.id.nav_message:
                    if (!(f instanceof Messagefragment)) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new Messagefragment(),"MESSAGEFRAGMENT").addToBackStack("MessageFragment").commit();
                  }
                    break;

                case R.id.nav_chat:
                    if (!(f instanceof Chatfragment)) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new Chatfragment(),"CHATFRAGMENT").addToBackStack("chatfrag").commit();
                    }
                    break;

                case R.id.deliver_address:
                    if (!(f instanceof DeliveryFragment)) {

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new DeliveryFragment(),"DELIVERYFRAGMENT").addToBackStack("deliverfrag").commit();
                    }
                    break;

                case R.id.cart_side:
                    if (!(f instanceof CartFragment)) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new CartFragment(),"CARTFRAGMENT").addToBackStack("cartfrag").commit();
                    }
                    break;
                case R.id.sign_up:
                    if (!(f instanceof SignupFragment)) {

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new SignupFragment(),"SIGNUPFRAGMENT").addToBackStack("signupfrag").commit();
                    }
                    break;

                case R.id.nav_profile:
                    if (!(f instanceof MyAccountFragment)) {

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new MyAccountFragment(),"MYACCOUNTFRAGMENT").addToBackStack("myaccountfrag").commit();
                    }
                    break;
                case R.id.coupons_side:
                    if (!(f instanceof couponsFragment)) {

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new couponsFragment(),"COUPONSFRAGMENT").addToBackStack("couponfragment").commit();
                    }
                    break;
                case R.id.privacy_side:
                    if (!(f instanceof PrivacyFragment)) {

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new PrivacyFragment(),"PRIVACYFRAGMENT").addToBackStack("privacyfragment").commit();
                    }
                    break;
                case R.id.location_Status:
                    if (!(f instanceof LocationFragment)) {

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new LocationFragment(),"LOCATIONFRAGMENT").addToBackStack("locationfragment").commit();
                    }
                    break;
                case R.id.help_side:
                  String contact = "917010288829"; // use country code with your phone number
                    String url = "https://api.whatsapp.com/send?phone=" + contact+"&text=&source=&data=";
                    try {
                        PackageManager pm = this.getPackageManager();
                        pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    } catch (PackageManager.NameNotFoundException e) {
                        Toast.makeText(MainActivity.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    break;
                case R.id.nav_share:
                    Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                    Intent appshare = new Intent(Intent.ACTION_SEND);
                    appshare.setType("text/plain");
                    String shareBody = "Hey Foodie... I care your Health, So i am sharing you about Foody Box app at https://www.foodybox.in    Download at https://play.google.com/store/apps/details?id=com.sss.foody.box";
                    String shareSubject = "Foody Box";
                    appshare.putExtra(Intent.EXTRA_TEXT, shareBody);
                    appshare.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
                    startActivity(Intent.createChooser(appshare, "Share Using"));
                    break;


            }
            mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;

    }

}