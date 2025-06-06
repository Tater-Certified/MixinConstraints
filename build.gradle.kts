plugins {
    java
    idea
    id("com.vanniktech.maven.publish") version("0.28.0") // `maven-publish` doesn't support new maven central
}

version = "1.0.8"
group = "com.moulberry.mixinconstraints"

idea.module.isDownloadSources = true

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
    maven("https://repo.spongepowered.org/maven") // provides lexforge
    maven("https://maven.neoforged.net/releases")
}

val fabric: SourceSet by sourceSets.creating {
    compileClasspath += sourceSets.main.get().output
}

val forge: SourceSet by sourceSets.creating {
    compileClasspath += sourceSets.main.get().output
}

val neoforge: SourceSet by sourceSets.creating {
    compileClasspath += sourceSets.main.get().output
}

dependencies {
    compileOnly("org.spongepowered:mixin:0.8.5")
    compileOnly("org.ow2.asm:asm-tree:9.7")
    implementation("org.slf4j:slf4j-api:2.0.12")
    implementation("org.jetbrains:annotations:24.1.0")

    "fabricImplementation"("net.fabricmc:fabric-loader:0.15.0")

    "forgeImplementation"("net.minecraftforge:fmlloader:1.20.1-47.3.27")
    "forgeImplementation"("net.minecraftforge:fmlcore:1.20.1-47.3.27")

    "neoforgeImplementation"("net.neoforged.fancymodloader:loader:3.0.13")
}

tasks.jar {
    from(fabric.output)
    from(forge.output)
    from(neoforge.output)
}

tasks.register<Jar>("sourcesJar") {
    group = "build"
    archiveClassifier.set("sources")
    sourceSets.map { it.allSource }.forEach {
        from(it)
    }
}
