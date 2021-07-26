package lu.uni.bicslab.greenbot.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import lu.uni.bicslab.greenbot.android.ui.activity.scanitem.ScanSelectedItemActivity;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {
	
	private AppBarConfiguration mAppBarConfiguration;
	TextView gallery;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		
		mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.profileFragment, R.id.helpFragment).build();
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
		
		//getting bottom navigation view and attaching the listener
		BottomNavigationView navigation = findViewById(R.id.bottomNavigation);
		navigation.setOnNavigationItemSelectedListener(this);
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onSupportNavigateUp() {
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		return NavigationUI.navigateUp(navController, mAppBarConfiguration)
				|| super.onSupportNavigateUp();
	}
	
	
	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId()) {
			case R.id.homemain:
				Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_global_nav_home);
				return true;
				
			case R.id.scanner:
				Intent i = new Intent(this, ScanSelectedItemActivity.class);
				startActivity(i);
				return false;
				
			case R.id.profile:
				Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_global_profileFragment);
				return true;
			
			case R.id.help:
//				Toast.makeText(this, "not fully implemented yet", Toast.LENGTH_SHORT).show();
				Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_global_helpFragment);
				return true;
		}
		
		return false;
	}
}
