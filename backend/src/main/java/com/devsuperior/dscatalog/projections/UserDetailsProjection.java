package com.devsuperior.dscatalog.projections;

public interface UserDetailsProjection {

    String email();

    String getPassword();

    Long getRoleId();

    String getAuthority();

}
