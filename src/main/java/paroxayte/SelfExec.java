package paroxayte;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.PosixFilePermissions;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;


@Mojo(name = "selfexec")
public class SelfExec extends AbstractMojo {

  // The name of the jar to be made self executing.
  @Parameter(property = "jarName", defaultValue = "${project.artifactId}-${project.version}")
  private String jarName;

  // Determines the name of the outputed selfexec jar file. Default value is jarName without .jar extension
  @Parameter(property = "finalName")
  private String finalName;

  //The directory to look for the jar file in.
  @Parameter(property = "jarLoc", defaultValue = "${project.build.directory}")
  private String buildDir;

  //The command to run the jar with.
  @Parameter(property = "flags", defaultValue = "java -jar $0 \"$@\"")
  private String flags;

  //The shebang to use (change to use a different bash shell)
  @Parameter(property = "shebang", defaultValue = "#!/bin/sh")
  private String shebang;

  public void execute() throws MojoExecutionException {
    if(!jarName.matches(".+\\.jar$")) {
      jarName += ".jar";
    }

    if(finalName == null) {
      finalName = jarName.substring(0, jarName.length() - 4);
    }

    Path finalNamePath = FileSystems.getDefault().getPath(buildDir, finalName);
    Path jarFile = FileSystems.getDefault().getPath(buildDir, jarName);

    if (Files.exists(jarFile)) {

      try(InputStream baseIn = Files.newInputStream(jarFile, StandardOpenOption.READ); OutputStream baseOut = Files.newOutputStream(finalNamePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);) {
        
        // Begin constructing self executing jar
        PrintWriter scriptWrite = new PrintWriter(new OutputStreamWriter(baseOut, StandardCharsets.UTF_8), true);
        scriptWrite.println(shebang);
        scriptWrite.println("\n\n");
        scriptWrite.println("exec " + flags);
        scriptWrite.println("\n\n\n\n");

        // Build a execute script and append jar to it
        DataInputStream readIn = new DataInputStream(new BufferedInputStream(baseIn));
        DataOutputStream writeOut = new DataOutputStream(new BufferedOutputStream(baseOut));
        byte[] bufStore = new byte[readIn.available()];
        int bytesRead;
        while((bytesRead = readIn.read(bufStore)) > 0) {
          writeOut.write(bufStore, 0, bytesRead);
        }
        writeOut.flush();

      } catch(IOException e) {
        e.printStackTrace();
      } finally {
          try {
            Files.setPosixFilePermissions(finalNamePath, PosixFilePermissions.fromString("rwxr-xr-x"));
          } catch(IOException e) {
            throw new RuntimeException("Failed to set executable permissions...", e);
          }
      }

    } else {
      throw new MojoExecutionException(jarName + " not found in build output directory");
    }
   
  }

}