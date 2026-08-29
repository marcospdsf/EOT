/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2026 - Stendhal                    *
 ***************************************************************************/
package games.stendhal.client.gui.market;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import games.stendhal.client.GameObjects;
import games.stendhal.client.entity.IEntity;
import games.stendhal.client.entity.NPC;
import games.stendhal.client.entity.User;
import games.stendhal.client.gui.DropTarget;
import games.stendhal.client.gui.WindowUtils;
import games.stendhal.client.gui.j2DClient;
import games.stendhal.client.gui.layout.SBoxLayout;
import games.stendhal.client.gui.market.HaroldMarketController.DeliveryCollectionItem;
import games.stendhal.client.gui.market.HaroldMarketController.MarketOfferItem;
import games.stendhal.client.gui.market.HaroldMarketController.MarketRequestItem;
import games.stendhal.client.gui.styled.Style;
import games.stendhal.client.gui.styled.StyleUtil;
import games.stendhal.client.sprite.Sprite;
import games.stendhal.client.sprite.SpriteStore;

/**
 * External floating Swing GUI for Harold's Trading Center ("Harold's offers"), featuring 4 tabs:
 * 1. Market (Sell Offers)
 * 2. My Offers (Selling & Manage Offers)
 * 3. My Requests (Buy Orders, Escrow, Delivery Claiming)
 * 4. All Requests (Order Book Fulfillment)
 */
public class HaroldMarketWindow extends JDialog {

	private static final long serialVersionUID = 1L;

	/**
	 * Resolves item sprite using Stendhal's standard Item resource format (class/subclass),
	 * exactly matching ItemListImageViewerEvent, Item2DView, and NpcShopWindow.
	 *
	 * @param classPath
	 *            Resource subpath in format "class/subclass" (e.g. "food/baguette")
	 * @param itemName
	 *            Item display name fallback
	 * @return Sprite or null
	 */
	public static Sprite getItemSprite(final String classPath, final String itemName) {
		final SpriteStore store = SpriteStore.get();

		// 1. If explicit classPath given (e.g. "food/baguette" or "data/sprites/items/food/baguette.png")
		if (classPath != null && !classPath.trim().isEmpty()) {
			String cp = classPath.trim().replace('\\', '/');
			if (cp.endsWith(".png")) {
				cp = cp.substring(0, cp.length() - 4);
			}
			if (cp.startsWith("/data/sprites/items/")) {
				cp = cp.substring("/data/sprites/items/".length());
			} else if (cp.startsWith("data/sprites/items/")) {
				cp = cp.substring("data/sprites/items/".length());
			}

			final String fullPath = "data/sprites/items/" + cp + ".png";
			final Sprite sprite = store.getSprite(fullPath);
			if (sprite != null && sprite != store.getFailsafe()) {
				if (sprite.getWidth() > sprite.getHeight()) {
					return store.getAnimatedSprite(sprite, 100);
				}
				return sprite;
			}
		}

		// 2. Lookup via ItemSearchDialog (finds direct spritePath like data/sprites/items/food/baguette.png for "bread")
		if (itemName != null && !itemName.trim().isEmpty()) {
			final String indexedPath = ItemSearchDialog.getSpritePath(itemName);
			if (indexedPath != null) {
				final Sprite sprite = store.getSprite(indexedPath);
				if (sprite != null && sprite != store.getFailsafe()) {
					if (sprite.getWidth() > sprite.getHeight()) {
						return store.getAnimatedSprite(sprite, 100);
					}
					return sprite;
				}
			}

			final String clean = itemName.toLowerCase().trim().replace(" ", "_");
			final String directRef = "data/sprites/items/" + clean + ".png";
			final Sprite directSprite = store.getSprite(directRef);
			if (directSprite != null && directSprite != store.getFailsafe()) {
				if (directSprite.getWidth() > directSprite.getHeight()) {
					return store.getAnimatedSprite(directSprite, 100);
				}
				return directSprite;
			}
		}

		return null;
	}

	public static Sprite getItemSprite(final String itemName) {
		return getItemSprite(null, itemName);
	}

	private final HaroldMarketController controller;

	private JTabbedPane tabbedPane;

	// Tab 1: Market
	private JComboBox<String> categoryComboBox;
	private JTable marketTable;
	private MarketTableModel marketTableModel;

	// Tab 2: My Offers
	private ItemDropBox offerDropBox;
	private JTextField offerItemInput;
	private JTextField offerQtyField;
	private JTextField offerPriceField;
	private JCheckBox offerSeparatelyCheckBox;
	private JLabel offerFeeLabel;
	private JButton sellButton;
	private JTable myOffersTable;
	private MyOffersTableModel myOffersTableModel;

	// Tab 3: My Requests
	private ItemDropBox reqDropBox;
	private JTextField reqItemInput;
	private JTextField reqQtyField;
	private JTextField reqPriceField;
	private JLabel reqSummaryLabel;
	private JButton placeReqButton;
	private JButton claimButton;
	private JTable myRequestsTable;
	private MyRequestsTableModel myRequestsTableModel;

	// Tab 4: All Requests
	private JComboBox<String> otherReqCatComboBox;
	private JTable otherRequestsTable;
	private OtherRequestsTableModel otherRequestsTableModel;

	// Styling colors
	private Color bgColor;
	private Color fgColor;
	private Color highlightColor;
	private Border borderDown;

	public HaroldMarketWindow(final HaroldMarketController controller) {
		super(j2DClient.get() != null ? j2DClient.get().getMainFrame() : null, "Harold's Trading Center - Semos Tavern", false);
		this.controller = controller;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		initStyle();
		initUI();

		WindowUtils.closeOnEscape(this);
		WindowUtils.trackLocation(this, "harold_offers", true);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				stopProximityCheck();
			}
			@Override
			public void windowClosed(WindowEvent e) {
				stopProximityCheck();
			}
		});
	}

	private javax.swing.Timer proximityTimer;

	private void startProximityCheck() {
		if (proximityTimer == null) {
			proximityTimer = new javax.swing.Timer(400, e -> checkProximityToHarold());
			proximityTimer.start();
		}
	}

	private void stopProximityCheck() {
		if (proximityTimer != null) {
			proximityTimer.stop();
			proximityTimer = null;
		}
	}

	private void checkProximityToHarold() {
		if (!isVisible()) {
			stopProximityCheck();
			return;
		}

		if (User.isNull()) return;
		User user = User.get();

		IEntity harold = null;
		for (IEntity entity : GameObjects.getInstance()) {
			if (entity instanceof NPC && "Harold".equalsIgnoreCase(entity.getName())) {
				harold = entity;
				break;
			}
		}

		if (harold == null) {
			stopProximityCheck();
			setVisible(false);
			dispose();
			return;
		}

		double dx = user.getX() - harold.getX();
		double dy = user.getY() - harold.getY();
		double distance = Math.hypot(dx, dy);

		// If player moves more than 2 tiles away from Harold, automatically close
		if (distance > 2.2) {
			stopProximityCheck();
			setVisible(false);
			dispose();
		}
	}

	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		if (b) {
			startProximityCheck();
		} else {
			stopProximityCheck();
		}
	}

	private void initStyle() {
		Style style = StyleUtil.getStyle();
		if (style != null) {
			bgColor = style.getPlainColor();
			fgColor = style.getForeground();
			highlightColor = style.getHighLightColor();
			borderDown = style.getBorderDown();
		} else {
			bgColor = new Color(110, 80, 50);
			fgColor = new Color(255, 235, 205);
			highlightColor = new Color(140, 100, 60);
			borderDown = BorderFactory.createLineBorder(new Color(80, 50, 30));
		}
	}

	private void initUI() {
		final int padding = SBoxLayout.COMMON_PADDING;
		JPanel mainPanel = new JPanel(new BorderLayout(padding, padding));
		mainPanel.setOpaque(true);
		mainPanel.setBackground(bgColor);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));

		tabbedPane = new JTabbedPane();
		tabbedPane.setBackground(bgColor);
		tabbedPane.setForeground(fgColor);

		// Tab 1: Market (Buy Offers)
		tabbedPane.addTab("Market", buildMarketTab());

		// Tab 2: My Offers (Sell Offers)
		tabbedPane.addTab("My Offers", buildMyOffersTab());

		// Tab 3: My Requests (Buy Orders & Escrow)
		tabbedPane.addTab("My Requests", buildMyRequestsTab());

		// Tab 4: All Requests (Order Book)
		tabbedPane.addTab("All Requests", buildOtherRequestsTab());

		mainPanel.add(tabbedPane, BorderLayout.CENTER);
		setContentPane(mainPanel);

		setPreferredSize(new Dimension(780, 540));
		setMinimumSize(new Dimension(650, 420));
		pack();
		setLocationRelativeTo(j2DClient.get() != null ? j2DClient.get().getMainFrame() : null);
	}

	// -------------------------------------------------------------
	// TAB 1: Market
	// -------------------------------------------------------------
	private JComponent buildMarketTab() {
		JPanel panel = new JPanel(new BorderLayout(2, 2));
		panel.setOpaque(true);
		panel.setBackground(bgColor);

		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
		topPanel.setOpaque(false);

		JLabel catLabel = new JLabel("Category:");
		catLabel.setFont(catLabel.getFont().deriveFont(Font.BOLD));
		catLabel.setForeground(fgColor);
		topPanel.add(catLabel);

		String[] categories = { "Most recent", "All", "Weapons", "Armor", "Consumables", "Tools", "Other" };
		categoryComboBox = new JComboBox<>(categories);
		categoryComboBox.setBackground(highlightColor);
		categoryComboBox.setForeground(fgColor);
		categoryComboBox.addActionListener(e -> updateData());
		topPanel.add(categoryComboBox);

		JButton fetchEarningsBtn = new JButton("Collect Earnings");
		fetchEarningsBtn.setBackground(new Color(218, 165, 32));
		fetchEarningsBtn.setForeground(Color.BLACK);
		fetchEarningsBtn.setFont(fetchEarningsBtn.getFont().deriveFont(Font.BOLD, 11f));
		fetchEarningsBtn.addActionListener(e -> controller.fetchEarnings());
		topPanel.add(fetchEarningsBtn);

		JButton refreshBtn = new JButton("🔄 Refresh");
		refreshBtn.setBackground(highlightColor);
		refreshBtn.setForeground(fgColor);
		refreshBtn.setFont(refreshBtn.getFont().deriveFont(Font.BOLD, 11f));
		refreshBtn.addActionListener(e -> controller.refreshOffers());
		topPanel.add(refreshBtn);

		panel.add(topPanel, BorderLayout.NORTH);

		marketTableModel = new MarketTableModel();
		marketTable = new JTable(marketTableModel);
		styleTable(marketTable);
		marketTable.setRowHeight(42);

		marketTable.getColumnModel().getColumn(0).setPreferredWidth(45);
		marketTable.getColumnModel().getColumn(1).setPreferredWidth(360);
		marketTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		marketTable.getColumnModel().getColumn(3).setPreferredWidth(85);

		marketTable.getColumnModel().getColumn(0).setCellRenderer(new ItemIconRenderer());
		marketTable.getColumnModel().getColumn(3).setCellRenderer(new ActionButtonRenderer("Buy", new Color(34, 139, 34)));

		marketTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = marketTable.rowAtPoint(e.getPoint());
				int col = marketTable.columnAtPoint(e.getPoint());
				if (row >= 0 && col == 3) {
					List<MarketOfferItem> offers = getFilteredActiveOffers();
					if (row < offers.size()) {
						MarketOfferItem item = offers.get(row);
						handleBuyClick(item);
					}
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(marketTable);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setBackground(bgColor);
		if (borderDown != null) {
			scrollPane.setBorder(borderDown);
		}
		panel.add(scrollPane, BorderLayout.CENTER);

		return panel;
	}

	// -------------------------------------------------------------
	// TAB 2: My Offers
	// -------------------------------------------------------------
	private JComponent buildMyOffersTab() {
		JPanel panel = new JPanel(new BorderLayout(2, 2));
		panel.setOpaque(true);
		panel.setBackground(bgColor);

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setOpaque(false);
		topPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(borderDown, "Place New Offer", 0, 0, null, fgColor),
				BorderFactory.createEmptyBorder(4, 6, 4, 6)));

		// Row 1: Drop box + input fields + search button + sell button
		JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
		row1.setOpaque(false);

		offerDropBox = new ItemDropBox(item -> {
			if (item != null) {
				offerItemInput.setText(item.getName());
			}
		});
		row1.add(offerDropBox);

		offerItemInput = new JTextField(10);
		offerItemInput.setPreferredSize(new Dimension(115, 26));
		offerItemInput.setBackground(highlightColor);
		offerItemInput.setForeground(fgColor);
		offerItemInput.setCaretColor(fgColor);
		offerItemInput.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyReleased(java.awt.event.KeyEvent e) {
				offerDropBox.setItemName(offerItemInput.getText().trim());
				offerDropBox.repaint();
			}
		});
		row1.add(offerItemInput);

		JButton searchOfferItemBtn = new JButton("🔍");
		searchOfferItemBtn.setPreferredSize(new Dimension(28, 26));
		searchOfferItemBtn.setMargin(new Insets(1, 1, 1, 1));
		searchOfferItemBtn.setBackground(highlightColor);
		searchOfferItemBtn.setForeground(fgColor);
		searchOfferItemBtn.setToolTipText("Search item to sell");
		searchOfferItemBtn.addActionListener(e -> {
			new ItemSearchDialog(this, (selectedName, spritePath) -> {
				offerItemInput.setText(selectedName);
				offerDropBox.setItem(selectedName, spritePath);
				offerPriceField.requestFocus();
			}).setVisible(true);
		});
		row1.add(searchOfferItemBtn);

		JLabel qtyLabel = new JLabel("Qty:");
		qtyLabel.setForeground(fgColor);
		row1.add(qtyLabel);

		offerQtyField = new JTextField("1", 3);
		offerQtyField.setPreferredSize(new Dimension(45, 26));
		offerQtyField.setBackground(highlightColor);
		offerQtyField.setForeground(fgColor);
		offerQtyField.setCaretColor(fgColor);
		row1.add(offerQtyField);

		JLabel priceLabel = new JLabel("Price:");
		priceLabel.setForeground(fgColor);
		row1.add(priceLabel);

		offerPriceField = new JTextField("100", 4);
		offerPriceField.setPreferredSize(new Dimension(55, 26));
		offerPriceField.setBackground(highlightColor);
		offerPriceField.setForeground(fgColor);
		offerPriceField.setCaretColor(fgColor);
		offerPriceField.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyReleased(java.awt.event.KeyEvent e) {
				updateOfferCalculatedFee();
			}
		});
		row1.add(offerPriceField);

		offerSeparatelyCheckBox = new JCheckBox("/ pc");
		offerSeparatelyCheckBox.setSelected(true);
		offerSeparatelyCheckBox.setOpaque(false);
		offerSeparatelyCheckBox.setForeground(fgColor);
		row1.add(offerSeparatelyCheckBox);

		sellButton = new JButton("Sell");
		sellButton.setPreferredSize(new Dimension(80, 26));
		sellButton.setFont(sellButton.getFont().deriveFont(Font.BOLD));
		sellButton.setBackground(new Color(34, 139, 34));
		sellButton.setForeground(Color.WHITE);
		sellButton.addActionListener(e -> {
			String item = offerItemInput.getText().trim();
			if (item.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please drag an item into the slot or enter an item name.");
				return;
			}
			try {
				int price = Integer.parseInt(offerPriceField.getText().trim());
				int qty = 1;
				try {
					qty = Integer.parseInt(offerQtyField.getText().trim());
				} catch (Exception ex) {}

				showSellConfirmation(item, price, qty, offerSeparatelyCheckBox.isSelected());
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Please enter a valid price.");
			}
		});
		row1.add(sellButton);

		// Row 2: Fee calculation + Refresh
		JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 2));
		row2.setOpaque(false);

		offerFeeLabel = new JLabel("Fee: 3 money");
		offerFeeLabel.setForeground(fgColor);
		row2.add(offerFeeLabel);

		JButton refreshBtn = new JButton("🔄 Refresh");
		refreshBtn.setBackground(highlightColor);
		refreshBtn.setForeground(fgColor);
		refreshBtn.setFont(refreshBtn.getFont().deriveFont(Font.BOLD, 10.5f));
		refreshBtn.addActionListener(e -> controller.refreshOffers());
		row2.add(refreshBtn);

		topPanel.add(row1);
		topPanel.add(row2);
		panel.add(topPanel, BorderLayout.NORTH);

		myOffersTableModel = new MyOffersTableModel();
		myOffersTable = new JTable(myOffersTableModel);
		styleTable(myOffersTable);
		myOffersTable.setRowHeight(36);

		myOffersTable.getColumnModel().getColumn(0).setPreferredWidth(45);
		myOffersTable.getColumnModel().getColumn(1).setPreferredWidth(75);
		myOffersTable.getColumnModel().getColumn(2).setPreferredWidth(180);
		myOffersTable.getColumnModel().getColumn(3).setPreferredWidth(140);
		myOffersTable.getColumnModel().getColumn(4).setPreferredWidth(200);

		myOffersTable.getColumnModel().getColumn(0).setCellRenderer(new ItemIconRenderer());
		myOffersTable.getColumnModel().getColumn(4).setCellRenderer(new MyOffersActionRenderer());

		myOffersTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = myOffersTable.rowAtPoint(e.getPoint());
				int col = myOffersTable.columnAtPoint(e.getPoint());
				if (row >= 0 && col == 4) {
					List<MarketOfferItem> mine = controller.getMyOffers();
					List<MarketOfferItem> expired = controller.getMyExpiredOffers();

					MarketOfferItem targetItem = null;
					if (row < mine.size()) {
						targetItem = mine.get(row);
					} else if (row - mine.size() < expired.size()) {
						targetItem = expired.get(row - mine.size());
					}

					if (targetItem != null) {
						int cellX = e.getX() - myOffersTable.getCellRect(row, col, false).x;
						if (cellX < 95) {
							showProlongConfirmation(targetItem);
						} else {
							showRemoveConfirmation(targetItem);
						}
					}
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(myOffersTable);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setBackground(bgColor);
		if (borderDown != null) {
			scrollPane.setBorder(borderDown);
		}
		panel.add(scrollPane, BorderLayout.CENTER);

		return panel;
	}

	// -------------------------------------------------------------
	// TAB 3: My Requests (Buy Orders & Delivery Box)
	// -------------------------------------------------------------
	private JComponent buildMyRequestsTab() {
		JPanel panel = new JPanel(new BorderLayout(2, 2));
		panel.setOpaque(true);
		panel.setBackground(bgColor);

		// Top Area: Place Buy Request & Claim Deliveries Button
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setOpaque(false);
		topPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(borderDown, "Place New Buy Request", 0, 0, null, fgColor),
				BorderFactory.createEmptyBorder(4, 6, 4, 6)));

		// Row 1: Drop box + input fields + search button + button
		JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
		row1.setOpaque(false);

		reqDropBox = new ItemDropBox(item -> {
			if (item != null) {
				reqItemInput.setText(item.getName());
			}
		});
		row1.add(reqDropBox);

		reqItemInput = new JTextField(10);
		reqItemInput.setPreferredSize(new Dimension(115, 26));
		reqItemInput.setBackground(highlightColor);
		reqItemInput.setForeground(fgColor);
		reqItemInput.setCaretColor(fgColor);
		reqItemInput.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyReleased(java.awt.event.KeyEvent e) {
				reqDropBox.setItemName(reqItemInput.getText().trim());
				reqDropBox.repaint();
			}
		});
		row1.add(reqItemInput);

		JButton searchReqItemBtn = new JButton("🔍");
		searchReqItemBtn.setPreferredSize(new Dimension(28, 26));
		searchReqItemBtn.setMargin(new Insets(1, 1, 1, 1));
		searchReqItemBtn.setBackground(highlightColor);
		searchReqItemBtn.setForeground(fgColor);
		searchReqItemBtn.setToolTipText("Search item to request");
		searchReqItemBtn.addActionListener(e -> {
			new ItemSearchDialog(this, (selectedName, spritePath) -> {
				reqItemInput.setText(selectedName);
				reqDropBox.setItem(selectedName, spritePath);
				reqQtyField.requestFocus();
			}).setVisible(true);
		});
		row1.add(searchReqItemBtn);

		JLabel qtyLabel = new JLabel("Wanted Qty:");
		qtyLabel.setForeground(fgColor);
		row1.add(qtyLabel);

		reqQtyField = new JTextField("1", 3);
		reqQtyField.setPreferredSize(new Dimension(45, 26));
		reqQtyField.setBackground(highlightColor);
		reqQtyField.setForeground(fgColor);
		reqQtyField.setCaretColor(fgColor);
		reqQtyField.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyReleased(java.awt.event.KeyEvent e) {
				updateReqSummary();
			}
		});
		row1.add(reqQtyField);

		JLabel priceLabel = new JLabel("Price / ea:");
		priceLabel.setForeground(fgColor);
		row1.add(priceLabel);

		reqPriceField = new JTextField("1", 4);
		reqPriceField.setPreferredSize(new Dimension(55, 26));
		reqPriceField.setBackground(highlightColor);
		reqPriceField.setForeground(fgColor);
		reqPriceField.setCaretColor(fgColor);
		reqPriceField.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyReleased(java.awt.event.KeyEvent e) {
				updateReqSummary();
			}
		});
		row1.add(reqPriceField);

		placeReqButton = new JButton("Place Request");
		placeReqButton.setPreferredSize(new Dimension(115, 26));
		placeReqButton.setFont(placeReqButton.getFont().deriveFont(Font.BOLD));
		placeReqButton.setBackground(new Color(34, 139, 34));
		placeReqButton.setForeground(Color.WHITE);
		placeReqButton.addActionListener(e -> {
			String item = reqItemInput.getText().trim();
			if (item.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please enter or drag an item to request.");
				return;
			}
			try {
				int qty = Integer.parseInt(reqQtyField.getText().trim());
				int price = Integer.parseInt(reqPriceField.getText().trim());
				showRequestConfirmation(item, qty, price);
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Please enter valid numbers for quantity and price.");
			}
		});
		row1.add(placeReqButton);

		// Row 2: Escrow Summary + Claim Button + Refresh Button
		JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 2));
		row2.setOpaque(false);

		reqSummaryLabel = new JLabel("Escrow: 1 money, Fee: 1 money (Total: 2)");
		reqSummaryLabel.setForeground(fgColor);
		row2.add(reqSummaryLabel);

		claimButton = new JButton("Claim Delivered Items (0 ready)");
		claimButton.setPreferredSize(new Dimension(220, 24));
		claimButton.setFont(claimButton.getFont().deriveFont(Font.BOLD, 11f));
		claimButton.setBackground(new Color(70, 130, 180));
		claimButton.setForeground(Color.WHITE);
		claimButton.addActionListener(e -> controller.claimItems());
		row2.add(claimButton);

		JButton refreshBtn = new JButton("🔄 Refresh");
		refreshBtn.setBackground(highlightColor);
		refreshBtn.setForeground(fgColor);
		refreshBtn.setFont(refreshBtn.getFont().deriveFont(Font.BOLD, 10.5f));
		refreshBtn.addActionListener(e -> controller.refreshOffers());
		row2.add(refreshBtn);

		topPanel.add(row1);
		topPanel.add(row2);
		panel.add(topPanel, BorderLayout.NORTH);

		// Bottom Table: My active buy requests
		myRequestsTableModel = new MyRequestsTableModel();
		myRequestsTable = new JTable(myRequestsTableModel);
		styleTable(myRequestsTable);
		myRequestsTable.setRowHeight(36);

		myRequestsTable.getColumnModel().getColumn(0).setPreferredWidth(40);  // Icon
		myRequestsTable.getColumnModel().getColumn(1).setPreferredWidth(125); // Item
		myRequestsTable.getColumnModel().getColumn(2).setPreferredWidth(60);  // Total
		myRequestsTable.getColumnModel().getColumn(3).setPreferredWidth(65);  // Fulfilled
		myRequestsTable.getColumnModel().getColumn(4).setPreferredWidth(65);  // Remaining
		myRequestsTable.getColumnModel().getColumn(5).setPreferredWidth(75);  // Price / ea
		myRequestsTable.getColumnModel().getColumn(6).setPreferredWidth(75);  // Deposit Left
		myRequestsTable.getColumnModel().getColumn(7).setPreferredWidth(85);  // Claim Delivered
		myRequestsTable.getColumnModel().getColumn(8).setPreferredWidth(65);  // Cancel

		myRequestsTable.getColumnModel().getColumn(0).setCellRenderer(new ItemIconRenderer());
		myRequestsTable.getColumnModel().getColumn(7).setCellRenderer(new MyRequestsClaimRenderer());
		myRequestsTable.getColumnModel().getColumn(8).setCellRenderer(new ActionButtonRenderer("Cancel", new Color(178, 34, 34)));

		myRequestsTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = myRequestsTable.rowAtPoint(e.getPoint());
				int col = myRequestsTable.columnAtPoint(e.getPoint());
				if (row >= 0) {
					List<MarketRequestItem> myReqs = controller.getMyRequests();
					if (row < myReqs.size()) {
						MarketRequestItem req = myReqs.get(row);
						if (col == 7) {
							int claimable = getClaimableCount(req.getItemName());
							if (claimable > 0) {
								controller.claimItem(req.getItemName());
							} else {
								JOptionPane.showMessageDialog(HaroldMarketWindow.this,
										"No delivered items are ready to claim for " + req.getItemName() + " yet.");
							}
						} else if (col == 8) {
							showCancelRequestConfirmation(req);
						}
					}
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(myRequestsTable);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setBackground(bgColor);
		if (borderDown != null) {
			scrollPane.setBorder(borderDown);
		}
		panel.add(scrollPane, BorderLayout.CENTER);

		return panel;
	}

	// -------------------------------------------------------------
	// TAB 4: All Requests (Order Book Fulfillment)
	// -------------------------------------------------------------
	private JComponent buildOtherRequestsTab() {
		JPanel panel = new JPanel(new BorderLayout(2, 2));
		panel.setOpaque(true);
		panel.setBackground(bgColor);

		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
		topPanel.setOpaque(false);

		JLabel catLabel = new JLabel("Category:");
		catLabel.setFont(catLabel.getFont().deriveFont(Font.BOLD));
		catLabel.setForeground(fgColor);
		topPanel.add(catLabel);

		String[] categories = { "All Requests", "Weapons", "Armor", "Consumables", "Tools", "Other" };
		otherReqCatComboBox = new JComboBox<>(categories);
		otherReqCatComboBox.setBackground(highlightColor);
		otherReqCatComboBox.setForeground(fgColor);
		otherReqCatComboBox.addActionListener(e -> updateData());
		topPanel.add(otherReqCatComboBox);

		JButton refreshBtn = new JButton("🔄 Refresh");
		refreshBtn.setBackground(highlightColor);
		refreshBtn.setForeground(fgColor);
		refreshBtn.setFont(refreshBtn.getFont().deriveFont(Font.BOLD, 11f));
		refreshBtn.addActionListener(e -> controller.refreshOffers());
		topPanel.add(refreshBtn);

		panel.add(topPanel, BorderLayout.NORTH);

		otherRequestsTableModel = new OtherRequestsTableModel();
		otherRequestsTable = new JTable(otherRequestsTableModel);
		styleTable(otherRequestsTable);
		otherRequestsTable.setRowHeight(36);

		otherRequestsTable.getColumnModel().getColumn(0).setPreferredWidth(45);  // Icon
		otherRequestsTable.getColumnModel().getColumn(1).setPreferredWidth(140); // Item
		otherRequestsTable.getColumnModel().getColumn(2).setPreferredWidth(110); // Buyer
		otherRequestsTable.getColumnModel().getColumn(3).setPreferredWidth(70);  // Wanted
		otherRequestsTable.getColumnModel().getColumn(4).setPreferredWidth(70);  // Fulfilled
		otherRequestsTable.getColumnModel().getColumn(5).setPreferredWidth(70);  // Remaining
		otherRequestsTable.getColumnModel().getColumn(6).setPreferredWidth(85);  // Price / ea
		otherRequestsTable.getColumnModel().getColumn(7).setPreferredWidth(85);  // Action (Fulfill / Mine)

		otherRequestsTable.getColumnModel().getColumn(0).setCellRenderer(new ItemIconRenderer());
		otherRequestsTable.getColumnModel().getColumn(7).setCellRenderer(new OtherRequestsActionRenderer());

		otherRequestsTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = otherRequestsTable.rowAtPoint(e.getPoint());
				int col = otherRequestsTable.columnAtPoint(e.getPoint());
				if (row >= 0 && col == 7) {
					List<MarketRequestItem> reqs = controller.getOtherRequests();
					if (row < reqs.size()) {
						MarketRequestItem req = reqs.get(row);
						String currentUserName = User.get() != null ? User.get().getName() : "";
						if (req.getBuyer().equalsIgnoreCase(currentUserName)) {
							JOptionPane.showMessageDialog(HaroldMarketWindow.this,
									"This is your own buy request. You can view, manage, or cancel it under the 'My Requests' tab.");
						} else {
							handleFulfillClick(req);
						}
					}
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(otherRequestsTable);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setBackground(bgColor);
		if (borderDown != null) {
			scrollPane.setBorder(borderDown);
		}
		panel.add(scrollPane, BorderLayout.CENTER);

		return panel;
	}

	// -------------------------------------------------------------
	// Confirmation Dialogues (Direct Action RPCs)
	// -------------------------------------------------------------
	private void handleBuyClick(MarketOfferItem item) {
		int qty = 1;
		if (item.isStackable() && item.getQuantity() > 1) {
			String input = JOptionPane.showInputDialog(this,
					"How many " + item.getItemName() + " would you like to buy? (1 - " + item.getQuantity() + ")",
					item.getQuantity());
			if (input == null || input.trim().isEmpty()) {
				return;
			}
			try {
				qty = Integer.parseInt(input.trim());
				if (qty <= 0 || qty > item.getQuantity()) {
					JOptionPane.showMessageDialog(this, "Please enter a quantity between 1 and " + item.getQuantity());
					return;
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Invalid quantity number.");
				return;
			}
		}
		showBuyConfirmation(item, qty);
	}

	private void handleFulfillClick(MarketRequestItem req) {
		int qty = 1;
		if (req.getRemainingQuantity() > 1) {
			String input = JOptionPane.showInputDialog(this,
					"How many " + req.getItemName() + " do you want to sell to " + req.getBuyer() + "? (1 - " + req.getRemainingQuantity() + ")",
					req.getRemainingQuantity());
			if (input == null || input.trim().isEmpty()) {
				return;
			}
			try {
				qty = Integer.parseInt(input.trim());
				if (qty <= 0 || qty > req.getRemainingQuantity()) {
					JOptionPane.showMessageDialog(this, "Please enter a quantity between 1 and " + req.getRemainingQuantity());
					return;
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Invalid quantity number.");
				return;
			}
		}
		showFulfillConfirmation(req, qty);
	}

	private void showProlongConfirmation(MarketOfferItem item) {
		int fee = Math.max(1, (int) Math.ceil(item.getPrice() * 0.03));
		int choice = JOptionPane.showConfirmDialog(
				this,
				"Do you want to prolong your offer of " + item.getQuantity() + " " + item.getItemName() + " for " + fee + " money?",
				"Prolong Offer",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (choice == JOptionPane.YES_OPTION) {
			controller.prolongOffer(item.getId());
		}
	}

	private void showRemoveConfirmation(MarketOfferItem item) {
		int choice = JOptionPane.showConfirmDialog(
				this,
				"Do you want to remove your offer of " + item.getQuantity() + " " + item.getItemName() + "?",
				"Remove Offer",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (choice == JOptionPane.YES_OPTION) {
			controller.removeOffer(item.getId());
		}
	}

	private void showSellConfirmation(String item, int price, int qty, boolean perPiece) {
		int fee = Math.max(1, (int) Math.ceil(price * 0.03));
		int choice = JOptionPane.showConfirmDialog(
				this,
				"Do you want to sell " + qty + " " + item + " for " + price + " money?\nFee: " + fee + " money.",
				"Place Offer",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (choice == JOptionPane.YES_OPTION) {
			controller.placeOffer(item, price, qty, perPiece);
			offerItemInput.setText("");
			offerDropBox.clear();
		}
	}

	private void showBuyConfirmation(MarketOfferItem item, int qty) {
		int totalPrice = item.isPerPiece() ? item.getPrice() * qty : item.getPrice();
		int choice = JOptionPane.showConfirmDialog(
				this,
				"Do you want to buy " + qty + " " + item.getItemName() + " for " + totalPrice + " money?",
				"Buy Offer",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (choice == JOptionPane.YES_OPTION) {
			controller.buyOffer(item.getId(), qty);
		}
	}

	private void showRequestConfirmation(String item, int qty, int price) {
		int totalDeposit = qty * price;
		int fee = Math.max(1, (int) Math.ceil(totalDeposit * 0.01));
		int choice = JOptionPane.showConfirmDialog(
				this,
				"Do you want to request " + qty + " " + item + " at " + price + " money each?\nDeposit: " + totalDeposit + " money\nFee: " + fee + " money\nTotal: " + (totalDeposit + fee) + " money",
				"Place Buy Request",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (choice == JOptionPane.YES_OPTION) {
			controller.placeRequest(item, qty, price);
			reqItemInput.setText("");
			reqDropBox.clear();
		}
	}

	private void showFulfillConfirmation(MarketRequestItem req, int qty) {
		int totalPayment = qty * req.getPricePerPiece();
		int choice = JOptionPane.showConfirmDialog(
				this,
				"Do you want to sell " + qty + " " + req.getItemName() + " to " + req.getBuyer() + " for " + totalPayment + " money?",
				"Fulfill Buy Request",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (choice == JOptionPane.YES_OPTION) {
			controller.fulfillRequest(req.getId(), qty);
		}
	}

	private void showCancelRequestConfirmation(MarketRequestItem req) {
		int choice = JOptionPane.showConfirmDialog(
				this,
				"Do you want to cancel your buy request for " + req.getItemName() + "?\nRemaining deposit of " + req.getTotalCost() + " money will be refunded to your earnings.",
				"Cancel Buy Request",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (choice == JOptionPane.YES_OPTION) {
			controller.cancelRequest(req.getId());
		}
	}

	private void updateOfferCalculatedFee() {
		try {
			int price = Integer.parseInt(offerPriceField.getText().trim());
			int fee = Math.max(1, (int) Math.ceil(price * 0.03));
			offerFeeLabel.setText("Fee: " + fee + " money");
		} catch (Exception e) {
			offerFeeLabel.setText("Fee: -");
		}
	}

	private void updateReqSummary() {
		try {
			int qty = Integer.parseInt(reqQtyField.getText().trim());
			int price = Integer.parseInt(reqPriceField.getText().trim());
			int totalDeposit = qty * price;
			int fee = Math.max(1, (int) Math.ceil(totalDeposit * 0.01));
			reqSummaryLabel.setText("Deposit: " + totalDeposit + " money, Fee: " + fee + " (Total: " + (totalDeposit + fee) + ")");
		} catch (Exception e) {
			reqSummaryLabel.setText("Deposit: -");
		}
	}

	public void updateData() {
		if (marketTableModel != null) {
			marketTableModel.fireTableDataChanged();
		}
		if (myOffersTableModel != null) {
			myOffersTableModel.fireTableDataChanged();
		}
		if (myRequestsTableModel != null) {
			myRequestsTableModel.fireTableDataChanged();
		}
		if (otherRequestsTableModel != null) {
			otherRequestsTableModel.fireTableDataChanged();
		}

		if (claimButton != null) {
			List<DeliveryCollectionItem> deliveries = controller.getDeliveryItems();
			int count = deliveries.size();
			claimButton.setText("Claim Delivered Items (" + count + " ready)");
			if (count > 0) {
				claimButton.setBackground(new Color(218, 165, 32));
				claimButton.setForeground(Color.BLACK);
			} else {
				claimButton.setBackground(new Color(70, 130, 180));
				claimButton.setForeground(Color.WHITE);
			}
		}
	}

	private List<MarketOfferItem> getFilteredActiveOffers() {
		List<MarketOfferItem> active = controller.getActiveOffers();
		if (categoryComboBox == null) return active;
		String category = (String) categoryComboBox.getSelectedItem();
		if (category == null || category.equals("All") || category.equals("Most recent")) {
			return active;
		}

		List<MarketOfferItem> filtered = new ArrayList<>();
		for (MarketOfferItem item : active) {
			String path = item.getClassPath().toLowerCase();
			if (category.equals("Weapons") && (path.contains("weapon") || path.contains("sword") || path.contains("bow") || path.contains("axe") || path.contains("dagger") || path.contains("club") || path.contains("hammer"))) {
				filtered.add(item);
			} else if (category.equals("Armor") && (path.contains("armor") || path.contains("shield") || path.contains("helmet") || path.contains("boot") || path.contains("cloak") || path.contains("ring") || path.contains("legs"))) {
				filtered.add(item);
			} else if (category.equals("Consumables") && (path.contains("food") || path.contains("potion") || path.contains("herb") || path.contains("meat"))) {
				filtered.add(item);
			} else if (category.equals("Tools") && (path.contains("tool") || path.contains("rod") || path.contains("key") || path.contains("pickaxe"))) {
				filtered.add(item);
			} else if (category.equals("Other")) {
				filtered.add(item);
			}
		}
		return filtered;
	}

	private void styleTable(JTable table) {
		table.setOpaque(false);
		table.setBackground(bgColor);
		table.setForeground(fgColor);
		table.setSelectionBackground(highlightColor);
		table.setSelectionForeground(fgColor);
		table.setGridColor(new Color(90, 60, 40));
		table.setShowGrid(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setBackground(highlightColor);
		table.getTableHeader().setForeground(fgColor);
		table.getTableHeader().setFont(table.getTableHeader().getFont().deriveFont(Font.BOLD));
	}

	// -------------------------------------------------------------
	// Renderers & Drag/Drop Components
	// -------------------------------------------------------------
	private class ItemDropBox extends JPanel implements DropTarget {
		private static final long serialVersionUID = 1L;
		private IEntity heldEntity;
		private String customItemName;
		private String customSpritePath;
		private java.util.function.Consumer<IEntity> onDropCallback;

		public ItemDropBox(java.util.function.Consumer<IEntity> onDropCallback) {
			this.onDropCallback = onDropCallback;
			setPreferredSize(new Dimension(40, 40));
			setMinimumSize(new Dimension(40, 40));
			setMaximumSize(new Dimension(40, 40));
			setOpaque(true);
			setBackground(highlightColor);
			setBorder(BorderFactory.createLoweredBevelBorder());
		}

		public void setItem(String name, String spritePath) {
			this.heldEntity = null;
			this.customItemName = name;
			this.customSpritePath = spritePath;
			repaint();
		}

		public void setItemName(String name) {
			this.heldEntity = null;
			this.customItemName = name;
			this.customSpritePath = ItemSearchDialog.getSpritePath(name);
			repaint();
		}

		public void clear() {
			heldEntity = null;
			customItemName = null;
			customSpritePath = null;
			repaint();
		}

		@Override
		public void dropEntity(IEntity entity, int amount, Point point) {
			if (entity != null) {
				heldEntity = entity;
				customItemName = entity.getName();
				customSpritePath = null;
				if (onDropCallback != null) {
					onDropCallback.accept(entity);
				}
				repaint();
			}
		}

		@Override
		public boolean canAccept(IEntity entity) {
			return entity != null;
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			String classPath = customSpritePath;
			String name = null;
			if (heldEntity != null) {
				name = heldEntity.getName();
				if (heldEntity.getEntityClass() != null) {
					classPath = heldEntity.getEntityClass();
					if (heldEntity.getEntitySubclass() != null) {
						classPath += "/" + heldEntity.getEntitySubclass();
					}
				}
			} else if (customItemName != null && !customItemName.trim().isEmpty()) {
				name = customItemName.trim();
				if (classPath == null) {
					classPath = ItemSearchDialog.getSpritePath(name);
				}
			}

			if (classPath != null || name != null) {
				final Sprite sprite = getItemSprite(classPath, name);
				if (sprite != null) {
					final int x = (getWidth() - sprite.getWidth()) / 2;
					final int y = (getHeight() - sprite.getHeight()) / 2;
					sprite.draw(g, x, y);
				}
			}
		}
	}

	private class ItemIconRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel label = (JLabel) super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
			label.setIcon(null);
			label.setText("");
			label.setHorizontalAlignment(CENTER);

			Sprite sprite = null;
			int amount = 1;

			if (value instanceof MarketOfferItem) {
				MarketOfferItem item = (MarketOfferItem) value;
				sprite = getItemSprite(item.getClassPath(), item.getItemName());
				amount = item.getQuantity();
			} else if (value instanceof MarketRequestItem) {
				MarketRequestItem req = (MarketRequestItem) value;
				sprite = getItemSprite(req.getClassPath(), req.getItemName());
				amount = req.getRemainingQuantity();
			} else if (value instanceof DeliveryCollectionItem) {
				DeliveryCollectionItem del = (DeliveryCollectionItem) value;
				sprite = getItemSprite(del.getClassPath(), del.getItemName());
				amount = del.getQuantity();
			}

			if (sprite != null) {
				final Sprite finalSprite = sprite;
				final int finalAmount = amount;
				label.setIcon(new javax.swing.Icon() {
					@Override
					public void paintIcon(Component c, Graphics g, int x, int y) {
						finalSprite.draw(g, x + 2, y + 2);
						if (finalAmount > 1) {
							g.setColor(Color.YELLOW);
							g.setFont(g.getFont().deriveFont(Font.BOLD, 10f));
							g.drawString(String.valueOf(finalAmount), x + 18, y + 30);
						}
					}
					@Override
					public int getIconWidth() { return 36; }
					@Override
					public int getIconHeight() { return 36; }
				});
			} else {
				label.setIcon(null);
			}

			return label;
		}
	}

	private class ActionButtonRenderer implements TableCellRenderer {
		private final JButton button = new JButton();

		public ActionButtonRenderer(String text, Color bg) {
			button.setOpaque(true);
			button.setBackground(bg);
			button.setForeground(Color.WHITE);
			button.setFont(button.getFont().deriveFont(Font.BOLD, 11f));
			button.setText(text);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			return button;
		}
	}

	private class OtherRequestsActionRenderer implements TableCellRenderer {
		private final JButton button = new JButton();

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			button.setOpaque(true);
			button.setFont(button.getFont().deriveFont(Font.BOLD, 11f));

			List<MarketRequestItem> reqs = controller.getOtherRequests();
			if (row < reqs.size()) {
				MarketRequestItem req = reqs.get(row);
				String currentUserName = User.get() != null ? User.get().getName() : "";
				if (req.getBuyer().equalsIgnoreCase(currentUserName)) {
					button.setText("Mine");
					button.setBackground(new Color(110, 110, 110));
					button.setForeground(new Color(220, 220, 220));
				} else {
					button.setText("Fulfill");
					button.setBackground(new Color(34, 139, 34));
					button.setForeground(Color.WHITE);
				}
			}
			return button;
		}
	}

	private int getClaimableCount(String itemName) {
		if (itemName == null) return 0;
		int total = 0;
		for (DeliveryCollectionItem item : controller.getDeliveryItems()) {
			if (itemName.equalsIgnoreCase(item.getItemName())) {
				total += item.getQuantity();
			}
		}
		return total;
	}

	private class MyRequestsClaimRenderer implements TableCellRenderer {
		private final JButton claimBtn = new JButton();
		private final JLabel emptyLabel = new JLabel("-", JLabel.CENTER);

		public MyRequestsClaimRenderer() {
			claimBtn.setOpaque(true);
			claimBtn.setFont(claimBtn.getFont().deriveFont(Font.BOLD, 10.5f));
			claimBtn.setBackground(new Color(34, 139, 34)); // forest green
			claimBtn.setForeground(Color.WHITE);
			claimBtn.setMargin(new Insets(1, 2, 1, 2));

			emptyLabel.setOpaque(true);
			emptyLabel.setBackground(bgColor);
			emptyLabel.setForeground(new Color(180, 180, 180));
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			List<MarketRequestItem> myReqs = controller.getMyRequests();
			if (row < myReqs.size()) {
				MarketRequestItem req = myReqs.get(row);
				int claimable = getClaimableCount(req.getItemName());
				if (claimable > 0) {
					claimBtn.setText("Claim (" + claimable + ")");
					return claimBtn;
				}
			}
			return emptyLabel;
		}
	}

	private class MyOffersActionRenderer implements TableCellRenderer {
		private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 2));
		private final JButton prolongBtn = new JButton("Prolong");
		private final JButton removeBtn = new JButton("Remove");

		public MyOffersActionRenderer() {
			panel.setOpaque(true);
			panel.setBackground(bgColor);

			Dimension btnSize = new Dimension(62, 22);
			prolongBtn.setPreferredSize(btnSize);
			removeBtn.setPreferredSize(btnSize);

			prolongBtn.setMargin(new Insets(1, 2, 1, 2));
			removeBtn.setMargin(new Insets(1, 2, 1, 2));

			prolongBtn.setFont(prolongBtn.getFont().deriveFont(10.5f));
			removeBtn.setFont(removeBtn.getFont().deriveFont(10.5f));

			prolongBtn.setBackground(new Color(70, 130, 180));
			prolongBtn.setForeground(Color.WHITE);
			removeBtn.setBackground(new Color(178, 34, 34));
			removeBtn.setForeground(Color.WHITE);

			panel.add(prolongBtn);
			panel.add(removeBtn);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			return panel;
		}
	}

	// -------------------------------------------------------------
	// Table Models
	// -------------------------------------------------------------
	private class MarketTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		private final String[] headers = { "Item", "Description", "Price", "Action" };

		@Override
		public int getRowCount() {
			return getFilteredActiveOffers().size();
		}

		@Override
		public int getColumnCount() {
			return headers.length;
		}

		@Override
		public String getColumnName(int col) {
			return headers[col];
		}

		@Override
		public Object getValueAt(int row, int col) {
			List<MarketOfferItem> offers = getFilteredActiveOffers();
			if (row >= offers.size()) return "";
			MarketOfferItem item = offers.get(row);
			switch (col) {
			case 0:
				return item;
			case 1:
				return item.getQuantity() + " " + item.getItemName() + " (" + item.getOfferer() + ")";
			case 2:
				return item.getPrice() + (item.isPerPiece() ? " / pc" : "");
			default:
				return "";
			}
		}
	}

	private class MyOffersTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		private final String[] headers = { "Item", "Price", "Description", "Status", "Action" };

		@Override
		public int getRowCount() {
			return controller.getMyOffers().size() + controller.getMyExpiredOffers().size();
		}

		@Override
		public int getColumnCount() {
			return headers.length;
		}

		@Override
		public String getColumnName(int col) {
			return headers[col];
		}

		@Override
		public Object getValueAt(int row, int col) {
			List<MarketOfferItem> mine = controller.getMyOffers();
			List<MarketOfferItem> expired = controller.getMyExpiredOffers();

			MarketOfferItem item;
			if (row < mine.size()) {
				item = mine.get(row);
			} else if (row - mine.size() < expired.size()) {
				item = expired.get(row - mine.size());
			} else {
				return "";
			}

			switch (col) {
			case 0:
				return item;
			case 1:
				return item.getPrice() + (item.isPerPiece() ? " / pc" : "");
			case 2:
				return item.getItemName();
			case 3:
				return item.isExpired() ? "Expired" : "Expires in 7 days";
			case 4:
				return item.isExpired() ? "Prolong | Remove" : "Remove";
			default:
				return "";
			}
		}
	}

	private class MyRequestsTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		private final String[] headers = { "Item", "Wanted", "Total", "Fulfilled", "Remaining", "Price/ea", "Deposit", "Claim", "Cancel" };

		@Override
		public int getRowCount() {
			return controller.getMyRequests().size();
		}

		@Override
		public int getColumnCount() {
			return headers.length;
		}

		@Override
		public String getColumnName(int col) {
			return headers[col];
		}

		@Override
		public Object getValueAt(int row, int col) {
			List<MarketRequestItem> myReqs = controller.getMyRequests();
			if (row >= myReqs.size()) return "";
			MarketRequestItem req = myReqs.get(row);
			int claimable = getClaimableCount(req.getItemName());
			switch (col) {
			case 0:
				return req;
			case 1:
				return req.getItemName();
			case 2:
				return req.getTotalQuantity();
			case 3:
				return req.getFulfilledQuantity();
			case 4:
				return req.getRemainingQuantity();
			case 5:
				return req.getPricePerPiece() + " money";
			case 6:
				return req.getTotalCost() + " money";
			case 7:
				return claimable > 0 ? ("Claim (" + claimable + ")") : "-";
			case 8:
				return "Cancel";
			default:
				return "";
			}
		}
	}

	private class OtherRequestsTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		private final String[] headers = { "Item", "Wanted Item", "Buyer", "Total", "Fulfilled", "Remaining", "Price/ea", "Action" };

		@Override
		public int getRowCount() {
			return controller.getOtherRequests().size();
		}

		@Override
		public int getColumnCount() {
			return headers.length;
		}

		@Override
		public String getColumnName(int col) {
			return headers[col];
		}

		@Override
		public Object getValueAt(int row, int col) {
			List<MarketRequestItem> reqs = controller.getOtherRequests();
			if (row >= reqs.size()) return "";
			MarketRequestItem req = reqs.get(row);
			switch (col) {
			case 0:
				return req;
			case 1:
				return req.getItemName();
			case 2:
				return req.getBuyer();
			case 3:
				return req.getTotalQuantity();
			case 4:
				return req.getFulfilledQuantity();
			case 5:
				return req.getRemainingQuantity();
			case 6:
				return req.getPricePerPiece() + " money";
			default:
				return "";
			}
		}
	}
}
