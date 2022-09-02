package lu.uni.bicslab.greenbot.android.ui.fragment.help;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import lu.uni.bicslab.greenbot.android.R;
import lu.uni.bicslab.greenbot.android.datamodel.ProductCategoryModel;
import lu.uni.bicslab.greenbot.android.other.Utils;

/**
 * A fragment representing a list of Items.
 */
public class CategoryHelpFragment extends Fragment {

    public CategoryHelpFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category_help_list, container, false);

        if(view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            List<ProductCategoryModel> categoryModels = Utils.getProductCategoryList(context);

            recyclerView.setAdapter(new CategoryHelpAdapter(categoryModels));
        }

        // Inflate the layout for this fragment
        return view;
    }
}