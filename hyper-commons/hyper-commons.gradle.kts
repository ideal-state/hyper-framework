plugins {
    id("java")
    id("java-library")
}

dependencies {
    // asm
    api("org.ow2.asm:asm:9.2")

    // slf4j-api
    api("org.slf4j:slf4j-api:2.0.6")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = sourceCompatibility
}

val encoding = "UTF-8"
tasks.withType<JavaCompile> {
    options.encoding = encoding
}

tasks.create<Jar>("sourcesJar") {
    dependsOn(tasks.classes)
    charset(encoding)
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}
