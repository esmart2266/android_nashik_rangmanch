package in.gov.nmc.rangmach.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OTP_Receiver extends BroadcastReceiver
{
    private static EditText editText;

    public void setEditText(EditText editText)
    {
        OTP_Receiver.editText = editText;
    }



    @Override
    public void onReceive(Context context, Intent intent)
    {
            try {

              SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);

            for (SmsMessage sms : messages)
            {
                String message = sms.getMessageBody();
                Pattern p = Pattern.compile("\\d+");
                Matcher m = p.matcher(message);
                while(m.find()) {
                    System.out.println(m.group());
                    if (m.group().trim().length() == 4) {
                        editText.setText(m.group());
                    }
                }
            }

        } catch (Exception e) {

        }

    }
}