package com.example.developer.messageservice;

import android.graphics.Color;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

public class RecieveMessagesActivity extends LoginRequiredActivity {

    private final int RECIEVE_MESSAGE_UI_LAYOUT = R.layout.activity_recieve_message;
    @Override
    protected void initialiseActivity() {
        this.setContentView(RECIEVE_MESSAGE_UI_LAYOUT);
        this.downloadMessagesAndConfigureTableWithDownloadedMessages();
    }

    private void downloadMessagesAndConfigureTableWithDownloadedMessages() {
        RemoteServerAPI remoteServerAPI = RemoteServerAPI.remoteServerAPIWithContext(RecieveMessagesActivity.this.getApplicationContext());
        remoteServerAPI.receiveMyMessagesWithAPIDelegate(this.delegateWhichConfiguresTablesWithNewReceivedMessages());
    }

    private RemoteServerAPI.RemoteServerAPIDelegate delegateWhichConfiguresTablesWithNewReceivedMessages() {
        return new RemoteServerAPI.RemoteServerAPIDelegate() {
            @Override
            public void receiveResponseFromAPI(RemoteServerAPIResponse apiResponse) {
                final List<ReceivedMessage> receivedMessages = ReceivedMessage.parseReceivedMessagesFromAPIResponse(apiResponse);
                RecieveMessagesActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RecieveMessagesActivity.this.configureTableWithReceivedMessages(receivedMessages);
                    }
                });
            }
        };
    }

    private void configureTableWithReceivedMessages(List<ReceivedMessage> receivedMessages) {

        TableLayout t1;

        TableLayout tl = (TableLayout) findViewById(R.id.receivedMessagesTable);

        TextView[] textArray = new TextView[receivedMessages.size()];
        TableRow[] tr_head = new TableRow[receivedMessages.size()];

        int i = -1;
        for(final ReceivedMessage receivedMessage : receivedMessages){
            i++;
            String productDescription = receivedMessage.getSentFromUsername();

//Create the tablerows
            tr_head[i] = new TableRow(this);
            tr_head[i].setId(i+1);
            tr_head[i].setBackgroundColor(Color.GRAY);
            tr_head[i].setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            tr_head[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RecieveMessagesActivity.this.showPopupAlertMessageToUser(receivedMessage.getMessageText());
                }
            });

            // Here create the TextView dynamically

            textArray[i] = new TextView(this);
            textArray[i].setId(i+111);
            textArray[i].setText(productDescription);
            textArray[i].setTextColor(Color.WHITE);
            textArray[i].setPadding(5, 5, 5, 5);
            tr_head[i].addView(textArray[i]);
// Add each table row to table layout

            tl.addView(tr_head[i], new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

        } // end of for loop
    }

}
