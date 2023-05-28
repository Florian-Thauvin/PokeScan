package org.pokescan.processing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Utils for test
 */
public class TestUtils {
    /**
     * The development directory
     */
    public static  final String DEV_DIRECTORY = new File("src/test/java/org/pokescan/processing/dev").getAbsolutePath();

    /**
     * The result directory
     */
    public static  final String RES_DIRECTORY = new File("src/test/resources").getAbsolutePath();

    /**
     * The result dir for development
     */
    public static final String RESULT_DIR = "/res/";

    /**
     * The JPG extension
     */
    public static final String JPG_EXTENSION =".jpg";
    /**
     * The PNG extension
     */
    public static final String PNG_EXTENSION  =".png";

    /**
     * Method used to clean a directory
     * @param dir the directory to clean
     */
    public static void cleanDirectory(File dir){
        try {
            // Check if the file exist
            if( dir.exists()){
                System.out.println("Clean " + dir.getAbsolutePath());

                // Delete each file from the dir
                for(File file : Objects.requireNonNull(dir.listFiles())){
                    if(!file.delete()){
                        System.out.println("    - Can't delete file "+ file.getAbsolutePath());
                    } else {
                        System.out.println("    - Delete file "+ file.getAbsolutePath());
                    }
                }
            } else {
                // Create a new dir
                Files.createDirectory(dir.toPath());
            }
        } catch (IOException e){
            System.out.println("Can't create rest directory to "+ dir.getAbsolutePath()+": "+e);
        }
    }

    /**
     * Method used to get an image name without the file extension
     * @param file the file corresponding the image
     * @return the image name
     */
    public static String getImageNameWithoutExtension(File file){
        return file.getName().replace(JPG_EXTENSION,"").replace(PNG_EXTENSION, "");
    }

    /**
     * Method used to get an image file
     * @param fileName the filename
     * @param extension the file extension
     * @param path the file path
     * @return the file
     */
    public static File getFile(String fileName, String extension, String... path){
       Path fullPath=  Paths.get("",path).toAbsolutePath();
        return new File(fullPath.toString(), String.format("%s%s", fileName, extension));
    }
}
