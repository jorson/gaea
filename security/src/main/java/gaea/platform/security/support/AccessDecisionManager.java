package gaea.platform.security.support;

import gaea.foundation.core.config.SystemConfig;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;

import java.util.List;

public class AccessDecisionManager extends AffirmativeBased {

    public AccessDecisionManager(List<AccessDecisionVoter> decisionVoters) {
        super(decisionVoters);
    }

    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        this.setAllowIfAllAbstainDecisions(SystemConfig.Instance.getBooleanProperty(SecurityConstant.SECURITY_ALLOWALLRESOURCES));
    }
}
