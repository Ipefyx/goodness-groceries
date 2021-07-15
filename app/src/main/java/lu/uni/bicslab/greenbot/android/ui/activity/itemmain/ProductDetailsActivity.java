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
	
	private CollapsingToolbarLayout collapsingToolbar;
	private AppBarLayout appBarLayout;
	private RecyclerView recyclerView;
	
	private CustomAdapter adapter;
	
	private Menu collapsedMenu;
	private boolean appBarExpanded = true;
	
	String productCode;
	
	TextView type_data, description, type_category, type_provider;
	ImageView header, img_compare;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_productdetails);
		recyclerView = findViewById(R.id.scrollableview);
		type_data = findViewById(R.id.type_data);
		description = findViewById(R.id.description);
		type_category = findViewById(R.id.type_category);
		type_provider = findViewById(R.id.type_provider);
		header = findViewById(R.id.header);
		img_compare = findViewById(R.id.img_compare);
		//  Use when your list size is constant for better performance
		recyclerView.setHasFixedSize(true);
		
		productCode = getIntent().getExtras().getString("code");
		
		final Toolbar toolbar = findViewById(R.id.anim_toolbar);
		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null)
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		appBarLayout = findViewById(R.id.appbar);
		
		collapsingToolbar = findViewById(R.id.collapsing_toolbar);
		
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.header);
		
		Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
			@SuppressWarnings("ResourceType")
			@Override
			public void onGenerated(Palette palette) {
				int vibrantColor = palette.getVibrantColor(R.color.blue);
				collapsingToolbar.setContentScrimColor(vibrantColor);
				collapsingToolbar.setStatusBarScrimColor(R.color.gray_dark);
			}
		});
		
		
		img_compare.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//finish();
				Intent mIntent = new Intent(getApplicationContext(), CompareActivity.class);
				mIntent.putExtra("key_product", productCode);
				startActivity(mIntent);
				
			}
		});
		appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
			@Override
			public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
				Log.d(ProductDetailsActivity.class.getSimpleName(), "onOffsetChanged: verticalOffset: " + verticalOffset);
				
				//  Vertical offset == 0 indicates appBar is fully expanded.
				if (Math.abs(verticalOffset) > 200) {
					appBarExpanded = false;
					invalidateOptionsMenu();
				} else {
					appBarExpanded = true;
					invalidateOptionsMenu();
				}
			}
		});
		
		
		setView(Utils.getProductByCode(this, productCode));
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (collapsedMenu != null
				&& (!appBarExpanded || collapsedMenu.size() != 1)) {
			//collapsed
			collapsedMenu.add("Add")
					.setIcon(R.drawable.ic_action_add)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		} else {
			//expanded
		}
		return super.onPrepareOptionsMenu(collapsedMenu);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		collapsedMenu = menu;
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			case R.id.action_settings:
				return true;
		}
		if (item.getTitle() == "Add") {
			Toast.makeText(this, "clicked add", Toast.LENGTH_SHORT).show();
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void setView(ProductModel product) {
		ProductCategoryModel category = Utils.getProductCategoryByID(this, product.getCategory());
		
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(linearLayoutManager);
		
		adapter = new CustomAdapter(this, product.indicators.stream().filter(ind -> ind.isApplicable() && ind.sub_indicators.size() > 0).collect(Collectors.toList()));
		
		recyclerView.setAdapter(adapter);
		collapsingToolbar.setTitle(product.getName());
		
		type_data.setText(product.getType());
		description.setText(product.getDescription());
		type_category.setText(category.getName());
		type_provider.setText(product.getProvider());
		header = findViewById(R.id.header);
		Glide.with(getApplicationContext()).load(product.getImage_url()).apply(RequestOptions.centerCropTransform()).into(header);
	}
}
