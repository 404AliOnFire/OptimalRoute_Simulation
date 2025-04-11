package minimumcost_prj.ProjectCode;

public class test {
    public static String get2DArrayPrint(int[][] matrix) {
        String output = "";  // يفضل استخدام "" بدلاً من new String() لتمثيل السلسلة الفارغة
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                // أغلق القوس بعد عملية إضافة العنصر
                output = output + (matrix[i][j] == Integer.MAX_VALUE ? "∞" : matrix[i][j]) + "\t";
            }
            // إضافة خط جديد بعد كل صف
            output = output + "\n";
        }
        return output;
    }

}
