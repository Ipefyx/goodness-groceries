package lu.uni.bicslab.greenbot.android.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


import lu.uni.bicslab.greenbot.android.MainActivity;
import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.other.ServerConnection;
import lu.uni.bicslab.greenbot.android.other.UserData;
import lu.uni.bicslab.greenbot.android.ui.activity.scan.SigninActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class StartActivity extends AppCompatActivity implements ServerConnection.ServerConnectionListner {
	
	ServerConnection.ServerConnectionListner mServerConnectionListner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Redirect to correct activities or show correct layout based on the user status
		String userStatus = UserData.getStatus(this);
		
		if (userStatus.equals(UserData.USER_VALID)) {
			if (UserData.isFirstTimeVisit(getApplicationContext())) {
				// Show first-time screen 1 or 2
				
				int firstTimePage = getIntent().getIntExtra("first_time_page", 1);
				
				if (firstTimePage == 1) {
					setContentView(R.layout.activity_firsttime1);
					
					Button continue_button = findViewById(R.id.button_continue);
					continue_button.setOnClickListener(v -> {
						Intent i = new Intent(this, StartActivity.class);
						i.putExtra("first_time_page", 2);
						startActivity(i);
					});
					
				} else if (firstTimePage == 2) {
					setContentView(R.layout.activity_firsttime2);
					
					Button continue_button = findViewById(R.id.button_continue);
					continue_button.setOnClickListener(v -> {
						UserData.setFirstTimeVisit(this, false);
						startActivity(new Intent(this, StartActivity.class));
					});
				}
				
			} else {
				// Go to main view
				
				Intent i = new Intent(this, MainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(i);
			}
			
		} else if (userStatus.equals(UserData.USER_REQUESTED) || userStatus.equals(UserData.USER_ARCHIVED)) {
			// Show requested/survey screen
			setContentView(R.layout.activity_waitingpage_layout);
			
		} else {
			// User invalid, show welcome+login screen
			Intent i = new Intent(StartActivity.this, SigninActivity.class);
			startActivity(i);
			finish();
		}
		
		mServerConnectionListner = this;
		
		
	}
	
	public void postRequestUserAccess() {
		//ServerConnection.postRequestUserAccess(OnbordStartActivity.this,null);//working
//		Profile profile = Utils.readProfileData(getApplicationContext());
//		
//		ServerConnection.getDataFetchUserStatus(mServerConnectionListner, OnbordStartActivity.this, profile.getSerialscanner());//working
		//ServerConnection.getDataGetBoughtProducts(OnbordStartActivity.this,"12345678");//working
		//ServerConnection.postPostMonitoringData(OnbordStartActivity.this);//working
		//ServerConnection.postPostProductReview(OnbordStartActivity.this);//working
	}
	
	@Override
	public void onServerConnectionActionComplete(String value) {
		
//		Profile profile = Utils.readProfileData(getApplicationContext());
//		if (profile != null && profile.isLogedin() == Utils.user_requested) {
//			Intent i = new Intent(OnbordStartActivity.this, WaitingPageActivity.class);
//			startActivity(i);
//		} else {
//			Profile profileData = Utils.readProfileData(getApplicationContext());
//			profileData.setLogedin(Utils.user_loggedin_firsttime);
//			Utils.saveProfile(getApplicationContext(), profileData);
//			Intent intent = new Intent(getApplicationContext(), OnbordingActivity.class);
//			startActivity(intent);
//		}
		
	}
}
