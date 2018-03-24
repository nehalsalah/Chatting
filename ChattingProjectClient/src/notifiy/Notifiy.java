/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notifiy;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 * Generic class that used to send notification to the user
 * @author EslamWaheed
 */
public class Notifiy {

    /**
     * only we have the constructor that take the date to show the notification
     * @param title
     * @param text
     * @param duration
     * @param position 
     */
    public Notifiy(String title, String text, Duration duration, Pos position) {
        Image image = new Image("logo.png");
        Notifications notifications = Notifications.create()
                .title(title)
                .text(text)
                .graphic(new ImageView(image))
                .hideAfter(duration)
                .position(position)
                .darkStyle();
        notifications.show();
    }
}
