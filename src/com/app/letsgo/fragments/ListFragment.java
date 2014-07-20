package com.app.letsgo.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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
import com.app.letsgo.models.LocalEventParcel;

public class ListFragment extends Fragment {

	private ArrayList<LocalEventParcel> itemList;
	private ListEventsAdapter itemsAdapter;
	private ListView items;
	
	public static ListFragment newInstance(ArrayList<LocalEventParcel> events){
		ListFragment listFragment = new ListFragment();
		Bundle args = new Bundle();
		args.putParcelableArrayList("events", events);
		listFragment.setArguments(args);
		return listFragment;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	public void setList(ArrayList<LocalEventParcel> events){
		itemList.clear();
		itemList.addAll(events);
		itemsAdapter.notifyDataSetChanged();
	}

	public void addEvent(LocalEvent event){
		itemList.add(new LocalEventParcel(event));
		itemsAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		itemList = args.getParcelableArrayList("events");
	}
	
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	      // Defines the xml file for the fragment
		 View view = inflater.inflate(R.layout.fragment_map_list_view, container, false);
		 items = (ListView) view.findViewById(R.id.map_list_view);
	 	 itemsAdapter = new ListEventsAdapter(getActivity(), itemList);
	 	 items.setAdapter(itemsAdapter);
	 	 items.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LocalEventParcel event = itemList.get(position);
				Intent i = new Intent(getActivity(), EventDetailActivity.class);
				i.putExtra("event", event);
				startActivity(i);
			}
	 	 });
	 	 return view;
	 }

}
