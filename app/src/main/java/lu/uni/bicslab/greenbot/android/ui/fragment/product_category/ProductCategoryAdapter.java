package lu.uni.bicslab.greenbot.android.ui.fragment.product_category;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Html;
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
import lu.uni.bicslab.greenbot.android.datamodel.ProductCategoryModel;
import lu.uni.bicslab.greenbot.android.other.Utils;
import lu.uni.bicslab.greenbot.android.ui.fragment.indicator_category.ItemHolder;

public class ProductCategoryAdapter extends RecyclerView.Adapter<ItemHolder> implements Filterable {
	
	
	private List<ProductCategoryModel> modelList;
	private List<ProductCategoryModel> modelListFiltered;
	
	private Context context;
	private Consumer<String> clickCallback;
	
	public ProductCategoryAdapter(Context context, Consumer<String> clickCallback) {
		this.context = context;
		this.clickCallback = clickCallback;
	}
	
	public void setModelList(final List<ProductCategoryModel> indCatList) {
		if (this.modelList == null) {
			this.modelList = indCatList;
			this.modelListFiltered = indCatList;
			notifyItemChanged(0, modelListFiltered.size());
		} else {
			final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
				@Override
				public int getOldListSize() {
					return ProductCategoryAdapter.this.modelList.size();
				}
				
				@Override
				public int getNewListSize() {
					return indCatList.size();
				}
				
				@Override
				public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
					return ProductCategoryAdapter.this.modelList.get(oldItemPosition).getName() == indCatList.get(newItemPosition).getName();
				}
				
				@Override
				public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
					
					ProductCategoryModel newdata = ProductCategoryAdapter.this.modelList.get(oldItemPosition);
					
					ProductCategoryModel olddata = indCatList.get(newItemPosition);
					
					return newdata.getName() == olddata.getName();
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
		final ProductCategoryModel model = modelListFiltered.get(position);
		holder.txtName.setText(model.getName());
		holder.txtDoc.setText(model.getDescription());
		holder.card_view.setOnClickListener(v -> clickCallback.accept(model.getId()));
		
//		holder.imageview_icon.setImageDrawable(Utils.getDrawableImage(context, model.getIcon_name()));
		Glide.with(context).load(Utils.getDrawableImage(context, model.getIcon_name())).error(R.drawable.ic_menu_gallery).into(holder.imageview_icon);

		// Info popup for category description
		if(!model.getId().equals(Utils.prod_cat_any)) { // Don't show popup for 'any category' element
			holder.imageview_icon.setOnClickListener(new View.OnClickListener() {
				 @Override
				 public void onClick(View view) {
					 AlertDialog.Builder infoPopup = new AlertDialog.Builder(context);
					 infoPopup.setTitle(model.getName());

					 String content = model.getDescription();

					 infoPopup.setMessage(Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY));
					 infoPopup.setPositiveButton("OK", null);
					 infoPopup.show();
				 }
			 }

			);
		}
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
					List<ProductCategoryModel> filteredList = new ArrayList<>();
					for (ProductCategoryModel movie : modelList) {
						/*if(charString.toLowerCase().contains(movie.getName().toLowerCase()))
							filteredList.add(movie);
*/
						if (movie.getName().toLowerCase().contains(charString.toLowerCase())) {
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
				modelListFiltered = (List<ProductCategoryModel>) filterResults.values;
				
				notifyDataSetChanged();
			}
		};
	}
}

