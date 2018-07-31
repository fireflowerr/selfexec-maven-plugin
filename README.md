# selfexec-maven-plugin

A maven plugin that allows jar files to be executed transparently (no java -jar ...)
This maven plugin appends an sh script to the head of a certain jar in the ${project.build.directory} allowing it to be executed from a CLI.
</br>

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
          <version>v1.3</version>
        </dependency>
      </dependencies>
      <groupId>com.github.paroxayte</groupId>
      <artifactId>selfexec-maven-plugin</artifactId>
      <version>v1.3</version>
    </plugin>

## Configurations

This plugin has two public properties, `jarName`  and `overwrite`.

* `jarName` - alias = `selfexec.jarName`: Represents the jarFile to be made self executable. Its default value is `${project.artifactId}-${project.version}`

* `overwrite` - alias = `selfexec.overwrite`: If true deletes the original jar.

* `keepExt` - alias = `selfexec.keepExt`: If true, leaves the .jar extention on the jar. Use this for crossplatform compatiability. Eg unix like systems can run `./example.jar` and windows/other systems can run java `-jar example.jar` If `keepExt` is true `overwrite` will be forcibly set to true.
  
  The single goal of this plugin, **selfexec** simply creates a new file which is a self executing version of the provided jar.

### Example configuration

For a jar project with `<finalName>example</finalName>`

    <plugin>
      <dependencies>
        <dependency>
          <groupId>com.github.paroxayte</groupId>
          <artifactId>selfexec-maven-plugin</artifactId>
          <version>v1.3</version>
        </dependency>
      </dependencies>
      <groupId>com.github.paroxayte</groupId>
      <artifactId>selfexec-maven-plugin</artifactId>
      <version>v1.3</version>
      <configuration>
        <jarName>${buildName}</jarName>
        <overwrite>true</overwrite>
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

It may be desirable to set a property to keep you finalName and jarName linked since they must be the same. EG:

    <properties>
        <buildName>example</buildName>
    </properties>
    ...
    <finalName>${buildName}</finalName>
    ...
    <jarName>${buildName}</jarName>

This plugin may also be run from the CLI in a maven project directory. `mvn selfexec:selfexec`

</br>

## Screenshots

![example](/exampleSSv2.png?raw=true "simple")
