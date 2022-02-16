package costmanager;


import javax.swing.*;

public class Program {
    public static void main(String[] args) throws CostManagerException
    {

        ICostManagerView view = new CostManagerView();
        ICostManagerModel model = new CostManagerModel();
        ICostManagerViewModel viewModel = new CostManagerViewModel(view ,model);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void  run() {
                    view.initialize();
                    view.start();
            }
        });
        view.setIViewModel(viewModel);
    }
}
