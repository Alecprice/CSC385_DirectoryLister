import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.awt.*;
import javax.swing.*;

/**
 * DirectoryLister class.
 * This class allows the user to recursively display the contents of a
 * selected directory in the file system.
 */
public class DirectoryLister {

    // -----------------------------------------------------------------------
    // Attributes
    // -----------------------------------------------------------------------

    /**
     * GUI used to display results
     */
    private GUI gui;

    /**
     * base path of directory to be traversed
     */
    private String basePath;


    // -----------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------

    /**
     * Create a new DirectoryLister that uses the specified GUI.
     */
    public DirectoryLister(GUI gui) {
        this.gui = gui;
    }


    // -----------------------------------------------------------------------
    // Methods
    // -----------------------------------------------------------------------

    /**
     * Allow user to select a directory for traversal.
     */
    public void selectDirectory() {
        // clear results of any previous traversal
        gui.resetGUI();

        // allow user to select a directory from the file system
        basePath = gui.getAbsoluteDirectoryPath();

        // update the address label on the GUI
        gui.setAddressLabelText(basePath);

        // traverse the selected directory, and display the contents
        showDirectoryContents(basePath);


    }

    /**
     * Show the directory listing.
     * An error message is displayed if basePath does not represent a valid directory.
     *
     * @param basePath the absolute path of a directory in the file system.
     */
    public void showDirectoryContents(String basePath) {
        this.basePath = basePath;
        System.out.println(basePath);

        try {
            File f = new File(basePath);
            Long totalSize = f.length();

            String Size = getSizeString(totalSize);
            String Date = formattedDateString(f.lastModified());
            enumerateDirectory(f);


            if (f.isDirectory()) {
                String Type = "Folder";
                gui.updateListing(basePath, Size, Type, Date);
                // System.out.println("is Directory");

            } else if (f.isFile() && !(f.isDirectory())) {

                String Type = "File";
                gui.updateListing(basePath, Size, Type, Date);
                // System.out.println("is File, not Directory");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Please try again",
                    "Invalid Directory",
                    JOptionPane.WARNING_MESSAGE);
            // System.out.println("Something went wrong.");
        }
    }
    /**
     * Recursive method to enumerate the contents of a directory.
     *
     * @param f directory to enumerate
     */
    private void enumerateDirectory(File f) {

        f = new File(basePath);
        File[] dirList = f.listFiles();
        if (dirList != null) {
            for (File subDir : dirList) {
                if (subDir.isDirectory()) {

                    String contents = subDir.getAbsolutePath();
                    showDirectoryContents(contents);

                }

            }
            for (File file : dirList) {
                if (file.isFile()) {
                    String path = file.getAbsolutePath();
                    showDirectoryContents(path);
                    enumerateDirectory(f); // recursive call
                }
            }
        }

    }


    /**
     * Convert a size from bytes to kilobytes, rounded down, and return an appropriate descriptive string.
     * Example: 123456 bytes returns 120 KB
     *
     * @param size size, in bytes
     * @return size, in kilobytes (rounded down) + "KB"
     */
    private String getSizeString(long size) {
        long kbSize = size / 1024;

        return "" + kbSize + " KB";
    }


    /**
     * Return a numeric time value as a formatted date string.
     *
     * @param time numeric time value in milliseconds
     * @return formatted string using the format "MM/dd/yyyy hh:mm:ss aaa"
     * Example: 01/15/2010 12:37:52 PM
     */
    private String formattedDateString(long time) {
        // create Date object from numeric time
        Date d = new Date(time);

        // create formatter
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aaa");

        // return formatted date string
        return sdf.format(d);
    }
}