package lu.uni.bicslab.greenbot.android.other;

import java.util.List;

import lu.uni.bicslab.greenbot.android.datamodel.IndicatorModel;

public interface UpdateFeedbackListener {
	void updateFeedbackAction(boolean isUpdated, List<IndicatorModel> mIndicatorModel, int pos, int itemposchnged);
	
}
