package lu.uni.bicslab.greenbot.android.ui.fragment.indicator;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.datamodel.IndicatorModel;
import lu.uni.bicslab.greenbot.android.datamodel.ProductModel;
import lu.uni.bicslab.greenbot.android.other.Utils;
import lu.uni.bicslab.greenbot.android.ui.activity.itemmain.ItemDetailsActivity;

public class IndicatorFragment extends Fragment {
	private RecyclerView recyclerView;
	
	private IndicatorAdapter itemAdapter;
	
	private String indicatorCategoryFilter;
	private String productCategoryFilter;
	
	SearchManager searchManager;
	SearchView searchView;
	TextView textviewloading;
	List<ProductModel> list;
	
	public View onCreateView(@NonNull LayoutInflater inflater,
									 ViewGroup container, Bundle savedInstanceState) {
		
		View root = inflater.inflate(R.layout.fragment_items_main_layout, container, false);
		recyclerView = root.findViewById(R.id.indicator_view);
		textviewloading = root.findViewById(R.id.textviewloading);
		//searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
		searchView = root.findViewById(R.id.search_src_text);
		searchView.setMaxWidth(Integer.MAX_VALUE);
		
		indicatorCategoryFilter = getArguments().getString("filter_indicator_category");
		productCategoryFilter = getArguments().getString("filter_product_category");
		
		itemAdapter = new IndicatorAdapter(getActivity(), code -> {
				Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
				intent.putExtra("code", code);
				startActivity(intent);
		});
		
		itemAdapter.setMovieList(fillData());
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
	
	private List<ProductModel> fillData() {
		textviewloading.setText(R.string.loading);
		
		Log.i("product receive", indicatorCategoryFilter + " " + productCategoryFilter);
		
		List<ProductModel> productList = Utils.getProductList(getActivity());
		
		// Filters the product list according to this logic:
		// For each PRODUCT: (there exists at least one INDICATOR from PRODUCT.INDICATORS where INDICATOR.CATEGORY == indicatorCategoryFilter) and PRODUCT.CATEGORY == productCategoryFilter
		List<ProductModel> filteredProductList = productList.stream().filter(product -> 
				product.indicators.stream().anyMatch(indicator -> 
						indicator.getCategory_id().equals(indicatorCategoryFilter))
				&& product.getCategory().equals(productCategoryFilter)
		).collect(Collectors.toList());
		
		// Remove all indicators that don't match the indicator category
		for (ProductModel product : filteredProductList) {
			product.indicators = product.indicators.stream().filter(ind -> ind.getCategory_id().equals(indicatorCategoryFilter)).collect(Collectors.toList());
		}
		
		
		
		if (filteredProductList.size() > 0) {
			textviewloading.setVisibility(View.GONE);
			searchView.setVisibility(View.VISIBLE);
		} else {
			textviewloading.setVisibility(View.VISIBLE);
			textviewloading.setText(R.string.nodata);
			searchView.setVisibility(View.GONE);
		}
		
		return filteredProductList;

//		Log.e("productfinallist", "" + productfinallist);
//		return productfinallist;
		
	}
}

