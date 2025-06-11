import java.util.regex.Pattern

plugins {
    id("java-library")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.16"
    id("com.github.hierynomus.license") version "0.16.1"
}

group = "com.guy7cc"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
}

dependencies {
    implementation(paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

val targetJavaVersion = 21
java {
    sourceCompatibility = JavaVersion.toVersion(targetJavaVersion)
    targetCompatibility = JavaVersion.toVersion(targetJavaVersion)
    if (JavaVersion.current() < JavaVersion.toVersion(targetJavaVersion)) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
}

license {
    header = file("$rootDir/config/license/header.txt")
    exclude("**/*")
    include("**/*.java")
    mapping("java", "SLASHSTAR_STYLE")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible()) {
        options.release.set(targetJavaVersion)
    }
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

tasks.jar {
    archiveBaseName.set("VoxelOdysseyCore")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register("collectTranslatableKeys") {
    group = "assets"
    description = "Collect translatable keys used in Component.translatable and output to a file"

    doLast {
        val srcDirs = listOf(
            "src/main/java",
            "vo-plugin/src/main/java",
            "vodev-plugin/src/main/java"
        )
        val pattern = Pattern.compile("""Component\.translatable\(\s*"([^"]+)"""")
        val keys = mutableSetOf<String>()

        srcDirs.map { file(it) }
            .filter { it.exists() }
            .flatMap { it.walkTopDown().filter { f -> f.isFile && f.extension == "java" } }
            .forEach { file ->
                file.forEachLine { line ->
                    val matcher = pattern.matcher(line)
                    while (matcher.find()) {
                        keys.add(matcher.group(1) + "=")
                    }
                }
            }

        val output = file("build/translatable-keys.txt")
        output.parentFile.mkdirs()
        output.writeText(keys.sorted().joinToString("\n"))
        println("Output collected translations keys to ${output.path}")
    }
}