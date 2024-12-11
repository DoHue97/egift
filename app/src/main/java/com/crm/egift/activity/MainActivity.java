package com.crm.egift.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.crm.egift.R;
import com.crm.egift.storage.Storage;
import com.crm.egift.utils.CustomDialog;
import com.crm.egift.utils.LanguageUtil;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DrawerLayout drawerLayout = findViewById(R.id.main);
        NavigationView navigationView = findViewById(R.id.nav_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        toolbar.setNavigationIcon(R.drawable.ic_home);
        toolbar.setNavigationOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.app_bar_switch) {
                onChangeLanguage();
            }
            return true;
        });

        Menu menu = navigationView.getMenu();
        MenuItem userMenu = menu.findItem(R.id.nav_user);
        userMenu.setTitle(Storage.getUserFullname(this));
        MenuItem logoutMenu = menu.findItem(R.id.nav_logout);
        MenuItem switchBusinessMenu = menu.findItem(R.id.nav_switch_business);
        logoutMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                new CustomDialog(MainActivity.this, new CustomDialog.OKListener() {
                    @Override
                    public void onOk() {
                        onbtnLogout();
                    }
                    @Override
                    public void onCancel() {

                    }
                }, getString(R.string.logout_msg), getString(R.string.menu_logout), getString(R.string.cancel),false).show();
                return false;
            }
        });

        switchBusinessMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(MainActivity.this, SwitchBusinessActivity.class);
                intent.putExtra("from_home", true);
                startActivity(intent);
                finish();
                return false;
            }
        });
    }

    private void onbtnLogout() {
        Storage.setToken(MainActivity.this, "");
        Storage.setOrganisations(MainActivity.this, "");
        Storage.setBusiness(MainActivity.this, "");
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onChangeLanguage() {
        String language = LanguageUtil.getCurrentLanguage(MainActivity.this);
        if(language == "el") {
            LanguageUtil.setLanguage(MainActivity.this, "en");
        }else {
            LanguageUtil.setLanguage(MainActivity.this, "el");
        }
        LanguageUtil.initLanguage(MainActivity.this);
        MainActivity.this.recreate();
    }
}