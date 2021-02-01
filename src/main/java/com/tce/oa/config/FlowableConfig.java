package com.tce.oa.config;

/**
 * @author wangxinyang
 * @version 1.0
 * @date 2018/11/16 14:58
 **/
//@Configuration
//public class FlowableConfig {
//
//    /**
//     * 默认多数据源的链接
//     */
//    private String url = "jdbc:mysql://192.168.2.223:3306/tce_oa?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull";
//
//    /**
//     * 默认多数据源的数据库账号
//     */
//    private String username = "root";
//
//    /**
//     * 默认多数据源的数据库密码
//     */
//    private String password = "123456";
//
//    private String driverClassName = "com.mysql.jdbc.Driver";
//
//    @Bean
//    @Primary
//    protected ProcessEngineConfiguration configuration() {
//
//        ProcessEngineConfiguration processEngineConfiguration = SpringProcessEngineConfiguration
//                .createStandaloneProcessEngineConfiguration()
//                .setJdbcUrl(url)
//                .setJdbcDriver(driverClassName)
//                .setJdbcUsername(username)
//                .setJdbcPassword(password)
//                //如果表不存在，自动创建表
//                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
//                .setDatabaseSchema("flow")
//                //属性asyncExecutorActivate定义为true，工作流引擎在启动时就建立启动async executor线程池
//                .setAsyncExecutorActivate(false)
//                //流程发布的时候是否生成流程图
//                .setCreateDiagramOnDeploy(true)
//                //生成流程图参数
//                .setProcessDiagramGenerator(new DefaultProcessDiagramGenerator())
//                .setActivityFontName("幼圆")
//                .setAnnotationFontName("幼圆")
//                .setLabelFontName("幼圆");
//        return processEngineConfiguration;
//    }
//
//    @Bean
//    protected ProcessEngine engine() {
//        //创建流程引擎  服务启动是初始化一次即可
//        return ProcessEngines.getDefaultProcessEngine();
//    }
//}
