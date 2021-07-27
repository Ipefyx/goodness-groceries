package lu.uni.bicslab.greenbot.android.ui.fragment.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.other.UserData;
import lu.uni.bicslab.greenbot.android.ui.activity.feedback.FeedbackMainActivity;

public class ProfileFragment extends Fragment {
	
	private TextView profile_id;
	private Button review_products;
	
	private String[] productsToReview;
	
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_profile, container, false);
		
		profile_id = root.findViewById(R.id.profile_id);
		review_products = root.findViewById(R.id.review_products);
		
		productsToReview = getArguments().getStringArray("products_to_review");
		if (productsToReview == null)
			productsToReview = new String[]{};
		
		profile_id.setText(UserData.getID(getContext()));
		
		review_products.setOnClickListener(v -> {
				Intent i = new Intent(getActivity(), FeedbackMainActivity.class);
				i.putExtra("product_id", productsToReview[0]);
				startActivity(i);
		});
		
		// TODO: Add badge with number of products to the review_products button
		
		return root;
	}
}
