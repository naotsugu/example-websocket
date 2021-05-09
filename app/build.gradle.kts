plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.glassfish.tyrus:tyrus-server:2.0.0")
    implementation("org.glassfish.tyrus:tyrus-container-grizzly-server:2.0.0")
}

application {
    mainClass.set("com.mammb.code.example.websocket.App")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(14))
    }
}

tasks.withType<JavaCompile> {
    options.encoding = Charsets.UTF_8.name()
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
