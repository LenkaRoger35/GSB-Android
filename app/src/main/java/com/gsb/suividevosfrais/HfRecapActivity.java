package com.gsb.suividevosfrais;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.app.Activity;
import android.text.Layout;
import android.view.ViewGroup;
import android.widget.DatePicker.OnDateChangedListener;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

public class HfRecapActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hf_recap);

		// modification de l'affichage du DatePicker
		Global.changeAfficheDate((DatePicker) findViewById(R.id.datHfRecap)) ;
        // valorisation des proprietes
        afficheListe();
        // chargement des methodes evenementielles
        imgReturn_clic();
        dat_clic() ;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hf_recap, menu);
		return true;
	}

	/**
     * Affiche la liste des frais hors forfaits de la date selectionnee
     */
    public void afficheListe() {
		Integer annee = ((DatePicker)findViewById(R.id.datHfRecap)).getYear() ;
		Integer mois = ((DatePicker)findViewById(R.id.datHfRecap)).getMonth() + 1 ;
        // recuperation des frais HF pour cette date
        Integer key = annee * 100 + mois;
        ArrayList<FraisHf> liste = null ;
		if (Global.listFraisMois.containsKey(key)) {
			liste = Global.listFraisMois.get(key).getLesFraisHf() ;
		}else{
			liste = new ArrayList<FraisHf>() ;
			// insertion dans la listview
		}
		final ListView listView = (ListView) findViewById(R.id.lstHfRecap);
		FraisHfAdapter adapter = new FraisHfAdapter(HfRecapActivity.this, liste, key) ;
		listView.setAdapter(adapter) ;

	}
	
	/**
	 * Sur la selection de l'image : retour au menu principal
	 */
    private void imgReturn_clic() {
    	((ImageView)findViewById(R.id.imgHfRecapReturn)).setOnClickListener(new ImageView.OnClickListener() {
    		public void onClick(View v) {
    			retourActivityPrincipale() ;    		
    		}
    	}) ;
    }

    /**
     * Sur le changement de date : mise a jour de l'affichage de la qte
     */
	private void dat_clic() {
		final DatePicker uneDate = (DatePicker) findViewById(R.id.datHfRecap);
		uneDate.init(uneDate.getYear(), uneDate.getMonth(), uneDate.getDayOfMonth(), new OnDateChangedListener(){
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				afficheListe() ;				
			}
    	});       	
    }


	/**
     * Retour a l'activite principale (le menu)
     */
    private void retourActivityPrincipale() {
		Intent intent = new Intent(HfRecapActivity.this, MainActivity.class) ;
		startActivity(intent) ;   					
	}


}
