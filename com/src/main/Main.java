package com.src.main ;
import java.io.IOException;
import java.text.ParseException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.InputMismatchException;
import java.util.Properties;
import java.util.Scanner;

import com.microsoft.graph.models.Message;
import com.microsoft.graph.models.OnlineMeeting;
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.MessageCollectionPage;
import com.microsoft.graph.requests.UserCollectionPage;
import com.microsoft.graph.serializer.OffsetDateTimeSerializer;

public class Main {

    public  static void main(String[] a){
        System.out.println("Java Graph Tutorial");
        System.out.println();

        final Properties oAuthProperties = new Properties();
        try {
            oAuthProperties.load(Main.class.getResourceAsStream("resources/graphApiConfig/oAuth.properties"));
        } catch (IOException e) {
            System.out.println("Unable to read OAuth configuration. Make sure you have a properly formatted oAuth.properties file. See README for details.");
            return;
        }

        initializeGraph(oAuthProperties);
        createMeating() ;

    }




    private static void initializeGraph(Properties properties) {
        try {
            Graph.initializeGraphForUserAuth(properties,
                    challenge -> System.out.println(challenge.getMessage()));
        } catch (Exception e)
        {
            System.out.println("Error initializing Graph for user auth");
            System.out.println(e.getMessage());
        }
    }
    private static void greetUser() {
        // TODO
    }

    private static void createMeating(){
        try {
            OnlineMeeting meeting = Graph.createOnlineMeeting() ;
            System.out.println(meeting.joinWebUrl);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
