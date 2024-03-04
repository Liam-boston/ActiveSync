package edu.psu.sweng888.activesync.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

/**
 * RecyclerView adapter that handles the creation of ViewHolders and the binding of events
 * for the workout entry screen's set creator.
 */
public class WorkoutSetAdapter extends RecyclerView.Adapter<WorkoutSetAdapter.ViewHolder> {

    private final List<WorkoutSet> sets = new ArrayList<>();

    public WorkoutSetAdapter(Long workoutId, List<WorkoutSet> sets) {
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

    public WorkoutSet addBlankSet(Long workoutId) {
        WorkoutSet blankSet = createBlankSet(workoutId);
        this.sets.add(blankSet);
        this.notifyItemInserted(this.sets.size());
        return blankSet;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView setIndexLabel;
        private final EditText numRepsInput;
        private final EditText weightAmountInput;
        private final ToggleButton weightUnitToggle;

        private int currentlyHeldIndex = -1;

        public ViewHolder(View view) {
            super(view);
            // Bind references for inputs
            setIndexLabel = view.findViewById(R.id.workout_set_entry_index_label);
            numRepsInput = view.findViewById(R.id.workout_set_entry_num_reps_input);
            weightAmountInput = view.findViewById(R.id.workout_set_entry_weight_input);
            weightUnitToggle = view.findViewById(R.id.workout_set_entry_weight_unit_toggle);
        }

        public EditText getNumRepsInput() { return numRepsInput; }
        public EditText getWeightAmountInput() { return weightAmountInput; }
        public ToggleButton getWeightUnitToggle() { return weightUnitToggle; }
        public int getCurrentlyHeldIndex() { return currentlyHeldIndex; }
        public void setCurrentlyHeldIndex(int index) {
            this.currentlyHeldIndex = index;
            this.setIndexLabel.setText("Set " + (index + 1));
        }
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
            // Short-circuit if we don't have any held item. Otherwise, get a reference to the item
            // that is modeled by this input
            int index = viewHolder.getCurrentlyHeldIndex();
            if (index < 0) return;
            WorkoutSet set = this.sets.get(index);

            // When the input loses focus, use the new value of the input to update the
            // backing model
            /*
            if (!hasFocus) {
                set.reps = Integer.parseInt(((EditText) v).getText().toString());
            }
            //*/
        });

        viewHolder.getWeightAmountInput().setOnFocusChangeListener((v, hasFocus) -> {
            // Short-circuit if we don't have any held item. Otherwise, get a reference to the item
            // that is modeled by this input
            int index = viewHolder.getCurrentlyHeldIndex();
            if (index < 0) return;
            WorkoutSet set = this.sets.get(index);

            // When the input loses focus, use the new value of the input to update the
            // backing model
            if (!hasFocus) {
                set.weight.amount = Double.parseDouble(((EditText) v).getText().toString());
            }
        });

        viewHolder.getWeightUnitToggle().setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Short-circuit if we don't have any held item. Otherwise, get a reference to the item
            // that is modeled by this input
            int index = viewHolder.getCurrentlyHeldIndex();
            if (index < 0) return;
            WorkoutSet set = this.sets.get(index);

            // Set the workout weight unit type based on the state of the toggle button.
            // When the button is "off", it shows pounds; otherwise, it shows "kg".
            set.weight.unit = toggleStateToWeightUnit(isChecked);
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
        holder.setCurrentlyHeldIndex(position);
        WorkoutSet set = this.sets.get(position);
        holder.getNumRepsInput().setText(String.valueOf(set.reps));
        holder.getWeightAmountInput().setText(String.valueOf(set.weight.amount));
        holder.getWeightUnitToggle().setChecked(weightUnitToToggleState(set.weight.unit));
    }

    @Override
    public int getItemCount() { return this.sets.size(); }
}
