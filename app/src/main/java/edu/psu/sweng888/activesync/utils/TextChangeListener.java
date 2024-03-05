package edu.psu.sweng888.activesync.utils;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Inspired by this StackOverflow answer about watching EditText instances for changes:
 * <a href="https://stackoverflow.com/a/39624625">https://stackoverflow.com/a/39624625</a>
 */
public abstract class TextChangeListener <TWatched> implements TextWatcher {

    private TWatched watchTarget;

    public TextChangeListener(TWatched target) {
        this.watchTarget = target;
    }

    /**
     * Event handler that fires when the text in this listener's watched object changes.
     */
    public abstract void handleTextChange(TWatched target, Editable editable);

    @Override
    public void beforeTextChanged(CharSequence seq, int start, int count, int after) {
        // Intentionally left blank.
    }

    @Override
    public void onTextChanged(CharSequence seq, int start, int before, int count) {
        // Intentionally left blank.
    }

    @Override
    public void afterTextChanged(Editable editable) {
        this.handleTextChange(this.watchTarget, editable);
    }
}
