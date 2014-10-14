package serverinstaller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ShellCommand
{
  private String _stdOut = "";
  private String _stdErr = "";
  private String _cmdLine = "";
  private boolean _bCaptureOutput = true;

  public ShellCommand(String cmdLine)
  {
    this._cmdLine = cmdLine;
  }

  public void setCaptureOutput(boolean flag)
  {
  }

  public int execute()
  {
    this._stdOut = "";
    this._stdErr = "";
    int ret = -1;
    try
    {
      Process proc = Runtime.getRuntime().exec(this._cmdLine);
      try { Thread.sleep(250L); } catch (Exception sleepe) { sleepe.printStackTrace(); }


      StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR", new PrintWriter(new File("subproc-error.log")));

      StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT", new PrintWriter(new File("subproc-output.log")));

      errorGobbler.start();
      outputGobbler.start();

      return proc.waitFor();
    }
    catch (Exception e)
    {
      this._stdErr = e.toString();
    }

    return ret;
  }

  public String getStdOut()
  {
    return this._stdOut;
  }

  public String getStdErr()
  {
    return this._stdErr;
  }

  class StreamGobbler extends Thread {
    InputStream is;
    String type;
    PrintWriter _pw;

    StreamGobbler(InputStream is, String type, PrintWriter pw) {
      this.is = is;
      this.type = type;
      this._pw = pw;
    }

    public void run()
    {
      try
      {
        InputStreamReader isr = new InputStreamReader(this.is);
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        while ((line = br.readLine()) != null)
        {
          System.out.println(this.type + ">" + line);
          this._pw.write(line + "\n");
        }
      }
      catch (IOException ioe)
      {
        System.out.println(ioe.getMessage());
      }
      finally
      {
        try
        {
          this._pw.close();
        }
        catch (Exception e)
        {
          System.out.println(e.getMessage());
        }
      }
    }
  }
}