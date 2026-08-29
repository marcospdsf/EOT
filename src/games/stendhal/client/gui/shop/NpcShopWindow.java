/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2026 - Stendhal                    *
 ***************************************************************************/
package games.stendhal.client.gui.shop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import games.stendhal.client.GameObjects;
import games.stendhal.client.entity.IEntity;
import games.stendhal.client.entity.NPC;
import games.stendhal.client.entity.User;
import games.stendhal.client.gui.WindowUtils;
import games.stendhal.client.gui.j2DClient;
import games.stendhal.client.gui.shop.NpcShopController.ShopItem;
import games.stendhal.client.gui.styled.Style;
import games.stendhal.client.gui.styled.StyleUtil;
import games.stendhal.client.sprite.Sprite;
import games.stendhal.client.sprite.SpriteStore;

/**
 * Split-panel NPC Merchant Trade Window with Buy/Sell tabs, item search,
 * quantity slider (1..1000), quick amount buttons, and distance auto-close.
 */
public class NpcShopWindow extends JDialog {

	private static final long serialVersionUID = 1L;

	/**
	 * Resolves item sprite using Stendhal's standard Item resource format (class/subclass),
	 * exactly matching ItemListImageViewerEvent and Item2DView.
	 *
	 * @param classPath
	 *            Resource subpath in format "class/subclass" (e.g. "food/baguette")
	 * @param itemName
	 *            Item display name fallback
	 * @return Sprite
	 */
	public static Sprite getItemSprite(final String classPath, final String itemName) {
		final SpriteStore store = SpriteStore.get();

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

		if (itemName != null && !itemName.trim().isEmpty()) {
			final String indexedPath = games.stendhal.client.gui.market.ItemSearchDialog.getSpritePath(itemName);
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

		return store.getSprite("data/sprites/items/money.png");
	}

	public static Sprite getItemSprite(final String itemName) {
		return getItemSprite(null, itemName);
	}

	private String npcName;
	private List<ShopItem> rawBuyItems = new ArrayList<>();
	private List<ShopItem> rawSellItems = new ArrayList<>();

	private JTabbedPane tabbedPane;
	private ShopPanel buyPanel;
	private ShopPanel sellPanel;

	private Timer proximityTimer;

	private Color bgColor;
	private Color fgColor;
	private Color highlightColor;
	private Border borderDown;

	public NpcShopWindow() {
		super(j2DClient.get().getMainFrame(), "Shop", false);
		initComponents();
		initProximityCheck();
	}

	private void initStyle() {
		final Style style = StyleUtil.getStyle();
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

	private void initComponents() {
		initStyle();
		setLayout(new BorderLayout());
		getContentPane().setBackground(bgColor);
		setSize(640, 460);
		setMinimumSize(new Dimension(580, 400));
		setLocationRelativeTo(j2DClient.get().getMainFrame());

		tabbedPane = new JTabbedPane();
		tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 12));
		tabbedPane.setBackground(bgColor);
		tabbedPane.setForeground(fgColor);

		buyPanel = new ShopPanel("buy");
		sellPanel = new ShopPanel("sell");

		add(tabbedPane, BorderLayout.CENTER);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				stopProximityTimer();
				if (npcName != null) {
					NpcShopController.get().sendClose(npcName);
				}
			}
		});
	}

	public void setData(final String npcName, final List<ShopItem> buyItems, final List<ShopItem> sellItems) {
		this.npcName = npcName;
		this.rawBuyItems = buyItems != null ? buyItems : new ArrayList<ShopItem>();
		this.rawSellItems = sellItems != null ? sellItems : new ArrayList<ShopItem>();

		setTitle(npcName + " - Shop");

		tabbedPane.removeAll();
		if (!rawBuyItems.isEmpty()) {
			buyPanel.setItems(rawBuyItems);
			tabbedPane.addTab("Buy from NPC (" + rawBuyItems.size() + ")", buyPanel);
		}
		if (!rawSellItems.isEmpty()) {
			sellPanel.setItems(rawSellItems);
			tabbedPane.addTab("Sell to NPC (" + rawSellItems.size() + ")", sellPanel);
		}

		startProximityTimer();
	}

	private void initProximityCheck() {
		proximityTimer = new Timer(500, new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				if (!isVisible() || npcName == null) {
					return;
				}
				if (User.isNull()) {
					setVisible(false);
					stopProximityTimer();
					if (npcName != null) {
						NpcShopController.get().sendClose(npcName);
					}
					return;
				}
				final User user = User.get();
				boolean foundNpc = false;
				for (final IEntity entity : GameObjects.getInstance()) {
					if (entity instanceof NPC) {
						final NPC npc = (NPC) entity;
						if (npc.getName().equalsIgnoreCase(npcName) || (npc.getTitle() != null && npc.getTitle().equalsIgnoreCase(npcName))) {
							final int dx = Math.abs((int) user.getX() - (int) npc.getX());
							final int dy = Math.abs((int) user.getY() - (int) npc.getY());
							if (dx <= 4 && dy <= 4) {
								foundNpc = true;
								break;
							}
						}
					}
				}
				if (!foundNpc) {
					setVisible(false);
					stopProximityTimer();
					if (npcName != null) {
						NpcShopController.get().sendClose(npcName);
					}
				}
			}
		});
	}

	private void startProximityTimer() {
		if (proximityTimer != null && !proximityTimer.isRunning()) {
			proximityTimer.start();
		}
	}

	private void stopProximityTimer() {
		if (proximityTimer != null && proximityTimer.isRunning()) {
			proximityTimer.stop();
		}
	}

	/**
	 * Inner panel representing either the Buy or Sell view.
	 */
	private class ShopPanel extends JPanel {

		private static final long serialVersionUID = 1L;

		private final String mode; // "buy" or "sell"
		private List<ShopItem> allItems = new ArrayList<>();
		private List<ShopItem> displayedItems = new ArrayList<>();

		private JTextField searchField;
		private JTable itemTable;
		private ItemTableModel tableModel;

		// Detail Panel Components
		private ShopItem selectedItem;
		private JPanel detailPanel;
		private JLabel itemIconLabel;
		private JLabel itemNameLabel;
		private JLabel unitPriceLabel;
		private JTextField quantityField;
		private JSlider quantitySlider;
		private JLabel totalCostLabel;
		private JButton actionButton;

		private boolean updatingQuantity = false;

		public ShopPanel(final String mode) {
			this.mode = mode;
			setOpaque(true);
			setBackground(bgColor);
			setLayout(new BorderLayout(10, 10));
			setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			add(createLeftPanel(), BorderLayout.CENTER);
			add(createRightPanel(), BorderLayout.EAST);
		}

		public void setItems(final List<ShopItem> items) {
			this.allItems = new ArrayList<>(items);
			applyFilter(searchField != null ? searchField.getText() : "");
			if (!displayedItems.isEmpty()) {
				itemTable.setRowSelectionInterval(0, 0);
				selectItem(displayedItems.get(0));
			} else {
				selectItem(null);
			}
		}

		private JPanel createLeftPanel() {
			final JPanel panel = new JPanel(new BorderLayout(5, 5));
			panel.setOpaque(false);

			// Search Box at Top
			final JPanel searchBox = new JPanel(new BorderLayout(5, 5));
			searchBox.setOpaque(false);
			searchBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
			final JLabel searchLabel = new JLabel("Search: ");
			searchLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
			searchLabel.setForeground(fgColor);
			searchField = new JTextField();
			searchField.setBackground(highlightColor);
			searchField.setForeground(fgColor);
			searchField.setCaretColor(fgColor);
			searchField.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void insertUpdate(final DocumentEvent e) { applyFilter(searchField.getText()); }
				@Override
				public void removeUpdate(final DocumentEvent e) { applyFilter(searchField.getText()); }
				@Override
				public void changedUpdate(final DocumentEvent e) { applyFilter(searchField.getText()); }
			});
			searchBox.add(searchLabel, BorderLayout.WEST);
			searchBox.add(searchField, BorderLayout.CENTER);

			// Items Table
			tableModel = new ItemTableModel();
			itemTable = new JTable(tableModel);
			itemTable.setOpaque(false);
			itemTable.setBackground(bgColor);
			itemTable.setForeground(fgColor);
			itemTable.setSelectionBackground(highlightColor);
			itemTable.setSelectionForeground(fgColor);
			itemTable.setGridColor(new Color(90, 60, 40));
			itemTable.setShowGrid(true);
			itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			itemTable.setRowHeight(36);
			itemTable.getTableHeader().setReorderingAllowed(false);
			itemTable.getTableHeader().setBackground(highlightColor);
			itemTable.getTableHeader().setForeground(fgColor);
			itemTable.getTableHeader().setFont(itemTable.getTableHeader().getFont().deriveFont(Font.BOLD));

			itemTable.getColumnModel().getColumn(0).setPreferredWidth(42);
			itemTable.getColumnModel().getColumn(0).setMaxWidth(50);
			itemTable.getColumnModel().getColumn(0).setCellRenderer(new IconCellRenderer());

			itemTable.getColumnModel().getColumn(1).setPreferredWidth(170);
			itemTable.getColumnModel().getColumn(1).setCellRenderer(new NameCellRenderer());

			itemTable.getColumnModel().getColumn(2).setPreferredWidth(80);
			itemTable.getColumnModel().getColumn(2).setCellRenderer(new PriceCellRenderer());

			itemTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(final ListSelectionEvent e) {
					if (!e.getValueIsAdjusting()) {
						final int row = itemTable.getSelectedRow();
						if (row >= 0 && row < displayedItems.size()) {
							selectItem(displayedItems.get(row));
						}
					}
				}
			});

			final JScrollPane scrollPane = new JScrollPane(itemTable);
			scrollPane.setPreferredSize(new Dimension(320, 300));
			scrollPane.setOpaque(false);
			scrollPane.getViewport().setOpaque(true);
			scrollPane.getViewport().setBackground(bgColor);
			if (borderDown != null) {
				scrollPane.setBorder(borderDown);
			}

			panel.add(searchBox, BorderLayout.NORTH);
			panel.add(scrollPane, BorderLayout.CENTER);
			return panel;
		}

		private JPanel createRightPanel() {
			detailPanel = new JPanel();
			detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
			detailPanel.setPreferredSize(new Dimension(250, 320));
			detailPanel.setOpaque(true);
			detailPanel.setBackground(bgColor);
			detailPanel.setBorder(BorderFactory.createCompoundBorder(
				borderDown != null ? borderDown : BorderFactory.createLineBorder(new Color(80, 50, 30), 1),
				BorderFactory.createEmptyBorder(12, 12, 12, 12)
			));

			// Item Icon
			itemIconLabel = new JLabel() {
				private static final long serialVersionUID = 1L;
				@Override
				protected void paintComponent(final Graphics g) {
					super.paintComponent(g);
					if (selectedItem != null) {
						final Sprite sprite = getItemSprite(selectedItem.getClassPath(), selectedItem.getName());
						if (sprite != null) {
							final int ix = (getWidth() - 32) / 2;
							final int iy = (getHeight() - 32) / 2;
							sprite.draw(g, ix, iy);
						}
					}
				}
			};
			itemIconLabel.setPreferredSize(new Dimension(48, 48));
			itemIconLabel.setMaximumSize(new Dimension(48, 48));
			itemIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

			// Item Name
			itemNameLabel = new JLabel("Select an Item", SwingConstants.CENTER);
			itemNameLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
			itemNameLabel.setForeground(fgColor);
			itemNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

			// Unit Price
			unitPriceLabel = new JLabel("Price: -", SwingConstants.CENTER);
			unitPriceLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
			unitPriceLabel.setForeground(fgColor);
			unitPriceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

			// Quantity Section
			final JPanel qtyBox = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
			qtyBox.setOpaque(false);
			final JLabel qtyLabel = new JLabel("Amount:");
			qtyLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
			qtyLabel.setForeground(fgColor);
			quantityField = new JTextField("1", 5);
			quantityField.setHorizontalAlignment(JTextField.CENTER);
			quantityField.setBackground(highlightColor);
			quantityField.setForeground(fgColor);
			quantityField.setCaretColor(fgColor);
			quantityField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(final KeyEvent e) {
					onManualQuantityInput();
				}
			});
			qtyBox.add(qtyLabel);
			qtyBox.add(quantityField);
			qtyBox.setAlignmentX(Component.CENTER_ALIGNMENT);

			// Slider (1 to 1000)
			quantitySlider = new JSlider(1, 1000, 1);
			quantitySlider.setOpaque(false);
			quantitySlider.setBackground(bgColor);
			quantitySlider.setForeground(fgColor);
			quantitySlider.setPaintTicks(false);
			quantitySlider.setPaintLabels(false);
			quantitySlider.setAlignmentX(Component.CENTER_ALIGNMENT);
			quantitySlider.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(final ChangeEvent e) {
					if (!updatingQuantity) {
						updatingQuantity = true;
						quantityField.setText(String.valueOf(quantitySlider.getValue()));
						updateCalculations();
						updatingQuantity = false;
					}
				}
			});

			// Quick Quantity Buttons
			final JPanel quickBtnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 2));
			quickBtnRow.setOpaque(false);
			final int[] amounts = {1, 5, 10, 50, 100, 1000};
			for (final int amt : amounts) {
				final JButton btn = new JButton(amt == 1000 ? "Max" : String.valueOf(amt));
				btn.setFont(new Font("SansSerif", Font.PLAIN, 10));
				btn.setMargin(new java.awt.Insets(1, 4, 1, 4));
				btn.setBackground(highlightColor);
				btn.setForeground(fgColor);
				btn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(final ActionEvent e) {
						setQuantity(amt);
					}
				});
				quickBtnRow.add(btn);
			}
			quickBtnRow.setAlignmentX(Component.CENTER_ALIGNMENT);

			// Total Price Label
			totalCostLabel = new JLabel("Total: 0 gold", SwingConstants.CENTER);
			totalCostLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
			totalCostLabel.setForeground(fgColor);
			totalCostLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

			// Action Button (Buy / Sell)
			actionButton = new JButton("buy".equals(mode) ? "Buy" : "Sell");
			actionButton.setFont(new Font("SansSerif", Font.BOLD, 12));
			actionButton.setBackground(new Color(34, 139, 34));
			actionButton.setForeground(Color.WHITE);
			actionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			actionButton.setMaximumSize(new Dimension(200, 32));
			actionButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					executeTrade();
				}
			});

			detailPanel.add(Box.createVerticalStrut(5));
			detailPanel.add(itemIconLabel);
			detailPanel.add(Box.createVerticalStrut(5));
			detailPanel.add(itemNameLabel);
			detailPanel.add(Box.createVerticalStrut(3));
			detailPanel.add(unitPriceLabel);
			detailPanel.add(Box.createVerticalStrut(10));
			detailPanel.add(qtyBox);
			detailPanel.add(Box.createVerticalStrut(2));
			detailPanel.add(quantitySlider);
			detailPanel.add(Box.createVerticalStrut(2));
			detailPanel.add(quickBtnRow);
			detailPanel.add(Box.createVerticalStrut(15));
			detailPanel.add(totalCostLabel);
			detailPanel.add(Box.createVerticalStrut(8));
			detailPanel.add(actionButton);
			detailPanel.add(Box.createVerticalGlue());

			return detailPanel;
		}

		private void applyFilter(final String query) {
			final String clean = query != null ? query.trim().toLowerCase() : "";
			displayedItems.clear();
			for (final ShopItem item : allItems) {
				if (clean.isEmpty() || item.getName().toLowerCase().contains(clean)) {
					displayedItems.add(item);
				}
			}
			tableModel.fireTableDataChanged();
		}

		private void selectItem(final ShopItem item) {
			this.selectedItem = item;
			if (item == null) {
				itemNameLabel.setText("Select an Item");
				unitPriceLabel.setText("Price: -");
				totalCostLabel.setText("Total: 0 gold");
				actionButton.setEnabled(false);
				itemIconLabel.repaint();
				return;
			}

			itemNameLabel.setText(capitalize(item.getName()));
			unitPriceLabel.setText("Unit Price: " + formatPrice(item.getPrice()));
			actionButton.setEnabled(true);
			setQuantity(1);
			itemIconLabel.repaint();
		}

		private void setQuantity(final int qty) {
			final int clamped = Math.max(1, Math.min(1000, qty));
			updatingQuantity = true;
			quantityField.setText(String.valueOf(clamped));
			quantitySlider.setValue(clamped);
			updateCalculations();
			updatingQuantity = false;
		}

		private void onManualQuantityInput() {
			if (updatingQuantity) return;
			try {
				final String text = quantityField.getText().trim();
				if (!text.isEmpty()) {
					int val = Integer.parseInt(text);
					val = Math.max(1, Math.min(1000, val));
					updatingQuantity = true;
					quantitySlider.setValue(val);
					updateCalculations();
					updatingQuantity = false;
				}
			} catch (final NumberFormatException ignored) {
			}
		}

		private void updateCalculations() {
			if (selectedItem == null) return;
			int qty = 1;
			try {
				qty = Integer.parseInt(quantityField.getText().trim());
			} catch (final Exception e) {
				qty = quantitySlider.getValue();
			}
			final long total = (long) selectedItem.getPrice() * qty;

			if ("buy".equals(mode)) {
				totalCostLabel.setText("Total: " + formatPrice(total));
				actionButton.setText("Buy (" + formatPrice(total) + ")");
			} else {
				totalCostLabel.setText("Total Reward: +" + formatPrice(total));
				actionButton.setText("Sell (+" + formatPrice(total) + ")");
			}
		}

		private void executeTrade() {
			if (selectedItem == null || npcName == null) return;
			int qty = 1;
			try {
				qty = Integer.parseInt(quantityField.getText().trim());
			} catch (final Exception e) {
				qty = quantitySlider.getValue();
			}
			qty = Math.max(1, Math.min(1000, qty));

			NpcShopController.get().sendTrade(npcName, mode, selectedItem.getName(), qty);
		}

		private String formatPrice(final long amount) {
			return String.format("%,d", amount) + " gold";
		}

		private String capitalize(final String s) {
			if (s == null || s.isEmpty()) return "";
			return Character.toUpperCase(s.charAt(0)) + s.substring(1);
		}

		private class ItemTableModel extends AbstractTableModel {
			private static final long serialVersionUID = 1L;
			private final String[] columns = {"", "Item", "Price"};

			@Override
			public int getRowCount() {
				return displayedItems.size();
			}

			@Override
			public int getColumnCount() {
				return columns.length;
			}

			@Override
			public String getColumnName(final int col) {
				return columns[col];
			}

			@Override
			public Object getValueAt(final int rowIndex, final int columnIndex) {
				if (rowIndex >= displayedItems.size()) return null;
				final ShopItem item = displayedItems.get(rowIndex);
				switch (columnIndex) {
				case 0:
					return item;
				case 1:
					return capitalize(item.getName());
				case 2:
					return formatPrice(item.getPrice());
				default:
					return null;
				}
			}
		}

		private class IconCellRenderer extends DefaultTableCellRenderer {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(final JTable table, final Object value,
					final boolean isSelected, final boolean hasFocus, final int row, final int column) {
				final JLabel label = (JLabel) super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
				label.setOpaque(true);
				label.setBackground(isSelected ? highlightColor : bgColor);
				label.setForeground(fgColor);
				final ShopItem item = (value instanceof ShopItem) ? (ShopItem) value : ((row >= 0 && row < displayedItems.size()) ? displayedItems.get(row) : null);
				final Sprite sprite = item != null ? getItemSprite(item.getClassPath(), item.getName()) : getItemSprite(null, null);
				label.setIcon(new javax.swing.Icon() {
					@Override
					public void paintIcon(final Component c, final Graphics g, final int x, final int y) {
						if (sprite != null) {
							sprite.draw(g, x, y);
						}
					}
					@Override
					public int getIconWidth() {
						return 32;
					}
					@Override
					public int getIconHeight() {
						return 32;
					}
				});
				label.setHorizontalAlignment(SwingConstants.CENTER);
				return label;
			}
		}

		private class NameCellRenderer extends DefaultTableCellRenderer {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(final JTable table, final Object value,
					final boolean isSelected, final boolean hasFocus, final int row, final int column) {
				final JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				label.setOpaque(true);
				label.setBackground(isSelected ? highlightColor : bgColor);
				label.setForeground(fgColor);
				return label;
			}
		}

		private class PriceCellRenderer extends DefaultTableCellRenderer {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(final JTable table, final Object value,
					final boolean isSelected, final boolean hasFocus, final int row, final int column) {
				final JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				label.setOpaque(true);
				label.setBackground(isSelected ? highlightColor : bgColor);
				label.setForeground(fgColor);
				label.setHorizontalAlignment(SwingConstants.RIGHT);
				return label;
			}
		}
	}
}
