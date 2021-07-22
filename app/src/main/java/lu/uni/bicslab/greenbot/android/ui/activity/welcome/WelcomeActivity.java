package lu.uni.bicslab.greenbot.android.ui.activity.welcome;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lu.uni.bicslab.greenbot.android.MainActivity;
import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.databinding.OnbordingMainLayoutBinding;
import lu.uni.bicslab.greenbot.android.other.ServerConnection;
import lu.uni.bicslab.greenbot.android.other.UserData;
import lu.uni.bicslab.greenbot.android.ui.activity.StartActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class WelcomeActivity extends AppCompatActivity {
	
	private OnbordFragmentStateAdapter sliderAdapter;
	private TextView[] dots;
	private OnbordingMainLayoutBinding binding;
	JSONObject jsonObject;
	
	public WelcomeSelectable[] selectableIndicatorCategories;
	public WelcomeSelectable[] selectableProductCategories;
	public String id;
	
	private int currentPage = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = OnbordingMainLayoutBinding.inflate(getLayoutInflater());
		
		
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
		sliderAdapter = new OnbordFragmentStateAdapter(this);
		
		binding.viewPager.setAdapter(sliderAdapter);
		binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
			@Override
			public void onPageSelected(int position) {
				super.onPageSelected(position);
				currentPage = position;
				updatePage();
			}
		});
		
		binding.btnNext.setOnClickListener(v -> {
			int current = binding.viewPager.getCurrentItem();
			if (current < sliderAdapter.getItemCount()-1) {
				binding.viewPager.setCurrentItem(current+1);
			} else {
				finishWelcome();
			}
		});
		
		// adding bottom dots
		updatePage();
	}
	
	private void finishWelcome() {
		
		// TODO: Post user request to server and go back to StartActivity
		List<String> selectedIndicators = new ArrayList<>();
		List<String> selectedProducts = new ArrayList<>();
		
		for (WelcomeSelectable w : selectableIndicatorCategories) selectedIndicators.add(w.getId());
		for (WelcomeSelectable w : selectableProductCategories) selectedProducts.add(w.getId());
		
		ServerConnection.requestUserAccess(this, id, selectedProducts.toArray(new String[]{}), selectedIndicators.toArray(new String[]{}), status -> {
			UserData.setStatus(this, status);
			startActivity(new Intent(this, StartActivity.class));
			finish();
		}, error -> {
			if (error instanceof TimeoutError || error instanceof NoConnectionError) {
				Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, R.string.general_error_try_again, Toast.LENGTH_SHORT).show();
			}
		});
		
		
	}
	
	
	/*
	 * Adds bottom dots indicator
	 * */
	private void updatePage() {
		
		// Manage the bottom dots
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
		
		if (id == null)
			setIDValid(false);
	}
	
	public void setIDValid(boolean valid) {
		if (currentPage == sliderAdapter.getIDLockPosition()) {
			binding.btnNext.setEnabled(valid);
			binding.viewPager.setUserInputEnabled(valid);
			if (valid) {
				binding.btnNext.getBackground().setColorFilter(null);
			} else {
				binding.btnNext.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
			}
		}
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
					return new SignInFragment();
				case 2:
					return new SimpleLayoutFragment(R.layout.onbording_two_layout);
				case 3:
					return new SelectProductCategoriesFragment();
				case 4:
					return new SelectIndicatorCategoriesFragment();
				case 5:
					return new SimpleLayoutFragment(R.layout.onbording_three_layout);
			}
		}
		
		@Override
		public int getItemCount() {
			return 6;
		}
		
		// Return the position of the SignInFragment
		public int getIDLockPosition() {
			return 1;
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
	
}