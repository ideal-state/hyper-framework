group = "team.idealstate.hyper"
version = "1.0.0"

subprojects {
    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenLocal()
        maven {
            name = "aliyun-public"
            url = uri("https://maven.aliyun.com/repository/public")
        }
        mavenCentral()
    }
}
