import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileHandler {

    public static void exportMesh(File file, int [][] tab, int x, int y) {

        String path = file.getAbsolutePath();
        PrintWriter out = null;
        try{
            out = new PrintWriter(path);
            out.println(String.format("%d %d",x, y));
            for(int i=0 ; i < x ; i++){
                for (int j=0 ; j < y ; j++) {
                    out.println(String.format("%d %d %d %d",i, j, 0, tab[i][j]));
                }
            }
        }catch(IOException e) {
            System.out.println(e.getMessage());
        }
        finally {
            if(out!=null) {
                out.close();
            }
        }

    }

    public static Mesh importMesh(File file) {
        String path = file.getAbsolutePath();
        BufferedReader br = null;
        List<String> lines = new ArrayList<String>();
        Mesh mesh = null;
        int last_id = 0;

        try {
            br = new BufferedReader(new FileReader(path));
            String line = br.readLine();

            while (line != null) {
                lines.add(line);
                line = br.readLine();
            }
            String[] first_line = lines.get(0).split(" ");
            lines.remove(0);
            int x_size = Integer.parseInt(first_line[0]);
            int y_size = Integer.parseInt(first_line[1]);

            int tab[][] = new int[x_size][y_size];
            Iterator<String> lines_it = lines.iterator();

            for(int i = 0 ; i < lines.size() ; i++) {
                String[] split_line = lines.get(i).split(" ");
                int x = Integer.parseInt(split_line[0]);
                int y = Integer.parseInt(split_line[1]);
                int phase = Integer.parseInt(split_line[2]);
                int id = Integer.parseInt(split_line[3]);
                tab[x][y] = id;
                if(i == lines.size() - 1) {
                    last_id = Integer.parseInt(split_line[2]);
                }
            }

            mesh = new Mesh(x_size, y_size, tab, last_id);
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }finally {
            if(br!=null) {
                try {
                    br.close();
                }catch (IOException e){
                    System.out.println(e.getMessage());
                }
            }
        }

        return mesh;

    }
}
