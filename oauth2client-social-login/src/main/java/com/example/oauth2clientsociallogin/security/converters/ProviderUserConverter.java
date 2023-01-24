package com.example.oauth2clientsociallogin.security.converters;

public interface ProviderUserConverter<T, R> {

    R converter(T t);

}
