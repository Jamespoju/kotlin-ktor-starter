plugins {
    id("org.jetbrains.kotlin.plugin.serialization")
}

group = "io.initialcapacity.web"

val ktorVersion: String by project
val kMongoVersion: String by project
val jSerialVersion: String by project

dependencies {
    implementation(project(":components:data-analyzer"))
    implementation(project(":support:logging-support"))
    implementation(project(":support:workflow-support"))

    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-freemarker-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")

    // HTTP client for data collection - CSCA 5020 Module 3 Data Collection
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$jSerialVersion")

    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")

    // Database
    implementation("org.litote.kmongo:kmongo:$kMongoVersion")
    implementation("org.litote.kmongo:kmongo-coroutine:$kMongoVersion")

    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
}


task<JavaExec>("run") {
    classpath = files(tasks.jar)
}

tasks.register<JavaExec>("runCollectorGames") {
    group = "application"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("io.initialcapacity.web.CollectorGamesKt")
}

tasks {
    jar {
        manifest { attributes("Main-Class" to "io.initialcapacity.web.AppKt") }
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        from({
            configurations.runtimeClasspath.get()
                .filter { it.name.endsWith("jar") }
                .map(::zipTree)
        })
    }
}