plugins {
  groovy
  jacoco
  `java-library`
  `maven-publish`
  signing
  id("io.github.gradle-nexus.publish-plugin") version "1.3.0"
  id("org.javacc.javacc") version "3.0.0"
}

repositories {
  mavenCentral()
}

dependencies {
  compileOnly("net.jcip:jcip-annotations:1.0")

  testImplementation("nl.jqno.equalsverifier:equalsverifier:3.15.6")

  testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.1")

  testImplementation("org.spockframework:spock-core:2.4-M1-groovy-4.0")

  javacc("net.java.dev.javacc:javacc:7.0.13")
}

group = "com.github.jensborch"
description = "RSQL-parser"

java {
  withSourcesJar()
  withJavadocJar()

  toolchain {
    languageVersion.set(JavaLanguageVersion.of(8))
  }

  sourceSets {
    main {
      java.srcDirs(project.layout.buildDirectory.dir("generated/javacc"))
    }
  }
}

publishing {
  publications.create<MavenPublication>("java") {
    from(components["java"])

    pom {
      name.set("RSQL-parser")
      description.set("Parser of RSQL / FIQL (query language for RESTful APIs) written in JavaCC.")
      url.set("https://github.com/jensborch/rsql-parser")
      inceptionYear.set("2011")

      licenses {
        license {
          name.set("MIT")
          url.set("https://opensource.org/licenses/MIT")
        }
      }

      developers {
        developer {
          id.set("jirutka")
          name.set("Jakub Jirutka")
          organization.set("CTU in Prague")
          organizationUrl.set("https://www.cvut.cz")
        }
        developer {
          id.set("nstdio")
          name.set("Edgar Asatryan")
          email.set("nstdio@gmail.com")
        }
        developer {
          id.set("jensborch")
          name.set("Jens Borch Christiansen")
          email.set("jens.borch@gmail.com")
        }
      }

      scm {
        connection.set("scm:git:git@github.com:jensborch/rsql-parser.git")
        developerConnection.set("scm:git:git@github.com:jensborch/rsql-parser.git")
        url.set("https://github.com/jensborch/rsql-parser")
      }

      ciManagement {
        system.set("GitHub Actions")
        url.set("https://github.com/jensborch/rsql-parser/actions")
      }

      issueManagement {
        system.set("GitHub Issues")
        url.set("https://github.com/jensborch/rsql-parser/issues")
      }
    }
  }
}

signing {
  isRequired = (version as String).endsWith("SNAPSHOT")

  val signingKey = findProperty("SIGNING_KEY") as String?
  val signingPassword = findProperty("SIGNING_PASSWORD") as String?
  useInMemoryPgpKeys(signingKey, signingPassword)

  sign(publishing.publications["java"])
}

nexusPublishing {
  repositories {
    sonatype {
      val baseUri = uri("https://oss.sonatype.org")
      nexusUrl.set(baseUri.resolve("/service/local/"))
      snapshotRepositoryUrl.set(baseUri.resolve("/content/repositories/snapshots/"))
      username = findProperty("SONATYPE_USERNAME") as String?
      password = findProperty("SONATYPE_PASSWORD") as String?
    }
  }
}



tasks {
  test {
    useJUnitPlatform()
  }

  withType<JacocoReport> {
    reports {
      val isCI = System.getenv("CI").toBoolean()
      xml.required.set(isCI)
      html.required.set(!isCI)
    }
  }

  withType<Test> {
    finalizedBy(named("jacocoTestReport"))
  }

  withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
  }

  withType<Javadoc>().configureEach {
    options.encoding = "UTF-8"
  }

  withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
  }

  compileJavacc {
    outputDirectory = outputDirectory.resolve("cz/jirutka/rsql/parser")
  }

  named("sourcesJar") {
    dependsOn(compileJavacc)
  }
}

val newVersion: String? by project
tasks.register("bumpVersion") {
    this.doFirst {
      bumpVersion("gradle.properties", "version=")
      bumpVersion("README.adoc", "version: ")
    }
}

tasks.register("toSnapshot") {
    this.doFirst {
      val file = file("gradle.properties")
      val snapshot = version.toString().contains("-SNAPSHOT")
      if (!snapshot) {
        file.readText().apply {
          println("Adding SNAPSHOT")
          val content = this.replace("version=$version", "version=$version-SNAPSHOT")
          file.writeText(content)
        }
      }
    }
}

fun bumpVersion(fileName: String, versionPattern: String) {
  val file = file(fileName)
  val snapshot = version.toString().contains("-SNAPSHOT")
  val newVersion = takeIf { newVersion.isNullOrBlank() }?.let {
    val versionArray = version.toString().removeSuffix("-SNAPSHOT").split(".")
    "${versionArray[0]}.${versionArray[1]}.${if (snapshot) versionArray.last().toInt() else versionArray.last().toInt().plus(1)}"
    } ?: newVersion

  println("Bumping from version $version to $newVersion in $fileName")
  file.readText().apply {
    val content = this.replace("$versionPattern$version", "$versionPattern$newVersion")
    file.writeText(content)
  }
}

