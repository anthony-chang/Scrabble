package scrabble;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;

class Player{
    //Player class
    //has a rack and also boolean if player is user
    public Rack rack;
    boolean user;
    //constructors
    public Player(){
        user=false;
        rack=new Rack(false, 0, 0);
    }
    public Player(int x, int y){
        user=true;
        rack=new Rack(true, x, y);
    }
}
class User extends Player{
    //child class of player
    public User(int x, int y){
        super(x, y);
    }
}
class Computer extends Player{
    //child class of player
    //level determines how well the computer plays
    public int level;
    //constructor
    public Computer(int lvl){
        super();
        level=lvl;
    }
    public char[][] tryAll(Board board){
        //O( size ^ 4 * num! * num )
        //lovely time complexity
        //approximately 2*10^9, so around 10 seconds to run this method maybe

        //pos are the possible scores the computer can achieve and the move required to get that score
        //ret is the play the computer will make
        //exists is a boolean tracking if a score is possible

        char[][][] pos=new char[500][15][15];
        char[][] ret=new char[15][15];
        boolean[] exists=new boolean[500];
        int[] fac=new int[8];
        fac[0]=1;

        for(int i=1;i<=7;i++)fac[i]=fac[i-1]*i;

        //loop through all possible places to start playing the word
        for(int i=0;i<board.sz;i++){
            for(int j=0;j<board.sz;j++){
                for(int k=0;k<5040;k++){
                    //loop through all permutations of the rack
                    int[] perm=new int[7];
                    boolean[] marked=new boolean[7];
                    int tempk=k;
                    for(int n=7;n>0;n--){
                        int a=tempk/fac[n-1];
                        int ptr=0;
                        while(a>=0){
                            if(!marked[ptr]){
                                a--;
                            }
                            ptr++;
                        }
                        ptr--;
                        perm[7-n]=ptr;
                        marked[ptr]=true;
                        tempk%=fac[n-1];
                    }
                    //and all prefixes of the permutation
                    for(int n=0;n<7;n++){
                        {
                            //play the prefix and see if it's valid
                            char[][] play=new char[15][15];
                            int ii=i;
                            int nn=n;
                            while(nn>=0&&ii<board.sz){
                                while(ii<board.sz&&board.board[ii][j]!=0){
                                    ii++;
                                }
                                if(ii<board.sz){
                                    while(nn>=0&&rack.letters[perm[nn]]==0){
                                        nn--;
                                    }
                                    if(nn>=0){
                                        play[ii][j]=rack.letters[perm[nn]];
                                    }
                                    nn--;
                                }
                                ii++;
                            }
                            //if it is valid, store it
                            if(board.isValid(play)){
                                int score=board.score(play);
                                for(int ir=0;ir<board.sz;ir++){
                                    for(int jr=0;jr<board.sz;jr++){
                                        pos[score][ir][jr]=play[ir][jr];
                                    }
                                }
                                exists[score]=true;
                            }
                        }
                        {
                            //play the prefix and see if it's valid
                            char[][] play=new char[15][15];
                            int jj=j;
                            int nn=n;
                            while(nn>=0&&jj<board.sz){
                                while(jj<board.sz&&board.board[i][jj]!=0){
                                    jj++;
                                }
                                if(jj<board.sz){
                                    while(nn>=0&&rack.letters[perm[nn]]==0){
                                        nn--;
                                    }
                                    if(nn>=0){
                                        play[i][jj]=rack.letters[perm[nn]];
                                    }
                                    nn--;
                                }
                                jj++;
                            }
                            //if it is valid, store it
                            if(board.isValid(play)){
                                int score=board.score(play);
                                for(int ir=0;ir<board.sz;ir++){
                                    for(int jr=0;jr<board.sz;jr++){
                                        pos[score][ir][jr]=play[ir][jr];
                                    }
                                }
                                exists[score]=true;
                            }
                        }
                    }
                }
            }
        }
        int k=0;
        //depending on the computer's level, they will choose a certain move
        boolean lvlMove=true;
        for(int i=1;i<500&&lvlMove;i++){
            if(exists[i])k=i;
            if(level==1&&k>7)lvlMove=false;
            if(level==2&&k>15)lvlMove=false;
        }
        for(int i=0;i<15;i++){
            for(int j=0;j<15;j++){
                ret[i][j]=pos[k][i][j];
            }
        }
        return ret;
    }
}
class Rack{
    //Rack class
    //pos stores the position each tile will be on the rack
    //cur is the number of tiles the player currently has (not necessarily on the rack)
    //letters are the letters on the rack
    //tiles are the tiles on the rack
    //user determines if the rack is visible
    public int[][] pos=new int[7][2];
    public int cur=0;
    public char[] letters=new char[7];
    public Tile[] tiles=new Tile[7];
    public boolean user;
    //constructor
    public Rack(Rack another){
        //"clones" rack
        this.cur=another.cur;
        for(int i=0;i<7;i++){
            this.letters[i]=another.letters[i];
        }
        this.user=false;
    }
    public Rack(boolean user, int x, int y){
        this.user=user;
        //x, y are the positions of the top left corner of the rack
        for(int i=0;i<7;i++){
            pos[i][0]=x+34*i;
            pos[i][1]=y;
        }
    }
    public void drawTiles(ArrayList bag) throws IOException{
        //draws tiles from the bag
        //note that bag changes
        int j=0;
        while(cur<7&&j<7&&bag.size()>0){
            if(letters[j]==0){
                letters[j]=(char)('a'+(int)bag.get(bag.size()-1));
                bag.remove(bag.size()-1);
                tiles[j]=new Tile(pos[j][0], pos[j][1], letters[j], ImageIO.read(new File("tiles\\"+letters[j]+".png")), 0, user, false);
                tiles[j].putInRack(j);
                cur++;
            }
            j++;
        }
    }
    //removes the tile at a certain position from the rack
    public void remove(int position){
        letters[position]=(char)((int)(0));
        tiles[position]=null;
    }
}

class Tile extends JLabel{
    //Tile class
    //x, y are position of tile
    //rackPos is position in rack, if it is in a rack
    //c is the letter it has
    //img is it's image
    //imgnum is it's position in Draw
    //inRack stores whether the tile is in a rack
    //visible stores whether the tile is visible or not
    //if the tile is frozen, the user cannot move it
    public int x, y, rackPos;
    public char c;
    public BufferedImage img;
    public int imgnum=0;
    public boolean inRack, visible, frozen;
    //constructor
    public Tile(int a, int b, char c, BufferedImage img, int num, boolean visible, boolean frozen){
        x=a+img.getWidth();
        y=b+img.getHeight();
        this.c=c;
        this.img=img;
        imgnum=num;
        this.visible=visible;
        this.frozen=frozen;
    }

    //these methods change various aspects of a tile
    public void putInRack(int rackPos){
        inRack=true;
        this.rackPos=rackPos;
    }
    public void removeFromRack(){
        inRack=false;
    }
    public void changeNum(int num){
        this.imgnum=num;
    }
    public void move(int x, int y){
        this.x=x;
        this.y=y;
    }
    public void freeze(){
        this.frozen=true;
    }
}
class Board{
    //Board class
    //sz is dimensions of a Scrabble board (15 x 15)
    //board represents the letters on the board
    //scoring represents those coloured tiles and what they do to the score
    //letterScore is how much each letter is worth
    //touch is used to see if a play is valid, as the tiles have to touch tiles already played
    //dictionary is a dictionary
    public int sz=15;
    public char[][] board = new char[15][15];
    private int[][] scoring=new int[15][15];
    private int[] letterScore=new int[26];
    public boolean[][] touch=new boolean[15][15];
    public HashMap<String, Boolean> dictionary= new HashMap<>();

    //constructor
    public Board() throws IOException{
        //1 = double letter score
        //2 = triple letter score
        //3 = double word score
        //4 = triple letter score
        scoring=new int[][] {
                {4, 0, 0, 1, 0, 0, 0, 4, 0, 0, 0, 1, 0, 0, 4},
                {0, 3, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 3, 0},
                {0, 0, 3, 0, 0, 0, 1, 0, 1, 0, 0, 0, 3, 0, 0},
                {1, 0, 0, 3, 0, 0, 0, 1, 0, 0, 0, 3, 0, 0, 1},
                {0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0},
                {0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0},
                {0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0},
                {4, 0, 0, 1, 0, 0, 0, 3, 0, 0, 0, 1, 0, 0, 4},
                {0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0},
                {0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0},
                {0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0},
                {1, 0, 0, 3, 0, 0, 0, 1, 0, 0, 0, 3, 0, 0, 1},
                {0, 0, 3, 0, 0, 0, 1, 0, 1, 0, 0, 0, 3, 0, 0},
                {0, 3, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 3, 0},
                {4, 0, 0, 1, 0, 0, 0, 4, 0, 0, 0, 1, 0, 0, 4}
        };
        letterScore=new int[] {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
        //first move must be on center square
        touch[7][7]=true;
        inputDictionary();
        dictionary.put("", true);
        //one letter words are not allowed
        for(int i=0;i<26;i++){
            if(dictionary.get(""+(char)('a'+i))!=null)dictionary.remove(""+(char)('a'+i));
        }
    }
    public Board(Board b){
        //"clones" board
        this.dictionary=b.dictionary;
        this.sz=b.sz;
        this.letterScore=b.letterScore;
        for(int i=0;i<sz;i++){
            for(int j=0;j<sz;j++){
                this.board[i][j]=b.board[i][j];
                this.scoring[i][j]=b.scoring[i][j];
                this.touch[i][j]=b.touch[i][j];
            }
        }
    }
    private void inputDictionary() throws IOException{
        //inputs dictionary from random dictionary we found online which is mostly english as far as i can tell
        //official scrabble dictionary costs money
        Scanner sc=new Scanner(new FileReader(new File("dictionary.txt")));
        while(sc.hasNextLine()){
            String str=sc.nextLine();
            dictionary.put(str, true);
        }
    }
    public void play(char[][] play){
        //char[][] play is the letters they played
        //for example, if play[1][3] is 'a', then A was played on the 2nd row, 4th column
        for(int i=0;i<sz;i++){
            for(int j=0;j<sz;j++){
                //update board
                if(board[i][j]==0)board[i][j]=play[i][j];
            }
        }
        for(int i=0;i<sz;i++){
            for(int j=0;j<sz;j++){
                if(board[i][j]!=0){
                    //update touch
                    if(i>0)touch[i-1][j]=true;
                    if(i<sz-1)touch[i+1][j]=true;
                    if(j>0)touch[i][j-1]=true;
                    if(j<sz-1)touch[i][j+1]=true;
                    //the bonuses from special squares only apply the first time
                    scoring[i][j]=0;
                }
            }
        }
    }
    public int score(char[][] play){
        //determines the score a play will receive
        int total=0;
        //vis is used to make sure the same word is not scored multiple times
        boolean[][][] vis=new boolean[15][15][2];
        int bingo=0;
        for(int row=0;row<sz;row++){
            for(int col=0;col<sz;col++){
                if(play[row][col]!=0){
                    bingo++;
                    int lptr, rptr;
                    //scoring vertical words
                    if(!vis[row][col][0]){
                        int wordScore=0;
                        lptr=row;
                        rptr=row;
                        while(lptr>=0&&(play[lptr][col]!=0||board[lptr][col]!=0))lptr--;
                        while(rptr<sz&&(play[rptr][col]!=0||board[rptr][col]!=0))rptr++;
                        lptr++;
                        rptr--;
                        int multiplier=1;
                        for (int k = lptr; k <= rptr && lptr!=rptr; k++) {
                            vis[k][col][0]=true;
                            char letter=play[k][col];
                            if (play[k][col] == 0 )letter=board[k][col];
                            int c=letterScore[(int)(letter-'a')];
                            wordScore+=c;
                            if(scoring[k][col]==1)wordScore+=c;
                            if(scoring[k][col]==2)wordScore+=2*c;
                            if(scoring[k][col]==3)multiplier*=2;
                            if(scoring[k][col]==4)multiplier*=3;
                        }
                        wordScore*=multiplier;
                        total+=wordScore;
                    }
                    //scoring horizontal words
                    if(!vis[row][col][1]){
                        int wordScore=0;
                        lptr=col;
                        rptr=col;
                        while(lptr>=0&&(play[row][lptr]!=0||board[row][lptr]!=0))lptr--;
                        while(rptr<sz&&(play[row][rptr]!=0||board[row][rptr]!=0))rptr++;
                        lptr++;
                        rptr--;
                        int multiplier=1;
                        for (int k = lptr; k <= rptr && lptr!=rptr; k++) {
                            vis[row][k][1]=true;
                            char letter=play[row][k];
                            if (play[row][k] == 0 )letter=board[row][k];
                            int c=letterScore[(int)(letter-'a')];
                            wordScore+=c;
                            if(scoring[row][k]==1)wordScore+=c;
                            if(scoring[row][k]==2)wordScore+=2*c;
                            if(scoring[row][k]==3)multiplier*=2;
                            if(scoring[row][k]==4)multiplier*=3;
                        }
                        wordScore*=multiplier;
                        total+=wordScore;
                    }
                }
            }
        }
        //if a player uses all seven tiles, they receive a bonus of 50
        if(bingo==7)total+=50;
        return total;
    }
    public boolean isValid(char[][] play){
        String wordHor="", wordVer="";
        int firstCol = 15, lastCol = 0, firstRow = 15, lastRow = 0; //first and last occurrences of the player's move in a row/column
        boolean touching=false;
        for (int row = 0; row < sz; row++){
            for (int col = 0; col < sz; col++) {
                if (play[row][col] != 0 && board[row][col] != 0) //if the tile is on top of another tile
                    return false;
                else if (play[row][col] != 0) {
                    if (play[row][col] != 0 && row < firstRow)
                        firstRow = row;
                    if (play[row][col] != 0 && row > lastRow)
                        lastRow = row;
                    if (play[row][col] != 0 && col < firstCol)
                        firstCol = col;
                    if (play[row][col] != 0 && col > lastCol)
                        lastCol = col;
                }
                if(play[row][col]!=0)touching|=touch[row][col];
            }
        }
        //the move has to be adjacent to tiles already played
        if(!touching)return false;
        //if the tiles are not contained in a row/column, then it is invalid
        if (firstRow != lastRow && firstCol != lastCol) {
            return false;
        }
        if(firstRow==lastRow){
            //check that there are no gaps
            for(int i=firstCol;i<=lastCol;i++){
                if(board[firstRow][i]==0&&play[firstRow][i]==0)return false;
            }
        }
        if(firstCol==lastCol){
            //check that there are no gaps
            for(int i=firstRow;i<=lastRow;i++){
                if(board[i][firstCol]==0&&play[i][firstCol]==0)return false;
            }
        }
        //only called bingo because variable which did the same thing was called bingo in the scoring method
        //but calling it bingo made sense there
        //it doesn't really make sense here
        int bingo=0;
        for(int row=0;row<sz;row++){
            for(int col=0;col<sz;col++){
                if(play[row][col]!=0){
                    bingo++;
                    wordHor="";
                    wordVer="";
                    int lptr=row;
                    int rptr=row;
                    //check vertical word
                    while(lptr>=0&&(play[lptr][col]!=0||board[lptr][col]!=0))lptr--;
                    while(rptr<sz&&(play[rptr][col]!=0||board[rptr][col]!=0))rptr++;
                    lptr++;
                    rptr--;
                    for (int k = lptr; k <= rptr && lptr!=rptr; k++) {
                        if (play[k][col] == 0 && board[k][col] == 0) //so that there is no space between word
                            return false;
                        //it's called wordHor but i'm pretty sure it's vertical
                        if(play[k][col]==0)wordHor+=board[k][col];
                        else wordHor+=play[k][col];
                    }
                    //verify that the word is in the dictionary
                    if (dictionary.get(wordHor) == null) {
                        return false;
                    }
                    lptr=col;
                    rptr=col;
                    //check horizontal word
                    while(lptr>=0&&(play[row][lptr]!=0||board[row][lptr]!=0))lptr--;
                    while(rptr<sz&&(play[row][rptr]!=0||board[row][rptr]!=0))rptr++;
                    lptr++;
                    rptr--;
                    for (int k = lptr; k <= rptr && lptr!=rptr; k++) {
                        if (play[row][k] == 0 && board[row][k] == 0) //so that there is no space between word
                            return false;
                        if(play[row][k]==0)wordVer+=board[row][k];
                            //probably horizontal??
                        else wordVer+=play[row][k];
                    }
                    //verify that the word is in the dictionary
                    if (dictionary.get(wordVer) == null) {
                        return false;
                    }
                }
            }
        }
        //player must put down at least one tile
        //though if they don't, it'll probably return false since firstRow!=lastRow or something
        //just being unnecessarily safe here
        if(bingo==0)return false;
        return true;
    }
}
class Draw extends JPanel{
    //maintains all the graphics
    //size is the number of images
    //BufferedImage[] draw are the images
    //pos are the positions of each image
    public int size;
    public BufferedImage[] draw;
    public int[][] pos;
    //constructor
    public Draw(int k, BufferedImage[] img, int[][] arr){
        size=k;
        this.draw=img;
        this.pos=arr;
    }

    //draws everything using magical graphics
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        for(int j=0;j<size;j++)g.drawImage(this.draw[j], this.pos[j][0], this.pos[j][1], this);
    }
}
public class Scrabble{
    //stuff
    private JFrame mainFrame;
    private Draw[] draw=new Draw[2];
    private BufferedImage[] img;
    private BufferedImage nothing;
    private int[][] pos;
    private Tile[] visibleTiles=new Tile[1000];
    private int ind=0, tileNum=0, num=0, computerLvl=1, rackX=150, rackY=650, messagesNum=0, recentScore=0, passing=0;
    private Board board;
    private Computer comp;
    private Player user;

    //more stuff
    private boolean userTurn=true, ended=false;
    private char[][] userPlay;
    private int[][][] PosBoard = new  int[15][15][2];
    private int[] playerScores;
    private JLabel[] displayScores, displayMessages;
    private ArrayList<Integer> bag;
    private MyMouseAdapter myMouseAdapter;

    //constructor
    public Scrabble(int lvl) throws IOException{
        //lvl is the difficulty setting

        //prepare some stuff
        //the method names are practically the comments here
        computerLvl=lvl;
        playerScores=new int[2];
        displayScores=new JLabel[2];
        displayMessages=new JLabel[6];
        prepareGUI();
        prepareBag();
        startScores();
        startMessages();
        prepareBackground();
        //user's rack is located at (rackX, rackY)
        user=new User(rackX, rackY);
        userPlay=new char[15][15];
        userDrawTiles();
        comp=new Computer(computerLvl);
        comp.rack.drawTiles(bag);
        implementBoard();
        //doesn't go smoothly without timer
        //since when redraw is called, not everything has prepared in time
        //the timer gives time for everything else to get ready
        Timer s=new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                redraw();
            }
        });
        s.setRepeats(false);
    }
    //prepares basic gui
    private void prepareGUI() throws IOException{
        mainFrame = new JFrame("Scrabble");
        //all the stackoverflow people discouraged setting the layout to null
        //they were probably right
        mainFrame.setLayout(null);
        mainFrame.setSize(724, 745);
        //closes window
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });
    }
    private void prepareBackground() throws IOException{
        //board coordinate positions (top left corners)
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++){
                PosBoard[row][col][0] =(int)(12.7+32.9*col+0.16*row);
                PosBoard[row][col][1] =(int)(115+35.8*row);
            }
        }
        nothing=ImageIO.read(new File("tiles\\nothing.png"));
        img=new BufferedImage[1000];
        //background image
        img[0]=ImageIO.read(new File("tiles\\background.png"));
        pos=new int[1000][2];
        pos[0][0]=0;
        pos[0][1]=0;
        draw[0]=new Draw(1, img, pos);
        mainFrame.add(draw[0]);
        draw[1]=draw[0];
        ind=1-ind;
        num++;
        redraw();
        //allows user to do stuff with mouse
        myMouseAdapter = new MyMouseAdapter();
        mainFrame.addMouseListener(myMouseAdapter);
        mainFrame.addMouseMotionListener(myMouseAdapter);
    }
    private void prepareBag(){
        bag=new ArrayList<Integer>();
        //freq is amount of times each letter appears
        int[] freq={9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
        for(int i=0;i<26;i++){
            for(int j=0;j<freq[i];j++){
                bag.add(i);
            }
        }
        shuffleBag();
    }
    private void shuffleBag(){
        //hooray for builtin methods
        Collections.shuffle(bag);
    }
    private void userDrawTiles() throws IOException{
        //draws user's tiles
        //temp is the rack before drawing
        char[] temp=new char[7];
        for(int i=0;i<7;i++)temp[i]=user.rack.letters[i];
        //draw tiles from bag
        user.rack.drawTiles(bag);
        for(int i=0;i<7;i++){
            //draw (like graphics draw) the tiles which were just drawn from the bag
            if(temp[i]==0&&user.rack.letters[i]!=0){
                img[num]=user.rack.tiles[i].img;
                pos[num][0]=user.rack.pos[i][0];
                pos[num][1]=user.rack.pos[i][1];
                visibleTiles[tileNum]=user.rack.tiles[i];
                tileNum++;
                user.rack.tiles[i].changeNum(num);
                num++;
            }
        }
        redraw();
    }
    private void implementBoard() throws IOException{
        board=new Board();
    }
    private void startScores(){
        //display scores for the first time
        displayScores[0]=new JLabel("You: "+playerScores[0]+" pts");
        displayScores[0].setBounds(530, 105, 200, 30);
        displayScores[0].setFont(new Font("Arial", Font.BOLD, 24));
        displayScores[0].setForeground(new Color(53, 50, 56));
        mainFrame.repaint();
        mainFrame.add(displayScores[0]);
        displayScores[0].setVisible(true);
        displayScores[1]=new JLabel("CPU: "+playerScores[1]+" pts");
        displayScores[1].setBounds(530, 145, 200, 30);
        displayScores[1].setFont(new Font("Arial", Font.BOLD, 24));
        displayScores[1].setForeground(new Color(53, 50, 56));
        mainFrame.repaint();
        mainFrame.add(displayScores[1]);
        displayScores[1].setVisible(true);
    }
    private void updateScores(int playerInd, int score){
        playerScores[playerInd]+=score;
        String message="CPU";
        if(playerInd==0)message="You";
        message+=(" earned "+score+" points");
        if(messagesNum<4){
            messagesNum++;
            //you physically cannot earn only 1 point in this version
            addMessages(message, messagesNum);
        }
        else{
            pushMessages(message);
        }
        displayScores();
        displayMessages();
    }
    private void displayScores(){
        //this is used after the first time
        //since this way, the old scores are removed

        mainFrame.remove(displayScores[0]);
        displayScores[0]=new JLabel("You: "+playerScores[0]+" pts");
        displayScores[0].setBounds(530, 105, 200, 30);
        displayScores[0].setFont(new Font("Arial", Font.BOLD, 24));
        displayScores[0].setForeground(new Color(53, 50, 56));
        mainFrame.repaint();
        mainFrame.add(displayScores[0]);
        displayScores[0].setVisible(true);

        mainFrame.remove(displayScores[1]);
        displayScores[1]=new JLabel("CPU: "+playerScores[1]+" pts");
        displayScores[1].setBounds(530, 145, 200, 30);
        displayScores[1].setFont(new Font("Arial", Font.BOLD, 24));
        displayScores[1].setForeground(new Color(53, 50, 56));
        mainFrame.repaint();
        mainFrame.add(displayScores[1]);
        displayScores[1].setVisible(true);

    }
    private void startMessages(){

        displayMessages[0]=new JLabel("Welcome to Scrabble!");
        displayMessages[0].setBounds(544, 205, 150, 20);
        displayMessages[0].setFont(new Font("Arial", Font.BOLD, 13));
        displayMessages[0].setForeground(new Color(53, 50, 56));
        mainFrame.add(displayMessages[0]);
        displayMessages[0].setVisible(true);

        displayMessages[1]=new JLabel("You earned 999 points");
        displayMessages[1].setBounds(540, 240, 150, 20);
        displayMessages[1].setFont(new Font("Arial", Font.BOLD, 12));
        displayMessages[1].setForeground(new Color(53, 50, 56));
        mainFrame.add(displayMessages[1]);
        displayMessages[1].setVisible(false);

        displayMessages[2]=new JLabel("CPU earned 999 points");
        displayMessages[2].setBounds(540, 270, 150, 20);
        displayMessages[2].setFont(new Font("Arial", Font.BOLD, 12));
        displayMessages[2].setForeground(new Color(53, 50, 56));
        mainFrame.add(displayMessages[2]);
        displayMessages[2].setVisible(false);

        displayMessages[3]=new JLabel("You earned 999 points");
        displayMessages[3].setBounds(540, 300, 150, 20);
        displayMessages[3].setFont(new Font("Arial", Font.BOLD, 12));
        displayMessages[3].setForeground(new Color(53, 50, 56));
        mainFrame.add(displayMessages[3]);
        displayMessages[3].setVisible(false);

        displayMessages[4]=new JLabel("CPU earned 999 points");
        displayMessages[4].setBounds(540, 330, 150, 20);
        displayMessages[4].setFont(new Font("Arial", Font.BOLD, 12));
        displayMessages[4].setForeground(new Color(53, 50, 56));
        mainFrame.add(displayMessages[4]);
        displayMessages[4].setVisible(false);

        displayMessages[5]=new JLabel("It's now your turn");
        displayMessages[5].setBounds(540, 360, 150, 20);
        displayMessages[5].setFont(new Font("Arial", Font.BOLD, 12));
        displayMessages[5].setForeground(new Color(53, 50, 56));
        mainFrame.add(displayMessages[5]);
        displayMessages[5].setVisible(true);

    }
    private void addMessages(String message, int messageInd){
        mainFrame.remove(displayMessages[messageInd]);
        displayMessages[messageInd]=new JLabel(message);
        displayMessages[messageInd].setBounds(540, 210+messageInd*30, 150, 20);
        displayMessages[messageInd].setFont(new Font("Arial", Font.BOLD, 12));
        displayMessages[messageInd].setForeground(new Color(53, 50, 56));
        mainFrame.add(displayMessages[messageInd]);
        displayMessages[messageInd].setVisible(true);
    }
    private void pushMessages(String message){
        for(int i=1;i<=4;i++){
            mainFrame.remove(displayMessages[i]);
            if(i!=4)displayMessages[i].setText(displayMessages[i+1].getText());
        }
        displayMessages[4]=new JLabel(message);
        displayMessages[4].setBounds(540, 330, 150, 20);
        displayMessages[4].setFont(new Font("Arial", Font.BOLD, 12));
        displayMessages[4].setForeground(new Color(53, 50, 56));
        for(int i=1;i<=4;i++){
            mainFrame.add(displayMessages[i]);
            displayMessages[i].setVisible(true);
        }
    }
    public void displayMessages(){
        for(int i=0;i<6;i++){
            mainFrame.remove(displayMessages[i]);
            mainFrame.add(displayMessages[i]);
        }
    }
    public void computerTurn() throws IOException{
        char[][] compPlay;
        compPlay=comp.tryAll(board);
        int compScore=board.score(compPlay);
        if(compScore==0)passing++;
        else passing=0;
        updateScores(1, compScore);
        for(int i=0;i<15;i++){
            for(int j=0;j<15;j++){
                if(compPlay[i][j]!=0){
                    for(int col=0;col<7;col++){
                        if(compPlay[i][j]==comp.rack.letters[col]){
                            comp.rack.letters[col]=0;
                            col=7;
                        }
                    }
                    comp.rack.cur--;
                    try {
                        visibleTiles[tileNum]=new Tile(0, 0, compPlay[i][j], ImageIO.read(new File("tiles\\"+compPlay[i][j]+".png")), num, true, true);
                        visibleTiles[tileNum].move(PosBoard[i][j][0]+5, PosBoard[i][j][1]-18);
                        img[num]=visibleTiles[tileNum].img;
                        pos[num][0]=visibleTiles[tileNum].x;
                        pos[num][1]=visibleTiles[tileNum].y;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    num++;
                    tileNum++;
                }
            }
        }
        if(passing==6||(bag.size()==0&&comp.rack.cur==0)){
            endGame();
        }
        try {
            comp.rack.drawTiles(bag);
        } catch (IOException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        board.play(compPlay);
        addMessages("It's now your turn", 5);
        userTurn=true;
        redraw();
    }
    public void endTurn() throws IOException{
        updateScores(0, recentScore);
        if(recentScore!=0)passing=0;
        else passing++;
        if(passing==6||(bag.size()==0&&user.rack.cur==0)){
            endGame();
        }
        if(ended)return;
        recentScore=0;
        userDrawTiles();
        userPlay=new char[15][15];
        userTurn=false;
        addMessages("The computer is thinking...", 5);
        for(int i=0;i<tileNum;i++){
            int[] coord=myMouseAdapter.board(visibleTiles[i].x, visibleTiles[i].y);
            if(coord[0]!=-1&&visibleTiles[i].x == PosBoard[coord[1]][coord[0]][0]+38&&visibleTiles[i].y == PosBoard[coord[1]][coord[0]][1]+15){
                visibleTiles[i].freeze();
            }
        }
        redraw();
        Timer ts=new Timer(100, new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try {
                    computerTurn();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        ts.setRepeats(false);
        ts.start();
    }
    //game ends
    public void endGame(){
        if(playerScores[0]>playerScores[1]){
            addMessages("You win! Congrats!", 5);
        }
        if(playerScores[0]==playerScores[1]){
            addMessages("It's a tie! Not bad.", 5);
        }
        if(playerScores[0]<playerScores[1]){
            addMessages("Better luck next time.", 5);
        }
    }
    //this method updates draw, which is more or less in charge of graphics
    public void redraw(){
        draw[ind]=new Draw(num, img, pos);
        mainFrame.remove(draw[1-ind]);
        displayScores();
        displayMessages();
        draw[ind].setBounds(0, 0, 750, 750);
        draw[ind].repaint();
        mainFrame.add(draw[ind]);
        ind=1-ind;
        draw[ind]=draw[1-ind];
        mainFrame.revalidate();
        mainFrame.repaint();
        mainFrame.setVisible(true);
    }
    //click, drag, release, repeat
    private class MyMouseAdapter extends MouseAdapter {
        //clickedTile is the tile being clicked
        //rx, ry are how far the mouse is from the clicked tile's official position
        //tileInd is the position of the clickedTile in the visibleTiles array
        private int rx, ry, tileInd;
        private Tile clickedTile = null;
        private boolean submitClicked=false, exchangeClicked=false, passClicked=false, exchangeSubmitClicked=false, exchangeCancelClicked=false, recallClicked=false, shuffleClicked=false;
        public boolean inRange(Point p, int cx, int cy, int width, int height){
            if(2*p.x+20-2*cx<=width&&2*cx-2*p.x-20<=width&&2*p.y-2*cy-35<=height&&2*cy-2*p.y+35<=height)return true;
            return false;
        }
        public boolean submitClicked(Point p){
            if(p.x>=595&&p.x<=650&&p.y>=675&&p.y<=725)return true;
            return false;
        }
        public boolean exchangeClicked(Point p){
            if(p.x>=598&&p.x<=644&&p.y>=536&&p.y<=585)return true;
            return false;
        }
        public boolean passClicked(Point p){
            if(p.x>=605&&p.x<=633&&p.y>=465&&p.y<=517)return true;
            return false;
        }
        public boolean exchangeSubmitClicked(Point p){
            if(p.x>=560&&p.x<=600&&p.y>=605&&p.y<=652)return true;
            return false;
        }
        public boolean exchangeCancelClicked(Point p){
            if(p.x>=633&&p.x<=679&&p.y>=605&&p.y<=652)return true;
            return false;
        }
        public boolean recallClicked(Point p){
            if(p.x>=85&&p.x<=120&&p.y>=675&&p.y<=725)return true;
            return false;
        }
        public boolean shuffleClicked(Point p){
            if(p.x>=32&&p.x<=63&&p.y>=675&&p.y<=725)return true;
            return false;
        }
        public int[] board(int x, int y) { //checks for closest position
            int[] coord={-1, -1};
            for (int row = 0; row < 15; row++)
                for (int col = 0; col < 15; col++) {
                    if (x >= PosBoard[row][col][0] + 16 && x <= PosBoard[row][col][0] + 60 && y >= PosBoard[row][col][1]&& y <= PosBoard[row][col][1] + 50) {
                        coord[0]=col;
                        coord[1]=row;
                    }
                }
            return coord;
        }
        public int[] rack(int x, int y){
            int[] coord={-1, -1};
            for(int col=0;col<7;col++){
                if(x>=user.rack.pos[col][0]&&x<=user.rack.pos[col][0]+33&&y>=user.rack.pos[col][1]&&y<=user.rack.pos[col][1]+33){
                    coord[0]=0;
                    coord[1]=col;
                }
            }
            return coord;
        }
        public void returnTile(Tile t, int numInd){
            for(int i=0;i<7;i++){
                if(user.rack.letters[i]==0){
                    t.x=user.rack.pos[i][0]+33;
                    t.y=user.rack.pos[i][1]+33;
                    t.putInRack(i);
                    user.rack.tiles[i]=t;
                    user.rack.letters[i]=t.c;
                    img[t.imgnum]=user.rack.tiles[i].img;
                    pos[t.imgnum][0]=user.rack.pos[i][0];
                    pos[t.imgnum][1]=user.rack.pos[i][1];
                    i=7;
                    visibleTiles[numInd]=t;
                }
            }
        }
        public void placeTile(Tile t, int i){
            t.x=user.rack.pos[i][0]+33;
            t.y=user.rack.pos[i][1]+33;
            t.putInRack(i);
            user.rack.tiles[i]=t;
            user.rack.letters[i]=t.c;
            img[t.imgnum]=user.rack.tiles[i].img;
            pos[t.imgnum][0]=user.rack.pos[i][0];
            pos[t.imgnum][1]=user.rack.pos[i][1];
            visibleTiles[tileInd]=t;
        }
        public void recallTiles(){
            for(int i=0;i<tileNum;i++){
                int[] coord=board(visibleTiles[i].x, visibleTiles[i].y);
                if(coord[0]!=-1&&!visibleTiles[i].frozen&&visibleTiles[i].x == PosBoard[coord[1]][coord[0]][0]+38&&visibleTiles[i].y == PosBoard[coord[1]][coord[0]][1]+15){
                    userPlay[coord[1]][coord[0]]=0;
                    returnTile(visibleTiles[i], i);
                }
            }
        }
        public void mousePressed(MouseEvent me){
            if(!userTurn||ended)return;
            if(exchangeClicked){
                for(int i=0;i<tileNum&&clickedTile==null;i++){
                    Tile cur=visibleTiles[i];
                    if(!cur.frozen&&inRange(me.getPoint(), cur.x, cur.y, cur.img.getWidth(), cur.img.getHeight())){
                        int[] coord=rack(cur.x, cur.y);
                        if(coord[0]!=-1&&cur.x == user.rack.pos[coord[1]][0]+33&&cur.y == user.rack.pos[coord[1]][1]+33){
                            clickedTile=cur;
                            draw[ind].draw[visibleTiles[i].imgnum]=nothing;
                            draw[ind].pos[visibleTiles[i].imgnum][0]=10000;
                            draw[ind].pos[visibleTiles[i].imgnum][1]=10000;
                            visibleTiles[i].freeze();
                        }
                    }
                }
                exchangeSubmitClicked=exchangeSubmitClicked(me.getPoint());
                exchangeCancelClicked=exchangeCancelClicked(me.getPoint());
                redraw();
                return;
            }
            recallClicked=recallClicked(me.getPoint());
            shuffleClicked=shuffleClicked(me.getPoint());
            submitClicked=submitClicked(me.getPoint());
            passClicked=passClicked(me.getPoint());
            exchangeClicked=exchangeClicked(me.getPoint());
            if(exchangeClicked){
                if(bag.size()<7){
                    addMessages("Bag too empty to exchange", 5);
                    exchangeClicked=false;
                }
                else{
                    addMessages("Click tiles to exchange", 5);
                }
                redraw();
                return;
            }
            for(int i=0;i<tileNum&&clickedTile==null;i++){
                Tile cur=visibleTiles[i];
                if(!cur.frozen&&inRange(me.getPoint(), cur.x, cur.y, cur.img.getWidth(), cur.img.getHeight())){
                    clickedTile=cur;
                    int[] coord=rack(clickedTile.x, clickedTile.y);
                    if(coord[0]!=-1)user.rack.remove(clickedTile.rackPos);
                    tileInd=i;
                    for(int j=clickedTile.imgnum;j<num-1;j++){
                        img[j]=img[j+1];
                        pos[j][0]=pos[j+1][0];
                        pos[j][1]=pos[j+1][1];
                    }
                    for(int j=0;j<tileNum;j++){
                        if(visibleTiles[j].imgnum>clickedTile.imgnum)visibleTiles[j].imgnum--;
                    }
                    coord=board(clickedTile.x, clickedTile.y);
                    if(coord[0]!=-1&&clickedTile.x == PosBoard[coord[1]][coord[0]][0]+38&&clickedTile.y == PosBoard[coord[1]][coord[0]][1]+15){
                        userPlay[coord[1]][coord[0]]=(char)(0);
                    }
                    coord=rack(clickedTile.x, clickedTile.y);
                    if(coord[0]!=-1&&clickedTile.x == user.rack.pos[coord[1]][0]+33&&clickedTile.y == user.rack.pos[coord[1]][1]+33){
                        user.rack.tiles[coord[1]]=null;
                        user.rack.letters[coord[1]]=0;
                    }
                    img[num-1]=clickedTile.img;
                    pos[num-1][0]=clickedTile.x-clickedTile.img.getWidth();
                    pos[num-1][1]=clickedTile.y-clickedTile.img.getHeight();
                    clickedTile.imgnum=num-1;
                    visibleTiles[i].imgnum=num-1;
                }
            }
            if(clickedTile!=null){
                rx=me.getPoint().x-clickedTile.x;
                ry=me.getPoint().y-clickedTile.y;
            }
            redraw();
        }
        public void mouseDragged(MouseEvent me){
            if(!userTurn||submitClicked||exchangeClicked||passClicked||exchangeSubmitClicked||exchangeCancelClicked||recallClicked||shuffleClicked||ended)return;
            if(clickedTile!=null){
                int x = me.getPoint().x - rx;
                int y = me.getPoint().y - ry;
                clickedTile.x=x;
                clickedTile.y=y;
                pos[num-1][0]=x-clickedTile.img.getWidth();
                pos[num-1][1]=y-clickedTile.img.getHeight();
                redraw();
            }
        }
        public void mouseReleased(MouseEvent me) {
            if(!userTurn||ended)return;
            if(exchangeClicked){
                recallTiles();
            }
            if(shuffleClicked){
                int[] visInd=new int[7];
                for(int i=0;i<7;i++)visInd[i]=-1;
                for(int i=0;i<tileNum;i++){
                    int[] coord=rack(visibleTiles[i].x, visibleTiles[i].y);
                    if(coord[0]!=-1){
                        visInd[coord[1]]=i;
                    }
                }
            }
            else if(exchangeCancelClicked){

                addMessages("It's now your turn", 5);
                for(int i=0;i<tileNum;i++){
                    if(visibleTiles[i].inRack&&visibleTiles[i].frozen){
                        visibleTiles[i].frozen=false;
                        int[] coord=rack(visibleTiles[i].x, visibleTiles[i].y);
                        draw[ind].draw[visibleTiles[i].imgnum]=visibleTiles[i].img;
                        draw[ind].pos[visibleTiles[i].imgnum][0]=visibleTiles[i].x-visibleTiles[i].img.getWidth();
                        draw[ind].pos[visibleTiles[i].imgnum][1]=visibleTiles[i].y-visibleTiles[i].img.getHeight();
                    }
                }
                exchangeClicked=false;

            }
            else if(exchangeSubmitClicked){

                for(int i=0;i<tileNum;i++){
                    if(visibleTiles[i].img.getWidth()!=draw[ind].draw[visibleTiles[i].imgnum].getWidth()&&visibleTiles[i].frozen){
                        visibleTiles[i].img=nothing;
                        int[] coord=rack(visibleTiles[i].x, visibleTiles[i].y);
                        user.rack.letters[coord[1]]=0;
                        user.rack.cur--;
                        user.rack.tiles[coord[1]]=null;
                        visibleTiles[i].x=10000;
                        visibleTiles[i].y=10000;
                        bag.add((int)(visibleTiles[i].c-'a'));
                    }
                }
                exchangeClicked=false;
                shuffleBag();
                try {
                    endTurn();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            else if(passClicked){
                recallTiles();
                try {
                    endTurn();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(recallClicked){
                recallTiles();
            }
            else if(submitClicked){
                if(board.isValid(userPlay)){
                    recentScore=board.score(userPlay);
                    for(int i=0;i<15;i++){
                        for(int j=0;j<15;j++){
                            if(userPlay[i][j]!=0)user.rack.cur--;
                        }
                    }
                    board.play(userPlay);
                    try {
                        endTurn();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    for(int i=0;i<tileNum;i++){
                        int[] coord=board(visibleTiles[i].x, visibleTiles[i].y);
                        if(coord[0]!=-1&&!visibleTiles[i].frozen&&visibleTiles[i].x == PosBoard[coord[1]][coord[0]][0]+38&&visibleTiles[i].y == PosBoard[coord[1]][coord[0]][1]+15){
                            userPlay[coord[1]][coord[0]]=0;
                            returnTile(visibleTiles[i], i);
                        }
                    }
                }
            }
            else if (clickedTile!=null) {
                //places tile onto board/rack and locks into place

                int[] coord = board(me.getPoint().x - rx, me.getPoint().y - ry);
                if(coord[0]==-1&&coord[1]==-1){
                    coord=rack(me.getPoint().x-rx, me.getPoint().y-ry);
                    if(coord[0]==-1&&coord[1]==-1){
                        returnTile(clickedTile, tileInd);
                    }
                    else{
                        if(user.rack.letters[coord[1]]==0)placeTile(clickedTile, coord[1]);
                        else returnTile(clickedTile, tileInd);
                    }
                }
                else{
                    clickedTile.x = PosBoard[coord[1]][coord[0]][0]+38;
                    clickedTile.y = PosBoard[coord[1]][coord[0]][1]+15;
                    pos[num - 1][0] = clickedTile.x - clickedTile.img.getWidth();
                    pos[num - 1][1] = clickedTile.y - clickedTile.img.getHeight();
                    if(board.board[coord[1]][coord[0]]!=0||userPlay[coord[1]][coord[0]]!=0)returnTile(clickedTile, tileInd);
                    else userPlay[coord[1]][coord[0]]=clickedTile.c;
                    visibleTiles[tileInd]=clickedTile;
                }

            }
            redraw();
            submitClicked=false;
            passClicked=false;
            exchangeSubmitClicked=false;
            exchangeCancelClicked=false;
            recallClicked=false;
            shuffleClicked=false;
            clickedTile=null;
        }
    }
    public static void main(String[] args) throws IOException{
        Menu menuScreen = new Menu();
    }
}