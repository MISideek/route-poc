package com.src.main;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.identity.DeviceCodeCredential;
import com.azure.identity.DeviceCodeCredentialBuilder;
import com.azure.identity.DeviceCodeInfo;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.*;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.MessageCollectionPage;
import com.microsoft.graph.requests.UserCollectionPage;

import com.microsoft.graph.serializer.OffsetDateTimeSerializer;
import okhttp3.Request;
public class Graph {
    private static Properties _properties;
    private static DeviceCodeCredential _deviceCodeCredential;
    private static GraphServiceClient<Request> _userClient;

    public static void initializeGraphForUserAuth(Properties properties, Consumer<DeviceCodeInfo> challenge) throws Exception {
        // Ensure properties isn't null
        if (properties == null) {
            throw new Exception("Properties cannot be null");
        }

        _properties = properties;

        final String clientId = properties.getProperty("app.clientId");
        final String authTenantId = properties.getProperty("app.authTenant");
        final String secret = properties.getProperty("app.clientSecret");
        final List<String> graphUserScopes = Arrays
                .asList(properties.getProperty("app.graphUserScopes").split(","));


        final ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
                .clientId(clientId)
                .clientSecret(secret)
                .tenantId(authTenantId)
                .build();


        final TokenCredentialAuthProvider authProvider =
                new TokenCredentialAuthProvider(clientSecretCredential);

        _userClient = GraphServiceClient.builder()
                .authenticationProvider(authProvider)
                .buildClient();
    }




    public static OnlineMeeting createOnlineMeeting() throws Exception {
        if (_userClient == null) {
            throw new Exception("Graph has not been initialized for user auth");
        }

        OnlineMeeting onlineMeeting = new OnlineMeeting();
        onlineMeeting.startDateTime = OffsetDateTimeSerializer.deserialize("2022-07-12T21:30:34.2444915+00:00");
        onlineMeeting.endDateTime = OffsetDateTimeSerializer.deserialize("2022-07-12T22:00:34.2464912+00:00");
        onlineMeeting.subject = "Route Test Meeting Meeting";
        LobbyBypassSettings settings = new LobbyBypassSettings() ;
        settings.isDialInBypassEnabled = true ;
        settings.scope = LobbyBypassScope.EVERYONE ;
        onlineMeeting.lobbyBypassSettings  = settings;

        MeetingParticipants meetingParticipants = new MeetingParticipants();
        return  _userClient.users("a819e5c9-364f-4478-a214-c682505d3b7a").onlineMeetings()
                .buildRequest()
                .post(onlineMeeting);
    }

    public static User getUser() throws Exception {
        // Ensure client isn't null
        if (_userClient == null) {
            throw new Exception("Graph has not been initialized for user auth");
        }

        return _userClient.me()
                .buildRequest()
                .select("displayName,mail,userPrincipalName")
                .get();
    }



}
