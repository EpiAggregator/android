package com.epiagregator.model.providers;


import com.epiagregator.utils.ReferenceProvider;

public final class Providers {
    private static final ReferenceProvider<IAccountProvider> mAccountProvider = new ReferenceProvider<>("AccountProvider");

    private Providers() {
    }

    public static IAccountProvider getAccountProvider() {
        return mAccountProvider.get();
    }

    public static void setAccountProvider(IAccountProvider provider) {
        mAccountProvider.set(provider);
    }
}