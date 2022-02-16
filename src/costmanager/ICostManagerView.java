package costmanager;

import java.util.Collection;

/**
 * IView is an interface which represents the View which represent the UI of the Application
 */
public interface ICostManagerView {
    /**
     * initialize is the first method that runs and while the View Object is created
     * its role is equivalent to the constructor
     * initialize all the components
     */
    void initialize();

    /**
     * start is a method which places all the view components on the main frame
     * and making them visible and functional.
     * add listeners for all the buttons'.
     * initialize the cost table according to current user etc.
     */
    void start();

    /**
     * A method which is responsible for the login dialog
     * and start the login flow
     */
    void login();

    /**
     * Responsible for showing the logout dialog
     * Handles the user logout request and starts the flow
     */
    void logout();

    /**
     * showMessage gets a Message Object and displays a popUp dialog over the view
     * which shows a unique message to the user.
     * @param message The message which contains the content to be showed
     */
    void showMessage(Message message);

    /**
     * Set the viewModel for this view
     * @param viewModel The viewModel to be set
     */
    void setIViewModel(ICostManagerViewModel viewModel);

    /**
     * Responsible for displaying user costs in Table with cost attributes
     * @param allCostsList Collection of CostItems that contains all exiting costs
     */
    void setAllCosts(Collection<CostItem> allCostsList);

    /**
     * Provide data to the list model which inflate the Category List component
     * Which provide and show the optional cost categories
     * @param categoryList A Collection containing categories objects
     */
    void setCategoryList(Collection<Category> categoryList);

    /**
     * Set current username
     * @param userName The username to be set
     */
    void setUserName(String userName);

    /**
     * Show the view who contains specific cost items
     * that filtered from all cost according to selected time period
     * @param allCostsList an instance of Cost item Collection that hold filters items
     */
    void showFilteredCosts(Collection<CostItem> allCostsList);
}
