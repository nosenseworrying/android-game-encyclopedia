package com.forthegamer.android.encyclopedia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class AndroidGameEncyclopedia extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final ImageButton btnFeatures = (ImageButton) findViewById(R.id.btnFeatures);
        btnFeatures.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Perform action on clicks
            	 Intent i = new Intent(AndroidGameEncyclopedia.this, NewsFeatureList.class);
		         startActivity(i);
            }
        });
        
        final ImageButton btnPlatform = (ImageButton) findViewById(R.id.btnBrowse);
        btnPlatform.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Perform action on clicks
            	 Intent i = new Intent(AndroidGameEncyclopedia.this, GameBrowser.class);
		         startActivity(i);
            }
        });
        
        final ImageButton btnSearch = (ImageButton) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Perform action on clicks
                //Toast.makeText(AndroidGameEncyclopedia.this, "Search", Toast.LENGTH_SHORT).show();
	           	 Intent i = new Intent(AndroidGameEncyclopedia.this, GameSearch.class);
		         startActivity(i);
            }
        });
        
        final ImageButton btnAbout = (ImageButton) findViewById(R.id.btnAbout);
        btnAbout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Perform action on clicks
                //Toast.makeText(AndroidGameEncyclopedia.this, "About", Toast.LENGTH_SHORT).show();
	           	 Intent i = new Intent(AndroidGameEncyclopedia.this, About.class);
		         startActivity(i);
            }
        });        
    }
}