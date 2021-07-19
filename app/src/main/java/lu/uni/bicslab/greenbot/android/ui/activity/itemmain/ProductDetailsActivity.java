package lu.uni.bicslab.greenbot.android.ui.activity.itemmain;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.stream.Collectors;

import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.datamodel.IndicatorModel;
import lu.uni.bicslab.greenbot.android.other.Utils;
import lu.uni.bicslab.greenbot.android.ui.fragment.compare.CompareActivity;
import lu.uni.bicslab.greenbot.android.datamodel.ProductModel;
import lu.uni.bicslab.greenbot.android.ui.fragment.product_category.ProductCategoryModel;

public class ProductDetailsActivity extends AppCompatActivity {
	
	String productCode;
	String indicatorCategoryFilter;
	
	TextView title, description, type_data, provider_data, category_data;
	ImageView product_image, category_icon;
	
	LinearLayout indicator_list1, indicator_list2;
	View indicators_section, show_more;
	TextView show_more_text;
	ImageView show_more_icon;
	
	LinearLayout similar_product_list;
	
	boolean indicatorsExpanded = false;
	
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
		
		indicators_section = findViewById(R.id.indicators_section);
		
		indicator_list1 = findViewById(R.id.indicator_list1);
		indicator_list2 = findViewById(R.id.indicator_list2);
		
		show_more = findViewById(R.id.show_more);
		show_more_text = findViewById(R.id.show_more_text);
		show_more_icon = findViewById(R.id.show_more_icon);
		
		// Set GONE by default, make VISIBLE when at least one indicator is added
		show_more.setVisibility(View.GONE);
		show_more.setOnClickListener(v -> {
			indicatorsExpanded = !indicatorsExpanded;
			updateHiddenIndicators();
		});
		
		similar_product_list = findViewById(R.id.similar_product_list);
		
		productCode = getIntent().getExtras().getString("code");
		indicatorCategoryFilter = getIntent().getExtras().getString("filter_indicator_category");
		
		Toolbar toolbar = findViewById(R.id.anim_toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.product);
		
		setView(Utils.getProductByCode(this, productCode));
		
		updateHiddenIndicators();
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
	
	private void updateHiddenIndicators() {
		if (indicatorsExpanded) {
			show_more_text.setText(R.string.show_less);
			indicator_list2.setVisibility(View.VISIBLE);
			show_more_icon.setRotation(90);
		} else {
			show_more_text.setText(R.string.show_more);
			indicator_list2.setVisibility(View.GONE);
			show_more_icon.setRotation(0);
		}
	}
	
	private void setView(ProductModel product) {
		ProductCategoryModel category = Utils.getProductCategoryByID(this, product.getCategory());
		List<IndicatorModel> indicators = product.indicators.stream().filter(ind -> ind.isApplicable() && ind.sub_indicators.size() > 0).collect(Collectors.toList());
		
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
		
		if (indicators.size() == 0) {
			indicators_section.setVisibility(View.GONE);
		}
		
		for (IndicatorModel ind : indicators) {
			View view = this.getLayoutInflater().inflate(R.layout.item_row, indicator_list1, false);
			
			((TextView) view.findViewById(R.id.indicator_name)).setText(ind.getName());
			Glide.with(this).load(Utils.getDrawableImage(this, ind.getIcon_name())).error(R.drawable.ic_menu_gallery).into((ImageView) view.findViewById(R.id.indicator_image));
			
			if (ind.getCategory_id().equals(indicatorCategoryFilter)) {
				indicator_list1.addView(view);
			} else {
				indicator_list2.addView(view);
				
				show_more.setVisibility(View.VISIBLE);
			}
		}
		
		for (ProductModel simProduct : Utils.getProductsByType(this, product.getType())) {
			if (simProduct.getCode().equals(productCode))
				continue;
			
			View view = this.getLayoutInflater().inflate(R.layout.item_collumn, similar_product_list, false);
			String catIcon = Utils.getProductCategoryByID(this, simProduct.getCategory()).getIcon_name();
			
			((TextView) view.findViewById(R.id.txtName)).setText(simProduct.getName());
			Glide.with(this).load(Utils.getDrawableImage(this, simProduct.getImage_url())).error(R.drawable.ic_menu_gallery).into((ImageView) view.findViewById(R.id.imageview_icon));
			Glide.with(this).load(Utils.getDrawableImage(this, catIcon)).error(R.drawable.ic_menu_gallery).into((ImageView) view.findViewById(R.id.imageview_origin));
			
			view.setOnClickListener(v -> {
				Intent intent = new Intent(this, ProductDetailsActivity.class);
				intent.putExtra("code", simProduct.getCode());
				intent.putExtra("filter_indicator_category", indicatorCategoryFilter);
				startActivity(intent);
			});
			
			similar_product_list.addView(view);
		}
	}
}
