# selfexec-maven-plugin

A maven plugin that allows jar files to be executed transparently (no java -jar ...). OS X and Linux systems are supported, Windows may be usable with a WSL. This maven plugin appends an sh script to the head of a certain jar in the ${project.build.directory} allowing it to be executed from a CLI without ceremony. The self-exec plugin may also be used to embed flags and user defined run commands.  
</br>

## Usage
*Making a self executing jar from a Hello World project and running*
![](expose.gif?raw=true "simple")

## Installation Instructions

    <pluginRepositories>
      <pluginRepository>
          <id>jitpack.io</id>
          <url>https://jitpack.io</url>
      </pluginRepository>
    </pluginRepositories>
</br>

    <plugin>
      <dependencies>
        <dependency>
          <groupId>com.github.paroxayte</groupId>
          <artifactId>selfexec-maven-plugin</artifactId>
          <version>v1.5.1</version>
        </dependency>
      </dependencies>
      <groupId>com.github.paroxayte</groupId>
      <artifactId>selfexec-maven-plugin</artifactId>
      <version>v1.5.1</version>
    </plugin>

  ### *Optional*
  To making running this plugin from the command line significantly shorter, a configuration can be added to the maven settings.xml file. The is located in your maven home repository (eg: ~/.m2/repository). If a settings.xml does not exist create one and add the following:

    <pluginGroups>
        ....

        <pluginGroup>com.github.paroxayte</pluginGroup>
    </pluginGroups>

  After this added you will be able to invoke the plugin with `mvn selfexec:selfexec`

## Configurations

This plugin has the following public properties:

* **jarName:** Represents the jarFile to be made self executable. Default value - `${project.artifactId}-${project.version}`

* **finalName:** Determines the name of the generated self-executing jar. If it is the same as another file name in the output directory, it will overwrite that file.

* **jarLoc:** The directory which contains the target jarFile. Default value -`${project.build.directory}`

* **flags:** The command to embed in to the jar file. Default value - `java -jar $0 \"$@\"`

* **shebang** The shebang of the embedded script. Default value - `#!/bin/sh`
  
  The single goal of this plugin, **selfexec** simply creates a new file which is a self executing version of the provided jar.

### Example configuration


    <plugin>
      <dependencies>
        <dependency>
          <groupId>com.github.paroxayte</groupId>
          <artifactId>selfexec-maven-plugin</artifactId>
          <version>v1.5.1</version>
        </dependency>
      </dependencies>
      <groupId>com.github.paroxayte</groupId>
      <artifactId>selfexec-maven-plugin</artifactId>
      <version>v1.5.1</version>
      <configuration>
        <finalName>foo</finalName>
      </configuration>
      <executions>
        <execution>
          <phase>install</phase>
          <goals>
            <goal>selfexec</goal>
          </goals>
        </execution>
      </executions>
    </plugin>

If you use a custom name on your jar, it may be desirable to set a property to keep you finalName and jarName linked since they must be the same. EG:

    <properties>
        <buildName>example</buildName>
    </properties>
    ...
    <finalName>${buildName}</finalName>
    ...
    <jarName>${buildName}</jarName>

This plugin may also be run from the CLI in a maven project directory. `mvn selfexec:selfexec`

</br>

