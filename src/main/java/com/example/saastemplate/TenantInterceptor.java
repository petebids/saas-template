package com.example.saastemplate;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@RequiredArgsConstructor
public class TenantInterceptor extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        final String requestedTenant = (String) pathVariables.get("tenant");

        final String iss = jwt.getClaimAsString("iss");

        URI myURI;
        try {
            myURI = new URI(iss);
        } catch (URISyntaxException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        String authorizedTenant = jwt.getClaimAsString("tenant");

        if (null == authorizedTenant || !authorizedTenant.equals(requestedTenant)) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }


        TenantContextHolder.setTenant(authorizedTenant);

        filterChain.doFilter(request, response);
    }
}
