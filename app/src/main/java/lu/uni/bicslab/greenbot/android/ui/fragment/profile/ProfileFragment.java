package lu.uni.bicslab.greenbot.android.ui.fragment.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.other.UserData;
import lu.uni.bicslab.greenbot.android.ui.activity.feedback.FeedbackMainActivity;
import lu.uni.bicslab.greenbot.android.ui.activity.fromprofile.ProductConsultActivity;
import lu.uni.bicslab.greenbot.android.ui.activity.fromprofile.ProductsScannerActivity;
import lu.uni.bicslab.greenbot.android.ui.activity.fromprofile.RecompensesActivity;
import lu.uni.bicslab.greenbot.android.ui.activity.fromprofile.RetourClientActivity;
import lu.uni.bicslab.greenbot.android.ui.activity.fromprofile.TermesAndConditionActivity;

public class ProfileFragment extends Fragment {
	
	private TextView profile_id;
	private Button review_products;
	
	public View onCreateView(@NonNull LayoutInflater inflater,
									 ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_profile, container, false);
		
		profile_id = root.findViewById(R.id.profile_id);
		review_products = root.findViewById(R.id.review_products);
		
		profile_id.setText(UserData.getID(getContext()));
		review_products.setOnClickListener(v -> {
			startActivity(new Intent(getActivity(), FeedbackMainActivity.class));
		});
		
		return root;
	}
}
