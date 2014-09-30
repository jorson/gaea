package gaea.platform.batch;

import gaea.platform.batch.communication.CommunicationController;
import gaea.platform.batch.job.bean.Job;
import gaea.platform.batch.job.dispatcher.JobDispatcher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 进程的主类,该进程用于和.net进程交互获取job，并执行job，每个该进程只能跑一个job
 *
 * @author wangchaoxu.
 */
public class JobMainController {

    private static Log logger = LogFactory.getLog(JobMainController.class);
    private static ApplicationContext applicationContext;

    public static void main(String[] args) {
        startJobHander(args);
    }

    public static void startJobHander(String[] args) {
        loadSpringFramework();
        try {
            CommunicationController ctrl = getCommunicationControllerByCommandArgs(args);
            try {
                ctrl.openConnect();
                Job job = ctrl.getJob();
                JobDispatcher dispatcher = new JobDispatcher(ctrl);
                dispatcher.dispatch(job);
            } finally {
                ctrl.closeConnect();
            }
        } catch (Exception e) {
            logger.error("JobMainController startJobHander", e);
        }
    }

    /**
     * 加载spring
     */
    public static void loadSpringFramework() {
        applicationContext = new ClassPathXmlApplicationContext("spring/*.xml");
    }


//    /**
//     * 加载所有jobrunner的jar文件，完成runner的初始化
//     */
//    public static void loadJobRunnerPackage() {
//        //加载package目录下的所有包
//        ClassLoader loader = ClassLoad.getClassLoad("package", false);
//
//        //设置当前线程的classLoader
//        Thread.currentThread().setContextClassLoader(loader);
//
//        //注册bean
//        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner((DefaultListableBeanFactory)
//                applicationContext.getAutowireCapableBeanFactory());//使用Spring的注解自动扫描
//        //扫描jar中的包路径，使用通配符，另外在导出jar时必须选择add directory entries（即把目录也加入到jar中）
//        //否则spring扫描时将无法找到class
//        scanner.scan("com.huayu.*");
//
//        //手动拿jar包里的bean的实例时因为存在ClassLoader的隔离虽然在开始设置了ContextClassLoader
//        //但是Spring默认getBean的时候并没有每次都去拿最新的ContextClassLoader使用，所以需要手动设置Bean的ClassLoader
//        //因为是手动设置的这里存在线程安全的问题...不知道有没有其他方法.
//        DefaultListableBeanFactory factory = (DefaultListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
//        factory.setBeanClassLoader(loader);
//
//        applicationContext.getBeansOfType(AbstractJobRunner.class);
//
//
//    }

    /**
     * 根据命令行参数获取CommunicationController实例
     * 命令行中-requestpipe参数和-responsepipe参数表示用于进程间通信的管道名称
     *
     * @param args
     * @return
     */
    private static CommunicationController getCommunicationControllerByCommandArgs(String[] args) {
        String readPipeName = null;
        String writePipeName = null;
        for (int i = 0; i < args.length - 1; ++i) {
            if ("-requestpipe".equals(args[i])) {
                writePipeName = args[i + 1];
            }
            if ("-responsepipe".equals(args[i])) {
                readPipeName = args[i + 1];
            }
        }
        return new CommunicationController(readPipeName, writePipeName);
    }
}


//        CommunicationController ctrl = null;
//        try {
//
//            Job job = new Job();
//            job.setIdentity("apicalldatasclear");
//            HashMap<String, String> map = new HashMap<String, String>();
//            map.put("data.servertime", Long.valueOf(1212121212L).toString());
//            map.put("data.keeptime", Long.valueOf(12121212L).toString());
//            job.setDatas(map);
//            JobDispatcher dispatcher = new JobDispatcher(ctrl);
//            dispatcher.dispatch(job);
//
//        } catch (Exception e) {
//            logger.error("JobMainController main", e);
//        }
//
//        CommunicationController ctrl = null;
//        try {
//            Job job = new Job();
//            job.setIdentity("apicallarchive");
//            HashMap<String, String> map = new HashMap<String, String>();
//            map.put("data.servertime", String.valueOf(new Date().getTime()));
//            map.put("data.limit", String.valueOf(60000 * 5));
//            map.put("data.periodType", String.valueOf(4));
//            job.setDatas(map);
//            JobDispatcher dispatcher = new JobDispatcher(ctrl);
//            dispatcher.dispatch(job);
//        } catch (Exception e) {
//            logger.error("JobMainController main", e);
//        }
//
//        CommunicationController ctrl = null;
//        try {
//            Job job = new Job();
//            job.setIdentity("appinfocollect");
//            HashMap<String, String> map = new HashMap<String, String>();
//            map.put("data.serverAddress", "test.cloud.91open.ty.nd");
//            map.put("data.clientId", "155");
//            map.put("data.clientSecret", "b4df94e38d8b4e5c8e19223313f4d334");
//            job.setDatas(map);
//            JobDispatcher dispatcher = new JobDispatcher(ctrl);
//            dispatcher.dispatch(job);
//        } catch (Exception e) {
//            logger.error("JobMainController main", e);
//        }