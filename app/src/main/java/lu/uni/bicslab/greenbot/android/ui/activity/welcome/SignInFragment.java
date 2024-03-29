package lu.uni.bicslab.greenbot.android.ui.activity.welcome;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import lu.uni.bicslab.greenbot.android.R;

import lu.uni.bicslab.greenbot.android.other.Utils;
import lu.uni.bicslab.greenbot.android.ui.activity.scan.BarcodeScannerActivity;

public class SignInFragment extends Fragment {
	
	public static final int BARCODE_CAPTURE = 1;

	private EditText signin_id;
	private CheckBox cb_noId;
	
	private String id;

	boolean isFocusCleared = false;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		WelcomeActivity.s_logoLayout.setVisibility(View.VISIBLE);
		isFocusCleared = false;
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

		/* DPERECATED
		scan_directly.setOnClickListener(v -> {
			startActivityForResult(new Intent(getActivity(), BarcodeScannerActivity.class), BARCODE_CAPTURE);
		});
		*/
		scan_directly.setOnClickListener(new View.OnClickListener() {
				 @Override
				 public void onClick(View view) {
					 Context ctx = view.getContext();
					 AlertDialog.Builder infoPopup = new AlertDialog.Builder(ctx);
					 infoPopup.setTitle(view.getContext().getString(R.string.USER_AGREEMENT_TITLE));

					 String content = ctx.getString(R.string.USER_AGREEMENT1);

					 content += " <a href=\"" + ctx.getString(R.string.url_user_agreement) + "\">"
						+ ctx.getString(R.string.USER_AGREEMENT2) + "</a> ";

					 content += ctx.getString(R.string.USER_AGREEMENT3);

					 infoPopup.setMessage(Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY));
					 infoPopup.setPositiveButton("OK", null);
					 AlertDialog d = infoPopup.show();

					 ((TextView)d.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
				 }
			 }
		);


		signin_id = getView().findViewById(R.id.signin_id);
		cb_noId = getView().findViewById((R.id.no_id_cb));

		// Hacky code to keep focus on signin edittext when keyboard appear
		signin_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean b) {
				if (b) {
					// Request focus in a short time because the
					// keyboard may steal it away.
					v.postDelayed(new Runnable() {
						@Override
						public void run() {
							if (!v.hasFocus()) {
								v.requestFocus();
							}
						}
					}, 200);
				}
			}
		});


		signin_id.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// May not be good design as it keeps this Fragment and the WelcomeActivity tightly coupled, but it simplifies this horrible mess of a welcome screen a lot
				WelcomeActivity activity = ((WelcomeActivity) getActivity());

				if (s.length() != 13) {
					signin_id.setError(getResources().getString(R.string.id_digit_alert));
					activity.setIDValid(false);

					if(!isFocusCleared) {
						isFocusCleared = true;
						signin_id.clearFocus(); // Force recover of focus after displaying error
						signin_id.requestFocus();
					}
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

		cb_noId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				WelcomeActivity activity = ((WelcomeActivity) getActivity());

				signin_id.clearFocus();

				if(isChecked) {
					signin_id.setError(null);
					activity.setIDValid(true);
					activity.id = Utils.guestId();
				} else {
					if(signin_id.getText().length() != 13) {
						signin_id.setError(getResources().getString(R.string.id_digit_alert));
						activity.setIDValid(false);
					}
					activity.id = signin_id.getText().toString();
				}
			}
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
