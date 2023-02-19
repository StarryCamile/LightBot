package qwq.wumie.utils;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class FileUtil {
    public static void writeFile(String file, List<String> newcontent) {
        try {
            FileWriter fw = new FileWriter(file);
            for (String s : newcontent) {
                fw.append(String.valueOf(s) + "\r\n");
            }
            fw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createFile(File file) {
        try {
            if (!file.exists()) {
                PrintWriter printWriter = new PrintWriter(new FileWriter(file));
                printWriter.println();
                printWriter.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteFile(File file) {
        System.out.println(file.delete());
        return file.delete();
    }

    public static boolean deleteFile(String path) {
        File file = new File(path);
        System.out.println(file.delete());
        return file.delete();
    }

    public static boolean deleteDir(File file) {
        try {
            File[] files = file.listFiles();
            if (files.length > 0) {
                File[] arrfile = files;
                int n = arrfile.length;
                int n2 = 0;
                while (n2 < n) {
                    File f = arrfile[n2];
                    File[] filesmore = f.listFiles();
                    if (filesmore.length > 0) {
                        File[] arrfile2 = filesmore;
                        int n3 = arrfile2.length;
                        int n4 = 0;
                        while (n4 < n3) {
                            File fi = arrfile2[n4];
                            File[] filesmore1 = fi.listFiles();
                            if (filesmore1.length > 0) {
                                File[] arrfile3 = filesmore1;
                                int n5 = arrfile3.length;
                                int n6 = 0;
                                while (n6 < n5) {
                                    File f1 = arrfile3[n6];
                                    f1.delete();
                                    ++n6;
                                }
                            }
                            fi.delete();
                            ++n4;
                        }
                    }
                    f.delete();
                    ++n2;
                }
            }
            return file.delete();
        }
        catch (Exception e) {
            return true;
        }
    }

    public static String getDateString() {
        return "#Last Updated: " + new SimpleDateFormat("yyyy.MM.dd").format(new Date()) + " at " + new SimpleDateFormat("HH:mm:ss").format(new Date());
    }
}