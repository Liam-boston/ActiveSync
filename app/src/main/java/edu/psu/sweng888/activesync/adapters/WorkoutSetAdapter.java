package edu.psu.sweng888.activesync.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.psu.sweng888.activesync.dataAccessLayer.models.Weight;
import edu.psu.sweng888.activesync.dataAccessLayer.models.WeightUnit;
import edu.psu.sweng888.activesync.dataAccessLayer.models.WorkoutSet;
import edu.psu.sweng888.activesync.R;
import edu.psu.sweng888.activesync.eventListeners.WorkoutSetAddListener;
import edu.psu.sweng888.activesync.eventListeners.WorkoutSetChangeListener;
import edu.psu.sweng888.activesync.eventListeners.WorkoutSetDeleteListener;

/**
 * RecyclerView adapter that handles the creation of ViewHolders and the binding of events
 * for the workout entry screen's set creator.
 */
public class WorkoutSetAdapter extends RecyclerView.Adapter<WorkoutSetAdapter.ViewHolder> {

    private final List<WorkoutSet> sets = new ArrayList<>();

    private final WorkoutSetAddListener addListener;
    private final WorkoutSetChangeListener changeListener;
    private final WorkoutSetDeleteListener deleteListener;

    public WorkoutSetAdapter(
        Long workoutId,
        List<WorkoutSet> sets,
        WorkoutSetAddListener addListener,
        WorkoutSetChangeListener changeListener,
        WorkoutSetDeleteListener deleteListener
    ) {
        this.addListener = addListener;
        this.changeListener = changeListener;
        this.deleteListener = deleteListener;
        if (sets.isEmpty()) {
            addBlankSet(workoutId);
        }
        else {
            this.sets.addAll(sets);
        }
    }

    private static WorkoutSet createBlankSet(Long workoutId) {
        return new WorkoutSet(
            null,
            workoutId,
            0,
            new Weight(WeightUnit.Pounds, 0)
        );
    }

    public void addBlankSet(Long workoutId) {
        WorkoutSet blankSet = createBlankSet(workoutId);
        this.sets.add(blankSet);
        this.notifyItemInserted(this.sets.size());
        if (this.addListener != null) {
            this.addListener.handleAdd(blankSet);
        }
    }

    public void deleteSet(int position) {
        // Guard against out-of-range access
        if (position >= this.sets.size() || position < 0) return;

        // Delete the set at the given position, invoking any registered callback
        WorkoutSet deletedSet = this.sets.get(position);
        this.sets.remove(position);
        this.notifyDataSetChanged(); // HACK: Force a re-render of everything since the index text wasn't changing... fix this bug later.
        if (this.deleteListener != null) {
            this.deleteListener.handleDelete(position, deletedSet);
        }
    }

    private void invokeChangeHandler(int updatedIndex, WorkoutSet updatedSet) {
        if (this.changeListener != null) {
            this.changeListener.handleChange(updatedIndex, updatedSet);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView setIndexLabel;
        private final EditText numRepsInput;
        private final EditText weightAmountInput;
        private final ToggleButton weightUnitToggle;
        private final ImageButton deleteSetButton; // TODO: Do we really need this to be an "ImageButton" type? Will "Button" suffice?

        public ViewHolder(View view) {
            super(view);
            // Bind references for inputs
            setIndexLabel = view.findViewById(R.id.workout_set_entry_index_label);
            numRepsInput = view.findViewById(R.id.workout_set_entry_num_reps_input);
            weightAmountInput = view.findViewById(R.id.workout_set_entry_weight_input);
            weightUnitToggle = view.findViewById(R.id.workout_set_entry_weight_unit_toggle);
            deleteSetButton = view.findViewById(R.id.workout_set_entry_delete_button);
        }

        public EditText getNumRepsInput() { return numRepsInput; }
        public EditText getWeightAmountInput() { return weightAmountInput; }
        public ToggleButton getWeightUnitToggle() { return weightUnitToggle; }
        public ImageButton getDeleteSetButton() { return deleteSetButton; }
        public void setIndexLabel(int index) {
            this.setIndexLabel.setText("Set " + index);
        }
    }

    /**
     * Helper method used to determine whether or not the given position (index) is in range for
     * the underlying list of items being displayed by this adapter. During the handling of certain
     * events, the position passed in (e.g., from "getAdapterPosition") may be negative to indicate
     * that an item has already been removed. This function facilitates checking for such cases
     * before attempting to index into a list with the negative position.
     * @param position The position of interest.
     * @return `true` if the given position is in range and can safely be used to select a list element; otherwise, `false`.
     */
    private boolean positionInRange(int position) {
        return position >= 0 && position < this.sets.size();
    }

    @NonNull
    @Override
    public WorkoutSetAdapter.ViewHolder onCreateViewHolder(
        @NonNull ViewGroup parent,
        int viewType
    ) {
        // Create the view holder for a workout set entry item
        ViewHolder viewHolder = new ViewHolder(
            LayoutInflater.from(
                parent.getContext()
            ).inflate(
                R.layout.workout_set_entry_item,
                parent,
                false
            )
        );

        // TODO: Use actual two-way data binding instead of custom impl.:
        //       https://developer.android.com/topic/libraries/data-binding/two-way
        // Bind event handlers for the inputs to change the fields of the currently held model.
        viewHolder.getNumRepsInput().setOnFocusChangeListener((v, hasFocus) -> {
            // When the input loses focus, use the new value of the input to update the
            // backing model
            int index = viewHolder.getAdapterPosition();
            if (!positionInRange(index)) return;
            WorkoutSet set = this.sets.get(index);
            if (!hasFocus) {
                int previousReps = set.reps;
                set.reps = Integer.parseInt(((EditText) v).getText().toString());
                if (previousReps != set.reps) {
                    invokeChangeHandler(index, set);
                }
            }
        });

        viewHolder.getWeightAmountInput().setOnFocusChangeListener((v, hasFocus) -> {
            // When the input loses focus, use the new value of the input to update the
            // backing model
            int index = viewHolder.getAdapterPosition();
            if (!positionInRange(index)) return;
            WorkoutSet set = this.sets.get(index);
            if (!hasFocus) {
                double previousAmount = set.weight.amount;
                set.weight.amount = Double.parseDouble(((EditText) v).getText().toString());
                if (previousAmount != set.weight.amount) {
                    invokeChangeHandler(index, set);
                }
            }
        });

        viewHolder.getWeightUnitToggle().setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Set the workout weight unit type based on the state of the toggle button.
            // When the button is "off", it shows pounds; otherwise, it shows "kg".
            int index = viewHolder.getAdapterPosition();
            if (!positionInRange(index)) return;
            WorkoutSet set = this.sets.get(index);
            set.weight.unit = toggleStateToWeightUnit(isChecked);

            // Invoke the change handler
            invokeChangeHandler(index, set);
        });

        viewHolder.getDeleteSetButton().setOnClickListener(v -> {
            // Invoke delete logic
            int index = viewHolder.getAdapterPosition();
            if (!positionInRange(index)) return;
            deleteSet(index);
        });

        return viewHolder;
    }

    private static boolean weightUnitToToggleState(WeightUnit unit) {
        // When the toggle is "on" (true), we consider it to be representing kilograms. The only
        // other state it can hold is "off" (false), which would be pounds, the only other value
        // of "unit" here. We use this expression as shorthand for a more verbose switch statement
        // over the two possible values of WeightUnit.
        return unit == WeightUnit.Kilograms;
    }

    private static WeightUnit toggleStateToWeightUnit(boolean toggleOn) {
        return toggleOn ? WeightUnit.Kilograms : WeightUnit.Pounds;
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutSetAdapter.ViewHolder holder, int position) {
        // Bind the current item's details to the ViewHolder's views.
        holder.setIndexLabel(holder.getAdapterPosition() + 1);
        WorkoutSet set = this.sets.get(position);
        holder.getNumRepsInput().setText(String.valueOf(set.reps));
        holder.getWeightAmountInput().setText(String.valueOf(set.weight.amount));
        holder.getWeightUnitToggle().setChecked(weightUnitToToggleState(set.weight.unit));
    }

    @Override
    public int getItemCount() { return this.sets.size(); }
}
