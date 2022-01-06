package org.prog3.project.muppetsmail.Client;

import org.prog3.project.muppetsmail.Client.Controller.HomeController;
import org.prog3.project.muppetsmail.Client.Model.ClientModel;
import org.prog3.project.muppetsmail.SharedModel.Constants;
import javafx.application.Platform;


public class ClientDemon extends Thread {

    private ClientModel appModel;
    private HomeController hmCtrl;

    public ClientDemon(ClientModel mdl, HomeController hme) {
        this.appModel = mdl;
        this.hmCtrl = hme;
        this.setDaemon(true);
    }

    public void run(){
        try{
            while(true){

               if(appModel.getClientIsLogged().get()){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            hmCtrl.refreshMailbox();
                        }
                    }); 
                    checkNewEmailPresence();
                }
                

                Thread.sleep(1000);
            }
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    private void checkNewEmailPresence() {
        Object lock = new Object();
        appModel.connectionManager.runTask(Constants.COMMAND_CHECK_NEW_MAIL_PRESENCE, lock);
    }

}
