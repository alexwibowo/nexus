package org.isolution.nexus.invoker;

import org.isolution.nexus.domain.Endpoint;
import org.isolution.nexus.domain.ServiceURI;
import org.isolution.nexus.routing.InactiveServiceException;
import org.isolution.nexus.routing.NoActiveRouteException;
import org.isolution.nexus.routing.ServiceNotFoundException;
import org.isolution.nexus.routing.ServiceRouter;
import org.isolution.nexus.xml.SOAPDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * An orchestrator for SOAP invocation.
 *
 * User: Alex Wibowo
 * Date: 28/08/11
 * Time: 9:23 AM
 */
@Component
public class InvokerController {

    private InvokerResolver invokerResolver;

    private ServiceRouter serviceRouter;

    @Autowired
    public InvokerController(InvokerResolver invokerResolver, ServiceRouter serviceRouter) {
        this.invokerResolver = invokerResolver;
        this.serviceRouter = serviceRouter;
    }

    /**
     * Perform the actual SOAP invocation:
     * <ul>
     *     <li>Figure out what is the service URI for the provided SOAPDocument</li>
     *     <li>Find the target endpoint for that service URI</li>
     *     <li>Find the invoker for the endpoint</li>
     *     <li>Invoke the endpoint, giving the original SOAPDocument</li>
     * </ul>
     *
     * @param soapRequest  the request message
     * @return response message, an instance of {@link SOAPDocument}
     * @throws IOException error that occurred during invocation
     * @throws InactiveServiceException if the service has been tagged with {@link org.isolution.nexus.domain.Status#INACTIVE}
     * @throws ServiceNotFoundException if there is no service found for the {@link ServiceURI} derived from the SOAPDocument
     * @throws NoActiveRouteException if there is no active service found for the derived {@link ServiceURI}
     */
    public SOAPDocument invoke(final SOAPDocument soapRequest)
            throws IOException, InactiveServiceException, ServiceNotFoundException, NoActiveRouteException {
        ServiceURI serviceURI = soapRequest.getServiceURI();

        Endpoint targetEndpoint = serviceRouter.findSingleActiveEndpoint(serviceURI.getServiceURIString());
         Invoker invoker = invokerResolver.resolveForEndpoint(targetEndpoint);

        return invoker.invoke(soapRequest);
    }
}
