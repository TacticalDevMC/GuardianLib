package net.rubixdevelopment.guardianlibApi;

/*
 Created and owned by FloorNectarine
 Created on 01/10/2025 at time 17:12
*/

import java.util.Objects;

public final class GuardianLibProvider {
    private static GuardianLib instance;
    public static void register(GuardianLib impl) { instance = Objects.requireNonNull(impl); }
    public static GuardianLib get() {
        if (instance == null) throw new IllegalStateException("GuardianLib not loaded");
        return instance;
    }
}
