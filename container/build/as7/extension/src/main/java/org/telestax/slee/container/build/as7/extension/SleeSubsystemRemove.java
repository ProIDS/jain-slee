package org.telestax.slee.container.build.as7.extension;

import org.jboss.as.controller.*;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.controller.registry.ImmutableManagementResourceRegistration;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DEPLOYMENT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REMOVE;

/**
 * Handler responsible for removing the subsystem resource from the model.
 *
 * @author Eduardo Martins
 *
 */
class SleeSubsystemRemove extends AbstractRemoveStepHandler {

    static final SleeSubsystemRemove INSTANCE = new SleeSubsystemRemove();

    private final Logger log = Logger.getLogger(SleeSubsystemRemove.class);

    private SleeSubsystemRemove() {
    }

    @Override
    protected void performRemove(OperationContext context, ModelNode operation, ModelNode model) throws OperationFailedException {
        // Add a steps to remove deployments (a mixed approach solution)
        if (requiresRuntime(context)) {
            removeDeploymentStep(context, "datasource-ejb-app-deployment.ear");
        }
        super.performRemove(context, operation, model);
    }

    private void removeDeploymentStep(OperationContext context, String deploymentName) {
        PathAddress deploymentAddress = PathAddress.pathAddress(PathElement.pathElement(DEPLOYMENT, deploymentName));
        ModelNode op = Util.createOperation(REMOVE, deploymentAddress);
        log.info("op: "+op.toJSONString(false));

        ImmutableManagementResourceRegistration rootResourceRegistration = context.getRootResourceRegistration();
        OperationStepHandler handler = rootResourceRegistration.getOperationHandler(deploymentAddress, REMOVE);
        context.addStep(op, handler, OperationContext.Stage.MODEL);
    }

    @Override
    protected void performRuntime(OperationContext context, ModelNode operation, ModelNode model) throws OperationFailedException {
        //Remove any services installed by the corresponding add handler here
        //context.removeService(ServiceName.of("some", "name"));
    }


}
