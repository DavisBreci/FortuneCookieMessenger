import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.io.*;
import java.net.URL;
import java.nio.channels.*;
import java.util.*;
import javax.swing.*;

public class GUI implements ActionListener {
	static String basePath;
	static String lastAccessed;
	static JFrame frame;
	static JPanel panel;
	static JButton generateButton = new JButton(new AbstractAction("Get My Daily Message") {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(canUpdate()) {
				messageLabel.setText(message);
//				errorLabel.setText("");
				errorLabel.setVisible(false);
			}
		}
	});
	static JButton saveMessageButton = new JButton(new AbstractAction("<html><center>Save This Message to<br>Favorites</center></html>") {
		@Override
		public void actionPerformed(ActionEvent e){
			if(message!=null) {
				savedMessages.add(0,message);
				if(!savedMessages.get(5).equals(""))
					affirmationsList.set(currentMessageIndex, savedMessages.get(5));
				else
					affirmationsList.remove(currentMessageIndex);
				updateMemory();
				message = null;
				savedMessagesList.setText("Saved Messages:\n");
				for(int i = 0; i<5; i++)
					savedMessagesList.setText(savedMessagesList.getText()+"\n"+displayedSavedMessages[i]);
			}else if(!mostRecentMessage.equals(savedMessages.get(0))) {
				savedMessages.add(0,mostRecentMessage);
				if(!savedMessages.get(5).equals(""))
					affirmationsList.set(currentMessageIndex, savedMessages.get(5));
				else
					affirmationsList.remove(currentMessageIndex);
				updateMemory();
			}
		}
	});
	static JTextArea savedMessagesList;
	
	static JTextArea messageLabel;
	static JLabel errorLabel;
	static ArrayList<String> affirmationsList;
	static String message;
	static ArrayList<String> savedMessages;
	static String[] displayedSavedMessages;
	static String mostRecentMessage;
	static int currentMessageIndex;

	public GUI(){
		frame = new JFrame("Fortune Cookie Message Generator");
		panel = new JPanel();
		messageLabel =  new JTextArea(mostRecentMessage);
		errorLabel = new JLabel("sorry, you'll have to wait until tomorrow");
		errorLabel.setOpaque(true);
		errorLabel.setBackground(new Color(255,255,0));
		errorLabel.setVisible(false);
		
		savedMessagesList = new JTextArea("Saved Messages:\n");
		savedMessagesList.setLineWrap(true);
		savedMessagesList.setBackground(new Color(166,222,222));
		savedMessagesList.setEditable(false);
		for(int i = 0; i<5; i++)
			savedMessagesList.setText(savedMessagesList.getText()+"\n"+displayedSavedMessages[i]+"\n");
		
		saveMessageButton.setBackground(new Color(158,209,165));
        
        generateButton.setBackground(new Color(158,209,165));
        
        messageLabel.setLineWrap(true);
		messageLabel.setEditable(false);
		messageLabel.setFont(new Font("serif", Font.PLAIN, 30));
		messageLabel.setOpaque(false);
		
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		panel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
		panel.setLayout(new BorderLayout());
		panel.setBackground(new Color(166,222,222));
		
		panel.add(errorLabel, BorderLayout.NORTH);
		panel.add(generateButton, BorderLayout.WEST);
		panel.add(new JScrollPane(messageLabel), BorderLayout.CENTER);
		panel.add(saveMessageButton, BorderLayout.EAST);
		panel.add(savedMessagesList, BorderLayout.SOUTH);
		
		frame.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(1000,600));
		frame.pack();
		frame.setVisible(true);
	}
	public static void main(String[] args) throws Exception {
		basePath = "";																			//	works for IDE
//		basePath = System.getProperty("user.dir") + "\\Anniversary\\Required_Files\\";			//	works for .jar file
//		basePath = "Required_Files\\";															//	works for .exe file
		getFile();
		try {
			Scanner fileReader = new Scanner(new File(basePath+"messages.txt"));
			affirmationsList = new ArrayList<String>();
			while(fileReader.hasNextLine()) {
				affirmationsList.add(fileReader.nextLine());
			}
			fileReader.close();
			PrintWriter pw = new PrintWriter(new FileOutputStream(new File(basePath+"messages.txt")));
			pw.println("no looking ;)");
			pw.close();
			Scanner fileReader2 = new Scanner(new File(basePath+"memory.txt"));
			savedMessages = new ArrayList<String>();
			if(fileReader2.hasNextLine())
				lastAccessed = fileReader2.nextLine();
			else 
				lastAccessed = "0000-00-00";
			if(fileReader2.hasNextLine())
				mostRecentMessage = fileReader2.nextLine();
			for(int i = 0; i<5; i++)
				savedMessages.add("");
			while(fileReader2.hasNextLine()) {
				savedMessages.add(0,fileReader2.nextLine());
			}
			fileReader2.close();
			for(int i = 0; i<savedMessages.size(); i++)
				for(int j = 0; j<affirmationsList.size();j++) 
					if(savedMessages.get(i).equals(affirmationsList.get(j))) {
						affirmationsList.remove(j);
						break;
					}
		}catch(Exception e) {
			System.exit(0);
		}
		displayedSavedMessages = new String[5];
		for(int i=displayedSavedMessages.length-1; i>=0;i--)
			displayedSavedMessages[i] = savedMessages.get(i);
		new GUI();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
//keep empty
	}
	private static boolean canUpdate() {
		if(LocalDate.now().toString().equals(lastAccessed)) {
			errorLabel.setVisible(true);
			return false;
		}
		lastAccessed = LocalDate.now().toString();
		if(LocalDate.now().toString().substring(5).equals("05-16"))
			message = "Happy Birthday Ryn. You're one year closer to being my favorite MILF";
		else if(LocalDate.now().toString().substring(5).equals("10-18"))
			message = "Happy Anniversary, baby. I love you so much and I can't wait to spend the rest of my life with you.";
		else {
			currentMessageIndex = (int)(Math.random()*affirmationsList.size());
			if(mostRecentMessage!=null)
				while(mostRecentMessage.equals(affirmationsList.get(currentMessageIndex)))
					currentMessageIndex = (int)(Math.random()*affirmationsList.size());
			message = affirmationsList.get(currentMessageIndex);
			mostRecentMessage = message;
		}
		updateMemory();
		return true;
	}
	private static void updateMemory() {
		try {
			PrintWriter pw = new PrintWriter(new FileOutputStream(new File(basePath+"memory.txt")));
			pw.println(lastAccessed);
			pw.println(mostRecentMessage);
			for(int i=displayedSavedMessages.length-1; i>=0;i--) {
				displayedSavedMessages[i] = savedMessages.get(i);
				pw.println(displayedSavedMessages[i]);
			}
			pw.close();
		}catch(Exception e) {
			System.out.println("this message should never display");
		}
	}
	public static void getFile() {
		try{
			URL website = new URL("https://davisbreci.github.io/files/messages.txt");
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(new File(basePath+"messages.txt"));
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}