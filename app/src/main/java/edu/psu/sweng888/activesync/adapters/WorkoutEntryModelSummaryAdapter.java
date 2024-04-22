package edu.psu.sweng888.activesync.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import edu.psu.sweng888.activesync.R;
import edu.psu.sweng888.activesync.dataAccessLayer.viewModels.WorkoutEntryModel;
import edu.psu.sweng888.activesync.databinding.WorkoutEntrySetSummaryItemBinding;

public class WorkoutEntryModelSummaryAdapter extends RecyclerView.Adapter<WorkoutEntryModelSummaryAdapter.ViewHolder> {


    private final Function<WorkoutEntryModel, Boolean> onItemEditClick;
    private final Function<WorkoutEntryModel, Boolean> onItemDeleteClick;
    private final ArrayList<WorkoutEntryModel> models;

    public WorkoutEntryModelSummaryAdapter(
        List<WorkoutEntryModel> items,
        Function<WorkoutEntryModel, Boolean> onItemEditClick,
        Function<WorkoutEntryModel, Boolean> onItemDeleteClick
    ) {
        this.models = new ArrayList<>();
        this.models.addAll(items);
        this.onItemEditClick = onItemEditClick;
        this.onItemDeleteClick = onItemDeleteClick;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<WorkoutEntryModel> items) {
        this.models.clear();
        this.models.addAll(items);
        this.notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder vh = new ViewHolder(
            LayoutInflater.from(parent.getContext())
                .inflate(R.layout.workout_entry_set_summary_item, parent, false)
        );
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WorkoutEntryModel model = this.models.get(holder.getAdapterPosition());
        holder.setupText(model);
        holder.getDeleteButton().setOnClickListener(__ -> {
            if (this.onItemDeleteClick.apply(model)) {
                this.models.remove(holder.getAdapterPosition());
                this.notifyItemRemoved(holder.getAdapterPosition());
            }
        });
        holder.getEditButton().setOnClickListener(__ -> {
            this.onItemEditClick.apply(model);
        });
    }

    @Override
    public int getItemCount() {
        return this.models.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final WorkoutEntrySetSummaryItemBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = WorkoutEntrySetSummaryItemBinding.bind(itemView);
        }

        public ImageButton getDeleteButton() {
            return binding.workoutSummaryItemButtonDelete;
        }

        public ImageButton getEditButton() {
            return binding.workoutSummaryItemButtonEdit;
        }

        @SuppressLint("SetTextI18n")
        public void setupText(WorkoutEntryModel model) {
            // Populate the exercise name label
            binding.workoutSummaryItemLabelExerciseName.setText(model.exerciseType.exerciseType.name);
            // Determine the number of sets and the total weight lifted (in pounds) and display
            // the info in the "details" text.
            String setWord = "set";
            int numSets = model.sets.size();
            if (numSets != 1) {
                setWord += "s";
            }
            double totalWeightLifted = model.sets.stream()
                .map(set -> set.weight.asPounds().amount)
                .reduce(0.0, Double::sum);
            binding.workoutSummaryItemLabelExerciseDescription.setText(
                numSets + " " + setWord + " (Total weight: " + (int) Math.round(totalWeightLifted) + " lbs)"
            );
        }
    }
}
