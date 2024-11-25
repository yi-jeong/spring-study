package com.jeong.oauth2authserver.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

import com.jeong.oauth2authserver.entity.Client;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

import static org.springframework.util.StringUtils.collectionToCommaDelimitedString;
import static org.springframework.util.StringUtils.commaDelimitedListToSet;

@Component
public class JpaRegisteredClientRepository implements RegisteredClientRepository {
    private final ClientRepository clientRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JpaRegisteredClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;

        ClassLoader classLoader = JpaRegisteredClientRepository.class.getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
        this.objectMapper.registerModules(securityModules);
        this.objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        Client entity = this.toEntity(registeredClient);
        clientRepository.save(entity);
    }

    @Override
    public RegisteredClient findById(String id) {
        Client client = clientRepository.findById(id).orElseThrow();
        return this.toObject(client);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Client client = clientRepository.findByClientId(clientId).orElseThrow();
        return this.toObject(client);
    }

    private RegisteredClient toObject(Client client) {
        Set<String> clientAuthenticationMethods = commaDelimitedListToSet(client.getClientAuthenticationMethods());
        Set<String> authorizationGrantTypes = commaDelimitedListToSet(client.getAuthorizationGrantTypes());
        Set<String> redirectUris = commaDelimitedListToSet(client.getRedirectUris());
        Set<String> clientScopes = commaDelimitedListToSet(client.getScopes());
        Set<String> postLogoutUris = commaDelimitedListToSet(client.getPostLogoutRedirectUris());

        RegisteredClient.Builder registeredClient = RegisteredClient.withId(client.getId())
                .clientId(client.getClientId())
                .clientIdIssuedAt(client.getClientIdIssuedAt())
                .clientSecret(client.getClientSecret())
                .clientSecretExpiresAt(client.getClientSecretExpiresAt())
                .clientName(client.getClientName())
                .clientAuthenticationMethods(authenticationMethods -> clientAuthenticationMethods.forEach(authenticationMethod -> authenticationMethods.add(resolveClientAuthenticationMethod(authenticationMethod))))
                .authorizationGrantTypes((grantTypes) -> authorizationGrantTypes.forEach(grantType -> grantTypes.add(resolveAuthorizationGrantType(grantType))))
                .redirectUris((uris) -> uris.addAll(redirectUris))
                .postLogoutRedirectUris(uris -> uris.addAll(postLogoutUris))
                .scopes((scopes) -> scopes.addAll(clientScopes))
                .clientSettings(client.getClientSettings())
                .tokenSettings(client.getTokenSettings());

        return registeredClient.build();
    }

    public Client toEntity(RegisteredClient registeredClient) {
        List<String> clientAuthenticationMethods = new ArrayList<>(registeredClient.getClientAuthenticationMethods().size());
        registeredClient.getClientAuthenticationMethods().forEach(clientAuthenticationMethod -> clientAuthenticationMethods.add(clientAuthenticationMethod.getValue()));

        List<String> authorizationGrantTypes = new ArrayList<>(registeredClient.getAuthorizationGrantTypes().size());
        registeredClient.getAuthorizationGrantTypes().forEach(authorizationGrantType -> authorizationGrantTypes.add(authorizationGrantType.getValue()));

        Client entity = new Client();
        entity.setId(registeredClient.getId());
        entity.setClientId(registeredClient.getClientId());
        entity.setClientIdIssuedAt(registeredClient.getClientIdIssuedAt());
        entity.setClientSecret(registeredClient.getClientSecret());
        entity.setClientSecretExpiresAt(registeredClient.getClientSecretExpiresAt());
        entity.setClientName(registeredClient.getClientName());
        entity.setClientAuthenticationMethods(collectionToCommaDelimitedString(clientAuthenticationMethods));
        entity.setAuthorizationGrantTypes(collectionToCommaDelimitedString(authorizationGrantTypes));
        entity.setRedirectUris(collectionToCommaDelimitedString(registeredClient.getRedirectUris()));
        entity.setPostLogoutRedirectUris(collectionToCommaDelimitedString(registeredClient.getPostLogoutRedirectUris()));
        entity.setScopes(collectionToCommaDelimitedString(registeredClient.getScopes()));
        entity.setClientSettings(registeredClient.getClientSettings());
        entity.setTokenSettings(registeredClient.getTokenSettings());

        return entity;
    }

    private static AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
        if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.AUTHORIZATION_CODE;
        } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.CLIENT_CREDENTIALS;
        } else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.REFRESH_TOKEN;
        }
        return new AuthorizationGrantType(authorizationGrantType);
    }

    private static ClientAuthenticationMethod resolveClientAuthenticationMethod(String clientAuthenticationMethod) {
        if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
        } else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_POST;
        } else if (ClientAuthenticationMethod.NONE.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.NONE;
        }
        return new ClientAuthenticationMethod(clientAuthenticationMethod);
    }
}
