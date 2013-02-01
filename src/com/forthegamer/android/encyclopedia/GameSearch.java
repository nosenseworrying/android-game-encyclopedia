package com.forthegamer.android.encyclopedia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class GameSearch extends Activity {
	private String[] mPlatformList={"PC", "PS2", "PS3", "PSP",  "Xbox", "Xbox 360", "Nintendo 64", 
			"GameCube", "Wii", "Nintendo DS", "Game Boy", "Game Boy Advance", "Dreamcast"};
	private String[] mPlatforms={"pc", "ps2", "ps3", "psp",  "xbox", "xbox360", "n64", 
			"gc", "wii", "ds", "gameboy", "gba", "dc"};	
	private Button mSearchButton;
	private Spinner mSpnPlatform;
	private EditText mQueryTextView;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_search);
        mSpnPlatform = (Spinner)findViewById(R.id.spPlatform);
        mSearchButton = (Button)findViewById(R.id.btnGo);
        
        ArrayAdapter<String> platformAdapter=new ArrayAdapter<String>(this,
        		android.R.layout.simple_spinner_item,
        		mPlatformList);
        
        platformAdapter.setDropDownViewResource(
        		android.R.layout.simple_spinner_dropdown_item);
        mSpnPlatform.setAdapter(platformAdapter);
        
        mQueryTextView = (EditText)findViewById(R.id.txtName);
        mQueryTextView.addTextChangedListener (new TextWatcher(){

			public void onTextChanged(CharSequence text, int start, int before,
					int count) {
					int numChars = 0;
					if(text != null){
						numChars = text.length();
					}
					
					if(numChars > 2){
						mSearchButton.setEnabled(true);
					} else {
						mSearchButton.setEnabled(false);
					}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			
			public void afterTextChanged(Editable s) {}
        });
        
        Button btnSearch = (Button)findViewById(R.id.btnGo);
        btnSearch.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				 Intent i = new Intent(GameSearch.this, GameSearchResults.class);
				 long platformID = mSpnPlatform.getSelectedItemId();
				 String platform = mPlatforms[(int) platformID];
				 String platformDisplay = mPlatformList[(int) platformID];
				 String query = mQueryTextView.getText().toString().trim();
				 
				 i.putExtra("platformdisplay", platformDisplay);
				 i.putExtra("platform", platform);
				 i.putExtra("query", query);
				 startActivity(i);	
			}
		});
    }
}
