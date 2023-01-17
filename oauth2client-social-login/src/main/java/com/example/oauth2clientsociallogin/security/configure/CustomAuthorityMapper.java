package com.example.oauth2clientsociallogin.security.configure;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

public class CustomAuthorityMapper implements GrantedAuthoritiesMapper {

    private static final String PREFIX_ROLE = "ROLE_";

    @Override
    public Set<GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        HashSet<GrantedAuthority> mapped = new HashSet<>(authorities.size());
        for (GrantedAuthority authority : authorities) {
            mapped.add(mapAuthority(authority.getAuthority()));
        }
        return mapped;
    }


    /**
     * Google 인 경우 name 이 다음과 같다
     * <pre>
     *     http://google.com/xxx/xxx/xxx.email
     * </pre>
     *
     * @param name
     * @return
     */
    private GrantedAuthority mapAuthority(String name) {

        int index = name.lastIndexOf(".");

        String authority = name;

        if (index > 8) {
            authority = "SCOPE_" + name.substring(index + 1);
        }

        if (!name.startsWith(PREFIX_ROLE)) {
            authority = PREFIX_ROLE + authority;
        }

        return new SimpleGrantedAuthority(authority);

    }
}
