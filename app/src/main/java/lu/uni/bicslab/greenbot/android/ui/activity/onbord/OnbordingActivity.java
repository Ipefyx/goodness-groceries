package lu.uni.bicslab.greenbot.android.ui.activity.onbord;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import org.json.JSONObject;

import lu.uni.bicslab.greenbot.android.MainActivity;
import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.other.Profile;
import lu.uni.bicslab.greenbot.android.ui.activity.scan.SigninActivity;
import lu.uni.bicslab.greenbot.android.databinding.OnbordingMainLayoutBinding;
import lu.uni.bicslab.greenbot.android.barcoderead.transformers.Utils;
import lu.uni.bicslab.greenbot.android.ui.activity.selectgrid.SelectGridOneActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class OnbordingActivity extends AppCompatActivity {
	// private ViewsSliderAdapter mAdapter;
	private ViewsSliderAdapter mAdapter;
	private TextView[] dots;
	private int[] pages;
	private OnbordingMainLayoutBinding binding;
	Profile profile = null;
	JSONObject jsonObject;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = OnbordingMainLayoutBinding.inflate(getLayoutInflater());
		profile = lu.uni.bicslab.greenbot.android.other.Utils.readProfileData(getApplicationContext());
		Log.e("isLogedin ", "" + profile.isLogedin());
		// if user not logedin show the waiting page
       /* if(profile.isLogedin() == lu.uni.bicslab.greenbot.android.other.Utils.user_notloggedin){
            initLogin = true;
        }else{
            initLogin = false;
        }*/
		String data = getIntent().getStringExtra("data");
		try {
			jsonObject = new JSONObject(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setContentView(binding.getRoot());
		init();
		
	}
	
	private void init() {
		// layouts of all welcome sliders
		// add few more layouts if you want
		pages = new int[] {
				R.layout.onbording_two_layout,
				R.layout.onbording_one_layout,
				R.layout.onbording_three_layout};
		
		mAdapter = new ViewsSliderAdapter();
		binding.viewPager.setAdapter(mAdapter);
		binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
			@Override
			public void onPageSelected(int position) {
				super.onPageSelected(position);
				updateBottomDots(position);
			}
		});
		
		binding.btnNext.setOnClickListener(v -> {
			int current = binding.viewPager.getCurrentItem();
			if (current < pages.length) {
				binding.viewPager.setCurrentItem(current+1);
			}
		});
		
		// adding bottom dots
		updateBottomDots(0);
		
		if (profile.isLogedin() == lu.uni.bicslab.greenbot.android.other.Utils.user_notloggedin) {
			binding.viewPager.setPageTransformer(Utils.getTransformer(0));
			binding.viewPager.setCurrentItem(0);
			binding.viewPager.getAdapter().notifyDataSetChanged();
		} else {
			binding.viewPager.setPageTransformer(Utils.getTransformer(1));
			binding.viewPager.setCurrentItem(1);
			binding.viewPager.getAdapter().notifyDataSetChanged();
		}//scroll avoid
		if (profile.isLogedin() == lu.uni.bicslab.greenbot.android.other.Utils.user_notloggedin) {
			binding.viewPager.setUserInputEnabled(false);
		} else {
			binding.viewPager.setUserInputEnabled(true);
		}
	}
	
	private void launchHomeScreen(int count) {
		if (profile.isLogedin() == lu.uni.bicslab.greenbot.android.other.Utils.user_notloggedin) {
			Intent i = new Intent(this, SelectGridOneActivity.class);
			i.putExtra("data", jsonObject.toString());//
			startActivity(i);
			finish();
		} else if (profile.isLogedin() == lu.uni.bicslab.greenbot.android.other.Utils.user_loggedin_firsttime) {
			if (count == 2) {
				binding.viewPager.setCurrentItem(count + 1);
			} else {
				Profile profileData = lu.uni.bicslab.greenbot.android.other.Utils.readProfileData(getApplicationContext());
				profileData.setLogedin(lu.uni.bicslab.greenbot.android.other.Utils.user_loggedin);
				lu.uni.bicslab.greenbot.android.other.Utils.saveProfile(getApplicationContext(), profileData);
				Intent i = new Intent(this, MainActivity.class);
				startActivity(i);
				finish();
			}
			
		} else {
			Intent i = new Intent(this, SigninActivity.class);
			startActivity(i);
			finish();
		}
		
		
	}
	
	
	/*
	 * Adds bottom dots indicator
	 * */
	private void updateBottomDots(int currentPage) {
		dots = new TextView[pages.length];
		
		int colorActive = getResources().getColor(R.color.onbord_dot_active, null);
		int colorInactive = getResources().getColor(R.color.onbord_dot_inactive, null);
		
		binding.layoutDots.removeAllViews();
		for (int i = 0; i < dots.length; i++) {
			dots[i] = new TextView(this);
			dots[i].setText(Html.fromHtml("&#8226;"));
			dots[i].setTextSize(50);
			dots[i].setTextColor(colorInactive);
			binding.layoutDots.addView(dots[i]);
		}
		
		if (dots.length > 0)
			dots[currentPage].setTextColor(colorActive);
	}
	
	
	public class ViewsSliderAdapter extends RecyclerView.Adapter<ViewsSliderAdapter.SliderViewHolder> {
		
		public ViewsSliderAdapter() {
		}
		
		@NonNull
		@Override
		public ViewsSliderAdapter.SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext())
					.inflate(viewType, parent, false);
			return new SliderViewHolder(view);
		}
		
		public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
			
		}
		
		@Override
		public int getItemViewType(int position) {
			return pages[position];
		}
		
		@Override
		public int getItemCount() {
			return pages.length;
		}
		
		public class SliderViewHolder extends RecyclerView.ViewHolder {
			public TextView onbord_one_title, onbord_one_doc, slide_two_title, slide_two_doc, slide_three_title, slide_three_doc;
			
			public SliderViewHolder(View view) {
				super(view);
				
				onbord_one_title = view.findViewById(R.id.slide_one_title);
				onbord_one_doc = view.findViewById(R.id.slide_one_doc);
				slide_two_title = view.findViewById(R.id.slide_two_title);
				slide_two_doc = view.findViewById(R.id.slide_two_doc);
				slide_three_title = view.findViewById(R.id.slide_three_title);
				slide_three_doc = view.findViewById(R.id.slide_three_doc);
				
				
			}
		}
	}
	
}