package com.sss.foody.box;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class LoadingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,BottomNavigationView.OnNavigationItemSelectedListener  {

    private DrawerLayout mDrawerLayout;
    Fragment fragment = null;

    private ActionBarDrawerToggle mToggle;

    private void showHome(){
        fragment = new Messagefragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.loading_Container,
                fragment).commit();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

       // View contentView = inflater.inflate(R.layout.navigation_view, activity_main, false);
      //  mDrawer.addView(contentView, 0);

        mDrawerLayout = (DrawerLayout) findViewById ( R.id.sidebar_loading);
        NavigationView navigationView = findViewById ( R.id.nav_loading_view );
        BottomNavigationView mbottomNavigationView = findViewById(R.id.bottom_navigation);

        navigationView.setNavigationItemSelectedListener ( this );
        mbottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

       // if (appStatus.isOnline(MainActivity.this)) {
        showHome();
       // }else {
        //    buildDialog(MainActivity.this).show();
        //}
        mToggle = new ActionBarDrawerToggle (this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener ( mToggle );
        mToggle.syncState ();
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );

    }
    Appstatus appStatus = new Appstatus();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_left:
                    getSupportFragmentManager().beginTransaction().replace(R.id.loading_Container,
                            new Chatfragment()).addToBackStack("chatfrag").commit();
                    break;

                case R.id.action_right:
                    getSupportFragmentManager().beginTransaction().replace(R.id.loading_Container,
                            new Chatfragment()).addToBackStack("chatfrag").commit();
                    break;
            }
            return false;
        }
    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment f = this.getSupportFragmentManager().findFragmentById(R.id.loading_Container);
        if (appStatus.isOnline(this)) {
            switch (menuItem.getItemId()) {
                case R.id.nav_message:
                    if (!(f instanceof Messagefragment)) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.loading_Container,
                                new Messagefragment()).addToBackStack("msgfrag").commit();
                    }
                    break;

                case R.id.nav_chat:
                    if (!(f instanceof Chatfragment)) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.loading_Container,
                                new Chatfragment()).addToBackStack("chatfrag").commit();
                    }
                    break;

                case R.id.deliver_address:
                    if (!(f instanceof DeliveryFragment)) {

                        getSupportFragmentManager().beginTransaction().replace(R.id.loading_Container,
                                new DeliveryFragment()).addToBackStack("deliverfrag").commit();
                    }
                    break;

                case R.id.cart_side:
                    if (!(f instanceof CartFragment)) {

                        getSupportFragmentManager().beginTransaction().replace(R.id.loading_Container,
                                new CartFragment()).addToBackStack("cartfrag").commit();
                    }
                    break;
                case R.id.sign_up:
                    if (!(f instanceof SignupFragment)) {

                        getSupportFragmentManager().beginTransaction().replace(R.id.loading_Container,
                                new SignupFragment()).addToBackStack("signupfrag").commit();
                    }
                    break;

                case R.id.nav_profile:
                    if (!(f instanceof MyAccountFragment)) {

                        getSupportFragmentManager().beginTransaction().replace(R.id.loading_Container,
                                new MyAccountFragment()).addToBackStack("myaccountfrag").commit();
                    }
                    break;
                case R.id.coupons_side:
                    if (!(f instanceof couponsFragment)) {

                        getSupportFragmentManager().beginTransaction().replace(R.id.loading_Container,
                                new couponsFragment()).addToBackStack("termsfrag").commit();
                    }
                    break;
                case R.id.privacy_side:
                    if (!(f instanceof PrivacyFragment)) {

                        getSupportFragmentManager().beginTransaction().replace(R.id.loading_Container,
                                new PrivacyFragment()).addToBackStack("privacyfrag").commit();
                    }
                    break;
                case R.id.help_side:
//                    if (!(f instanceof HelpFragment)) {

                    //                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    //                          new HelpFragment()).addToBackStack("helpfrag").commit();
                    //                  }
 /*                   String contact = "918122216892"; // use country code with your phone number
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
                    }*/

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

        } else {
            //   buildDialog(MainActivity.this).show();
            //   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
            //      new Nointernetfragment()).addToBackStack("internetfrag").commit();
        }

        return false;

    }
}
