package lu.uni.bicslab.greenbot.android.ui.fragment.help;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.datamodel.IndicatorCategoryModel;
import lu.uni.bicslab.greenbot.android.datamodel.IndicatorModel;
import lu.uni.bicslab.greenbot.android.other.Utils;

/**
 * A fragment representing a list of Items.
 */
public class IndicatorHelpFragment extends Fragment {

    private List<IndicatorHelpItem> indicatorHelpItems;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public IndicatorHelpFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_indicator_help_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            createHelpItemsList(context);

            recyclerView.setAdapter(new IndicatorHelpAdapter(indicatorHelpItems));
        }
        return view;
    }


    public void createHelpItemsList(Context context) {
        //if(indicatorHelpItems != null) return;

        indicatorHelpItems = new ArrayList<>();

        List<IndicatorModel> indicators = Utils.getIndicatorList(context);
        String currentCategoryId = "";
        IndicatorHelpItem currentItem;

        for (IndicatorModel ind: indicators) {
            if(!ind.getCategory_id().equals(currentCategoryId)) {
                currentCategoryId = ind.getCategory_id();
                IndicatorCategoryModel icm = Utils.getIndicatorCategoryById(context, currentCategoryId);

                currentItem = new IndicatorHelpItem();
                currentItem.iconName = icm.getIcon_name();
                currentItem.name = icm.getName();
                currentItem.description = null;
                indicatorHelpItems.add(currentItem);
            }

            currentItem = new IndicatorHelpItem();
            currentItem.iconName = ind.getIcon_name();
            currentItem.name = ind.getName();;
            currentItem.description = ind.getGeneral_description();
            indicatorHelpItems.add(currentItem);
        }


    }

    public class IndicatorHelpItem {
        public String iconName;
        public String name;
        public String description;
    }
}