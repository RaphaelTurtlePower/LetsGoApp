package com.app.letsgo.models;

import com.app.letsgo.R;

import it.gmariotti.cardslib.library.internal.Card;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class EventCard extends Card {

	private String startDate;
	private String cost;
	private String percentage;
	private String address;
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	public EventCard(Context context) {
		super(context, R.layout.inner_base_main);
	}

	 public void setupInnerViewElements(ViewGroup parent, View view) {
		 TextView tvAddress = (TextView) view.findViewById(R.id.tvAddressCard);
		 TextView tvStartDate = (TextView) view.findViewById(R.id.tvStartDateCard);
	       TextView tvCost = (TextView) view.findViewById(R.id.tvCostCard);
	       TextView tvPercent = (TextView) view.findViewById(R.id.tvPercentLike); 
	       tvCost.setText(cost);
	        tvStartDate.setText(startDate);
	        tvPercent.setText(percentage);
	        tvAddress.setText(address);
	   }

	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
