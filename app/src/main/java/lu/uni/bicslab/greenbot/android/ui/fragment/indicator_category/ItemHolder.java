package lu.uni.bicslab.greenbot.android.ui.fragment.indicator_category;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import lu.uni.bicslab.greenbot.android.R;

public class ItemHolder extends RecyclerView.ViewHolder {
	
	public TextView txtName, txtDoc;
	public CardView card_view;
	public ImageView imageview_icon;
	
	public ItemHolder(View view) {
		super(view);
		
		txtName = view.findViewById(R.id.txtName);
		txtDoc = view.findViewById(R.id.txtDoc);
		card_view = view.findViewById(R.id.card_view);
		imageview_icon = view.findViewById(R.id.imageview_icon);
	}
}