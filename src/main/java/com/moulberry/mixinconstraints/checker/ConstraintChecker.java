package com.moulberry.mixinconstraints.checker;

import com.moulberry.mixinconstraints.MixinConstraints;
import com.moulberry.mixinconstraints.util.Abstractions;

import java.lang.reflect.Method;

public class ConstraintChecker {
    /**
     * Check if *ANY* of the modIds provided are loaded
     */
    public static boolean checkModLoaded(String main, Iterable<String> aliases, String minVersion, String maxVersion) {
        if (isModLoadedWithinVersion(main, minVersion, maxVersion)) {
            return true;
        }

        for (String modId : aliases) {
            if (isModLoadedWithinVersion(modId, minVersion, maxVersion)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if the return value of a method is true
     * @param methodPath Path to the method: "com.example.Class"
     * @param methodName The name of the method to invoke in the class
     * @return Return value of the method
     */
    public static boolean checkBooleanValue(String methodPath, String methodName) {
        try {
            Class<?> clazz = Class.forName(methodPath);

            Method method = clazz.getMethod(methodName);

            Object result = method.invoke(null);

            if (result instanceof Boolean) {
                return (Boolean) result;
            } else {
                MixinConstraints.LOGGER.warn("Method does not return a boolean.");
                return false;
            }
        } catch (Exception e) {
            MixinConstraints.LOGGER.warn("Failed to find class: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if *ALL* the modIds provided are absent
     */
    public static boolean checkModAbsent(String main, Iterable<String> modIds, String minVersion, String maxVersion) {
        return !checkModLoaded(main, modIds, minVersion, maxVersion);
    }

    public static boolean checkDevEnvironment() {
        return Abstractions.isDevelopmentEnvironment();
    }

    public static boolean checkMinecraftVersion(String minVersion, String maxVersion) {
        return isModLoadedWithinVersion("minecraft", minVersion, maxVersion);
    }

    private static boolean isModLoadedWithinVersion(String modId, String minVersion, String maxVersion) {
        return Abstractions.isModLoadedWithinVersion(modId, minVersion, maxVersion);
    }

}
