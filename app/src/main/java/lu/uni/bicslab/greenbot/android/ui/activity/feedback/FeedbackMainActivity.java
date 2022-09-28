package lu.uni.bicslab.greenbot.android.ui.activity.feedback;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.datamodel.IndicatorModel;
import lu.uni.bicslab.greenbot.android.datamodel.ProductModel;
import lu.uni.bicslab.greenbot.android.other.ServerConnection;
import lu.uni.bicslab.greenbot.android.other.UserData;
import lu.uni.bicslab.greenbot.android.other.Utils;
import lu.uni.bicslab.greenbot.android.ui.fragment.profile.ProfileFragment;

public class FeedbackMainActivity extends AppCompatActivity {
	
	private String productID;
	private List<SelectableFeedback> selectedCheckboxes = new ArrayList<>();
	
	public CheckBox price_checkbox;
	public CheckBox other_reason_checkbox;
	
	private class SelectableFeedback {
		public CompoundButton checkbox;
		public String indicator;
		
		public SelectableFeedback(CompoundButton checkbox, String indicator) {
			this.checkbox = checkbox;
			this.indicator = indicator;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback_main);
		
		getSupportActionBar().setTitle("Feedback");
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setDisplayShowHomeEnabled(true);
		
		productID = getIntent().getStringExtra("product_id");
		
		TextView txtName = findViewById(R.id.txtName);
		ConstraintLayout indicator_layout = findViewById(R.id.indicator_layout);
		Flow indicator_flow = findViewById(R.id.indicator_flow);
		ImageView imageview_icon = findViewById(R.id.imageview_icon);
		ImageView imageview_origin = findViewById(R.id.imageview_origin);
		
		LinearLayout indicators = findViewById(R.id.indicators);
		price_checkbox = findViewById(R.id.price_checkbox);
		other_reason_checkbox = findViewById(R.id.other_reason_checkbox);
		EditText other_reason_text = findViewById(R.id.other_reason_text);
		
		Button btn_next = findViewById(R.id.btn_next);
		
		SelectableFeedback s1 = new SelectableFeedback(price_checkbox, null);
		price_checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
			onCheckedChange(s1, isChecked);
		});
		
		SelectableFeedback s2 = new SelectableFeedback(other_reason_checkbox, null);
		other_reason_checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
			other_reason_text.setEnabled(isChecked);
			onCheckedChange(s2, isChecked);
		});

		// Fill in the product info, below code taken partially from IndicatorAdapter
		ProductModel product = Utils.getProductByCode(this, productID);

		try {
			txtName.setText(product.getName());
		} catch (NullPointerException e) {
			Snackbar.make(findViewById(android.R.id.content), "Invalid product!", Snackbar.LENGTH_INDEFINITE).show();
			//finish();
			return;
		}

		// Assume Flow is element 0 and remove all of the ImageViews that may already be there
		indicator_layout.removeViewsInLayout(1, indicator_layout.getChildCount()-1);
		
		for (IndicatorModel ind : product.indicators) {
			if (!ind.isApplicable() /*|| ind.sub_indicators.size() == 0*/)
				continue;
			
			// Inflate the indicator icons on the product
			ImageView imageview = (ImageView) LayoutInflater.from(this).inflate(R.layout.indicator_item_layout_indicator_imageview, indicator_layout, false);
			Glide.with(this).load(Utils.getDrawableImage(this, ind.getIcon_name())).into(imageview);
			imageview.setId(View.generateViewId());
			indicator_layout.addView(imageview);
			indicator_flow.addView(imageview);
			
			// Inflate the indicator checkboxes in the LinearLayout
			View indicatorRow = LayoutInflater.from(this).inflate(R.layout.feedback_indicator_row, indicators, false);
			Glide.with(this).load(Utils.getDrawableImage(this, ind.getIcon_name())).into((ImageView) indicatorRow.findViewById(R.id.imageview_indicator));
			
			CheckBox indicator_checkbox = indicatorRow.findViewById(R.id.indicator_checkbox);
			indicator_checkbox.setText(ind.getName());
			
			SelectableFeedback selectable = new SelectableFeedback(indicator_checkbox, ind.getId());
			indicator_checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
				// ! Is also called when programmatically calling setChecked()
				onCheckedChange(selectable, isChecked);
			});
			
			indicators.addView(indicatorRow);
		}
		
		Glide.with(this).load(Utils.getDrawableImage(this, product.getImage_url())).error(R.drawable.ic_menu_gallery).into(imageview_icon);
		Glide.with(this).load(Utils.getDrawableImage(this, product.getCategory())).error(R.drawable.ic_menu_gallery).into(imageview_origin);
		
		btn_next.setOnClickListener(v -> {
			ServerConnection.sendProductFeedback(this, UserData.getID(this), productID,
					selectedCheckboxes.size() > 0 ? selectedCheckboxes.get(0).indicator : null,
					selectedCheckboxes.size() > 1 ? selectedCheckboxes.get(1).indicator : null,
					other_reason_text.getText().toString(), price_checkbox.isChecked(), object -> {
						setResult(ProfileFragment.FEEDBACK_RESULT_OK);
						finish();
					}, error -> {
						if (error instanceof TimeoutError || error instanceof NoConnectionError) {
							Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(this, R.string.general_error_try_again, Toast.LENGTH_SHORT).show();
						}
					});
		});
	}
	
	private void onCheckedChange(SelectableFeedback selectable, boolean isChecked) {
		if (isChecked) {
			selectedCheckboxes.add(selectable);
			
			if (selectedCheckboxes.size() > 2) {
				selectedCheckboxes.get(0).checkbox.setChecked(false);
			}
		} else {
			selectedCheckboxes.remove(selectable);
		}
		
		Log.i("CHECK", ""+selectedCheckboxes.size());
	}
}