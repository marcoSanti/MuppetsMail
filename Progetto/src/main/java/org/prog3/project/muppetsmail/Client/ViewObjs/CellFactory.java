package org.prog3.project.muppetsmail.Client.ViewObjs;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import org.prog3.project.muppetsmail.Client.ClientApp;
import org.prog3.project.muppetsmail.SharedModel.Mail;

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

            ImageButton forwardMail = new ImageButton(new Image(ClientApp.class.getResource("bin.png").toString()), 27 ,27);
            ImageButton deleteMail = new ImageButton(new Image(ClientApp.class.getResource("forward.png").toString()), 30 ,30);



            gridPane.add( forwardMail , 4,0);
            gridPane.add( deleteMail , 3,0);



            setText(null);
            setGraphic(gridPane);

        }
    }
}
