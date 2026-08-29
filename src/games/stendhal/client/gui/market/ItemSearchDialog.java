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
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import games.stendhal.client.gui.styled.Style;
import games.stendhal.client.gui.styled.StyleUtil;
import games.stendhal.client.sprite.Sprite;

/**
 * Real-time searchable modal item picker dialog for Harold's Trading Center.
 */
public class ItemSearchDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	public static class ItemEntry implements Comparable<ItemEntry> {
		public final String displayName;
		public final String rawName;
		public final String category;
		public final String spritePath;

		public ItemEntry(String rawName, String category, String spritePath) {
			this.rawName = rawName;
			this.displayName = capitalizeWords(rawName.replace("_", " ").replace("-", " "));
			this.category = capitalizeWords(category.replace("_", " ").replace("/", " > "));
			this.spritePath = spritePath;
		}

		private static String capitalizeWords(String str) {
			if (str == null || str.isEmpty()) return "";
			StringBuilder sb = new StringBuilder();
			for (String s : str.split(" ")) {
				if (!s.isEmpty()) {
					sb.append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).append(" ");
				}
			}
			return sb.toString().trim();
		}

		@Override
		public int compareTo(ItemEntry o) {
			return this.displayName.compareToIgnoreCase(o.displayName);
		}
	}

	@FunctionalInterface
	public interface ItemSelectListener {
		void onItemSelect(String name, String spritePath);
	}

	private static final List<ItemEntry> ALL_ITEMS = new ArrayList<>();
	private static boolean indexDone = false;

	static {
		ensureIndex();
	}

	public static synchronized void ensureIndex() {
		if (indexDone) {
			return;
		}
		indexDone = true;
		final File[] candidates = new File[] {
			new File("data/sprites/items"),
			new File("../data/sprites/items"),
			new File("EmpireOfThorns/data/sprites/items"),
			new File(System.getProperty("user.dir"), "data/sprites/items")
		};
		for (final File candidate : candidates) {
			if (candidate.exists() && candidate.isDirectory()) {
				indexDirectory(candidate, "data/sprites/items", "");
				break;
			}
		}
		Collections.sort(ALL_ITEMS);
	}

	public static String getSpritePath(String itemName) {
		if (itemName == null || itemName.trim().isEmpty()) {
			return null;
		}
		ensureIndex();
		final String clean = itemName.trim().toLowerCase().replace("-", " ").replace("_", " ");
		for (final ItemEntry item : ALL_ITEMS) {
			if (item.displayName.equalsIgnoreCase(clean)
					|| item.rawName.equalsIgnoreCase(clean)
					|| item.rawName.replace("_", " ").equalsIgnoreCase(clean)
					|| item.rawName.replace("-", " ").equalsIgnoreCase(clean)) {
				return item.spritePath;
			}
		}
		return null;
	}

	private static void indexDirectory(File dir, String currentPath, String category) {
		if (!dir.exists() || !dir.isDirectory()) return;
		File[] files = dir.listFiles();
		if (files == null) return;
		for (File f : files) {
			if (f.isDirectory()) {
				String newCat = category.isEmpty() ? f.getName() : category + "/" + f.getName();
				indexDirectory(f, currentPath + "/" + f.getName(), newCat);
			} else if (f.getName().endsWith(".png")) {
				String baseName = f.getName().substring(0, f.getName().length() - 4).toLowerCase();
				String relPath = currentPath + "/" + f.getName();
				ALL_ITEMS.add(new ItemEntry(baseName, category.isEmpty() ? "General" : category, relPath));
			}
		}
	}

	private final ItemSelectListener onItemSelected;
	private final List<ItemEntry> filteredItems = new ArrayList<>();

	private JTextField searchField;
	private JTable itemTable;
	private ItemTableModel tableModel;

	private Color bgColor;
	private Color fgColor;
	private Color highlightColor;

	public ItemSearchDialog(Component parentComponent, ItemSelectListener onItemSelected) {
		super((Frame) SwingUtilities.getWindowAncestor(parentComponent), "Search Item", true);
		this.onItemSelected = onItemSelected;

		initStyle();
		initUI(parentComponent);
		filter("");
	}

	public ItemSearchDialog(Component parentComponent, Consumer<String> onNameSelected) {
		this(parentComponent, (name, path) -> {
			if (onNameSelected != null) {
				onNameSelected.accept(name);
			}
		});
	}

	private void initStyle() {
		Style style = StyleUtil.getStyle();
		if (style != null) {
			bgColor = style.getPlainColor();
			fgColor = style.getForeground();
			highlightColor = style.getHighLightColor();
		} else {
			bgColor = new Color(110, 80, 50);
			fgColor = new Color(255, 235, 205);
			highlightColor = new Color(140, 100, 60);
		}
	}

	private void initUI(Component parentComponent) {
		JPanel mainPanel = new JPanel(new BorderLayout(6, 6));
		mainPanel.setBackground(bgColor);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		// Top Search Bar
		JPanel searchPanel = new JPanel(new BorderLayout(6, 6));
		searchPanel.setOpaque(false);

		JLabel label = new JLabel("Type item name to search:");
		label.setForeground(fgColor);
		label.setFont(label.getFont().deriveFont(Font.BOLD));
		searchPanel.add(label, BorderLayout.NORTH);

		searchField = new JTextField();
		searchField.setPreferredSize(new Dimension(300, 28));
		searchField.setBackground(highlightColor);
		searchField.setForeground(fgColor);
		searchField.setCaretColor(fgColor);
		searchField.getDocument().addDocumentListener(new DocumentListener() {
			@Override public void insertUpdate(DocumentEvent e) { filter(searchField.getText()); }
			@Override public void removeUpdate(DocumentEvent e) { filter(searchField.getText()); }
			@Override public void changedUpdate(DocumentEvent e) { filter(searchField.getText()); }
		});
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					confirmSelection();
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					itemTable.requestFocus();
					if (itemTable.getRowCount() > 0) {
						itemTable.setRowSelectionInterval(0, 0);
					}
				}
			}
		});
		searchPanel.add(searchField, BorderLayout.CENTER);
		mainPanel.add(searchPanel, BorderLayout.NORTH);

		// Center Item Table
		tableModel = new ItemTableModel();
		itemTable = new JTable(tableModel);
		itemTable.setBackground(bgColor);
		itemTable.setForeground(fgColor);
		itemTable.setSelectionBackground(highlightColor);
		itemTable.setSelectionForeground(fgColor);
		itemTable.setGridColor(new Color(90, 60, 40));
		itemTable.setRowHeight(36);
		itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		itemTable.getTableHeader().setBackground(highlightColor);
		itemTable.getTableHeader().setForeground(fgColor);
		itemTable.getTableHeader().setFont(itemTable.getTableHeader().getFont().deriveFont(Font.BOLD));

		itemTable.getColumnModel().getColumn(0).setPreferredWidth(45);
		itemTable.getColumnModel().getColumn(1).setPreferredWidth(180);
		itemTable.getColumnModel().getColumn(2).setPreferredWidth(120);

		itemTable.getColumnModel().getColumn(0).setCellRenderer(new ItemSpriteRenderer());

		itemTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					confirmSelection();
				}
			}
		});

		itemTable.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					confirmSelection();
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(itemTable);
		scrollPane.getViewport().setBackground(bgColor);
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		// Bottom Buttons
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
		buttonPanel.setOpaque(false);

		JButton cancelBtn = new JButton("Cancel");
		cancelBtn.setBackground(new Color(178, 34, 34));
		cancelBtn.setForeground(Color.WHITE);
		cancelBtn.addActionListener(e -> dispose());
		buttonPanel.add(cancelBtn);

		JButton confirmBtn = new JButton("Confirm");
		confirmBtn.setFont(confirmBtn.getFont().deriveFont(Font.BOLD));
		confirmBtn.setBackground(new Color(34, 139, 34));
		confirmBtn.setForeground(Color.WHITE);
		confirmBtn.addActionListener(e -> confirmSelection());
		buttonPanel.add(confirmBtn);

		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		setContentPane(mainPanel);
		setSize(420, 460);
		setLocationRelativeTo(parentComponent);
	}

	private void filter(String query) {
		String q = query == null ? "" : query.trim().toLowerCase().replace(" ", "").replace("_", "");
		filteredItems.clear();
		for (ItemEntry item : ALL_ITEMS) {
			String normName = item.displayName.toLowerCase().replace(" ", "");
			String normCat = item.category.toLowerCase().replace(" ", "");
			if (q.isEmpty() || normName.contains(q) || normCat.contains(q)) {
				filteredItems.add(item);
			}
		}
		tableModel.fireTableDataChanged();
		if (!filteredItems.isEmpty()) {
			itemTable.setRowSelectionInterval(0, 0);
		}
	}

	private void confirmSelection() {
		int row = itemTable.getSelectedRow();
		if (row >= 0 && row < filteredItems.size()) {
			ItemEntry selected = filteredItems.get(row);
			if (onItemSelected != null) {
				onItemSelected.onItemSelect(selected.displayName.toLowerCase(), selected.spritePath);
			}
			dispose();
		}
	}

	private class ItemTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		private final String[] headers = { "Icon", "Item Name", "Category" };

		@Override
		public int getRowCount() {
			return filteredItems.size();
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
			if (row >= filteredItems.size()) return "";
			ItemEntry item = filteredItems.get(row);
			switch (col) {
			case 0:
				return item;
			case 1:
				return item.displayName;
			case 2:
				return item.category;
			default:
				return "";
			}
		}
	}

	private class ItemSpriteRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			JLabel label = (JLabel) super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
			label.setIcon(null);
			label.setHorizontalAlignment(CENTER);

			if (value instanceof ItemEntry) {
				ItemEntry item = (ItemEntry) value;
				Sprite sprite = HaroldMarketWindow.getItemSprite(item.spritePath, item.rawName);
				if (sprite != null) {
					final Sprite s = sprite;
					label.setIcon(new javax.swing.Icon() {
						@Override public void paintIcon(Component c, Graphics g, int x, int y) { s.draw(g, x + 2, y + 2); }
						@Override public int getIconWidth() { return 32; }
						@Override public int getIconHeight() { return 32; }
					});
				}
			}
			return label;
		}
	}
}
