package dBase;

import java.awt.*;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;

import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManageDB {
	private static JdbcTemplate jdbc;

	public void setJdbc(JdbcTemplate jdbc) {
		ManageDB.jdbc = jdbc;
	}

	static String tmpName;
	static String tmpLName;
	static String tmpAdres;
	static String tmpPhone;
	static int tmpDep;
	static int tmpCh;
	static String crit;
	static String arith;
	static String value;
	static String[] arop = { " > ", " >= ", " = ", " =< ", " < " };
	String seg[] = new String[10];
	static JComboBox<String> cmb = new JComboBox<>(arop);
	static String sql;
	static int count;
	static int i;
	static int num;
	static int rowNumber;
	static Frame input = new Frame();
	static Frame ads = new Frame();
	static Frame cros = new Frame();
	static Label label = new Label();
	static TextField tf = new TextField();
	static Button Ok = new Button("Ok");
	static Button adOk = new Button("Ok");
	static Button crOk = new Button("Ok");
	static ButtonGroup bg = new ButtonGroup();
	static JRadioButton[] tfradio = { new JRadioButton("Name = "), new JRadioButton("Last name = "),
			new JRadioButton("Adres = "), new JRadioButton("Phone Number = "), new JRadioButton("Deposit"),
			new JRadioButton("Charge"), new JRadioButton("Difference") };
	static JRadioButton[] crossRadio = { new JRadioButton("Name"), new JRadioButton("Last name"),
			new JRadioButton("Adres"), new JRadioButton("Phone Number"), new JRadioButton("Deposit"),
			new JRadioButton("Charge (customers)"), new JRadioButton("Difference"), new JRadioButton("ID"),
			new JRadioButton("Department"), new JRadioButton("Charge") };
	static JTextField tfinput = new JTextField();
	static String copy;
	static int rowCopy;
	static int a=0;
	static boolean nonSelect;
	ManageDB() {
		count = 0;
		Ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					rowCopy=MainFrame.table.getSelectedRow() + 1;
					if (rowCopy!=0) a=rowCopy;
				if (MainFrame.action < 3) {			
					switch (count) {
					case 0:
						tmpName = tf.getText();
						tf.setText("");
						label.setText("Enter last name: ");
						break;
					case 1:
						tmpLName = tf.getText();
						tf.setText("");
						label.setText("Enter adres: ");
						break;
					case 2:
						tmpAdres = tf.getText();
						tf.setText("");
						label.setText("Enter phone number: ");
						break;
					case 3:
						tmpPhone = tf.getText();
						tf.setText("");
						label.setText("Enter deposit: ");
						break;
					case 4:
						if (tf.getText().equals("")) {
							tmpDep = 0;
						} else {
							tmpDep = Integer.valueOf(tf.getText());
						}
						tf.setText("");
						label.setText("Enter charge: ");
						break;
					case 5:
						if (MainFrame.action == 1) {
							if (tf.getText().equals("")) {
								tmpCh = 0;
							} else {
								tmpCh = Integer.valueOf(tf.getText());
							}
							jdbc.execute("insert into customers values(?,?,?,?,?,?,0);",
									new PreparedStatementCallback<Object>() {

										@Override
										public Object doInPreparedStatement(PreparedStatement ps)
												throws SQLException, DataAccessException {
											ps.setObject(1, tmpName);
											ps.setObject(2, tmpLName);
											ps.setObject(3, tmpAdres);
											ps.setObject(4, tmpPhone);
											ps.setObject(5, tmpDep);
											ps.setObject(6, tmpCh);
											ps.execute();
											return null;
										}

									});
						}
						if (MainFrame.action == 2) {
							if (tf.getText().equals("")) {
								tmpCh = 0;
							} else {
								tmpCh = Integer.valueOf(tf.getText());
							}
							jdbc.execute(
									"update customers set name = ?,lastname = ?,adres = ?,phnum = ?,deposit = ?,topay = ? where rowid = ?;",
									new PreparedStatementCallback<Object>() {

										@Override
										public Object doInPreparedStatement(PreparedStatement ps)
												throws SQLException, DataAccessException {
											ps.setObject(1, tmpName);
											ps.setObject(2, tmpLName);
											ps.setObject(3, tmpAdres);
											ps.setObject(4, tmpPhone);
											ps.setObject(5, tmpDep);
											ps.setObject(6, tmpCh);
											ps.setObject(7, a);
											ps.execute();
											return null;
										}

									});

						}
						retrieveData();
						input.dispose();
						MainFrame.setButtons();
						break;

					}
					count++;
				} else {/// action-3
					jdbc.execute(copy, new PreparedStatementCallback<Object>() {
						@Override
						public Object doInPreparedStatement(PreparedStatement ps)
								throws SQLException, DataAccessException {
							if (MainFrame.table.getSelectedColumn() + 1 < 6) {
								ps.setObject(1, tf.getText());
							} else {
								if (tf.getText().equals("")) {
									ps.setObject(1, "0");
								} else {
									ps.setObject(1, Integer.valueOf(tf.getText()));
								}
							}
							ps.setObject(2, a);
							ps.execute();							
							return null;
						}
					});					
					retrieveData();
					input.dispose();
					MainFrame.setButtons();				
				}
				}catch(NumberFormatException e) {
				FormEx();				
				}
			}	
		});

		adOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				nonSelect=true;
				MainFrame.model.setRowCount(0);
				if (num < 4) {
					value = "'" + tfinput.getText() + "'";
				} else {
					if (tfinput.getText().equals("")) {
						value = "0";
					} else {
						value = tfinput.getText();
					}

				}
				jdbc.query("select * from customers where " + crit + arith + value + ";", new RowMapper<Object>() {

					@Override
					public Object mapRow(ResultSet rs, int rn) throws SQLException {
						String[] data = new String[8];
						data[0] = String.valueOf(MainFrame.model.getRowCount() + 1);
						data[1] = rs.getString(1);
						data[2] = rs.getString(2);
						data[3] = rs.getString(3);
						data[4] = rs.getString(4);
						data[5] = rs.getString(5);
						data[6] = String.valueOf(rs.getInt(6));
						data[7] = String.valueOf(rs.getInt(7));
						MainFrame.model.addRow(data);
						return null;
					}

				});
				ads.setVisible(false);
				MainFrame.setButtons();
				MainFrame.jp.setVisible(true);
				MainFrame.jp2.setVisible(false);
			}
		});
		crOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nonSelect=true;
				MainFrame.model2.setColumnCount(0);
				MainFrame.model2.addColumn("ID");
				MainFrame.model2.setRowCount(0);
				int counter = 0;
				sql = "Select ";
				for (i = 0; i < seg.length; i++) {
					if (seg[i] != null) {
						if (counter == 0) {
							sql += seg[i];
						} else {
							sql += "," + seg[i];
						}
						counter++;
						MainFrame.model2.addColumn(seg[i]);
					}
				}
				sql += " from customers cross join dep;";
				cros.dispose();
				MainFrame.jp.setVisible(false);
				MainFrame.jp2.setVisible(true);
				MainFrame.jp2.setBounds(170, 100, 580, 350);
				int cpcounter = counter;
				rowNumber = 1;
				jdbc.query(sql, new RowMapper<Object>() {

					@Override
					public Object mapRow(ResultSet rs, int rn) throws SQLException {
						String[] data = new String[cpcounter + 1];

						data[0] = String.valueOf(rowNumber);
						for (int i = 1; i < data.length; i++) {
							data[i] = rs.getString(i);
						}
						MainFrame.model2.addRow(data);
						rowNumber++;
						return null;
					}

				});
			}

		});
		for (i = 0; i < tfradio.length; i++) {
			tfradio[i].addActionListener(new ActionListener() {
				int j = i;

				public void actionPerformed(ActionEvent e) {
					switch (j) {
					case 0:
						crit = "name";
						tfinput.setBounds(200, 65, 100, 20);
						break;
					case 1:
						crit = "lastname";
						tfinput.setBounds(200, 85, 100, 20);
						break;
					case 2:
						crit = "adres";
						tfinput.setBounds(200, 105, 100, 20);
						break;
					case 3:
						crit = "phnum";
						tfinput.setBounds(200, 125, 100, 20);
						break;
					case 4:
						crit = "deposit";
						tfinput.setBounds(200, 145, 100, 20);
						cmb.setBounds(145, 145, 50, 20);
						break;
					case 5:
						crit = "topay";
						tfinput.setBounds(200, 165, 100, 20);
						cmb.setBounds(145, 165, 50, 20);
						break;
					case 6:
						crit = "diff";
						tfinput.setBounds(200, 185, 100, 20);
						cmb.setBounds(145, 185, 50, 20);
						break;
					}
					num = j;
					if (j >= 4) {
						arith = cmb.getSelectedItem().toString();
						cmb.setEnabled(true);
						cmb.setVisible(true);
					} else {
						arith = " = ";
						cmb.setEnabled(false);
						cmb.setVisible(false);
					}
					tfinput.setText("");
					tfinput.setVisible(true);
					tfinput.setEnabled(true);
					adOk.setEnabled(true);
				}
			});
		}
		for (i = 0; i < crossRadio.length; i++) {
			crossRadio[i].addActionListener(new ActionListener() {
				int j = i;

				public void actionPerformed(ActionEvent arg0) {
					if (crossRadio[j].isSelected()) {
						switch (j) {
						case 0:
							seg[j] = "name";
							break;
						case 1:
							seg[j] = "lastname";
							break;
						case 2:
							seg[j] = "adres";
							break;
						case 3:
							seg[j] = "phnum";
							break;
						case 4:
							seg[j] = "deposit";
							break;
						case 5:
							seg[j] = "topay";
							break;
						case 6:
							seg[j] = "diff";
							break;
						case 7:
							seg[j] = "ID";
							break;
						case 8:
							seg[j] = "dept";
							break;
						case 9:
							seg[j] = "charge";
							break;
						}
					} else {
						seg[j] = null;
					}
					int radcount = 0;
					for (int k = 0; k < crossRadio.length; k++) {
						if (crossRadio[k].isSelected()) {
							radcount++;
						}
						if (radcount > 0) {
							crOk.setEnabled(true);
						} else {
							crOk.setEnabled(false);
						}
					}
				}
			});
		}

	}//ManageDB consttuctor

	public static void retrieveData() {
		
		MainFrame.jp.setVisible(true);
		MainFrame.jp2.setVisible(false);
		MainFrame.model.setRowCount(0);
		String[] data = new String[8];
		jdbc.query("select * from customers", new RowMapper<Object>() {

			@Override
			public Object mapRow(ResultSet rs, int rn) throws SQLException {
				data[0] = String.valueOf(MainFrame.model.getRowCount() + 1);
				data[1] = rs.getString(1);
				data[2] = rs.getString(2);
				data[3] = rs.getString(3);
				data[4] = rs.getString(4);
				data[5] = rs.getString(5);
				data[6] = String.valueOf(rs.getInt(6));
				data[7] = String.valueOf(rs.getInt(7));
				MainFrame.model.addRow(data);
				nonSelect=false;
				return null;
			}
		});
		MainFrame.setButtons();
	}

	public static void retrieveDep() {
		MainFrame.setTableDep();
		MainFrame.jp.setVisible(false);
		MainFrame.jp2.setVisible(true);
		MainFrame.model2.setRowCount(0);
		MainFrame.jp2.setBounds(170, 100, 580, 87);
		MainFrame.setButtons();
		String[] data = new String[3];
		jdbc.query("select * from dep", new RowMapper<Object>() {

			@Override
			public Object mapRow(ResultSet rs, int rn) throws SQLException {
				data[0] = String.valueOf(rs.getInt(1));
				data[1] = rs.getString(2);
				data[2] = String.valueOf(rs.getInt(3));
				MainFrame.model2.addRow(data);
				return null;
			}
		});

	}

	public static void displayDBox() {
		input.setLayout(null);
		label.setBounds(50, 30, 120, 25);
		tf.setBounds(50, 60, 100, 20);
		tf.setText("");
		Ok.setVisible(false);
		Ok.setEnabled(false);
		Ok.setBounds(80, 100, 40, 25);
		input.setSize(200, 140);
		input.setLocation(550, 400);
		input.setBackground(Login.c);
		input.setVisible(true);
		input.add(Ok);
		input.add(label);
		input.add(tf);
		input.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				input.dispose();
				count = 0;
			}
		});
	}

	public static void addData() {
		label.setText("Enter name: ");
		displayDBox();
		Ok.setEnabled(true);
		Ok.setVisible(true);
		count = 0;

	}

	public static void modifyData() {
		label.setText("Enter name: ");
		displayDBox();
		Ok.setEnabled(true);
		Ok.setVisible(true);
		count = 0;

	}

	public static void modifyField() {
		displayDBox();
		Ok.setVisible(true);
		Ok.setEnabled(true);
		String sql = new String();
		switch (MainFrame.table.getSelectedColumn()) {
		case 1:
			label.setText("Enter name: ");
			sql = "Update customers set name = ? where rowid = ?;";
			break;
		case 2:
			label.setText("Enter  last name: ");
			sql = "Update customers set lastname = ? where rowid = ?;";
			break;
		case 3:
			label.setText("Enter adres: ");
			sql = "Update customers set adres = ? where rowid = ?;";
			break;
		case 4:
			label.setText("Enter phone number: ");
			sql = "Update customers set phnum = ? where rowid = ?;";
			break;
		case 5:
			label.setText("Enter deposit: ");
			sql = "Update customers set deposit = ? where rowid = ?;";
			break;
		case 6:
			label.setText("Enter charge: ");
			sql = "Update customers set topay = ? where rowid = ?;";
			break;
		}
		copy = sql;

	}

	public static void deleteData() {
		jdbc.update("Delete from customers where rowid = " + String.valueOf(MainFrame.table.getSelectedRow() + 1));
		jdbc.update("vacuum;");
		retrieveData();
	}

	public static void advancedSearch() {
		ads.setLayout(null);
		ads.setSize(350, 280);
		ads.setLocation(525, 370);
		ads.setBackground(Login.c);
		ads.setVisible(true);
		cmb.setBounds(145, 165, 50, 20);
		cmb.setSelectedItem(0);
		cmb.setEnabled(false);
		cmb.setVisible(false);
		tfinput.setText("");
		tfinput.setEnabled(false);
		tfinput.setVisible(false);
		tfinput.setBounds(200, 65, 100, 20);
		ads.add(cmb);
		ads.add(tfinput);
		int y = 65;
		int len = 130;
		for (i = 0; i < tfradio.length; i++) {
			if (i >= 4) {
				len = 90;
			}
			tfradio[i].setBounds(50, y, len, 20);
			tfradio[i].setSelected(false);
			tfradio[i].setBackground(Login.c);
			bg.add(tfradio[i]);

			ads.add(tfradio[i]);
			y += 20;
		}
		cmb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				arith = cmb.getSelectedItem().toString();
			}
		});
		label.setText("Choose criterium Field");
		label.setBounds(55, 35, 200, 20);
		adOk.setBounds(105, 220, 40, 25);
		adOk.setEnabled(false);
		ads.add(adOk);
		ads.add(label);
		ads.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				ads.dispose();
			}
		});
	}

	public static void crossJoin() {
		cros.setLayout(null);
		cros.setSize(300, 340);
		cros.setLocation(550, 370);
		cros.setBackground(Login.c);
		cros.setVisible(true);
		crOk.setBounds(125, 280, 40, 25);
		crOk.setEnabled(false);
		int y = 60;
		for (int i = 0; i < crossRadio.length; i++) {
			crossRadio[i].setBounds(80, y, 150, 20);
			crossRadio[i].setBackground(Login.c);
			cros.add(crossRadio[i]);
			y += 20;
		}
		cros.add(crOk);
		cros.add(label);
		label.setText("Choose column(s):");
		label.setBounds(85, 35, 200, 20);
		cros.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				cros.dispose();
			}
		});

	}
	public static void FormEx() {
		Frame incorrect = new Frame();
		incorrect.setLayout(null);
		incorrect.setBackground(Login.c);
		Button close = new Button("Ok");
		close.setBounds(75, 100, 50, 30);
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				incorrect.dispose();
			}
		});
		Label msg = new Label("Please enter a number.");
		msg.setBounds(25,40,150,20);
		incorrect.setSize(200, 150);
		incorrect.setLocation(550, 400);
		incorrect.setVisible(true);
		incorrect.add(msg);
		incorrect.add(close);					
		incorrect.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				incorrect.dispose();
			}
		});	
	}

}
