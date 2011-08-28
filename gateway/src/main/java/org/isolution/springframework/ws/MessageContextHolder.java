package org.isolution.springframework.ws;

import org.springframework.ws.context.MessageContext;

/**
 * Spring Web Services framework has a special MessageContext class for holding request and response messages as well as
 * arbitrary properties during the processing of a WS request. This class is especially useful if you are writing your own endpoint
 * interceptors, because it allows you to do whatever you want with all incoming or outgoing web service messages,
 * regardless of the endpoint in which they are processed.
 * There is certainly no problem getting to MessageContext in an EndpointInterceptor (all interface methods contain
 * a MessageContext parameter), but what if you need to access it in your endpoint handler? For example, if you had to know the content type of
 * all SOAP attachments that came in request, you would do it like this:
 *
 * <pre>
 *         SoapMessage request = (SoapMessage) messageContext.getRequest();
*           for (Iterator it = request.getAttachments(); it.hasNext();) {
*                   Attachment attachment = (Attachment) it.next();
*                   System.out.println(attachment.getContentType());
*           }
 *  </pre>
 *  You could do that in a MessageEndpoint, but not in a PayloadEndpoint where you have no access to MessageContext since
 *  it is not passed as parameter to your endpoint method.
  * Curiously, TransportContext does not have this limitation, as it is bound to current thread and accessible via TransportContextHolder.getTransportContext().
 *  Users of Spring Security will remember that SecurityContext can be accessed in the same manner via SecurityContextHolder.getContext().
 *  Notice a recurring theme here? What we lack in Spring WS is a “MessageContextHolder” class that would allow us to access
 *  MessageContext from anywhere in our code.
 *
 *
 * User: Alex Wibowo
 * Date: 20/04/11
 * Time: 10:34 PM
 */
public class MessageContextHolder {

    private static ThreadLocal<MessageContext> threadLocal =
            new ThreadLocal<MessageContext>() {
                @Override
                protected MessageContext initialValue() {
                    return null;
                }
            };

    private MessageContextHolder() {
    }

    public static MessageContext getMessageContext() {
        return threadLocal.get();
    }

    public static void setMessageContext(MessageContext context) {
        threadLocal.set(context);
    }

    public static void removeMessageContext() {
        threadLocal.remove();
    }
}
