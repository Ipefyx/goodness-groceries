package lu.uni.bicslab.greenbot.android.ui.activity.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import lu.uni.bicslab.greenbot.android.R;

public class SignInFragment extends Fragment {
	
	public static final int RC_BARCODE_CAPTURE = 9001;
	
	private EditText signin_id;
	
	private String id;
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.onbording_signin, container);
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		signin_id = getView().findViewById(R.id.signin_id);
		signin_id.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// May not be good design as it keeps this Fragment and the WelcomeActivity tightly coupled, but it simplifies this horrible mess of a welcome screen a lot
				WelcomeActivity activity = ((WelcomeActivity) getActivity());
				
				if (s.length() != 13) {
					signin_id.setError("id must be 13 digits long");
					activity.setIDValid(false);
				} else {
					signin_id.setError(null);
					activity.setIDValid(true);
				}
				
				if (s.length() == 0) {
					signin_id.setError(null);
				}
				
				activity.id = s.toString();
			}
			
			@Override public void afterTextChanged(Editable s) {}
			@Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		});
	}
}
