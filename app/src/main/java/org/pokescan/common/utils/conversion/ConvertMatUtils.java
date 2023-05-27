/*
 * Copyright (c) PokeScan2023.
 */
package org.pokescan.common.utils.conversion;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.*;

/**
 * Class used to convert data to {@link Mat}
 */
public class ConvertMatUtils {

    /**
     * Constructor
     */
    private ConvertMatUtils(){
        // Hidden
    }

    /**
     * Method used to convert a list of byte to a {@link Mat}
     * @param buffer input data
     * @return converted {@link Mat}
     */
    public static Mat matFromBytes(byte[] buffer) {
        return Imgcodecs.imdecode(new MatOfByte(buffer), Imgcodecs.IMREAD_UNCHANGED);
    }

    /**
     *       Method used to convert a {@link Mat} to a list of byte
     * @param mat input {@link Mat} 
     * @return converted buffer input data
     */
    public static byte[] bytesFromMat(Mat mat) {
        int length = (int) (mat.total() * mat.elemSize());
        byte[] buffer = new byte[length];
        mat.get(0, 0, buffer);
        return buffer;
    }


    public static File copyInputStreamToFile(InputStream in) {
        File file = new File("");
        OutputStream out = null;

        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            // Ensure that the InputStreams are closed even if there's an exception.
            try {
                if ( out != null ) {
                    out.close();
                }

                // If you want to close the "in" InputStream yourself then remove this
                // from here but ensure that you close it yourself eventually.
                in.close();
            }
            catch ( IOException e ) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
