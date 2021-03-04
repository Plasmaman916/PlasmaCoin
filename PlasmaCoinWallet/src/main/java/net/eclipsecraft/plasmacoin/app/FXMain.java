package net.eclipsecraft.plasmacoin.app;

import net.eclipsecraft.plasmacoin.Objects.GuiFrame;
import net.eclipsecraft.plasmacoin.Objects.TransactionUpdater;


public class FXMain extends Thread{
    public static void main(String[] args) {
//        launch(args);
        new FXMain().start();
    }

    @Override
    public void run() {
        super.run();
        GuiFrame f = new GuiFrame();
        f.setVisible(true);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //    @Override
//    public void stop() throws Exception {
//        super.stop();
//        System.exit(0);
//    }
//
//    @Override
//    public void start(Stage primaryStage) throws IOException {
//        primaryStage.setTitle("Hello World!");
//
//        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/start.fxml"));
//        AnchorPane root = (AnchorPane) loader.load();
//
//        Scene scene = new Scene(root, 600, 400);
//
//        primaryStage.setScene(scene);
//        primaryStage.show();
//
//
//        Label address = (Label) scene.lookup("#address");
//        address.setText("No Wallet Selected. Please load a wallet or create a new one!");
//
//        new TransactionUpdater(address).start();
//
//        Button openWallet = (Button) scene.lookup("#openWallet");
//        openWallet.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
//            DirectoryChooser directoryChooser = new DirectoryChooser();
//            directoryChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
//            File selectedDirectory = directoryChooser.showDialog(primaryStage);
//            Wallet wallet = new Wallet();
//            boolean loaded = false;
//            try {
//                loaded = wallet.loadKeyPair(selectedDirectory.getAbsolutePath());
//            }catch (NullPointerException e){}
//            if(!loaded){
//                Dialog dialog = new Dialog<>();
//                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
//                FXMLLoader diaLoader = new FXMLLoader(Main.class.getResource("/dialog.fxml"));
//
//                DialogPane pane = null;
//                try {
//                    pane = diaLoader.load();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Button cont = (Button) pane.lookup("#warnCont");
//                cont.setText("Ok");
//                Button canc = (Button) pane.lookup("#warnCanc");
//                Label text = (Label) pane.lookup("#warnText");
//                text.setText("Error! This directory ("+selectedDirectory.getAbsolutePath()+") does not contain properly named key files!");
//                cont.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
//                    dialog.setResult(Boolean.TRUE);
//                    dialog.close();
//                });
//                canc.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
//                    dialog.setResult(Boolean.TRUE);
//                    dialog.close();
//                });
//                dialog.setDialogPane(pane);
//                dialog.show();
//                return;
//            }
//            address.setText("Address: "+wallet.getPublicKeyHex());
//            address.setWrapText(true);
//            Main.setPathToCurrentWallet(selectedDirectory.getAbsolutePath());
//            Main.loadWallet(wallet);
//        });
//
//        Button walletGen = (Button) scene.lookup("#walletGen");
//        walletGen.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
//            DirectoryChooser directoryChooser = new DirectoryChooser();
//            directoryChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
//            File selectedDirectory = directoryChooser.showDialog(primaryStage);
//            if(selectedDirectory != null) {
//                Dialog dialog = new Dialog<>();
//                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
//                FXMLLoader diaLoader = new FXMLLoader(Main.class.getResource("/dialog.fxml"));
//
//                DialogPane pane = null;
//                try {
//                    pane = diaLoader.load();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Button cont = (Button) pane.lookup("#warnCont");
//                Button canc = (Button) pane.lookup("#warnCanc");
//                Label text = (Label) pane.lookup("#warnText");
//                text.setText("Warning! This action (Generate New Wallet) will overwrite any exist wallet files in this directory ("+selectedDirectory.getAbsolutePath()+")! You may choose to cancel or continue!");
//                cont.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
//                    System.out.println(selectedDirectory.getAbsolutePath());
//                    Wallet wallet = new Wallet();
//                    wallet.saveKeyPair(selectedDirectory.getAbsolutePath());
//                    address.setText("Address: "+wallet.getPublicKeyHex());
//                    address.setWrapText(true);
//                    Main.setPathToCurrentWallet(selectedDirectory.getAbsolutePath());
//                    Main.loadWallet(wallet);
//                    dialog.setResult(Boolean.TRUE);
//                    dialog.close();
//                });
//                canc.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
//                    dialog.setResult(Boolean.TRUE);
//                    dialog.close();
//                });
//                dialog.setDialogPane(pane);
//                dialog.show();
//            }
//        });
//        Button walletSave = (Button) scene.lookup("#saveWallet");
//        walletSave.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
//            DirectoryChooser directoryChooser = new DirectoryChooser();
//            directoryChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
//            File selectedDirectory = directoryChooser.showDialog(primaryStage);
//            if(selectedDirectory != null) {
//                Dialog dialog = new Dialog<>();
//                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
//                FXMLLoader diaLoader = new FXMLLoader(Main.class.getResource("/dialog.fxml"));
//
//                DialogPane pane = null;
//                try {
//                    pane = diaLoader.load();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Button cont = (Button) pane.lookup("#warnCont");
//                Button canc = (Button) pane.lookup("#warnCanc");
//                Label text = (Label) pane.lookup("#warnText");
//                text.setText("Warning! This action (Save Wallet) will overwrite any existing wallet files in this directory ("+selectedDirectory.getAbsolutePath()+")! You may choose to cancel or continue!");
//                cont.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
//                    System.out.println(selectedDirectory.getAbsolutePath());
//                    Wallet wallet = Main.getWallet();
//                    wallet.saveKeyPair(selectedDirectory.getAbsolutePath());
//                    address.setText("Address: "+wallet.getPublicKeyHex());
//                    address.setWrapText(true);
//                    Main.setPathToCurrentWallet(selectedDirectory.getAbsolutePath());
//                    dialog.setResult(Boolean.TRUE);
//                    dialog.close();
//                });
//                canc.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
//                    dialog.setResult(Boolean.TRUE);
//                    dialog.close();
//                });
//                dialog.setDialogPane(pane);
//                dialog.show();
//            }
//        });
//    }
}
