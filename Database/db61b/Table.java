package db61b;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static db61b.Utils.*;

/** A single table in a database.
 *  @author P. N. Hilfinger
 */
class Table implements Iterable<Row> {
    /** A new Table whose columns are given by COLUMNTITLES, which may
     *  not contain dupliace names. */
    Table(String[] columnTitles) {
        for (int i = columnTitles.length - 1; i >= 1; i -= 1) {
            for (int j = i - 1; j >= 0; j -= 1) {
                if (columnTitles[i].equals(columnTitles[j])) {
                    throw error("duplicate column name: %s",
                                columnTitles[i]);
                }
            }
        }
        _columnTitles = columnTitles;
    }

    /** A new Table whose columns are give by COLUMNTITLES. */
    Table(List<String> columnTitles) {
        this(columnTitles.toArray(new String[columnTitles.size()]));
    }

    /** Return the number of columns in this table. */
    public int columns() {
        return _columnTitles.length;
    }

    /** Return the title of the Kth column.  Requires 0 <= K < columns(). */
    public String getTitle(int k) {
        return _columnTitles[k];
    }

    /** Return the number of the column whose title is TITLE, or -1 if
     *  there isn't one. */
    public int findColumn(String title) {
        if (Arrays.asList(_columnTitles).contains(title)) {
            for (int i = 0; i < columns(); i++) {
                if (_columnTitles[i].equals(title)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /** Return the number of Rows in this table. */
    public int size() {
        return _rows.size();
    }

    /** Returns an iterator that returns my rows in an unspecfied order. */
    @Override
    public Iterator<Row> iterator() {
        return _rows.iterator();
    }

    /** Add ROW to THIS if no equal row already exists.  Return true if anything
     *  was added, false otherwise. */
    public boolean add(Row row) {
        if (_rows.contains(row)) {
            return false;
        } else if (row.size() != columns()) {
            throw error("inserted row has wrong length");
        } else {
            _rows.add(row);
            return true;
        }
    }

    /** Read the contents of the file NAME.db, and return as a Table.
     *  Format errors in the .db file cause a DBException. */
    static Table readTable(String name) {
        BufferedReader input;
        Table table;
        input = null;
        table = null;
        try {
            input = new BufferedReader(new FileReader(name + ".db"));
            String header = input.readLine();
            if (header == null) {
                throw error("missing header in DB file");
            }
            String[] columnNames = header.split(",");
            table = new Table(columnNames);
            header = input.readLine();
            while (header != null) {
                columnNames = header.split(",");
                Row row = new Row(columnNames);
                table.add(row);
                header = input.readLine();
            }

        } catch (FileNotFoundException e) {
            throw error("could not find %s.db", name);
        } catch (IOException e) {
            throw error("problem reading from %s.db", name);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    /* Ignore IOException */
                }
            }
        }
        return table;
    }

    /** Write the contents of TABLE into the file NAME.db. Any I/O errors
     *  cause a DBException. */
    void writeTable(String name) {
        PrintStream output;
        output = null;
        Iterator<Row> itr = iterator();
        int finalVal = columns() - 1;
        try {
            String sep;
            sep = "";
            output = new PrintStream(name + ".db");
            for (int i = 0; i < finalVal; i++) {
                output.append(_columnTitles[i] + ",");
            }
            output.append(_columnTitles[finalVal]);
            output.append("\n");
            while (itr.hasNext()) {
                Row row = itr.next();
                for (int i = 0; i < finalVal; i++) {
                    output.append(row.get(i) + ",");
                }
                output.append(row.get(finalVal));
                output.append("\n");
            }
        } catch (IOException e) {
            throw error("trouble writing to %s.db", name);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    /** Print my contents on the standard output. */
    void print() {
        Iterator<Row> itr = iterator();
        while (itr.hasNext()) {
            Row row = itr.next();
            System.out.print("  ");
            for (int i = 0; i < columns(); i++) {
                System.out.print(row.get(i) + " ");
            }
            System.out.println();
        }
    }

    /** Return a new Table whose columns are COLUMNNAMES, selected from
     *  rows of this table that satisfy CONDITIONS. */
    Table select(List<String> columnNames, List<Condition> conditions) {
        Table result = new Table(columnNames);
        Iterator<Row> itr = iterator();
        List<Column> lstCol = new ArrayList<Column>();
        for (int i = 0; i < columnNames.toArray().length; i++) {
            Column col = new Column(columnNames.get(i), this);
            lstCol.add(col);
        }
        if (conditions.isEmpty()) {
            while (itr.hasNext()) {
                Row r = new Row(lstCol, itr.next());
                result.add(r);
            }
        } else {
            while (itr.hasNext()) {
                Row next = itr.next();
                if (Condition.test(conditions, next)) {
                    Row r = new Row(lstCol, next);
                    result.add(r);
                }
            }
        }
        return result;
    }

    /** Return a new Table whose columns are COLUMNNAMES, selected
     *  from pairs of rows from this table and from TABLE2 that match
     *  on all columns with identical names and satisfy CONDITIONS. */
    Table select(Table table2, List<String> columnNames,
                 List<Condition> conditions) {
        Table result = new Table(columnNames);
        boolean columnMatch = false;
        Iterator<Row> itr1 = this.iterator();
        List<Column> lstCol = new ArrayList<Column>();
        List<Column> com1 = new ArrayList<Column>();
        List<Column> com2 = new ArrayList<Column>();
        for (int i = 0; i < columnNames.toArray().length; i++) {
            Column col = new Column(columnNames.get(i), this, table2);
            lstCol.add(col);
        }
        for (int i = 0; i < _columnTitles.length; i++) {
            for (int j = 0; j < table2._columnTitles.length; j++) {
                if (getTitle(i).equals(table2.getTitle(j))) {
                    Column col1 = new Column(this.getTitle(i), this);
                    Column col2 = new Column(table2.getTitle(j), table2);
                    com1.add(col1);
                    com2.add(col2);
                    columnMatch = true;
                }
            }
        }
        while (itr1.hasNext()) {
            Iterator<Row> itr2 = table2.iterator();
            Row nextRow = itr1.next();
            while (itr2.hasNext()) {
                Row nextRow2 = itr2.next();
                if (columnMatch) {
                    if (equijoin(com1, com2, nextRow, nextRow2)) {
                        if (conditions.isEmpty()) {
                            Row r = new Row(lstCol, nextRow, nextRow2);
                            result.add(r);
                        } else {
                            if (Condition.test(conditions, nextRow, nextRow2)) {
                                Row r = new Row(lstCol, nextRow, nextRow2);
                                result.add(r);
                            }
                        }
                    }
                } else {
                    if (conditions.isEmpty()) {
                        Row r = new Row(lstCol, nextRow, nextRow2);
                        result.add(r);
                    } else {
                        if (Condition.test(conditions, nextRow, nextRow2)) {
                            Row r = new Row(lstCol, nextRow, nextRow2);
                            result.add(r);
                        }
                    }
                }
            }
        }
        return result;
    }
    /** Return true if the columns COMMON1 from ROW1 and COMMON2 from
      * ROW2 all have identical values. Assumes that COMMON1 and
      * COMMON2 have the same number of elements and the same names,
      * that the columns in COMMON1 apply to this table, those in
      * COMMON2 to another, and that ROW1 and ROW2 come, respectively,
      * from those tables. */
    private static boolean equijoin(List<Column> common1, List<Column> common2,
                                    Row row1, Row row2) {
        boolean marked = true;
        for (int i = 0; i < common1.toArray().length; i++) {
            String first = common1.get(i).getFrom(row1);
            String second = common2.get(i).getFrom(row2);
            if (!first.equals(second)) {
                marked = false;
            }
        }
        return marked;
    }
    /** My rows. */
    private HashSet<Row> _rows = new HashSet<>();

    /** Column Titles of this Table. */
    private String[] _columnTitles;
}

