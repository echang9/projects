package db61b;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;


/** Test the Row, Table, and Database Classes for db.
 *  @author Eric Chang.
 */

public class BasicTests {

    /** Tests size method of Row class. */
    @Test
    public void testSize() {
        Row random = new Row(new String[]{"hello", "world", "I", "am", "here"});
        assertEquals(5, random.size());
    }

    /** Tests get method of Row class. */
    @Test
    public void testGet() {
        Row random = new Row(new String[]{"world", "I", "am", "here"});
        assertEquals("am", random.get(2));
        assertEquals("world", random.get(0));
    }

    /** Tests equals method to check if two Rows are the same. */
    @Test
    public void testEquals() {
        Row random = new Row(new String[]{"world", "I", "am", "here"});
        Row random2 = new Row(new String[]{"world", "I", "am", "here"});
        Row wrong = new Row(new String[]{"I", "am", "here"});
        assertTrue(random.equals(random2));
        assertFalse(random.equals(wrong));
    }

    /** Test constructor for Table */
    @Test
    public void testTableConstructor() {
        Table t = new Table(new String[]{"class", "SID", "grade"});
    }

    /** Test number of columns for Table */
    @Test
    public void testColumns() {
        Table t = new Table(new String[]{"class", "SID", "grade"});
        Table t1 = new Table(new String[]{"class", "SID", "grade", "teacher"});
        assertEquals(3, t.columns());
        assertEquals(4, t1.columns());
    }

    /** Test return value of getTitle. */
    @Test
    public void testGetTitle() {
        Table t = new Table(new String[]{"class", "SID", "grade"});
        assertEquals("class", t.getTitle(0));
        assertEquals("SID", t.getTitle(1));
        assertEquals("grade", t.getTitle(2));
    }

    /** Return number of column for findColumn. */
    @Test
    public void testFindColumn() {
        Table t = new Table(new String[]{"class", "SID", "grade", "teacher"});
        assertEquals(2, t.findColumn("grade"));
        assertEquals(3, t.findColumn("teacher"));
        assertEquals(-1, t.findColumn("student"));
    }

    /** Tests size and add methods of Table. */
    @Test
    public void testSizeAndAdd() {
        Table t = new Table(new String[]{"class", "SID", "grade", "teacher"});
        Row r1 = new Row(new String[]{"cs", "0101", "A", "PH"});
        Row r2 = new Row(new String[]{"cs", "0202", "A", "PH"});
        Row r3 = new Row(new String[]{"cs", "0303", "A+", "PH"});
        Row duplicate = new Row(new String[]{"cs", "0101", "A", "PH"});
        assertTrue(t.add(r1));
        assertTrue(t.add(r2));
        assertTrue(t.add(r3));
        assertFalse(t.add(duplicate));
        assertEquals(3, t.size());
        t.print();
        t.writeTable("t");
    }

    /** Tests database class and methods. */
    @Test
    public void testDatabase() {
        Database d = new Database();
        Table t1 = new Table(new String[]{"class", "SID", "grade", "teacher"});
        Table t2 = new Table(new String[]{"SID", "FirstName", "LastName"});
        d.put("schedule", t1);
        d.put("students", t2);
        assertEquals(t1, d.get("schedule"));
        assertEquals(t2, d.get("students"));
    }

    /** Tests select without conditions. */
    @Test
    public void testSelect() {
        Table t1 = new Table(new String[]{"class", "SID", "grade", "teacher"});
        Row r1 = new Row(new String[]{"cs", "0101", "A", "PH"});
        Row r2 = new Row(new String[]{"cs", "0202", "A", "PH"});
        Row r3 = new Row(new String[]{"cs", "0303", "A+", "PH"});
        t1.add(r1);
        t1.add(r2);
        t1.add(r3);
        List<String> columnNames = new ArrayList<String>();
        List<Condition> cond = new ArrayList<Condition>();
        Table result = t1.select(columnNames, cond);
        result.print();
    }
    /** Tests select with Condtions. */
    @Test
    public void testSelectWithConditions() {
        Table t1 = new Table(new String[]{"class", "SID", "grade", "teacher"});
        Row r1 = new Row(new String[]{"cs", "0101", "A", "PH"});
        Row r2 = new Row(new String[]{"cs", "0202", "A", "PH"});
        Row r3 = new Row(new String[]{"cs", "0303", "A+", "PH"});
        t1.add(r1);
        t1.add(r2);
        t1.add(r3);
        List<String> columnNames = new ArrayList<String>();
        columnNames.add("grade");
        columnNames.add("SID");
        Column col1 = new Column("grade", t1);
        Column col2 = new Column("SID", t1);
        List<Condition> condlst = new ArrayList<Condition>();
        Condition cond1 = new Condition(col1, "=", "A");
        Condition cond2 = new Condition(col2, "=", "0101");
        condlst.add(cond1);
        condlst.add(cond2);
        Table result = t1.select(columnNames, condlst);
        result.print();
    }
    /** Test equijoin, */
    @Test
    public void testEquijoin() {
        List<Column> com1 = new ArrayList<Column>();
        List<Column> com2 = new ArrayList<Column>();
        Table t1 = new Table(new String[]{"class", "SID", "grade", "teacher"});
        Row r1 = new Row(new String[]{"cs", "0101", "A", "PH"});
        t1.add(r1);
        Table t2 = new Table(new String[]{"hello", "class", "hi"});
        Row r2 = new Row(new String[]{"chair", "cs", "desk"});
        Column col1 = new Column("class", t1);
        com1.add(col1);
        Column col2 = new Column("class", t2);
        com2.add(col2);
    }
    /** Test two table conditions. */
    @Test
    public void testTwoTableSelect() {
        List<Condition> condlst = new ArrayList<Condition>();
        Table result = null;
        Table t1 = new Table(new String[]{"class", "SID", "grade", "teacher"});
        Row r1 = new Row(new String[]{"cs", "0101", "A", "PH"});
        Row r2 = new Row(new String[]{"cs", "0202", "A", "PH"});
        Row r3 = new Row(new String[]{"cs", "0303", "A+", "PH"});
        t1.add(r1);
        t1.add(r2);
        t1.add(r3);
        Table t2 = new Table(new String[]{"FirstName", "LastName", "SID"});
        Row row1 = new Row(new String[]{"Eric", "Chang", "0101"});
        Row row2 = new Row(new String[]{"Bob", "Jones", "202"});
        List<String> columnNames = new ArrayList<String>();
        columnNames.add("class");
        columnNames.add("FirstName");
        Column col1 = new Column("class", t1);
        Column col2 = new Column("FirstName", t2);
        result = t1.select(t2, columnNames, condlst);
        result.print();
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(BasicTests.class));
    }
}
