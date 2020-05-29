package com.food.nofoodwaste.actvities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

import com.food.nofoodwaste.ImageSliderAdapter;
import com.food.nofoodwaste.PermissionHandler;
import com.food.nofoodwaste.R;
import com.food.nofoodwaste.ViewPagerCustomDuration;
import com.food.nofoodwaste.utils.AppSharedPreferences;

public class DashBoardActivity extends AppCompatActivity {
    private EditText edtName,edtPhone;
    AppSharedPreferences appSharedPreferences;
    final int CHANGE_AFTER = 2;
    final int CHANGING_SPEED = 4;
    private String[] urls = {
            "https://i.ytimg.com/vi/Q8B71EDsZTM/maxresdefault.jpg",
            "https://pbs.twimg.com/media/C9ghQEqW0AETXuL.jpg",
            "http://newsofwoodlandpark.com/wp-content/uploads/2017/02/Help-the-Needy-Logo.jpg",
            "https://pre00.deviantart.net/c7c6/th/pre/f/2015/016/1/2/i_love_humanity_no_game_no_life_by_bl4ckf4ll-d8e5cj9.png"
            };


    private ImageSliderAdapter imageSliderAdapter;
    private ViewPagerCustomDuration viewPager;
    private Timer timer;
    private int page = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        String[] permissions = {Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        new PermissionHandler().checkPermission(DashBoardActivity.this, permissions, new PermissionHandler.OnPermissionResponse() {
            @Override
            public void onPermissionGranted() {
                imageSliderAdapter = new ImageSliderAdapter(getSupportFragmentManager(), urls);
                setupViewpager();
            }

            @Override
            public void onPermissionDenied() {
            }
        });
        //initView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.layout_donate_food).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donationBtnClick();
            }
        });
        findViewById(R.id.layout_map_a_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapLocationBtnClick();
            }
        });
        findViewById(R.id.about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutUsBtnClick();
            }
        });
        findViewById(R.id.contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactUsBtnClick();
            }
        });

    }
    public void pageSwitcher(int seconds) {
        timer = new Timer();
        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000); // delay
    }

    class RemindTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (page < imageSliderAdapter.getCount())
                        viewPager.setCurrentItem(page++);
                    else {
                        page = 0;
                        viewPager.setCurrentItem(page++);
                    }
                }
            });

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Intent intent = new Intent("PERMISSION_RECEIVER");
        intent.putExtra("requestCode", requestCode);
        intent.putExtra("permissions", permissions);
        intent.putExtra("grantResults", grantResults);
        sendBroadcast(intent);
    }

    private void setupViewpager() {
        viewPager = (ViewPagerCustomDuration) findViewById(R.id.pager);
        viewPager.setScrollDurationFactor(CHANGING_SPEED);
        viewPager.setAdapter(imageSliderAdapter);
        pageSwitcher(CHANGE_AFTER);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                page = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void donationBtnClick(){
        Intent intent = new Intent(getApplicationContext(),EnterDonationDetailsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void mapLocationBtnClick(){
        Intent intent = new Intent(getApplicationContext(),MapALocationActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    private void aboutUsBtnClick(){
        Intent intent = new Intent(getApplicationContext(),AboutUs.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    private void contactUsBtnClick(){
        Intent intent = new Intent(getApplicationContext(),ContactUs.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }


}
