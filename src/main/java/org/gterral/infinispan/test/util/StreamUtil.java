package org.gterral.infinispan.test.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public final class StreamUtil {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Static fields/initializers 
    //~ ----------------------------------------------------------------------------------------------------------------

    private static final int DEFAULT_BUFFER_SIZE = 2048;

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Constructors 
    //~ ----------------------------------------------------------------------------------------------------------------

    private StreamUtil() {
    }

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Methods 
    //~ ----------------------------------------------------------------------------------------------------------------

    /**
     * Copies information from the input stream to the output stream using a default buffer size of 2048 bytes.
     *
     * @throws java.io.IOException
     */
    public static void copy(InputStream input, OutputStream output) throws IOException {
        copy(input, output, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Copies information from the input stream to the output stream using the specified buffer size
     *
     * @throws java.io.IOException
     */
    public static void copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
        byte[] buf = new byte[bufferSize];
        int bytesRead = input.read(buf);
        while (bytesRead != -1) {
            output.write(buf, 0, bytesRead);
            bytesRead = input.read(buf);
        }
        output.flush();
    }

    /**
     * Copies information between specified streams and then closes both of the streams.
     *
     * @throws java.io.IOException
     */
    public static void copyThenClose(InputStream input, OutputStream output) throws IOException {
        copy(input, output);
        input.close();
        output.close();
    }

    /**
     * @return a byte[] containing the information contained in the specified InputStream.
     *
     * @throws java.io.IOException
     */
    public static byte[] getBytes(InputStream input) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        copy(input, result);
        result.close();
        return result.toByteArray();
    }

    /**
     * @return a byte[] containing the information contained in the specified InputStream and then closes the stream.
     *
     * @throws java.io.IOException
     */
    public static byte[] getBytesThenClose(InputStream input) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        copyThenClose(input, result);
        return result.toByteArray();
    }
}
