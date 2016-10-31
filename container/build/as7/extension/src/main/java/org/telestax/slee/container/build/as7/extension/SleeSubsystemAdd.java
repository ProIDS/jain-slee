package org.telestax.slee.container.build.as7.extension;

import org.jboss.as.controller.*;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.controller.registry.ImmutableManagementResourceRegistration;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.jmx.MBeanServerService;
import org.jboss.as.server.AbstractDeploymentChainStep;
import org.jboss.as.server.DeploymentProcessorTarget;
import org.jboss.as.txn.service.TransactionManagerService;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.msc.service.ServiceTarget;
import org.telestax.slee.container.build.as7.deployment.SleeDeploymentInstallProcessor;
import org.telestax.slee.container.build.as7.deployment.SleeDeploymentParseProcessor;
import org.telestax.slee.container.build.as7.service.SleeContainerService;
import org.telestax.slee.container.build.as7.service.SleeServiceNames;

import javax.management.MBeanServer;
import javax.transaction.TransactionManager;
import java.util.List;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.*;

class SleeSubsystemAdd extends AbstractBoottimeAddStepHandler {

    static final SleeSubsystemAdd INSTANCE = new SleeSubsystemAdd();

    private final Logger log = Logger.getLogger(SleeSubsystemAdd.class);
    private final String deploymentsPath = "modules/system/layers/base/org/telestax/slee/container/extension/main/deployments/";

    private SleeSubsystemAdd() {
    }

    /** {@inheritDoc} */
    @Override
    protected void populateModel(OperationContext context, ModelNode operation, Resource resource) throws OperationFailedException {
        log.info("Populating the model: "+resource.getModel());
        resource.getModel().setEmptyObject();

        // Add a steps to install deployments (a mixed approach solution)
        if (requiresRuntime(context)) {
            addDeploymentStep(context, "datasource-ejb-app-deployment.ear");
        }

    }

    private void addDeploymentStep(OperationContext context, String deploymentName) {
        PathAddress deploymentAddress = PathAddress.pathAddress(PathElement.pathElement(DEPLOYMENT, deploymentName));
        ModelNode op = Util.createOperation(ADD, deploymentAddress);
        op.get(ENABLED).set(true);
        op.get(PERSISTENT).set(false); // prevents writing this deployment out to standalone.xml

        ModelNode contentItem = new ModelNode();
        contentItem.get(RELATIVE_TO).set("jboss.home.dir");
        contentItem.get(PATH).set(deploymentsPath + deploymentName);
        contentItem.get(ARCHIVE).set(true);

        op.get(CONTENT).add(contentItem);
        log.info("op: "+op.toJSONString(false));

        ImmutableManagementResourceRegistration rootResourceRegistration = context.getRootResourceRegistration();
        OperationStepHandler handler = rootResourceRegistration.getOperationHandler(deploymentAddress, ADD);

        context.addStep(op, handler, OperationContext.Stage.MODEL);
    }

    /** {@inheritDoc} */
    @Override
    protected void populateModel(ModelNode operation, ModelNode model) throws OperationFailedException {
        // We overrode the code that calls this method
        throw new UnsupportedOperationException();
    }

    protected boolean requiresRuntimeVerification() {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void performBoottime(OperationContext context, ModelNode operation, ModelNode model,
            ServiceVerificationHandler verificationHandler, List<ServiceController<?>> newControllers)
            throws OperationFailedException {

        // Add deployment processors here
        // Remove this if you don't need to hook into the deployers, or you can add as many as you like
        // see SubDeploymentProcessor for explanation of the phases
        context.addStep(new AbstractDeploymentChainStep() {
            public void execute(DeploymentProcessorTarget processorTarget) {
                processorTarget.addDeploymentProcessor(SleeExtension.SUBSYSTEM_NAME,
                        SleeDeploymentParseProcessor.PHASE,
                        SleeDeploymentParseProcessor.PRIORITY,
                        new SleeDeploymentParseProcessor());
                processorTarget.addDeploymentProcessor(SleeExtension.SUBSYSTEM_NAME,
                        SleeDeploymentInstallProcessor.PHASE,
                        SleeDeploymentInstallProcessor.PRIORITY,
                        new SleeDeploymentInstallProcessor());
            }
        }, OperationContext.Stage.RUNTIME);

    	// Installs the msc service which builds the SleeContainer instance and its modules
        final ServiceTarget target = context.getServiceTarget();
        final SleeContainerService sleeContainerService = new SleeContainerService();
        newControllers.add(target.addService(SleeServiceNames.SLEE_CONTAINER, sleeContainerService)
                //.addDependency(PathManagerService.SERVICE_NAME, PathManager.class, service.getPathManagerInjector())
                .addDependency(MBeanServerService.SERVICE_NAME, MBeanServer.class, sleeContainerService.getMbeanServer())
                .addDependency(TransactionManagerService.SERVICE_NAME, TransactionManager.class, sleeContainerService.getTransactionManager())
                .setInitialMode(Mode.ACTIVE)
                .install());
    }
    
}
