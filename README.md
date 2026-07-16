# MixinConstraints

A library to enable/disable mixins using annotations.

Annotations can be applied to mixin classes to toggle the whole mixin, or individual fields/methods for more precision.

# Installing

MixinConstraints is available through Maven Central.

__Gradle__
```groovy
maven { url 'https://jitpack.io' }

dependencies {
    // You can find commit versions here: https://jitpack.io/#Tater-Certified/MixinConstraints
    include(implementation("com.github.Tater-Certified:MixinConstraints:<version>"))
}
```

Next, you will need to bootstrap the library to affect your mixins.

The easiest way to do so is by using the provided Mixin Plugin by adding the following to your modid.mixins.json:
```json5
{
    "plugin": "mixinconstraints.com.github.tatercertified.ConstraintsMixinPlugin",
}
```

If you already have your own mixin plugin, you can use the [ConstraintsMixinPlugin](https://github.com/Tater-Certified/MixinConstraints/blob/master/src/main/java/com/github/tatercertified/mixinconstraints/ConstraintsMixinPlugin.java) class as a reference to add support.

# Using the library

The library provides 5 annotations
- @IfModLoaded (checks if a mod is loaded)
- @IfModAbsent (checks if a mod is absent)
- @IfMinecraftVersion (checks if the Minecraft version matches a range)
- @IfDevEnvironment (checks if the game is running inside a development environment)
- @IfBoolean (checks if the boolean is true)
- @IfBooleans (checks multiple `@IfBoolean` annotations)

These annotations can be applied to classes to control whether the whole mixin is applied

For example, the following mixin will only be applied if sodium or embeddium is present
```java
@IfModLoaded(value = "sodium", aliases = {"embeddium"})
@Mixin(BlockOcclusionCache.class)
public class MixinSodiumBlockOcclusionCache {
   ...
}
```

Additionally, the annotations can also be applied to individual fields/methods

For example, the following mixin will inject only one of the two methods depending on whether the mod is in a dev environment
```java
@Mixin(Minecraft.class)
public class MixinMinecraft {
    @IfDevEnvironment
    @Inject(at = @At("HEAD"), method = "run")
    private void runDev(CallbackInfo info) {
        System.out.println("Hello from a dev environment!");
    }

    @IfDevEnvironment(negate = true)
    @Inject(at = @At("HEAD"), method = "run")
    private void runProd(CallbackInfo info) {
        System.out.println("Hello from production!");
    }
}
```

Some constraints support version ranges, which can be used like so:
```java
@IfModLoaded(value = "modernfix", minVersion = "5.11", maxVersion = "5.15")
```

The `@IfBoolean` constraint requires an external method that returns a boolean. You can use it like this:
```java
@IfBoolean(booleanPath = "com.example.group.mod.ClassName", booleanMethodName = "thisMethodReturnsABoolean")
```

The `@IfBooleans` just takes an array of multiple `@IfBoolean`s and **ANDS** them together like `If_1 && If_2 && ...`
```java
@IfBooleans(value = {
        @IfBoolean(booleanPath = "com.example.group.mod.ClassName", booleanMethodName = "thisMethodReturnsABoolean")
        @IfBoolean(booleanPath = "com.example.group.mod.ClassName", booleanMethodName = "thisMethodReturnsABooleanToo", negate = true) // Negate just makes "false" return "true"
})
```

The version comparison uses Fabric API's [Version](https://github.com/FabricMC/fabric-loader/blob/master/src/main/java/net/fabricmc/loader/api/Version.java). Ensure the version resembles a semver string in order for range comparison to work as expected.

# License
The library is available under the MIT license.
