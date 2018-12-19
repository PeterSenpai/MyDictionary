package com.example.petersenpai.mydictionary;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    public static NotificationHelper helper;

    public static NotificationHelper getHelper() {
        return helper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new NotificationHelper(this);
        setContentView(R.layout.activity_main);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO: clear search history
        // TODO: hide/show translation
        // TODO: detailed word translation page

        // drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // navigation view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // update history word list
        UpdateWordList();

        // start service
        Toast.makeText(getApplicationContext(), "start service!",
                Toast.LENGTH_SHORT).show();
        Intent startIntent = new Intent(this, MyService.class);
        startService(startIntent);
        clickCallBack();
    }

    private void clickCallBack() {
        ListView lv = (ListView)findViewById(R.id.list_view);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String message = "hhhiiii";
                TextView textView = (TextView)findViewById(R.id.translation);
                List<Word> words = Util.getHistoryWords(MainActivity.this);
                Word w = words.get(position);
                message = w.getTranslation();
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UpdateWordList() {
        WordAdapter adapter = new WordAdapter(MainActivity.this, R.layout.word_list_item, Util.getHistoryWords(this));
        ListView listview = (ListView) findViewById(R.id.list_view);
        listview.setAdapter(adapter);

//        LinearLayout l = (LinearLayout) listview.getChildAt(0 - listview.getFirstVisiblePosition());
//        TextView t = (TextView) l.getChildAt(0);
//        t.setTextColor(Color.rgb(255, 0, 0));
    }

    @Override
    public void onResume() {
        // history words list view
        super.onResume();
        UpdateWordList();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "open setting page",
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_hide_translation) {
            Toast.makeText(getApplicationContext(), "hide word translation",
                    Toast.LENGTH_SHORT).show();
            Util.hideTranslation();
            UpdateWordList();
            return true;
        }
        if (id == R.id.action_clear_history) {
            Util.clearHistoryWords();
            UpdateWordList();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        Log.d("system.out", "stop service");
        Util.SaveHistoryWordsToFile(this);
        Intent stopIntent = new Intent(this, MyService.class);
        stopService(stopIntent);
        super.onDestroy();
    }
}
