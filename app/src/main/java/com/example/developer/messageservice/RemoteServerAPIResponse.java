package com.example.developer.messageservice;
//--------------------------------------------------------
//
// DESCRIPTION
// -----------
// This is an object that encapsulates the responses
// returned by the Remote Server API
//
// AUTHOR
// ------
// Lumberjacks Incorperated (2018)
//
//--------------------------------------------------------

public class RemoteServerAPIResponse {

    private final String responseAsString;

    private RemoteServerAPIResponse(String responseAsString) {
        this.responseAsString = responseAsString;
    }

    public String responseAsString() {
        return responseAsString;
    }

    public boolean responseAsBoolean() {
        boolean booleanResponse = (this.responseAsString().equals("true"));
        return booleanResponse;
    }

    public static RemoteServerAPIResponse failedToCompleteAPIRequest() {
        return new RemoteServerAPIResponse("Failed to connect to API");
    }

    public static RemoteServerAPIResponse responseWithMessageStringFromAPI(String messageStringFromAPI) {
        return new RemoteServerAPIResponse(messageStringFromAPI);
    }
}
