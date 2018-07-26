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
        <version>v0.11</version>
    </dependency>  
    
</br>

## Configurations 

This plugin has a single public property, `jarName` whose default value is `${project.artifactId}-${project.version}`.
The jarName property represents the jarFile to be made self executable. 
</br>
The single goal of this plugin, **selfexec** simply creates a new file which is a self executing version of the provided jar.
