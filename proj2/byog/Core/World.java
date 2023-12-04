package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class World {
    public TETile[][] generate(int width, int height, long seed) {
        TETile[][] T = new TETile[width][height];
        Random r = new Random(seed);
        init(T);
        initboard(T);
        double comp = r.nextDouble();
        double dens = r.nextDouble();
        int complexity = (int) (comp * (5 * (width + height)));
        int density = (int) (dens * (int) (width / 2) * (int) (height / 2));
        for (int i = 0; i < density; i++) {
            int x = r.nextInt((int) (width / 2)) * 2;
            int y = r.nextInt((int) (height / 2)) * 2;
            T[x][y] = Tileset.WALL;
            for (int j = 0; j < complexity; j++) {
                Position[] P = new Position[4];
                int size = 0;
                if (x > 1) {
                    P[size] = new Position(x - 2, y);
                    size++;
                }
                if (x < width - 2) {
                    P[size] = new Position(x + 2, y);
                    size++;
                }
                if (y > 1) {
                    P[size] = new Position(x, y - 2);
                    size++;
                }
                if (y < height - 2) {
                    P[size] = new Position(x, y + 2);
                    size++;
                }
                if (size > 0) {
                    Position p = P[r.nextInt(size)];
                    if (T[p.getX()][p.getY()].equals(Tileset.NOTHING)) {
                        T[p.getX()][p.getY()] = Tileset.WALL;
                        T[p.getX() + (int) ((x - p.getX()) / 2)][p.getY() + (int) ((y - p.getY()) / 2)] = Tileset.WALL;
                        x = p.getX();
                        y = p.getY();
                    }
                }
            }
        }
        initfloor(T);
        lockdoor(width, height, T, seed);
        return T;
    }

    private void init(TETile[][] T) {
        for (int i = 0; i < T.length; i++) {
            for (int j = 0; j < T[i].length; j++) {
                T[i][j] = Tileset.NOTHING;
            }
        }
    }

    private void initboard(TETile[][] T) {
        for (int i = 0; i < T.length; i++) {
            T[i][0] = Tileset.WALL;
            T[i][T[i].length - 1] = Tileset.WALL;
        }

        for (int i = 0; i < T[0].length; i++) {
            T[0][i] = Tileset.WALL;
            T[T.length - 1][i] = Tileset.WALL;
        }
    }

    private void initfloor(TETile[][] T) {
        for (int i = 0; i < T.length; i++) {
            for (int j = 0; j < T[i].length; j++) {
                if (T[i][j].equals(Tileset.NOTHING)) {
                    T[i][j] = Tileset.FLOOR;
                }
            }
        }
    }

    private boolean iswall(int x, int y, TETile[][] T) {
        Position[] p = new Position[4];
        int size = 0;
        if (x > 0) {
            p[size] = new Position(x - 1, y);
            size++;
        }
        if (x < T.length - 1) {
            p[size] = new Position(x + 1, y);
            size++;
        }
        if (y < T[0].length - 1) {
            p[size] = new Position(x, y + 1);
            size++;
        }
        if (y > 0) {
            p[size] = new Position(x, y - 1);
            size++;
        }
        for (int i = 0; i < size; i++) {
            if (!T[p[i].getX()][p[i].getY()].equals(Tileset.WALL)) {
                return false;
            }
        }
        return true;
    }

    private void lockdoor(int width, int height, TETile[][] T, long seed) {
        Random r = new Random(seed);
        while (true) {
            int x = r.nextInt(width);
            int y = r.nextInt(height);
            if (!iswall(x, y, T)) {
                T[x][y] = Tileset.LOCKED_DOOR;
                break;
            }
        }
        return;
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(80, 30);
        TETile[][] T = new World().generate(80, 30, 123);
        ter.renderFrame(T);
    }
}
