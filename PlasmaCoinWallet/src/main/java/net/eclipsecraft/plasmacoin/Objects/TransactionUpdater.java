package net.eclipsecraft.plasmacoin.Objects;

import javafx.application.Platform;
import javafx.concurrent.Task;
import net.eclipsecraft.plasmacoin.app.Main;

public class TransactionUpdater extends Task {
    private GuiFrame frame;
    public TransactionUpdater(GuiFrame frame){
        this.frame = frame;
    }

    private static int test = 0;
    @Override
    protected Object call() throws Exception {
        while(test > -1) {
//            label.setWrapText(true);
            System.out.println("updating");
            frame.getAddressField().setText(Main.getCurrentChain().getChain().size()+"");
            Thread.sleep(1000);
        }
        return null;
    }

    @Override
    public void run() {
        super.run();
        try{
            call();
        }catch (Exception e){}
    }

    public Thread start(){
        System.out.println("Starting");
        Thread thread = new Thread(this);
        thread.start();
        return thread;
    }
}
