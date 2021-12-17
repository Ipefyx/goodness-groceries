package lu.uni.bicslab.greenbot.android.ui.fragment.compare;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
	ProductModel modelmain; // The actual product to compare with others
	int currentPagemain;
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = FragmentComareLayoutBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		
		getSupportActionBar().setTitle("Compare");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		mContext = getApplicationContext();

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
				binding.btnNext.setText("next");
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
		// Liste des categories des indicateurs existantes
		List<IndicatorCategoryModel> indicatorCategoryModels = Utils.getIndicatorCategoryList(mContext);
		// Liste des indicateurs existants dans la DB
		List<IndicatorModel> indicatorModels = Utils.getIndicatorList(mContext);
		// Liste de tous les produits dans la DB
		List<ProductModel> productModels = Utils.getProductList(mContext);


		// Liste des CompareModel contenenant les data permettant de comparer entre eux
		List<CompareModel> compareModels = new ArrayList<>();

		// Comparaison avec chaque produit existant
		for (ProductModel pm : productModels) {
			// Si le type des 2 produits sont les même
			if(modelmain.getType().equals(pm.getType())) {
				// Alors on l'ajoute pour le comparer

				// Creer pour chaque categorie d'indicateurs, une liste des indicateurs de cette cat
				List<IndicatorModel> indCatEnvironmentList = new ArrayList<>();
				List<IndicatorModel> indCatSocialList = new ArrayList<>();
				List<IndicatorModel> indCatGoodGovernanceList = new ArrayList<>();
				List<IndicatorModel> indCatEconomicList = new ArrayList<>();

				// Pour chaque indicateur existant
				for(IndicatorModel im : indicatorModels) {

					// Verifier et selectionner si le produit contient cet indicateur
					IndicatorModel nIm = new IndicatorModel(im);
					nIm.setSelected(pm.isFeatured(im));

					// Verifier a quelle categorie appartient l'indicateur et l'ajouter dans le
					// tableau correspondant
					switch (nIm.getCategory_id()) {
						case Utils.ind_cat_environment: indCatEnvironmentList.add(nIm); break;
						case Utils.ind_cat_social: indCatSocialList.add(nIm); break;
						case Utils.ind_cat_good_governance: indCatGoodGovernanceList.add(nIm); break;
						case Utils.ind_cat_economic: indCatEconomicList.add(nIm); break;
					}
				}


				// Ajout à la liste des objets à comparer
				CompareModel compareModel = new CompareModel(new ProductModel(pm),
						new CompareModel.CompareItemsModel(indCatEnvironmentList, indCatSocialList,
								indCatGoodGovernanceList, indCatEconomicList));
				compareModels.add(compareModel);

			}
		}

		setmAdapterViewpager(indicatorCategoryModels, compareModels);
	}
	
}
