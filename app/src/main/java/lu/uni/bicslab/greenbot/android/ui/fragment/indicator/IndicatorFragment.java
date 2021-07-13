package lu.uni.bicslab.greenbot.android.ui.fragment.indicator;

import android.app.SearchManager;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.other.Utils;

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
		
		View root = inflater.inflate(R.layout.fragment_indicator, container, false);
		recyclerView = root.findViewById(R.id.indicator_view);
		textviewloading = root.findViewById(R.id.textviewloading);
		//searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
		searchView = root.findViewById(R.id.search_src_text);
		searchView.setMaxWidth(Integer.MAX_VALUE);
		
		indicatorCategoryFilter = getArguments().getString("filter_indicator_category");
		productCategoryFilter = getArguments().getString("filter_product_category");
		
		list = new ArrayList<ProductModel>();
		itemAdapter = new IndicatorAdapter();
		list = fillData();
		
		
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
	
	private List<ProductModel> fillData() {
		textviewloading.setText(R.string.loading);
		
		Log.i("product receive", indicatorCategoryFilter + " " + productCategoryFilter);
		
		List<IndicatorModel> indicatorCategoryList = Utils.getIndicatorList(getActivity());
		List<ProductModel> productList = Utils.getProductList(getActivity());
		
		// Feels hacky, but gets the job done (kinda had to work with the existing stuff)
		// For each indicator id listed for the product, find the corresponding indicator, put it into the product, and fill in the indicator_description
		// Surprisingly doing this also simplifies filtering drastically (a little further below)
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
		
		// Filters the product list according to this logic:
		// For each PRODUCT: (there exists at least one INDICATOR from PRODUCT.INDICATORS where INDICATOR.CATEGORY == indicatorCategoryFilter) and PRODUCT.CATEGORY == productCategoryFilter
		List<ProductModel> filteredProductList = productList.stream().filter(product -> 
				product.indicators.stream().anyMatch(indicator -> 
						indicator.category_id.equals(indicatorCategoryFilter))
				&& product.category.equals(productCategoryFilter) //TODO Temporary filtering of product category using the icon name
		).collect(Collectors.toList());
		
		
		
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

