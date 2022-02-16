package costmanager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * The model of the CostManager Application , implements the IModel interface.
 * CostManagerModel is responsible with dealing with the database and operating it (Inserting ,Updating ,Deleting ....)
 */
public class CostManagerModel implements ICostManagerModel
{

    public String driverFullQualifiedName = "com.mysql.jdbc.Driver";
    public String connectionString = "jdbc:mysql://localhost:3306/costmanger";
    private final String dataBaseUserName = "natan";
    private final String dataBasePassword = "1234";
    private String currentUser ;
    private User loginUser ;
    private  User connectedUser;

    /**
     * CostMangerModel constructor
     * @throws CostManagerException if there's a ClassNotFoundException
     */
    public CostManagerModel() throws CostManagerException {
        try
        {
            Class.forName(driverFullQualifiedName);
        }
        catch (ClassNotFoundException e)
        {
            throw new CostManagerException("Error with the driver manager", e);
        }

    }

    /**
     * currentUser Getter
     * @return the currentUer
     */
    @Override
    public String getCurrentUser() {return currentUser;}

    /**
     * currentUser Setter
     * @param currentUser the name of the current user to set
     */
    @Override
    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * A method which connects the user to the database
     * if the username is not in the users table a new row will be created in the database
     * @param userName The name of the user to connect
     * @throws CostManagerException if there's been a SQLException or if the insert was not successful
     */
    @Override
    public void connectUser(String userName) throws CostManagerException
    {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString, dataBaseUserName, dataBasePassword);
            String getAllUsersQuery =  "SELECT * FROM `users`";
            String findSpecificUserQuery = "SELECT  *  FROM `users` WHERE `userName` = ?" ;
            PreparedStatement allUserStatement = connection.prepareStatement(getAllUsersQuery);
            ResultSet allUserResult = allUserStatement.executeQuery();

            //users table isn't empty
            if (allUserResult.next()) {
                PreparedStatement specificUserStatement = connection.prepareStatement(findSpecificUserQuery);
                specificUserStatement.setString(1,userName);
                ResultSet specificUserResultSet = specificUserStatement.executeQuery();

                //Check if the user already exists in the table
                boolean isUserExist = specificUserResultSet.next();

                //The user doesn't exist a new one is created
                if (!isUserExist) {
                    // move to the last row in the users table
                    allUserResult.last();
                    PreparedStatement insertNewUserPreparedStatement = connection.prepareStatement(
                            "INSERT INTO `users`(`id`, `userName`) VALUES(?,?)");

                    //Get the value of the last id in the table , set the new user id : last id + 1
                    insertNewUserPreparedStatement.setInt(1,allUserResult.getInt("id") + 1 );
                    insertNewUserPreparedStatement.setString(2,userName);

                    //The insert was successful
                    if (insertNewUserPreparedStatement.executeUpdate() == 1) {
                        //adding the default categories for the new user
                        addDefaultCategories(userName);
                    }
                    else {
                        throw new CostManagerException("Error while inserting new user into the database");
                    }
                }
            }
            //The users table is empty , so we insert the first row of the table
            else {
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO `users`(`id`, `userName`) VALUES(?,?)");
                statement.setInt(1,1);
                statement.setString(2,userName);

                //The insert was successful
                if (statement.executeUpdate() == 1) {
                    addDefaultCategories(userName);
                    System.out.println("New user was created");
                    System.out.println("new user is:"+userName);
                }
                else {
                    throw new CostManagerException("Error while trying to create the first user");
                }
            }
        } catch (SQLException e) {
            throw new CostManagerException("Error while connecting the user");
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        setCurrentUser(userName);
    }
    @Override
    public void connectUserNew(String userName,String password) throws CostManagerException
    {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString, dataBaseUserName, dataBasePassword);
            String getAllUsersQuery =  "SELECT * FROM `users`";
            String findSpecificUserQuery = "SELECT  *  FROM `users` WHERE `userName` = AND `password` = ?" ;
            PreparedStatement allUserStatement = connection.prepareStatement(getAllUsersQuery);
            ResultSet allUserResult = allUserStatement.executeQuery();

            //users table isn't empty
            if (allUserResult.next()) {
                PreparedStatement specificUserStatement = connection.prepareStatement(findSpecificUserQuery);
                specificUserStatement.setString(1,userName);
                specificUserStatement.setString(2,password);
                ResultSet specificUserResultSet = specificUserStatement.executeQuery();

                //Check if the user already exists in the table
                boolean isUserExist = specificUserResultSet.next();

                //The user doesn't exist a new one is created
                if (!isUserExist) {
                    // move to the last row in the users table
                    allUserResult.last();
                    PreparedStatement insertNewUserPreparedStatement = connection.prepareStatement(
                            "INSERT INTO `users`(`id`, `userName`,`password`) VALUES(?,?,?)");

                    //Get the value of the last id in the table , set the new user id : last id + 1
                    insertNewUserPreparedStatement.setInt(1,allUserResult.getInt("id") + 1 );
                    insertNewUserPreparedStatement.setString(2,userName);
                    insertNewUserPreparedStatement.setString(3,password);

                    //The insert was successful
                    if (insertNewUserPreparedStatement.executeUpdate() == 1) {
                        //adding the default categories for the new user
                        addDefaultCategories(userName);
                    }
                    else {
                        throw new CostManagerException("Error while inserting new user into the database");
                    }
                }
            }
            //The users table is empty , so we insert the first row of the table
            else {
                PreparedStatement insertionStatement = connection.prepareStatement(
                        "INSERT INTO `users`(`id`, `userName`,`password`) VALUES(?,?,?)");
                insertionStatement.setInt(1,1);
                insertionStatement.setString(2, userName);
                insertionStatement.setString(3, password);

                //The insert was successful
                if (insertionStatement.executeUpdate() == 1) {
                    addDefaultCategories(userName);
                    System.out.println("New user was created");
                    System.out.println("new user is:"+ password);
                }
                else {
                    throw new CostManagerException("Error while trying to create the first user");
                }
            }
        } catch (SQLException e) {
            throw new CostManagerException("Error while connecting the user");
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        setCurrentUser(userName);
    }

    /**
     * A method which creates a list of default categories for a new user in the database
     * @param userName The username of which the categories are created for
     * @throws CostManagerException if there's been a SQLException or if the insert was not successful
     */
    @Override
    public void addDefaultCategories(String userName) throws CostManagerException {
        List<String> defaultCategories = new ArrayList<>();
        //Default categories
        defaultCategories.add("General");
        defaultCategories.add("Transport");
        defaultCategories.add("Hygiene");
        defaultCategories.add("Clothes");
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString, dataBaseUserName, dataBasePassword);

            //Insert each category from the defaultCategories list to the database
            for (String category : defaultCategories) {
                PreparedStatement insertNewCategoryStatement = connection.prepareStatement("INSERT INTO `categories`(`categoryName` ,`userName`) VALUES (?,?)");
                insertNewCategoryStatement.setString(1, category);
                insertNewCategoryStatement.setString(2, userName);

                //Insert was unsuccessful
                if (insertNewCategoryStatement.executeUpdate() == 0) {
                    throw new CostManagerException("Error while adding new category");
                }
            }
        } catch (SQLException e) {
            throw new CostManagerException("Error while adding the default categories");
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * A method which inserts the new cost item to the database
     * @param costItem The new cost item to insert to the database
     * @throws CostManagerException if there's been a SQLException or if the insert was not successful
     */
    @Override
    public void addCost(CostItem costItem) throws CostManagerException
    {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(connectionString, dataBaseUserName, dataBasePassword);
            String sqlInsertQuery = "INSERT INTO `costmanagerdb` (`userName`,`Category`, `CostSum`, `Currency`, `Description`,`Date Added`) VALUES(?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlInsertQuery);
            preparedStatement.setString(1, costItem.getUserName());
            preparedStatement.setString(2, costItem.getCategory());
            preparedStatement.setDouble(3, costItem.getCostSum());
            preparedStatement.setString(4, costItem.getCurrency());
            preparedStatement.setString(5, costItem.getDescription());
            preparedStatement.setDate(6, costItem.getDate());
             int result = preparedStatement.executeUpdate();

             //The insert was unsuccessful
             if (result == 0){
                 throw new CostManagerException("The item was not inserted to the db");
             }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            throw new CostManagerException("Error while trying to add a new cost to the db");
        }

        finally {
            try {
                if (connection != null)
                {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * A method which removes the selected costItem from the database
     * @param itemToRemove The costItem to remove
     * @throws CostManagerException if there's been a SQLException or if the update was not successful
     */
    @Override
    public void removeCost(CostItem itemToRemove) throws CostManagerException
    {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(connectionString, dataBaseUserName, dataBasePassword);
            String sqlDeleteUpdate = "DELETE FROM `costmanagerdb` WHERE `UserName` = ? AND `CostID` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteUpdate);
            preparedStatement.setString(1, itemToRemove.getUserName());
            preparedStatement.setInt(2, itemToRemove.getCostId());
            int result = preparedStatement.executeUpdate();

            //Check if the update was successful
            if (result == 0){
                throw new CostManagerException("The removal from the db was unsuccessful!");
            }
        }
        catch (SQLException e) {
            throw new CostManagerException("Error while removing the selected costItem");
        }
        finally {
            try {
                if (connection != null)
                {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * A method which deletes every item between the startDateBoundary and endDateBoundary from the database
     * @param startDateBoundary The start date from where to delete
     * @param endDateBoundary The end date of items to delete
     * @throws CostManagerException if there's been a SQLException or if the update was not successful
     */
    @Override
    public void removeCostByDateRange(Date startDateBoundary, Date endDateBoundary) throws CostManagerException
    {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(connectionString, dataBaseUserName, dataBasePassword);
            String sqlDeleteUpdate = "DELETE FROM `costmanagerdb`  WHERE `Date Added` BETWEEN ? AND ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteUpdate);
            preparedStatement.setDate(1, startDateBoundary);
            preparedStatement.setDate(2, endDateBoundary);
            int result = preparedStatement.executeUpdate();

            //The update was unsuccessful
            if (result == 0){
                throw new CostManagerException("The remove was unsuccessful!");
            }

        } catch (SQLException e) {
            throw new CostManagerException("Couldn't remove the items within the given range : "
                    + startDateBoundary.toString() + " - " + endDateBoundary.toString());
        }
        finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * A method which adds a new category to a specific user to the database
     * @param categoryToAdd The category to add to the database
     * @param currentUser The name of the user which the category belongs to
     * @throws CostManagerException if there's been a SQLException or if the update was not successful
     */
    @Override
    public void addCategory(Category categoryToAdd ,String currentUser) throws CostManagerException {
        Connection connection = null;
        boolean isCategoryAlreadyPresent = false;

        //Get all the categories which belongs to the user currently
        List<Category> currentCategoriesList = (List<Category>) getAllCategories(currentUser);

        //Check if the category already exists
        for (Category category: currentCategoriesList) {
            if(Objects.equals(category.getCategoryName(), categoryToAdd.getCategoryName()))
            {
                isCategoryAlreadyPresent = true;
                break;
            }
        }

        //If the category does not exist , it gets added to the database
        if (!isCategoryAlreadyPresent) {
            try {
                connection = DriverManager.getConnection(connectionString, dataBaseUserName, dataBasePassword);
                PreparedStatement insertNewCategoryStatement = connection.prepareStatement("INSERT INTO `categories`(`categoryName` ,`userName`) VALUES (?,?)");
                insertNewCategoryStatement.setString(1, categoryToAdd.getCategoryName());
                insertNewCategoryStatement.setString(2, currentUser);
                int result = insertNewCategoryStatement.executeUpdate();

                //The insert was unsuccessful
                if(result == 0){
                    throw new CostManagerException("The new category was not added");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                throw new CostManagerException("Error while adding a new category");
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * A method which gets all the costItems of the given user from the database and returns it
     * @param userName The username of which the costItems that gets retrieved from the database
     * @return A collection of the costItems
     * @throws CostManagerException if there's been a SQLException
     */
    @Override
    public Collection<CostItem> getAllCosts(String userName) throws CostManagerException
    {
        Connection connection = null;
        List<CostItem> costItemsList = new ArrayList<>();
        try
        {
            connection = DriverManager.getConnection(connectionString, dataBaseUserName, dataBasePassword);
            PreparedStatement getAllCostsStatement = connection.prepareStatement("SELECT * FROM `costmanagerdb` WHERE `userName` = ?");
            getAllCostsStatement.setString(1,userName);
            ResultSet allCostsResultSet = getAllCostsStatement.executeQuery();

            //Retrieve all the costItems from the result set and add it to the Collection
            while (allCostsResultSet.next())
            {
                String category = allCostsResultSet.getString("category");
                String currency = allCostsResultSet.getString("currency");
                String description = allCostsResultSet.getString("description");
                Date date = allCostsResultSet.getDate("Date Added");
                double costSum = allCostsResultSet.getDouble("costSum");
                int costID = allCostsResultSet.getInt("CostID");
                costItemsList.add(new CostItem(userName, costID , costSum, description, category, currency ,date));
            }

        } catch (SQLException e) {
            throw new CostManagerException("Error while getting all the cost items from the db");
        }
        finally
        {
            try {
                if (connection != null)
                {
                    connection.close();
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return  costItemsList;
    }

    /**
     * A method which retrieves all the categories for the chosen user from the database and returns them
     * @param currentUser The username of which the categories list gets retrieved from the database
     * @return A collection of Categories
     * @throws CostManagerException if there's been a SQLException or if the query was unsuccessful
     */
    @Override
    public Collection<Category> getAllCategories(String currentUser) throws CostManagerException
    {
        Connection connection = null;
        List<Category> categoryList = new ArrayList<>();

        try
        {
            connection = DriverManager.getConnection(connectionString, dataBaseUserName, dataBasePassword);
            PreparedStatement getAllCategoriesStatement = connection.prepareStatement("SELECT * FROM `categories` WHERE `userName` = ?");
            getAllCategoriesStatement.setString(1,currentUser);
            ResultSet allCategoriesResultSet = getAllCategoriesStatement.executeQuery();

            //The query was unsuccessful
            if(allCategoriesResultSet == null){
                throw new CostManagerException("Error while getting all the categories!");
            }

            while (allCategoriesResultSet.next())
            {
                String category = allCategoriesResultSet.getString("categoryName");

                categoryList.add(new Category(category));
            }

        } catch (SQLException e) {
            throw new CostManagerException("Error while getting the collection of categories from the db");
        }
        finally
        {
            try {
                if (connection != null)
                {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return  categoryList;
    }

    /**
     *A method which gets every item between the startDateBoundary and endDateBoundary from the database
     * and returns them as collection
     * @param startDateBoundary The start date of the costItems to be retrieved
     * @param endDateBoundary The end date of the costItems to be retrieved
     * @param currentUser THe name of the user of the costItems that are being retrieved
     * @return A collection of costItems
     * @throws CostManagerException if there's been a SQLException
     */
    @Override
    public Collection<CostItem> getCostByDateRange(String startDateBoundary, String endDateBoundary,String currentUser) throws CostManagerException
    {
        Connection connection = null;
        List<CostItem> costItemsList = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(connectionString, dataBaseUserName, dataBasePassword);
            PreparedStatement getFilteredCostsStatement = connection.prepareStatement(
                    "SELECT * FROM `costmanagerdb` WHERE `Date Added` BETWEEN ? AND ? AND `userName` = ?");
            getFilteredCostsStatement.setDate(1,Date.valueOf(startDateBoundary));
            getFilteredCostsStatement.setDate(2,Date.valueOf(endDateBoundary));
            getFilteredCostsStatement.setString(3,currentUser);
            ResultSet allCostsResultSet = getFilteredCostsStatement.executeQuery();

            //Retrieve all the costItems from the result set and add it to the Collection
            while (allCostsResultSet.next()) {
                String category = allCostsResultSet.getString("category");
                String currency = allCostsResultSet.getString("currency");
                String description = allCostsResultSet.getString("description");
                Date date = allCostsResultSet.getDate("Date Added");
                double costSum = allCostsResultSet.getDouble("costSum");
                int costID = allCostsResultSet.getInt("CostID");
                costItemsList.add(new CostItem(getCurrentUser(), costID , costSum, description, category, currency,date));
            }

        } catch (SQLException e) {
            throw new CostManagerException("Error while retrieving all costItems by the given date range");
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return  costItemsList;
    }
}
