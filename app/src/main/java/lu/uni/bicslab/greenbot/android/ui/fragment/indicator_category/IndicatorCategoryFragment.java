package lu.uni.bicslab.greenbot.android.ui.fragment.indicator_category;

import android.app.SearchManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import android.widget.SearchView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.datamodel.IndicatorCategoryModel;
import lu.uni.bicslab.greenbot.android.other.Utils;

public class IndicatorCategoryFragment extends Fragment {
	private RecyclerView recyclerView;
	
	private IndicatorCategoryAdapter itemAdapter;
	List<IndicatorCategoryModel> list;
	SearchManager searchManager;
	SearchView searchView;
	TextView textviewloading;
	
	public View onCreateView(@NonNull LayoutInflater inflater,
									 ViewGroup container, Bundle savedInstanceState) {
		
		View root = inflater.inflate(R.layout.fragment_items_main_layout, container, false);
		//ArrayList<IndicatorViewModel> itemList = new ArrayList<>();
		recyclerView = root.findViewById(R.id.indicator_view);
		textviewloading = root.findViewById(R.id.textviewloading);
		searchView = root.findViewById(R.id.search_src_text);
		searchView.setMaxWidth(Integer.MAX_VALUE);
		
		itemAdapter = new IndicatorCategoryAdapter(getActivity(), id -> {
			// Callback used by the adapter to signal the user clicked on category to proceed
			Bundle bundle = new Bundle();
			bundle.putString("filter_indicator_category", "" + id);
			Log.i("product send", "" + id);
			Navigation.findNavController(container).navigate(R.id.goto_prod_cat, bundle);
		});
		itemAdapter.setModelList(fillData());
		
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(mLayoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.setAdapter(itemAdapter);
		
		
		//searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
		
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
	
	private List<IndicatorCategoryModel> fillData() {
		textviewloading.setText(R.string.loading);
		
		List<IndicatorCategoryModel> indicatorCategoryList = Utils.getIndicatorCategoryList(getActivity());

		// 'Hacky' way to add the possibility to browse on any indicators
		indicatorCategoryList.add(Utils.getAllIndicatorCategoryItem(getActivity()));
		
		
		if (indicatorCategoryList.size() > 0) {
			textviewloading.setVisibility(View.GONE);
			searchView.setVisibility(View.VISIBLE);
		} else {
			textviewloading.setVisibility(View.VISIBLE);
			textviewloading.setText(R.string.nodata);
			searchView.setVisibility(View.GONE);
		}
		return indicatorCategoryList;
		
	}
}
