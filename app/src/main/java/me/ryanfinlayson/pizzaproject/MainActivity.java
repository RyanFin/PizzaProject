package me.ryanfinlayson.pizzaproject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ShareActionProvider;

public class MainActivity extends Activity {

    private String[] titles;
    private ListView drawerList;

    //create a private variable for the shareActionProvider
    private ShareActionProvider shareActionProvider;
    //create the DrawerListener
    private ActionBarDrawerToggle drawerToggle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer){
            //when drawer is fully open
            @Override
            public void onDrawerOpened(View drawerView) {
               // invalidateOptionsMenu();
                super.onDrawerOpened(drawerView);
            }

            //when drawer is fully closed
            @Override
            public void onDrawerClosed(View drawerView) {
               // invalidateOptionsMenu();
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        //populate the listView with the array of titles
        titles = getResources().getStringArray(R.array.titles);
        drawerList = (ListView) findViewById(R.id.drawer);
        drawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, titles ));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        //if the app hasnt changed configuration display Home by default
        if(savedInstanceState == null){
            selectItem(0);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        //sync the toggle state after onRestoreInstanceState has occurred
        drawerToggle.syncState();
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        drawerToggle.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    //called once we use InvalidateOptionsMenu() for drawer open and close logic
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //check if the listView drawerlist is fully open
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        //set the actionShare in the actionBar to invisible i
        menu.findItem(R.id.action_share).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    //drawer onClick method
    private class DrawerItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            //code runs when drawer item is clicked
            selectItem(position);

        }
    }

    private void selectItem(int position){
        Fragment fragment;
        switch(position){
            case 1:
                fragment = new PizzaFragment();
                break;
            case 2:
                fragment = new PastaFragment();
                break;
            case 3:
                fragment = new StoresFragment();
                break;
            default:
                fragment = new TopFragment();
                break;
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

        //set the actionBar title
        setActionBarTitle(position);
        //create a reference to the drawer
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //close the ListView in the drawer
        drawerLayout.closeDrawer(drawerList);

    }

    private void setActionBarTitle(int position){
        //create a new String for the title
        String title;
        //if user selects the home screen(position 0 in listView)
        if(position == 0){
            //display the name of the app
            title = getResources().getString(R.string.app_name);
        } else{
            //give the String the value of the String at the index in the 'titles' array
            title = titles[position];
        }
        //set the actionBar value to the title
        getActionBar().setTitle(title);
    }

    //add the menu item to the actionBar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) menuItem.getActionProvider();
        setIntent("Enjoy our skateboards? Share this app with your mates :)");

        return super.onCreateOptionsMenu(menu);
    }

    //create the setIntent() method

    private void setIntent(String text){
        //implicit intent
        Intent intent = new Intent(Intent.ACTION_SEND);
        //set the mime type
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        shareActionProvider.setShareIntent(intent);

    }



    //actionBar button logic
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        switch (item.getItemId()){
            case R.id.action_create_order:
                //code to run when order button  is pressed
                Intent intent = new Intent(this, OrderActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
