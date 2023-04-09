plugins {
    id("java")
    id("java-library")
}

dependencies {
    api(project(":hyper-resources"))
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
