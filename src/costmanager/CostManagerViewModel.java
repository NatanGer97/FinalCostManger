package costmanager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CostManagerViewModel is the view model of the CostManager Application
 * implements the interface ICostManagerViewModel
 * executing operations on a different thread for the view.
 * separating the view and model from each other , and handles the communication between them with a thread pool via ExecutorService
 */
public class CostManagerViewModel implements ICostManagerViewModel {
    private ICostManagerModel model;
    private ICostManagerView view;
    private final ExecutorService executorService;
    private String userName;

    /**
     * CostManagerViewModel constructor , sets the model and the view
     * @param iView The view to be set
     * @param iModel The model to be set
     */
    public CostManagerViewModel(ICostManagerView iView, ICostManagerModel iModel){
        this.executorService = Executors.newFixedThreadPool(3);
        setModel(iModel);
        setView(iView);
    }

    /**
     *A method which inserts a new item to the database and then update the view.
     * The method runs on a separate thread.
     * in case of a failure the method catches CostManagerException and shows the error message
     * in the view via SwingUtilities.invokeLater
     * @param costItem The costItem to insert
     */
    @Override
    public void addCostItem(CostItem costItem) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //add the costItem to the database through the model
                    model.addCost(costItem);

                    //call on getAllCosts method which updates the view
                    getAllCosts(userName);
                } catch (CostManagerException e) {

                    //Show failure message in view
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            view.showMessage(new Message(e.getMessage()));
                        }
                    });
                }
            }
        });
    }

    /**
     * A method which gets all the categories from the database via the model and updates the categories in the view.
     * The method runs on a separate thread.
     * in case of a failure the method catches CostManagerException and shows the error message
     * in the view via SwingUtilities.invokeLater.
     */
    @Override
    public void getAllCategories() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //Get all the categories for the current user from the database
                    ArrayList<Category> categoryList = (ArrayList<Category>) model.getAllCategories(userName);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            //update the categories list in the view
                            view.setCategoryList(categoryList);
                        }
                    });
                } catch (CostManagerException e) {
                    //Show failure message in view
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {

                            view.showMessage(new Message(e.getMessage()));
                        }
                    });
                }
            }
        });
    }

    /**
     * A method which deletes a selected costItem from the database and then updates the view.
     * The method runs on a separate thread.
     * in case of a failure the method catches CostManagerException and shows the error message
     * in the view via SwingUtilities.invokeLater.
     * @param indexOfCostItemToDelete The index of the costItem to delete.
     */
    @Override
    public void deleteCostItem(int indexOfCostItemToDelete) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //Get all the costItems for the current user from the database
                    List<CostItem> allCostsList = (List<CostItem>) model.getAllCosts(userName);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //Remove the costItem from the database
                                model.removeCost(allCostsList.get(indexOfCostItemToDelete));

                                //Update the costItems list in the view
                                getAllCosts(userName);
                            }catch (CostManagerException e){
                                //Show failure message in view
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        view.showMessage(new Message(e.getMessage()));
                                    }
                                });
                            }
                        }
                    });
                } catch (CostManagerException e) {
                    //Show failure message in view
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            view.showMessage(new Message(e.getMessage()));
                        }
                    });
                }
            }
        });
    }

  /*  @Override
    public void deleteCostItems(int[] indexesOfCostItemToDelete)
    {
        executorService.submit(() -> {
            try {
                //Get all the costItems for the current user from the database
                List<CostItem> allCostsList = (List<CostItem>) model.getAllCosts(userName);
                SwingUtilities.invokeLater(() -> {
                    try {
                        //Remove the costItems from the database
                        for(int costToRemoveIndex = 0; costToRemoveIndex < indexesOfCostItemToDelete.length; costToRemoveIndex++)
                        {
                            model.removeCost(allCostsList.get(indexesOfCostItemToDelete[costToRemoveIndex]));
                        }

                        //Update the costItems list in the view
                        getAllCosts(userName);
                    }catch (CostManagerException e){
                        //Show failure message in view
                        SwingUtilities.invokeLater(() -> view.showMessage(new Message(e.getMessage())));
                    }
                });
            } catch (CostManagerException e) {
                //Show failure message in view
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        view.showMessage(new Message(e.getMessage()));
                    }
                });
            }
        });
    }
*/
    /**
     * A method which disconnects the current user and connects the new user
     * @param newUsername The new username to connect
     */
    @Override
    public void logOut(String newUsername) {
        //Set the new user
        setUserName(newUsername);
    }

    /**
     * A method which adds a new category to the database via the model and then updates the view
     *  The method runs on a separate thread.
     *  in case of a failure the method catches CostManagerException and shows the error message
     *  in the view via SwingUtilities.invokeLater
     * @param categoryName The name of the new category
     */
    @Override
    public void addNewCategory(String categoryName) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //Add the category to the database
                    model.addCategory(new Category(categoryName), userName);

                    //Update the category list in the view
                    getAllCategories();
                } catch (CostManagerException e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        //Show failure message in view
                        @Override
                        public void run() {
                            view.showMessage(new Message(e.getMessage()));
                        }
                    });
                }
            }
        });
    }

    /**
     *A method which gets all the costItems of the current user from the database via the model and updates the view
     *The method runs on a separate thread.
     *in case of a failure the method catches CostManagerException and shows the error message
     *in the view via SwingUtilities.invokeLater
     * @param userName The username of which the costItems belong
     */
    @Override
    public void getAllCosts(String userName) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //Get the list of costItems from the database via the model
                    List<CostItem> allCostsList = (List<CostItem>) model.getAllCosts(userName);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            //update the view
                            view.setAllCosts(allCostsList);
                        }
                    });
                } catch (CostManagerException e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        //Show failure message in view
                        @Override
                        public void run() {
                            view.showMessage(new Message(e.getMessage()));
                        }
                    });
                }
            }
        });
    }

    /**
     * Sets the model
     * @param model The model to be set
     */
    @Override
    public void setModel(ICostManagerModel model) {
        this.model = model;
    }

    /**
     * Sets the view
     * @param view The view to be set
     */
    @Override
    public void setView(ICostManagerView view) {
        this.view = view;
    }

    /**
     * A method which sets the current user of the application
     * The method runs on a separate thread.
     * in case of a failure the method catches CostManagerException and shows the error message
     * in the view via SwingUtilities.invokeLater
     * @param userName The username to connect
     */
    @Override
    public void setUserName(String userName) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //Connect the user via the model
                    model.connectUser(userName);

                    //Get the user costItems
                    getAllCosts(userName);

                    //Get the user categories
                    getAllCategories();
                } catch (CostManagerException e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        //Show failure message in the view
                        @Override
                        public void run() {
                            view.showMessage(new Message(e.getMessage()));
                        }
                    });
                }
            }
        });
        this.userName = userName;
    }

    /**
     * A method which gets filtered by date costItems from the database via the model and updates the view
     * The method runs on a separate thread.
     * in case of a failure the method catches CostManagerException and shows the error message
     * in the view via SwingUtilities.invokeLater
     * @param startDate The start date for the costItems
     * @param endDate The end date for the costItems
     */
    @Override
    public void getFilteredCostsByDatesRange(String startDate, String endDate) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //Get the filtered costItems from the database via the model
                    List<CostItem> filteredCostsList = (List<CostItem>) model.getCostByDateRange(startDate, endDate, userName);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            //Update the view
                            view.showFilteredCosts(filteredCostsList);
                        }
                    });
                } catch (CostManagerException e) {
                    //Show failure message in the view
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            view.showMessage(new Message(e.getMessage()));
                        }
                    });
                }

            }
        });
    }
}



