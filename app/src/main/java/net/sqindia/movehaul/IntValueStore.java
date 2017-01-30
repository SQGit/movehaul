package net.sqindia.movehaul;

/**
 * Created by SQINDIA on 1/30/2017.
 */


public class IntValueStore {

    /**
     * The current value.
     */
    String mValue;

    /**
     * The listener (you might want turn this into an array to support many
     * listeners)
     */
    private IntValueStoreListener mListener;

    /**
     * Construct a the int store.
     *
     * @param initialValue The initial value.
     */
    public IntValueStore(String initialValue) {
        mValue = initialValue;
    }

    /**
     * Sets a listener on the store. The listener will be modified when the
     * value changes.
     *
     * @param listener The {@link IntValueStoreListener}.
     */
    public void setListener(IntValueStoreListener listener) {
        mListener = listener;
    }

    /**
     * Set a new int value.
     *
     * @param newValue The new value.
     */
    public void setValue(String newValue) {
        mValue = newValue;
        if (mListener != null) {
            mListener.onValueChanged(mValue);
        }
    }

    /**
     * Get the current value.
     *
     * @return The current int value.
     */
    public String getValue() {
        return mValue;
    }

    /**
     * Callbacks by {@link }.
     */
    public static interface IntValueStoreListener {
        /**
         * Called when the value of the int changes.
         *
         * @param newValue The new value.
         */
        void onValueChanged(String newValue);
    }
}