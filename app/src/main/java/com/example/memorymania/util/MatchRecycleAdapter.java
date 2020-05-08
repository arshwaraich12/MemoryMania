package com.example.memorymania.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymania.R;
import com.example.memorymania.data.Product;
import com.example.memorymania.databinding.MatchCardBinding;
import com.example.memorymania.ui.MatchActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MatchRecycleAdapter extends RecyclerView.Adapter<MatchRecycleAdapter.MatchViewHolder> {
    private final static Integer NUM_MATCHES = 2;

    private List<Product> dataset;
    private List<Integer> matchedIndex;
    private Context context;

    class MatchViewHolder extends RecyclerView.ViewHolder {
        final MatchCardBinding item;

        public void bind(final Product product, final int position) {
            item.setMatchState(product.getMatchState());
            item.getRoot().setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showLogic(position);
                        }
                    }
            );
            item.executePendingBindings();
        }

        private void showLogic(Integer index) {
            if(item.getMatchState() == Product.MatchState.HIDDEN) {
                item.setMatchState(Product.MatchState.SHOWN);
                item.executePendingBindings();

                matchedIndex.add(index);

                if(!isMatch(index)) {
                    hideLogic();
                } else if(matchedIndex.size() >= NUM_MATCHES) {
                    matchLogic();
                }
            } else if(item.getMatchState() == Product.MatchState.SHOWN) {
                item.setMatchState(Product.MatchState.HIDDEN);
                item.executePendingBindings();
                hideLogic();
            }
        }

        // TODO: Improve looping
        private Boolean isMatch(Integer index) {
            boolean ret = true;
            long currId = dataset.get(index).getId();
            for(int i: matchedIndex) {
                ret = ret && (dataset.get(i).getId() == currId);
            }
            return ret;
        }

        // TODO: Improve looping
        private void hideLogic() {
            for(int i: matchedIndex) {
                dataset.get(i).setMatchState(Product.MatchState.HIDDEN);
                notifyDataSetChanged();
            }
            matchedIndex.clear();
        }

        // TODO: Improve looping
        private void matchLogic() {
            for(int i: matchedIndex) {
                dataset.get(i).setMatchState(Product.MatchState.MATCHED);
                notifyDataSetChanged();
            }
            matchedIndex.clear();
            ((MatchActivity) context).incrementMatches(NUM_MATCHES);
        }

        MatchViewHolder(MatchCardBinding itemView) {
            super(itemView.getRoot());
            item = itemView;
        }
    }

    public MatchRecycleAdapter(List<Product> dataset, Context context) {
        this.dataset = dataset;
        this.matchedIndex = new ArrayList<>();
        this.context = context;
        this.shuffleProducts();
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MatchCardBinding matchCardBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.match_card, parent, false);
        return new MatchViewHolder(matchCardBinding);
    }

    @Override
    public void onBindViewHolder(MatchViewHolder holder, final int position) {
        ViewGroup viewGroup = (ViewGroup) holder.item.getRoot();
        ImageView imageView = (ImageView) viewGroup.getChildAt(1);
        Picasso.get()
                .load(dataset.get(position).getImage().getSrc())
                .error(R.drawable.ic_broken_image_black_24dp)
                .into(imageView);

        holder.bind(dataset.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void shuffleProducts() {
        Collections.shuffle(dataset);
        notifyDataSetChanged();
    }
}
