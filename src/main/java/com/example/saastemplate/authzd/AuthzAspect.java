package com.example.saastemplate.authzd;


import com.authzed.api.v1.Core;
import com.authzed.api.v1.PermissionService;
import com.authzed.api.v1.PermissionsServiceGrpc;
import com.example.saastemplate.authzd.annotation.Authz;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Aspect
@Component
public class AuthzAspect {

    private final PermissionsServiceGrpc.PermissionsServiceBlockingStub permissionsService;

    @SneakyThrows
    @Before("@annotation(authz)")
    public void processAuthz(ProceedingJoinPoint pjp, Authz authz) {

        PermissionService.CheckPermissionRequest request = PermissionService.CheckPermissionRequest.newBuilder()
                .setPermission(authz.action())
                .setResource(Core.ObjectReference.newBuilder()
                        .setObjectId(authz.objectId())
                        .setObjectType(authz.objectType())
                        .build())
                .setSubject(Core.SubjectReference.newBuilder()
                        .setObject(Core.ObjectReference.newBuilder()
                                .setObjectType("user")
                                .setObjectId(SecurityContextHolder.getContext().getAuthentication().getName())
                                .build()))
                .build();

        PermissionService.CheckPermissionResponse response = permissionsService.checkPermission(request);

        if (response.getPermissionship().getNumber() <=  1) {
            throw new RuntimeException();

        }

        pjp.proceed();
    }
}
