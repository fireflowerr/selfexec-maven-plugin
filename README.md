# selfexec-maven-plugin
A maven plugin that allows jar files to be executed transparently (no java -jar ...)
This maven plugin appends an sh script to the head of a certain jar in the ${project.build.directory} allowing it to be executed from a CLI.
</br>
## Installation Instructions

    <repositories>
      <repository>
          <id>jitpack.io</id>
          <url>https://jitpack.io</url>
      </repository>
    </repositories>
    
</br>
    
    <dependency>
        <groupId>com.github.paroxayte</groupId>
        <artifactId>selfexec-maven-plugin</artifactId>
        <version>v0.12</version>
    </dependency>  
    
</br>

## Configurations 

This plugin has a single public property, `jarName` whose default value is `${project.artifactId}-${project.version}`.
The jarName property represents the jarFile to be made self executable. 
The single goal of this plugin, **selfexec** simply creates a new file which is a self executing version of the provided jar.

### Example configuration

For a jar project with `<finalName>example</finalName>`

    <plugin>
      <groupId>paroxayte</groupId>
      <artifactId>selfexec-maven-plugin</artifactId>
      <configuration>
        <jarName>example</jarName>
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
    <jarName>${buildName</jarName}

</br>

## Screenshots

![example](/exampleSSv2.png?raw=true "simple")
