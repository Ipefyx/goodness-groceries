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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.ui.activity.itemmain.ProductDetailsActivity;
import lu.uni.bicslab.greenbot.android.ui.activity.scan.BarcodeScannerActivity;

public class SignInFragment extends Fragment {
	
	public static final int BARCODE_CAPTURE = 1;
	
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
		
		TextView scan_directly = getView().findViewById(R.id.scan_directly);
		scan_directly.setOnClickListener(v -> {
			startActivityForResult(new Intent(getActivity(), BarcodeScannerActivity.class), BARCODE_CAPTURE);
		});
		
		signin_id = getView().findViewById(R.id.signin_id);
		signin_id.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// May not be good design as it keeps this Fragment and the WelcomeActivity tightly coupled, but it simplifies this horrible mess of a welcome screen a lot
				WelcomeActivity activity = ((WelcomeActivity) getActivity());
				
				if (s.length() != 13) {
					signin_id.setError(getResources().getString(R.string.id_digit_alert));
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
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == BARCODE_CAPTURE) {
			if (data != null) {
				String barcode = data.getStringExtra("barcode");
				Log.e("TAG", "Barcode read:final " + barcode);
				
				signin_id.setText(barcode);
			} else {
				Log.e("TAG", "No barcode captured, intent data is null");
			}
			
		}
	}
}
