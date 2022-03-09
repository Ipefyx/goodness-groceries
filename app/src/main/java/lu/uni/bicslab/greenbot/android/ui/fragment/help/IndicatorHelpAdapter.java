package lu.uni.bicslab.greenbot.android.ui.fragment.help;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import lu.uni.bicslab.greenbot.android.other.Utils;
import lu.uni.bicslab.greenbot.android.databinding.FragmentIndicatorHelpBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link lu.uni.bicslab.greenbot.android.ui.fragment.help.IndicatorHelpFragment.IndicatorHelpItem}.
 *
 */
public class IndicatorHelpAdapter extends RecyclerView.Adapter<IndicatorHelpAdapter.ViewHolder> {

    private final List<IndicatorHelpFragment.IndicatorHelpItem> items;

    private Context context;
    public int selectedItem;

    public IndicatorHelpAdapter(List<IndicatorHelpFragment.IndicatorHelpItem> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(FragmentIndicatorHelpBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        IndicatorHelpFragment.IndicatorHelpItem item = items.get(position);

        Drawable image = Utils.getDrawableImage(context, item.iconName);
        Glide.with(context).load(image).apply(RequestOptions.centerInsideTransform()).into(holder.mContentIcon);
        //Glide.with(mcontext).load(image).error(R.drawable.ic_menu_gallery).into(iv_product_icon);

        //Glide.with(context).load(image).error(R.drawable.ic_menu_gallery).into(holder.mContentIcon);
        //holder.mContentIcon.setImageDrawable(image);

        holder.mContentView.setText(item.name);

        if(item.description == null) {
            // No description -> Indicator category
            holder.mContentView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            holder.mContentArrow.setVisibility(View.INVISIBLE);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(selectedItem >= 0)
                        notifyItemChanged(selectedItem);

                    selectedItem = -1;
                }
            });

            holder.mContentDescription.setVisibility(View.GONE);

        } else {
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

            holder.mContentDescription.setText(item.description);

            if(position == selectedItem) {
                holder.mContentDescription.setVisibility(View.VISIBLE);
                holder.mContentArrow.setRotation(90);
            } else {
                holder.mContentDescription.setVisibility(View.GONE);
                holder.mContentArrow.setRotation(0);
            }
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

        public ViewHolder(FragmentIndicatorHelpBinding binding) {
            super(binding.getRoot());
            mContentIcon = binding.helpIndicatorsIcon;
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