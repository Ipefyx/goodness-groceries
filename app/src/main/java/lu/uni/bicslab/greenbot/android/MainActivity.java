package lu.uni.bicslab.greenbot.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import lu.uni.bicslab.greenbot.android.other.ServerConnection;
import lu.uni.bicslab.greenbot.android.other.UserData;
import lu.uni.bicslab.greenbot.android.ui.activity.itemmain.ProductDetailsActivity;
import lu.uni.bicslab.greenbot.android.ui.activity.scan.BarcodeScannerActivity;



public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

	private AppBarConfiguration mAppBarConfiguration;
	private BottomNavigationView navigation;
	
	private String[] productsToReview;
	
	public static final int BARCODE_CAPTURE = 1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		
		mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.profileFragment, R.id.helpFragment).build();
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
		
		
		navigation = findViewById(R.id.bottomNavigation);
		navigation.setOnNavigationItemSelectedListener(this);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// Get list of bought products from the API and show the notification badge
		ServerConnection.fetchProductsBought(this, UserData.getID(this), products -> {
			productsToReview = products;
			BadgeDrawable badge = navigation.getOrCreateBadge(R.id.profile);
			
			if (productsToReview.length > 0) {
				badge.setVisible(true);
				badge.setNumber(productsToReview.length);
			} else {
				badge.setVisible(false);
			}
		}, error -> {});
		
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
				Intent i = new Intent(this, BarcodeScannerActivity.class);
				startActivityForResult(i, BARCODE_CAPTURE);
				return false;
				
			case R.id.profile:
				Bundle bundle = new Bundle();
				bundle.putStringArray("products_to_review", productsToReview);
				Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_global_profileFragment, bundle);
				return true;
			
			case R.id.help:
//				Toast.makeText(this, "not fully implemented yet", Toast.LENGTH_SHORT).show();
				Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_global_helpFragment);
				return true;
		}
		
		return false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == BARCODE_CAPTURE) {
			if (data != null) {
				String barcode = data.getStringExtra("barcode");
				Log.e("TAG", "Barcode read:final " + barcode);
				
				Intent intent = new Intent(this, ProductDetailsActivity.class);
				intent.putExtra("code", barcode);
				startActivity(intent);
			} else {
				Log.e("TAG", "No barcode captured, intent data is null");
			}
			
		}
	}
}
