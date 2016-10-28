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
    private Boolean locking = true;

    @Resource(name="java:jboss/datasources/ExampleDS")
    private DataSource liveDataSource;

    @PostConstruct
    private void startup() {
        System.out.println("HELLO from SleeDataSource Singleton!");
    }

    @PreDestroy
    private void shutdown() {
        System.out.println("GOODBYE from SleeDataSource Singleton!");

        System.out.println("listener: "+listener);
        if (listener != null) {
            listener.shutdown();
            listener.waitForShutdownCompletion();
        }

        System.out.println("GOODBYE COMPLETED from SleeDataSource Singleton!");
    }

    @Override
    public void setListener(SleeDataSourceListener listener) {
        System.out.println("listener: " + listener);
        this.listener = listener;
    }
}