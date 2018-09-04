package com.example.developer.messageservice;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SendMessageActivity extends LoginRequiredActivity {

    private EditText textBoxForInputtingUserMessage;
    private EditText textBoxForInputtingMessageRecipient;

    private final int SEND_MESSAGE_UI_LAYOUT = R.layout.activity_send_message;
    @Override
    protected void initialiseActivity() {
        this.setContentView(SEND_MESSAGE_UI_LAYOUT);
        this.initialiseTextInputFields();
        this.initialiseSendMessageToRemoteServerButton();
    }

    private void initialiseTextInputFields() {
        textBoxForInputtingUserMessage = (EditText) this.findViewById(R.id.messageTextField);
        textBoxForInputtingMessageRecipient  = (EditText) this.findViewById(R.id.recipientTextField);
    }

    private void initialiseSendMessageToRemoteServerButton() {
        Button sendMessageToRemoteServerButton = (Button)this.findViewById(R.id.moveToSendMessageActivityButton);
        sendMessageToRemoteServerButton.setOnClickListener(this.whenSendMessageButtonIsPressedThenSendTheMessageFromTheTextBoxToRemoteServerAndFinishActivityIfRecipientUserExists());
    }

    private View.OnClickListener whenSendMessageButtonIsPressedThenSendTheMessageFromTheTextBoxToRemoteServerAndFinishActivityIfRecipientUserExists() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerformableAction actionToSendTheMessageFromTheTextBoxToRemoteServerAndFinishActivity = SendMessageActivity.this.createActionToSendTheMessageFromTheTextBoxToRemoteServerAndFinishActivity();
                String reciepientUsername = textBoxForInputtingMessageRecipient.getText().toString();
                SendMessageActivity.this.performActionIfRecipientUserExistsWithRecipientUsername(actionToSendTheMessageFromTheTextBoxToRemoteServerAndFinishActivity, reciepientUsername);
            }
        };
    }

    private void performActionIfRecipientUserExistsWithRecipientUsername(PerformableAction actionToPerform, String recipientUserName) {
        RemoteServerAPI remoteServerAPI = RemoteServerAPI.remoteServerAPIWithContext(SendMessageActivity.this.getApplicationContext());
        remoteServerAPI.checkIfUserExistsWithUsernameAndAPIDelegate(recipientUserName,
                SendMessageActivity.this.delegateWhichPerformsAGivenActionIfResponseIsTrueAndGivesErrorOtherwise(actionToPerform, "Invalid recipient \'" + recipientUserName + "\'"));

    }

    private PerformableAction createActionToSendTheMessageFromTheTextBoxToRemoteServerAndFinishActivity() {
        return new PerformableAction() {
            @Override
            public void performAction() {
                SendMessageActivity.this.sendMessageFromTextBoxToRemoteServer();
                SendMessageActivity.this.finish();
            }
        };
    }

    private void sendMessageFromTextBoxToRemoteServer() {
        String messageToSendToServer = textBoxForInputtingUserMessage.getText().toString();
        String reciepientOfMessageToRemoteServer = textBoxForInputtingMessageRecipient.getText().toString();
        RemoteServerAPI remoteServerAPI = RemoteServerAPI.remoteServerAPIWithContext(SendMessageActivity.this.getApplicationContext());
        remoteServerAPI.sendMessageToRemoteServerWithDestinationUsernameAndMessageToSendAndAPIDelegate(reciepientOfMessageToRemoteServer, messageToSendToServer, SendMessageActivity.this.delegateWhichDisplaysAPIResponseAsToastMessage());
    }

    private RemoteServerAPI.RemoteServerAPIDelegate delegateWhichDisplaysAPIResponseAsToastMessage() {
        return new RemoteServerAPI.RemoteServerAPIDelegate() {
            @Override
            public void receiveResponseFromAPI(RemoteServerAPIResponse apiResponse) {
                SendMessageActivity.this.showMessageToUser(apiResponse.responseAsString());
            }
        };
    }

    private RemoteServerAPI.RemoteServerAPIDelegate delegateWhichPerformsAGivenActionIfResponseIsTrueAndGivesErrorOtherwise(final PerformableAction actionToPerform, final String errorMessageOnFalse) {
        return new RemoteServerAPI.RemoteServerAPIDelegate() {
            @Override
            public void receiveResponseFromAPI(RemoteServerAPIResponse apiResponse) {
                if (apiResponse.responseAsBoolean())
                    actionToPerform.performAction();
                else SendMessageActivity.this.showMessageToUser(errorMessageOnFalse);
            }
        };
    }
}
