package com.opark.opark;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.flipkart.chatheads.ui.ChatHead;
import com.flipkart.chatheads.ui.ChatHeadViewAdapter;
import com.flipkart.chatheads.ui.MaximizedArrangement;
import com.flipkart.chatheads.ui.MinimizedArrangement;
import com.flipkart.chatheads.ui.container.DefaultChatHeadManager;
import com.flipkart.chatheads.ui.container.WindowManagerContainer;
import com.flipkart.circularImageView.CircularDrawable;
import com.flipkart.circularImageView.TextDrawer;
import com.flipkart.circularImageView.notification.CircularNotificationDrawer;
import com.opark.opark.fixtures.MessagesFixtures;
import com.opark.opark.model.Message;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by thyemunchun on 14/10/2017.
 */

public class ChatHeadService extends Service {

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    private DefaultChatHeadManager<String> chatHeadManager;
    private int chatHeadIdentifier = 0;
    private WindowManagerContainer windowManagerContainer;
    private Map<String, View> viewCache = new HashMap<>();

    private MessagesList messagesList;
    private MessagesListAdapter<Message> adapter;
    private Date lastLoadedDate;
    protected ImageLoader imageLoader;


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        windowManagerContainer = new WindowManagerContainer(this);
        chatHeadManager = new DefaultChatHeadManager<String>(this, windowManagerContainer);
        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Picasso.with(getApplicationContext()).load(url).into(imageView);
            }
        };
        chatHeadManager.setViewAdapter(new ChatHeadViewAdapter<String>() {

            @Override
            public View attachView(String key, ChatHead chatHead, ViewGroup parent) {
                View cachedView = viewCache.get(key);
                if (cachedView == null) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.chat_fragment, parent, false);
//                    TextView identifier = (TextView) view.findViewById(R.id.identifier);
//                    identifier.setText(key);
                    MessageInput inputView = (MessageInput) view.findViewById(R.id.input);
                    messagesList = (MessagesList) view.findViewById(R.id.messagesList);
                    adapter = new MessagesListAdapter<>("0", imageLoader);
                    messagesList.setAdapter(adapter);
                    adapter.addToStart(MessagesFixtures.getTextMessage(), true);
                    adapter.setLoadMoreListener(new MessagesListAdapter.OnLoadMoreListener() {
                        @Override
                        public void onLoadMore(int page, int totalItemsCount) {
                            Log.i("Chat before",String.valueOf(totalItemsCount));
                            if (totalItemsCount < 100) {
                                loadMessages();
                                Log.i("Chat",String.valueOf(totalItemsCount));
                            }
                        }
                    });
                    inputView.setInputListener(new MessageInput.InputListener() {
                        @Override
                        public boolean onSubmit(CharSequence input) {
                            //validate and send message
                            adapter.addToStart(
                                    MessagesFixtures.getTextMessage(input.toString(),"0"), true);
                            new Handler().postDelayed(new Runnable() { //imitation of internet connection
                                @Override
                                public void run() {
                                    adapter.addToStart(MessagesFixtures.getTextMessage(),true);
                                }
                            }, 2000);
                            return true;
                        }
                    });
                    inputView.setAttachmentsListener(new MessageInput.AttachmentsListener() {
                        @Override
                        public void onAddAttachments() {
                            //select attachments
                            adapter.addToStart(
                                    MessagesFixtures.getImageMessage(), true);
                        }
                    });
                    cachedView = view;
                    viewCache.put(key, view);
                }
                parent.addView(cachedView);
                return cachedView;
            }

            @Override
            public void detachView(String key, ChatHead<? extends Serializable> chatHead, ViewGroup parent) {
                View cachedView = viewCache.get(key);
                if(cachedView!=null) {
                    parent.removeView(cachedView);
                }
            }

            @Override
            public void removeView(String key, ChatHead<? extends Serializable> chatHead, ViewGroup parent) {
                View cachedView = viewCache.get(key);
                if(cachedView!=null) {
                    viewCache.remove(key);
                    parent.removeView(cachedView);
                }
            }

            @Override
            public Drawable getChatHeadDrawable(String key) {
                return ChatHeadService.this.getChatHeadDrawable(key);
            }
        });

//        addChatHead();
//        addChatHead();
//        addChatHead();
        addChatHead();
        chatHeadManager.setArrangement(MinimizedArrangement.class, null);
        moveToForeground();

    }

    private Drawable getChatHeadDrawable(String key) {
        Random rnd = new Random();
        int randomColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        CircularDrawable circularDrawable = new CircularDrawable();
        circularDrawable.setBitmapOrTextOrIcon(new TextDrawer().setText("C" + key).setBackgroundColor(randomColor));
        int badgeCount = (int) (Math.random() * 10f);
        circularDrawable.setNotificationDrawer(new CircularNotificationDrawer().setNotificationText(String.valueOf(badgeCount)).setNotificationAngle(45).setNotificationColor(Color.WHITE, Color.RED));
//        circularDrawable.setBorder(Color.WHITE, 3);
        return circularDrawable;

    }

    private void moveToForeground() {
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_template_icon_bg)
                .setContentTitle("Springy heads")
                .setContentText("Click to configure.")
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, Log.class), 0))
                .build();

        startForeground(1, notification);
    }

    public void addChatHead() {
        chatHeadIdentifier++;
        chatHeadManager.addChatHead(String.valueOf(chatHeadIdentifier), false, true);
        chatHeadManager.bringToFront(chatHeadManager.findChatHeadByKey(String.valueOf(chatHeadIdentifier)));
    }

    public void removeChatHead() {
        chatHeadManager.removeChatHead(String.valueOf(chatHeadIdentifier), true);
        chatHeadIdentifier--;
    }

    public void removeAllChatHeads() {
        chatHeadIdentifier = 0;
        chatHeadManager.removeAllChatHeads(true);
    }

    public void toggleArrangement() {
        if (chatHeadManager.getActiveArrangement() instanceof MinimizedArrangement) {
            chatHeadManager.setArrangement(MaximizedArrangement.class, null);
        } else {
            chatHeadManager.setArrangement(MinimizedArrangement.class, null);
        }
    }

    public void updateBadgeCount() {
        chatHeadManager.reloadDrawable(String.valueOf(chatHeadIdentifier));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        windowManagerContainer.destroy();
    }

    public void minimize() {
        chatHeadManager.setArrangement(MinimizedArrangement.class,null);
    }


    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        ChatHeadService getService() {
            // Return this instance of LocalService so clients can call public methods
            return ChatHeadService.this;
        }
    }

    protected void loadMessages() {
        new Handler().postDelayed(new Runnable() { //imitation of internet connection
            @Override
            public void run() {
                ArrayList<Message> messages = MessagesFixtures.getMessages(lastLoadedDate);
                lastLoadedDate = messages.get(messages.size() - 1).getCreatedAt();
                adapter.addToEnd(messages, false);
            }
        }, 1000);
    }
}