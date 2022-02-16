package costmanager;

import java.sql.Date;
import java.util.Collection;

/**
 * ICostManagerModel is an interface which represents a model who deals with a database
 */
public interface ICostManagerModel {
    /**
     * A method which gets all the costItems of the given user from the database and returns it
     * @param userName The username of which the costItems that gets retrieved from the database
     * @return A collection of the costItems
     * @throws CostManagerException if there's been a SQLException
     */
    Collection<CostItem> getAllCosts(String userName) throws CostManagerException;

    /**
     * A method which retrieves all the categories for the chosen user from the database and returns them
     * @param currentUser The username of which the categories list gets retrieved from the database
     * @return A collection of Categories
     * @throws CostManagerException if there's been a SQLException or if the query was unsuccessful
     */
    Collection<Category>  getAllCategories(String currentUser) throws CostManagerException;

    /**
     *A method which gets every item between the startDateBoundary and endDateBoundary from the database
     * and returns them as collection
     * @param startDateBoundary The start date of the costItems to be retrieved
     * @param endDateBoundary The end date of the costItems to be retrieved
     * @param currentUser THe name of the user of the costItems that are being retrieved
     * @return A collection of costItems
     * @throws CostManagerException if there's been a SQLException
     */
    Collection<CostItem>  getCostByDateRange(String startDateBoundary, String endDateBoundary, String currentUser) throws CostManagerException;

    /**
     * A method which adds a new category to a specific user to the database
     * @param categoryToAdd The category to add to the database
     * @param currentUser The name of the user which the category belongs to
     * @throws CostManagerException if there's been a SQLException or if the update was not successful
     */
    void addCategory(Category categoryToAdd, String currentUser) throws CostManagerException;

    /**
     * A method which inserts the new cost item to the database
     * @param costItem The new cost item to insert to the database
     * @throws CostManagerException if there's been a SQLException or if the insert was not successful
     */
    void addCost(CostItem costItem) throws CostManagerException;
    /**
     * A method which removes the selected costItem from the database
     * @param itemToRemove The costItem to remove
     * @throws CostManagerException if there's been a SQLException or if the update was not successful
     */
    void removeCost(CostItem itemToRemove) throws CostManagerException;

    /**
     * A method which deletes every item between the startDateBoundary and endDateBoundary from the database
     * @param startDateBoundary The start date from where to delete
     * @param endDateBoundary The end date of items to delete
     * @throws CostManagerException if there's been a SQLException or if the update was not successful
     */
    void removeCostByDateRange(Date startDateBoundary, Date endDateBoundary) throws CostManagerException;

    /**
     * A method which connects the user to the database
     * if the username is not in the users table a new row will be created in the database
     * @param userName The name of the user to connect
     * @throws CostManagerException if there's been a SQLException or if the insert was not successful
     */
    void connectUser(String userName)throws CostManagerException;
    void connectUserNew(String userName,String password)throws CostManagerException;

    /**
     * A method which creates a list of default categories for a new user in the database
     * @param userName The username of which the categories are created for
     * @throws CostManagerException if there's been a SQLException or if the insert was not successful
     */
    void addDefaultCategories(String userName) throws CostManagerException;

    /**
     * currentUser Setter
     * @param currentUser the name of the current user to set
     */
    void setCurrentUser(String currentUser) throws CostManagerException;

    /**
     * currentUser Getter
     * @return the currentUer
     */
    String getCurrentUser() throws CostManagerException;
}
