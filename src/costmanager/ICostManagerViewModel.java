package costmanager;

/**
 * ICostManagerViewModel is an interface of the view model for the CostManager Application which responsible for
 * the communication of the view and the model
 */
public interface ICostManagerViewModel {
    /**
     *A method which inserts a new item to the database and then update the view.
     * @param costItem The costItem to insert
     */
    void addCostItem(CostItem costItem);

    /**
     *A method which gets all the costItems of the current user from the database via the model and updates the view
     * @param userName The username of which the costItems belong
     */
    void getAllCosts(String userName);

    /**
     * Sets the view
     * @param view The view to be set
     */
    void setView(ICostManagerView view);

    /**
     * Sets the model
     * @param model The model to be set
     */
    void setModel(ICostManagerModel model);

    /**
     * A method which sets the current user of the application
     * @param userName The username to connect
     */
    void setUserName(String userName);

    /**
     * A method which gets all the categories from the database via the model and updates the categories in the view.
     */
    void getAllCategories();

    /**
     * A method which deletes a selected costItem from the database and then updates the view.
     * @param indexOfCostItemToDelete The index of the costItem to delete.
     */
    void deleteCostItem(int indexOfCostItemToDelete);
//    void deleteCostItems(int[] indexesOfCostItemToDelete);

    /**
     * A method which disconnects the current user and connects the new user
     * @param newUsername The new username to connect
     */
    void logOut(String newUsername);

    /**
     * A method which adds a new category to the database via the model and then updates the view
     * @param categoryName The name of the new category
     */
    void addNewCategory(String categoryName);

    /**
     * A method which gets filtered by date costItems from the database via the model and updates the view
     * @param startDate The start date for the costItems
     * @param endDate The end date for the costItems
     */
    void getFilteredCostsByDatesRange(String startDate, String endDate);
}
