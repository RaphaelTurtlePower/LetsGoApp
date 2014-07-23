package com.app.letsgo.activities;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.app.letsgo.R;

public class FragmentNavigationDrawer extends DrawerLayout {
	
	HashMap<Integer, Fragment> drawerItems;
	
	private ActionBarDrawerToggle drawerToggle;
		private ListView lvDrawer;
		private ArrayAdapter<String> drawerAdapter;
		private ArrayList<FragmentNavItem> drawerNavItems;
		private int drawerContainerRes;

		public FragmentNavigationDrawer(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
		}

		public FragmentNavigationDrawer(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		public FragmentNavigationDrawer(Context context) {
			super(context);
		}
	    
		// setupDrawerConfiguration((ListView) findViewById(R.id.lvDrawer), R.layout.drawer_list_item, R.id.flContent);
		public void setupDrawerConfiguration(ListView drawerListView, int drawerItemRes, int drawerContainerRes) {
			// Setup navigation items array
			drawerNavItems = new ArrayList<FragmentNavigationDrawer.FragmentNavItem>();
			
			// Set the adapter for the list view
			drawerAdapter = new ArrayAdapter<String>(getActivity(), drawerItemRes, 
                            new ArrayList<String>());
			this.drawerContainerRes = drawerContainerRes; 
			// Setup drawer list view and related adapter
			lvDrawer = drawerListView;
			lvDrawer.setBackgroundResource(R.drawable.abstractbkgrnd);
			lvDrawer.setAdapter(drawerAdapter);
			// Setup item listener
			lvDrawer.setOnItemClickListener(new FragmentDrawerItemListener());
			// ActionBarDrawerToggle ties together the the proper interactions
			// between the sliding drawer and the action bar app icon
			drawerToggle = setupDrawerToggle();
			setDrawerListener(drawerToggle);
			// set a custom shadow that overlays the main content when the drawer
			// setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
			// Setup action buttons
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);
			Log.d("debug", "Drawer.setupDrawerConfig(): drawerNavItems.size = " + drawerNavItems.size());
			
		}

		// addNavItem("First", "First Fragment", FirstFragment.class)
		public void addNavItem(String navTitle, String windowTitle, Class<? extends Fragment> fragmentClass) {
			drawerAdapter.add(navTitle);
			drawerNavItems.add(new FragmentNavItem(windowTitle, fragmentClass));
		}
		
		/** Swaps fragments in the main content view */
		public void selectDrawerItem(int position) {
			// Create a new fragment and specify the item to show based on position
			Log.d("debug", "Drawer.selectDrawerItem(): drawerNavItems.size = " + drawerNavItems.size());
			FragmentNavItem navItem = drawerNavItems.get(position);
			
			// Fragment fragment = null;
			if (drawerItems == null)  {
				drawerItems = new HashMap<Integer, Fragment>();
			}
			// Fragment fragment = drawerItems.get(position);
			try {
				// need to pass in the events 
				Fragment fragment = navItem.getFragmentClass().newInstance();
				Log.d("debug", "selectDrawerItem(): fragment = " + fragment);
				Bundle args = navItem.getFragmentArgs();
				Log.d("debug", "selectDrawerItem(): args = " + args);
				if (args != null) { 
				  fragment.setArguments(args);
				}
				
				// save the fragment in its position
				drawerItems.put(position,  fragment);

				// Insert the fragment by replacing any existing fragment
				FragmentManager fragmentManager = getActivity().getFragmentManager();
				fragmentManager.beginTransaction().replace(drawerContainerRes, fragment).commit();

				// Highlight the selected item, update the title, and close the drawer
				lvDrawer.setItemChecked(position, true);
				setTitle(navItem.getTitle());
				closeDrawer(lvDrawer);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		

		public ActionBarDrawerToggle getDrawerToggle() {
			return drawerToggle;
		}
		
		private FragmentActivity getActivity() {
			return (FragmentActivity) getContext();
		}

		private ActionBar getSupportActionBar() {
			return ((ActionBarActivity) getActivity()).getActionBar();
		}

		private void setTitle(CharSequence title) {
			getSupportActionBar().setTitle(title);
		}
		
		private class FragmentDrawerItemListener implements ListView.OnItemClickListener {		
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectDrawerItem(position);
			}	
		}
		
		private class FragmentNavItem {
			private Class<? extends Fragment> fragmentClass;
			private String title;
			private Bundle fragmentArgs;
			
			public FragmentNavItem(String title, Class<? extends Fragment> fragmentClass) {
				this(title, fragmentClass, null);
			}

			public FragmentNavItem(String title, Class<? extends Fragment> fragmentClass, Bundle args) {
				this.fragmentClass = fragmentClass;
				this.fragmentArgs = args;
				this.title = title;
			}
			
			public Class<? extends Fragment> getFragmentClass() {
				return fragmentClass;
			}
			
			public String getTitle() {
				return title;
			}
			
			public Bundle getFragmentArgs() {
				return fragmentArgs;
			}
		}
		
		private ActionBarDrawerToggle setupDrawerToggle() {
			return new ActionBarDrawerToggle(getActivity(), /* host Activity */
			this, /* DrawerLayout object */
			R.drawable.nav_drawer, /* nav drawer image to replace 'Up' caret */
			R.string.drawer_open, /* "open drawer" description for accessibility */
			R.string.drawer_close /* "close drawer" description for accessibility */
			) {
				public void onDrawerClosed(View view) {
					// setTitle(getCurrentTitle());
					// call onPrepareOptionsMenu()
					getActivity().supportInvalidateOptionsMenu(); 
				}

				public void onDrawerOpened(View drawerView) {
					// setTitle("Navigate");
					// call onPrepareOptionsMenu()
					getActivity().supportInvalidateOptionsMenu(); 
				}
			};
		}

		public boolean isDrawerOpen() {
			return isDrawerOpen(lvDrawer);
		}
		
		public Fragment getFragment(int position) {
			return drawerItems.get(position);
		}

}