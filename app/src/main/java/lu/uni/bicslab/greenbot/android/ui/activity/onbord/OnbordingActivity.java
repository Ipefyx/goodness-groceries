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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import org.json.JSONObject;

import lu.uni.bicslab.greenbot.android.MainActivity;
import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.other.Profile;
import lu.uni.bicslab.greenbot.android.ui.activity.scan.SigninActivity;
import lu.uni.bicslab.greenbot.android.databinding.OnbordingMainLayoutBinding;
import lu.uni.bicslab.greenbot.android.other.Utils;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class OnbordingActivity extends AppCompatActivity {
	
	private OnbordFragmentStateAdapter sliderAdapter;
	private TextView[] dots;
	private OnbordingMainLayoutBinding binding;
	Profile profile = null;
	JSONObject jsonObject;
	
	public OnbordSelectable[] selectableIndicatorCategories;
	public OnbordSelectable[] selectableOrigins;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = OnbordingMainLayoutBinding.inflate(getLayoutInflater());
		profile = Utils.readProfileData(getApplicationContext());
		Log.e("isLogedin ", "" + profile.isLogedin());
		// if user not logedin show the waiting page
       /* if(profile.isLogedin() == Utils.user_notloggedin){
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
//		pages = new Fragment[] {
//				onbordingOneFragment,
//				onbordingTwoFragment,
//				onbordingThreeFragment,
//		};
		
		sliderAdapter = new OnbordFragmentStateAdapter(this);
		binding.viewPager.setAdapter(sliderAdapter);
		binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
			@Override
			public void onPageSelected(int position) {
				super.onPageSelected(position);
				updateBottomDots(position);
			}
		});
		
		binding.btnNext.setOnClickListener(v -> {
			int current = binding.viewPager.getCurrentItem();
			if (current < sliderAdapter.getItemCount()-1) {
				binding.viewPager.setCurrentItem(current+1);
			} else {
				launchHomeScreen();
			}
		});
		
		// adding bottom dots
		updateBottomDots(0);
		
	}
	
	private void launchHomeScreen() {
//		if (profile.isLogedin() == Utils.user_loggedin_firsttime) {
//			Profile profileData = Utils.readProfileData(getApplicationContext());
//			profileData.setLogedin(Utils.user_loggedin);
//			Utils.saveProfile(getApplicationContext(), profileData);
//			Intent i = new Intent(this, MainActivity.class);
//			startActivity(i);
//			finish();
//		} else {
//			Intent i = new Intent(this, SigninActivity.class);
//			startActivity(i);
//			finish();
//		}

		// Code above should be correct but will not work until logging-in works, so temporarily go to MainActivity by default.
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
		finish();
	}
	
	
	/*
	 * Adds bottom dots indicator
	 * */
	private void updateBottomDots(int currentPage) {
		dots = new TextView[sliderAdapter.getItemCount()];
		
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
	
	
	private static class OnbordFragmentStateAdapter extends FragmentStateAdapter {
		
		public OnbordFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity) {
			super(fragmentActivity);
		}
		
		// Here a switch and a manual item count in getItemCount() are used because that was the best way
		// to define the mixed fragments without going completely overkill with reflexion, even it may seem "unclean"
		// (and also because Fragments MUST be instantiated within createFragment)
		@NonNull
		@Override
		public Fragment createFragment(int position) {
			switch (position) {
				default:
				case 0:
					return new SimpleLayoutFragment(R.layout.onbording_one_layout);
				case 1:
					return new SelectIndicatorsFragment();
				case 2:
					return new SelectOriginsFragment();
				case 3:
					return new SimpleLayoutFragment(R.layout.onbording_two_layout);
				case 4:
					return new SimpleLayoutFragment(R.layout.onbording_three_layout);
			}
		}
		
		@Override
		public int getItemCount() {
			return 5;
		}
	}
	
	
	public static class SimpleLayoutFragment extends Fragment {
		
		private final int layoutID;
		
		public SimpleLayoutFragment(int layoutID) {
			this.layoutID = layoutID;
		}
		
		@Nullable
		@Override
		public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
			return inflater.inflate(layoutID, container, false);
		}
	}
	
	
//	public class ViewsSliderAdapter extends RecyclerView.Adapter<ViewsSliderAdapter.SliderViewHolder> {
//		
//		public ViewsSliderAdapter() {
//		}
//		
//		@NonNull
//		@Override
//		public ViewsSliderAdapter.SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//			View view = LayoutInflater.from(parent.getContext())
//					.inflate(viewType, parent, false);
//			return new SliderViewHolder(view);
//		}
//		
//		public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
//			
//		}
//		
//		@Override
//		public int getItemViewType(int position) {
//			return pages[position];
//		}
//		
//		@Override
//		public int getItemCount() {
//			return pages.length;
//		}
//		
//		public class SliderViewHolder extends RecyclerView.ViewHolder {
//			
//			public SliderViewHolder(View view) {
//				super(view);
//			}
//		}
//	}
	
}