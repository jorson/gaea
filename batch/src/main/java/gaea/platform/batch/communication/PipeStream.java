package gaea.platform.batch.communication;

import java.io.*;

/**
 * 命名管道的输入输出流类
 *
 * @author wangchaoxu
 */
public class PipeStream implements Closeable {
    private static final String DEFAULT_STRING_ENCODE = "UTF-16LE";

    private String readPipeName;
    private String writePipeName;
    FileOutputStream writeStream;
    FileInputStream readStream;

    public PipeStream() {
    }

    public PipeStream(String readPipeName, String writePipeName) {
        this.readPipeName = readPipeName;
        this.writePipeName = writePipeName;
    }

    /**
     * open a PipeStream
     * @throws Exception
     */
    public void open() throws FileNotFoundException {
        readStream = new  FileInputStream(getPipePath(readPipeName));
        writeStream = new FileOutputStream(getPipePath(writePipeName));
    }

    /**
     * close a PipeStream
     * @throws java.io.IOException
     */
    public void close() throws IOException {
        if (writeStream != null) {
            writeStream.close();
        }
        if (readStream != null) {
            readStream.close();
        }
    }

    /**
     * write string to named pipe
     * @param req
     * @throws java.io.IOException
     */
    public void writeString(String req) throws IOException {
        byte[] bytes = req.getBytes(DEFAULT_STRING_ENCODE);
        Integer len = bytes.length;
        writeStream.write(getBytes(len), 0, 4);
        writeStream.write(bytes);
    }

    /**
     * read string from named pipe
     * @return
     * @throws java.io.IOException
     */
    public String readString() throws IOException {
        byte[] lenBytes = new byte[4];
        readStream.read(lenBytes, 0, 4);
        int lenReadBytes = getInt(lenBytes);
        byte[] contentBytes = new byte[lenReadBytes];
        readStream.read(contentBytes);
        if (lenReadBytes == 0) {
            return null;
        } else {
            return new String(contentBytes, DEFAULT_STRING_ENCODE);
        }
    }

    /**
     * 根据命名管道名称获取管道真实路径
     *
     * @return
     */
    private String getPipePath(String pipeName) {
        String pipePath = null;
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("linux")) {
            pipePath = "/var/tmp/" + pipeName;
        } else if (osName.contains("windows")) {
            pipePath = "\\\\.\\pipe\\" + pipeName;
        }
        return pipePath;
    }

    /**
     * convert Int to byte array
     * @param data
     * @return
     */
    private byte[] getBytes(int data) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        bytes[2] = (byte) ((data & 0xff0000) >> 16);
        bytes[3] = (byte) ((data & 0xff000000) >> 24);
        return bytes;
    }

    /**
     * convert byte array to int
     * @param bytes
     * @return
     */
    private int getInt(byte[] bytes) {
        return (0xff & bytes[0])
                | (0xff00 & (bytes[1] << 8))
                | (0xff0000 & (bytes[2] << 16))
                | (0xff000000 & (bytes[3] << 24));
    }

    public String getWritePipeName() {
        return writePipeName;
    }

    public void setWritePipeName(String writePipeName) {
        this.writePipeName = writePipeName;
    }

    public String getReadPipeName() {
        return readPipeName;
    }

    public void setReadPipeName(String readPipeName) {
        this.readPipeName = readPipeName;
    }
}
