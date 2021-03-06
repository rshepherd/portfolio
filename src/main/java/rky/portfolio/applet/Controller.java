
package rky.portfolio.applet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import rky.portfolio.ScoreBoard;
import rky.portfolio.ScoreBoard.GameMode;

/*
 * Game Controller
 */
public class Controller extends JApplet {

	static final int BAR_MIN = 0;
	static final int BAR_MAX = 10;
	static final int BAR_INIT = 0;

	final static int GABMLE_NUM = 10;

	private JPanel leftPanel = new JPanel();
	private JPanel centerPanel = new JPanel();
	private JPanel operPanel = new JPanel(new GridLayout(4, 2));
	private JPanel barPanel = new JPanel(new GridBagLayout());
	private JScrollPane infoPanel = new JScrollPane();
	private JScrollPane gamblePanel = new JScrollPane();
	private MyPanel showPanel = new MyPanel();

	private JTable infoTable = new JTable();
	//private JEditorPane editorPane = new JEditorPane();

	private JButton playButton = new JButton("Play");
	private JButton startButton = new JButton("Start");

	private JLabel roundLabel = new JLabel("Rounds: ", JLabel.CENTER);
	private JTextField roundField = new JTextField();
	private JLabel roundReminder = new JLabel("Current Round : ", JLabel.CENTER);
	private JLabel roundDigit = new JLabel("0", JLabel.CENTER);
	private JRadioButton mode1Button = new JRadioButton("Mode 1");
	private JRadioButton mode2Button = new JRadioButton("Mode 2");
	private ButtonGroup group = new ButtonGroup();



	public static int playerCount = 2;
	public static int ROUND = 10;
	public static HashMap<Integer, Integer> classProp = new HashMap<Integer, Integer>();
	public static int GAMBLENUM = 200;
	public static int CLASSNUM = 5;
	public static int MODE = 1;

	private List<Player> playerList = new ArrayList<Player>();
	private List<Gamble> gambleList = new ArrayList<Gamble>();
	private int linkedMatrix[][];
	private List<Integer> gambleOrder = new ArrayList<Integer>();
	private int currentRound = 0;
	private Map<Integer, List<JSlider>> allocMap = new HashMap<Integer, List<JSlider>>();
	private Player winnerPlayer;
	public String winner = "not set";
	private List<Double> oneRoundResult = new ArrayList<Double>();
	private List<Integer> roundToChangeType = new ArrayList<Integer>();
	private DefaultTableModel dtm;

	private final int WIDTH = 1195;

	boolean isHistagramLoaded = false;
	boolean isTableLoaded = false;

	private final HashMap<Integer,rky.portfolio.Player> mappIdtorkyPlayer = new HashMap<Integer, rky.portfolio.Player>();

	public String getWinner()
	{
		return winner;
	}

	public void init() {

		// Execute a job on the event-dispatching thread:
		// creating this applet's GUI.
		try {

			this.setSize(WIDTH, 600);
			javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					createGUI();
				}
			});
		} catch (Exception e) {
			System.err.println("createGUI didn't successfully complete");
		}
	}

	/*
	 * Init game
	 */
	public void initGame() throws Exception {

		if(mode1Button.isSelected())
			MODE = 1;
		else
			MODE = 2;


		gambleList = PortfolioGenerator.generateGambles(GAMBLENUM, CLASSNUM);
		linkedMatrix = PortfolioGenerator.genLinks(GAMBLENUM);
		playerList.clear();
		for (int i = 0; i < playerCount; i++) {
			Player gambler = new Player(i,"????");
			playerList.add(gambler);
		}

		initTable();


		String output = PortfolioGenerator.outputAsString(gambleList,
				linkedMatrix);
		assignClassType();
		//editorPane.setText(output);
		ROUND = Integer.parseInt(roundField.getText());
		currentRound = 0;
		roundDigit.setText(String.valueOf(currentRound));
		playButton.setEnabled(true);

		for (int i = 0; i < playerCount; i++) {
			List<JSlider> list = allocMap.get(i);
			for (JSlider slider : list) {
				slider.setValue(0);
			}
		}

		if(MODE == 2)
		{
			int changeTimes = ROUND/10;
			for(int i =0;i<changeTimes;i++)
			{
				int round = 0;
				do{
					round = (int)(Math.random()*ROUND);
				}while(roundToChangeType.contains(round));

				roundToChangeType.add(round);	
			}
		}

		if (startButton.getText().equals("Start")) {
			startButton.setText("Restart");
		}

		showPanel.init();
		loadHistogram(gambleList);
		try{
			updateInfoTable();
		}catch (Exception e) {
		}		

	}

	/*
	 * Change one class's type
	 */
	public void classTypeChange()
	{
		//random pick one class
		int classId = (int)(Math.random()*CLASSNUM)%CLASSNUM;
		int classType = classProp.get(classId);
		if(classType == -1)
		{
			int newType = (Math.random()>0.5?0:1);
			classProp.put(classId, newType);
		}else if(classType == 1)
		{
			int newType = (Math.random()>0.5?0:-1);
			classProp.put(classId, newType);
		}else
		{
			int newType = (Math.random()>0.5?1:-1);
			classProp.put(classId, newType);
		}
	}

	/*
	 * Generate gambles play order for one round
	 */
	public void genreateGambleOrder() {
		List<Integer> inOrder = new ArrayList<Integer>();
		gambleOrder.clear();
		for (int i = 0; i < gambleList.size(); i++) {
			inOrder.add(i);
		}
		for (int i = 0; i < gambleList.size(); i++) {
			int pos = (int) (Math.random() * inOrder.size());
			gambleOrder.add(inOrder.get(pos));
			inOrder.remove(pos);
		}
	}

	/*
	 * play gambles in one round
	 */
	public void playARound() {

		currentRound++;

		if(MODE == 2 && roundToChangeType.contains(currentRound))
		{
			classTypeChange();
		}

		genreateGambleOrder();

		for (int i = 0; i < gambleOrder.size(); i++) {

			/*
			 * Update probabilities based on its class
			 */

			Gamble current = gambleList.get(gambleOrder.get(i));

			double highProb = current.highProb;
			double medProb = current.medProb;
			double lowProb = current.lowProb;

			if (classProp.get(current.classId) == 1) {
				highProb = current.highProb + current.lowProb / 2;
				medProb = current.medProb;
				lowProb = current.lowProb / 2;

			} else if (classProp.get(current.classId) == -1) {
				highProb = current.highProb / 2;
				medProb = current.medProb;
				lowProb = current.highProb / 2 + current.lowProb;
			}

			int Hi = 0;
			int Mi = 0;
			int Li = 0;

			for (int j = 0; j < i; j++) {
				Gamble previous = gambleList.get(gambleOrder.get(j));
				if (linkedMatrix[current.id][previous.id] == 1) {
					if (previous.getLastResult() == previous.high_return) {
						Hi++;
					} else if (previous.getLastResult() == previous.medium_return) {
						Mi++;
					} else {
						Li++;
					}
				}
			}

			/*
			 * If Hi > Mi + Li, then halve gilowprob (from the value that it
			 * might have already been assigned based on its class) the value
			 * that and add that probability to gihiprob. If Li > Hi + Mi, then
			 * halve gihiprob and add that probability to gilowprob
			 */

			if (Hi > Mi + Li) {
				highProb = highProb + lowProb / 2;
				lowProb = lowProb / 2;

			} else if (Li > Hi + Mi) {
				lowProb = lowProb + highProb / 2;
				highProb = highProb / 2;
			}

			current.playWithNewProb(highProb, medProb, lowProb);

		}

		computeReturnForOneRound();

		updateHistogram(gambleList);
		try{
			updateInfoTable();
		}catch (Exception e) {
		}		
		roundDigit.setText(String.valueOf(currentRound));

		if (currentRound == ROUND) {
			playButton.setEnabled(false);
			winnerPlayer = getWinnerPlayer();
			String title = "Game End";
			String result;
			if(winnerPlayer == null)
			{
				result = "Tie !";
				winner = "Tie ";
			}else
			{
				result = "Winner is Player "+String.valueOf(winnerPlayer.getId()+1);
				winner = "Player "+String.valueOf(winnerPlayer.getId()+1);
			}
			String multiLineMsg[] = {title,result} ;
			JOptionPane.showMessageDialog(this, multiLineMsg);
		}
	}

	/*
	 * Update info table each round 
	 */

	public void updateInfoTable()
	{
		NumberFormat formatter = new DecimalFormat("####.##");

		if(MODE == 1)
		{
			for(int i=0;i<playerList.size();i++)
			{
				Player  player = playerList.get(i);
				String score = formatter.format(player.getScore());
				String PNL = formatter.format(player.getPnl());
				String teamName = playerList.get(i).getTeamName();
				dtm.setValueAt(teamName, i, 0);
				dtm.setValueAt(score, i, 1);
				dtm.setValueAt(PNL, i, 2);

				try{
					infoTable.updateUI();
				}catch (Exception e) {
				}
			}

		}else
		{
			for(int i=0;i<playerList.size();i++)
			{
				Player  player = playerList.get(i);
				String sharpe = formatter.format(player.getSharpeRatio());

				String amt = formatNum(player.getAmount());
				dtm.setValueAt(playerList.get(i).getTeamName(), i, 0);
				dtm.setValueAt(sharpe, i, 1);
				dtm.setValueAt(amt, i, 2);

				try{
					infoTable.updateUI();
				}catch (Exception e) {
				}			
			}
		}
	}
	/*
	 * Decide who is winner
	 */
	public Player getWinnerPlayer() {
		Player winner = null;
		double max = -1;

		if(MODE == 1)
		{
			if(playerList.get(0).getScore()==playerList.get(1).getScore())
				return null;

			if(playerList.get(0).getScore() > playerList.get(1).getScore())
				winner = playerList.get(0);
			else
				winner = playerList.get(1);

		}else
		{
			if(playerList.get(0).getWealth()==playerList.get(1).getWealth())
				return null;

			if(playerList.get(0).getWealth() > playerList.get(1).getWealth())
				winner = playerList.get(0);
			else
				winner = playerList.get(1);

			/*
			for (int i = 0; i < playerCount; i++) {
				Player player = playerList.get(i);
				if (player.getWealth() > max) {
					max = player.getWealth();
					winner = player;
				}
			}*/

		}

		return winner;
	}

	/*
	 * Get allocation from player
	 */
	public List<Integer> getAllocation(int id) {
		List<JSlider> sliderList = allocMap.get(id);
		List<Integer> allocList = new ArrayList<Integer>();

		for (JSlider slider : sliderList) {
			allocList.add(slider.getValue());
		}

		return allocList;
	}

	/*
	 * Normalize the allocation of player base on current wealth
	 */
	public List<Double> normalizeAllocation(Player player,
			List<Integer> allocation) {
		double wealth;
		if(MODE ==1)
			wealth = 1.0;
		else
			wealth = player.getWealth();

		List<Double> normalizeList = new ArrayList<Double>();
		double sum = 0.0;
		for (Integer amount : allocation) {
			sum += amount;
		}
		for (Integer amount : allocation) {
			double value;
			if (sum == 0.0) {
				value = 0.0;
			} else {
				value = amount / sum * wealth;
			}

			normalizeList.add(value);
		}
		return normalizeList;
	}

	/*
	 * Compute the sum of a list
	 */
	public double sum(List<Double> outcome) {

		double total = 0.0;

		for (Double value : outcome) {
			total += value;
		}

		return total;
	}

	/*
	 * Compute the outcome baseon on allocation in a round
	 */
	public double computeOutcome(List<Double> alloc) {
		ArrayList<Double> outcome = new ArrayList<Double>();
		for (int i = 0; i < gambleList.size(); i++) {
			Double amount = alloc.get(i);
			Double returnValue = gambleList.get(i).getLastResult();
			Double newValue = amount * returnValue;
			outcome.add(newValue);
		}
		return sum(outcome);
	}


	/*
	 * Compute returns for one round
	 */
	public void computeReturnForOneRound() {

		for (int i = 0; i < playerCount; i++) {

			Player player = playerList.get(i);

			List<Integer> allocList = getAllocation(player.getId());
			List<Double> normalizeList = normalizeAllocation(player, allocList);

			if(MODE == 1)
				player.setWealth(1.0);

			double cost = sum(normalizeList);
			double yesterday = player.getWealth();
			player.setPrevious(yesterday);
			player.setWealth(player.getWealth() - cost);

			double outcome = 0;

			outcome = computeOutcome(normalizeList);

			player.setWealth(player.getWealth() + outcome);
			double today = player.getWealth();
			double gamblesReturn;
			if(MODE ==1)
			{
				gamblesReturn = today - yesterday;
			}else
			{
				gamblesReturn= (today - yesterday) / (today + yesterday)* 2;
			}

			player.getReturns().add(gamblesReturn);

			if(MODE == 1 && gamblesReturn >= 1)
				player.increaseScoreByOne();

			if(MODE == 2)
				player.caculateSharpeRatio();


		}
	}

	/*
	 * Load histogram at first
	 */
	public void loadHistogram(List<Gamble> gambleList) {
		showPanel.setHistogramTitle("Return", "Gamble");

		Collections.sort(gambleList);

		for (Gamble g : gambleList) {
			showPanel.insert(String.valueOf(g.id), 0);
		}

		showPanel.updateUI();
	}

	/*
	 * Update histogram each round
	 */
	public void updateHistogram(List<Gamble> gambleList) {

		Collections.sort(gambleList);
		for (Gamble p : gambleList) {
			showPanel.update(String.valueOf(p.id), p.getReturnValue());
		}

		showPanel.updateUI();
	}

	/*
	 * Assign each class as favorable/unfavorable/neutral
	 */
	public void assignClassType() {
		for (int i = 0; i < CLASSNUM; i++) {
			double rand = Math.random();
			if (rand < 1.0 / 3) {
				classProp.put(i, 1);
			} else if (rand < 2.0 / 3) {
				classProp.put(i, 0);
			} else
				classProp.put(i, -1);
		}
	}


	/*
	 * Initialize info table
	 */
	public void initTable()
	{

		NumberFormat formatter = new DecimalFormat("####.##");
		if(MODE == 2)
		{			

			String [] coloumTitle = {"Player","Sharpe Ratio","Amount"};
			dtm = new DefaultTableModel(coloumTitle,0);
			infoTable.setModel(dtm);
			for(int i=0;i<playerList.size();i++)
			{
				Player  player = playerList.get(i);
				String name = player.getTeamName();
				//String current = formatter.format(player.getWealth());
				//String previous = formatter.format(player.getPrevious());
				String sharpe = formatter.format(player.getSharpeRatio());
				String amt = formatNum(player.getAmount());
				String [] cols = {name,sharpe,amt};
				dtm.addRow(cols);
				infoTable.updateUI();
			}
		}else
		{
			String [] coloumTitle = {"Player","Score","Profit/Loss"};
			dtm = new DefaultTableModel(coloumTitle,0);
			infoTable.setModel(dtm);
			for(int i=0;i<playerList.size();i++)
			{
				Player  player = playerList.get(i);
				String name = player.getTeamName();
				//String score = formatter.format(player.getScore());
				String pnl = formatter.format(player.getPNL());
				String [] cols = {name,pnl};
				dtm.addRow(cols);
				infoTable.updateUI();
			}
		}

	}


	/*
	 * Initialize the GUI
	 */
	private void createGUI() {

		GridBagConstraints c = new GridBagConstraints();
		this.getContentPane().setSize(WIDTH, 600);
		getContentPane().setLayout(new BorderLayout());

		leftPanel.setLayout(new BorderLayout());
		leftPanel.setPreferredSize(new Dimension(300,600));
		leftPanel.setBackground(Color.BLUE);
		getContentPane().add(leftPanel,BorderLayout.WEST);

		operPanel.setPreferredSize(new Dimension(300,120));
		leftPanel.add(operPanel,BorderLayout.NORTH);

		roundLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


		mode1Button.setSelected(true);

		group.add(mode1Button);
		group.add(mode2Button);


		operPanel.add(mode1Button);
		operPanel.add(mode2Button);
		operPanel.add(roundLabel);
		operPanel.add(roundField);
		operPanel.add(roundReminder);
		operPanel.add(roundDigit);
		//operPanel.add(startButton);
		//operPanel.add(playButton);

		roundField.setText("0");

		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					initGame();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		playButton.setEnabled(false);
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playARound();
			}
		});

		infoPanel.setPreferredSize(new Dimension(300,100));
		leftPanel.add(infoPanel,BorderLayout.CENTER);

		//		for (int i = 0; i < playerCount; i++) {
		//			Player gambler = new Player(i);
		//			playerList.add(gambler);
		//		}
		//
		initTable();

		infoPanel.getViewport().add(infoTable);

		//gamblePanel.setPreferredSize(new Dimension(300,380));

		//editorPane.setEditable(false);
		//editorPane
		//.setText("Gamble\n\nLink\n");

		//gamblePanel.getViewport().add(editorPane);
		//leftPanel.add(gamblePanel,BorderLayout.SOUTH);

		centerPanel.setPreferredSize(new Dimension(695,600));
		centerPanel.setLayout(new BorderLayout());
		getContentPane().add(centerPanel,BorderLayout.CENTER);

		showPanel.setPreferredSize(new Dimension(695,600));
		//barPanel.setPreferredSize(new Dimension(695,450));

		centerPanel.add(showPanel,BorderLayout.CENTER);
		//centerPanel.add(barPanel,BorderLayout.CENTER);

		List<JSlider> sliderPlayer1 = new ArrayList<JSlider>();
		List<JSlider> sliderPlayer2 = new ArrayList<JSlider>();


		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		JLabel player1Label = new JLabel("P1", JLabel.CENTER);
		barPanel.add(player1Label, c);

		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		JLabel player2Label = new JLabel("P2", JLabel.CENTER);
		barPanel.add(player2Label, c);


		for (int i = 0; i < GABMLE_NUM; i++) {

			JLabel sliderLabel = new JLabel(String.valueOf(i), JLabel.CENTER);
			sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

			JSlider barPlayer1 = new JSlider(JSlider.VERTICAL, BAR_MIN,
					BAR_MAX, BAR_INIT);
			barPlayer1.setMajorTickSpacing(10);
			barPlayer1.setMinorTickSpacing(1);
			barPlayer1.setPaintTicks(true);
			barPlayer1.setPaintLabels(true);
			sliderPlayer1.add(barPlayer1);

			JSlider barPlayer2 = new JSlider(JSlider.VERTICAL, BAR_MIN,
					BAR_MAX, BAR_INIT);
			barPlayer2.setMajorTickSpacing(10);
			barPlayer2.setMinorTickSpacing(1);
			barPlayer2.setPaintTicks(true);
			barPlayer2.setPaintLabels(true);
			sliderPlayer2.add(barPlayer2);

			/*
			c.fill = GridBagConstraints.BOTH;
			c.gridx = i;
			c.gridy = 0;
			c.gridwidth = 1;
			c.gridheight = 1;
			barPanel.add(sliderLabel, c); */

			c.fill = GridBagConstraints.BOTH;
			c.gridx = i+1;
			c.gridy = 0;
			c.gridwidth = 1;
			c.gridheight = 1;
			barPanel.add(barPlayer1, c);

			c.gridx = i+1;
			c.gridy = 1;
			c.gridwidth = 1;
			c.gridheight = 1;
			barPanel.add(barPlayer2, c);
		}

		allocMap.put(0, sliderPlayer1);
		allocMap.put(1, sliderPlayer2);

		//testWithDummyData();
	}

	/**
	 * Only two possibilites 1 ,2
	 */
	public void setMode(int mode){

		if(mode == 1){
			mode1Button.setSelected(true);
			MODE =1;
		}else{
			mode2Button.setSelected(true);
			MODE =2;
		}

	}

	public void setNumberOfRounds(int rounds){
		roundField.setText(rounds+"");
	}

	public void setCurrentRound(int round){
		roundDigit.setText(round+"");
	}

	public void setPlayers(Set<rky.portfolio.Player> players){


		Iterator<rky.portfolio.Player> iter = players.iterator();
		int i = 0;
		while (iter.hasNext()) {

			rky.portfolio.Player p = iter.next();
			Player gambler = new Player(i,p.name);
			mappIdtorkyPlayer.put(new Integer(gambler.id),p);
			playerList.add(gambler);
			i++;
		}

		initTable();
	}

	public void setDisqualifiedPlayers(Set<rky.portfolio.Player> players){

		for(int i = 0 ; i < playerList.size() ; i++){
			if(MODE == 1){
				Integer pid = playerList.get(i).id;

				for(rky.portfolio.Player p : players ){

					if(mappIdtorkyPlayer.get(pid) == p){
						playerList.get(i).setPnl(0);
						playerList.get(i).setScore(0);
						playerList.get(i).setTeamName("Disqualified: "+playerList.get(i).getTeamName());
					}
				}

			}else{
				Integer pid = playerList.get(i).id;

				for(rky.portfolio.Player p : players ){

					if(mappIdtorkyPlayer.get(pid) == p){				
						playerList.get(i).setSharpeRatio(0);
						playerList.get(i).setTeamName("Disqualified: "+playerList.get(i).getTeamName());

					}
				}
			}


			try{
				updateInfoTable();
			}catch (Exception e) {
			}		
		}
	}

	public void updateScore(ScoreBoard sb){

		for(int i = 0 ; i < playerList.size() ; i++){
			if(MODE == 1){

				Integer pid = playerList.get(i).id;
				playerList.get(i).setPnl(sb.getProfit(mappIdtorkyPlayer.get(pid),Integer.parseInt(roundDigit.getText()) -1 ));
				playerList.get(i).setScore(sb.getFinalScore(mappIdtorkyPlayer.get(pid),Integer.parseInt(roundDigit.getText())-1 ));

			}else{
				Integer pid = playerList.get(i).id;
				playerList.get(i).setSharpeRatio((sb.getFinalScore(mappIdtorkyPlayer.get(pid),Integer.parseInt(roundDigit.getText()) -1 )));
				int round = Integer.parseInt(roundDigit.getText())-1;
				playerList.get(i).setAmount((sb.getStartBudget(round,mappIdtorkyPlayer.get(pid),GameMode.mode2)));
			}

		}

		if(!isTableLoaded){
			isTableLoaded = true;
			initTable();
		}
		try{
			updateInfoTable();
		}catch (Exception e) {
		}		
	}

	public void showGambles(Map<rky.portfolio.gambles.Gamble, rky.portfolio.gambles.Return> gameReturns,Map<rky.portfolio.gambles.Gamble, Integer> ids){

		List<Gamble> gambleL = new ArrayList<Gamble>();

		Iterator it = gameReturns.entrySet().iterator();

		while (it.hasNext()) {

			Map.Entry pairs = (Map.Entry)it.next();

			Gamble g = new Gamble();
			rky.portfolio.gambles.Gamble rkyG = (rky.portfolio.gambles.Gamble) pairs.getKey();
			rky.portfolio.gambles.Return returnV = (rky.portfolio.gambles.Return)pairs.getValue();
			//g.id = rkyG;
			g.setReturnValue(rkyG.getV(returnV));
			g.id = ids.get(rkyG);
			gambleL.add(g);

			//System.out.println(pairs.getKey() + " = " + pairs.getValue());
		}
		if(!isHistagramLoaded){
			isHistagramLoaded = true;
			loadHistogram(gambleL);
		}
		updateHistogram(gambleL);
	}

	public void testWithDummyData(){

		setMode(1);
		setNumberOfRounds(0);
		setCurrentRound(0);

		//List of players
		List<rky.portfolio.Player> players = new ArrayList<rky.portfolio.Player>();
		players.add(new rky.portfolio.Player("anyName",null));
		players.add(new rky.portfolio.Player("anyName",null));

		try {
			List<Gamble> gambleList = PortfolioGenerator.generateGambles(GAMBLENUM, CLASSNUM);

			for(Gamble g: gambleList){
				g.play();
			}


			loadHistogram(gambleList);
			updateHistogram(gambleList);


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	private static final int MAX_LENGTH = 9;

	private static String formatNum(double number) {
	    int digitsAvailable = MAX_LENGTH - 2;
	    if (Math.abs(number) < Math.pow(10, digitsAvailable)
	            && Math.abs(number) > Math.pow(10, -digitsAvailable)) {
	        String format = "0.";
	        double temp = number;
	        for (int i = 0; i < digitsAvailable; i++) {
	            if ((temp /= 10) < 1) {
	                format += "#";
	            }
	        }
	        return new DecimalFormat(format).format(number);
	    }
	    String format = "0.";
	    for (int i = 0; i < digitsAvailable; i++) {
	            format += "#";
	    }
	    String r = new DecimalFormat(format + "E0").format(number);
	    int lastLength = r.length() + 1;
	    while (r.length() > MAX_LENGTH && lastLength > r.length()) {
	        lastLength = r.length();
	        r = r.replaceAll("\\.?[0-9]E", "E");
	    }
	    return r;
	}
}
