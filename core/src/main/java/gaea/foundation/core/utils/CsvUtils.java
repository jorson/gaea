package gaea.foundation.core.utils;

import gaea.foundation.core.Constant;
import gaea.foundation.core.utils.csv.CsvReader;
import gaea.foundation.core.utils.csv.CsvWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Csv操作工具类
 *
 * @author wuhy
 */
public final class CsvUtils {

    private static Log logger = LogFactory.getLog(CsvUtils.class);
    /**
     * 默认的栏位分隔符
     */
    public static final char DEFAULT_FIELD_SEPARATOR_CHAR = ',';
    /**
     * 默认的记录分隔符
     */
    public static final char DEFAULT_RECORD_SEPARATOR_CHAR = '\n';

    /**
     * 默认的字符集
     */
    public static final Charset DEFAULT_CHARSET = Constant.DEFAULT_CHARSET;

    /**
     * 私有化构造函数，不允许实例化该类
     */
    private CsvUtils() {
    }

    /**
     * 使用标准CSV格式连接字符串数组
     *
     * @param datas 字符串数组
     * @return CSV字符串
     */
    public static String join(String[] datas) {
        String result = "";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Writer writer = new OutputStreamWriter(baos);
            CsvWriter wr = new CsvWriter(writer, DEFAULT_FIELD_SEPARATOR_CHAR);
            wr.setRecordDelimiter(DEFAULT_RECORD_SEPARATOR_CHAR);
            wr.writeRecord(datas);
            wr.close();
            result = baos.toString();
            result = removeRecordSeparator(result);
        } catch (IOException ex) {
            logger.warn("join csv string happen error," + ex.getMessage());
        }
        return result;
    }

    /**
     * 在路径指定的文件中使用默认编码(UTF-8)写入CSV字符串，有Bom
     * <p/>
     * 如果文件已经存在会被直接覆盖
     *
     * @param path
     * @param datas
     */
    public static void write(String path, List<String[]> datas) throws IOException {
        write(path, datas, DEFAULT_CHARSET);
    }

    /**
     * 在路径指定的文件中使用默认编码(UTF-8)写入CSV字符串
     * <p/>
     * 如果文件已经存在会被直接覆盖
     *
     * @param path
     * @param datas
     * @param hasBom 是否存在Bom
     */
    public static void write(String path, List<String[]> datas, boolean hasBom) throws IOException {
        write(path, datas, DEFAULT_CHARSET, hasBom);
    }

    /**
     * 在路径指定的文件中使用自定义的编码写入CSV字符串，有Bom
     * <p/>
     * 如果文件已经存在会被直接覆盖
     *
     * @param path
     * @param datas
     * @param charset 编码
     */
    public static void write(String path, List<String[]> datas, Charset charset) throws IOException {
        write(path, datas, charset, true);
    }


    /**
     * 在路径指定的文件中使用自定义的编码写入CSV字符串
     * <p/>
     * 如果文件已经存在会被直接覆盖
     *
     * @param path
     * @param datas
     * @param charset 编码
     * @param hasBom  是否存在Bom，只有在UTF-8和UTF-16时有效
     */
    public static void write(String path, List<String[]> datas, Charset charset, boolean hasBom) throws IOException {
        CsvWriter wr = null;
        try {
            wr = new CsvWriter(path, DEFAULT_FIELD_SEPARATOR_CHAR, charset);
            if (hasBom && (DEFAULT_CHARSET.equals(charset) || Charset.forName("UTF-16").equals(charset))) {
                byte[] bom = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
                wr.write(new String(bom));
                try {
                    BeanUtils.forceSetProperty(wr, "firstColumn", true);
                } catch (NoSuchFieldException e) {
                    //
                }
            }
            for (int i = 0; i < datas.size(); i++) {
                String[] data = datas.get(i);
                wr.writeRecord(data);
            }
        } finally {
            if (wr != null) {
                wr.close();
            }
        }

    }

    /**
     * 把单行字符串使用CSV标准格式分隔成字符串数组
     *
     * @param record
     * @return
     */
    public static String[] split(String record) {
        CsvReader csvReader = null;
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(record.getBytes());
            Reader reader = new InputStreamReader(stream);
            csvReader = new CsvReader(reader, DEFAULT_FIELD_SEPARATOR_CHAR);
            while (csvReader.readRecord()) {
                return csvReader.getValues();
            }
        } catch (IOException ex) {
            logger.warn("split single csv string happen error," + ex.getMessage());
        } finally {
            if (csvReader != null) {
                csvReader.close();
            }
        }
        return null;
    }

    /**
     * 把多行字符串使用CSV标准格式分隔成字符串数组
     *
     * @param records
     * @return
     */
    public static List<String[]> splitMultiLines(String records) {
        CsvReader csvReader = null;
        List<String[]> results = new ArrayList<String[]>(); //用来保存数据
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(records.getBytes());
            Reader reader = new InputStreamReader(stream);
            csvReader = new CsvReader(reader, DEFAULT_FIELD_SEPARATOR_CHAR);
            while (csvReader.readRecord()) { //逐行读入数据
                results.add(csvReader.getValues());
            }
        } catch (IOException ex) {
            logger.warn("split multi csv string happen error," + ex.getMessage());
        } finally {
            if (csvReader != null) {
                csvReader.close();
            }
        }
        return results;
    }

    /**
     * 使用默认的编码(UTF-8)从CSV文件中读取数据
     *
     * @param path CSV文件地址
     * @return
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public static List<String[]> read(String path) throws FileNotFoundException, IOException {
        return read(path, DEFAULT_CHARSET);
    }

    /**
     * 使用自定义的编码(UTF-8)从CSV文件中读取数据
     *
     * @param path CSV文件地址
     * @return
     * @throws java.io.IOException
     */
    public static List<String[]> read(String path, Charset charset) throws IOException {
        CsvReader reader = null;
        List<String[]> results = new ArrayList<String[]>(); //用来保存数据
        try {
            reader = new CsvReader(path, DEFAULT_FIELD_SEPARATOR_CHAR, charset);    //使用默认编码来读取
            while (reader.readRecord()) { //逐行读入数据
                results.add(reader.getValues());
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return results;
    }

    /**
     * 去掉末尾的分分隔符
     *
     * @param record
     * @return
     */
    private static String removeRecordSeparator(String record) {
        if (record.charAt(record.length() - 1) == DEFAULT_RECORD_SEPARATOR_CHAR) {
            return record.substring(0, record.length() - 1);
        }
        return record;
    }
}
