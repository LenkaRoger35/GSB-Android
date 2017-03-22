package com.gsb.suividevosfrais;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import static com.gsb.suividevosfrais.R.id.datRepas;

/**
 * Created by L.R. on 22/03/17.
 */

public class RepasActivity extends Activity {

    // informations affich�es dans l'activit�
    private Integer annee;
    private Integer mois;
    private Integer qte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repas);
        // modification de l'affichage du DatePicker
        Global.changeAfficheDate((DatePicker) findViewById(datRepas));
        // valorisation des propri�t�s
        valoriseProprietes();
        // chargement des m�thodes �v�nementielles
        imgReturn_clic();
        cmdValider_clic();
        cmdPlus_clic();
        cmdMoins_clic();
        dat_clic();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.repas, menu);
        return true;
    }

    /**
     * Valorisation des propri�t�s avec les informations affich�es
     */
    private void valoriseProprietes() {
        annee = ((DatePicker) findViewById(R.id.datRepas)).getYear();
        mois = ((DatePicker) findViewById(R.id.datRepas)).getMonth() + 1;
        // r�cup�ration de la qte correspondant au mois actuel
        qte = 0;
        Integer key = annee * 100 + mois;
        if (Global.listFraisMois.containsKey(key)) {
            qte = Global.listFraisMois.get(key).getRepas();
        }
        ((EditText) findViewById(R.id.txtRepas)).setText(qte.toString());
    }

    /**
     * Sur la selection de l'image : retour au menu principal
     */
    private void imgReturn_clic() {
        ((ImageView) findViewById(R.id.imgRepasReturn)).setOnClickListener(new ImageView.OnClickListener() {
            public void onClick(View v) {
                retourActivityPrincipale();
            }
        });
    }

    /**
     * Sur le clic du bouton valider : s�rialisation
     */
    private void cmdValider_clic() {
        ((Button) findViewById(R.id.cmdRepasValider)).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Serializer.serialize(Global.filename, Global.listFraisMois, RepasActivity.this);
                retourActivityPrincipale();
            }
        });
    }

    /**
     * Sur le clic du bouton plus : ajout de 10 dans la quantit�
     */
    private void cmdPlus_clic() {
        ((Button) findViewById(R.id.cmdRepasPlus)).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                qte += 1;
                enregNewQte();
            }
        });
    }

    /**
     * Sur le clic du bouton moins : enl�ve 10 dans la quantit� si c'est possible
     */
    private void cmdMoins_clic() {
        ((Button) findViewById(R.id.cmdRepasMoins)).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                qte = Math.max(0, qte - 1); // suppression de 10 si possible
                enregNewQte();
            }
        });
    }

    /**
     * Sur le changement de date : mise � jour de l'affichage de la qte
     */
    private void dat_clic() {
        final DatePicker uneDate = (DatePicker) findViewById(R.id.datRepas);
        uneDate.init(uneDate.getYear(), uneDate.getMonth(), uneDate.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                valoriseProprietes();
            }
        });
    }

    /**
     * Enregistrement dans la zone de texte et dans la liste de la nouvelle qte, � la date choisie
     */
    private void enregNewQte() {
        // enregistrement dans la zone de texte
        ((EditText) findViewById(R.id.txtRepas)).setText(qte.toString());
        // enregistrement dans la liste
        Integer key = annee * 100 + mois;
        if (!Global.listFraisMois.containsKey(key)) {
            // creation du mois et de l'annee s'ils n'existent pas d�j�
            Global.listFraisMois.put(key, new FraisMois(annee, mois));
        }
        Global.listFraisMois.get(key).setRepas(qte);
    }

    /**
     * Retour � l'activit� principale (le menu)
     */
    private void retourActivityPrincipale() {
        Intent intent = new Intent(RepasActivity.this, MainActivity.class) ;
        startActivity(intent) ;
    }




}