package com.opark.opark.fixtures;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import com.opark.opark.model.Message;
import com.opark.opark.model.Author;


/*
 * Created by troy379 on 12.12.16.
 */
public final class MessagesFixtures extends FixturesData {
    private MessagesFixtures() {
        throw new AssertionError();
    }

    public static Message getImageMessage() {
        Message message = new Message(getRandomId(), getUser("1"), null);
        message.setImage(new Message.Image(getRandomImage()));
        return message;
    }

//    public static Message getVoiceMessage() {
//        Message message = new Message(getRandomId(), getUser(), null);
//        message.setVoice(new Message.Voice("http://example.com", rnd.nextInt(200) + 30));
//        return message;
//    }

    public static Message getTextMessage() {
        return getTextMessage(getRandomMessage(),"1");
    }

    public static Message getTextMessage(String text, String senderID) {
        return new Message(getRandomId(), getUser(senderID), text);
    }

    public static ArrayList<Message> getMessages(Date startDate) {
        ArrayList<Message> messages = new ArrayList<>();
        for (int i = 0; i < 10/*days count*/; i++) {
            int countPerDay = rnd.nextInt(5) + 1;

            for (int j = 0; j < countPerDay; j++) {
                Message message;
                if (i % 2 == 0 && j % 3 == 0) {
                    message = getImageMessage();
                } else {
                    message = getTextMessage();
                }

                Calendar calendar = Calendar.getInstance();
                if (startDate != null) calendar.setTime(startDate);
                calendar.add(Calendar.DAY_OF_MONTH, -(i * i + 1));

                message.setCreatedAt(calendar.getTime());
                messages.add(message);
            }
        }
        return messages;
    }

    private static Author getUser(String senderID) {
        boolean even = rnd.nextBoolean();
        return new Author(
                senderID,
                even ? names.get(0) : names.get(1),
                even ? avatars.get(0) : avatars.get(1),
                true);
    }
}
