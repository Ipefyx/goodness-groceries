package lu.uni.bicslab.greenbot.android.ui.activity.itemmain;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.other.Utils;
import lu.uni.bicslab.greenbot.android.ui.fragment.compare.CompareActivity;
import lu.uni.bicslab.greenbot.android.datamodel.ProductModel;
import lu.uni.bicslab.greenbot.android.ui.fragment.product_category.ProductCategoryModel;

public class ProductDetailsActivity extends AppCompatActivity {
	
	String productCode;
	TextView title, description, type_data, provider_data, category_data;
	ImageView product_image, category_icon;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_productdetails);
		
		title = findViewById(R.id.title);
		description = findViewById(R.id.description);
		type_data = findViewById(R.id.type_data);
		provider_data = findViewById(R.id.provider_data);
		category_data = findViewById(R.id.category_data);
		
		product_image = findViewById(R.id.product_image);
		category_icon = findViewById(R.id.category_icon);
		
		productCode = getIntent().getExtras().getString("code");
		
		Toolbar toolbar = findViewById(R.id.anim_toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.product);
		
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
		
//		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//		recyclerView.setLayoutManager(linearLayoutManager);
//		
//		CustomAdapter adapter = new CustomAdapter(this, product.indicators.stream().filter(ind -> ind.isApplicable() && ind.sub_indicators.size() > 0).collect(Collectors.toList()));
//		recyclerView.setAdapter(adapter);
		
		title.setText(product.getName());
		description.setText(product.getDescription());
		type_data.setText(product.getType());
		provider_data.setText(product.getProvider());
		category_data.setText(category.getName());
		
		Glide.with(this).load(Utils.getDrawableImage(this, product.getImage_url())).error(R.drawable.ic_menu_gallery).into(product_image);
		Glide.with(this).load(Utils.getDrawableImage(this, category.getIcon_name())).error(R.drawable.ic_menu_gallery).into(category_icon);
	}
}
