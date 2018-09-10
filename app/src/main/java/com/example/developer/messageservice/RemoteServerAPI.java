package com.example.developer.messageservice;
//--------------------------------------------------------
//
// DESCRIPTION
// -----------
// This is an API for the MyApplication remote server
//
// AUTHOR
// ------
// Lumberjacks Incorperated (2018)
//
//--------------------------------------------------------

import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RemoteServerAPI {

    private static final String BASE_API_URL = "http://165.227.25.45/";

    private final Context androidApplicationContext;

    //----------------------------------------------------------------------------------
    // INTERNAL FUNCTIONS
    //----------------------------------------------------------------------------------
    private RemoteServerAPI(Context context) {
        this.androidApplicationContext = context;
    }

    private static String createHTTPParameterStringSectionForParameterValueMapping(Map.Entry<String, String> parameterToValueMapping) {
        String httpParameterString = "";
        String parameter = parameterToValueMapping.getKey();
        String value = parameterToValueMapping.getValue();
        try {
            httpParameterString +=  URLEncoder.encode(parameter, "UTF-8");
            httpParameterString += "=";
            httpParameterString += URLEncoder.encode(value, "UTF-8");
        } catch (Exception thrownException) {

        }
        return httpParameterString;
    }

    private static String generateHTTPParameterQueryStringFromMappingOfParametersToValues(Map<String, String> mappingOfParametersToValues) {
        String HTTPParameterQueryString = new String();
        boolean currentlyOnTheFirstParameterInTheList = true;
        for (Map.Entry<String, String> parameterToValueMapping : mappingOfParametersToValues.entrySet()) {
            if (!currentlyOnTheFirstParameterInTheList) HTTPParameterQueryString += "&";
            HTTPParameterQueryString += createHTTPParameterStringSectionForParameterValueMapping(parameterToValueMapping);
            currentlyOnTheFirstParameterInTheList = false;
        }
        return HTTPParameterQueryString;
    }

    private static void setURLConenctionParametersForPOSTRequest(HttpURLConnection connectionToURLForMakingPOSTRequest) throws ProtocolException {
        connectionToURLForMakingPOSTRequest.setReadTimeout(10000);
        connectionToURLForMakingPOSTRequest.setConnectTimeout(15000);
        connectionToURLForMakingPOSTRequest.setRequestMethod("GET");
        connectionToURLForMakingPOSTRequest.setDoInput(true);
        connectionToURLForMakingPOSTRequest.setDoOutput(true);
    }

    private static URL createURLToMakePOSTRequestToWithParameterQueryStringFromMappingAndStringURL(String stringUrlToMakePOSTRequestTo, Map<String, String> mappingOfParametersToValues) throws IOException {
        String parameterQueryString = generateHTTPParameterQueryStringFromMappingOfParametersToValues(mappingOfParametersToValues);
        URL urlToMakePOSTRequestTo = new URL(stringUrlToMakePOSTRequestTo + "?" + parameterQueryString);
        return urlToMakePOSTRequestTo;
    }

    private static String sendPOSTRequestWithUrlConenctionAndReturnResponseString( HttpURLConnection connectionToURLForMakingPOSTRequest) throws IOException {
        connectionToURLForMakingPOSTRequest.connect();
        connectionToURLForMakingPOSTRequest.getResponseCode();
        String responseString = RemoteServerAPI.getStringFromInputStream(connectionToURLForMakingPOSTRequest.getInputStream());
        return responseString;
    }

    private static String getStringFromInputStream(InputStream inputStreamToExtractStringFrom) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStreamToExtractStringFrom));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }
        reader.close();
        return out.toString();
    }

    private static RemoteServerAPIResponse makeHTTPPOSTRequestFromURLWithParameterToValueMappingAndReturnAPIResponse(String stringUrlToMakePOSTRequestTo, Map<String, String> mappingOfParametersToValues) {
        RemoteServerAPIResponse apiResponse = null;
        try {
            URL urlToMakePOSTRequestTo = createURLToMakePOSTRequestToWithParameterQueryStringFromMappingAndStringURL(stringUrlToMakePOSTRequestTo, mappingOfParametersToValues);
            HttpURLConnection connectionToURLForMakingPOSTRequest = (HttpURLConnection) urlToMakePOSTRequestTo.openConnection();
            setURLConenctionParametersForPOSTRequest(connectionToURLForMakingPOSTRequest);
            String responseString = sendPOSTRequestWithUrlConenctionAndReturnResponseString(connectionToURLForMakingPOSTRequest);
            apiResponse = RemoteServerAPIResponse.responseWithMessageStringFromAPI(responseString);
        } catch (Exception thrownException) {
            thrownException.printStackTrace();
            apiResponse = RemoteServerAPIResponse.failedToCompleteAPIRequest(); // If anything goes wrong in the whole process, we call it a fail here
        }
        return apiResponse;
    }

    private static void makeAsyncronousHTTPPOSTRequestFromURLWithParameterToValueMappingAndAPIDelegate(final String stringUrlToMakePOSTRequestTo, final Map<String, String> mappingOfParametersToValues, final RemoteServerAPIDelegate apiDelegate) {
        Thread threadToSendMesasgeToServer = new Thread(new Runnable() {
            public void run() {
                RemoteServerAPIResponse apiResponse = RemoteServerAPI.makeHTTPPOSTRequestFromURLWithParameterToValueMappingAndReturnAPIResponse(stringUrlToMakePOSTRequestTo, mappingOfParametersToValues);
                apiDelegate.receiveResponseFromAPI(apiResponse);
            }
        });
        threadToSendMesasgeToServer.start();
    }

    static private final String KEY_FOR_SAVED_SESSION_KEY = "saved_session_key";
    private String currentSavedSessionKey() {
        UserDataStorage userDataStorage = UserDataStorage.userDataStorageWithContext(androidApplicationContext);
        String currentSavedSessionKey = userDataStorage.getUserDataStringWithKey(KEY_FOR_SAVED_SESSION_KEY);
        //SharedPreferences prefs = androidApplicationContext.getSharedPreferences("com.example.developer.messageservice", Context.MODE_PRIVATE);
        //String currentSavedSessionKey = prefs.getString("SESSION", null);
        return currentSavedSessionKey;
    }
    private void extractSessionKeyFromResponseAndSaveAsCurrentSessionKey(RemoteServerAPIResponse apiResponse) {
        UserDataStorage userDataStorage = UserDataStorage.userDataStorageWithContext(androidApplicationContext);
        String sessionKeyToSave = apiResponse.responseAsString();
        userDataStorage.setUserDataStringForKey(sessionKeyToSave, KEY_FOR_SAVED_SESSION_KEY);
        //SharedPreferences prefs = androidApplicationContext.getSharedPreferences("com.example.developer.messageservice", Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = prefs.edit();
        //editor.putString("SESSION", sessionKeyToSave);
    }

    //----------------------------------------------------------------------------------
    // EXPORTED FUNCTIONS
    //----------------------------------------------------------------------------------
    private static final String KEY_FOR_SESSION_KEY_PARAMETER = "session_key";

    private static final String REQUEST_URL_FOR_SENDING_MESSAGE_TO_SERVER = BASE_API_URL+"send_todo.php";
    private static final String KEY_FOR_MESSAGE_PARAMETER = "todoText";
    public void sendMessageToRemoteServerWithDestinationUsernameAndMessageToSendAndAPIDelegate(String destinationUsername, String messageToSend, RemoteServerAPIDelegate apiDelegate) {
        HashMap parametersInRequest = new HashMap<String, String>();
        parametersInRequest.put(KEY_FOR_MESSAGE_PARAMETER, messageToSend);
        parametersInRequest.put(KEY_FOR_SESSION_KEY_PARAMETER, this.currentSavedSessionKey());
        makeAsyncronousHTTPPOSTRequestFromURLWithParameterToValueMappingAndAPIDelegate(REQUEST_URL_FOR_SENDING_MESSAGE_TO_SERVER, parametersInRequest, apiDelegate);
    }

    private static final String REQUEST_URL_FOR_LOGIN_TO_SERVER = BASE_API_URL+"login.php";
    private static final String KEY_FOR_USERNAME_PARAMETER = "username";
    private static final String KEY_FOR_PASSWORD_PARAMETER = "password";
    public void loginToRemoteServerWithUsernamePasswordAndAPIDelegate(String username, String password, final RemoteServerAPIDelegate apiDelegate) {
        HashMap parametersInRequest = new HashMap<String, String>();
        parametersInRequest.put(KEY_FOR_USERNAME_PARAMETER, username);
        parametersInRequest.put(KEY_FOR_PASSWORD_PARAMETER, password);
        makeAsyncronousHTTPPOSTRequestFromURLWithParameterToValueMappingAndAPIDelegate(REQUEST_URL_FOR_LOGIN_TO_SERVER, parametersInRequest, new RemoteServerAPIDelegate() {
            @Override
            public void receiveResponseFromAPI(RemoteServerAPIResponse apiResponse) {
                RemoteServerAPI.this.extractSessionKeyFromResponseAndSaveAsCurrentSessionKey(apiResponse);
                apiDelegate.receiveResponseFromAPI(apiResponse);
            }
        });
    }

    private static final String REQUEST_URL_FOR_CHECKING_IF_LOGGED_IN_TO_SERVER = BASE_API_URL+"check_login.php";
    public void checkIfUserIsLoggedInToRemoteServerWithAPIDelegate(RemoteServerAPIDelegate apiDelegate) {
        HashMap parametersInRequest = new HashMap<String, String>();
        parametersInRequest.put(KEY_FOR_SESSION_KEY_PARAMETER, this.currentSavedSessionKey());
        makeAsyncronousHTTPPOSTRequestFromURLWithParameterToValueMappingAndAPIDelegate(REQUEST_URL_FOR_CHECKING_IF_LOGGED_IN_TO_SERVER, parametersInRequest, apiDelegate);
    }


    private static final String REQUEST_URL_FOR_CHECKING_IF_USER_EXISTS = BASE_API_URL+"check_user_exists.php";
    public void checkIfUserExistsWithUsernameAndAPIDelegate(String usernameToCheck, RemoteServerAPIDelegate apiDelegate) {
        HashMap parametersInRequest = new HashMap<String, String>();
        parametersInRequest.put(KEY_FOR_USERNAME_PARAMETER, usernameToCheck);
        parametersInRequest.put(KEY_FOR_SESSION_KEY_PARAMETER, this.currentSavedSessionKey());
        makeAsyncronousHTTPPOSTRequestFromURLWithParameterToValueMappingAndAPIDelegate(REQUEST_URL_FOR_CHECKING_IF_USER_EXISTS, parametersInRequest, apiDelegate);
    }


    private static final String REQUEST_URL_FOR_RECEIVING_MY_MESSAGES = BASE_API_URL+"get_todo.php";
    public void receiveMyMessagesWithAPIDelegate(RemoteServerAPIDelegate apiDelegate) {
        HashMap parametersInRequest = new HashMap<String, String>();
        parametersInRequest.put(KEY_FOR_SESSION_KEY_PARAMETER, this.currentSavedSessionKey());
        makeAsyncronousHTTPPOSTRequestFromURLWithParameterToValueMappingAndAPIDelegate(REQUEST_URL_FOR_RECEIVING_MY_MESSAGES, parametersInRequest, apiDelegate);
    }

    public static RemoteServerAPI remoteServerAPIWithContext(Context context) {
        return new RemoteServerAPI(context);
    }

    //----------------------------------------------------------------------------------
    // EXPORTED INTERFACES
    //----------------------------------------------------------------------------------
    public interface RemoteServerAPIDelegate {
        public void receiveResponseFromAPI(RemoteServerAPIResponse apiResponse);
    }
}
