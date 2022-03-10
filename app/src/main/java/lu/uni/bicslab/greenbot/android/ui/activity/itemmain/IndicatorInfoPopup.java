package lu.uni.bicslab.greenbot.android.ui.activity.itemmain;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.datamodel.IndicatorModel;
import lu.uni.bicslab.greenbot.android.datamodel.SubIndicatorModel;
import lu.uni.bicslab.greenbot.android.other.Utils;

public class IndicatorInfoPopup extends Dialog {

    private Context mContext;
    private IndicatorModel indicator;
    private ImageView indicatorIconView;
    private TextView indicatorNameView;
    private LinearLayout descriptionLayout;
    private Button okBtn;


    public IndicatorInfoPopup(@NonNull Context context, IndicatorModel indicator) {
        super(context, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.popup_indicator_info);

        this.mContext = context;
        this.indicator = indicator;

        this.indicatorIconView = findViewById(R.id.indicatorIconImage);
        this.indicatorNameView = findViewById(R.id.indicatorNameText);
        this.descriptionLayout = findViewById(R.id.descriptionsLayout);
        this.okBtn = findViewById(R.id.popupIndOkBtn);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void build() {

        Drawable img = Utils.getDrawableImage(mContext, indicator.getIcon_name());
        Glide.with(mContext).load(img).apply(RequestOptions.centerInsideTransform()).into(indicatorIconView);
        indicatorNameView.setText(indicator.getName());

        for(SubIndicatorModel sub : indicator.sub_indicators) {
            TextView subName = new TextView(mContext);
            TextView subDesc = new TextView(mContext);
            TextView more = new TextView(mContext);

            LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            txtParams.setMargins(0,2,0,4);

            subName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            subName.setLayoutParams(txtParams);
            subName.setText(sub.getName());
            subDesc.setLayoutParams(txtParams);
            subDesc.setText(sub.getDescription());


            descriptionLayout.addView(subName);
            descriptionLayout.addView(subDesc);

            if(sub.getFile() != null) {
                String ref = "<a href=\"" + sub.getFile() + "\">" + mContext.getString(R.string.moreinfo) + "</a>";
                more.setLayoutParams(txtParams);
                more.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                more.setGravity(Gravity.RIGHT);
                more.setMovementMethod(LinkMovementMethod.getInstance());
                more.setText(Html.fromHtml(ref));
                descriptionLayout.addView(more);
            }
        }

        show();
    }
}
