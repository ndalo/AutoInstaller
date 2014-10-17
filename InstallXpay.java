package serverinstaller;

import java.io.File;
import java.util.List;
import org.apache.commons.io.FileUtils;
import static serverinstaller.ServerInstaller.installPath;
import static serverinstaller.ServerInstaller.log;

/**
 *
 * @author Nate Dalo, Nate@jdssc.com
 */
public class InstallXpay {

    public static String installer;
    public static String keystorePath;
    public static String truststorePath;

    public static void installXpay(String hostname, String userID,
            String userPassword, String dbhost, String dbname, String dbuser,
            String dbpassword, String keystorePassword, String certAlias,
            String truststorePassword) {

        try {
            boolean xpayIsInstalled = false;

            log("Installing Xpay...");
            String[] extensions = {"exe"};

            List<File> fileList = (List<File>) FileUtils.listFiles(new File(installPath), extensions, true);
            log(installPath);
            for (File file : fileList) {
                if (file.toString().matches("xpay.+")) {
                    installer = file.toString();
                    log("Found Xpay Installer: " + installer);
                    xpayIsInstalled = true;
                    break;
                }
            }
            if (!xpayIsInstalled) {
                log("Xpay Installer could not be found!");
            }
        } catch (Exception ex) {
            log(ex.toString());
        }

        try {
            boolean keystoreFound = false;

            log("Searching for Xpay Keystore...");
            String[] extensions = {"keystore"};

            List<File> fileList = (List<File>) FileUtils.listFiles(new File(installPath), extensions, true);
            log(installPath);
            for (File file : fileList) {
                if (file.toString().contains("xpay")) {
                    keystorePath = file.toString();
                    log("Found Xpay Keystore: " + keystorePath);
                    keystoreFound = true;
                    break;
                }
            }
            if (!keystoreFound) {
                log("Xpay keystore could not be found!");
            }
        } catch (Exception ex) {
            log(ex.toString());
        }

        try {
            boolean truststoreFound = false;

            log("Searching for Xpay truststore...");
            String[] extensions = {"truststore"};

            List<File> fileList = (List<File>) FileUtils.listFiles(new File(installPath), extensions, true);
            log(installPath);
            for (File file : fileList) {
                if (file.toString().contains("xpay")) {
                    truststorePassword = file.toString();
                    log("Found Xpay truststore: " + truststorePassword);
                    truststoreFound = true;
                    break;
                }
            }
            if (!truststoreFound) {
                log("Xpay .truststore could not be found!");
            }
        } catch (Exception ex) {
            log(ex.toString());
        }

        String unattendedXpayInstall = installer + " --unattendedmodeui minimal --debuglevel 4 --mode unattended "
                + " --debugtrace trace.txt --installer-language en --xpayInstallMode new_install --hostname " + hostname
                + " --userID " + userID + " --userPassword " + userPassword + " --db yes --dbPlatform sqlserver "
                + " --dbhost " + dbhost + " --dbname " + dbname + " --dbuser " + dbuser + " --dbpassword " + dbpassword
                + " --keystoreFile xpay.keystore --keystorePassword " + keystorePassword + " --certAlias " + certAlias
                + " --truststoreFile xpay.truststore --truststorePassword " + truststorePassword;
        log(unattendedXpayInstall);
        ServerInstaller.executeProcess(unattendedXpayInstall, "", "Error Installing Xpay!");

        hostname = userID = userPassword = dbhost = dbname = dbuser = dbpassword = keystorePassword = certAlias = truststorePassword = null;
    }

    public static void main(String[] args) {
        installPath = "c:\\xpayinstalltest";
        InstallXpay.installXpay("SUPPORTND764", "admin", "Vc2014", "SUPPORTND764", "Xpay", "pos", "pos",
                "123456", "xpay-20140917", "123456");
    }
}
