package com.tsimerekis.map.proxy;

import com.tsimerekis.submission.entity.Submission;
import com.vaadin.flow.spring.annotation.UIScope;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@UIScope
@Aspect
@Component
public class CacheManagementAspect {

    private static final int MAX_CACHE_SIZE = 50;

    private static final Logger log = LogManager.getLogger(CacheManagementAspect.class);

    @AfterReturning(
            pointcut = "execution(* com.tsimerekis.map.proxy.SubmissionProxy.getSubmission())"
    )
    public void trimCacheAfterProcessing(JoinPoint joinPoint) {

        Object target = joinPoint.getTarget();
        if (!(target instanceof SubmissionProxy proxy)) {
            return; // Safety check
        }

        HashMap<Long, Submission> mapToTrim = proxy.getCache();

        trimRandomly(mapToTrim);
    }

    private static <K, V> void trimRandomly(HashMap<K, V> map) {
        int currentSize = map.size();

        if (currentSize <= CacheManagementAspect.MAX_CACHE_SIZE) {
            log.info("[AOP Trim] Map size {} is within limit.", currentSize);
            return;
        }

        int itemsToRemove = currentSize - CacheManagementAspect.MAX_CACHE_SIZE;

        // Get keys, shuffle, and remove the first 'itemsToRemove'
        List<K> keys = new ArrayList<>(map.keySet());
        Collections.shuffle(keys);

        for (int i = 0; i < itemsToRemove; i++) {
            K keyToRemove = keys.get(i);
            map.remove(keyToRemove);
        }

        log.info("[AOP Trim] Successfully trimmed {} items. New size: {}", itemsToRemove, map.size());
    }
}