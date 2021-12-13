package lu.uni.bicslab.greenbot.android.ui.fragment.compare;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.databinding.FragmentComareLayoutBinding;
import lu.uni.bicslab.greenbot.android.other.CompareModel;
import lu.uni.bicslab.greenbot.android.other.Utils;
import lu.uni.bicslab.greenbot.android.datamodel.IndicatorModel;
import lu.uni.bicslab.greenbot.android.datamodel.ProductModel;
import lu.uni.bicslab.greenbot.android.datamodel.IndicatorCategoryModel;

public class CompareActivity extends AppCompatActivity {
	
	private CompareViewModel compareViewModel;
	private ViewSliderPagerAdapter mAdapter;
	private TextView[] dots;
	static ArrayList<Integer> layouts;
	List<ProductModel> mProductToReviewlist;
	private FragmentComareLayoutBinding binding;
	ProductModel modelmain;
	int currentPagemain;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = FragmentComareLayoutBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		
		getSupportActionBar().setTitle("Compare");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		
		modelmain = (ProductModel) getIntent().getSerializableExtra("key_product");
		
		
		readData();
		
		
	}
	
	/*
	 * ViewPager page change listener
	 */
	ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
		@Override
		public void onPageSelected(int position) {
			super.onPageSelected(position);
			addBottomDots(position);
			
			// changing the next button text 'NEXT' / 'GOT IT'
			if (position == 0) {
				// last page. make button text to GOT IT
				binding.btnNext.setText("start");
				binding.btnSkip.setVisibility(View.GONE);
			} else if (position == (dots.length - 1)) {
				binding.btnNext.setText("Finish");
				binding.btnSkip.setVisibility(View.VISIBLE);
			} else {
				// still pages are left
				binding.btnNext.setText("next");
				binding.btnSkip.setVisibility(View.VISIBLE);
			}
		}
	};
	
	
	/*
	 * Adds bottom dots indicator
	 * */
	private void addBottomDotsold(int currentPage) {
		dots = new TextView[layouts.size()];
		
		int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
		int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);
		int green = getResources().getColor(R.color.lightgreen);
		int blue = getResources().getColor(R.color.lightblue);
		int yellow = getResources().getColor(R.color.lightyellow);
		int orange = getResources().getColor(R.color.lightorange);
		
		
		binding.layoutDots.removeAllViews();
		for (int i = 0; i < dots.length; i++) {
			dots[i] = new TextView(getApplicationContext());
			
			dots[i].setTextSize(35);
			dots[i].setTextColor(colorsInactive[currentPage]);
			binding.layoutDots.addView(dots[i]);
		}
		
		if (dots.length > 0)
			dots[currentPage].setTextColor(colorsActive[currentPage]);
	}
	
	private void addBottomDots(int currentPage) {
		dots = new TextView[layouts.size()];
		currentPagemain = currentPage;
		int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
		int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);
		int[] colorsgreen = getResources().getIntArray(R.array.array_dot_green);
		int[] colorsblue = getResources().getIntArray(R.array.array_dot_blue);
		int[] colorsyellow = getResources().getIntArray(R.array.array_dot_yellow);
		int[] colorsorange = getResources().getIntArray(R.array.array_dot_lightorange);
		
		binding.layoutDots.removeAllViews();
		for (int i = 0; i < dots.length; i++) {
			dots[i] = new TextView(this);
			dots[i].setText(Html.fromHtml("&#8226;"));
			dots[i].setTextSize(35);
			dots[i].setTextColor(colorsInactive[currentPage]);
			binding.layoutDots.addView(dots[i]);
		}
		
		if (currentPage == 0) {
			dots[currentPage].setTextColor(colorsgreen[currentPage]);
		} else if (currentPage == 1) {
			dots[currentPage].setTextColor(colorsblue[currentPage]);
		} else if (currentPage == 2) {
			dots[currentPage].setTextColor(colorsyellow[currentPage]);
		} else if (currentPage == 3) {
			dots[currentPage].setTextColor(colorsorange[currentPage]);
		}
		binding.btnSkip.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		binding.btnNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int cout = 0;
				if (binding.btnNext.getText().equals("Finish")) {
					finish();
				}
				binding.viewPager.setCurrentItem(currentPagemain + 1);
			}
		});
		
	}
	
	public void setmAdapterViewpager(List<IndicatorCategoryModel> mCategoryListmain, List<CompareModel> mCompareMdellist) {
		layouts = new ArrayList<Integer>();
		for (int i = 0; i < mCategoryListmain.size(); i++) {
			layouts.add(R.layout.comare_viewpager_row);
		}
		mAdapter = new ViewSliderPagerAdapter(getApplicationContext(), layouts, mCategoryListmain, mCompareMdellist);
		binding.viewPager.setAdapter(mAdapter);
		addBottomDots(0);
		binding.viewPager.registerOnPageChangeCallback(pageChangeCallback);
		
	}
	
	public void readData() {
		
		List<IndicatorModel> indicatorCategoryList = Utils.getIndicatorList(getApplicationContext());
		
		List<ProductModel> productModelList = Utils.getProductList(getApplicationContext());
		
		//get the items for compare
		List<CompareModel> compareViewModelList = new ArrayList<>();
		
		for (ProductModel mProductModel : productModelList) {
			//main products
			if (modelmain.getType().equals(mProductModel.getType())) {
				//add products
				//................................................................
				
				List<IndicatorModel> indCatEnvironmentlist = new ArrayList<>();
				List<IndicatorModel> indCatSociallist = new ArrayList<>();
				List<IndicatorModel> indCatGoodGevernanceList = new ArrayList<>();
				List<IndicatorModel> indCatEconomicList = new ArrayList<>();
				//................................................................
				
				for (int j = 0; j < indicatorCategoryList.size(); j++) {
					IndicatorModel indicatorModelMain = indicatorCategoryList.get(j);
					
					//our data
					if (indicatorModelMain.getCategory_id().equals(Utils.ind_cat_environment)) {
						indicatorModelMain.setSelected(false);
						
						indCatEnvironmentlist.add(indicatorModelMain);
						
					} else if (indicatorModelMain.getCategory_id().equals(Utils.ind_cat_social)) {
						indicatorModelMain.setSelected(false);
						//if (indicatorModelMain.getId().equals(indicator)) {
						//  indicatorModelMain.setSelected(true);
						//}
						indCatSociallist.add(indicatorModelMain);
					} else if (indicatorModelMain.getCategory_id().equals(Utils.ind_cat_good_gevernance)) {
						indicatorModelMain.setSelected(false);
						
						indCatGoodGevernanceList.add(indicatorModelMain);
					} else if (indicatorModelMain.getCategory_id().equals(Utils.ind_cat_economic)) {
						indicatorModelMain.setSelected(false);
						
						indCatEconomicList.add(indicatorModelMain);
					}
					
				}
				//................................................................
				CompareModel.CompareItemsModel comparemodel = new CompareModel.CompareItemsModel(indCatEnvironmentlist,
						indCatEconomicList, indCatSociallist, indCatGoodGevernanceList);
				CompareModel mCompareViewModel = new CompareModel(mProductModel, comparemodel);
				compareViewModelList.add(mCompareViewModel);
				
				//main
				for (int j = 0; j < mProductModel.indicators.size(); j++) {
					String indicator = mProductModel.indicators.get(j).getId();
					String category = getCategoryIndicator(indicatorCategoryList, indicator);
					if (category.equals(Utils.ind_cat_environment)) {
						int n = 0;
						for (IndicatorModel mm : comparemodel.getIndCatEnvironmentlist()) {
							if (mm.getId().equals(indicator)) {
								comparemodel.getIndCatEnvironmentlist().get(n).setSelected(true);
							}
							n++;
						}
						
					} else if (category.equals(Utils.ind_cat_social)) {
						int n = 0;
						for (IndicatorModel mm : comparemodel.getIndCatSociallist()) {
							if (mm.getId().equals(indicator)) {
								comparemodel.getIndCatSociallist().get(n).setSelected(true);
							}
							n++;
						}
					} else if (category.equals(Utils.ind_cat_good_gevernance)) {
						int n = 0;
						for (IndicatorModel mm : comparemodel.getIndCatGoodGevernanceList()) {
							if (mm.getId().equals(indicator)) {
								comparemodel.getIndCatGoodGevernanceList().get(n).setSelected(true);
							}
							n++;
						}
					} else if (category.equals(Utils.ind_cat_economic)) {
						int n = 0;
						for (IndicatorModel mm : comparemodel.getIndCatEconomicList()) {
							if (mm.getId().equals(indicator)) {
								comparemodel.getIndCatEconomicList().get(n).setSelected(true);
							}
							n++;
						}
					}
				}
				
			}
			
		}
		
		
		//category
		List<IndicatorCategoryModel> mCategoryListmain = Utils.getIndicatorCategoryList(getApplicationContext());
		
		
		setmAdapterViewpager(mCategoryListmain, compareViewModelList);
		
	}
	
	public String getCategoryIndicator(List<IndicatorModel> model, String id) {
		String category = Utils.ind_cat_environment;
		for (int j = 0; j < model.size(); j++) {
			if (model.get(j).getId().equals(id)) {
				category = model.get(j).getCategory_id();
				break;
			}
		}
		return category;
		
	}
	
}
