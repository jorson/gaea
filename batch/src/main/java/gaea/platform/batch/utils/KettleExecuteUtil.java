package gaea.platform.batch.utils;

import gaea.platform.batch.define.PropertiesConstDefine;
import gaea.foundation.core.utils.PropertiesLoaderUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.parameters.NamedParams;
import org.pentaho.di.core.parameters.UnknownParamException;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
/**
 * 执行kettle生成的ktr,kjb文件的工具
 * @author wangchaoxu.
 */
public class KettleExecuteUtil {
    private static Log logger = LogFactory.getLog(KettleExecuteUtil.class);
    //ktr，kjb文件存放的目录
    private static String kettleFileDirPath = null;

    private static Properties kettleProperties = null;
    static {
        try {
            String batchMainDirPath = System.getProperty(PropertiesConstDefine.BATCH_MAIN_DIR_KEY);
            String propertiesFilePath = batchMainDirPath + '/' + "config/kettle.properties";
            kettleProperties = loadProperties(propertiesFilePath);
            kettleFileDirPath = kettleProperties.getProperty("kettlefile.dirpath");
        } catch (IOException e) {
            logger.error("KettleExecuteUtil static", e);
        }
    }

    public static Properties loadProperties(String path) throws IOException {
        Properties props = new Properties();
        Resource resource = new FileSystemResource(path);
        PropertiesLoaderUtils.fillProperties(props, resource);
        return props;
    }

    /**
     * 执行转换文件
     * @param filePath
     * @throws org.pentaho.di.core.exception.KettleException
     * @throws java.io.IOException
     */
    public static void runTrans(String filePath) throws KettleException, IOException {
        runTrans(filePath, null);
    }

    /**
     * 执行job文件
     * @param filePath
     * @throws org.pentaho.di.core.exception.KettleException
     * @throws java.io.IOException
     */
    public static void runJob(String filePath) throws KettleException, IOException {
        runJob(filePath, null);
    }

    /**
     * 执行转换文件(带参数)
     * @param filePath
     * @param paramsMap
     * @throws org.pentaho.di.core.exception.KettleException
     * @throws java.io.IOException
     */
    public static void runTrans(String filePath, Map<String, String> paramsMap) throws KettleException, IOException {
        String realFilePath = kettleFileDirPath + "/" + filePath;
        KettleEnvironment.init();
        TransMeta transMeta = new TransMeta(realFilePath);
        Trans trans = new Trans(transMeta);
        setDefaultVariable(trans);
        setParamsMap(trans, paramsMap);
        trans.prepareExecution(null);
        trans.startThreads();
        trans.waitUntilFinished();

        if (trans.getErrors()!=0) {
            logger.error("runTrans Error");
        }
    }

    /**
     * 执行转换，转换文件从流中读入
     * @param transFileInputStream
     * @param paramsMap
     * @throws org.pentaho.di.core.exception.KettleException
     * @throws java.io.IOException
     */
    public static void runTrans(InputStream transFileInputStream, Map<String, String> paramsMap) throws KettleException, IOException {
        KettleEnvironment.init();
        TransMeta transMeta = new TransMeta(transFileInputStream, null, true, null, null);
        Trans trans = new Trans(transMeta);
        setDefaultVariable(trans);
        setParamsMap(trans, paramsMap);
        trans.prepareExecution(null);
        trans.startThreads();
        trans.waitUntilFinished();
        if (trans.getErrors()!=0) {
            logger.error("runTrans Error");
        }
    }

    /**
     * 执行job文件(带参数)
     * @param filePath
     * @param paramsMap
     * @throws org.pentaho.di.core.exception.KettleException
     * @throws java.io.IOException
     */
    public static void runJob(String filePath, Map<String, String> paramsMap) throws KettleException, IOException {
        String realFilePath = kettleFileDirPath + "/" + filePath;
        KettleEnvironment.init();
        //jobname 是Job脚本的路径及名称
        JobMeta jobMeta = new JobMeta(realFilePath, null);
        Job job = new Job(null, jobMeta);
        setDefaultVariable(job);
        setParamsMap(job, paramsMap);
        job.start();
        job.waitUntilFinished();
        if (job.getErrors() > 0) {
            logger.error("runJob Error");
        }
    }

    /**
     * 执行job，job文件从流中读入
     * @param jobFileInputStream
     * @param paramsMap
     * @throws org.pentaho.di.core.exception.KettleException
     * @throws java.io.IOException
     */
    public static void runJob(InputStream jobFileInputStream, Map<String, String> paramsMap) throws KettleException, IOException {
        KettleEnvironment.init();
        //本来下一行语句就能完成job创建，但是源代码有BUG，没有在读取流中XML后获取job节点,所以只能用更复杂的语句替代
        //JobMeta jobMeta = new JobMeta(jobFileInputStream, null, null);
        JobMeta jobMeta = new JobMeta();
        Document doc = XMLHandler.loadXMLFile(jobFileInputStream, null, false, false);
        jobMeta.clear();
        Node jobnode = XMLHandler.getSubNode(doc, JobMeta.XML_TAG);
        jobMeta.loadXML(jobnode, null, null);

        Job job = new Job(null, jobMeta);
        setDefaultVariable(job);
        setParamsMap(job, paramsMap);
        job.start();
        job.waitUntilFinished();
        if (job.getErrors() > 0) {
            logger.error("runJob Error");
        }
    }

    /**
     * 设置参数
     * @param obj
     * @param map
     * @throws org.pentaho.di.core.parameters.UnknownParamException
     */
    private static void setParamsMap(VariableSpace obj, Map<String, String> map) throws UnknownParamException {
        if (map == null) {
            return;
        }
        for (String key : map.keySet()) {
            obj.setVariable(key, map.get(key));
            logger.debug("kettle params: " + key + " " + map.get(key));
        }
    }

    /**
     * 设置默认变量
     * @param obj
     * @throws java.io.IOException
     */
    private static void setDefaultVariable(VariableSpace obj) throws IOException {
        Enumeration enu = kettleProperties.propertyNames();
        while(enu.hasMoreElements()){
            String key = (String)enu.nextElement();
            obj.setVariable(key, kettleProperties.getProperty(key));
            logger.debug("kettle Variable: " + key + " " + kettleProperties.getProperty(key));
        }
    }

}