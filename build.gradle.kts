plugins {
    id("application")
    id("org.openjfx.javafxplugin") version "0.0.13"
}

repositories {
    mavenCentral()
}

dependencies {
    // JavaFX
    implementation("org.openjfx:javafx-controls:17.0.6")
    implementation("org.openjfx:javafx-fxml:17.0.6")

    // VLCJ for media playback
    implementation("uk.co.caprica:vlcj:4.8.2")
    implementation("uk.co.caprica:vlcj-javafx:1.2.0")

    // Javalin for HTTP/WebSocket implementation
    implementation("io.javalin:javalin:5.6.3")
    implementation("org.slf4j:slf4j-simple:2.0.7")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")

    // JUnit 5 for testing
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

tasks.test {
    useJUnitPlatform()
}

javafx {
    version = "17.0.6"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainClass.set("com.syncplay.Main")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

sourceSets {
    main {
        java {
            srcDirs("src/main/java")
        }
    }
}
