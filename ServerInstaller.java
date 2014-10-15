package serverinstaller;

import java.util.*;
import java.io.*;
import javax.swing.*;
import org.apache.commons.io.*;

/**
 * @author Nate Dalo, JDS Solutions 2014 Nate@jdssc.com
 */
public class ServerInstaller {
    
    public static String installPath = "";

    public static void main(String[] args) {
        ServerGUI gui = new ServerGUI();
        gui.show();
    }
    
    /** Performs an unattended install of Notepad++ */
    public static void installNotepad() {
        try {
            boolean notepadIsInstalled = false;

            log("Installing Notepad++...");
            String[] extensions = {"exe"};

            List<File> fileList = (List<File>) FileUtils.listFiles(new File(installPath), extensions, true);
            log(installPath);
            for (File file : fileList) {
                if (file.toString().contains("npp")) {
                    String commandToExecute = "cmd /c " + file.toString() + " /S";
                    ShellCommand installNotepad = new ShellCommand(commandToExecute);
                    installNotepad.execute();
                    log("Notepad++ Installed!");
                    notepadIsInstalled = true;
                }
            }

            if (!notepadIsInstalled) {
                log("Notepad++ could not be found!");
            }
        } catch (Exception ex) {
            log(ex.toString());
        }
    }
    
    /** Performs an unattended install of 7Zip */
    public static void install7Zip() {
        try {
            boolean SevenZipIsInstalled = false;

            log("Installing 7Zip...");
            String[] extensions = {"exe", "msi"};

            List<File> fileList = (List<File>) FileUtils.listFiles(new File(installPath), extensions, true);
            log(installPath);
            for (File file : fileList) {
                if (file.toString().contains("7z") && file.toString().contains("exe")) {
                    String commandToExecute = "cmd /c " + file.toString() + " /S";
                    ShellCommand installNotepad = new ShellCommand(commandToExecute);
                    installNotepad.execute();
                    log("7Zip installed!");
                    SevenZipIsInstalled = true;
                }
                if (file.toString().contains("7z") && file.toString().contains("msi")) {
                    String commandToExecute = "cmd /c " + file.toString() + " /q";
                    ShellCommand installNotepad = new ShellCommand(commandToExecute);
                    installNotepad.execute();
                    log("7Zip installed!");
                    SevenZipIsInstalled = true;
                }
            }

            if (!SevenZipIsInstalled) {
                log("7Zip could not be found!");
            }
        } catch (Exception ex) {
            log(ex.toString());
        }
    }
    /** Performs an unattended install of SQL Server 2012 */
    public static void installSQL2012() {
        log("Installing SQL Server 2012...");
        log("Not implemented yet...");
    }
    /** Simple logging function */
    public static void log(String str) {
        System.out.println(str);
        File logDir = new File("c:\\jdslog");
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
        try {
            File f = new File("c:\\jdslog\\jds-serverinstall.log");
            f.createNewFile();

            PrintWriter pw = new PrintWriter(new FileOutputStream(f, true));
            pw.append(new Date().toString() + " -> " + str + "\n");
            pw.close();
        } catch (IOException e) {
            log(e.toString());
        }
    }
    /** executeProcess can execute a process, passing in input parameters 
        as necessary and then waits for the process to complete */
    public static void executeProcess(String command, String inputValues, String errorMessage) {
        try {
            String[] splitString = command.split(" ");
            ProcessBuilder pb = new ProcessBuilder(splitString);
            pb.redirectErrorStream(true);
            Process p = pb.start();

            StreamGobbler err = new StreamGobbler(p.getErrorStream(), "ERROR");
            StreamGobbler output = new StreamGobbler(p.getInputStream(), "OUTPUT");
            err.start();
            output.start();
            if (!inputValues.equals("")) {
                log("Inputing data...");
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
                out.write(inputValues);
                out.flush();
                out.close();
            }
            p.waitFor();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, errorMessage);
            log(errorMessage);
            log(ex.toString());
            System.exit(0);
        }
    }

}
