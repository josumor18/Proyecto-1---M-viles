package me.flux.fluxme.Business;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.flux.fluxme.R;

public class EmisoraActivity extends BaseActivity {

    TabLayout tabLayout;
    FragmentTransaction fragmentTransaction;
    //PerfilEmisoraFragment perfil;
    private int[] tabTextUser = {R.string.tabTitPerfilEmisora, R.string.tabTitChat, R.string.tabTitVotaciones, R.string.tabTitTendencias, R.string.tabTitProgramacion};
    private int[] tabTextAdmin = {R.string.tabTitPerfil, R.string.tabTitChat, R.string.tabTitEstadisticas, R.string.tabTitGPS, R.string.tabTitProgramacion};
    private int[] tabIconsUser_Des = {
            R.drawable.tab_perfil_emisora_des,
            R.drawable.tab_chat_des,
            R.drawable.tab_votar_des,
            R.drawable.tab_tendencias_des,
            R.drawable.tab_programacion_des
    };
    private int[] tabIconsUser_Sel = {
            R.drawable.tab_perfil_emisora_sel,
            R.drawable.tab_chat_sel,
            R.drawable.tab_votar_sel,
            R.drawable.tab_tendencias_sel,
            R.drawable.tab_programacion_sel
    };
    private int[] tabIconsAdmin_Des = {
            R.drawable.tab_perfil_emisora_des,
            R.drawable.tab_chat_des,
            R.drawable.tab_estadisticas_des,
            R.drawable.tab_gps_des,
            R.drawable.tab_programacion_des
    };
    private int[] tabIconsAdmin_Sel = {
            R.drawable.tab_perfil_emisora_sel,
            R.drawable.tab_chat_sel,
            R.drawable.tab_estadisticas_sel,
            R.drawable.tab_gps_sel,
            R.drawable.tab_programacion_sel
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emisora);

        initToolbar();

        tabLayout = findViewById(R.id.appbartabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        cargarTabLayout();

        /*perfil = new PerfilEmisoraFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contenedor,perfil);
        fragmentTransaction.commit();*/

        PerfilEmisoraFragment perfil=new PerfilEmisoraFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contenedor,perfil);
        fragmentTransaction.commit();


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0:
                        if(!Usuario_Singleton.getInstance().isAdmin()) {
                            PerfilEmisoraFragment perfil = new PerfilEmisoraFragment();
                            fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.contenedor, perfil);
                            fragmentTransaction.commit();
                        }
                        else if (Usuario_Singleton.getInstance().isAdmin()){
                            PerfilEmisoraAdminFragment perfilAdmin = new PerfilEmisoraAdminFragment();
                            fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.contenedor, perfilAdmin);
                            fragmentTransaction.commit();
                        }
                        break;
                    case 1:
                        /*
                        fr2 fragmento2 = new fr2();

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.contenedor,fragmento2);
                        fragmentTransaction.commit();*/
                        break;
                    case 3:
                        if (Usuario_Singleton.getInstance().isAdmin()){
                            GPSFragment gpsFragment = new GPSFragment();

                            fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.contenedor, gpsFragment);
                            fragmentTransaction.commit();
                        }else{
                            TendenciasFragment tendenciasFragment = new TendenciasFragment();

                            fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.contenedor, tendenciasFragment);
                            fragmentTransaction.commit();
                        }
                        break;
                    case 4:
                        if (Usuario_Singleton.getInstance().isAdmin()){
                            ProgramacionAdminFragment programacionAdminFragment = new ProgramacionAdminFragment();

                            fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.contenedor, programacionAdminFragment);
                            fragmentTransaction.commit();
                        }
                        break;
                }
                cambiarIconoSeleccionado(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                cambiarIconoDeseleccionado(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void cargarTabLayout(){
        boolean admin = Usuario_Singleton.getInstance().isAdmin();
        for(int i = 0; i < 5; i++){
            if(admin){
                tabLayout.addTab(tabLayout.newTab().setText(tabTextAdmin[i]));
                if(i == 0){
                    tabLayout.getTabAt(0).setIcon(tabIconsAdmin_Sel[0]);
                }else{
                    tabLayout.getTabAt(i).setIcon(tabIconsAdmin_Des[i]);
                }
            }else{
                tabLayout.addTab(tabLayout.newTab().setText(tabTextUser[i]));
                if(i == 0){
                    tabLayout.getTabAt(0).setIcon(tabIconsUser_Sel[0]);
                }else{
                    tabLayout.getTabAt(i).setIcon(tabIconsUser_Des[i]);
                }
            }
        }
    }

    private void cambiarIconoSeleccionado(int pos){
        if(Usuario_Singleton.getInstance().isAdmin()){
            tabLayout.getTabAt(pos).setIcon(tabIconsAdmin_Sel[pos]);
        }else{
            tabLayout.getTabAt(pos).setIcon(tabIconsUser_Sel[pos]);
        }
    }

    private void cambiarIconoDeseleccionado(int pos) {
        if (Usuario_Singleton.getInstance().isAdmin()) {
            tabLayout.getTabAt(pos).setIcon(tabIconsAdmin_Des[pos]);
        } else {
            tabLayout.getTabAt(pos).setIcon(tabIconsUser_Des[pos]);
        }
    }
}
