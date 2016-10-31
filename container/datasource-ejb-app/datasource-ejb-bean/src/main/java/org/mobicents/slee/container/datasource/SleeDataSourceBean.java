package org.mobicents.slee.container.datasource;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.sql.DataSource;

@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@EJB(name = "SleeDataSourceBean", beanInterface = SleeDataSourceInterface.class)
public class SleeDataSourceBean implements SleeDataSourceInterface {
    private SleeDataSourceListener listener;

    @Resource(name="java:jboss/datasources/ExampleDS")
    private DataSource liveDataSource;

    @PostConstruct
    private void startup() {
    }

    @PreDestroy
    private void shutdown() {
        if (listener != null) {
            listener.shutdown();
            listener.waitForShutdownCompletion();
        }
    }

    @Override
    public void setListener(SleeDataSourceListener listener) {
        this.listener = listener;
    }
}