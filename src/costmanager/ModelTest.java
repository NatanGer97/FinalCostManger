package costmanager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModelTest
{
    static final String userName = "UserForTestingModel11";
    static ICostManagerModel model;
    @BeforeAll
    static void init(){
        try{
            model = new CostManagerModel();
            model.connectUser(userName);
        }
        catch (CostManagerException e){
            e.printStackTrace();
        }
    }


    @Test
    void addCost(){
        try{
            CostItem costItem = new CostItem(userName, 150, "fuel ", "General", "ISL");
            int oldSize = model.getAllCosts(costItem.getUserName()).size();
            model.addCost(costItem);
            assertEquals(model.getAllCosts(costItem.getUserName()).size(), oldSize + 1);
        }

        catch (CostManagerException e){
            e.printStackTrace();
        }
    }

    @Test
    void removeCost(){
        try{
            ArrayList<CostItem> costItems = (ArrayList<CostItem>) model.getAllCosts(userName);
            System.out.println(costItems.size());
            int oldSize = costItems.size();
            model.removeCost(costItems.get(0));
            assertEquals(oldSize - 1, model.getAllCosts(userName).size());
        }
        catch (CostManagerException e){
            e.printStackTrace();
        }
    }

    @Test
    void addCategory(){

        try{
            Category categoryItem = new Category("CategoryToTestFinal");
            int oldSize = model.getAllCategories(userName).size();
            model.addCategory(categoryItem, userName);
            assertEquals(oldSize + 1, model.getAllCategories(userName).size());
        }
        catch (CostManagerException e){
            e.printStackTrace();
        }
    }
}