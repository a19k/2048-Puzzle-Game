package com.example.rma_2_anis_karic;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class TileAdapter extends ListAdapter<Tile, TileAdapter.TileViewHolder> {

    private static final DiffUtil.ItemCallback<Tile> DIFF_CALLBACK = new DiffUtil.ItemCallback<Tile>() {
        @Override
        public boolean areItemsTheSame(@NonNull Tile oldItem, @NonNull Tile newItem) {
            return (oldItem.getRow() == newItem.getRow() && oldItem.getCol()== newItem.getCol());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Tile oldItem, @NonNull Tile newItem) {
            return oldItem.equals(newItem);
        }
    };

    public TileAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public TileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tile, parent, false);
        return new TileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TileViewHolder holder, int position) {
        int spanCount = 4; // or from GridLayoutManager
        int totalSpacing = dpToPx(holder.itemView.getContext(),64); // total spacing between items (px)

        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int tileSize = (screenWidth - totalSpacing) / spanCount;

        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        params.width = tileSize;
        params.height = tileSize;
        holder.itemView.setLayoutParams(params);

        Tile currentTile = getItem(position);
        holder.bind(currentTile);
    }


    static class TileViewHolder extends RecyclerView.ViewHolder{
        private final CardView tileCard;
        private final TextView tileText;

        public TileViewHolder(@NonNull View itemView) {
            super(itemView);
            tileCard = itemView.findViewById(R.id.tileCard);
            tileText = itemView.findViewById(R.id.tileText);
        }

        public void bind(Tile tile){
            if (tile.getValue() > 0)
                tileText.setText(String.valueOf(tile.getValue()));
            else
                tileText.setText("");

            setTileColor(tileCard,tileText,tile.getValue());

        }

        private void setTileColor(CardView cardView,TextView textView, int tileValue) {
            int colorResId;
            switch (tileValue) {
                case 2:    colorResId = R.color.purple; break;
                case 4:    colorResId = R.color.bluedark; break;
                case 8:    colorResId = R.color.blue; break;
                case 16:   colorResId = R.color.teal; break;
                case 32:   colorResId = R.color.green; break;
                case 64:   colorResId = R.color.lime; break;
                case 128:  colorResId = R.color.piss; break;
                case 256:  colorResId = R.color.yellow; break;
                case 512:  colorResId = R.color.orange; break;
                case 1024: colorResId = R.color.pink; break;
                case 2048: colorResId = R.color.red; break;
                default:   colorResId = R.color.white;
            }
            cardView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), colorResId));
            }
    }
        public static int dpToPx(Context context, float dp) {
            return Math.round(dp * context.getResources().getDisplayMetrics().density);
        }

}
