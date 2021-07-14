package lu.uni.bicslab.greenbot.android.ui.fragment.indicator_category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.datamodel.IndicatorCategoryModel;
import lu.uni.bicslab.greenbot.android.other.Utils;

public class IndicatorCategoryAdapter extends RecyclerView.Adapter<ItemHolder> implements Filterable {
	
	
	private List<IndicatorCategoryModel> modelList;
	private List<IndicatorCategoryModel> modelListFiltered;
	private Context context;
	private Consumer<String> clickCallback;
	
	public IndicatorCategoryAdapter(Context context, Consumer<String> clickCallback) {
		this.context = context;
		this.clickCallback = clickCallback;
	}
	
	
	public void setModelList(final List<IndicatorCategoryModel> indCatList) {
		if (this.modelList == null) {
			this.modelList = indCatList;
			this.modelListFiltered = indCatList;
			notifyItemChanged(0, modelListFiltered.size());
		} else {
			final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
				@Override
				public int getOldListSize() {
					return IndicatorCategoryAdapter.this.modelList.size();
				}
				
				@Override
				public int getNewListSize() {
					return indCatList.size();
				}
				
				@Override
				public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
					return IndicatorCategoryAdapter.this.modelList.get(oldItemPosition).getIndicator_name() == indCatList.get(newItemPosition).getIndicator_name();
				}
				
				@Override
				public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
					
					IndicatorCategoryModel newdata = IndicatorCategoryAdapter.this.modelList.get(oldItemPosition);
					
					IndicatorCategoryModel olddata = indCatList.get(newItemPosition);
					
					return newdata.getIndicator_name() == olddata.getIndicator_name();
				}
			});
			this.modelList = indCatList;
			this.modelListFiltered = indCatList;
			result.dispatchUpdatesTo(this);
		}
	}
	
	@Override
	public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.indicator_category_item_layout, parent, false);
		return new ItemHolder(view);
	}
	
	@Override
	public void onBindViewHolder(ItemHolder holder, int position) {
		final int pos = position;
		final IndicatorCategoryModel model = modelListFiltered.get(position);
		holder.txtName.setText(model.getIndicator_name());
		holder.txtDoc.setText(model.getDescription());
		holder.card_view.setOnClickListener(v -> clickCallback.accept(model.getId()));
		
//		holder.imageview_icon.setImageDrawable(Utils.getDrawableImage(context, model.getIcon_name()));
		Glide.with(context).load(Utils.getDrawableImage(context, model.getIcon_name())).error(R.drawable.ic_menu_gallery).into(holder.imageview_icon);
	}
	
	@Override
	public int getItemCount() {
		
		if (modelList != null) {
			return modelListFiltered.size();
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
					modelListFiltered = modelList;
				} else {
					List<IndicatorCategoryModel> filteredList = new ArrayList<>();
					for (IndicatorCategoryModel movie : modelList) {
						if (movie.getIndicator_name().toLowerCase().contains(charString.toLowerCase())) {
							filteredList.add(movie);
						}
					}
					modelListFiltered = filteredList;
				}
				
				FilterResults filterResults = new FilterResults();
				filterResults.values = modelListFiltered;
				return filterResults;
			}
			
			@Override
			protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
				modelListFiltered = (ArrayList<IndicatorCategoryModel>) filterResults.values;
				
				notifyDataSetChanged();
			}
		};
	}
}

