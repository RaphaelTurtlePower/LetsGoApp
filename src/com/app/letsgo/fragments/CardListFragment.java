package com.app.letsgo.fragments;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.letsgo.R;
import com.app.letsgo.activities.EventDetailActivity;
import com.app.letsgo.models.EventCard;
import com.app.letsgo.models.LocalEvent;
import com.app.letsgo.models.LocalEventParcel;

public class CardListFragment extends Fragment {

	private ArrayList<LocalEventParcel> itemList;
	private ArrayList<Card> cards;
	private CardArrayAdapter mCardArrayAdapter; 
    private CardListView listView; 
	
	public static CardListFragment newInstance(ArrayList<LocalEventParcel> events){
		CardListFragment listFragment = new CardListFragment();
		Bundle args = new Bundle();
		args.putParcelableArrayList("events", events);
		listFragment.setArguments(args);
		return listFragment;
	}
	
	public ArrayList<Card> cardsPresenter(ArrayList<LocalEventParcel> events){
		ArrayList<Card> myCards = new ArrayList<Card>();
		
		if(events == null){
			return myCards; 
		}
		for(int i=0; i<events.size(); i++){
			LocalEvent e = events.get(i).getEvent();
			EventCard card = generateCard(e);
            myCards.add(card);
		}
		
		return myCards;
	}
	
	public EventCard generateCard(final LocalEvent e){
		 // Create a Card
		EventCard card = new EventCard(getActivity());
        // Create a CardHeader
        CardHeader header = new CardHeader(getActivity());
        // Add Header to card
        header.setTitle(e.getEventName());
        card.setAddress(e.getLocation().getAddress());
        card.addCardHeader(header);
        CardThumbnail thumb = new CardThumbnail(getActivity());
        thumb.setDrawableResource(LocalEvent.getTypeImage(e.getEventType()));
        card.addCardThumbnail(thumb);
        card.setCost("$" + e.getCost().toString());
        card.setStartDate(e.getStartDate());
        String percentage = "Be the first to recommend!";
        int total = e.getUpCount().intValue() + e.getDownCount().intValue();
        if(total > 0){
        	Integer p = e.getUpCount().intValue() * 100 / total;
        	percentage = p.toString() + "% Recommended";
        }
        card.setPercentage(percentage);
    /*    CardExpand ce = new CardExpand(getActivity());
        ce.setTitle(e.getItemShortDescription());
        card.addCardExpand(ce);
        card.setExpanded(true); */
        
        card.setOnClickListener(new Card.OnCardClickListener() {
			@Override
			public void onClick(Card arg0, View arg1) {
				Intent i = new Intent(getActivity(), EventDetailActivity.class);
				i.putExtra("event", new LocalEventParcel(e));
				startActivity(i);
			}
		});
        return card;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	public void setList(ArrayList<LocalEventParcel> events){
		cards.clear();
		cards.addAll(cardsPresenter(events));
		mCardArrayAdapter.notifyDataSetChanged();
	}

	public void addEvent(LocalEvent event){
		cards.add(generateCard(event));
		mCardArrayAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		itemList = args.getParcelableArrayList("events");
		cards = cardsPresenter(itemList);
	}
	
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	      // Defines the xml file for the fragment
		 View view = inflater.inflate(R.layout.fragment_card_list, container, false);
		 mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);
		 listView = (CardListView) view.findViewById(R.id.myList);
		 listView.setAdapter(mCardArrayAdapter);
		 	
	 	 return view;
	 }

}
