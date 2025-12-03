
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * View: JavaFX multi-window GUI. Uses controller to perform actions.
 */
public class GroceryView {
    private final GroceryController controller;
    private final GroceryModel model;
    private Stage primaryStage;

    // Main UI containers and controls
    private final ListView<String> inventoryList = new ListView<>();
    private final ListView<String> cartList = new ListView<>();
    private final Label totalLabel = new Label("Total: $0.00");
    private final Label pointsLabel = new Label("Points: 0");
    private final TextField customerNameField = new TextField();

    public GroceryView(GroceryController controller, GroceryModel model) {
        this.controller = controller;
        this.model = model;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public Scene getMainScene() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Top: Header with customer name and points
        HBox top = new HBox(10);
        top.setAlignment(Pos.CENTER_LEFT);
        Label nameLabel = new Label("Customer Name:");
        customerNameField.setPrefColumnCount(12);
        Button setNameBtn = new Button("Set Name");
        // anonymous handler (no switch)
        setNameBtn.setOnAction(e -> {
            String name = customerNameField.getText();
            if (name != null && name.trim().length() > 0) {
                controller.setCustomerName(name.trim());
                refreshAll();
            } else {
                alert("Enter a valid name.");
            }
        });
        top.getChildren().addAll(nameLabel, customerNameField, setNameBtn, new Separator());
        top.getChildren().add(pointsLabel);

        // Left: inventory and actions
        VBox left = new VBox(10);
        left.setPrefWidth(330);
        left.setPadding(new Insets(5));
        left.setStyle("-fx-border-color: #ccc; -fx-border-width: 1px;");
        Label invLabel = new Label("Inventory");
        inventoryList.setPrefHeight(300);
        Button refreshInvBtn = new Button("Refresh Inventory");
        refreshInvBtn.setOnAction(e -> refreshInventoryList());

        // Add to cart controls
        HBox addBox = new HBox(5);
        TextField addName = new TextField();
        addName.setPromptText("Item name");
        TextField addQty = new TextField();
        addQty.setPromptText("Qty");
        Button addToCartBtn = new Button("Add to Cart");
        addToCartBtn.setOnAction(e -> {
            String name = addName.getText();
            int qty = safeParseInt(addQty.getText());
            if (name == null || name.trim().isEmpty() || qty < 1) {
                alert("Enter item name and valid quantity (>=1).");
                return;
            }
            boolean ok = controller.addToCart(name.trim(), qty);
            if (!ok) {
                alert("Unable to add item to cart (check stock and name).");
            }
            refreshAll();
        });
        addBox.getChildren().addAll(addName, addQty, addToCartBtn);

        // Inventory admin: Add new item, remove item, restock
        HBox adminRow = new HBox(5);
        Button newItemBtn = new Button("New Item");
        newItemBtn.setOnAction(e -> openNewItemWindow());
        Button removeItemBtn = new Button("Remove Item");
        removeItemBtn.setOnAction(e -> {
            String sel = null;
            if (!inventoryList.getSelectionModel().isEmpty()) {
                sel = inventoryList.getSelectionModel().getSelectedItem();
            }
            if (sel == null) {
                alert("Select an inventory line to remove.");
                return;
            }
            // parse item name from line (format: Name | cat | $price | qty)
            String name = parseNameFromInventoryLine(sel);
            if (name != null) {
                boolean ok = controller.removeItemFromInventory(name);
                if (!ok) alert("Could not remove item.");
                refreshAll();
            }
        });

        Button restockBtn = new Button("Restock");
        restockBtn.setOnAction(e -> {
            String sel = null;
            if (!inventoryList.getSelectionModel().isEmpty()) {
                sel = inventoryList.getSelectionModel().getSelectedItem();
            }
            if (sel == null) {
                alert("Select an inventory line to restock.");
                return;
            }
            String name = parseNameFromInventoryLine(sel);
            TextInputDialog d = new TextInputDialog("10");
            d.setTitle("Restock");
            d.setHeaderText("Enter quantity to add:");
            d.initOwner(primaryStage);
            d.showAndWait();
            String qtyStr = d.getEditor().getText();
            int q = safeParseInt(qtyStr);
            if (q > 0) {
                controller.restockItem(name, q);
                refreshAll();
            } else {
                alert("Enter valid quantity.");
            }
        });

        adminRow.getChildren().addAll(newItemBtn, removeItemBtn, restockBtn);

        left.getChildren().addAll(invLabel, inventoryList, refreshInvBtn, addBox, adminRow);

        // Right: cart and checkout
        VBox right = new VBox(10);
        right.setPadding(new Insets(5));
        right.setStyle("-fx-border-color: #ccc; -fx-border-width: 1px;");
        Label cartLabel = new Label("Shopping Cart");
        cartList.setPrefHeight(240);
        HBox cartBtns = new HBox(5);
        Button removeFromCartBtn = new Button("Remove Selected");
        removeFromCartBtn.setOnAction(e -> {
            String sel = null;
            if (!cartList.getSelectionModel().isEmpty()) {
                sel = cartList.getSelectionModel().getSelectedItem();
            }
            if (sel == null) {
                alert("Select a cart line to remove.");
                return;
            }
            String name = parseNameFromCartLine(sel);
            boolean ok = controller.removeFromCart(name);
            if (!ok) {
                alert("Could not remove item from cart.");
            }
            refreshAll();
        });

        Button checkoutBtn = new Button("Checkout");
        checkoutBtn.setOnAction(e -> openCheckoutWindow());

        Button loyaltyBtn = new Button("Loyalty");
        loyaltyBtn.setOnAction(e -> openLoyaltyWindow());

        cartBtns.getChildren().addAll(removeFromCartBtn, checkoutBtn, loyaltyBtn);

        right.getChildren().addAll(cartLabel, cartList, totalLabel, cartBtns);

        // Layout assembly
        root.setTop(top);
        HBox center = new HBox(10, left, right);
        root.setCenter(center);

        // initial refresh
        refreshAll();

        return new Scene(root, 720, 420);
    }

    /* ---------- Helper UI windows ---------- */

    private void openNewItemWindow() {
        Stage dialog = new Stage();
        dialog.initOwner(primaryStage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Add New Inventory Item");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(8);
        grid.setVgap(8);

        TextField nameF = new TextField();
        TextField priceF = new TextField();
        TextField qtyF = new TextField();
        TextField brandF = new TextField();
        ChoiceBox<String> categoryChoice = new ChoiceBox<>();
        for (Category c : Category.values()) {
            categoryChoice.getItems().add(c.name());
        }
        categoryChoice.getSelectionModel().selectFirst();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameF, 1, 0);
        grid.add(new Label("Price:"), 0, 1);
        grid.add(priceF, 1, 1);
        grid.add(new Label("Qty:"), 0, 2);
        grid.add(qtyF, 1, 2);
        grid.add(new Label("Brand (optional):"), 0, 3);
        grid.add(brandF, 1, 3);
        grid.add(new Label("Category:"), 0, 4);
        grid.add(categoryChoice, 1, 4);

        Button addBtn = new Button("Add");
        addBtn.setOnAction(e -> {
            String name = nameF.getText();
            double price = safeParseDouble(priceF.getText());
            int qty = safeParseInt(qtyF.getText());
            String brand = brandF.getText();
            String catName = categoryChoice.getValue();
            if (name == null || name.trim().isEmpty() || price <= 0 || qty < 0) {
                alert("Enter valid name, price (>0) and quantity (>=0).");
                return;
            }
            Category cat = Category.valueOf(catName);
            GroceryItem item;
            if (brand != null && brand.trim().length() > 0) {
                item = new PackagedItem(name.trim(), cat, price, qty, brand.trim());
            } else {
                item = new FreshItem(name.trim(), cat, price, qty, 7);
            }
            controller.addNewItemToInventory(item);
            dialog.close();
            refreshAll();
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(e -> dialog.close());

        HBox row = new HBox(10, addBtn, cancelBtn);
        row.setPadding(new Insets(10));
        grid.add(row, 1, 5);

        Scene scene = new Scene(grid, 380, 260);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void openCheckoutWindow() {
        Stage dialog = new Stage();
        dialog.initOwner(primaryStage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Checkout");

        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.CENTER_LEFT);

        Label subtotalLbl = new Label(String.format("Subtotal: $%.2f", model.getCart().getTotal()));
        Label pointsAvailable = new Label(String.format("Available points: %d", controller.getCustomerPoints()));

        // Payment method selection via radio buttons (no switch)
        ToggleGroup tg = new ToggleGroup();
        RadioButton cashRb = new RadioButton("Cash");
        cashRb.setToggleGroup(tg);
        cashRb.setSelected(true);
        RadioButton cardRb = new RadioButton("Card");
        cardRb.setToggleGroup(tg);

        // Points redemption
        TextField usePointsField = new TextField();
        usePointsField.setPromptText("Points to use (optional)");

        Button payBtn = new Button("Pay");
        payBtn.setOnAction(e -> {
            int usePoints = safeParseInt(usePointsField.getText());
            PaymentMethod pm = PaymentMethod.CASH;
            if (cardRb.isSelected()) pm = PaymentMethod.CARD;
            // perform checkout via controller
            String receipt = controller.checkout(pm, usePoints);
            // show receipt
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Receipt");
            a.setHeaderText("Purchase Complete");
            a.setContentText(receipt);
            a.showAndWait();
            dialog.close();
            refreshAll();
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(e -> dialog.close());

        HBox methods = new HBox(10, cashRb, cardRb);
        HBox ptsRow = new HBox(5, new Label("Use points:"), usePointsField);

        box.getChildren().addAll(subtotalLbl, pointsAvailable, methods, ptsRow, new HBox(10, payBtn, cancelBtn));

        Scene scene = new Scene(box, 380, 220);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void openLoyaltyWindow() {
        Stage dialog = new Stage();
        dialog.initOwner(primaryStage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Loyalty Dashboard");

        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.CENTER_LEFT);

        Label nameLbl = new Label("Customer: " + controller.getCustomerName());
        Label ptsLbl = new Label("Points: " + controller.getCustomerPoints());
        TextField addPts = new TextField();
        addPts.setPromptText("Add points (admin)");

        Button addBtn = new Button("Add Points");
        addBtn.setOnAction(e -> {
            int pts = safeParseInt(addPts.getText());
            if (pts <= 0) {
                alert("Enter points > 0.");
                return;
            }
            controller.addCustomerPoints(pts);
            ptsLbl.setText("Points: " + controller.getCustomerPoints());
            refreshAll();
        });

        TextField redeemField = new TextField();
        redeemField.setPromptText("Redeem points");
        Button redeemBtn = new Button("Redeem");
        redeemBtn.setOnAction(e -> {
            int pts = safeParseInt(redeemField.getText());
            if (pts <= 0) {
                alert("Enter points >0.");
                return;
            }
            boolean ok = controller.redeemCustomerPoints(pts);
            if (!ok) {
                alert("Not enough points.");
            } else {
                ptsLbl.setText("Points: " + controller.getCustomerPoints());
                refreshAll();
            }
        });

        box.getChildren().addAll(nameLbl, ptsLbl, new HBox(5, addPts, addBtn), new HBox(5, redeemField, redeemBtn));

        Scene s = new Scene(box, 360, 200);
        dialog.setScene(s);
        dialog.showAndWait();
    }

    /* ---------- Utilities ---------- */
    private void refreshAll() {
        refreshInventoryList();
        refreshCartList();
        totalLabel.setText(String.format("Total: $%.2f", model.getCart().getTotal()));
        pointsLabel.setText(String.format("Points: %d", controller.getCustomerPoints()));
    }

    private void refreshInventoryList() {
        inventoryList.getItems().clear();
        ArrayList<GroceryItem> items = controller.listAllItems();
        for (GroceryItem it : items) {
            String line = String.format("%s | %s | $%.2f | qty:%d", it.getName(), it.getCategory(), it.getPrice(), it.getQuantityInStock());
            inventoryList.getItems().add(line);
        }
    }

    private void refreshCartList() {
        cartList.getItems().clear();
        for (CartItem ci : controller.viewCartItems()) {
            String line = String.format("%s x%d - $%.2f", ci.getName(), ci.getQuantity(), ci.getSubTotal());
            cartList.getItems().add(line);
        }
    }

    private String parseNameFromInventoryLine(String line) {
        if (line == null) return null;
        // expected format: "Name | CATEGORY | $price | qty:N"
        int pipe = line.indexOf("|");
        if (pipe == -1) return null;
        return line.substring(0, pipe).trim();
    }

    private String parseNameFromCartLine(String line) {
        if (line == null) return null;
        // expected "Name xN - $X.XX"
        int x = line.indexOf(" x");
        if (x == -1) return null;
        return line.substring(0, x).trim();
    }

    private int safeParseInt(String s) {
        try {
            if (s == null) return 0;
            s = s.trim();
            if (s.length() == 0) return 0;
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }

    private double safeParseDouble(String s) {
        try {
            if (s == null) return 0.0;
            s = s.trim();
            if (s.length() == 0) return 0.0;
            return Double.parseDouble(s);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.initOwner(primaryStage);
        a.showAndWait();
    }
}
