package com.example.rma_2_anis_karic;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
        private CardView tileCard;
        private TextView tileText;

        public TileViewHolder(@NonNull View itemView) {
            super(itemView);
            tileCard = (CardView) itemView.findViewById(R.id.tileCard);
            tileText = (TextView) itemView.findViewById(R.id.tileText);
        }

        public void bind(Tile tile){
            Log.d("TileAdapter", "Binding tile at (" + tile.getRow() + ", " + tile.getCol() + ") with value: " + tile.getValue());
            if (tile.getValue() > 0)
                tileText.setText(String.valueOf(tile.getValue()));
            else
                tileText.setText("");

            setTileColor(tileCard,tileText,tile.getValue());
        }

        private void setTileColor(CardView cardView,TextView textView, int tileValue) {
            switch (tileValue) {
                case 0:
                    cardView.setBackgroundColor(Color.WHITE);
                    break;
                case 2:
                    cardView.setBackgroundColor(Color.parseColor("#388E3C"));
                    break;
                case 4:
                    cardView.setBackgroundColor(Color.parseColor("#00796B"));
                    break;
                case 8:
                    cardView.setBackgroundColor(Color.BLUE);
                    break;
                case 16:
                    cardView.setBackgroundColor(Color.CYAN);
                    break;
                case 32:
                    cardView.setBackgroundColor(Color.parseColor("#0288D1"));
                    break;
                case 64:
                    cardView.setBackgroundColor(Color.MAGENTA);
                    break;
                case 128:
                    cardView.setBackgroundColor(Color.LTGRAY);
                    break;
                case 256:
                    cardView.setBackgroundColor(Color.parseColor("#C2185B"));
                    break;
                case 512:
                    cardView.setBackgroundColor(Color.parseColor("#E64A19"));
                    break;
                case 1024:
                    cardView.setBackgroundColor(Color.YELLOW);
                    break;
                case 2048:
                    cardView.setBackgroundColor(Color.RED);
                    break;
                default:
                    cardView.setBackgroundColor(Color.BLACK);
                    textView.setTextColor(Color.RED);
            }
        }

    }
        public static int dpToPx(Context context, float dp) {
            return Math.round(dp * context.getResources().getDisplayMetrics().density);
        }

}
