package paroxayte;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jooq.lambda.Unchecked;

@Mojo(name = "selfexec")
public class SelfExec extends AbstractMojo {

  private static String SCRIPT_NAME = "/stub.sh";

  private DataOutputStream writeOut = null;

  @Parameter(property = "jarName", defaultValue = "${project.artifactId}-${project.version}")
  String jarName;

  @Parameter(defaultValue = "${project.build.directory}", readonly = true)
  String buildDir;

  @Parameter(property = "selfexec.overWrite", defaultValue = "false")
  boolean overWrite;

  public void execute() throws MojoExecutionException {

    Path jarFile = FileSystems.getDefault().getPath(buildDir + '/' + jarName);
    String noExt = jarName.substring(0, jarName.length() - 4);
    Path execFile = FileSystems.getDefault().getPath(buildDir + '/' + noExt);

    if (Files.exists(jarFile)) {
      try (DataInputStream readIn = new DataInputStream(
          new BufferedInputStream(Files.newInputStream(jarFile, StandardOpenOption.READ)))) {

        byte[] bufStore = new byte[4096];
        OutputStream baseOut = Files.newOutputStream(execFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE,
            StandardOpenOption.TRUNCATE_EXISTING);

        writeOut = new DataOutputStream(new BufferedOutputStream(baseOut, bufStore.length));

        BufferedReader scriptRead = new BufferedReader(
            new InputStreamReader(getClass().getResourceAsStream(SCRIPT_NAME)));
        PrintWriter scriptWrite = new PrintWriter(baseOut, true);
        scriptRead.lines().map((line) -> line + "\n").forEach(Unchecked.consumer(scriptWrite::println));

        while (true) {
          writeOut.writeByte(readIn.readByte());
          writeOut.flush();
        }

      } catch (IOException e) {
        e.printStackTrace();
      }

    } else {
      throw new MojoExecutionException(jarName + " not found in build output directory");
    }

  }

}