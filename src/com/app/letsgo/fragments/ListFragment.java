package com.app.letsgo.fragments;

import java.util.ArrayList;
import java.util.Date;

import android.os.Bundle;
import android.app.Fragment;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.app.letsgo.R;
import com.app.letsgo.activities.EventDetailActivity;
import com.app.letsgo.adapters.ListEventsAdapter;
import com.app.letsgo.models.Event;

public class ListFragment extends Fragment {
	private ArrayList<Event> itemList;
	private ListEventsAdapter itemsAdapter;
	private ListView items;
	
	public static ListFragment newInstance(){
		ListFragment listFragment = new ListFragment();
		return listFragment;
	}
	
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	      // Defines the xml file for the fragment
		 View view = inflater.inflate(R.layout.fragment_map_list_view, container, false);
		 items = (ListView) view.findViewById(R.id.map_list_view);
		 // Setup handles to view objects here
	      // etFoo = (EditText) v.findViewById(R.id.etFoo);
	 	 itemList = new ArrayList<Event>();
	 	 itemList.add( new Event(getActivity(), "3081 Tulare Dr, San Jose, CA 95132", "Raphael's 1st Birthday", "It's his birthday!", new Date()));
		 itemList.add(new Event(getActivity(), "1 Washington Square, San Jose, CA 95132", "Graduation!", "Yay!", new Date()));
		 itemList.add(new Event(getActivity(), "San Francisco, CA", "Fourth of July Bash!", "Bashing", new Date()));
	
	 	 itemsAdapter = new ListEventsAdapter(getActivity(), itemList);
	 	 items.setAdapter(itemsAdapter);
	 	 items.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Event event = itemList.get(position);
				Intent i = new Intent(getActivity(), EventDetailActivity.class);
				i.putExtra("event", event);
				startActivity(i);
				
			}
			
			
		});
		 return view;
	 }	
}
