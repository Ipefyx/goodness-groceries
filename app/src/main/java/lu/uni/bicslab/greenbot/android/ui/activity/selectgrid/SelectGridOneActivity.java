package lu.uni.bicslab.greenbot.android.ui.activity.selectgrid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.other.CustomGridAdapter;
import lu.uni.bicslab.greenbot.android.ui.activity.onbord.OnbordSelectable;

public class SelectGridOneActivity extends AppCompatActivity implements SelectionActionCompleteListner {
	
	private static RecyclerView.Adapter adapter;
	private static RecyclerView recyclerView;
	private static ArrayList<OnbordSelectable> data;
	private GridLayoutManager gridLayoutManager;
	private Button btn_start;
	JSONObject jsonObject;
	SelectionActionCompleteListner mSelectionActionCompleteListner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onbording_select_one_layout);
		
		String datajson = getIntent().getStringExtra("data");
		try {
			jsonObject = new JSONObject(datajson);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mSelectionActionCompleteListner = this;
		
		recyclerView = findViewById(R.id.my_recycler_view);
		btn_start = findViewById(R.id.btn_start);
		recyclerView.setHasFixedSize(true);
		
		gridLayoutManager = new GridLayoutManager(this, 2);
		recyclerView.setLayoutManager(gridLayoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		
		data = new ArrayList<OnbordSelectable>();
//		for (int i = 0; i < OnbordSelectable.getDescription(getApplicationContext()).length; i++) {
//			data.add(new SelectLocalImportModel(SelectLocalImportModel.getTitle(getApplicationContext())[i],
//					SelectLocalImportModel.getImage(getApplicationContext())[i], false
//			
//			));
//		}
		btn_start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				Intent intent = new Intent(SelectGridOneActivity.this, SelectIGridTwoActivity.class);
				intent.putExtra("data", onJsonObjectSet(-1).toString());//
				startActivity(intent);
			}
		});
		adapter = new CustomGridAdapter(getApplicationContext(), data, mSelectionActionCompleteListner);
		recyclerView.setAdapter(adapter);
		
	}
	
	@Override
	public void onSelectionCompleted(int position) {
		onJsonObjectSet(position);
	}
	
	private JSONObject onJsonObjectSet(int position) {
		JSONObject object = new JSONObject();
		try {
			//input your API parameters
			object.put("participant_id", jsonObject.get("participant_id"));
			if (position == 0) {
				object.put("product_category_1", getResources().getString(R.string.local_organic));
			} else {
				object.put("product_category_1", jsonObject.get("product_category_1"));
			}
			if (position == 1) {
				object.put("product_category_2", getResources().getString(R.string.local_conventional));
			} else {
				object.put("product_category_2", jsonObject.get("product_category_2"));
			}
			if (position == 2) {
				object.put("product_category_3", getResources().getString(R.string.imported_organic));
			} else {
				object.put("product_category_3", jsonObject.get("product_category_3"));
			}
			if (position == 3) {
				object.put("product_category_4", getResources().getString(R.string.imported_conventional));
			} else {
				object.put("product_category_4", jsonObject.get("product_category_4"));
			}
			object.put("indicator_category_1", jsonObject.get("indicator_category_1"));
			object.put("indicator_category_2", jsonObject.get("indicator_category_2"));
			object.put("indicator_category_3", jsonObject.get("indicator_category_3"));
			object.put("indicator_category_4", jsonObject.get("indicator_category_4"));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}
}