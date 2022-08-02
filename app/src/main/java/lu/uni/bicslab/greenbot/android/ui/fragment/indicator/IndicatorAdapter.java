package lu.uni.bicslab.greenbot.android.ui.fragment.indicator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.datamodel.IndicatorModel;
import lu.uni.bicslab.greenbot.android.datamodel.ProductModel;
import lu.uni.bicslab.greenbot.android.other.Utils;

public class IndicatorAdapter extends RecyclerView.Adapter<IndicatorAdapter.IndicatorItemHolder> implements Filterable {
	
	private List<ProductModel> indicatorList;
	private List<ProductModel> indicatorListFiltered;
	private Context context;
	private Consumer<String> clickCallback;
	
	public IndicatorAdapter(Context context, Consumer<String> clickCallback) {
		this.context = context;
		this.clickCallback = clickCallback;
	}
	
	public void setMovieList(final List<ProductModel> indicatorList) {
		
		if (this.indicatorList == null) {
			this.indicatorList = indicatorList;
			this.indicatorListFiltered = indicatorList;
			notifyItemChanged(0, indicatorListFiltered.size());
		} else {
			final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
				@Override
				public int getOldListSize() {
					return IndicatorAdapter.this.indicatorList.size();
				}
				
				@Override
				public int getNewListSize() {
					return indicatorList.size();
				}
				
				@Override
				public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
					return IndicatorAdapter.this.indicatorList.get(oldItemPosition).getName() == indicatorList.get(newItemPosition).getName();
				}
				
				@Override
				public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
					
					ProductModel newdata = IndicatorAdapter.this.indicatorList.get(oldItemPosition);
					
					ProductModel olddata = indicatorList.get(newItemPosition);
					
					return newdata.getName() == olddata.getName();
				}
			});
			this.indicatorList = indicatorList;
			this.indicatorListFiltered = indicatorList;
			result.dispatchUpdatesTo(this);
		}
	}
	
	@Override
	public IndicatorItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.indicator_item_layout, parent, false);
		return new IndicatorItemHolder(view);
	}
	
	@Override
	public void onBindViewHolder(IndicatorItemHolder holder, int position) {
		ProductModel product = indicatorListFiltered.get(position);
		holder.txtName.setText(product.getName());
		holder.card_view.setOnClickListener(v -> clickCallback.accept(product.getCode()));
		
		// Assume Flow is element 0 and remove all of the ImageViews that may already be there
		holder.indicator_layout.removeViewsInLayout(1, holder.indicator_layout.getChildCount()-1);
		
		for (IndicatorModel ind : product.indicators) {
			if (!ind.isApplicable() /*|| ind.sub_indicators.size() == 0*/)
				continue;
			
			ImageView imageview = (ImageView) LayoutInflater.from(context).inflate(R.layout.indicator_item_layout_indicator_imageview, holder.indicator_layout, false);
			Glide.with(context).load(Utils.getDrawableImage(context, ind.getIcon_name())).into(imageview);
			imageview.setId(View.generateViewId());
			holder.indicator_layout.addView(imageview);
			holder.indicator_flow.addView(imageview);
		}

		// Product icon
		Glide.with(context).load(Utils.getDrawableImage(context, product.getImage_url())).error(R.drawable.ic_menu_gallery).into(holder.imageview_icon);
		//Product category icon
		Glide.with(context).load(Utils.getDrawableImage(context, product.getCategory())).error(R.drawable.ic_menu_gallery).into(holder.imageview_origin);
	}
	
	@Override
	public int getItemCount() {
		
		if (indicatorList != null) {
			return indicatorListFiltered.size();
		} else {
			return 0;
		}
	}
	
	@Override
	public Filter getFilter() {
		return new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence charSequence) {
				String charString = charSequence.toString();
				if (charString.isEmpty()) {
					indicatorListFiltered = indicatorList;
				} else {
					List<ProductModel> filteredList = new ArrayList<>();
					for (ProductModel movie : indicatorList) {
						if (movie.getName().toLowerCase().contains(charString.toLowerCase())) {
							filteredList.add(movie);
						}
					}
					indicatorListFiltered = filteredList;
				}
				
				FilterResults filterResults = new FilterResults();
				filterResults.values = indicatorListFiltered;
				return filterResults;
			}
			
			@Override
			protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
				indicatorListFiltered = (ArrayList<ProductModel>) filterResults.values;
				
				notifyDataSetChanged();
			}
		};
	}
	
	class IndicatorItemHolder extends RecyclerView.ViewHolder {
		
		public TextView txtName;
		public CardView card_view;
		public ImageView imageview_icon;
		public ImageView imageview_origin;
		public Flow indicator_flow;
		public ConstraintLayout indicator_layout;
		
		public IndicatorItemHolder(View view) {
			super(view);
			txtName = view.findViewById(R.id.txtName);
			card_view = view.findViewById(R.id.card_view);
			imageview_icon = view.findViewById(R.id.imageview_icon);
			imageview_origin = view.findViewById(R.id.imageview_origin);
			indicator_flow = view.findViewById(R.id.indicator_flow);
			indicator_layout = view.findViewById(R.id.indicator_layout);
		}
	}
}

