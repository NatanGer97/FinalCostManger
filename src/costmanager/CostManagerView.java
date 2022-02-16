package costmanager;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

/**
 * Implements the ICostManagerView interface
 * CostManagerView represent the UI of the application
 * holds reference to the CostManagerViewModel
 */
public class CostManagerView implements ICostManagerView {


    private ICostManagerViewModel viewModel;
    private JLabel headerLabel;
    private JFrame frameMain;
    private JTable tableCostItems;
    private DefaultListModel<String> jListModelForCategories;
    private JList<String> jListCategories;
    private JScrollPane scrollPaneForItemsTable, scrollPaneForCategories;
    private DefaultTableModel defaultTableCostItems;
    private JButton buttonAddCostItem, buttonDeleteCostItem, buttonLogout, buttonAddCategory, buttonFilterCostItemsByDates;
    private  JButton buttonCreateNewCost,buttonDeleteCostItems;
    private JPanel panelAddItem, panelButtons, panelAddItemAndButtonsPanels;
    private JTextField textFieldCostSum, textFieldDescription, textFieldCurrency;
    private JTextArea textAreaCostSum, textAreaDescription, textAreaCategory, textAreaCurrency;
    private final String[] colNamesForTable = {"Category", "Description", "Cost SUM", "Currency", "Date Added"};
    private String userName;
    private ArrayList<Category> currentCategoryList;

    /**
     * initialize is the first method that runs in the class and while the View Object is created
     * its role is equivalent to the constructor
     * initialize all the components
     * And made the login process of the user
     * This is the Entry Point of this Class
     */
    @Override
    public void initialize() {
        login();

        //initialize the main frame
        frameMain = new JFrame();
        frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameMain.setTitle("Cost Manager");
        /// header
        headerLabel.setEnabled(false);
        //initialize the buttons of the main frame
        buttonAddCostItem = new JButton("Add Item");
        buttonDeleteCostItem = new JButton("Delete selected item");
        buttonLogout = new JButton("Logout");
        buttonAddCategory = new JButton("Add Category");
        buttonFilterCostItemsByDates = new JButton("Filter by Dates");
        buttonCreateNewCost = new JButton("New Cost");
        buttonDeleteCostItems = new JButton("Delete selected items");

        // initialize label

        //initialize the panels
        panelAddItem = new JPanel();
        panelButtons = new JPanel();
        panelAddItemAndButtonsPanels = new JPanel();
        panelButtons.setBackground(new Color(153, 204, 255));


        //initialize the table for the CostItems
        tableCostItems = new JTable();
        defaultTableCostItems = new DefaultTableModel(0, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        defaultTableCostItems.setColumnIdentifiers(colNamesForTable);
        tableCostItems.setModel(defaultTableCostItems);
        tableCostItems.setRowSelectionAllowed(true);
        tableCostItems.setBackground(new Color(102, 178, 255));
        tableCostItems.getTableHeader().setBackground(new Color(0, 128, 255));

        //initialize the TextFields and TextAreas
        textFieldCostSum = new JTextField();
        textFieldDescription = new JTextField();
        textFieldCurrency = new JTextField();
        textAreaCostSum = new JTextArea("Cost");
        textAreaDescription = new JTextArea("Description");
        textAreaCategory = new JTextArea("Category");
        textAreaCurrency = new JTextArea("Currency");
        textAreaCategory.setEditable(false);
        textAreaDescription.setEditable(false);
        textAreaCostSum.setEditable(false);
        textAreaCurrency.setEditable(false);
        textAreaCategory.setBackground(new Color(153, 204, 255));
        textAreaCostSum.setBackground(new Color(153, 204, 255));
        textAreaCurrency.setBackground(new Color(153, 204, 255));
        textAreaDescription.setBackground(new Color(153, 204, 255));


        jListModelForCategories = new DefaultListModel<>();
        jListCategories = new JList<>(jListModelForCategories);
        scrollPaneForItemsTable = new JScrollPane(tableCostItems);
        scrollPaneForCategories = new JScrollPane(jListCategories);
        scrollPaneForItemsTable.getViewport().setBackground(new Color(204, 229, 255));
        jListCategories.setVisibleRowCount(0);
    }

    /**
     * start is a method which places all the view components on the main frame
     * and making them visible and functional.
     * add listeners for all the buttons'.
     * initialize the cost table according to current user.
     */
    @Override
    public void start() {
//        //Adding items to the panelAddItem panel
//        panelAddItem.setLayout(new GridLayout(2, 4));
//        panelAddItem.add(textAreaCategory, 0);
//        panelAddItem.add(textAreaDescription, 1);
//        panelAddItem.add(textAreaCostSum, 2);
//        panelAddItem.add(textAreaCurrency, 3);
//        panelAddItem.add(scrollPaneForCategories, 4);
//        panelAddItem.add(textFieldDescription, 5);
//        panelAddItem.add(textFieldCostSum, 6);
//        panelAddItem.add(textFieldCurrency, 7);

        //Adding items to the panelButtons panel
        panelButtons.setLayout(new FlowLayout());
        panelButtons.add(buttonDeleteCostItems);
        panelButtons.add(buttonDeleteCostItem);
        panelButtons.add(buttonLogout);
        panelButtons.add(buttonAddCategory);
        panelButtons.add(buttonFilterCostItemsByDates);
        panelButtons.add(buttonCreateNewCost);

        //Adding both previous panels into one panel
        panelAddItemAndButtonsPanels.setLayout(new GridLayout(2, 1));
        panelAddItemAndButtonsPanels.add(headerLabel, 0);
        panelAddItemAndButtonsPanels.add(panelButtons, 1);

        frameMain.setLayout(new BorderLayout());
        frameMain.add(panelAddItemAndButtonsPanels, BorderLayout.NORTH);

        //Get all the costs from the database and display them in the view
/*
        viewModel.getAllCosts(userName);
*/

        //Get all the categories from the database and display them in the view
        /*viewModel.getAllCategories();*/
        jListCategories.setLayoutOrientation(JList.VERTICAL);
        frameMain.add(scrollPaneForItemsTable, BorderLayout.CENTER);

        frameMain.setSize(700, 700);


        frameMain.setLocationRelativeTo(null);
        frameMain.setVisible(true);

        //Adding action listeners to the buttons
        buttonAddCategory.addActionListener(e -> addCategory());
        buttonLogout.addActionListener(e -> logout());
        buttonDeleteCostItem.addActionListener(e -> deleteItems());
//        buttonAddCostItem.addActionListener(e -> addItem());
        buttonFilterCostItemsByDates.addActionListener(e -> filterCosts());
        buttonCreateNewCost.addActionListener(e->newCost());
        buttonDeleteCostItems.addActionListener(e -> deleteItems());
    }

    private void newCost()
    {
        AddNewCostItemView addNewCostItemView = new AddNewCostItemView(currentCategoryList);
        addNewCostItemView.start();
    }

    private void addCategory() {
        /*
        Activated by the buttonAddCategory ActionListener
        get new category from user and move it to the viewModel in order to add her to the DB
        make also a validation of the input
        */
        String newCategoryName = JOptionPane.showInputDialog(frameMain, "Enter the name of the new category", "Add new category", JOptionPane.QUESTION_MESSAGE);
        if (newCategoryName != null && !Objects.equals(newCategoryName, "") && !Objects.equals(newCategoryName, "\n")) {
            viewModel.addNewCategory(newCategoryName);
        }
    }

//    private void addItem() {
//        /*
//        Activated by the buttonAddCostItem ActionListener
//        Get the input for the new costItem after validating it and send the new costItem to the ViewModel
//         */
//        if (costItemInputValidation()) {
//            double costSum = Double.parseDouble(textFieldCostSum.getText());
//            CostItem costItem = new CostItem(userName, costSum, textFieldDescription.getText(),
//                    currentCategoryList.get(jListCategories.getFirstVisibleIndex()).getCategoryName(), textFieldCurrency.getText());
//
//            viewModel.addCostItem(costItem);
//            // clear the input fields
//            textFieldCurrency.setText("");
//            textFieldDescription.setText("");
//            textFieldCostSum.setText("");
//        }
//    }

    private void deleteItems()
    {
        if (tableCostItems.getSelectedRow() == -1) // -1 -> indicate that no item was selected
        {
            JOptionPane.showMessageDialog(frameMain, "You have to select the cost you wish to delete");
        }
        else
        {
            for (int selectedRow : tableCostItems.getSelectedRows())
            {
                viewModel.deleteCostItem(selectedRow);
            }

            System.out.println(tableCostItems.getSelectedRows().length);
        }
    }
    private void deleteItem() {
        /*
        Activated by the buttonDeleteCostItem ActionListener
        Deleting an item from the table by sending it to the view model after validating if any row from the
        table has been selected.
         */
        if (tableCostItems.getSelectedRow() == -1) // -1 -> indicate that no item was selected
        {
            JOptionPane.showMessageDialog(frameMain, "You have to select the cost you wish to delete");
        }
        else
        {
            viewModel.deleteCostItem(tableCostItems.getSelectedRow());
        }
    }

    private void filterCosts() {
        /*
        Activated by the buttonFilterCostItemsByDates ActionListener
        Filtering the costItems by date by starting the chooseDateView
         */
        if (tableCostItems.getModel().getRowCount() == 0) {
            JOptionPane.showMessageDialog(frameMain, "Table is Empty");
        } else {
            ChooseDateView chooseDateView = new ChooseDateView();
            chooseDateView.start();
        }
    }

//    private boolean costItemInputValidation() {
//        /*
//        Validate all the fields entered at add new costItem text fields
//        and return the validation outcome
//         */
//        if (textFieldCostSum.getText().isEmpty() || textFieldDescription.getText().isEmpty()
//                || textFieldCurrency.getText().isEmpty() || currentCategoryList.isEmpty() || Character.isLetter(textFieldCostSum.getText().charAt(0))) {
//            JOptionPane.showMessageDialog(frameMain, "Invalid input");
//            return false;
//        }
//        return true;
//    }

    /**
     * A method which is responsible for the login dialog
     * and start the login flow
     */
    @Override
    public void login() {
        Object[] options1 = { "Login", "Cancel"};

        JPanel panel = new JPanel();
        panel.add(new JLabel("Enter your user name"));
        JTextField textField = new JTextField(10);
        panel.add(textField);

        int result = JOptionPane.showOptionDialog(null, panel, "Login",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options1, null);
        if (result == JOptionPane.YES_OPTION){
            viewModel.setUserName(textField.getText());
            setUserName(textField.getText());
            headerLabel = new JLabel("Hello " + userName,SwingConstants.CENTER);
        }

      /*  JFrame frame = new JFrame("Dialog");
        JLabel userNameLabel, passwordLabel, messageLabel;
        JTextField userNameText;
        JTextField passwordText;
        JButton registerButton, cancelButton;
//        JOptionPane jOptionPane = new JOptionPane();
        JDialog loginDialog = new JDialog(frame);
        loginDialog.setTitle("Login Dialog");
        // User Label
        userNameLabel = new JLabel();
        userNameLabel.setText("User Name :");
        userNameText = new JTextField();

        // Password
        passwordLabel = new JLabel();
        passwordLabel.setText("Password :");
        passwordText = new JTextField();
        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println(userNameText.getText() + " " + passwordText.getText());
                if (!userNameText.getText().isEmpty())
                {
                    viewModel.setUserName(userNameText.getText());
                    setUserName(userNameText.getText());
                    headerLabel = new JLabel("Hello " + userNameText.getText());
                }

            }
        });

        JPanel  loginPanel = new JPanel(new GridLayout(3, 1));

        loginPanel.add(userNameLabel);
        loginPanel.add(userNameText);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordText);
        loginPanel.add(registerButton);
//        loginDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        loginDialog.add(loginPanel, BorderLayout.CENTER);
        loginDialog.setTitle("Please Login Here !");
        loginDialog.setSize(300, 100);
        loginDialog.setVisible(true);
        frame.setVisible(true);;
        frame.setSize(50,50);*/
//        loginDialog.setLocationRelativeTo(frameMain);







       /* JLabel titleLabel = new JLabel("Enter User Name");
        ImageIcon icon = new ImageIcon("C:\\Users\\Natan\\OneDrive\\לימודים\\שנה ג\\Internet Programming\\CostManagerFinal\\src\\il\\ac\\hit\\costmanager\\login.png");
        JLabel loginIcon = new JLabel(icon);
        JPanel loginDialogPanel = new JPanel();
        loginDialogPanel.setLayout(new BorderLayout());
        loginIcon.setSize(10,10);
        loginDialogPanel.add(titleLabel,BorderLayout.NORTH);
        JButton loginButton = new JButton("login");
        JTextField userNameTextField = new JTextField();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        loginDialogPanel.add(loginIcon,BorderLayout.NORTH);
//        String inputUserName = JOptionPane.showInputDialog(null,"Login Dialog",  JOptionPane.QUESTION_MESSAGE);
        JOptionPane jOptionPane = new JOptionPane("hiii");
       JDialog dialog = new JDialog();
//       JDialog dialog = jOptionPane.createDialog("dialog");

       panel.add(loginButton,BorderLayout.SOUTH);
       panel.add(userNameTextField,BorderLayout.CENTER);
       dialog.add(panel);
       loginButton.addActionListener(new ActionListener()
       {
           @Override
           public void actionPerformed(ActionEvent e)
           {
               System.out.println(userNameTextField.getText());
           }
       });
       dialog.setSize(300,300);
       dialog.setResizable(false);
       dialog.setLocationRelativeTo(frameMain);
       dialog.setVisible(true);*/
        //The dialog was closed
//
//        if (inputUserName == null)
//            System.exit(0);
//
//        //set the username in the view model
//        viewModel.setUserName(inputUserName);
//        setUserName(inputUserName);
//        headerLabel = new JLabel("Hello " + userName,SwingConstants.CENTER);

    }



    /**
     * Responsible for showing the logout dialog
     * Handles the user logout request and starts the flow
     */
    @Override
    public void logout() {
        frameMain.setVisible(false);
        String newUsername = JOptionPane.showInputDialog(frameMain, "Enter Username", "Login", JOptionPane.QUESTION_MESSAGE);

        //Input validation
        while (Objects.equals(newUsername, null) || Objects.equals(newUsername, "")) {
            JOptionPane.showMessageDialog(frameMain, "Wrong username input");
            newUsername = JOptionPane.showInputDialog(frameMain, "Enter Username", "Login", JOptionPane.QUESTION_MESSAGE);
        }
        setUserName(newUsername);

        //Activates the logout method of the view model
        viewModel.logOut(newUsername);
        headerLabel.setText("Hello " + userName);
        frameMain.setVisible(true);
    }

    /**
     * Set the viewModel for this view
     * @param viewModel The viewModel to be set
     */
    @Override
    public void setIViewModel(ICostManagerViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Show the view who contains specific cost items
     * that filtered from all cost according to selected time period
     * @param allCostsList an instance of Cost item Collection that hold filters items
     */
    @Override
    public void showFilteredCosts(Collection<CostItem> allCostsList) {
        FilterCostsView filterCostsView = new FilterCostsView(allCostsList);
        filterCostsView.start();
    }

    /**
     * Responsible for displaying user costs in Table with cost attributes
     * @param allCostsList Collection of CostItems that contains all exiting costs
     */
    @Override
    public void setAllCosts(Collection<CostItem> allCostsList) {
        /*
        Iterate through each item in the collection and
        create a new row for each item and add it to the table
         */
        defaultTableCostItems.setRowCount(0);
        for (CostItem costItem : allCostsList) {
            defaultTableCostItems.addRow(new Object[]{costItem.getCategory(), costItem.getDescription(),
                    costItem.getCostSum(), costItem.getCurrency(), costItem.getDate().toString()});
        }
    }

    /**
     * Provide data to the list model which inflate the Category List component
     * Which provide and show the optional cost categories
     * @param categoryList A Collection containing categories objects
     */
    @Override
    public void setCategoryList(Collection<Category> categoryList) {
        if (categoryList != null) {
            currentCategoryList = (ArrayList<Category>) categoryList;
            jListModelForCategories.clear();
            for (int i = 0; i < categoryList.size(); i++) {
                jListModelForCategories.add(i, ((ArrayList<Category>) categoryList).get(i).getCategoryName());
            }
        }
    }

    /**
     * Set current username
     * @param userName The username to be set
     */
    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * showMessage gets a Message Object and displays a popUp dialog over the view
     * which shows a unique message to the user.
     * @param message The message which contains the content to be showed
     */
    @Override
    public void showMessage(Message message) {
        JOptionPane.showMessageDialog(frameMain, message.getMessageContent());
    }

    /**
     * Inner class represent the Date Chooser View
     * Provide  the user an option to choose Date range in order to filter costs by date range
     */
    public class ChooseDateView {
        private JDialog chooseDateJFrame;
        private JLabel title, leftDate, rightDate;
        private JDateChooser jDateChooserLeft;
        private JDateChooser jDateChooserRight;
        private JButton dateButton;
        private JPanel datePanel, labelsPanel;
        private JPanel leftPanel, rightPanel;

        /**
         * Constructor of ChooseDateView which calls the init method that initializes
         * the components
         */
        public ChooseDateView() {
            this.init();
        }

        private void init(){
            // init the view components
            chooseDateJFrame = new JDialog();
            chooseDateJFrame.setTitle("Choose Date");
            datePanel = new JPanel();
            labelsPanel = new JPanel();
            dateButton = new JButton("Choose Date");
            jDateChooserLeft = new JDateChooser();
            jDateChooserLeft.setDateFormatString("yyyy-MM-dd");
            jDateChooserRight = new JDateChooser();
            jDateChooserRight.setDateFormatString("yyyy-MM-dd");
            title = new JLabel("Chosen Date:");
            title.setHorizontalAlignment(SwingConstants.CENTER);
            leftDate = new JLabel("Start Date");
            rightDate = new JLabel("End Date");
            leftPanel = new JPanel();
            rightPanel = new JPanel();

            /*
            button which takes the chosen date,parsing them into String
            and pass them to the getFilteredCosts function
             */
            dateButton.addActionListener(e -> {
                Date startDate = jDateChooserLeft.getDate();
                Date endDate = jDateChooserRight.getDate();
                String strDateString = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
                String endDateString = new SimpleDateFormat("yyyy-MM-dd").format(endDate);
                chooseDateJFrame.dispose(); // close the frame
                viewModel.getFilteredCostsByDatesRange(strDateString, endDateString);
            });
        }
        /**
         * start responsible for make ChooseDateView visible and display this view to user
         */
        public void start() {
            // set and order components on thr view
            datePanel.setLayout(new BorderLayout());
            rightPanel.setLayout(new GridLayout(2, 0));
            rightPanel.add(rightDate);
            rightPanel.add(jDateChooserRight);
            leftPanel.setLayout(new GridLayout(2, 0));
            leftPanel.add(leftDate);
            leftPanel.add(jDateChooserLeft);
            labelsPanel.setLayout(new GridLayout(1, 0));
            datePanel.add(leftPanel, BorderLayout.WEST);
            datePanel.add(rightPanel, BorderLayout.EAST);
            datePanel.add(title, BorderLayout.NORTH);
            labelsPanel.add(dateButton);
            datePanel.add(labelsPanel, BorderLayout.SOUTH);
            chooseDateJFrame.add(datePanel);
            chooseDateJFrame.setSize(400, 200);
            chooseDateJFrame.setResizable(false);
            chooseDateJFrame.setLocationRelativeTo(frameMain);
            chooseDateJFrame.setVisible(true);
        }
    }

    /**
     * Inner class which represents a View
     * that show cost items between specific dated ranges
     */
    public class FilterCostsView {
        private JFrame filterFrame;
        private JPanel mainPanel;
        private JScrollPane scrollPaneToTable;
        private JTable filteredCostsTable;
        private DefaultTableModel tableModel;
        private JLabel emptyResultLabel;
        private Collection<CostItem> filteredCostsList;

        // flag to indicate whether the filter cost data is empty or not
        boolean emptyData;

        /**
         * FilterCostsView Constructor which receives Collection of CostsItem as parameter
         * @param filteredCostsList Collection of CostsItem that represents a filtered costs item
         */
        public FilterCostsView(Collection<CostItem> filteredCostsList) {
            setFilteredCostsList(filteredCostsList);
            init();
        }

        // set the emptyData with given value
        private void setEmptyData(boolean emptyData) {
            this.emptyData = emptyData;
        }

        private void setFilteredCostsList(Collection<CostItem> filteredCostsList){
            this.filteredCostsList = filteredCostsList;
        }

        private void init(){
            /*
            Initialize all the components
             */
            filterFrame = new JFrame("Filter Window");
            emptyResultLabel = new JLabel("Empty");
            emptyResultLabel.setVisible(false);
            mainPanel = new JPanel();
            filteredCostsTable = new JTable();
            tableModel = new DefaultTableModel(0, 0);
            tableModel.setColumnIdentifiers(colNamesForTable);
            filteredCostsTable.setModel(tableModel);
            filteredCostsTable.setRowSelectionAllowed(true);
            filteredCostsTable.setBackground(new Color(102, 178, 255));
            filteredCostsTable.getTableHeader().setBackground(new Color(0, 128, 255));
            scrollPaneToTable = new JScrollPane(filteredCostsTable);
            scrollPaneToTable.getViewport().setBackground(new Color(204, 229, 255));

            // fill the table
            setFilteredCostsTable(filteredCostsList);

            // determine if the table is empty
            setEmptyData(filteredCostsList.isEmpty());
        }

        /**
         * start is a method who place the components
         * and makes the view visible
         */
        public void start() {
            mainPanel.setLayout(new BorderLayout());
            /*
            if this condition is true -> main panel would set and have
            unique panel and not the table panel
             */
            if (emptyData) {
                mainPanel.add(emptyResultLabel, BorderLayout.CENTER);
                emptyResultLabel.setVisible(true);
                emptyResultLabel.setHorizontalAlignment(SwingConstants.CENTER);
            } else {
                mainPanel.add(scrollPaneToTable, BorderLayout.CENTER);
            }

            filterFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            filterFrame.setSize(400, 400);
            filterFrame.add(mainPanel);
            filterFrame.setLocationRelativeTo(frameMain);
            filterFrame.setVisible(true);
        }

        private void setFilteredCostsTable(Collection<CostItem> allCostsList) {
            /*
            fill the table with the filtered costItems
            */
            tableModel.setRowCount(0);
            for (CostItem costItem : allCostsList) {
                tableModel.addRow(new Object[]{costItem.getCategory(), costItem.getDescription(),
                        costItem.getCostSum(), costItem.getCurrency(), costItem.getDate().toString()});
            }
        }


    }


    /**
     * AddNewCostItemView is an inner class
     * which represents the view for adding a new costItem
     */
    public class AddNewCostItemView
    {
        // view components declarations;
        private final JDialog addNewCostMainDialog;
        private JScrollPane scrollPaneForCategories;
        private JComboBox<String> categoriesJComboBox;
        private JPanel mainPanel, addItemPanel;
        private JTextField textFieldCostSum, textFieldDescription, textFieldCurrency;
        private JTextArea textAreaCostSum;
        private JTextArea textAreaDescription;
        private JTextArea textAreaCategory;
        private final JTextArea textAreaCurrency;
        private final JButton addItemButton;
        private JDateChooser jDateChooser;


        public AddNewCostItemView(ArrayList<Category> categories)
        {
            // init the view components
            addNewCostMainDialog = new JDialog();
            addNewCostMainDialog.setTitle("Add new Cost");
            mainPanel = new JPanel();
            addItemPanel = new JPanel();
            categoriesJComboBox = new JComboBox<>();
            setCategoriesJComboBox(categories);
            scrollPaneForCategories = new JScrollPane(categoriesJComboBox);
            this.textFieldCostSum = new JTextField();
            this.textFieldDescription = new JTextField();
            this.textFieldCurrency = new JTextField();
            this.textAreaCostSum = new JTextArea("Cost");
            this.textAreaDescription = new JTextArea("Description");
            this.textAreaCategory = new JTextArea("Category");
            this.textAreaCurrency = new JTextArea("Currency");
            this.textAreaCategory.setEditable(false);
            this.textAreaDescription.setEditable(false);
            this.textAreaCostSum.setEditable(false);
            this.textAreaCurrency.setEditable(false);
            addItemButton = new JButton("Add Item");
            addItemButton.addActionListener(e -> addItem());
            //initialize date chooser
            jDateChooser = new JDateChooser();
            jDateChooser.setDate(new Date());
            jDateChooser.setDateFormatString("yyyy-MM-dd");


          // ignore not digit characters
            textFieldCostSum.addKeyListener(new KeyAdapter()
            {

                @Override
                public void keyTyped(KeyEvent e)
                {
                    if (Character.isLetter(e.getKeyChar()))
                    {
                        e.consume();
                    }
                }
            });


        }

        public void start()
        {
            //  locate and place the add item panel.
            mainPanel.setLayout(new BorderLayout());
            addItemPanel.setLayout(new GridLayout(0, 1));
            addItemPanel.add(new JLabel("Category",SwingConstants.CENTER));
            addItemPanel.add(scrollPaneForCategories);
            addItemPanel.add(new JLabel("Description",SwingConstants.CENTER));
            addItemPanel.add(textFieldDescription);
            addItemPanel.add(new JLabel("Cost",SwingConstants.CENTER));
            addItemPanel.add(textFieldCostSum);
            addItemPanel.add(new JLabel("Currency",SwingConstants.CENTER));
            addItemPanel.add(textFieldCurrency);
            addItemPanel.add(new JLabel("Date",SwingConstants.CENTER));
            addItemPanel.add(jDateChooser);
            addItemPanel.add(addItemButton);
            mainPanel.add(addItemPanel, BorderLayout.NORTH);

//            addNewCostMainDialog.add(new JScrollPane(text));


            addNewCostMainDialog.add(mainPanel);

            addNewCostMainDialog.setSize(400, 400);
            addNewCostMainDialog.setVisible(true);
        }
        private void addItem() {
        /*
        Activated by the buttonAddCostItem ActionListener
        Get the input for the new costItem after validating it and send the new costItem to the ViewModel
         */
            if (costItemInputValidation()) {
                double costSum = Double.parseDouble(textFieldCostSum.getText());
                CostItem costItem = new CostItem(
                        userName,
                        costSum,
                        textFieldDescription.getText(),
                        String.valueOf(categoriesJComboBox.getSelectedItem()),
                        textFieldCurrency.getText(),new SimpleDateFormat("yyyy-MM-dd").format(jDateChooser.getDate()));

                viewModel.addCostItem(costItem);
                addNewCostMainDialog.dispose();
                JOptionPane.showMessageDialog(frameMain,"New cost added successfully");
                // clear the input fields
//                textFieldCurrency.setText("");
//                textFieldDescription.setText("");
//                textFieldCostSum.setText("");

            }
        }
        private boolean costItemInputValidation() {
        /*
        Validate all the fields entered at add new costItem text fields
        and return the validation outcome
         */
            if (textFieldCostSum.getText().isEmpty() || textFieldDescription.getText().isEmpty()
                    || textFieldCurrency.getText().isEmpty()  ||  Character.isLetter(textFieldCostSum.getText().charAt(0))) {
                JOptionPane.showMessageDialog(frameMain, "Invalid input");

                return false;
            }
            return true;
        }
        public void setCategoriesJComboBox(ArrayList<Category> categories)
        {
            if (!categories.isEmpty())
            {
                categories.forEach(category -> categoriesJComboBox.addItem(category.getCategoryName()));
            }
        }
    }

    public static class LoginView extends  JDialog
    {
       /* private JTextField tfUsername;
        private JPasswordField pfPassword;
        private JLabel lbUsername;
        private JLabel lbPassword;
        private JButton btnLogin;
        private JButton btnCancel;
        private boolean succeeded;

        public LoginView(Frame parent) {
            super(parent, "Login", true);
            //
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints cs = new GridBagConstraints();

            cs.fill = GridBagConstraints.HORIZONTAL;

            lbUsername = new JLabel("Username: ");
            cs.gridx = 0;
            cs.gridy = 0;
            cs.gridwidth = 1;
            panel.add(lbUsername, cs);

            tfUsername = new JTextField(20);
            cs.gridx = 1;
            cs.gridy = 0;
            cs.gridwidth = 2;
            panel.add(tfUsername, cs);

            lbPassword = new JLabel("Password: ");
            cs.gridx = 0;
            cs.gridy = 1;
            cs.gridwidth = 1;
            panel.add(lbPassword, cs);

            pfPassword = new JPasswordField(20);
            cs.gridx = 1;
            cs.gridy = 1;
            cs.gridwidth = 2;
            panel.add(pfPassword, cs);
            panel.setBorder(new LineBorder(Color.GRAY));

            btnLogin = new JButton("Login");

            btnLogin.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (true) {
                        JOptionPane.showMessageDialog(LoginView.this,
                                "Hi " + getUsername() + "! You have successfully logged in.",
                                "Login",
                                JOptionPane.INFORMATION_MESSAGE);
                        succeeded = true;
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(LoginView.this,
                                "Invalid username or password",
                                "Login",
                                JOptionPane.ERROR_MESSAGE);
                        // reset username and password
                        tfUsername.setText("");
                        pfPassword.setText("");
                        succeeded = false;

                    }
                }
            });
            btnCancel = new JButton("Cancel");
            btnCancel.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            JPanel bp = new JPanel();
            bp.add(btnLogin);
            bp.add(btnCancel);

            getContentPane().add(panel, BorderLayout.CENTER);
            getContentPane().add(bp, BorderLayout.PAGE_END);

            pack();
            setResizable(false);
            setLocationRelativeTo(parent);
        }

        public String getUsername() {
            return tfUsername.getText().trim();
        }

        public String getPassword() {
            return new String(pfPassword.getPassword());
        }

        public boolean isSucceeded() {
            return succeeded;
        }*/
        String userName;

        public String getUserName()
        {
            return userNameTextField.getText();
        }

        private  JFrame loginFrame;
        private JTextField userNameTextField;
        private JPanel mainPanel,contentPanel;
        private JButton loginButton;
        boolean succeeded;

        public LoginView()
        {
            this.setModal(true);
//            super(frameMain ,true);

            init();
            start();
        }
        private void init()
        {
            loginFrame = new JFrame("Login");
            userNameTextField = new JTextField();
            mainPanel = new JPanel();
            contentPanel = new JPanel();
            loginButton = new JButton("Login");
//            loginButton.addActionListener(e ->submitFunction());
            loginButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    if (!userNameTextField.getText().isEmpty())
                    {
                        succeeded = true;
                    }
                    loginFrame.dispose();

                }
            });

        }

        public boolean isSucceeded()
        {
            return succeeded;
        }
        //        private ArrayList<String> submitFunction()
//        {
//            userName = userNameTextField.getText();
//            password = passwordTextField.getText();
//            ArrayList<String> userData = new ArrayList<>(2);
//            userData.add(userName);
//            userData.add(password);
//            JOptionPane.showMessageDialog(null,"hi" + userName,"login",JOptionPane.INFORMATION_MESSAGE);
//
//            loginFrame.dispose();
//            return userData;
//
//        }

        private void start()
        {
            mainPanel.setLayout(new BorderLayout());
            contentPanel.setLayout(new GridLayout(0,1));
            contentPanel.setSize(100,100);

            contentPanel.add(new JLabel("User Name"));
            contentPanel.add(userNameTextField);
            contentPanel.add(new JLabel("Password"));
            contentPanel.add(loginButton);
            mainPanel.add(contentPanel,BorderLayout.CENTER);
            loginFrame.setResizable(false);
//            setLocationRelativeTo(this);
//            mainPanel.add(loginButton,BorderLayout.SOUTH);
            loginFrame.add(mainPanel);

            loginFrame.setSize(400, 400);
            loginFrame.setVisible(true);


        }

    }

}


