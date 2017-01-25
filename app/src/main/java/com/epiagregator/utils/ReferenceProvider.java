package com.epiagregator.utils;

/**
 * Created by etien on 23/01/2017.
 */
public class ReferenceProvider<T> {
    /**
     * The name of the referenced item.
     */
    private final String mName;

    /**
     * The current reference value.
     */
    private T mReference;

    /**
     * Initializes this instance with the give name and {@code null} reference.
     *
     * @param name the name of the referenced item.
     * @throws IllegalArgumentException {@code name} is missing
     */
    public ReferenceProvider(
            String name) {

        if (name == null)
            throw new IllegalArgumentException("name is missing");

        mName = name;
        mReference = null;
    }

    /**
     * Resolves the current value of the reference.
     *
     * @return The current value of the reference if found; otherwise
     * an exception is thrown
     * @throws IllegalStateException no reference found.
     */
    public T get() {
        if (mReference == null)
            throw new IllegalStateException(String.format("%s is uninitialized", mName));

        return mReference;
    }

    /**
     * Sets the reference value to the specified value.
     *
     * @param reference the reference to set.
     */
    public void set(T reference) {
        mReference = reference;
    }
}