rootProject.name = "hyper-framework"

include(":hyper-logo")

val buildFiles = fileTree(rootDir) {
    val excludes = gradle.startParameter.projectProperties["excludeProjects"]?.split(",")
    include("**/*.gradle", "**/*.gradle.kts")
    exclude(
        "build", "**/gradle", "buildSrc", ".*", "out",
        "settings.gradle", "settings.gradle.kts",
        "/build.gradle", "/build.gradle.kts"
    )
    if (!excludes.isNullOrEmpty()) {
        exclude(excludes)
    }
}

buildFiles.forEach { buildFile ->
    val isDefaultName = buildFile.name == "build.gradle" || buildFile.name == "build.gradle.kts"
    val isKotlin = buildFile.name.endsWith(".kts")
    if (isDefaultName) {
        val buildFilePath = buildFile.parentFile.absolutePath
        val projectPath = buildFilePath.replace(rootDir.absolutePath, "")
            .replace(File.separator, ":")
        include(projectPath)
    } else {
        val projectName = if (isKotlin) buildFile.name.replace(".gradle.kts", "")
        else buildFile.name.replace(".gradle", "")

        val buildFilePath = buildFile.parentFile.absolutePath
        val projectPath = buildFilePath.replace(rootDir.absolutePath, "")
            .replace(File.separator, ":")
        include(projectPath)

        val project = findProject(projectPath)
        project?.name = projectName
        project?.projectDir = buildFile.parentFile
        project?.buildFileName = buildFile.name
    }
}
