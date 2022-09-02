package lu.uni.bicslab.greenbot.android.ui.activity.itemmain;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.stream.Collectors;

import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.datamodel.IndicatorModel;
import lu.uni.bicslab.greenbot.android.datamodel.SubIndicatorModel;
import lu.uni.bicslab.greenbot.android.other.Utils;
import lu.uni.bicslab.greenbot.android.ui.fragment.compare.CompareActivity;
import lu.uni.bicslab.greenbot.android.datamodel.ProductModel;
import lu.uni.bicslab.greenbot.android.datamodel.ProductCategoryModel;
import lu.uni.bicslab.greenbot.android.ui.fragment.compare.CompareActivity2;

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
	
	boolean slideTransition = false;

	Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContext = this;

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
		slideTransition = getIntent().getExtras().getBoolean("slide_transition", false);
		
		ProductModel product = Utils.getProductByCode(this, productCode);
		
		if (productCode == null || product == null) {
			Toast toast = Toast.makeText(getApplicationContext(), product == null ? R.string.product_not_found : R.string.general_error, Toast.LENGTH_SHORT);
			toast.show();
			finish();
			return;
		}
		
		Toolbar toolbar = findViewById(R.id.anim_toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.product);
		
		setView(product);
		
		updateHiddenIndicators();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (slideTransition)
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		
		menu.add(R.string.compare_button)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				if (slideTransition)
					overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				return true;
			case 0: // Start compare activity
				Intent mIntent = new Intent(getApplicationContext(), CompareActivity2.class);
				ProductModel product = Utils.getProductByCode(this, productCode);
				mIntent.putExtra("key_product", product);
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
		List<IndicatorModel> indicators = product.getFeaturedIndicators();

		title.setText(product.getName());
		description.setText(product.getDescription());
		type_data.setText(product.getType());
		provider_data.setText(product.getProvider());
		category_data.setText(category.getName());

		// Product picture
		Glide.with(this).load(Utils.getDrawableImage(this, product.getImage_url())).error(R.drawable.ic_menu_gallery).into(product_image);


		product_image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showImage(Utils.getDrawableImage(mContext, product.getImage_url()));
			}
		});

		// Category picture
		Glide.with(this).load(Utils.getDrawableImage(this, category.getIcon_name())).error(R.drawable.ic_menu_gallery).into(category_icon);

		// Product category info popup
		category_icon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AlertDialog.Builder infoPopup = new AlertDialog.Builder(mContext);
				infoPopup.setTitle(category.getName());

				String content = new String();
				content += "<div><p>" + category.getDescription() + "</p></div>";

				infoPopup.setMessage(Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY));
				infoPopup.setPositiveButton("OK", null);
				infoPopup.show();
			}
		});


		
		if (indicators.size() == 0) {
			indicators_section.setVisibility(View.GONE);
		}
		
		// Fill in indicators
		// If the category id matches the search filter, put in indicator_list1, else in indicator_list2 (collapsible)
		for (IndicatorModel ind : indicators) {
			View view = this.getLayoutInflater().inflate(R.layout.item_row, indicator_list1, false);

			((TextView) view.findViewById(R.id.indicator_name)).setText(ind.getName());
			Glide.with(this).load(Utils.getDrawableImage(this, ind.getIcon_name())).error(R.drawable.ic_menu_gallery).into((ImageView) view.findViewById(R.id.indicator_image));

			((TextView)view.findViewById(R.id.indicator_info)).setOnClickListener( new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						ProductIndicatorInfoPopup infoPopup = new ProductIndicatorInfoPopup(mContext, ind);
						infoPopup.build();

						/*AlertDialog.Builder infoPopup = new AlertDialog.Builder(mContext);
						infoPopup.setTitle(ind.getName());

						String content = new String();

						for (SubIndicatorModel si : ind.sub_indicators) {
							content += "<div><p><b>" + si.getName() +"</b><br>";
							content += si.getDescription() + "</p>";
							if(si.getFile() != null) {
								String link = "<a style=\"text-align: right\" href=\"" + si.getFile() + "\">More info...</a>";
								content +=  link + "</div>";
							}
						}

						infoPopup.setMessage(Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY));
						infoPopup.setPositiveButton("OK", null);
						infoPopup.show();
						*/
					}
				}
			);

			//indicator_list1.addView(view);

			if (ind.getCategory_id().equals(indicatorCategoryFilter) || indicatorCategoryFilter == null) {
				indicator_list1.addView(view);
			} else {
				indicator_list2.addView(view);
				
				show_more.setVisibility(View.VISIBLE);
			}
		}
		
		// Fill in similar products
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

	public void showImage(Drawable image) {
		Dialog builder = new Dialog(this);
		builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
		builder.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialogInterface) {
				//nothing;
			}
		});
		ImageView imageView = new ImageView(this);
		imageView.setImageDrawable(image);
		builder.addContentView(imageView, new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		builder.show();
	}
}
