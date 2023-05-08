import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

//Displaying Images by using Canvas
class Photo extends Canvas {
    String name;

    Photo(String name) {
        this.name = name;
    }

    public void paint(Graphics g) {
        Image image = Toolkit.getDefaultToolkit().getImage("images/" + this.name + ".jpeg");
        g.drawImage(image, 0, 0, this);
    }
}

class MyOrder {
    String foodName;
    int foodPrice, quantity;

    MyOrder(String foodName, int foodPrice, int quantity) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.quantity = quantity;
    }
}

class SelectedFoodItems {
    public static List<MyOrder> MY_ORDER = new ArrayList<>();

    public static void REMOVE_ORDER(String foodName) {
        for (MyOrder myOrder : MY_ORDER) {
            if (myOrder.foodName.equalsIgnoreCase(foodName)) {
                MY_ORDER.remove(myOrder);
                break;
            }
        }
    }
}

class FoodItem extends Panel implements ItemListener, ActionListener {
    static Font font = new Font("Monaco", Font.BOLD, 21);
    static Font smallerFont = new Font("Monaco", Font.BOLD, 15);
    static Cursor hand = new Cursor(Cursor.HAND_CURSOR);

    FoodItem(String path, String price) {
        Checkbox addItem = new Checkbox();
        Label foodName = new Label(path.toUpperCase());
        Label foodPrice = new Label(price + " ₹");
        Photo photo = new Photo(path);
        Panel quantity = new Panel();
        Label quantityValue = new Label("1");
        Button increment = new Button("⌃");
        Button decrement = new Button("⌄");
        quantityValue.setBounds(10, 10, 25, 22);
        quantityValue.setAlignment(Label.CENTER);
        quantityValue.setFont(smallerFont);
        quantityValue.setBackground(Color.WHITE);
        quantity.add(quantityValue);
        increment.setBounds(45, 2, 20, 18);
        increment.setFont(smallerFont);
        increment.setCursor(hand);
        increment.addActionListener(this);
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
        foodPrice.setFont(new Font("monospace", Font.BOLD, 25));
        foodPrice.setForeground(new Color(99, 64, 17));
        foodName.setFont(font);
        addItem.addItemListener(this);
        addItem.setBounds(5, 5, 25, 25);
        addItem.setCursor(hand);
        foodName.setBounds(40, 5, 230, 30);
        foodName.setBackground(new Color(230, 230, 230));
        foodPrice.setBounds(215, 100, 75, 50);
        foodPrice.setAlignment(Label.CENTER);
        foodPrice.setBackground(Color.LIGHT_GRAY);
        add(foodName);
        add(photo);
        add(addItem);
        add(foodPrice);
        setSize(300, 200);
        setLayout(null);
        setBackground(new Color(220, 220, 220));
        photo.setBounds(5, 45, 200, 200);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Component[] children = this.getComponents();
        Label foodName = (Label) children[1];
        Panel panel = (Panel) children[0];
        if (e.getStateChange() == ItemEvent.SELECTED) {
            panel.setVisible(true);
            Label foodPrice = (Label) children[4];
            String temp = foodPrice.getText();
            int price = Integer.parseInt(temp.substring(0, temp.indexOf(" ")));
            SelectedFoodItems.MY_ORDER.add(new MyOrder(foodName.getText(), price, 1));
        } else if (e.getStateChange() == ItemEvent.DESELECTED) {
            panel.setVisible(false);
            SelectedFoodItems.REMOVE_ORDER(foodName.getText());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Component[] children = this.getComponents();
        Panel panel = (Panel) children[0];
        Label foodName = (Label) children[1];
        String food = foodName.getText();
        Component[] panelChildren = panel.getComponents();
        Label value = (Label) panelChildren[0];
        Button button = (Button) e.getSource();
        int qty = Integer.parseInt(value.getText());
        if (button.getLabel() == "⌃" && qty < 10)
            value.setText(Integer.toString(qty + 1));
        else if (button.getLabel() == "⌄" && qty > 1)
            value.setText(Integer.toString(qty - 1));
        for (MyOrder order : SelectedFoodItems.MY_ORDER) {
            if (order.foodName.equalsIgnoreCase(food)) {
                order.quantity = Integer.parseInt(value.getText());
                System.out.println(order.quantity);
                break;
            }
        }
    }
}

class BillRow extends Panel {
    static Font font = new Font("Monaco", Font.ITALIC, 17);
    static int netPayableAmount = 0;

    BillRow() {
        Font headerFont = new Font("Monaco", Font.BOLD, 17);
        Label[] labels = new Label[5];
        labels[0] = new Label("S.no");
        labels[1] = new Label("Food Item");
        labels[2] = new Label("Price");
        labels[3] = new Label("Quantity");
        labels[4] = new Label("Total Cost");
        setLabels(labels, headerFont, 0);
    }

    BillRow(int sNo, String foodName, int foodPrice, int quantity) {
        Label[] labels = new Label[5];
        labels[0] = new Label(Integer.toString(sNo));
        labels[1] = new Label(foodName);
        labels[2] = new Label(Integer.toString(foodPrice));
        labels[3] = new Label(Integer.toString(quantity));
        labels[4] = new Label(Integer.toString(foodPrice * quantity));
        netPayableAmount += foodPrice * quantity;
        setLabels(labels, font, sNo);
    }

    BillRow(int sNo) {
        Label amountToBePaid = new Label("Total Amount");
        Label totalCost = new Label(Integer.toString(netPayableAmount) + " ₹");
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
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    static void addFoodItems(List<FoodItem> foodItems) {
        for (FoodItem foodItem : foodItems) {
            frame.add(foodItem);
        }
    }

    static void addFoodItemsToList(List<FoodItem> foodItemsList, String[][] foodItemData) {
        for (int i = 0; i < foodItemData[0].length; i++) {
            foodItemsList.add(new FoodItem(foodItemData[0][i], foodItemData[1][i]));
        }
    }

    public static void main(String[] args) {
        frame = new Frame("ISTHARA RESTAURANT");
        frame.setLayout(new BorderLayout());
        Label welcome=new Label("       WELCOME TO ISTHARA RESTAURANT");
        welcome.setFont(new Font("Monaco",Font.BOLD,50));
        welcome.setBackground(new Color(255, 251, 145));
        frame.add(welcome,BorderLayout.CENTER);
        List<FoodItem> tiffins = new ArrayList<>();
        List<FoodItem> meals = new ArrayList<>();
        List<FoodItem> curries = new ArrayList<>();
        List<FoodItem> desserts = new ArrayList<>();
        List<FoodItem> drinks= new ArrayList<>();
        String[][] tiffinsData = {
                { "Idli", "Dosa", "Vada", "Paratha", "Poha", "Upma", "Uttapam",
                        "Pesarattu", "Oats","Vermicelli Upma","Dhokla","Aloo Puri" },
                { "20", "35", "30", "20", "30", "20", "25", "40", "40", "45","35","30","40" }
        };
        String[][] mealsData = { { "Veg Thali", "Plain Rice", "Fried Rice", "Aloo Paratha", "Paneer Paratha",
                "Curd Rice", "Naan", "Rumaali Roti", "Tandoor Roti","Veg Biryani","Pulihora","Pakodi" },
                { "120", "30", "75", "25", "35", "15", "20", "25", "20","150","30","30" } };
        String[][] curriesData = {
                { "Aloo Curry", "Bendakay Fry", "Chole", "Kaju Masala", "Paneer Tikka", "Sambar", "Rasam","Palak Paneer","Baingan Bharta","Rajma Masala","Malai Kofta","Chana Masala" },
                { "35", "30", "50", "60", "75", "15", "10","60","40","50","70","40" } };
        String[][] dessertsData = { { "Double Ka Meetha", "Gulab Jamun", "Jalebi", "Kaja", "Malai Kulfi",
                "Mango Ice-cream", "Rasmalai", "Vanilla Ice-cream","Besan Laddu","Gajar Ka Halwa","Rasgulla","Kaju Barfi" },
                { "35", "30", "20", "20", "35", "40", "50", "30","30","35","25","40" } };
        String[][] drinksData={{
            "Badam Milk","Coffee","Falooda","Green Tea","Hot Chocolate","Lassi","Lemon Soda","Mango Juice","Masala Chai",
            "MilkShake","Sugarcane Juice","Aam Panna"
        },{"30","20","70","25","35","25","15","35","20","45","20","25"}};
        addFoodItemsToList(tiffins, tiffinsData);
        addFoodItemsToList(meals, mealsData);
        addFoodItemsToList(curries, curriesData);
        addFoodItemsToList(desserts, dessertsData);
        addFoodItemsToList(drinks, drinksData);
        MenuBar menuBar = new MenuBar();
        Menu foodItemsMenu = new Menu("FOOD MENU");
        Menu show = new Menu("SHOW");
        MenuItem showOrder = new MenuItem("Show my Order");
        show.add(showOrder);
        MenuItem[] foodItems = {
                new MenuItem("Tiffins"),
                new MenuItem("Meals"),
                new MenuItem("Curries"),
                new MenuItem("Desserts"),
                new MenuItem("Drinks")
        };

        menuBar.add(foodItemsMenu);
        menuBar.add(show);
        frame.setMenuBar(menuBar);
        for (MenuItem item : foodItems) {
            foodItemsMenu.add(item);
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    frame.setLayout(new FlowLayout(FlowLayout.CENTER));
                    MenuItem menu = (MenuItem) e.getSource();
                    String currentItem = menu.getLabel();
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
                    frame.revalidate();
                    frame.repaint();
                }
            });
        }

        showOrder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int i = 1;
                Frame billingFrame = new Frame("BILL");
                billingFrame.add(new BillRow());
                for (MyOrder order : SelectedFoodItems.MY_ORDER) {
                    billingFrame.add(new BillRow(i, order.foodName, order.foodPrice, order.quantity));
                    i++;
                }
                billingFrame.add(new BillRow(i));
                billingFrame.setLayout(null);
                billingFrame.setBounds(100, 100, 600, 700);
                billingFrame.setVisible(true);
                billingFrame.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        billingFrame.dispose();
                    }
                });
            }
        });
        frame.setBackground(new Color(100, 100, 100));
        frame.setSize(screenSize);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
