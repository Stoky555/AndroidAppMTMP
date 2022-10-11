package com.example.mtmp;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CalculationAdapter extends RecyclerView.Adapter<CalculationAdapter.CalculationViewHolder> {
    private final ArrayList<PointItem> mPointItemArrayList;

    public CalculationAdapter(ArrayList<PointItem> mPointItemArrayList) {
        this.mPointItemArrayList = mPointItemArrayList;
    }

    @NonNull
    @Override
    public CalculationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_row, parent, false);
        return new CalculationViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull CalculationViewHolder holder, int position) {
        PointItem currentItem = mPointItemArrayList.get(position);

        holder.mCalculationTimeField.setText(String.format("%.4f", currentItem.getTimeData()));
        holder.mCalculationXField.setText(String.format("%.4f", currentItem.getxData()));
        holder.mCalculationYField.setText(String.format("%.4f", currentItem.getyData()));
    }

    @Override
    public int getItemCount() {
        return mPointItemArrayList.size();
    }

    public static class CalculationViewHolder extends RecyclerView.ViewHolder{
        private final TextView mCalculationTimeField;
        private final TextView mCalculationXField;
        private final TextView mCalculationYField;

        public CalculationViewHolder(@NonNull View itemView){
            super(itemView);
            mCalculationTimeField = itemView.findViewById(R.id.calculationTimeField);
            mCalculationXField = itemView.findViewById(R.id.calculationXField);
            mCalculationYField = itemView.findViewById(R.id.calculationYField);
        }
    }
}
