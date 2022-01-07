package org.prog3.project.muppetsmail.Client;

import org.prog3.project.muppetsmail.Client.Controller.HomeController;
import org.prog3.project.muppetsmail.Client.Model.ClientModel;
import org.prog3.project.muppetsmail.SharedModel.Constants;
import javafx.application.Platform;

/**
 * this class is a thread that executes two functions:
 * 1) refresh every second the list of messages shown in the observable list in the home view
 * 2) checks for new emails in the inbox and shows an alert if new messages are found
 */
public class ClientDemon extends Thread {

    private ClientModel appModel;
    private HomeController hmCtrl;
    private int timeout;

    /**
     * 
     * @param mdl Model of the client app
     * @param hme Controller of the homeView
     * @param timeout Integer value to tell how many milliseconds to wait before running again checks
     */
    public ClientDemon(ClientModel mdl, HomeController hme, int timeout) {
        this.appModel = mdl;
        this.hmCtrl = hme;
        this.timeout = timeout;
        this.setDaemon(true);
    }

    public void run(){
        try{
            while(true){
                if(appModel.getClientIsLogged().get()){
                    refreshMailBox();
                    checkNewEmailPresence();
                }
                Thread.sleep(timeout);
            }
        }catch(InterruptedException e){
           System.out.println(e.getMessage());
        }
    }

    /**
     * this method check whether there are new messages in the inbox
     */
    private void checkNewEmailPresence() {
        appModel.connectionManager.runTask(Constants.COMMAND_CHECK_NEW_MAIL_PRESENCE, new Object());
    }

    /**
     * This method updates the shown mail messages in the homeView
     */
    private void refreshMailBox(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                hmCtrl.refreshMailbox();
            }
        }); 
    }

}
