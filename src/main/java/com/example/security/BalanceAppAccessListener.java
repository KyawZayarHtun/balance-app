package com.example.security;

import com.example.model.domain.entity.Balance;
import com.example.model.domain.entity.UserAccessLog;
import com.example.model.domain.entity.UserAccessLog.Type;
import com.example.model.repo.UserAccessLogRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static com.example.model.domain.entity.UserAccessLog.Type.Signin;
import static com.example.model.domain.entity.UserAccessLog.Type.Signout;

@Component
public class BalanceAppAccessListener {

    @Autowired
    private UserAccessLogRepo logRepo;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @EventListener
    @Transactional
    public void onSuccess(AuthenticationSuccessEvent event) {
        var time = LocalDateTime
                .ofInstant(new Date(event.getTimestamp()).toInstant(), ZoneId.systemDefault());
        var username = event.getAuthentication().getName();
        log.info("{} is sign in at {}.", username, time);
        logRepo.save(new UserAccessLog(username, Signin, time));
    }

    @EventListener
    @Transactional
    public void onFailure(AbstractAuthenticationFailureEvent event) {
        var time = LocalDateTime
                .ofInstant(new Date(event.getTimestamp()).toInstant(), ZoneId.systemDefault());
        var username = event.getAuthentication().getName();
        log.info("{} is failed to sign in at {} because of {}.", username, time,
                event.getException().getMessage());
        logRepo.save(new UserAccessLog(username, Type.Error, time,
                event.getException().getMessage()));
    }

    @EventListener
    @Transactional
    public void onSessionDestroy(HttpSessionDestroyedEvent event) {
        event.getSecurityContexts().stream().findAny()
                .ifPresent(auth -> {
                    var time = LocalDateTime
                            .ofInstant(new Date(event.getTimestamp()).toInstant(), ZoneId.systemDefault());
                    var username = auth.getAuthentication().getName();
                    log.info("{} is sign out at {}.", username, time);
                    logRepo.save(new UserAccessLog(username, Signout, time));
                });
    }

}
