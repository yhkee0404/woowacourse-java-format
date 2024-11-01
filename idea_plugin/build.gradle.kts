import org.jetbrains.intellij.platform.gradle.TestFrameworkType
/*
 * Copyright 2024 The Woowacourse Java Format Authors. All Rights Reserved.
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// https://github.com/JetBrains/intellij-platform-gradle-plugin/releases
plugins {
  id("org.jetbrains.intellij.platform") version "2.1.0"
}

repositories {
  mavenCentral()

  intellijPlatform {
    defaultRepositories()
  }

  flatDir {
      dirs("../core/target")
  }
}

// https://github.com/yhkee0404/woowacourse-java-format/releases
val googleJavaFormatVersion = "1.24.0.0_WOOWACOURSE"

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

intellijPlatform {
  pluginConfiguration {
    name = "woowacourse-java-format"
    version = "${googleJavaFormatVersion}"
    ideaVersion {
      sinceBuild = "223"
      untilBuild = provider { null }
    }
  }

  publishing {
    val jetbrainsPluginRepoToken: String by project
    token.set(jetbrainsPluginRepoToken)
  }
}

var gjfRequiredJvmArgs =
      listOf(
        "--add-exports", "jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED",
        "--add-exports", "jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED",
        "--add-exports", "jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED",
        "--add-exports", "jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED",
        "--add-exports", "jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED",
        "--add-exports", "jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED",
      )

tasks {
  runIde {
    jvmArgumentProviders += CommandLineArgumentProvider {
      gjfRequiredJvmArgs
    }
  }
}

tasks {
  withType<Test>().configureEach {
    jvmArgs(gjfRequiredJvmArgs)
  }
}

dependencies {
  intellijPlatform {
    intellijIdeaCommunity("2022.3")
    bundledPlugin("com.intellij.java")
    instrumentationTools()
    testFramework(TestFrameworkType.Plugin.Java)
  }
  implementation("com.github.yhkee0404.woowacoursejavaformat:woowacourse-java-format:${googleJavaFormatVersion}")
  // https://mvnrepository.com/artifact/junit/junit
  testImplementation("junit:junit:4.13.2")
  // https://mvnrepository.com/artifact/com.google.truth/truth
  testImplementation("com.google.truth:truth:1.4.4")
}
