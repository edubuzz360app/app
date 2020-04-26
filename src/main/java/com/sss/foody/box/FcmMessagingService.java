package com.sss.foody.box;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import androidx.core.app.NotificationCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FcmMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived ( remoteMessage );
        if(remoteMessage.getData ().size () >0)
        {
            String title,message,imageurl;
            title = remoteMessage.getData ().get("title");
            message = remoteMessage.getData ().get("message");
            imageurl =remoteMessage.getData ().get("imageurl");

            Intent intent = new Intent ( this, MainActivity.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity ( this,0,intent,PendingIntent.FLAG_ONE_SHOT );
            Uri sounduri = RingtoneManager.getDefaultUri ( RingtoneManager.TYPE_NOTIFICATION );
            final NotificationCompat.Builder builder = new NotificationCompat.Builder ( this , "M_CH_ID");
            builder.setContentTitle ( title );
            builder.setContentText ( message );
            builder.setContentIntent ( pendingIntent );
            builder.setSound ( sounduri );
            builder.setSmallIcon ( R.drawable.ic_stat_name );

            ImageRequest imageRequest = new ImageRequest ( imageurl, new Response.Listener<Bitmap> () {
                @Override
                public void onResponse(Bitmap response) {
                    builder.setStyle ( new NotificationCompat.BigPictureStyle().bigPicture ( response ) );
                    NotificationManager notificationManager = (NotificationManager) getSystemService ( NOTIFICATION_SERVICE );
                    notificationManager.notify ( 0,builder.build() );
                }
            },0, 0, null,Bitmap.Config.RGB_565, new Response.ErrorListener () {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            } );
            MySingleton.getmInstance ( this ).addToRequestQue ( imageRequest );
        }
    }
}
