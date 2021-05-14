package lu.uni.bicslab.greenbot.android.ui.activity.onbord;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.other.Utils;
import lu.uni.bicslab.greenbot.android.ui.fragment.indicator_category.IndicatorCategoryModel;

public class SelectOriginsFragment extends Fragment {
	
	private OnbordSelectable[] selectables;
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.onbording_select_two_layout, container);
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		LinearLayout selectorLayout = getView().findViewById(R.id.selector_layout);
		
		// Origins are fully hardcoded, because there is no origins.json in the first place
		OnbordSelectable[] selectables = new OnbordSelectable[] {
				new OnbordSelectable(getResources().getString(R.string.biolocal), "origin_localorganic", R.color.palette_green2),
				new OnbordSelectable(getResources().getString(R.string.bioimporte), "origin_importedorganic", R.color.palette_green2),
				new OnbordSelectable(getResources().getString(R.string.conlocal), "origin_localconventional", R.color.palette_green2),
				new OnbordSelectable(getResources().getString(R.string.conimporte), "origin_importedconventional", R.color.palette_green2),
		};
		// Sorry for this hacky mess, but technically it's not wrong (just keeps the Activity and the Fragment tightly coupled),
		// much simpler for this use than trying to serialize the data through the bundle
		((OnbordingActivity) getActivity()).selectableOrigins = selectables;
		
		for (OnbordSelectable selectable : selectables) {
			
			View view = LayoutInflater.from(selectorLayout.getContext()).inflate(R.layout.onbording_cardview_row, selectorLayout, false);
			((TextView) view.findViewById(R.id.text_title)).setText(selectable.getDescription());
			
			Glide.with(getActivity()).load(Utils.getDrawableImage(getActivity(), selectable.getImage())).error(R.drawable.ic_menu_gallery).into((ImageView) view.findViewById(R.id.imageview_icon));
			
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					selectable.setSelected(!selectable.isSelected());
					view.findViewById(R.id.card_view).setBackgroundColor(ResourcesCompat.getColor(getResources(), selectable.isSelected() ? selectable.getColor() : R.color.white, null));
				}
			});
			
			selectorLayout.addView(view);
		}
	}
}
