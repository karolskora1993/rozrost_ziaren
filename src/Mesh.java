import com.sun.org.apache.xml.internal.security.utils.resolver.implementations.ResolverAnonymous;
import com.sun.org.apache.xml.internal.serialize.Method;
import com.sun.org.apache.xml.internal.serializer.utils.SystemIDResolver;
import com.sun.tools.hat.internal.util.ArraySorter;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by apple on 24.04.2016.
 */
public class Mesh {

    enum Method {MOORE, VON_NEUMANN, HEX_LEFT, HEX_RIGHT, HEX_RAND, PENT, RAND_CA}


    private int x = 400;
    private int y = 400;
    private int[][] tab = new int[x][y];
    private boolean period = false;
    private boolean started = false;
    private Method method = Method.MOORE;
    private int nextId = 1;

    private int randomCaR=5;


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        tab = new int[x][y];

    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        tab = new int[x][y];

    }

    public Mesh() {
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                this.tab[i][j] = 0;
            }
        }
    }

    public Mesh(int x, int y, int[][] tab, int next_id) {
        this.x = x;
        this.y = y;
        this.tab = tab;
        this.nextId = next_id;
    }

    public void clear() {
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                this.tab[i][j] = 0;
            }
        }
        nextId = 1;
        System.out.println("Wyczysc");
    }

    public int[][] getTab() {
        return tab;
    }

    public void setTab(int[][] tab) {
        this.tab = tab;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        System.out.println("Zmiana metody: " + method.name());
        this.method = method;
    }

    public boolean isPeriod() {
        return period;
    }

    public void setPeriod(boolean period) {
        this.period = period;

        System.out.println("Periodyczne: " + this.period);


    }

    public void setAlive(int x, int y) {
        if (this.tab[x][y] == 0) {
            this.tab[x][y] = getNextId();
            nextId++;
        } else
            this.tab[x][y] = 0;

    }

    public int isAlive(int x, int y) {
        if (tab[x][y] != 0)
            return 1;
        else
            return 0;

    }

    public int getId(int i, int j) {
        return tab[i][j];
    }

    public void generateRand(int ammount) {
        System.out.println("===============================");

        System.out.println("Generowanie  losowe");

        int size = new Random().nextInt(ammount);
        size += 4;

        System.out.println("Ilosc wylosowanych zarodkow do generowania: " + size);

        int a, b;

        for (int i = 0; i < size; i++) {
            do {
                a = new Random().nextInt(x-1);
                b = new Random().nextInt(y-1);
            }
            while (tab[a][b] != 0);

            tab[a][b] = getNextId();

        }

        System.out.println("Wygenerowano losowe");
    }


    public boolean isStarted() {
        return started;
    }

    public void start() {
        this.started = true;
    }

    public void stop() {
        this.started = false;
    }


    public void nextRound(){
        if(method== Method.RAND_CA)
            nextCA();
        else
            next();
    }

    private int[][] generateTempTab(int i, int j)
    {
        int[][] temp = new int[3][3];

        if (period) {
            if (i == 0 && j == 0) {
                temp[0][0] = tab[x - 1][y - 1];
                temp[0][1] = tab[x - 1][0];
                temp[0][2] = tab[x - 1][1];
                temp[1][0] = tab[0][y - 1];
                temp[1][2] = tab[0][1];
                temp[2][0] = tab[1][y - 1];
                temp[2][1] = tab[1][0];
                temp[2][2] = tab[1][1];
            } else if (i == 0 && j != 0 && j != (y - 1)) {
                temp[0][0] = tab[x - 1][j - 1];
                temp[0][1] = tab[x - 1][j];
                temp[0][2] = tab[x - 1][j + 1];
                temp[1][0] = tab[0][j - 1];
                temp[1][2] = tab[0][j];
                temp[2][0] = tab[1][j - 1];
                temp[2][1] = tab[1][j];
                temp[2][2] = tab[1][j + 1];
            } else if (i == 0 && j == (y - 1)) {
                temp[0][0] = tab[x - 1][j - 1];
                temp[0][1] = tab[x - 1][j];
                temp[0][2] = tab[x - 1][0];
                temp[1][0] = tab[0][j - 1];
                temp[1][2] = tab[0][0];
                temp[2][0] = tab[1][j - 1];
                temp[2][1] = tab[1][j];
                temp[2][2] = tab[1][0];
            } else if (i == (x - 1) && j == 0) {
                temp[0][0] = tab[i - 1][y - 1];
                temp[0][1] = tab[i - 1][j];
                temp[0][2] = tab[i - 1][j + 1];
                temp[1][0] = tab[i][y - 1];
                temp[1][2] = tab[i][j + 1];
                temp[2][0] = tab[0][y - 1];
                temp[2][1] = tab[0][j];
                temp[2][2] = tab[0][j + 1];
            } else if (i == (x - 1) && j != 0 && j != (y - 1)) {
                temp[0][0] = tab[i - 1][j - 1];
                temp[0][1] = tab[i - 1][j];
                temp[0][2] = tab[i - 1][j + 1];
                temp[1][0] = tab[i][j - 1];
                temp[1][2] = tab[i][j + 1];
                temp[2][0] = tab[0][j - 1];
                temp[2][1] = tab[0][j];
                temp[2][2] = tab[0][j + 1];
            } else if (i != 0 && j == (y - 1) && i != (x - 1)) {
                temp[0][0] = tab[i - 1][j - 1];
                temp[0][1] = tab[i - 1][j];
                temp[0][2] = tab[i - 1][0];
                temp[1][0] = tab[i][j - 1];
                temp[1][2] = tab[i][0];
                temp[2][0] = tab[i + 1][j - 1];
                temp[2][1] = tab[i + 1][j];
                temp[2][2] = tab[i + 1][0];
            } else if (i == (x - 1) && j == (y - 1)) {
                temp[0][0] = tab[i - 1][j - 1];
                temp[0][1] = tab[i - 1][j];
                temp[0][2] = tab[i - 1][0];
                temp[1][0] = tab[i][j - 1];
                temp[1][2] = tab[i][0];
                temp[2][0] = tab[0][j - 1];
                temp[2][1] = tab[0][j];
                temp[2][2] = tab[0][0];
            } else if (i != 0 && j == 0 && i != (x - 1)) {
                temp[0][0] = tab[i - 1][y - 1];
                temp[0][1] = tab[i - 1][0];
                temp[0][2] = tab[i - 1][1];
                temp[1][0] = tab[i][y - 1];
                temp[1][2] = tab[i][1];
                temp[2][0] = tab[i + 1][y - 1];
                temp[2][1] = tab[i + 1][0];
                temp[2][2] = tab[i + 1][1];
            } else {
                for (int k = -1; k < 2; k++) {
                    for (int l = -1; l < 2; l++) {
                        temp[k + 1][l + 1] = tab[i + k][j + l];
                    }
                }
            }

        } else {
            if (i == 0 && j == 0) {
                for (int k = 0; k < 2; k++) {
                    for (int l = 0; l < 2; l++)
                        temp[k + 1][l + 1] = tab[i + k][j + l];
                }
            } else if (i == 0 && j != 0 && j != (y - 1)) {
                for (int k = 0; k < 2; k++) {
                    for (int l = -1; l < 2; l++)
                        temp[k + 1][l + 1] = tab[i + k][j + l];
                }
            } else if (i == 0 && j == (y - 1)) {
                for (int k = 0; k < 2; k++) {
                    for (int l = -1; l < 1; l++)
                        temp[k + 1][l + 1] = tab[i + k][j + l];
                }
            } else if (i == (x - 1) && j == 0) {
                for (int k = -1; k < 1; k++) {
                    for (int l = 0; l < 2; l++)
                        temp[k + 1][l + 1] = tab[i + k][j + l];
                }
            } else if (i == (x - 1) && j != 0 && j != (y - 1)) {
                for (int k = -1; k < 1; k++) {
                    for (int l = -1; l < 2; l++)
                        temp[k + 1][l + 1] = tab[i + k][j + l];
                }
            } else if (i != 0 && j == (y - 1) && i != (x - 1)) {
                for (int k = -1; k < 2; k++) {
                    for (int l = -1; l < 1; l++)
                        temp[k + 1][l + 1] = tab[i + k][j + l];
                }
            } else if (i == (x - 1) && j == (y - 1)) {
                for (int k = -1; k < 1; k++) {
                    for (int l = -1; l < 1; l++)
                        temp[k + 1][l + 1] = tab[i + k][j + l];
                }
            } else if (i != 0 && j == 0 && i != (x - 1)) {
                for (int k = -1; k < 2; k++) {
                    for (int l = 0; l < 2; l++) {
                        temp[k + 1][l + 1] = tab[i + k][j + l];
                        temp[0][0]=0;
                        temp[1][0]=0;
                        temp[2][0]=0;
                    }
                }
            } else {
                for (int k = -1; k < 2; k++) {
                    for (int l = -1; l < 2; l++)
                        temp[k + 1][l + 1] = tab[i + k][j + l];
                }
            }
        }
        switch (method) {

            case VON_NEUMANN:
                temp[0][0] = 0;
                temp[2][0] = 0;
                temp[0][2] = 0;
                temp[2][2] = 0;
                break;
            case HEX_LEFT:
                temp[0][2] = 0;
                temp[2][0] = 0;
                break;
            case HEX_RIGHT:
                temp[0][0] = 0;
                temp[2][2] = 0;
                break;
            case HEX_RAND: {
                Random rand = new Random();
                int choice = rand.nextInt(2);
                if (choice == 0) {
                    temp[0][2] = 0;
                    temp[2][0] = 0;
                } else {
                    temp[0][0] = 0;
                    temp[2][2] = 0;
                }
            }
            break;
            case PENT: {
                Random rand = new Random();
                int choice = rand.nextInt(4);
                if (choice == 0) {
                    temp[0][2] = 0;
                    temp[1][2] = 0;
                    temp[2][2] = 0;
                } else if (choice == 1) {
                    temp[0][0] = 0;
                    temp[1][0] = 0;
                    temp[2][0] = 0;
                } else if (choice == 2) {
                    temp[2][0] = 0;
                    temp[2][1] = 0;
                    temp[2][2] = 0;
                } else if (choice == 3) {
                    temp[0][0] = 0;
                    temp[0][1] = 0;
                    temp[0][2] = 0;
                }
            }
            break;
        }

        return temp;
    }

    private void next() {
        int[][] nextStep = new int[x][y];

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++)
                nextStep[i][j] = tab[i][j];
        }


        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {

                if (tab[i][j] == 0) {

                    int[][] temp = generateTempTab(i,j);

                    HashMap<Integer, Integer> neighbours = new HashMap<Integer, Integer>();

                    for (int k = 0; k < 3; k++) {
                        for (int l = 0; l < 3; l++) {
                            Integer id = temp[k][l];
                            if (id != 0) {
                                if (neighbours.containsKey(id)) {
                                    Integer value = neighbours.get(id);
                                    value++;
                                    neighbours.remove(id);
                                    neighbours.put(id, value);
                                } else
                                    neighbours.put(id, 1);
                            }
                        }
                    }
                    Integer max = 0;
                    Integer id = 0;
                    for (Integer k : neighbours.keySet()) {
                        if (neighbours.get(k) > max)
                            id = k;
                    }

                    nextStep[i][j] = id;
                }
            }
        }
        tab = nextStep;
    }

    private void nextCA(){

        int[][] nextStep = new int[x][y];

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++)
                nextStep[i][j] = tab[i][j];
        }


        for(int a=0; a<x; a++){
            for(int b=0; b<y; b++){
                if(tab[a][b]==0) {
                    int tmp[][];
                    tmp = new int[2 * randomCaR + 1][2 * randomCaR + 1];


                    for (int i = 0; i < 2 * randomCaR + 1; i++) {
                        for (int j = 0; j < 2 * randomCaR + 1; j++) {
                            float r_tmp = (float) (Math.sqrt(Math.pow(randomCaR - i, 2) + Math.pow(randomCaR - j, 2)));

                            if (randomCaR < r_tmp) {
                                tmp[i][j] = 0;

                            } else {

                                int xl = (a - randomCaR + i) % x;
                                xl = xl < 0 ? x - 1 : xl;

                                int yl = (b - randomCaR + j) % y;
                                yl = yl < 0 ? y - 1 : yl;

                                if (period) {
                                    tmp[i][j] = tab[xl][yl];
                                } else {
                                    if ((a - randomCaR + i) >= 0 && (a - randomCaR + i) < x && (b - randomCaR + j) >= 0 && (b - randomCaR + j) < y) {
                                        tmp[i][j] = tab[xl][yl];
                                    } else {
                                        tmp[i][j] = 0;
                                    }
                                }

                            }
                        }
                    }

                    HashMap<Integer, Integer> neighbours = new HashMap<Integer, Integer>();

                    for (int k = 0; k < 2 * randomCaR + 1; k++) {
                        for (int l = 0; l < 2 * randomCaR + 1; l++) {
                            Integer id = tmp[k][l];
                            if (id != 0) {
                                if (neighbours.containsKey(id)) {
                                    Integer value = neighbours.get(id);
                                    value++;
                                    neighbours.remove(id);
                                    neighbours.put(id, value);
                                } else
                                    neighbours.put(id, 1);
                            }
                        }
                    }
                    Integer max = 0;
                    Integer id = 0;
                    for (Integer k : neighbours.keySet()) {
                        if (neighbours.get(k) > max)
                            id = k;
                    }

                    nextStep[a][b] = id;
                }
            }
        }
        tab=nextStep;
    }


    public boolean isFilled() {

        boolean filled = true;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (tab[i][j] == 0) {
                    filled = false;
                    break;
                }
            }
            if (filled == false)
                break;
        }
        if(filled==true)
            started = false;

        return filled;
    }


    public int getNextId() {
        nextId++;

        return nextId;
    }

}
