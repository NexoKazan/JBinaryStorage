import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class main {
    public static void main(String[] args) throws Exception {

//        TableRow tr = new TableRow(new Object[] {"test", 1}, new ColumnType[] {ColumnType.STRING, ColumnType.INTEGER});
//        tr.Serialize(new FileOutputStream("test\\testing"));
//        TableBinaryStorage tbsw = new TableBinaryStorage("testing","test","rw");
//        tbsw.AddData(tr.Columns());
//

        //String[] tableNames = new String[]{"customer", "nation",  "part", "partsupp", "region", "supplier", "lineitem", "orders"};
        String[] tableNames = new String[]{"lineitem"};
        //PrintStream out = new PrintStream(new FileOutputStream("LOG.txt"));
        //System.setErr(out);
        long startTime = System.currentTimeMillis();
        //System.err.println(new Date (System.currentTimeMillis()));
        for ( String tableName : tableNames ) {
            System.out.println(tableName);
            TableBinaryStorage tbsr = new TableBinaryStorage(tableName,"5gb","r");
            WriteTBStoConsole(tbsr, 100000000);
            tbsr.close();
        }
        long workTime = System.currentTimeMillis() - startTime;
        //System.out.println(workTime);
        //System.err.println(new Date (System.currentTimeMillis()));
        //System.err.println(workTime);
        //CreateBinFiles( tableNames );
        //CreateBinFiles(  new String[] { "part"});

    }

    private static void CreateBinFiles(String[] tableNames ) throws IOException {
        System.out.println(new Date (System.currentTimeMillis()));
        for ( String tableName : tableNames ) {
            TableBinaryStorage tbsW = new TableBinaryStorage( tableName, "test", "rw" );
            BufferedReader reader = new BufferedReader( new FileReader( "test\\" + tableName + ".tbl" ) );
            String line = reader.readLine( );
            System.out.println(tableName+ "........Started!");
            while (line != null) {
                var parts = line.split( "\\|" );
                var row = new Object[tbsW.Meta().Columns.length];
                for ( var i = 0; i < tbsW.Meta().Columns.length; i++ ) {
                    switch (tbsW.Meta().Types[i])
                    {
                        case LONG -> row[i] = Long.parseLong(parts[i]);
                        case INTEGER -> row[i] = Integer.parseInt(parts[i]);
                        case FLOAT -> row[i] = Float.parseFloat(parts[i]);
                        case DOUBLE -> row[i] = Double.parseDouble(parts[i]);
                        case BOOLEAN -> row[i] = Boolean.parseBoolean(parts[i]);
                        case STRING -> row[i] = parts[i];
                        case DATE, TIME, DATETIME -> {

                            SimpleDateFormat f = new SimpleDateFormat ( "yyyy-MM-DD" );
                            try {
                                Date d = f.parse( parts[i] );
                                row[i] = d.getTime ();
                            } catch (ParseException e) {
                                e.printStackTrace ( );
                            }
                        }
                        default -> {}
                    }
                }
                tbsW.AddData( row );
                line = reader.readLine( );
            }
            reader.close( );
            System.out.println(tableName + "........Done!");
        }
        System.out.println(new Date (System.currentTimeMillis()));
    }

    private static void WriteTBStoConsole(TableBinaryStorage tbsr, int linesCount) throws IOException, ClassNotFoundException {
        var data = tbsr.ReadNextRow();
        int j = 0;
        while (j < linesCount && data[ 0 ] != null) {
//            for ( int i = 0; i < data.length; i++ ) {
//                System.out.print(data[ i ]);
//                if(i != data.length - 1) System.out.print("|");
//            }
            j++;
//            System.out.println();
            data = tbsr.ReadNextRow();

        }
    }
}
