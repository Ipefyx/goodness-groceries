package lu.uni.bicslab.greenbot.android.ui.fragment.help;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import lu.uni.bicslab.greenbot.android.databinding.FragmentCategoryHelpBinding;
import lu.uni.bicslab.greenbot.android.databinding.FragmentIndicatorHelpBinding;
import lu.uni.bicslab.greenbot.android.datamodel.ProductCategoryModel;
import lu.uni.bicslab.greenbot.android.other.Utils;

public class CategoryHelpAdapter extends RecyclerView.Adapter<CategoryHelpAdapter.ViewHolder> {

    private final List<ProductCategoryModel> items;

    private Context context;
    public int selectedItem = -1;

    public CategoryHelpAdapter(List<ProductCategoryModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CategoryHelpAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(FragmentCategoryHelpBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHelpAdapter.ViewHolder holder, int position) {
        ProductCategoryModel item = items.get(position);

        Drawable image = Utils.getDrawableImage(context, item.getIcon_name());
        Glide.with(context).load(image).apply(RequestOptions.centerInsideTransform()).into(holder.mContentIcon);

        holder.mContentView.setText(item.getName());


        holder.mContentView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        holder.mContentArrow.setVisibility(View.VISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedItem >= 0)
                    notifyItemChanged(selectedItem);

                selectedItem = holder.getAdapterPosition();
                notifyItemChanged(selectedItem);
            }
        });

        holder.mContentDescription.setText(item.getDescription());

        if(position == selectedItem) {
            holder.mContentDescription.setVisibility(View.VISIBLE);
            holder.mContentArrow.setRotation(90);
        } else {
            holder.mContentDescription.setVisibility(View.GONE);
            holder.mContentArrow.setRotation(0);
        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mContentIcon;
        public final ImageView mContentArrow;
        public final TextView mContentView;
        public final TextView mContentDescription;

        public ViewHolder(@NonNull FragmentCategoryHelpBinding binding) {
            super(binding.getRoot());
            mContentIcon = binding.helpCategoryIcon;
            mContentArrow = binding.iconArrow;
            mContentView = binding.content;
            mContentDescription = binding.descriptionText;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
