import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

//Displaying Images by using Canvas
class Photo extends Canvas {
    String name;

    // Image is in images folder with .jpeg extension ,passing the image name into
    // the constructor
    Photo(String name) {
        this.name = name;
    }

    public void paint(Graphics g) {
        Image image = Toolkit.getDefaultToolkit().getImage("images/" + this.name + ".jpeg");
        g.drawImage(image, 0, 0, this);
    }
}

// Class containing the information about the order placed
class MyOrder {
    String foodName;
    int foodPrice, quantity;

    MyOrder(String foodName, int foodPrice, int quantity) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.quantity = quantity;
    }
}

// Class containing a static Dynamic List of type Class MyOrder which contains
// all the items of the order
class SelectedFoodItems {
    public static List<MyOrder> MY_ORDER = new ArrayList<>();
    //returning the total cost
    public static int TOTAL_AMOUNT(){
        int total=0;
        for(MyOrder myOrder : MY_ORDER){
            total+=myOrder.foodPrice*myOrder.quantity;
        }
        return total;
    }
    // REMOVE_ORDER is a static function used to remove the item from the order when
    // Food Name is passed into it.
    public static void REMOVE_ORDER(String foodName) {
        for (MyOrder myOrder : MY_ORDER) {
            if (myOrder.foodName.equalsIgnoreCase(foodName)) {
                MY_ORDER.remove(myOrder);
                break;
            }
        }
    }
}

// Creating a Class FoodItem which is a container class containing
// Photo,Label,Checkbox,Labels
class FoodItem extends Panel implements ItemListener, ActionListener {
    static Font font = new Font("Monaco", Font.BOLD, 21);
    static Font smallerFont = new Font("Monaco", Font.BOLD, 15);
    static Cursor hand = new Cursor(Cursor.HAND_CURSOR);

    // Passing image path and its price to the constructor
    FoodItem(String path, String price) {
        Checkbox addItem = new Checkbox();
        Label foodName = new Label(path.toUpperCase());
        Label foodPrice = new Label(price + " ₹");
        Photo photo = new Photo(path);
        // This panel shows us the quantity of the food item and displays only when the
        // food item is selected
        Panel quantity = new Panel();
        Label quantityValue = new Label("1");
        Button increment = new Button("⌃");
        Button decrement = new Button("⌄");
        // Setting styles for the quantity
        quantityValue.setBounds(10, 10, 25, 22);
        quantityValue.setAlignment(Label.CENTER);
        quantityValue.setFont(smallerFont);
        quantityValue.setBackground(Color.WHITE);
        quantity.add(quantityValue);
        // Increment button to increase the quantity
        increment.setBounds(45, 2, 20, 18);
        increment.setFont(smallerFont);
        increment.setCursor(hand);
        increment.addActionListener(this);
        // Decrement button to decrease the quantity
        decrement.setBounds(45, 20, 20, 18);
        decrement.setFont(smallerFont);
        decrement.setCursor(hand);
        decrement.addActionListener(this);

        quantity.add(increment);
        quantity.add(decrement);
        quantity.setBounds(215, 50, 70, 40);
        quantity.setLayout(null);
        quantity.setBackground(Color.GRAY);
        quantity.setVisible(false);
        add(quantity);
        // Adding Price label to our panel
        foodPrice.setFont(new Font("monospace", Font.BOLD, 25));
        foodPrice.setForeground(new Color(99, 64, 17));
        foodPrice.setBounds(215, 100, 75, 50);
        foodPrice.setAlignment(Label.CENTER);
        foodPrice.setBackground(Color.LIGHT_GRAY);
        // Adding Checkbox
        addItem.addItemListener(this);
        addItem.setBounds(5, 5, 25, 25);
        addItem.setCursor(hand);
        // Adding Food Name
        foodName.setFont(font);
        foodName.setBounds(40, 5, 230, 30);
        foodName.setBackground(new Color(230, 230, 230));

        add(foodName);
        add(photo);
        add(addItem);
        add(foodPrice);
        setSize(300, 200);
        setLayout(null);
        setBackground(new Color(220, 220, 220));
        // Setting the size and position of the image
        photo.setBounds(5, 45, 200, 200);
    }

    @Override
    // This itemStateChanged function will be invoked when the user clicks on the
    // checkbox
    public void itemStateChanged(ItemEvent e) {
        // Getting the all the components in the panel
        Component[] children = this.getComponents();
        // TypeCasting the Food Name into a label
        Label foodName = (Label) children[1];
        // TypeCasting the quantity panel into a panel
        Panel panel = (Panel) children[0];
        // When the checkbox is checked
        if (e.getStateChange() == ItemEvent.SELECTED) {
            // Display the quantity panel
            panel.setVisible(true);
            // TypeCasting food price into a label
            Label foodPrice = (Label) children[4];
            String temp = foodPrice.getText();
            // Extracting price from the label
            int price = Integer.parseInt(temp.substring(0, temp.indexOf(" ")));
            // Adding the order to the static MY_ORDER List
            SelectedFoodItems.MY_ORDER.add(new MyOrder(foodName.getText(), price, 1));
        }
        // When the Checkbox is unchecked
        else if (e.getStateChange() == ItemEvent.DESELECTED) {
            // Not displaying the quantity panel
            panel.setVisible(false);
            // Removing the item from the List
            SelectedFoodItems.REMOVE_ORDER(foodName.getText());
        }
    }

    // When ' ⌃' or ' ⌄ ' button of the quantity panel is clicked
    @Override
    public void actionPerformed(ActionEvent e) {
        // Getting the children elements of the Parent panel
        Component[] children = this.getComponents();
        // TypeCasting the quantity panel to panel
        Panel panel = (Panel) children[0];
        // TypeCasting the Food Name into label
        Label foodName = (Label) children[1];
        // Getting the text from the foodName label
        String food = foodName.getText();
        // Getting the children elements of the quantity panel
        Component[] panelChildren = panel.getComponents();
        // Getting the value of the quantity
        Label value = (Label) panelChildren[0];
        // TypeCasting the event source to button
        Button button = (Button) e.getSource();
        // Converting string quantity value to integer value
        int qty = Integer.parseInt(value.getText());
        // Incrementing the quantity value
        if (button.getLabel() == "⌃" && qty < 10)
            value.setText(Integer.toString(qty + 1));
        // Decrementing the quantity value
        else if (button.getLabel() == "⌄" && qty > 1)
            value.setText(Integer.toString(qty - 1));
        // Updating the quantity value in MY_ORDER List using forEach loop iterating in
        // the list and finding the order with the Food Name
        for (MyOrder order : SelectedFoodItems.MY_ORDER) {
            if (order.foodName.equalsIgnoreCase(food)) {
                order.quantity = Integer.parseInt(value.getText());
                break;
            }
        }
    }
}

// Class for rows in the bill
class BillRow extends Panel {
    static Font font = new Font("Monaco", Font.ITALIC, 17);
    static int netPayableAmount = 0;

    // Constructor Overloading
    // When no parameters are passed this indicates that we want to create an
    // instance of Header in the Bill
    BillRow() {
        Font headerFont = new Font("Monaco", Font.BOLD, 17);
        Label[] labels = new Label[5];
        labels[0] = new Label("S.no");
        labels[1] = new Label("Food Item");
        labels[2] = new Label("Price");
        labels[3] = new Label("Quantity");
        labels[4] = new Label("Total Cost");
        // Using setLabels adding styles and adding it to the panel
        setLabels(labels, headerFont, 0);
    }

    // Constructor Overloading
    // Here we are passing data about the food item
    BillRow(int sNo, String foodName, int foodPrice, int quantity) {
        Label[] labels = new Label[5];
        labels[0] = new Label(Integer.toString(sNo));
        labels[1] = new Label(foodName);
        labels[2] = new Label(Integer.toString(foodPrice));
        labels[3] = new Label(Integer.toString(quantity));
        labels[4] = new Label(Integer.toString(foodPrice * quantity));
        // Updating the total amount to be paid
        setLabels(labels, font, sNo);
    }

    // Constructor Overloading
    // Here we are creating last row to displaying the total amount be paid
    BillRow(int sNo) {
        Label amountToBePaid = new Label("Total Amount");
        Label totalCost = new Label(Integer.toString(SelectedFoodItems.TOTAL_AMOUNT()) + " ₹");
        amountToBePaid.setBounds(5, 5, 400, 35);
        totalCost.setBounds(410, 5, 165, 35);
        amountToBePaid.setFont(font);
        amountToBePaid.setAlignment(Label.CENTER);
        amountToBePaid.setBackground(Color.LIGHT_GRAY);
        add(amountToBePaid);
        totalCost.setFont(font);
        totalCost.setAlignment(Label.CENTER);
        totalCost.setBackground(Color.LIGHT_GRAY);
        add(totalCost);
        setBounds(5, 105 + sNo * 30, 580, 45);
        setLayout(null);
        setBackground(Color.DARK_GRAY);
    }

    void setLabels(Label[] labels, Font font, int sNo) {
        Color color = new Color(230, 230, 230);
        labels[0].setBounds(5, 5, 50, 25);
        labels[1].setBounds(60, 5, 200, 25);
        labels[2].setBounds(265, 5, 90, 25);
        labels[3].setBounds(360, 5, 90, 25);
        labels[4].setBounds(455, 5, 120, 25);
        for (Label label : labels) {
            label.setFont(font);
            label.setAlignment(Label.CENTER);
            if (sNo > 0)
                label.setBackground(color);
            else
                label.setBackground(Color.LIGHT_GRAY);
            add(label);
        }
        setBounds(5, 105 + sNo * 30, 580, 35);
        setLayout(null);
        setBackground(Color.DARK_GRAY);
    }
}

public class Main {
    static Frame frame;
    static Panel panel;
    // Getting the screen size
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    // Adding the food items to the main frame
    static void addFoodItems(List<FoodItem> foodItems) {
        for (FoodItem foodItem : foodItems) {
            frame.add(foodItem);
        }
    }

    // Adding the food items to the Dynamic List of type 'FoodItem'
    static void addFoodItemsToList(List<FoodItem> foodItemsList, String[][] foodItemData) {
        for (int i = 0; i < foodItemData[0].length; i++) {
            foodItemsList.add(new FoodItem(foodItemData[0][i], foodItemData[1][i]));
        }
    }

    public static void main(String[] args) {
        // Setting the details of the frame
        frame = new Frame("ISTHARA RESTAURANT");
        frame.setLayout(new BorderLayout());
        Label welcome = new Label("       WELCOME TO INDIAN RESTAURANT");
        welcome.setFont(new Font("Monaco", Font.BOLD, 50));
        welcome.setBackground(new Color(255, 251, 145));
        frame.add(welcome, BorderLayout.CENTER);

        // Dynamic List of type FoodItem Class which contains the food item objects in
        // the List
        List<FoodItem> tiffins = new ArrayList<>();
        List<FoodItem> meals = new ArrayList<>();
        List<FoodItem> curries = new ArrayList<>();
        List<FoodItem> desserts = new ArrayList<>();
        List<FoodItem> drinks = new ArrayList<>();

        // 2-D array of strings containing food name and price
        String[][] tiffinsData = {
                { "Idli", "Dosa", "Vada", "Paratha", "Poha", "Upma", "Uttapam",
                        "Pesarattu", "Oats", "Vermicelli Upma", "Dhokla", "Aloo Puri" },
                { "20", "35", "30", "20", "30", "20", "25", "40", "40", "45", "35", "30", "40" }
        };

        String[][] mealsData = { { "Veg Thali", "Plain Rice", "Fried Rice", "Aloo Paratha", "Paneer Paratha",
                "Curd Rice", "Naan", "Rumaali Roti", "Tandoor Roti", "Veg Biryani", "Pulihora", "Pakodi" },
                { "120", "30", "75", "25", "35", "15", "20", "25", "20", "150", "30", "30" } };

        String[][] curriesData = {
                { "Aloo Curry", "Bendakay Fry", "Chole", "Kaju Masala", "Paneer Tikka", "Sambar", "Rasam",
                        "Palak Paneer", "Baingan Bharta", "Rajma Masala", "Malai Kofta", "Chana Masala" },
                { "35", "30", "50", "60", "75", "15", "10", "60", "40", "50", "70", "40" } };

        String[][] dessertsData = { { "Double Ka Meetha", "Gulab Jamun", "Jalebi", "Kaja", "Malai Kulfi",
                "Mango Ice-cream", "Rasmalai", "Vanilla Ice-cream", "Besan Laddu", "Gajar Ka Halwa", "Rasgulla",
                "Kaju Barfi" },
                { "35", "30", "20", "20", "35", "40", "50", "30", "30", "35", "25", "40" } };

        String[][] drinksData = { {
                "Badam Milk", "Coffee", "Falooda", "Green Tea", "Hot Chocolate", "Lassi", "Lemon Soda", "Mango Juice",
                "Masala Chai",
                "MilkShake", "Sugarcane Juice", "Aam Panna"
        }, { "30", "20", "70", "25", "35", "25", "15", "35", "20", "45", "20", "25" } };
        // Adding the data from the 2-D array to Dynamic List of type FoodItem Class
        addFoodItemsToList(tiffins, tiffinsData);
        addFoodItemsToList(meals, mealsData);
        addFoodItemsToList(curries, curriesData);
        addFoodItemsToList(desserts, dessertsData);
        addFoodItemsToList(drinks, drinksData);
        // Creating a MenuBar
        MenuBar menuBar = new MenuBar();
        // Menu to display the food items
        Menu foodItemsMenu = new Menu("FOOD MENU");
        // Menu to display the bill
        Menu show = new Menu("SHOW");
        // Adding Menu Item to show
        MenuItem showOrder = new MenuItem("Show my Order");
        show.add(showOrder);
        // Adding Menu Items to FoodItemsMenu
        MenuItem[] foodItems = {
                new MenuItem("Tiffins"),
                new MenuItem("Meals"),
                new MenuItem("Curries"),
                new MenuItem("Desserts"),
                new MenuItem("Drinks")
        };

        menuBar.add(foodItemsMenu);
        menuBar.add(show);
        // Adding MenuBar to the Frame
        frame.setMenuBar(menuBar);
        // Adding actionListener to the MenuItem
        for (MenuItem item : foodItems) {
            foodItemsMenu.add(item);
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Setting the frame layout to FlowLayout.CENTER
                    frame.setLayout(new FlowLayout(FlowLayout.CENTER));
                    // TypeCasting the event source to Menu Item
                    MenuItem menu = (MenuItem) e.getSource();
                    // Getting the name from the source
                    String currentItem = menu.getLabel();
                    // Removing all items from the frame and displaying the food items based on the
                    // currentItem
                    frame.removeAll();
                    switch (currentItem) {
                        case "Tiffins":
                            addFoodItems(tiffins);
                            break;
                        case "Meals":
                            addFoodItems(meals);
                            break;
                        case "Curries":
                            addFoodItems(curries);
                            break;
                        case "Desserts":
                            addFoodItems(desserts);
                            break;
                        case "Drinks":
                            addFoodItems(drinks);
                            break;
                    }
                    // Re-rendering the frame to reflect the changes
                    frame.revalidate();
                    frame.repaint();
                }
            });
        }

        // Adding actionListener to show order menu item
        showOrder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int i = 1;
                // Creating new Bill Frame
                Frame billingFrame = new Frame("BILL");
                // Adding Header to the bill
                BillRow header = new BillRow();
                billingFrame.add(header);
                // Adding each food item in the static MY_ORDER List
                for (MyOrder order : SelectedFoodItems.MY_ORDER) {
                    billingFrame.add(new BillRow(i, order.foodName, order.foodPrice, order.quantity));
                    i++;
                }
                // Adding the Total Amount Row at the last
                billingFrame.add(new BillRow(i));
                billingFrame.setLayout(null);
                billingFrame.setBounds(100, 100, 600, 700);
                billingFrame.setVisible(true);
                // When user clicks the exit button on the Bill Frame
                billingFrame.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        // Removes the bill frame
                        billingFrame.dispose();
                    }
                });
            }
        });
        // Setting the background color of the frame (rgb value)
        frame.setBackground(new Color(150, 150, 150));
        frame.setSize(screenSize);
        frame.setVisible(true);
        // Closing the frame when user clicks the exit button on the main frame
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
