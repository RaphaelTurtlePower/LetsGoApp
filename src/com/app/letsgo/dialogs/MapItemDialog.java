package com.app.letsgo.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.app.letsgo.R;
import com.app.letsgo.models.Event;

@SuppressLint("ValidFragment")
public class MapItemDialog extends DialogFragment {

private View dialogView;
private Event event;

public MapItemDialog(Event event){
	this.event = event;
	
}
@Override
public Dialog onCreateDialog(Bundle savedInstanceState) {
    // Use the Builder class for convenient dialog construction
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogSlideAnim);
    LayoutInflater inflater = getActivity().getLayoutInflater();
    dialogView = inflater.inflate(R.layout.map_item, null);
    TextView title = (TextView) dialogView.findViewById(R.id.map_item_title);
    TextView description = (TextView) dialogView.findViewById(R.id.map_item_description);
    title.setText(event.getName());
    description.setText(event.getDescription());
    builder.setView(dialogView);
    // Create the AlertDialog object and return it
    return builder.create();
}

}