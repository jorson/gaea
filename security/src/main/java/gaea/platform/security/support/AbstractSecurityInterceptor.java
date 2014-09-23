package gaea.platform.security.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.*;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.event.AuthenticationCredentialsNotFoundEvent;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.security.access.event.AuthorizedEvent;
import org.springframework.security.access.event.PublicInvocationEvent;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import java.util.Collection;

public abstract class AbstractSecurityInterceptor implements InitializingBean,
        ApplicationEventPublisherAware, MessageSourceAware {
    protected final Log logger = LogFactory.getLog(getClass());

    private MessageSource messageSource;

    private AccessDecisionManager accessDecisionManager;

    private ApplicationEventPublisher eventPublisher;

    private boolean rejectPublicInvocations = false;

    public void afterPropertiesSet() throws Exception {

    }

    /**
     * 检查权限
     *
     * @param object
     */
    protected void checkAuthority(Object object) {
        Assert.notNull(object, "Object was null");
        final boolean debug = logger.isDebugEnabled();
        // 如果不是继承自安全类
        if (!getSecureObjectClass().isAssignableFrom(object.getClass())) {
            throw new IllegalArgumentException("Security invocation attempted for object "
                    + object.getClass().getName()
                    + " but AbstractSecurityInterceptor only configured to gaea.platform.security.support secure objects of type: "
                    + getSecureObjectClass());
        }
        Collection<ConfigAttribute> attributes = this.obtainSecurityMetadataSource().getAttributes(object);
        Authentication authenticated = SecurityContextHolder.getContext().getAuthentication();
        if (attributes == null) {
            if (rejectPublicInvocations) {
                throw new IllegalArgumentException("Secure object invocation " + object
                        + " was denied as public invocations are not allowed via this interceptor. "
                        + "This indicates a configuration error because the "
                        + "rejectPublicInvocations property is set to 'true'");
            }
            if (debug) {
                logger.debug("Public object - authentication not attempted");
            }
            publishEvent(new PublicInvocationEvent(object));
            return; // no further work post-invocation
        }
        if (debug) {
            logger.debug("Secure object: " + object + "; Attributes: " + attributes);
        }
        if (authenticated == null) {
            credentialsNotFound("An Authentication object was not found in the SecurityContext", object, attributes);
        }
        try {
            this.accessDecisionManager.decide(authenticated, object, attributes);
        } catch (AccessDeniedException accessDeniedException) {
            publishEvent(new AuthorizationFailureEvent(object, attributes, authenticated, accessDeniedException));
            throw accessDeniedException;
        }
        if (debug) {
            logger.debug("Authorization successful");
        }
        publishEvent(new AuthorizedEvent(object, attributes, authenticated));
    }

    private void credentialsNotFound(String reason, Object secureObject, Collection<ConfigAttribute> configAttribs) {
        AuthenticationCredentialsNotFoundException exception = new AuthenticationCredentialsNotFoundException(reason);
        AuthenticationCredentialsNotFoundEvent event = new AuthenticationCredentialsNotFoundEvent(secureObject, configAttribs, exception);
        publishEvent(event);
        throw exception;
    }

    protected void publishEvent(ApplicationEvent event) {
        if (this.eventPublisher != null) {
            this.eventPublisher.publishEvent(event);
        }
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public ApplicationEventPublisher getEventPublisher() {
        return eventPublisher;
    }

    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public AccessDecisionManager getAccessDecisionManager() {
        return accessDecisionManager;
    }

    public void setAccessDecisionManager(AccessDecisionManager accessDecisionManager) {
        this.accessDecisionManager = accessDecisionManager;
    }

    public boolean isRejectPublicInvocations() {
        return rejectPublicInvocations;
    }

    public void setRejectPublicInvocations(boolean rejectPublicInvocations) {
        this.rejectPublicInvocations = rejectPublicInvocations;
    }

    public abstract SecurityMetadataSource obtainSecurityMetadataSource();

    public abstract Class<? extends Object> getSecureObjectClass();
}
