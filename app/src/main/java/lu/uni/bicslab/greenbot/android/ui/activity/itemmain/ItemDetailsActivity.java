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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.other.CustomAdapter;
import lu.uni.bicslab.greenbot.android.other.Utils;
import lu.uni.bicslab.greenbot.android.ui.fragment.compare.CompareActivity;
import lu.uni.bicslab.greenbot.android.ui.fragment.indicator.IndicatorModel;
import lu.uni.bicslab.greenbot.android.ui.fragment.indicator.ProductModel;

public class ItemDetailsActivity extends AppCompatActivity {
	
	private CollapsingToolbarLayout collapsingToolbar;
	private AppBarLayout appBarLayout;
	private RecyclerView recyclerView;
	
	private CustomAdapter adapter;
	
	private Menu collapsedMenu;
	private boolean appBarExpanded = true;
	String value;
	ProductModel productmodel;
	TextView type_data, description, type_category, type_provider;
	ImageView header, img_compare;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_itemdetails);
		recyclerView = findViewById(R.id.scrollableview);
		type_data = findViewById(R.id.type_data);
		description = findViewById(R.id.description);
		type_category = findViewById(R.id.type_category);
		type_provider = findViewById(R.id.type_provider);
		header = findViewById(R.id.header);
		img_compare = findViewById(R.id.img_compare);
		//  Use when your list size is constant for better performance
		recyclerView.setHasFixedSize(true);
		
		String title = getIntent().getExtras().getString("title");
		value = getIntent().getExtras().getString("code");
		
		final Toolbar toolbar = findViewById(R.id.anim_toolbar);
		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null)
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		appBarLayout = findViewById(R.id.appbar);
		
		collapsingToolbar = findViewById(R.id.collapsing_toolbar);
		collapsingToolbar.setTitle(title);
		
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
				mIntent.putExtra("key_product", productmodel);
				startActivity(mIntent);
				
			}
		});
		appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
			@Override
			public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
				Log.d(ItemDetailsActivity.class.getSimpleName(), "onOffsetChanged: verticalOffset: " + verticalOffset);
				
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
		
		
		setView(getIndicators());
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
	
	private List<IndicatorModel> getIndicators() {
		
		List<IndicatorModel> indicatorList = Utils.getIndicatorList(getApplicationContext());
		List<ProductModel> productList = Utils.getProductList(getApplicationContext());
		
		
		Optional<ProductModel> matchingProduct = productList.stream().filter(product -> product.code.equals(value)).findFirst();
		if (matchingProduct.isPresent()) {
			productmodel = matchingProduct.get();
			
			// Copied from IndicatorFragment.java because it serves the same purpose
			for (int i = 0; i < productmodel.indicators.size(); i++) {
				String ind_id = productmodel.indicators.get(i).getIndicator_id();
				String ind_desc = productmodel.indicators.get(i).getIndicator_description();
				
				Optional<IndicatorModel> matchingIndicator = indicatorList.stream().filter(ind -> ind.id.equals(ind_id)).findFirst();
				if (matchingIndicator.isPresent()) {
					productmodel.indicators.set(i, matchingIndicator.get());
					productmodel.indicators.get(i).setIndicator_description(ind_desc);
				}
			}
		}
		
		return productmodel.getIndicators();
	}
	
	private void setView(List<IndicatorModel> indicatorModel) {
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(linearLayoutManager);
		
		adapter = new CustomAdapter(this, indicatorModel);
		recyclerView.setAdapter(adapter);
		
		type_data.setText(productmodel.getType());
		description.setText(productmodel.getDescription());
		type_category.setText(productmodel.getProd_cat_icon());
		type_provider.setText(productmodel.getProvider());
		header = findViewById(R.id.header);
		Glide.with(getApplicationContext()).load(productmodel.getImage_url()).apply(RequestOptions.centerCropTransform()).into(header);
	}
}
