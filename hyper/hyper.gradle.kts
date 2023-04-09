plugins {
    id("java")
    id("java-library")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
    api(project(":hyper-context"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = sourceCompatibility
}

val encoding = "UTF-8"
tasks.withType<JavaCompile> {
    options.encoding = encoding
}

tasks.shadowJar {
    archiveClassifier.set("")
    manifest {
        manifest {
            attributes(
                "Add-Opens" to "java.base/java.lang java.base/java.net"
            )
        }
    }
    dependencies {
        include { it ->
            if (it.moduleArtifacts.isNotEmpty()) {
                it.moduleArtifacts.forEach {
                    if (it.id.componentIdentifier.displayName.startsWith("project :")) {
                        return@include true
                    }
                }
            }
            return@include false
        }
    }
}

rootProject.tasks.create("hyperJar") {
    group = "hyper"
    dependsOn(tasks.shadowJar)
}

tasks.create("cleanAll") {
    delete("$rootDir/build")
}
