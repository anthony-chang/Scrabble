import java.util.HashMap;
import java.io.*;
import java.util.*;
class Board{
    public int sz=15;
    private char[][] board = new char[sz][sz];
    private int[][] scoring=new int[sz][sz];
    private HashMap<String, Boolean> dictionary= new HashMap<>();

    public Board(){
        for(int i=0;i<15;i++){
            for(int j=0;j<15;j++){
                board[i][j]='!';
            }
        }
        dictionary.put("", true);
    }
    private void inputDictionary() throws IOException{
        Scanner sc=new Scanner(new FileReader(new File("dictionary.txt")));
        while(sc.hasNextLine()){
            String str=sc.nextLine();
            dictionary.put(str, true);
        }
    }
    public boolean isValid(char[][] play) throws IOException {
        this.inputDictionary();
        for (int i = 0; i < 15; i++)
            for (int j = 0; j < 15; j++) {
                if (play[i][j] == 0) {
                    play[i][j] = '!';
                }

            }
        String wordHor, wordVer;
        int i;
        int firstCol = 15, lastCol = 0, firstRow = 15, lastRow = 0; //first and last occurrences of the player's move

        for (int row = 0; row < sz; row++)
            for (int col = 0; col < sz; col++) {
                if (play[row][col] != '!' && board[row][col] != '!') //if the tile is on top of another tile
                    return false;
                else if (play[row][col] != '!') {
                    if (play[row][col] != '!' && row < firstRow)
                        firstRow = row;
                    if (play[row][col] != '!' && row > lastRow)
                        lastRow = row;
                    if (play[row][col] != '!' && col < firstCol)
                        firstCol = col;
                    if (play[row][col] != '!' && col > lastCol)
                        lastCol = col;
                }
            }
            if (firstRow != lastRow && firstCol != lastCol) {
                return false;
            }
            if (firstCol != lastCol) //horizontal words
                for (int k = firstCol; k < lastCol; k++) {
                    if (play[firstRow][k] == '!') //so that there is no space between word
                        return false;
                    wordHor = "";
                    i = k;
                    while (play[firstRow][i] != '!') {
                        wordHor += play[firstRow][i];
                        i++;
                    }
                    if (dictionary.get(wordHor) == null) {
                        return false;
                    }
                    else break;
                }
            else if (firstRow != lastRow) //vertical words
                for (int k = firstRow; k < lastRow; k++) {
                    if (play[k][firstCol] == '!')
                        return false;
                    wordVer = "";
                    i = k;
                    while (play[i][firstCol] != '!') {
                        wordVer += play[i][firstCol];
                        i++;
                    }
                    if (dictionary.get((wordVer)) == null) {
                        return false;
                    }
                    else break;
                }
            else if (firstRow == lastRow && firstCol == lastCol)
                if (dictionary.get(play[firstRow][firstCol] + "") == null)
                    return false;
        return true;
    }
}

public class Test {
    public static void main(String[] args) throws IOException {
        Board thing = new Board();
        char[][] play = new char[][]{
                {0 ,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 't', 'e', 's', 't', 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        };
        System.out.println(thing.isValid(play));
    }
}
