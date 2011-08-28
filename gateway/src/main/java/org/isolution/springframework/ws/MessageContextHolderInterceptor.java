package org.isolution.springframework.ws;

import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;

/**
 * We can configure this interceptor with whatever endpoint.
 * e.g.:
 * <pre>
 *         <bean class="org.springframework.ws.server.endpoint.mapping.PayloadRootAnnotationMethodEndpointMapping">
*               <property name="interceptors">
*                   <list>
*                           <bean class="my.project.MessageContextHolderInterceptor"/>
*                   </list>
*               </property>
*           </bean
 *  </pre>
 * User: Alex Wibowo
 * Date: 20/04/11
 * Time: 10:37 PM
 */
public class MessageContextHolderInterceptor implements EndpointInterceptor {

    public boolean handleRequest(MessageContext messageContext, Object endpoint)
            throws Exception {
        MessageContextHolder.setMessageContext(messageContext);
        return true;
    }

    public boolean handleResponse(MessageContext messageContext, Object endpoint)
            throws Exception {
        MessageContextHolder.removeMessageContext();
        return true;
    }

    public boolean handleFault(MessageContext messageContext, Object endpoint)
            throws Exception {
        MessageContextHolder.removeMessageContext();
        return true;
    }
}
