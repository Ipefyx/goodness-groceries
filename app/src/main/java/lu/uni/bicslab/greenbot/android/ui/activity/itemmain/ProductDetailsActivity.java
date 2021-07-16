package lu.uni.bicslab.greenbot.android.ui.activity.itemmain;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.palette.graphics.Palette;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.other.CustomAdapter;
import lu.uni.bicslab.greenbot.android.other.Utils;
import lu.uni.bicslab.greenbot.android.ui.fragment.compare.CompareActivity;
import lu.uni.bicslab.greenbot.android.datamodel.IndicatorModel;
import lu.uni.bicslab.greenbot.android.datamodel.ProductModel;
import lu.uni.bicslab.greenbot.android.ui.fragment.product_category.ProductCategoryModel;

public class ProductDetailsActivity extends AppCompatActivity {
	
	private RecyclerView recyclerView;
	
	String productCode;
	TextView type_data, description, type_category, type_provider;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_productdetails);
		recyclerView = findViewById(R.id.scrollableview);
		type_data = findViewById(R.id.type_data);
		description = findViewById(R.id.description);
		type_category = findViewById(R.id.type_category);
		type_provider = findViewById(R.id.type_provider);
		
		//  Use when your list size is constant for better performance
		recyclerView.setHasFixedSize(true);
		
		productCode = getIntent().getExtras().getString("code");
		
		Toolbar toolbar = findViewById(R.id.anim_toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		setView(Utils.getProductByCode(this, productCode));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		
		menu.add(0, 0, 0, R.string.compare_button)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			case 0:
				Intent mIntent = new Intent(getApplicationContext(), CompareActivity.class);
				mIntent.putExtra("key_product", productCode);
				startActivity(mIntent);
				return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void setView(ProductModel product) {
		ProductCategoryModel category = Utils.getProductCategoryByID(this, product.getCategory());
		
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(linearLayoutManager);
		
		CustomAdapter adapter = new CustomAdapter(this, product.indicators.stream().filter(ind -> ind.isApplicable() && ind.sub_indicators.size() > 0).collect(Collectors.toList()));
		recyclerView.setAdapter(adapter);
		
		getSupportActionBar().setTitle(product.getName());
		
		type_data.setText(product.getType());
		description.setText(product.getDescription());
		type_category.setText(category.getName());
		type_provider.setText(product.getProvider());
	}
}
