package paroxayte;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.PosixFilePermissions;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jooq.lambda.Unchecked;

@Mojo(name = "selfexec")
public class SelfExec extends AbstractMojo {

  private static String SCRIPT_NAME = "/stub.sh";

  // The name of the jar to be made self executing.
  @Parameter(property = "jarName", defaultValue = "${project.artifactId}-${project.version}", alias = "selfexec.jarName")
  String jarName;

  // If true, delete the original jar
  @Parameter(property = "overwrite", defaultValue = "false", alias = "selfexec.overwrite")
  boolean overwrite;

  @Parameter(defaultValue = "${project.build.directory}", readonly = true)
  String buildDir;

  public void execute() throws MojoExecutionException {

    jarName += ".jar";

    Path jarFile = FileSystems.getDefault().getPath(buildDir + '/' + jarName);
    String noExt = jarName.substring(0, jarName.length() - 4);
    Path execFile = FileSystems.getDefault().getPath(buildDir + '/' + noExt);

    if (Files.exists(jarFile)) {

      try(InputStream baseIn = Files.newInputStream(jarFile, StandardOpenOption.READ); OutputStream baseOut = Files.newOutputStream(execFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);) {
        
        // write script to new file
        BufferedReader scriptRead = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(SCRIPT_NAME)));
        PrintWriter scriptWrite = new PrintWriter(baseOut, true);
        scriptRead.lines().map((line) -> line + "\n").forEach(Unchecked.consumer(scriptWrite::println));

        // append jar to tail of script
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
            Files.setPosixFilePermissions(execFile, PosixFilePermissions.fromString("rwxr-xr-x"));
          } catch(IOException e) {
            throw new RuntimeException("Failed to set executable permissions...", e);
          }
      }

    } else {
      throw new MojoExecutionException(jarName + " not found in build output directory");
    }

    if(overwrite) {
      Unchecked.runnable(() -> Files.delete(jarFile)).run();
    }

  }

}