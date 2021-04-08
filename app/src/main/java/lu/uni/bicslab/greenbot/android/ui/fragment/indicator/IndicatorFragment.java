package lu.uni.bicslab.greenbot.android.ui.fragment.indicator;

import android.app.SearchManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.other.Utils;

public class IndicatorFragment extends Fragment {
	private RecyclerView recyclerView;
	
	private IndicatorAdapter itemAdapter;
	private String indicatorID;
	SearchManager searchManager;
	SearchView searchView;
	TextView textviewloading;
	List<ProductModel> list;
	
	public View onCreateView(@NonNull LayoutInflater inflater,
									 ViewGroup container, Bundle savedInstanceState) {
		
		View root = inflater.inflate(R.layout.fragment_indicator, container, false);
		recyclerView = root.findViewById(R.id.indicator_view);
		textviewloading = root.findViewById(R.id.textviewloading);
		//searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
		searchView = root.findViewById(R.id.search_src_text);
		searchView.setMaxWidth(Integer.MAX_VALUE);
		try {
			indicatorID = getActivity().getIntent().getExtras().getString("value");
		} catch (Exception e) {
			indicatorID = Utils.id;
		}
		list = new ArrayList<ProductModel>();
		itemAdapter = new IndicatorAdapter();
		list = fillDummyData();
		
		
		itemAdapter.setMovieList(getActivity(), list);
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(mLayoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.setAdapter(itemAdapter);
		
		// listening to search query text change
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				// filter recycler view when query submitted
				itemAdapter.getFilter().filter(query);
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String query) {
				// filter recycler view when text is changed
				itemAdapter.getFilter().filter(query);
				return false;
			}
		});
		
		return root;
	}
	
	private List<ProductModel> fillDummyData() {
		textviewloading.setText(R.string.loading);
		
		String jsonFileStringIndicator = Utils.getJsonFromAssets(getActivity(), "indicators.json");
		String jsonFileStringProduct = Utils.getJsonFromAssets(getActivity(), "products.json");
		
		Gson gson = new Gson();
		Type listUserTypeIndicator = new TypeToken<List<IndicatorModel>>() {
		}.getType();
		Type listUserTypeProduct = new TypeToken<List<ProductModel>>() {
		}.getType();
		
		List<IndicatorModel> indicatorCategoryList = gson.fromJson(jsonFileStringIndicator, listUserTypeIndicator);
		List<ProductModel> productList = gson.fromJson(jsonFileStringProduct, listUserTypeProduct);
		
		// Feels hacky, but gets the job done (kinda had to work with the existing stuff)
		for (ProductModel product : productList) {
			for (int i = 0; i < product.indicators.size(); i++) {
				String ind_id = product.indicators.get(i).getIndicator_id();
				String ind_desc = product.indicators.get(i).getIndicator_description();
				
				Optional<IndicatorModel> matchingIndicator = indicatorCategoryList.stream().filter(ind -> ind.id.equals(ind_id)).findFirst();
				if (matchingIndicator.isPresent()) {
					product.indicators.set(i, matchingIndicator.get());
					product.indicators.get(i).setIndicator_description(ind_desc);
				}
			}
		}
		
		//TODO: Fix this filter code
//		List<IndicatorModel> indicatorlist = new ArrayList<IndicatorModel>();
//		for (IndicatorModel c : indicatorCategoryList) {
//			if (indicatorlist.equals(c.getCategory_id())) {
//				indicatorlist.add(c);
//			}
//		}
//		
//		List<ProductModel> productfinallist = new ArrayList<ProductModel>();
//		for (ProductModel c : productList) {
//			boolean added = false;
//			Log.e("product", "" + c.getIndicators());
//			for (IndicatorModel indicator : c.getIndicators()) {
//				for (IndicatorModel indicatormain : indicatorCategoryList) {
//					Log.e("indicator", "" + indicator.getCategory_id());
//					if ((indicator.getIndicator_idForProduct().equals(indicatormain.getId())) &&
//							(indicatormain.getCategory_id().equals(indicatorID))) {
//						productfinallist.add(c);
//						added = true;
//						break;
//					}
//					if (added) break;
//				}
//				if (added) break;
//			}
//		}
		
		if (productList.size() > 0) {
			textviewloading.setVisibility(View.GONE);
			searchView.setVisibility(View.VISIBLE);
		} else {
			textviewloading.setVisibility(View.VISIBLE);
			textviewloading.setText(R.string.nodata);
			searchView.setVisibility(View.GONE);
		}
		
		return productList;

//		Log.e("productfinallist", "" + productfinallist);
//		return productfinallist;
		
	}
}

