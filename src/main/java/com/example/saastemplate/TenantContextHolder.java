package com.example.saastemplate;

public class TenantContextHolder {

    private static ThreadLocal<String> tenant = new ThreadLocal<>();

    public static void setTenant(String tenantId) {
        tenant.set(tenantId);

    }

    public static String getTenant() {
        return tenant.get();
    }
}
