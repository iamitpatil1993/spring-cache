package com.example.spring.cahce.configuration;

import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.support.MBeanServerFactoryBean;
import org.springframework.jmx.support.RegistrationPolicy;

import java.util.HashMap;
import java.util.Map;

/**
 * This class defines configurations related to metrics, monitoring jmx etc.
 *
 * @author amit
 */
@Configuration
public class MonitoringAndMetricsConfiguration {

    @Autowired
    private Statistics stats;

    /**
     * As we are executing in J2SE, create Mbean server to register mbeans.
     * By default it creates new mbean server.
     * This internally uses java jmx APIs to define mbean server.
     *
     * @return MBeanServerFactoryBean
     */
    @Bean
    public MBeanServerFactoryBean mBeanServerFactoryBean() {
        MBeanServerFactoryBean mBeanServerFactoryBean = new MBeanServerFactoryBean();

        // in order to use existing mbean server,
        // check any mbean server already running (useful when web container itself providers mbean server)
        mBeanServerFactoryBean.setLocateExistingServerIfPossible(true);

        return mBeanServerFactoryBean;
    }

    /**
     * JMX exporter that allows for exposing any Spring-managed bean to a JMX MBeanServer, without the need to define
     * any JMX-specific information in the bean classes.
     *
     * @param mBeanServerFactoryBean mbeanserver to which mbeans to be registered.
     * @return MBeanExporter
     * @throws Exception
     */
    @Bean
    public MBeanExporter mBeanExporter(final MBeanServerFactoryBean mBeanServerFactoryBean) throws Exception {
        MBeanExporter mBeanExporter = new MBeanExporter();

        // set mbean server to which this exporter will register spring beans as a mbeans
        mBeanExporter.setServer(mBeanExporter.getServer());

        // this enables us to override existing bean registered to mbean server with same object name.
        mBeanExporter.setRegistrationPolicy(RegistrationPolicy.REPLACE_EXISTING);

        // create map of beans to be registered to mbean server
        final Map<String, Object> mBeansToRegister = new HashMap<>(1);

        // register hibernate stats bean as a mbean
        mBeansToRegister.put("com.example.spring.cahce:type=Statistics", stats);
        mBeanExporter.setBeans(mBeansToRegister);

        return mBeanExporter;
    }
}
