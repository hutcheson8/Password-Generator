import java.awt.GridLayout;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class Main {
	char[] characters;
	Random r = new Random();
	JCheckBox[] forceCheckbox = new JCheckBox[4];
	JCheckBox[] useCheckbox = new JCheckBox[4];
	JTextArea[] tx = new JTextArea[4];
	JTextArea length = new JTextArea();
	boolean[] lastforcechk = new boolean[4];
	boolean[] lastusechk = new boolean[4];
	String[] lastchr = new String[4];
	int lastlng;

	public int passLength() {
		try {
			int r = Integer.parseInt(length.getText());
			if (r < 4)
				return 4;
			return r;
		} catch (Exception e) {
			return 10;
		}
	}

	public void revertChanges() {
		for (int x = 0; x < 4; x++) {
			tx[x].setText(lastchr[x]);
			useCheckbox[x].setSelected(lastusechk[x]);
			forceCheckbox[x].setSelected(lastforcechk[x]);
		}
		length.setText(lastlng + "");
	}

	public void revertDefault() {
		tx[0].setText("qwertyuiopasdfghjklzxcvbnm");
		tx[1].setText("QWERTYUIOPASDFGHJKLZXCVBNM");
		tx[2].setText("`-=[]\\;',./~!@#$%^&*()_+{}|:\"<>?");
		tx[3].setText("1234567890");
		for (int x = 0; x < 4; x++) {
			useCheckbox[x].setEnabled(true);
			forceCheckbox[x].setEnabled(true);
		}
		length.setText("10");
	}

	public void updateCharacterList() {
		String s = "";
		for (int x = 0; x < 4; x++) {
			lastforcechk[x] = forceCheckbox[x].isSelected();
			lastusechk[x] = useCheckbox[x].isSelected();
			lastchr[x] = tx[x].getText();
			if (useCheckbox[x].isSelected()) {
				s += tx[x].getText();
			}
		}
		lastlng = passLength();
		characters = s.toCharArray();
	}

	public String newPassword() {
		int numForced = 0;
		for (int x = 0; x < 4; x++) {
			if (forceCheckbox[x].isSelected()) {
				numForced++;
			}
		}
		char[] forcedChars = new char[numForced];
		int forcedIndex = 0;
		for (int x = 0; x < 4; x++) {
			if (forceCheckbox[x].isSelected()) {
				char[] typeChars = tx[x].getText().toCharArray();
				forcedChars[forcedIndex] = typeChars[r.nextInt(typeChars.length)];
				forcedIndex++;
			}
		}
		int[] forcedIndices = new int[numForced];
		for (int x = 0; x < numForced; x++) {
			boolean flag = false;
			while (!flag) {
				flag = true;
				int proposedIndex = r.nextInt(passLength());
				for (int y = 0; y < x; y++) {
					flag = flag && (forcedIndices[y] != proposedIndex);
				}
				forcedIndices[x] = proposedIndex;
			}
		}
		int indexOfForcedIndices = 0;
		String pass = "";
		for (int x = 0; x < passLength(); x++) {
			char nextChar;
			boolean isForced = false;
			for (int y = 0; y < numForced; y++) {
				isForced |= (forcedIndices[y] == x);
			}
			if (isForced) {
				nextChar = forcedChars[indexOfForcedIndices];
				indexOfForcedIndices++;
			} else {
				nextChar = characters[r.nextInt(characters.length)];
			}
			pass += nextChar;
		}
		return pass;
	}

	public Main() {
		JFrame generator = new JFrame("Password Generator");
		generator.setLayout(new GridLayout(1, 3));
		generator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final JFrame options = new JFrame("Options");
		options.setLayout(new GridLayout(4, 4));
		options.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		final JTextArea passwordArea = new JTextArea();
		generator.add(passwordArea);
		{
			JButton generateButton = new JButton("Generate");
			generateButton.addActionListener((e) -> passwordArea.setText(newPassword()));
			generator.add(generateButton);
		}
		{
			JButton optionsButton = new JButton("Options");
			optionsButton.addActionListener((e) -> {
				options.setLocationRelativeTo(null);
				options.setVisible(true);
			});
			generator.add(optionsButton);
		}
		generator.pack();
		generator.setLocationRelativeTo(null);
		generator.setVisible(true);
		for (int x = 0; x < 4; x++) {
			forceCheckbox[x] = new JCheckBox();
			forceCheckbox[x].setSelected(true);
			useCheckbox[x] = new JCheckBox();
			useCheckbox[x].setSelected(true);
			tx[x] = new JTextArea();
		}
		revertDefault();
		useCheckbox[0].setText("Use Lowercase");
		useCheckbox[1].setText("Use Uppercase");
		useCheckbox[2].setText("Use Symbols");
		useCheckbox[3].setText("Use Numbers");
		forceCheckbox[0].setText("Force 1 Lowercase");
		forceCheckbox[1].setText("Force 1 Uppercase");
		forceCheckbox[2].setText("Force 1 Symbol");
		forceCheckbox[3].setText("Force 1 Number");
		updateCharacterList();
		JButton[] b = new JButton[3];
		b[0] = new JButton("OK");
		b[0].addActionListener((e) -> {
			updateCharacterList();
			options.setVisible(false);
		});
		b[1] = new JButton("Default");
		b[1].addActionListener((e) -> revertDefault());
		b[2] = new JButton("Cancel");
		b[2].addActionListener((e) -> {
			revertChanges();
			options.setVisible(false);
		});
		for (int x = 0; x < 3; x++) {
			options.add(forceCheckbox[x]);
			options.add(useCheckbox[x]);
			options.add(tx[x]);
			options.add(b[x]);
		}
		options.add(forceCheckbox[3]);
		options.add(useCheckbox[3]);
		options.add(tx[3]);
		options.add(length);
		options.pack();
	}

	public static void main(String[] args) {
		new Main();
	}
}