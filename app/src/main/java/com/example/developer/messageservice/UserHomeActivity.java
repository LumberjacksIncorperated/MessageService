package com.example.developer.messageservice;

import android.view.View;
        import android.widget.Button;

public class UserHomeActivity extends LoginRequiredActivity {

    private final int HOME_SCREEN_MAIN_UI_LAYOUT = R.layout.activity_user_home;
    @Override
    protected void initialiseActivity() {
        this.setContentView(HOME_SCREEN_MAIN_UI_LAYOUT);
        this.initialiseHomeButtons();
    }

    private void initialiseHomeButtons() {
        this.initialiseMoveToSendMessageAcivityButton();
        this.initialiseMoveToRecieveMessagesActivityButton();
    }

    private void initialiseMoveToRecieveMessagesActivityButton() {
        Button recieveMessagesAcivityButton = (Button)this.findViewById(R.id.moveToRecieveMessagesActivityButton);
        recieveMessagesAcivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserHomeActivity.this.moveToActivityOfClass(RecieveMessagesActivity.class);
            }
        });
    }

    private void initialiseMoveToSendMessageAcivityButton() {
        Button sendMessageAcivityButton = (Button)this.findViewById(R.id.moveToSendMessageActivityButton);
        sendMessageAcivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserHomeActivity.this.moveToActivityOfClass(SendMessageActivity.class);
            }
        });
    }
}
