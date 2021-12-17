package lu.uni.bicslab.greenbot.android.ui.fragment.compare;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.other.CompareModel;
import lu.uni.bicslab.greenbot.android.datamodel.IndicatorModel;
import lu.uni.bicslab.greenbot.android.other.Utils;

public class CustomCompareGridAdapter extends RecyclerView.Adapter<CustomCompareGridAdapter.CustomViewHolder> {
	
	private final List<CompareModel> compareModels;
	private final Context mcontext;
	int positionViewpager;
	private final List<List<IndicatorModel>> modelIndicatorModelss; // Lists of indicators grouped by category
	
	public static class CustomViewHolder extends RecyclerView.ViewHolder {

		CardView card_view_main;
		TextView txt_categoryname;
		ImageView img_product_icon;
		RecyclerView recycler_viewindicator;
		
		public CustomViewHolder(View view) {
			super(view);
			this.card_view_main = view.findViewById(R.id.card_view);
			this.txt_categoryname = view.findViewById(R.id.txt_categoryname);
			this.img_product_icon = view.findViewById(R.id.img_product_icon);
			this.recycler_viewindicator = view.findViewById(R.id.indicator_view);
		}
	}
	
	public CustomCompareGridAdapter(Context mcontext, int positionViewpager, List<CompareModel> mCompareModelList) {
		this.compareModels = mCompareModelList;
		this.mcontext = mcontext;
		this.positionViewpager = positionViewpager;


		modelIndicatorModelss = new ArrayList<>();
		for (int i=0; i<compareModels.size(); i++ ) {
			if(positionViewpager == 0)
				modelIndicatorModelss.add(compareModels.get(i).getmCompareItemsModel().getIndCatEnvironmentlist());
			else if(positionViewpager == 1)
				modelIndicatorModelss.add(compareModels.get(i).getmCompareItemsModel().getIndCatEconomicList());
			else if(positionViewpager == 2)
				modelIndicatorModelss.add(compareModels.get(i).getmCompareItemsModel().getIndCatSociallist());
			else
				modelIndicatorModelss.add(compareModels.get(i).getmCompareItemsModel().getIndCatGoodGevernanceList());

		}
	}
	
	@Override
	public CustomCompareGridAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent,
																							  int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.comare_row, parent, false);
		
		CustomCompareGridAdapter.CustomViewHolder myViewHolder = new CustomCompareGridAdapter.CustomViewHolder(view);
		return myViewHolder;
	}
	
	@Override
	public void onBindViewHolder(CustomCompareGridAdapter.CustomViewHolder holder, int position) {
		// ProductModel dataModel = productModel.get(position);
		ImageView imageview_icon = holder.img_product_icon;
		RecyclerView recycler_viewindicator = holder.recycler_viewindicator;
		Log.e("eee position", "" + positionViewpager);

		//imageview_icon.setBackground(dataModel.get(position).getImage());
		Drawable image = Utils.getDrawableImage(mcontext, compareModels.get(position).getProductModelForcompare().getImage_url());
		Glide.with(mcontext).load(image).apply(RequestOptions.centerCropTransform()).into(imageview_icon);
		//Glide.with(mcontext).load(image).error(R.drawable.ic_menu_gallery).into(imageview_icon);

		CustomCompareListRowAdapter adapter = new CustomCompareListRowAdapter(mcontext, positionViewpager, modelIndicatorModelss.get(position));

		// Colored frame around compared/featured product
		if(compareModels.get(position).IsReference())
			holder.card_view_main.setCardBackgroundColor(Color.YELLOW);

		recycler_viewindicator.setAdapter(adapter);
		recycler_viewindicator.setLayoutManager(new LinearLayoutManager(mcontext));
		
	}
	
	@Override
	public int getItemCount() {
		return compareModels.size();
	}
}