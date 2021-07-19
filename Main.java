import java.util.Scanner;

public class Main {
    static final int BOARD_HEIGHT = 8;
    static final int BOARD_WIDGH = 8;
    static int[][] cells = new int[BOARD_HEIGHT][BOARD_WIDGH];
    static int[][] directions = {
        {0,-1}, // UP
        {1,-1}, // UP_RIGHT
        {1,0}, // RIGHT
        {1,1}, // DOWN_RIGHT
        {0,1}, // DOWN
        {-1,1}, // DOWN_LEFT
        {-1,0}, // LEFT
        {-1,-1} // UP_LEFT
    };
    static int blackNum = 0;
    static int whiteNum = 0;

    static int turn = 0;
    static boolean canPut = true;
    static String[] colors = {"Black", "White", "None"};
    static int cursorX = 0;
    static int cursorY = 0;


    public static void main(String[] args) {
        for (int y=0; y<BOARD_HEIGHT; y++) {
            for (int x=0; x<BOARD_HEIGHT; x++) {
                cells[y][x] = 2;
            }
            cells[3][3] = 0;
            cells[4][4] = 0;
            cells[3][4] = 1;
            cells[4][3] = 1;

        }
        while (true) {
            drawBoard();
            System.out.println(colors[turn] + " turn.");
            if (!canPut) {
                System.out.println("Can't put!");
                canPut = true;
            }
            String inputKey = new Scanner(System.in).nextLine();
                switch (inputKey) {
                    case "r": 
                    case "e": if(cursorY > 0) {cursorY--;} break;
                    case "d":
                    case "f": if(cursorX > 0) {cursorX--;} break;
                    case "j":
                    case "k": if(cursorX < BOARD_WIDGH-1) {cursorX++;} break;
                    case "c":
                    case "v": if(cursorY < BOARD_HEIGHT-1) {cursorY++;} break;
                    default :
                        if (checkCanPut(turn, cursorX, cursorY, false)) {
                            checkCanPut(turn, cursorX, cursorY, true);
                            turn = turn^1;
                        } else canPut = false;
                        if (!checkCanPutAll(turn) && !checkCanPutAll(turn^1)){
                            drawBoard();
                            System.out.println("Game set!!");
                            if (blackNum > whiteNum) {
                                System.out.println("Black win!!");
                            } else {
                                System.out.println("White win!!");
                            }
                            cursorX = 0;
                            cursorY = 0;
                            inputKey = new Scanner(System.in).nextLine();
                            main(args);
                        }
                        break;
                    }
                }
            }

    static boolean checkCanPut(int _color, int _x, int _y, boolean turnOver) {
        if (cells[_y][_x] != 2){
            return false;
        }
        for (int i=0;i<directions.length;i++) {
            int x = _x, y = _y;
            x += directions[i][0];
            y += directions[i][1];
            if (x<0 || x>=BOARD_WIDGH || y<0 || y>=BOARD_HEIGHT) {
                continue;
            }
            if (cells[y][x] != (_color^1)) {
                continue;
            }
            while (true) {
                x += directions[i][0];
                y += directions[i][1];
                // 範囲外なら次へ
                if (x<0 || x>=BOARD_WIDGH || y<0 || y>=BOARD_HEIGHT) {
                    break;
                }
                // 空白なら次へ
                if (cells[y][x] == 2) {
                    break;
                }
                if (cells[y][x] == _color) {
                    if (!turnOver) {
                        return true;
                    }
                    int x2 = _x;
                    int y2 = _y;
                    while (true) {
                        cells[y2][x2] = _color;
                        x2 += directions[i][0];
                        y2 += directions[i][1];
                        if (x2 == x && y2 == y) {
                            break;
                        }
                    }
                }
            }
        }
        return false;
    }

    static boolean checkCanPutAll(int color) {
        blackNum = 0;
        whiteNum = 0;
        for (int y=0; y<BOARD_HEIGHT; y++) {
            for (int x=0; x<BOARD_HEIGHT; x++) {
                if (cells[y][x] == 2) {
                    // 何もしない
                } else if (cells[y][x] == color) {
                    blackNum++;
                } else if (cells[y][x] != color) {
                    whiteNum++;
                }
                if (checkCanPut(color, x, y, false)) {
                    return true;
                }
            }
        }
        return false;
    }
    static void drawBoard() {
        System.out.print("\033[H\033[2J");
        for (int y=0; y<BOARD_HEIGHT; y++) {
            for (int x=0; x<BOARD_HEIGHT; x++) {
                if (x == cursorX && y == cursorY) {
                    System.out.print("＊");
                } else {
                    switch (colors[cells[y][x]]) {
                        case "Black": System.out.print("⚪️"); break;
                        case "White": System.out.print("⚫️"); break;
                        case "None": System.out.print("・"); break;
                    }
                }
            }
            System.out.println();
        }
    }
}