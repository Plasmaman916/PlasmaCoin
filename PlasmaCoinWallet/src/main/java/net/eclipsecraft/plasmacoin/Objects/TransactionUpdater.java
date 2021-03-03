package net.eclipsecraft.plasmacoin.Objects;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import net.eclipsecraft.plasmacoin.app.Main;

public class TransactionUpdater extends Task {
    private Label label;
    public TransactionUpdater(Label label){
        this.label = label;
    }

    private static int test = 0;
    @Override
    protected Object call() throws Exception {
//        while(test > -1) {
//            label.setWrapText(true);
//            Platform.runLater(() -> {
//                label.setText(Main.getCurrentChain().getChain().size()+"");
////                test++;
//            });
//            Thread.sleep(1000);
//        }
        return null;
    }

    public Thread start(){
        Thread thread = new Thread(this);
        thread.start();
        return thread;
    }
}
