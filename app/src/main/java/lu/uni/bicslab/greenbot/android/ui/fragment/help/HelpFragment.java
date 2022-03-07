package lu.uni.bicslab.greenbot.android.ui.fragment.help;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.other.Utils;

public class HelpFragment extends Fragment {

	private Context mContext;
	
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_help, container, false);
		mContext = getContext();

		// Create hyperlinks to web urls
		TextView text;

		//	To help page
		text = (TextView) root.findViewById(R.id.help_help_text);
		Utils.linkify(mContext, text, getResources().getString(R.string.url_help_page));

		//	To contact page
		text = (TextView) root.findViewById((R.id.help_contact_text));
		Utils.linkify(mContext, text, getResources().getString(R.string.url_contact_us));



		return root;
	}

}
