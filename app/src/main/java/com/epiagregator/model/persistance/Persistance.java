package com.epiagregator.model.persistance;

import com.epiagregator.model.persistance.userprofile.IUserProfilePersistance;
import com.epiagregator.utils.ReferenceProvider;

/**
 * Created by etien on 23/01/2017.
 */
public final class Persistance {
    /**
     * The reference to {@link IUserProfilePersistance} implementation.
     */
    private static final ReferenceProvider<IUserProfilePersistance> mUserProfilePersistance = new ReferenceProvider<>("UserProfilePersistance");

    /**
     * Suppresses default constructor, ensuring non-instantiability.
     */
    private Persistance() {
    }

    /**
     * Gets the reference to {@link IUserProfilePersistance} implementation.
     *
     * @return the reference to {@link IUserProfilePersistance} implementation.
     */
    public static IUserProfilePersistance getUserProfilePersistance() {
        return mUserProfilePersistance.get();
    }

    /**
     * Sets the reference of {@link IUserProfilePersistance} implementation.
     *
     * @param impl the reference of {@link IUserProfilePersistance} implementation.
     */
    public static void setUserProfilePersistance(IUserProfilePersistance impl) {
        mUserProfilePersistance.set(impl);
    }
}