package lu.uni.bicslab.greenbot.android.other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.ui.fragment.indicator.IndicatorModel;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomView> {
	
	private final List<IndicatorModel> indicatorModel;
	private final Context context;
	
	public CustomAdapter(Context context, List<IndicatorModel> indicatorModel) {
		this.context = context;
		this.indicatorModel = indicatorModel;
		
	}
	
	@Override
	public CustomView onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View view = inflater.inflate(R.layout.item_row, parent, false);
		return new CustomAdapter.CustomView(view);
	}
	
	@Override
	public void onBindViewHolder(CustomView holder, int position) {
		IndicatorModel model = indicatorModel.get(position);
		
		//holder.mName.setText(model.getName());
		holder.mDescription.setText(model.getIndicator_description());
		
//		holder.txt_firstletter.setImageDrawable(Utils.getDrawableImage(context, model.getIcon_name()));
		Glide.with(context).load(Utils.getDrawableImage(context, model.getIcon_name())).error(R.drawable.ic_menu_gallery).into(holder.txt_firstletter);
		
	}
	
	@Override
	public int getItemCount() {
		return indicatorModel == null ? 0 : indicatorModel.size();
	}
	
	public static class CustomView extends RecyclerView.ViewHolder {
		
		private final TextView mName;
		private final TextView mDescription;
		private final ImageView txt_firstletter;
		
		public CustomView(View itemView) {
			super(itemView);
			
			mName = itemView.findViewById(R.id.txt_name);
			mDescription = itemView.findViewById(R.id.txt_desc);
			txt_firstletter = itemView.findViewById(R.id.txt_firstletter);
		}
	}
}
