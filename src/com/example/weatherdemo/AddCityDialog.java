package com.example.weatherdemo;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class AddCityDialog implements View.OnClickListener{
	Dialog alert;
	
	EditText ed_country,ed_city;
	AddCityDialogListener addCityDialogListener;
	
	
	public Dialog ShowDialog(Context context){
		final Dialog addDialog = new Dialog(context, R.style.Dialog);
		addDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		addDialog.setContentView(R.layout.dialog_add);
		
		
		ed_country = (EditText) addDialog.findViewById(R.id.editText1);
		ed_city = (EditText) addDialog.findViewById(R.id.editText2);
		
		Button bt_addButton =  (Button) addDialog.findViewById(R.id.button1);
		bt_addButton.setOnClickListener(this);
	
		addDialog.show();
		
		alert = addDialog;
		return addDialog;
	}

	@Override
	public void onClick(View v) {
		if(ed_city.getText().toString().length()*ed_country.getText().toString().length()>0){
			
			addCityDialogListener.userSelectedAValue(ed_country.getText().toString(), ed_city.getText().toString());
			alert.cancel();
			
		}
		
	}

	public AddCityDialogListener getAddCityDialogListener() {
		return addCityDialogListener;
	}

	public void setAddCityDialogListener(AddCityDialogListener addCityDialogListener) {
		this.addCityDialogListener = addCityDialogListener;
	}
	

	
	

}
