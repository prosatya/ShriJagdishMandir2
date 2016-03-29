package com.matictechnology.shrijagdishmandir.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.matictechnology.shrijagdishmandir.R;
import com.matictechnology.shrijagdishmandir.Utility.AnimatorUtils;
import com.matictechnology.shrijagdishmandir.Utility.ClipRevealFrame;
import com.matictechnology.shrijagdishmandir.Utility.DbHelper;
import com.matictechnology.shrijagdishmandir.Utility.MainCardFragment;
import com.ogaclejapan.arclayout.ArcLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener
{
    int pause_flag=-1;  //for fab hide and show on pause and resume of activity
    View rootLayout;    //layout to show fab on
    ClipRevealFrame menuLayout;     //hidden full fab menu
    ArcLayout arcLayout;    //  fab menu buttons
    View centerItem;        //fab center button
    int x;  //x coordinate on the layout to show and hide fab menu buttons
    int y;  //y coordinate on the layout to show and hide fab menu buttons
    float radiusOfFab;  //radius of fab
    float radiusFromFabToRoot;  //radius for hiding and showing the fab full menu
    private static final long delay = 2000L;    //delay for double press exit
    private boolean mRecentlyBackPressed = false;   //flag for double click exit
    private Handler mExitHandler = new Handler();   //handler class object to check for the delay between double exit button pressed
    private PagerSlidingTabStrip tabs;  //tab strip for the pager slider
    private ViewPager pager;    //pager object for switching fragments in
    private MyPagerAdapter adapter; //pager adapter to help changing the fragments on pager
    int center_item_flag=-1;    //flag variable to check for two buttons on center fab full menu button
    Button center_item; //fab full menu center button
    String ci;  //string for the center button of fab full menu
    DbHelper dbhelper;  //Database helper class for storing and accessing the data
    SQLiteDatabase db;  //SQLiteDatabase object to gain read or write access to the DB

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //inflating the xml in the activity

        rootLayout = findViewById(R.id.rootlayout);
        menuLayout = (ClipRevealFrame) findViewById(R.id.menu_layout);
        arcLayout = (ArcLayout) findViewById(R.id.arc_layout);
        centerItem = findViewById(R.id.center_item);
        center_item= (Button)findViewById(R.id.center_item);
        //initializing all the required elements from the xml to use them in the java class

        centerItem.setOnClickListener(this);
        //on click listener for fab full menu center button

        for (int i = 0, size = arcLayout.getChildCount(); i < size; i++)
            arcLayout.getChildAt(i).setOnClickListener(this);
        //setting onclick listener for all the fab full menu buttons other then center button

        findViewById(R.id.fab).setOnClickListener(this);
        //onclick listener for fab

        DbHelper dbhelper = new DbHelper(ActivityMain.this);
        //initialising dh helper class
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        //getting write acces to the database

        //checking for user registration
        if(dbhelper.Login(db))
        {
            //if done show contact us fab center button
            center_item_flag=1;
            ci="Contact Us";
            center_item.setText(ci);
            Log.e("login check","logged in");
            //catlog message to show the current status of user login
        }
        else
        {
            //if not done show register fab center button
            center_item_flag=0;
            ci="Register";
            center_item.setText(ci);
            Log.e("login check", "not logged in");
            //catlog message to show the current status of user login
        }

        //setting alarm so as to get notification at 6pm
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Intent intent1 = new Intent(ActivityMain.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ActivityMain.this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) ActivityMain.this.getSystemService(ActivityMain.this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        //setting alarm so as to get notification at 6pm
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.HOUR_OF_DAY, 6);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        Intent intent11 = new Intent(ActivityMain.this, AlarmReceiver.class);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(ActivityMain.this, 0,intent11, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am1 = (AlarmManager) ActivityMain.this.getSystemService(ActivityMain.this.ALARM_SERVICE);
        am1.setRepeating(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent1);

        //setting toolbar and it's title to blank to show hindi centered label
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        setSupportActionBar(toolbar);


        //initializing drawer layout to show navigation drawer in it
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //initializing anvigation view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //initialising tabs, pager and apadter for the fragments
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);
        //setting adapter to the pager

        //setting margin to the pager in the activity
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        //setting different attributes to the tabs
        tabs.setViewPager(pager);
        tabs.setDividerColorResource(R.color.colorAccent);
        tabs.setTextColorResource(R.color.colorAccent);
        tabs.setIndicatorColorResource(R.color.colorAccent);
    }

    @Override
    public void onClick(View v)
    {
        //setting on click operation for all the click buttons

        if (v.getId() == R.id.fab)
        {
            //fab button
            onFabClick(v);
            return;
        }

        if (v.getId() == R.id.ujjain)
        {
            //starting ujjain activity
            Intent in=new Intent(ActivityMain.this, ActivityUjjain.class);
            startActivity(in);

        }
        if (v.getId() == R.id.haridwar)
        {
            //starting haridwar activity
            Intent in=new Intent(ActivityMain.this,ActivityHaridwar.class);
            startActivity(in);

        }
        if (v.getId() == R.id.bhopal)
        {
            //starting bhopal activity
            Intent in=new Intent(ActivityMain.this, ActivityBhopal.class);
            startActivity(in);


        }
        if (v.getId() == R.id.awlighat)
        {
            //starting awlighat activity
            Intent in=new Intent(ActivityMain.this,ActivityAvlighat.class);
            startActivity(in);

        }
        if (v.getId() == R.id.omkareshwar)
        {
            //starting omkareshwar activity
            Intent in=new Intent(ActivityMain.this,ActivityOmkareshwar.class);
            startActivity(in);

        }
        if (v.getId() == R.id.center_item)
        {
            //checking for the current status flag
            if(center_item_flag==1)
            {
                //starting contact us
                Intent in=new Intent(ActivityMain.this,ActivityContactUs.class);
                startActivity(in);

            }
            else if(center_item_flag==0)
            {
                //starting registration page
                Intent in=new Intent(ActivityMain.this,ActivityRegister.class);
                startActivity(in);

            }

        }

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        //setting the flag for fab hide and close management
        pause_flag=1;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //checking the current activity state and hiding the open fab full menu
        if(pause_flag==1)
            hideMenu(x, y, radiusFromFabToRoot, radiusOfFab);
    }

    private void onFabClick(View v)
    {
        //controlling fab button click operation
        dbhelper = new DbHelper(ActivityMain.this);
        db = dbhelper.getWritableDatabase();
        //getting the write access for the DB

        //checking for registered status of user
        if(dbhelper.Login(db))
        {
            //setting the the fab full menu center button to contact us
            center_item_flag=1;
            ci="Contact Us";
            center_item.setText(ci);
            Log.e("login check","logged in");
        }
        else
        {
            //setting the the fab full menu center button to Register
            center_item_flag=0;
            ci="Register";
            center_item.setText(ci);
            Log.e("login check", "not logged in");
        }

        //setting x and y coordinates for fab full menu
        x = (v.getLeft() + v.getRight()) / 2;
        y = (v.getTop() + v.getBottom()) / 2;
        radiusOfFab = 1f * v.getWidth() / 2f;
        radiusFromFabToRoot = (float) Math.hypot(
                Math.max(x, rootLayout.getWidth() - x),
                Math.max(y, rootLayout.getHeight() - y));

        //checking for the select operation and hiding and showing the menu accordingly
        if (v.isSelected())
        {
            hideMenu(x, y, radiusFromFabToRoot, radiusOfFab);
        }
        else
        {
            showMenu(x, y, radiusOfFab, radiusFromFabToRoot);
        }
        v.setSelected(!v.isSelected());
    }

    private void showMenu(int cx, int cy, float startRadius, float endRadius)
    {
        //showing the hidden fab full menu
        menuLayout.setVisibility(View.VISIBLE);

        List<Animator> animList = new ArrayList<>();

        //creating animation for showing fab full menu
        Animator revealAnim = createCircularReveal(menuLayout, cx, cy, startRadius, endRadius);
        revealAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        revealAnim.setDuration(200);

        animList.add(revealAnim);
        animList.add(createShowItemAnimator(centerItem));

        for (int i = 0, len = arcLayout.getChildCount(); i < len; i++)
            animList.add(createShowItemAnimator(arcLayout.getChildAt(i)));

        AnimatorSet animSet = new AnimatorSet();
        animSet.playSequentially(animList);
        animSet.start();
    }

    private Animator createShowItemAnimator(View item)
    {
        //method to show animation for the fab full menu items
        float dx = centerItem.getX() - item.getX();
        float dy = centerItem.getY() - item.getY();

        item.setScaleX(0f);
        item.setScaleY(0f);
        item.setTranslationX(dx);
        item.setTranslationY(dy);

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.scaleX(0f, 1f),
                AnimatorUtils.scaleY(0f, 1f),
                AnimatorUtils.translationX(dx, 0f),
                AnimatorUtils.translationY(dy, 0f)
        );

        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(50);
        return anim;
    }

    private void hideMenu(int cx, int cy, float startRadius, float endRadius)
    {
        //hide fab full menu method
        List<Animator> animList = new ArrayList<>();

        for (int i = arcLayout.getChildCount() - 1; i >= 0; i--)
            animList.add(createHideItemAnimator(arcLayout.getChildAt(i)));

        animList.add(createHideItemAnimator(centerItem));

        Animator revealAnim = createCircularReveal(menuLayout, cx, cy, startRadius, endRadius);
        revealAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        revealAnim.setDuration(200);
        revealAnim.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                menuLayout.setVisibility(View.INVISIBLE);
            }
        });

        animList.add(revealAnim);

        AnimatorSet animSet = new AnimatorSet();
        animSet.playSequentially(animList);
        animSet.start();

    }

    private Animator createHideItemAnimator(final View item)
    {
        //creating hide fab full menu animation
        final float dx = centerItem.getX() - item.getX();
        final float dy = centerItem.getY() - item.getY();

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.scaleX(1f, 0f),
                AnimatorUtils.scaleY(1f, 0f),
                AnimatorUtils.translationX(0f, dx),
                AnimatorUtils.translationY(0f, dy)
        );

        anim.setInterpolator(new DecelerateInterpolator());
        anim.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                item.setTranslationX(0f);
                item.setTranslationY(0f);
            }
        });
        anim.setDuration(50);
        return anim;
    }

    private Animator createCircularReveal(final ClipRevealFrame view, int x, int y, float startRadius,float endRadius)
    {
        //method creating circular movement in animation of fab full menu
        final Animator reveal;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            reveal = ViewAnimationUtils.createCircularReveal(view, x, y, startRadius, endRadius);
        }
        else
        {
            view.setClipOutLines(true);
            view.setClipCenter(x, y);
            reveal = ObjectAnimator.ofFloat(view, "ClipRadius", startRadius, endRadius);
            reveal.addListener(new Animator.AnimatorListener()
            {
                @Override
                public void onAnimationStart(Animator animation)
                {

                }

                @Override
                public void onAnimationEnd(Animator animation)
                {
                    view.setClipOutLines(false);

                }

                @Override
                public void onAnimationCancel(Animator animation)
                {

                }

                @Override
                public void onAnimationRepeat(Animator animation)
                {

                }
            });
        }
        return reveal;
    }

    public class MyPagerAdapter extends FragmentPagerAdapter
    {
        //fragment pager adapter for the fragments on pager
        private final String[] TITLES =
        {
                //titles for the fragments
            "आमंत्रणपत्रं", "अन्नक्षेत्र कार्यक्रम","शाही स्नान", "संपर्कं"
        };

        public MyPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return TITLES[position];
        }

        @Override
        public int getCount()
        {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position)
        {
            return MainCardFragment.newInstance(position);
        }

    }




    @Override
    public void onBackPressed()
    {
        //handling back pressed key for drawer layout and application exit
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {

            if (mRecentlyBackPressed)
            {
                mExitHandler.removeCallbacks(mExitRunnable);
                mExitHandler = null;
                System.exit(1);
            }
            else
            {
                mRecentlyBackPressed = true;
                Toast.makeText(this, "press again to exit", Toast.LENGTH_SHORT).show();
                mExitHandler.postDelayed(mExitRunnable, delay);
            }
        }
    }

    private Runnable mExitRunnable = new Runnable()
    {
        //thread to check for double tap exit
        @Override
        public void run()
        {
            mRecentlyBackPressed=false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_contact)
        {
            //handling menu options and opening the contact us page
            Intent in=new Intent(ActivityMain.this,ActivityContactUs.class);
            startActivity(in);
            return true;
        }
        if (id == R.id.action_share)
        {
            //handling menu options and opening the share intent
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Checkout this Awesome App.... =>https://https://play.google.com/store/apps/details?id=com.matictechnology.shrijagdishmandir";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Application u must try!");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
            return true;
        }
        if (id == R.id.action_Help)
        {
            //handling menu options and opening the help page
            Intent in=new Intent(ActivityMain.this,ActivityHelp.class);
            startActivity(in);
            return true;
        }
        if (id == R.id.action_about)
        {
            //handling menu options and opening the about us page
            Intent in=new Intent(ActivityMain.this,ActivityAboutUs.class);
            startActivity(in);
            return true;
        }
        if (id == R.id.action_faqs)
        {
            //handling menu options and opening the FAQS page
            Intent in=new Intent(ActivityMain.this,ActivityFAQs.class);
            startActivity(in);
            return true;
        }
        if (id == R.id.action_terms)
        {
            //handling menu options and opening the tems and conditions page
            Intent in=new Intent(ActivityMain.this,ActivityTerms.class);
            startActivity(in);
            return true;
        }
        if (id == R.id.action_settings)
        {
            //handling menu options and opening the settings page
            Intent in=new Intent(ActivityMain.this,ActivitySettings.class);
            startActivity(in);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Ujjain)
        {
            Intent in=new Intent(ActivityMain.this,ActivityUjjain.class);
            startActivity(in);
        }
        else if (id == R.id.Omkareshwar)
        {
            Intent in=new Intent(ActivityMain.this,ActivityOmkareshwar.class);
            startActivity(in);
        }
        else if (id == R.id.Haridwar)
        {
            Intent in=new Intent(ActivityMain.this, ActivityHaridwar.class);
            startActivity(in);
        }
        else if (id == R.id.Bhopal)
        {
            Intent in=new Intent(ActivityMain.this, ActivityBhopal.class);
            startActivity(in);
        }
        else if (id == R.id.Avlighat)
        {
            Intent in=new Intent(ActivityMain.this, ActivityAvlighat.class);
            startActivity(in);
        }
        else if (id == R.id.Gallery)
        {
            Intent in=new Intent(ActivityMain.this, ActivityGallery.class);
            startActivity(in);
        }
        else if (id == R.id.Share)
        {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Checkout this Awesome App.... =>link to app..";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Application u must try!");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
        else if (id == R.id.Visit_WebSite)
        {
            String url = "http://www.jagdishmandirujjain.in/";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
        else if (id == R.id.action_contact)
        {
            Intent in=new Intent(ActivityMain.this, ActivityContactUs.class);
            startActivity(in);
        }
        else if (id == R.id.profile)
        {

            dbhelper = new DbHelper(ActivityMain.this);
            db = dbhelper.getWritableDatabase();
            if(dbhelper.Login(db))
            {
                Intent in=new Intent(ActivityMain.this, ActivityProfile.class);
                startActivity(in);
            }
            else
            {
                Toast.makeText(ActivityMain.this, "Not registered yet... Try to Register first!!", Toast.LENGTH_SHORT).show();
            }


        }
        else if (id == R.id.register)
        {
            dbhelper = new DbHelper(ActivityMain.this);
            db = dbhelper.getWritableDatabase();
            if(dbhelper.Login(db))
            {
                Toast.makeText(ActivityMain.this, "Already Registered!! Enjoy the app!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Intent in=new Intent(ActivityMain.this, ActivityRegister.class);
                startActivity(in);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
