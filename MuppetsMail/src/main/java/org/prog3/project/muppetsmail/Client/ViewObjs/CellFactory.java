package org.prog3.project.muppetsmail.Client.ViewObjs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import org.prog3.project.muppetsmail.Client.ClientApp;
import org.prog3.project.muppetsmail.Client.Model.ClientModel;
import org.prog3.project.muppetsmail.SharedModel.Mail;
import org.prog3.project.muppetsmail.SharedModel.MailBox;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CellFactory extends ListCell<Mail> {
    public Label mailDateLabel;
    public Label mailFromLabel;
    public Label mailSubjectLabel;
    public GridPane gridPane;
    String dateOut;
    private final DateFormat dtfOld = new SimpleDateFormat("dd/MM/yy");
    private final DateFormat dtfToday = new SimpleDateFormat("HH:mm");

    private  MailBox userMailbox;

    public CellFactory( MailBox userMailbox){
        this.userMailbox = userMailbox;
    }

    @Override
    protected void updateItem(Mail mail, boolean empty) {
        super.updateItem(mail, empty);


        if(empty || mail == null){
            setText(null);
            setGraphic(null);
        }else{
            FXMLLoader loader = new FXMLLoader(ClientApp.class.getResource("mailCellFactory.fxml"));
            loader.setController(this);

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }


            Instant now = Instant.now();
            boolean isWithinPrior24Hours = ( ! mail.getDate().toInstant().isBefore( now.minus( 24 , ChronoUnit.HOURS) ) ) && ( mail.getDate().toInstant().isBefore( now )) ;

            if(isWithinPrior24Hours){
                 dateOut = dtfToday.format(mail.getDate()) ;
            }else{
                 dateOut = dtfOld.format(mail.getDate());
            }

            mailDateLabel.setText(dateOut);
            mailFromLabel.setText("from: " +mail.getFrom());
            mailSubjectLabel.setText("Subject: "+ mail.getSubject());

            ImageButton forwardMail = new ImageButton(new Image(ClientApp.class.getResource("forward.png").toString()), 27 ,27);
            ImageButton deleteMail = new ImageButton(new Image(ClientApp.class.getResource("bin.png").toString()), 30 ,30);

            forwardMail.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    //copiare la mail e inviarla a qualcun altro!
                }
            });

            deleteMail.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    synchronized (mail) {
                        int oldMailBox = mail.getCurrentMailBox();
                        mail.setCurrentMailBox(3);
                        userMailbox.moveTo(mail, oldMailBox, 3);
                    }
                }
            });


            gridPane.add( forwardMail , 3,0);
            gridPane.add( deleteMail , 4,0);



            setText(null);
            setGraphic(gridPane);

        }
    }
}
