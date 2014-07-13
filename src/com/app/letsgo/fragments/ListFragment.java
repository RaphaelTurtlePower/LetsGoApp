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
import com.app.letsgo.models.LocalEvent;

public class ListFragment extends Fragment {
	private ArrayList<LocalEvent> itemList;
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
	 	 itemList = LocalEvent.getLocalEvents();
	 	 itemsAdapter = new ListEventsAdapter(getActivity(), itemList);
	 	 items.setAdapter(itemsAdapter);
	 	 items.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LocalEvent event = itemList.get(position);
				Intent i = new Intent(getActivity(), EventDetailActivity.class);
				i.putExtra("event", event);
				startActivity(i);
				
			}
			
			
		});
		 return view;
	 }	
}
