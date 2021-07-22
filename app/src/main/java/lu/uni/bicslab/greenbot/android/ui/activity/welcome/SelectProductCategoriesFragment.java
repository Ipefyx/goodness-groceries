package lu.uni.bicslab.greenbot.android.ui.activity.welcome;

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

import java.util.List;

import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.other.Utils;
import lu.uni.bicslab.greenbot.android.ui.fragment.product_category.ProductCategoryModel;

public class SelectProductCategoriesFragment extends Fragment {
	
	private WelcomeSelectable[] selectables;
	
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
		
		List<ProductCategoryModel> productCategories = Utils.getProductCategoryList(getActivity());
		
		int[] colors = new int[] {R.color.palette_green2, R.color.palette_green2, R.color.palette_green2, R.color.palette_green2};
		
		WelcomeSelectable[] selectables = new WelcomeSelectable[productCategories.size()];
		// Sorry for this hacky mess, but technically it's not wrong (just keeps the Activity and the Fragment tightly coupled),
		// much simpler for this use than trying to serialize the data through the bundle
		((WelcomeActivity) getActivity()).selectableProductCategories = selectables;
		
		for (int i = 0; i < productCategories.size(); i++) {
			
			ProductCategoryModel prodCat = productCategories.get(i);
			selectables[i] = new WelcomeSelectable(prodCat.getId(), prodCat.getName(), prodCat.getIcon_name(), colors[i]);
			
			View view = LayoutInflater.from(selectorLayout.getContext()).inflate(R.layout.onbording_cardview_row, selectorLayout, false);
			((TextView) view.findViewById(R.id.text_title)).setText(selectables[i].getDescription());
			
			Glide.with(getActivity()).load(Utils.getDrawableImage(getActivity(), selectables[i].getImage())).error(R.drawable.ic_menu_gallery).into((ImageView) view.findViewById(R.id.imageview_icon));
			
			int localPosition = i;
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					selectables[localPosition].setSelected(!selectables[localPosition].isSelected());
					view.findViewById(R.id.card_view).setBackgroundColor(ResourcesCompat.getColor(getResources(), selectables[localPosition].isSelected() ? selectables[localPosition].getColor() : R.color.white, null));
				}
			});
			
			selectorLayout.addView(view);
		}
	}
}
