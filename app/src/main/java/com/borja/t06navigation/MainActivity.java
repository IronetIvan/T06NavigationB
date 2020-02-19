package com.borja.t06navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.borja.t06navigation.utils.Liga;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView textView;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private SwitchCompat switchCompat;
    private String urlPeticion;
    private ArrayList<Liga> listaLigas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instancias();
        acciones();
        //peticionInicial();
    }

    private void peticionInicial() {

        JsonObjectRequest peticionJson = new JsonObjectRequest(Request.Method.GET,
                urlPeticion, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Log.v("volley", response.toString());
                try {
                    JSONArray jsonArray = response.getJSONArray("leagues");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        String nombre = jsonObject.getString("strLeague");
                        String deporte = jsonObject.getString("strSport");
                        int id = jsonObject.getInt("idLeague");
                        if (deporte.toLowerCase().equals("soccer")) {
                            Liga liga = new Liga(id,nombre);
                            listaLigas.add(liga);
                        }
                    }
                    Log.v("volley", String.valueOf(listaLigas.size()));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("volley", "Error en la conexion");
            }
        });

        Volley.newRequestQueue(getApplicationContext()).add(peticionJson);

    }

    private void acciones() {


        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                Toast.makeText(getApplicationContext(), String.valueOf(b), Toast.LENGTH_SHORT).show();
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_competiciones:
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.sitio_fragments,new LigasFragment());
                        ft.commit();
                        textView.setText("competiciones");
                        break;
                    case R.id.liga_esp_nav:
                        textView.setText("la liga");
                        break;
                    case R.id.liga_ale_nav:
                        textView.setText("bundesliga");
                        break;
                    case R.id.liga_ing_nav:
                        textView.setText("premiere");
                        break;
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }


    private void instancias() {
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation);
        switchCompat = (SwitchCompat) navigationView.getMenu()
                .findItem(R.id.switch_nav).getActionView();
        textView = navigationView.getHeaderView(0)
                .findViewById(R.id.texto_header);
        listaLigas = new ArrayList();
        urlPeticion = "https://www.thesportsdb.com/api/v1/json/1/all_leagues.php";
        setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this
                ,drawerLayout,toolbar,R.string.open, R.string.close);
        





    }
}
