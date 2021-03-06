package com.ngs.stash.externalhooks.hook;

import com.atlassian.stash.hook.repository.*;
import com.atlassian.stash.repository.*;
import com.atlassian.stash.setting.*;
import com.atlassian.stash.env.SystemProperties;
import com.atlassian.stash.user.*;
import com.ngs.stash.externalhooks.hook.*;
import com.atlassian.stash.server.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collection;


public class ExternalAsyncPostReceiveHook
    implements AsyncPostReceiveRepositoryHook, RepositorySettingsValidator
{
    private static final Logger log = LoggerFactory.getLogger(
        ExternalAsyncPostReceiveHook.class);

    private StashAuthenticationContext authCtx;
    private PermissionService permissions;
    private RepositoryService repoService;
    private ApplicationPropertiesService properties;

    public ExternalAsyncPostReceiveHook(
        StashAuthenticationContext authenticationContext,
        PermissionService permissions,
        RepositoryService repoService,
        ApplicationPropertiesService properties
    ) {
        this.authCtx = authenticationContext;
        this.permissions = permissions;
        this.repoService = repoService;
        this.properties = properties;
    }

    @Override
    public void postReceive(
        RepositoryHookContext context,
        Collection<RefChange> refChanges
    ) {
        ExternalPreReceiveHook impl = new ExternalPreReceiveHook(
            this.authCtx, this.permissions, this.repoService, this.properties);
        impl.onReceive(context, refChanges, null);
    }

    @Override
    public void validate(
        Settings settings,
        SettingsValidationErrors errors,
        Repository repository
    ) {
        ExternalPreReceiveHook impl = new ExternalPreReceiveHook(this.authCtx,
            this.permissions, this.repoService, this.properties);
        impl.validate(settings, errors, repository);
    }
}
