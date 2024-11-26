import java.awt.geom.Point2D;

public class OptimalTriangulation {
    // Площа трикутника
    private static double area(Point2D a, Point2D b, Point2D c) {
        return Math.abs(a.getX() * (b.getY() - c.getY()) +
                b.getX() * (c.getY() - a.getY()) +
                c.getX() * (a.getY() - b.getY())) / 2.0;
    }

    // Довжина діагоналі
    private static double diagonalLength(Point2D a, Point2D b) {
        return Math.sqrt(
                Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2)
        );
    }

    public static double findTriangulation(Point2D[] points, boolean minimizeArea) {
        int len = points.length;
        if (len < 3) {
            return 0;
        }

        double[][] minimumCosts = new double[len][len]; // Мінімальні вартості
        int[][] optimalVertices = new int[len][len];   // Оптимальні вершини

        for (int gap = 2; gap < len; gap++) {
            for (int i = 0; i + gap < len; i++) {
                int j = i + gap;
                minimumCosts[i][j] = Double.MAX_VALUE;

                for (int k = i + 1; k < j; k++) {
                    double cost;
                    if (minimizeArea) {
                        cost = minimumCosts[i][k] + minimumCosts[k][j] + area(points[i], points[k], points[j]);
                    } else {
                        cost = minimumCosts[i][k] + minimumCosts[k][j] + diagonalLength(points[i], points[k]) + diagonalLength(points[k], points[j]);
                    }

                    if (cost < minimumCosts[i][j]) {
                        minimumCosts[i][j] = cost;
                        optimalVertices[i][j] = k; // Оптимальна точка
                    }
                }
            }
        }

        System.out.println("\nОптимальні трикутники:");
        printTriangles(optimalVertices, points, 0, len - 1);

        return minimumCosts[0][len - 1];
    }

    // Рекурсивна функція для відновлення трикутників
    private static void printTriangles(int[][] optimalVertices, Point2D[] points, int i, int j) {
        if (j <= i + 1) {
            return; // Трикутник неможливий
        }
        int k = optimalVertices[i][j];
        System.out.printf("Трикутник: (%.1f, %.1f), (%.1f, %.1f), (%.1f, %.1f)\n",
                points[i].getX(), points[i].getY(),
                points[k].getX(), points[k].getY(),
                points[j].getX(), points[j].getY()
        );
        printTriangles(optimalVertices, points, i, k);
        printTriangles(optimalVertices, points, k, j);
    }

    public static void main(String[] args) {
        Point2D[] points = {
                new Point2D.Double(2, 1),
                new Point2D.Double(2, 4),
                new Point2D.Double(3, 5)
        };

        double areaResult = findTriangulation(points, true);
        System.out.println("Мінімальна вартість тріангуляції за площею: " + areaResult);

        double lengthResult = findTriangulation(points, false);
        System.out.println("Мінімальна вартість тріангуляції за довжиною діагоналей: " + lengthResult);
    }
}
