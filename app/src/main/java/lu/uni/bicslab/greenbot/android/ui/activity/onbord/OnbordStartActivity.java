package lu.uni.bicslab.greenbot.android.ui.activity.onbord;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import lu.uni.bicslab.greenbot.android.MainActivity;
import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.other.Profile;
import lu.uni.bicslab.greenbot.android.other.ServerConnection;
import lu.uni.bicslab.greenbot.android.other.UserData;
import lu.uni.bicslab.greenbot.android.other.Utils;
import lu.uni.bicslab.greenbot.android.ui.activity.scan.SigninActivity;
import lu.uni.bicslab.greenbot.android.ui.activity.ui.WaitingPageActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class OnbordStartActivity extends AppCompatActivity implements ServerConnection.ServerConnectionListner {
	
	Button btn_start;
	ServerConnection.ServerConnectionListner mServerConnectionListner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_onbord_start);
		btn_start = findViewById(R.id.btn_start);
		mServerConnectionListner = this;
		
		// Set up the user interaction to manually show or hide the system UI.
		btn_start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				
				String userStatus = UserData.getStatus(getApplicationContext());
				
				if (userStatus.equals(UserData.USER_VALID)) {
					if (UserData.getFirstTime(getApplicationContext())) {
						//TODO: Show both first-time screens
					}
					
					Intent i = new Intent(OnbordStartActivity.this, MainActivity.class);
					startActivity(i);
					
				} else if (userStatus.equals(UserData.USER_REQUESTED) || userStatus.equals(UserData.USER_ARCHIVED)) {
					
					//TODO: Show requested screen
					
				} else {
					Intent i = new Intent(OnbordStartActivity.this, SigninActivity.class);
					startActivity(i);
				}
			}
		});
		
		
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
