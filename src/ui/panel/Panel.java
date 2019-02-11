package ui.panel;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import file.FileOperations;
import game.alive.Enemy;
import game.environment.Map;
import game.util.Log;

public class Panel extends Panel_Ab implements DropTargetListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1190704463563253754L;

	private final int windowX = 540, windowY = 400;
	private File source, destination;
	private JTextField textInput, textOutput;

	private JLabel heroLabel;

	private int heroX = 20; // hero start point 55
	private int enemyQueue;

	private TimerTask animator;

	public Panel() {
		super();
		setLayout(null);
		createView();
		setDropTarget(new DropTarget(this, this));

	}

	@Override
	public void display() {
		JFrame f = new JFrame("Would you die or survive?");

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(windowX, windowY);
		f.add(this);
		f.setLocationRelativeTo(null);
		f.setResizable(false);
		f.setVisible(true);

	}

	@Override
	public void createView() {

		ImageIcon heroImage = new ImageIcon(this.getClass().getResource("/image/hero.gif"));
		ImageIcon homeImage = new ImageIcon(this.getClass().getResource("/image/home.png"));
		ImageIcon containerImage = new ImageIcon(this.getClass().getResource("/image/container.png"));
		ImageIcon groundImage = new ImageIcon(this.getClass().getResource("/image/ground.png"));

		JLabel input = new JLabel("Input : ");
		JLabel output = new JLabel("Output : ");

		textInput = new JTextField();
		textOutput = new JTextField();
		JButton inputChoose = new JButton("Choose");
		JButton outputChoose = new JButton("Choose");
		JButton result = new JButton("Get Result");

		heroLabel = new JLabel(heroImage);
		JLabel homeLabel = new JLabel(homeImage);
		JLabel containerLabel = new JLabel(containerImage);
		JLabel groundLabel = new JLabel(groundImage);

		JFileChooser fileChooser = new JFileChooser();

		textInput.setEditable(false);
		textOutput.setEditable(false);

		input.setBounds(20, 30, input.getPreferredSize().width, input.getPreferredSize().height);
		textInput.setBounds(input.getX() + 50, input.getY(), 350, textInput.getPreferredSize().height);
		inputChoose.setBounds(textInput.getX() + textInput.getWidth() + 20, textInput.getY(),
				inputChoose.getPreferredSize().width, inputChoose.getPreferredSize().height);
		output.setBounds(input.getX(), input.getY() + 40, output.getPreferredSize().width,
				output.getPreferredSize().height);
		textOutput.setBounds(output.getX() + 50, output.getY(), 350, textOutput.getPreferredSize().height);
		outputChoose.setBounds(textOutput.getX() + textOutput.getWidth() + 20, textOutput.getY(),
				outputChoose.getPreferredSize().width, outputChoose.getPreferredSize().height);
		result.setBounds(windowX / 2 - 50, output.getY() + 50, result.getPreferredSize().width,
				result.getPreferredSize().height);

		heroLabel.setBounds(20, 200, heroLabel.getPreferredSize().width, heroLabel.getPreferredSize().height);
		homeLabel.setBounds(-5, 180, homeLabel.getPreferredSize().width, homeLabel.getPreferredSize().height);
		containerLabel.setBounds(500, 200, containerLabel.getPreferredSize().width,
				containerLabel.getPreferredSize().height);
		groundLabel.setBounds(55, 230, groundLabel.getPreferredSize().width, groundLabel.getPreferredSize().height);

		inputChoose.addActionListener(event -> {
			int returnVal = fileChooser.showOpenDialog(inputChoose);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				source = fileChooser.getSelectedFile();
				textInput.setText(fileChooser.getSelectedFile().getAbsolutePath());
			}
		});
		outputChoose.addActionListener(event -> {
			int returnVal = fileChooser.showOpenDialog(inputChoose);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				destination = fileChooser.getSelectedFile();
				textOutput.setText(fileChooser.getSelectedFile().getAbsolutePath());
			}
		});
		result.addActionListener(event -> {
			if (source != null && destination != null) {
				refresh();
				FileOperations fileOperations = new FileOperations();
				Map map = fileOperations.fileRead(source);
				map.startJourney(this);
				new Thread() {
					public void run() {
						super.run();
						fileOperations.fileWrite(destination, Log.getLog());
					}
				}.start();

				startAnimation(fileOperations.fileRead(source));
			}
		});
		add(input);
		add(textInput);
		add(inputChoose);
		add(output);
		add(textOutput);
		add(outputChoose);
		add(result);

		add(homeLabel);
		add(heroLabel);
		add(containerLabel);
		add(groundLabel);

	}

	private void refresh() {
		if (enemyQueue != 0) {
			animator.cancel();
			removeAll();
			enemyQueue = 0;
			heroX = 20;
			Log.clearLog();
			createView();
		}
	}

	private void startAnimation(Map map) {
		// start=55 finish=500
		// hero start=30 finish=475 width=25

		map.sortByPositions();
		ArrayList<Integer> points = drawPoints(map);
		heroWalk(map, points);

	}

	private ArrayList<Integer> drawPoints(Map map) {
		double rate = 445.0 / map.getDistance();
		ImageIcon pointImage = new ImageIcon(this.getClass().getResource("/image/point.png"));
		ArrayList<Integer> points = new ArrayList<>();
		for (Enemy enemy : map.getEnemies()) {
			JLabel pointLabel = new JLabel(pointImage);
			int x = 55 + (int) (rate * enemy.getPosition());
			pointLabel.setBounds(x, 230, pointLabel.getPreferredSize().width, pointLabel.getPreferredSize().height);
			add(pointLabel);
			points.add(x);
		}
		repaint();
		return points;
	}

	private void heroWalk(Map map, ArrayList<Integer> points) {
		ImageIcon zombieImage = new ImageIcon(this.getClass().getResource("/image/zombie.gif"));
		ImageIcon deadImage = new ImageIcon(this.getClass().getResource("/image/dead.png"));

		JLabel heroTitle = new JLabel("Hero info:");
		JLabel heroHP = new JLabel("HP = " + map.getVolunteer().getHealthPoint());
		JLabel heroDamage = new JLabel("Damage = " + map.getVolunteer().getAttackDamage());
		JLabel distance = new JLabel("Distance = " + map.getDistance());
		JLabel enemyTitle = new JLabel("Enemy info:");
		JLabel enemyType = new JLabel("Type = ");
		JLabel enemyHP = new JLabel("HP = ");
		JLabel enemyDamage = new JLabel("Damage = ");
		JLabel enemyPosition = new JLabel("Damage = ");
		JLabel result = new JLabel();

		heroTitle.setBounds(10, 250, 70, 20);
		heroHP.setBounds(10, 270, 80, 20);
		heroDamage.setBounds(10, 290, 90, 20);
		enemyTitle.setBounds(200, 250, 70, 20);
		enemyType.setBounds(200, 270, 110, 20);
		enemyHP.setBounds(200, 290, 80, 20);
		enemyDamage.setBounds(200, 310, 90, 20);
		enemyPosition.setBounds(200, 330, 90, 20);
		result.setBounds(150, 160, 300, 20);
		distance.setBounds(440, 180, distance.getPreferredSize().width, distance.getPreferredSize().height);

		add(heroTitle);
		add(heroHP);
		add(heroDamage);
		add(distance);
		add(enemyTitle);
		add(enemyType);
		add(enemyHP);
		add(enemyDamage);
		add(enemyPosition);
		add(result);

		animator = new TimerTask() {

			@Override
			public void run() {
				if (heroLabel.getX() <= 500 && map.getVolunteer().isAlive()) {
					heroLabel.setLocation(heroX++, 200);

					if (enemyQueue < points.size())
						if (heroLabel.getX() == points.get(enemyQueue) - 20) {					

							JLabel enemyLabel = new JLabel(zombieImage);
							enemyLabel.setBounds(points.get(enemyQueue) - 10, 200, enemyLabel.getPreferredSize().width,
									enemyLabel.getPreferredSize().height);
							add(enemyLabel);

							enemyType.setText("Type = " + map.getEnemies().get(enemyQueue).getType());
							enemyHP.setText("HP = " + map.getEnemies().get(enemyQueue).getHealthPoint());
							enemyDamage.setText("Damage = " + map.getEnemies().get(enemyQueue).getAttackDamage());
							enemyPosition.setText("Position = " + map.getEnemies().get(enemyQueue).getPosition());

							int heroHp = map.getVolunteer().getHealthPoint();
							int enemyHp = map.getEnemies().get(enemyQueue).getHealthPoint();
							int heroDamage = map.getVolunteer().getAttackDamage();
							int enemyDamage = map.getEnemies().get(enemyQueue).getAttackDamage();
							while (heroHp > 0 && enemyHp > 0) {
								heroHp -= enemyDamage;
								enemyHp -= heroDamage;
								try {
									Thread.sleep(300);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if (heroHp <= 0) {
									map.getVolunteer().setAlive(false);
									heroHp = 0;
								}
								if (enemyHp < 0)
									enemyHp = 0;
								heroHP.setText("HP = " + heroHp);
								enemyHP.setText("HP = " + enemyHp);
							}
							if (!map.getVolunteer().isAlive()) {
								heroLabel.setIcon(deadImage);
								result.setText("Hero is Dead!! Last seen at position "
										+ map.getEnemies().get(enemyQueue).getPosition());
							}
							map.getVolunteer().setHealthPoint(heroHp);
							map.getEnemies().get(enemyQueue).setHealthPoint(enemyHp);
							enemyQueue++;

							if (map.getVolunteer().isAlive())
								remove(enemyLabel);
						}
				}

				else {
					if (map.getVolunteer().isAlive()) {
						ImageIcon finish = new ImageIcon(this.getClass().getResource("/image/finish.png"));
						result.setText("Hero Survived!");
						heroLabel.setIcon(finish);
					}

					cancel();
				}

			}
		};
		new Timer().schedule(animator, 500, 50);
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragExit(DropTargetEvent dte) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drop(DropTargetDropEvent dtde) {
		try {
			Transferable tr = dtde.getTransferable();
			DataFlavor[] flavors = tr.getTransferDataFlavors();
			for (int i = 0; i < flavors.length; i++) {
				if (flavors[i].isFlavorJavaFileListType()) {
					dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
					@SuppressWarnings("unchecked")
					List<File> list = (List<File>) tr.getTransferData(flavors[i]);
					for (int j = 0; j < list.size(); j++) {
						source = (File) list.get(0);
						textInput.setText(source.getAbsolutePath());
						destination = new File(source.getParentFile().getAbsolutePath(), "result.txt");
						textOutput.setText(destination.getAbsolutePath());
					}
					dtde.dropComplete(true);
					return;
				}
			}
			dtde.rejectDrop();
		} catch (Exception e) {
			e.printStackTrace();
			dtde.rejectDrop();
		}

	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub

	}

}
