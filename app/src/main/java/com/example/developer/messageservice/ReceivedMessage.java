package com.example.developer.messageservice;

import java.util.ArrayList;
import java.util.List;

public class ReceivedMessage {
    private final String sentFromUsername;
    private final String messageText;


    private ReceivedMessage(String sentFromUsername, String messageText) {
        this.messageText = messageText;
        this.sentFromUsername = sentFromUsername;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getSentFromUsername() {
        return sentFromUsername;
    }

    public static List<ReceivedMessage> parseReceivedMessagesFromAPIResponse(RemoteServerAPIResponse apiResponse) {
        ArrayList<ReceivedMessage> receivedMessagesThatHaveBeenParsed = new ArrayList<ReceivedMessage>();
        String stringRepresentationOfApiResponse = apiResponse.responseAsString();
        String[] aConvolutedRepresentationOfSentFromAndThenTheMessage = stringRepresentationOfApiResponse.split("~");
        int numberOfDataPointsInConvolutedRepresentation = 2;
        int numberOfMessagesReturned = aConvolutedRepresentationOfSentFromAndThenTheMessage.length / numberOfDataPointsInConvolutedRepresentation;
        for (int currentMessageIndex = 0; currentMessageIndex< numberOfMessagesReturned; currentMessageIndex++) {
            String sentFrom = aConvolutedRepresentationOfSentFromAndThenTheMessage[currentMessageIndex*numberOfDataPointsInConvolutedRepresentation];
            String messageText = aConvolutedRepresentationOfSentFromAndThenTheMessage[currentMessageIndex*numberOfDataPointsInConvolutedRepresentation + 1];
            receivedMessagesThatHaveBeenParsed.add(new ReceivedMessage(sentFrom, messageText));
        }
        return receivedMessagesThatHaveBeenParsed;
    }
}
