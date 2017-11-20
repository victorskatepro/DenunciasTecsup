package com.victorsaico.denunciastecsup.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.victorsaico.denunciastecsup.R;
import com.victorsaico.denunciastecsup.fragments.AddDenunciaFragment;
import com.victorsaico.denunciastecsup.fragments.HomeFragment;
import com.victorsaico.denunciastecsup.fragments.PerfilFragment;
import com.victorsaico.denunciastecsup.servicies.ApiService;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout drawerLayout;
    private TextView menuusername;
    private TextView menucorreo;
    private ImageView menuphoto;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, android.R.string.ok,android.R.string.cancel);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(navigationView != null){
            prepararDrawer(navigationView);
            seleccionarItem(navigationView.getMenu().getItem(0));
        }
        menuusername = (TextView) navigationView.getHeaderView(0).findViewById(R.id.menu_fullname);
        menucorreo = (TextView) navigationView.getHeaderView(0).findViewById(R.id.menu_correo);
        menuphoto = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.menuphoto);
        imprimirDrawer();
    }
    private void imprimirDrawer(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = sharedPreferences.getString("username", null);
        String correo = sharedPreferences.getString("correo", null);
        String photo = sharedPreferences.getString("photo", null);
        String url = ApiService.API_BASE_URL + "/usuarios/" + photo;
        Picasso.with(MainActivity.this).load(url).into(menuphoto);
        menucorreo.setText(username);
        menuusername.setText(correo);
    }
    private void prepararDrawer(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        seleccionarItem(menuItem);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }
    private void seleccionarItem(MenuItem itemDrawer){
        Fragment fragmentoGenerico = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (itemDrawer.getItemId()){
            case R.id.nav_home:
                fragmentoGenerico = new HomeFragment();
                break;
            case R.id.nav_perfil:
                fragmentoGenerico = new PerfilFragment();
                break;
            case R.id.nav_register:
                fragmentoGenerico = new AddDenunciaFragment();
                break;
            case R.id.nav_logout:
                gologin();
                break;
        }
        if (fragmentoGenerico != null){
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.main_content, fragmentoGenerico)
                    .commit();
        }
        setTitle(itemDrawer.getTitle());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
//    private void logout(){
//        // remove from SharedPreferences
//        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
//                .setTitleText("Desea salir?")
//                .setContentText("")
//                .setCancelText("Si, pero vuelvo")
//                .setConfirmText("Cancelar")
//                .showContentText(true)
//                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sDialog) {
//                        sDialog.cancel();
//                        gologin();
//                    }
//                })
//                .show();
//    }
    private void gologin(){
        Log.d(TAG,"saliendo");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean success = editor.putBoolean("islogged", false).commit();
        finish();
    }

}

