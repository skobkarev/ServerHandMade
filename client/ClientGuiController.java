package client;

public class ClientGuiController extends Client {

    private ClientGuiModel model;

    private ClientGuiView view;

    public ClientGuiController(){
        this.model = new ClientGuiModel();
        this.view = new ClientGuiView(this);
    }

    @Override
    protected String getServerAddress() {
        return view.getServerAddress();
    }

    @Override
    protected int getServerPort() {
        return view.getServerPort();
    }

    protected String getUserName(){
        return view.getUserName();
    }

    public ClientGuiModel getModel(){
        return model;
    }

    public static void main(String[] args) {
        ClientGuiController clientGuiController = new ClientGuiController();
        clientGuiController.run();
    }

    public class GuiSocketThread extends SocketThread{

        @Override
        protected void processIncomingMessage(String message){
            model.setNewMessage(message);
            view.refreshMessages();
        }
        @Override
        protected void informAboutAddingNewUser(String userName){
            model.addUser(userName);
            view.refreshUsers();
        }
        @Override
        protected void informAboutDeletingNewUser(String userName){
            model.deleteUser(userName);
            view.refreshUsers();
        }
        @Override
        protected void notifyConnectionStatusChanged(boolean clientConnected){
            view.notifyConnectionStatusChanged(clientConnected);
        }
    }

    protected SocketThread getSocketThread(){

        return new GuiSocketThread();
    }

    public void run(){
        getSocketThread().start();
    }

}
