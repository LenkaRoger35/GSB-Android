package com.gsb.suividevosfrais;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by L.R. on 22/03/17.
 */

public class NuiteeActivity extends Activity {

    // informations affichees dans l'activite
    private Integer annee ;
    private Integer mois ;
    private Integer qte ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuitee);
        // modification de l'affichage du DatePicker
        Global.changeAfficheDate((DatePicker) findViewById(R.id.datNuitee)) ;
        // valorisation des proprietes
        valoriseProprietes() ;
        // chargement des methodes evenementielles
        imgReturn_clic() ;
        cmdValider_clic() ;
        cmdPlus_clic() ;
        cmdMoins_clic() ;
        dat_clic() ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nuitee, menu);
        return true;
    }

    /**
     * Valorisation des proprietes avec les informations affichees
     */
    private void valoriseProprietes() {
        annee = ((DatePicker)findViewById(R.id.datNuitee)).getYear() ;
        mois = ((DatePicker)findViewById(R.id.datNuitee)).getMonth() + 1 ;
        // recuperation de la qte correspondant au mois actuel
        qte = 0 ;
        Integer key = annee*100+mois ;
        if (Global.listFraisMois.containsKey(key)) {
            qte = Global.listFraisMois.get(key).getNuitee() ;
        }
        ((EditText)findViewById(R.id.txtNuitee)).setText(qte.toString()) ;
    }

    /**
     * Sur la selection de l'image : retour au menu principal
     */
    private void imgReturn_clic() {
        ((ImageView)findViewById(R.id.imgNuiteeReturn)).setOnClickListener(new ImageView.OnClickListener() {
            public void onClick(View v) {
                retourActivityPrincipale() ;
            }
        }) ;
    }

    /**
     * Sur le clic du bouton valider : serialisation
     */
    private void cmdValider_clic() {
        ((Button)findViewById(R.id.cmdNuiteeValider)).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Serializer.serialize(Global.filename, Global.listFraisMois, NuiteeActivity.this) ;
                retourActivityPrincipale() ;
            }
        }) ;
    }

    /**
     * Sur le clic du bouton plus : ajout de 10 dans la quantite
     */
    private void cmdPlus_clic() {
        ((Button)findViewById(R.id.cmdNuiteePlus)).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                qte+=1 ;
                enregNewQte() ;
            }
        }) ;
    }

    /**
     * Sur le clic du bouton moins : enleve 10 dans la quantite si c'est possible
     */
    private void cmdMoins_clic() {
        ((Button)findViewById(R.id.cmdNuiteeMoins)).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                qte = Math.max(0, qte-1) ; // suppression de 10 si possible
                enregNewQte() ;
            }
        }) ;
    }

    /**
     * Sur le changement de date : mise a jour de l'affichage de la qte
     */
    private void dat_clic() {
        final DatePicker uneDate = (DatePicker)findViewById(R.id.datNuitee) ;
        uneDate.init(uneDate.getYear(), uneDate.getMonth(), uneDate.getDayOfMonth(), new DatePicker.OnDateChangedListener(){
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                valoriseProprietes() ;
            }
        });
    }

    /**
     * Enregistrement dans la zone de texte et dans la liste de la nouvelle qte, a la date choisie
     */
    private void enregNewQte() {
        // enregistrement dans la zone de texte
        ((EditText)findViewById(R.id.txtNuitee)).setText(qte.toString()) ;
        // enregistrement dans la liste
        Integer key = annee*100+mois ;
        if (!Global.listFraisMois.containsKey(key)) {
            // creation du mois et de l'annee s'ils n'existent pas deja
            Global.listFraisMois.put(key, new FraisMois(annee, mois)) ;
        }
        Global.listFraisMois.get(key).setNuitee(qte) ;
    }

    /**
     * Retour a l'activite principale (le menu)
     */
    private void retourActivityPrincipale() {
        Intent intent = new Intent(NuiteeActivity.this, MainActivity.class) ;
        startActivity(intent) ;
    }


}
