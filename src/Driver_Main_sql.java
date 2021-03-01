import java.time.LocalTime;
import java.util.*;
import Login.adminLogin; // user defined package for Admin authentication
import Login.userLogin; // user defined package for user authentication
import java.sql.*;
import Connection.connect; // user defined package for connecting to MySql database using jdbc connection

class Fine extends Admin
{
    public static int d; // number of milliseconds after which fine should be applied

    static {
        d = 30000;
    }
}
class User // contains all functionalities related to user
{
    static Scanner sc = new Scanner(System.in);
    public User(){} // default constructor of User class
    static void notifications(long acc) throws SQLException, ClassNotFoundException // displays all notifications of a particular user
    {
        connect c = new connect();
        Connection con = c.getConnection();
        String query = "select * from notification";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        while(rs.next())
        {
            if(rs.getInt(1) == acc)
            {
                System.out.println("Notifications : " + rs.getString(2));
            }
        }
    }
    static boolean check_issuedBook(int acc) throws SQLException, ClassNotFoundException // checks the total number of books a user has issued
    {
        connect c = new connect();
        Connection con = c.getConnection();
        String query = "select * from Issued_books";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        int count = 0;
        while(rs.next())
        {
            if(acc == rs.getInt(2))
            {
                count ++;
                if(count > 2)
                {
                    return false;
                }
            }
        }
        return true;
    }
    void showBalance(long acc) throws SQLException, ClassNotFoundException // displays balance of current user
    {
        System.out.println("\n\n\n\t\t\t\t\t------------------------------------------------------------");
        System.out.println("\t\t\t\t\t************************************************************");
        System.out.println("\t\t\t\t\t------------------------------------------------------------");
        connect c = new connect();
        Connection con = c.getConnection();
        String query = "select * from users";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query); // users table retrieved
        while(rs.next())
        {
            if(acc == rs.getInt(1))
            {
                System.out.print("\t\t\t\t\t\t\t\tBALANCE REMAINING : " + rs.getDouble(4));
                if(rs.getDouble(4) < 0)
                {
                    String query_nest = "update users set Can_issue = " + false +  " where UID = " + acc;
                    st.executeUpdate(query_nest);
                }
                break;
            }
        }
        st.close();
        con.close();
        System.out.println("\n\t\t\t\t\t------------------------------------------------------------");
        System.out.println("\t\t\t\t\t\tPRESS ENTER TO CONTINUE : ");
        try{
            System.in.read();}
        catch(Exception e)
        {System.out.println(e);}
        clrscr();
    }
    static void issueBook(ResultSet rs) throws SQLException, ClassNotFoundException // Allows user to issue a book based on UID
    {
        if(rs.getBoolean(5)) // checks the Can_issue status of user in users database
        {
            int uid,check=0;
            if(check_issuedBook(rs.getInt(1))) // checks the total number of books a user has issued,if >3 then user cannot issue a new book
            {
            System.out.println("\n\n\n\t\t\t\t\t------------------------------------------------------------");
            System.out.println("\t\t\t\t\t************************************************************");
            System.out.println("\t\t\t\t\t------------------------------------------------------------");
            System.out.print("\t\t\t\t\tENTER UID OF THE BOOK : ");
            uid=sc.nextInt();
            connect c = new connect();
            Connection con = c.getConnection();
            String query = "select * from books";
            String query_nest;
            Statement st = con.createStatement();
            ResultSet rs_book = st.executeQuery(query); // books table retrieved
            while (rs_book.next())
            {
                if (uid == rs_book.getInt(1) && rs_book.getBoolean(3))
                {

                    String book_name = rs_book.getString(2);
                    System.out.println("\t\t\t\t\t------------------------------------------------------------");
                    System.out.println("\t\t\t\t\t\t\tBOOK DETAILS");
                    System.out.println("\t\t\t\t\t------------------------------------------------------------");
                    System.out.println("\t\t\t\t\t\tNAME : " + book_name);
                    System.out.println("\t\t\t\t\t------------------------------------------------------------");
                    if (rs_book.getBoolean(3)) // check if books is available
                    {
                        query_nest = "update books set Availability = " + false + " where UID = " + uid;
                        st.executeUpdate(query_nest);
                        LocalTime t = LocalTime.now();
                        Time time = Time.valueOf(t);
                        String query_nest1 = "update books set IssuedTime = '" + time + "' where UID = " + uid;
                        st.executeUpdate(query_nest1);  // storing book issuing time in variable issuedTime
                        Connection conn = c.getConnection();
                        String query2 = "insert into Issued_books values(?,?,?,?)";
                        PreparedStatement p = conn.prepareStatement(query2); // insert issued books in issued_books database
                        p.setInt(1, uid);
                        p.setInt(2, rs.getInt(1));
                        p.setString(3, book_name);
                        p.setTime(4, time);
                        int count = p.executeUpdate();
                        check = 1;
                        p.close();
                        conn.close();
                        if (count == 1) {
                            System.out.println("\t\t\t\t\t\tBOOK ISSUED SUCCESSFULLY!!!!!");
                            break;
                        } else {
                            check = 1;
                            System.out.println("\t\t\t\t\t\tBOOK NOT AVAILABLE RIGHT NOW!!");
                        }
                    }
                }
            }
                if(check == 0)
                {
                    System.out.println("\t\t\t\t\t\tBOOK WITH GIVEN UID NOT FOUND!!");
                }
                st.close();
                con.close();
            }
            else
            {
                System.out.println("\t\t\t\t\tYOU HAVE EXCEEDED BOOK ISSUING LIMIT!!!");
                System.out.println("\t\t\t\t\t------------------------------------------------------------");
                System.out.println("\t\t\t\t\t\tPRESS ENTER TO CONTINUE : ");
                try{
                    System.in.read();}
                catch(Exception e)
                {System.out.println(e);}
                clrscr();
                return;
            }
            System.out.println("\t\t\t\t\t------------------------------------------------------------");
            System.out.println("\t\t\t\t\t\tPRESS ENTER TO CONTINUE : ");
        }
        else
        {
            System.out.println("\t\t\t\t\tYOU CANNOT ISSUE BOOK CONTACT ADMIN!!!");
        }
        try{
            System.in.read();}
        catch(Exception e)
        {System.out.println(e);}
        clrscr();
    }
    static void issuedBooks(ResultSet rs) throws SQLException, ClassNotFoundException // displays all issued books of a particular user
    {
        System.out.println("\n\n\n\t\t\t\t\t----------------------------------------------------------------------");
        System.out.println("\t\t\t\t\t\tUID " + "\t\t\t\t\t" + "NAME" + "\t\t\t\t\t" + "ISSUED TIME");
        System.out.println("\t\t\t\t\t----------------------------------------------------------------------");
        connect c = new connect();
        Connection con = c.getConnection();
        String query = "select * from Issued_books";
        Statement st = con.createStatement();
        ResultSet rs_issuedBooks = st.executeQuery(query); // issued_books table retrieved
        while(rs_issuedBooks.next())
        {
            if(rs.getInt(1) == rs_issuedBooks.getInt(2))
            {
                System.out.println("\t\t\t\t\t\t" + rs_issuedBooks.getInt(1) + "\t\t\t\t" + rs_issuedBooks.getString(3) + "\t\t\t" + rs_issuedBooks.getTime(4));
            }
        }
        st.close();
        con.close();

        System.out.println("\t\t\t\t\t----------------------------------------------------------------------");
        System.out.println("\t\t\t\t\t\tPRESS ENTER TO CONTINUE : ");
        try{
            System.in.read();}
        catch(Exception e)
        {System.out.println(e);}
        clrscr();
    }
    static void returnBook(ResultSet rs) throws SQLException, ClassNotFoundException // Allows user to return a issued book
    {
        int uid,check = 0;
        int flag = 0;
        System.out.println("\n\n\n\t\t\t\t\t------------------------------------------------------------");
        System.out.println("\t\t\t\t\t************************************************************");
        System.out.println("\t\t\t\t\t------------------------------------------------------------");
        System.out.print("\t\t\t\t\tENTER UID OF THE BOOK YOU WANT TO RETURN : ");
        uid=sc.nextInt();
        connect c = new connect();
        Connection con = c.getConnection();
        String query = "select * from books";
        Statement st = con.createStatement();
        ResultSet rs_books = st.executeQuery(query); // books table retrieved
        while(rs_books.next())
        {
            if(uid == rs_books.getInt(1)) // if book uid matches any book in database
            {
                if(!rs_books.getBoolean(3)) // if matched book in database is issued
                {
                    String name = rs_books.getString(2);
                    Time it = rs_books.getTime(4);
                    String query_nest = "update books set Availability = " + true +  " where UID = " + uid;
                    st.executeUpdate(query_nest);
                    LocalTime tm = LocalTime.now(); // gets current time
                    Time time = Time.valueOf(tm); // converts current LocalTime to Time datatype
                    query_nest = "update books set ReturnTime = '" + time +  "' where UID = " + uid;
                    st.executeUpdate(query_nest);
                    Time rt = time;
                    long t;
                    t = (rt.getTime() - it.getTime()); // Finds the time elapsed between issuing time till now
                    if(t < Fine.d) // checks if time elapsed is greater than fine applicable time
                    {
                        flag = 1;
                        System.out.println("\t\t\t\t\t------------------------------------------------------------");
                        System.out.println("\t\t\t\t\t\tBOOK RETURNED SUCCESSFULLY!!!!!");
                        break;
                    }
                    else
                    {
                        String query_nest1 = "update users set Fine = " + (double) ((t-Fine.d) * 0.001) +  " where UID = " + rs.getInt(1);
                        st.executeUpdate(query_nest1);
                        String query_nest2 = "update users set Balance = " + (rs.getDouble(4) - (double) ((t-Fine.d) * 0.001)) +  " where UID = " + rs.getInt(1);
                        st.executeUpdate(query_nest2);
                        System.out.println("\t\t\t\t\t------------------------------------------------------------");
                        System.out.println("\t\t\t\t\tBOOK SUBMITTED " + (t-Fine.d)*0.001 + " DAYS LATE");
                        System.out.println("\t\t\t\t\t------------------------------------------------------------");
                        User u = new User();
                        u.showBalance(rs.getInt(1));
                        System.out.println("\t\t\t\t\t------------------------------------------------------------");
                    }
                    if((rs.getDouble(4) - (double) ((t-Fine.d) * 0.001)) < 0) // checks if balance is negative
                    {
                        String query_nest3 = "update users set Can_issue = " + false +  " where UID = " + rs.getInt(1);
                        st.executeUpdate(query_nest3); // if balance is negative then set Can_issue to false
                    }
                    st.close();
                    con.close();
                    check = 1;
                    break;
                }
            }
        }
        connect cn = new connect();
        Connection conn = c.getConnection();
        String query2 = "delete from Issued_books where Book_UID = ?";
        PreparedStatement ps = conn.prepareStatement(query2); // remove returned book from issue_books database on basis of UID
        ps.setInt(1,uid);
        int count = ps.executeUpdate();
        if(count == 1 && flag == 0)
        {
            System.out.println("\t\t\t\t\t\tBOOK RETURNED SUCCESSFULLY!!!!!");
        }
        if(check == 0 && flag == 0)
        {
            System.out.println("\t\t\t\t\tBOOK WITH GIVEN UID NOT ISSUED OR NOT IN LIBRARY!!!");
        }
        System.out.println("\t\t\t\t\t------------------------------------------------------------");
        System.out.println("\t\t\t\t\t\tPRESS ENTER TO CONTINUE : ");
        try{
            System.in.read();}
        catch(Exception e)
        {System.out.println(e);}
        clrscr();
    }
    public static void main(ResultSet rs,long acc) throws SQLException, ClassNotFoundException // Driver for User Class
    {
        User usr = new User(); // object of user class created
        int choice = -1;
        while(choice != 7)
        {
            System.out.println("\n\n\n\n\n\t\t\t\t\t----------------------------------------------");
            System.out.println("\t\t\t\t\t\t\t\t\tUSER PANEL");
            System.out.println("\t\t\t\t\t----------------------------------------------");
            System.out.println("\t\t\t\t\t----------------------------------------------");
            System.out.println("\t\t\t\t\t\t\t\t1. VIEW AVAILABLE BOOKS");
            System.out.println("\t\t\t\t\t\t\t\t2. ISSUE A BOOK");
            System.out.println("\t\t\t\t\t\t\t\t3. RETURN A BOOK");
            System.out.println("\t\t\t\t\t\t\t\t4. VIEW ISSUED BOOKS");
            System.out.println("\t\t\t\t\t\t\t\t5. CHECK BALANCE");
            System.out.println("\t\t\t\t\t\t\t\t6. NOTIFICATIONS");
            System.out.println("\t\t\t\t\t\t\t\t7. RETURN TO MAIN MENU");
            System.out.println("\t\t\t\t\t----------------------------------------------");
            System.out.print("\t\t\t\t\t\tENTER YOUR CHOICE : ");
            choice = sc.nextInt();
            System.out.println("\t\t\t\t\t----------------------------------------------");
            System.out.println();
            switch (choice)
            {
                case 1 -> {
                    clrscr();
                    Admin admin = new Admin(); // objects of admin class
                    admin.displayAllBooks();
                }
                case 2 -> {
                    clrscr();
                    issueBook(rs);
                }
                case 3 -> {
                    clrscr();
                    returnBook(rs);
                }
                case 4 -> {
                    clrscr();
                    issuedBooks(rs);
                }
                case 5 -> {
                    usr.showBalance(acc);
                }
                case 6 -> {
                    clrscr();
                    notifications(acc);
                }
                case 7 -> {
                    clrscr();
                }
                default -> {
                    System.out.println("\t\t\t\t\tINVALID INPUT, TRY AGAIN");
                }
            }
        }
    }
    public static void clrscr() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}

class Admin // contains al functionalities related to Admin
{
    static Scanner sc=new Scanner(System.in);
    private void displayAllUsers() throws SQLException, ClassNotFoundException // displays records of all users
    {
        System.out.println("\n\n\n\n\n\t\t\t------------------------------------------------------------");
        System.out.println("\t\t\t  UID \t\t NAME \t\t EMAIL \t\t\t\t BALANCE");
        System.out.println("\t\t\t------------------------------------------------------------");
        connect c = new connect();
        Connection con = c.getConnection();
        String query = "select * from users";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query); // users table retrieved from database
        String userdata = "";
        while(rs.next())
        {
            userdata = "\t\t\t" +  rs.getInt(1) + "\t\t" + rs.getString(2) + "\t\t" + rs.getString(3) + "\t\t" + rs.getDouble(4);
            System.out.println(userdata);
        }
        st.close();
        con.close();
        System.out.println();
        System.out.println("\t\t\t------------------------------------------------------------");
        System.out.println("\t\t\t\tPRESS ENTER TO CONTINUE : ");
        try{
            System.in.read();}
        catch(Exception e)
        {System.out.println(e);}
        clrscr();
    }
    void displayAllBooks() throws SQLException, ClassNotFoundException // displays all available books in the database
    {
        System.out.println("\n\n\n\n\n\t\t\t---------------------------------------------------------------------------");
        System.out.println("\t\t\t UID \t\t\t NAME \t\t\t\t AVAILABILITY STATUS");
        System.out.println("\t\t\t---------------------------------------------------------------------------");
        connect c = new connect();
        Connection con = c.getConnection();
        String query = "select * from books";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query); // books table retrieved from database
        String userdata = "";
        while(rs.next())
        {
            userdata = "\t\t\t" +  rs.getInt(1) + "\t\t\t" + rs.getString(2) + "\t\t\t\t" + rs.getBoolean(3);
            System.out.println(userdata);
        }
        st.close();
        con.close();
        System.out.println();
        System.out.println("\t\t\t---------------------------------------------------------------------------");
        System.out.println("\t\t\t\tPRESS ENTER TO CONTINUE : ");
        try{
            System.in.read();}
        catch(Exception e)
        {System.out.println(e);}
        clrscr();
    }

    void displayIssuedBooks() throws SQLException, ClassNotFoundException // displays all the books that have been issued by users
    {
        delay_op(1);
        System.out.println("\n\n\n\n\n\t\t\t------------------------------------------------------------");
        System.out.println("\t\t\t\t NAME \t\t\t\t\t ISSUED TIME ");
        System.out.println("\t\t\t------------------------------------------------------------");
        connect c = new connect();
        Connection con = c.getConnection();
        Statement st = con.createStatement();
        String query = "select * from books";
        ResultSet rs = st.executeQuery(query); // books table retrieved from database
        while(rs.next())
        {
            if(!rs.getBoolean(3)) // checks if the books availability status is true or false
            {
                System.out.println("\t\t\t" + rs.getString(2) + "\t\t\t\t" + rs.getTime(4) );
            }
        }
        System.out.println();
        System.out.println("\t\t\t------------------------------------------------------------");
        System.out.println("\t\t\t\tPRESS ENTER TO CONTINUE : ");
        try{
            System.in.read();}
        catch(Exception e)
        {System.out.println(e);}
        clrscr();
    }

    private void addUser() throws SQLException, ClassNotFoundException // Adds user into database
    {
        @SuppressWarnings("resource")
        Random rand = new Random();
        String name,email;
        System.out.println("\n\n\n\n\n\t\t\t------------------------------------------------------------");
        System.out.println("\t\t\t************************************************************");
        System.out.println("\t\t\t------------------------------------------------------------");
        System.out.print("\t\t\t\t\t\tENTER NAME : ");
        name = sc.next();
        System.out.println("\t\t\t------------------------------------------------------------");
        System.out.print("\t\t\t\t\t\tENTER EMAIL : ");
        email = sc.next();
        System.out.println("\t\t\t------------------------------------------------------------");
        int balance = 500;
        int uid = rand.nextInt(10000000);
        double fine = 0.0;
        connect c = new connect();
        // Adding user to user database
        Connection con = c.getConnection();
        String query = "insert into users values(?,?,?,?,?,?)";
        PreparedStatement p = con.prepareStatement(query); // inserts all data of user into the database
        p.setInt(1,uid);
        p.setString(2,name);
        p.setString(3,email);
        p.setDouble(4,balance);
        p.setBoolean(5,true);
        p.setDouble(6,0.0);
        int count = p.executeUpdate();
        if(count == 1)
        {
            System.out.println("\t\t\t\t\t\tUSER ADDED SUCCESSFULLY!!!!");
        }
        else
        {
            System.out.println("\t\t\t\t\t\tUSER NOT ADDED !!!!");
        }
        p.close();
        con.close();
        System.out.println("\t\t\t------------------------------------------------------------");
        System.out.println("\t\t\t\t\t\tPRESS ENTER TO CONTINUE");
        try{System.in.read();}
        catch(Exception e){System.out.println(e);}
        clrscr();
    }

    private void deleteUser() throws SQLException, ClassNotFoundException // Deletes users from the database on the basis of UID
    {
        int uid,count = 0;
        boolean flag = false;
        System.out.println("\n\n\n\n\n\t\t\t------------------------------------------------------------");
        System.out.println("\t\t\t************************************************************");
        System.out.println("\t\t\t------------------------------------------------------------");
        System.out.print("\t\t\t\t\t\tENTER UID OF THE USER : ");
        // @SuppressWarnings("resource")
        uid = sc.nextInt();
        System.out.println("\t\t\t------------------------------------------------------------");
        int check = 0;
        connect c = new connect();
        // checking if the user has any issued books before deleting him
        // if user has any issued books we cant delete the user before he returns the book
        Connection conn = c.getConnection();
        String query1 = "select * from Issued_books";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query1); // issued_books table retrieved from database
        while(rs.next())
        {
            if(uid == rs.getInt(2))
            {
                flag = true;
                break;
            }
        }
        // if user has no issued books we can safely delete the user
        if(flag == false)
        {
            Connection con = c.getConnection();
            String query = "delete from users where UID = ?";
            PreparedStatement p = con.prepareStatement(query); // deletes user on bases of UID from database
            p.setInt(1,uid);
            count = p.executeUpdate(); // returns the number of rows changes in the database

            p.close();
            con.close();
        }
        if(count == 0 && flag == true)
        {
            System.out.println("\t\t\t------------------------------------------------------------");
            System.out.println("\t\t\t\t\tPLEASE RETURN ISSUED BOOKS BEFORE DELETING USER!!!!");
            System.out.println("\t\t\t------------------------------------------------------------");
            System.out.println("\t\t\t\t\t\tPRESS ENTER TO CONTINUE");
        }
        else if(count == 0)
        {
            System.out.println("\t\t\t------------------------------------------------------------");
            System.out.println("\t\t\t\t\t\tUSER WITH GIVEN UID NOT FOUND!!!!");
            System.out.println("\t\t\t------------------------------------------------------------");
            System.out.println("\t\t\t\t\t\tPRESS ENTER TO CONTINUE");
        }
        else
        {
            System.out.println("\t\t\t------------------------------------------------------------");
            System.out.println("\t\t\t\t\t\tUSER DELETED SUCCESSFULLY!!!!");
            System.out.println("\t\t\t------------------------------------------------------------");
            System.out.println("\t\t\t\t\t\tPRESS ENTER TO CONTINUE");
        }
        try{System.in.read();}
        catch(Exception e){System.out.println(e);}
        clrscr();
    }
    private void addBook() throws SQLException, ClassNotFoundException // Adds book/books into the database
    {
        System.out.println("\n\n\n\n\n\t\t\t------------------------------------------------------------");
        System.out.println("\t\t\t************************************************************");
        System.out.println("\t\t\t------------------------------------------------------------");
        String name;
        int number_of_books;
        System.out.print("\t\t\t\t\t\tENTER NAME OF BOOK : ");
        name = sc.next();
        System.out.println("\n\t\t\t------------------------------------------------------------");
        System.out.print("\t\t\t\t\t\tENTER QUANTITY OF BOOK : ");
        number_of_books = sc.nextInt();
        Random rand = new Random();
        int uid,check=0;
        connect c = new connect();
        Connection con = c.getConnection();
        String query = "insert into books values(?,?,?,?,?)";
        // every time a book is inserted a new query is executed, this is because although the name of the book is same it will have a different UID
        for(int i=0;i<number_of_books;i++)
        {
            PreparedStatement p = con.prepareStatement(query);
            uid = rand.nextInt(1000000);
            p.setInt(1,uid);
            p.setString(2,name);
            p.setBoolean(3,true);
            p.setLong(4,0);
            p.setLong(5,0);
            int count = p.executeUpdate();
            p.close();
            if(count == 1)
            {
                check++;
            }
        }
        if(check == number_of_books) {
            System.out.println("\t\t\t\t\t\t ADDED SUCCESSFULLY!!!!");
        }
        else
        {
            System.out.println("\t\t\t\t\t\t BOOK NOT ADDED DUE TO ERROR!!!!\"");
        }
        con.close();
        System.out.println("\t\t\t------------------------------------------------------------");
        System.out.println("\t\t\t\t\t\tPRESS ENTER TO CONTINUE");
        try{System.in.read();}
        catch(Exception e){System.out.println(e);}
        clrscr();
    }
    private void addBalance() throws SQLException, ClassNotFoundException // Adds balance to existing user if needed
    {
        int uid,check=0;
        double balance;
        System.out.println("\n\n\n\n\t\t\t------------------------------------------------------------");
        System.out.println("\t\t\t************************************************************");
        System.out.println("\t\t\t------------------------------------------------------------");
        System.out.print("\t\t\tENTER UID OF THE USER : ");
        uid=sc.nextInt();
        System.out.println("\t\t\t------------------------------------------------------------");
        connect c = new connect();
        Connection con = c.getConnection();
        String query = "select * from users";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query); // users table retrieved from database
        while(rs.next())
        {
            if(uid == rs.getInt(1)) // checks id uid to which balance wants to be added either exists in the database or not
            {
                check = 1;
                System.out.println("\t\t\t\t\tUSER DETAILS");
                System.out.println("\t\t\t------------------------------------------------------------");
                System.out.println("\t\t\t\tNAME : " + rs.getString(2));
                System.out.println("\t\t\t\tBALANCE : " + rs.getDouble(4));
                System.out.println("\t\t\t------------------------------------------------------------");
                System.out.print("\t\t\t\tENTER BALANCE TO BE ADDED : ");
                balance=sc.nextDouble();
                System.out.println("\t\t\t------------------------------------------------------------");
                balance = rs.getDouble(4) + balance;
                String query_nest = "update users set Balance = " + balance +  " where UID = " + uid;
                st.executeUpdate(query_nest);
                if(balance > 0)
                {
                    String query_nest1 = "update users set Can_issue = " + true +  " where UID = " + uid;
                    st.executeUpdate(query_nest1);
                }
                break;
            }
        }
        st.close();
        con.close();
        if(check == 0)
        {
            System.out.println("\t\t\t\t\tUSER WITH GIVEN UID NOT FOUND!!");
        }
        System.out.println("\t\t\t------------------------------------------------------------");
        System.out.println("\t\t\t\t\t\tPRESS ENTER TO CONTINUE");
        try{System.in.read();}
        catch(Exception e){System.out.println(e);}
        clrscr();
    }

    private void deleteBook() throws SQLException, ClassNotFoundException // Deletes a book from the database depending on its UID
    {
        int uid,check=0,id=0;
        System.out.println("\n\n\n\n\n\t\t\t------------------------------------------------------------");
        System.out.println("\t\t\t************************************************************");
        System.out.println("\t\t\t------------------------------------------------------------");
        System.out.print("\t\t\t\t\t\tENTER UID OF THE BOOK : ");
        uid = sc.nextInt();
        System.out.println("\t\t\t------------------------------------------------------------");
        connect c = new connect();
        Connection con = c.getConnection();
        String query = "delete from books where UID = ?";
        PreparedStatement p = con.prepareStatement(query); // deletes the book with specified UID
        p.setInt(1,uid);
        int count = p.executeUpdate();
        p.close();
        con.close();
        System.out.println("\t\t\t------------------------------------------------------------");
        if(count == 1)
        {
            System.out.println("\t\t\t\t\t\tBOOK DELETED SUCCESSFULLY!!!!");
        }
        else
        {
            System.out.println("\t\t\t\t\t\tBOOK WITH GIVEN UID NOT FOUND!!!!");
        }
        System.out.println("\t\t\t------------------------------------------------------------");
        System.out.println("\t\t\t\t\t\tPRESS ENTER TO CONTINUE");
        try{System.in.read();}
        catch(Exception e){System.out.println(e);}
        clrscr();
    }
    private static void send_notifications() throws SQLException, ClassNotFoundException // Admin is able to send notification to those users who haven't returned a books beyond due date
    {
        System.out.println("\n\n\n\n\n\n\n\t\t\t\t\t");
        delay_op(2);
        connect c = new connect();
        Connection con = c.getConnection();
        Statement st = con.createStatement();
        String query = "select * from users";
        ResultSet rs_user = st.executeQuery(query); // users table retrieved from database
        while(rs_user.next())
        {
            int uid = rs_user.getInt(1);
            int count = 0;
            Connection con1 = c.getConnection(); // nested connection
            Statement st1 = con1.createStatement();
            String query1 = "select * from Issued_books";
            ResultSet rs_issued = st1.executeQuery(query1); // issued_books table retrieved from database
            while(rs_issued.next())
            {
                if(uid == rs_issued.getInt(2)) // checks for all issued books of a particular user
                {
                    LocalTime now = LocalTime.now();
                    Time time = Time.valueOf(now);
                    long t = (time.getTime() - rs_issued.getTime(4).getTime());
                    if((t-Fine.d) > 0)
                    {
                        String s = "LATE : PLEASE RETURN BOOK " + rs_issued.getString(3) + " FINE CHARGEABLE IF RETURNED TODAY : " + (double)((t-Fine.d)*0.001);
                        Connection con2 = c.getConnection(); // nested-nested connection
                        String query2 = "insert into notification values(?,?,?)";
                        PreparedStatement ps = con2.prepareStatement(query2); // inserts a notification in database for the particular user
                        ps.setInt(1,uid);
                        ps.setString(2,s);
                        ps.setTime(3,time);
                        count = ps.executeUpdate();
                        ps.close();
                        con2.close();
                    }
                }
            }
            if(count == 1)
            {
                System.out.println("\n\n\t\t\t\t\tNOTIFICATION SENT!");
            }
            st1.close();
            con1.close();
        }
        st.close();
        con.close();
    }

    protected static void main() throws SQLException, ClassNotFoundException // Driver for Admin class
    {
        Admin admin = new Fine(); // dynamic method dispatch
        int choice = -1;
        @SuppressWarnings("resource")
        Scanner ob = new Scanner(System.in);
        while(choice != 10)
        {
            System.out.println("\n\n\n\n\n\t\t\t\t\t----------------------------------------------");
            System.out.println("\t\t\t\t\t\t\t\t\tADMIN PANEL");
            System.out.println("\t\t\t\t\t----------------------------------------------");
            System.out.println("\t\t\t\t\t----------------------------------------------");
            System.out.println("\t\t\t\t\t\t\t\t1. DISPLAY DETAILS OF ALL USERS");
            System.out.println("\t\t\t\t\t\t\t\t2. VIEW ALL BOOKS");
            System.out.println("\t\t\t\t\t\t\t\t3. VIEW ISSUED BOOKS");
            System.out.println("\t\t\t\t\t\t\t\t4. ADD USER");
            System.out.println("\t\t\t\t\t\t\t\t5. DELETE USER");
            System.out.println("\t\t\t\t\t\t\t\t6. ADD BOOK");
            System.out.println("\t\t\t\t\t\t\t\t7. DELETE BOOK");
            System.out.println("\t\t\t\t\t\t\t\t8. NOTIFY USER");
            System.out.println("\t\t\t\t\t\t\t\t9. ADD BALANCE");
            System.out.println("\t\t\t\t\t\t\t\t10. RETURN TO MAIN MENU");
            System.out.println("\t\t\t\t\t----------------------------------------------");
            System.out.print("\t\t\t\t\t\tENTER YOUR CHOICE : ");
            choice = ob.nextInt();
            System.out.println("\t\t\t\t\t----------------------------------------------");
            clrscr();
            System.out.println();
            switch (choice)
            {
                case 1 -> {
                    admin.displayAllUsers();
                    break;
                }
                case 2 -> {
                    admin.displayAllBooks();
                    break;
                }
                case 3 -> {
                    admin.displayIssuedBooks();
                    break;
                }
                case 4 -> {
                    admin.addUser();
                    break;
                }
                case 5 -> {
                    admin.deleteUser();
                    break;
                }
                case 6 -> {
                    admin.addBook();
                    break;
                }
                case 7 -> {
                    admin.deleteBook();
                    break;
                }
                case 8 -> {
                    Admin.send_notifications();
                    break;
                }
                case 9 -> {
                    admin.addBalance();
                    break;
                }
                case 10 -> {
                    break;
                }
                default -> System.out.println("\t\t\t\t\tINVALID INPUT, TRY AGAIN");
            }
        }
    }
    public static void clrscr()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    static void delay_op(int m) // used to mimic data fetch
    {
        clrscr();
        String s;
        if(m==1)
        {
            s = "LOADING RECORDS FROM DATABASE";
        }
        else
        {
            System.out.println("\n\n\n\n\n\t\t\t\t-------------------------------");
            System.out.println("\t\t\t\t*******************************");
            System.out.println("\t\t\t\t-------------------------------");
            s= "NOTIFYING ALL USERS";
        }
        for (int i = 5; i >= 1; i--)
        {
            clrscr();
            System.out.println("\t\t\t\t " + s);
            s = s + ". ";
            delay();
        }
        clrscr();
    }
    static void delay() // uses multithreading to delay or stop program for 0.75 seconds
    {
        try {
            Thread.sleep(750);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}


class Driver_Main_sql extends Admin // Main Driver class
{
    final static String ESC = "\033[";
    public static void main(String[] args) throws Exception
    {
        int ch = -1;
        int password = 1590;
        clrscr();
        while(ch != 3)
        {
            System.out.println("\n\n\n\t\t\t----------------------------------------------\n");
            System.out.println("\t\t\t\t\tWELCOME TO XYZ LIBRARY PORTAL");
            System.out.println("\n\t\t\t----------------------------------------------");
            System.out.println("\t\t\t\t\t\t****   MAIN MENU   ****");
            System.out.println("\t\t\t----------------------------------------------");
            System.out.println("\t\t\t\t\t\t\t1. ADMIN");
            System.out.println("\t\t\t\t\t\t\t2. USER");
            System.out.println("\t\t\t\t\t\t\t3. EXIT");
            System.out.println("\t\t\t----------------------------------------------");
            System.out.println("\t\t\t----------------------------------------------");
            System.out.print("\t\t\t\t\tENTER YOUR CHOICE : ");
            @SuppressWarnings("resource")
            Scanner ob = new Scanner(System.in);
            ch = ob.nextInt();
            System.out.println("\t\t\t----------------------------------------------");
            clrscr();
            switch (ch)
            {
                case 1 -> {
                    adminLogin Login = new adminLogin(); // object of adminLogin class
                    if (Login.check(password)) // calls adminLogin class method for admin authentication
                    {
                        System.out.println("\t\t\t----------------------------------------------");
                        System.out.println("\t\t\t\t\t\tADMIN ACCESS GRANTED!!");
                        System.out.println("\t\t\t----------------------------------------------");
                        System.out.println("\t\t\t\t\tPRESS ENTER TO CONTINUE.");
                        System.in.read();
                        clrscr();
                        main(); // Driver of admin class called
                    }
                    else
                    {
                    System.out.println("\t\t\t----------------------------------------------");
                    System.out.println("\t\t\t\t\tWRONG PASSWORD, TRY AGAIN!");
                    System.out.println("\t\t\t----------------------------------------------");
                    System.out.println("\t\t\t\tPRESS ENTER TO CONTINUE.");
                    System.in.read();
                    clrscr();
                    }
                }
                case 2 -> {
                    int acc = 0;
                    boolean flag = false;
                    System.out.println("\n\n\n\n\n\t\t\t-------------------------------------------------");
                    System.out.println("\t\t\t*******************************************************");
                    System.out.println("\t\t\t------------------------------------------------------");
                    System.out.print("\t\t\tENTER YOUR UID TO GO TO YOUR ACCOUNT : ");
                    try
                    {
                        acc = ob.nextInt();
                    }
                    catch (InputMismatchException e)
                    {
                        e.getStackTrace();
                        System.out.println("\t\t\t----------------------------------------------");
                        System.out.println("\n\n\t\t\t\tINPUT A INTEGER INSTEAD OF STRING OR DOUBLE\n\n");
                    }
                    userLogin usrLgn = new userLogin(); // object of userLogin class created
                    connect c = new connect(); // object of connect class
                    Connection con = c.getConnection(); // calling method of connect class
                    Statement st = con.createStatement();
                    String query = "select * from users";
                    ResultSet rs = st.executeQuery(query); // users table retrieved from database
                    while(rs.next())
                    {
                        if(usrLgn.check(acc, rs.getInt(1))) // verifies the user UID by traversing the database
                        {
                            flag = true;
                            System.out.println("\n\t\t\t------------------------------------------------------");
                            System.out.println("\t\t\t\t\t\tWELCOME " + rs.getString(2) + " !!!!!!");
                            System.out.println("\t\t\t------------------------------------------------------");
                            System.out.println("\t\t\t\tPRESS ENTER TO CONTINUE : ");
                            try {
                                System.in.read();
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                            clrscr();
                            User.main(rs,acc); // Driver of User class called
                        }
                    }
                    if (!flag) {
                        System.out.println("\t\t\t------------------------------------------------------");
                        System.out.println("\t\t\t\t\t\tNO USER WITH SPECIFIED UID!");
                        System.out.println("\t\t\t------------------------------------------------------");
                        System.out.println("\t\t\t\tPRESS ENTER TO CONTINUE : ");
                        try {
                            System.in.read();
                        } catch (Exception e) {
                            throw(e);
                        }
                        clrscr();
                    }
                }
                case 3 -> {
                    clrscr();
                    System.out.println("\n\n\n\n\n\n\t\t\t\t\t\t----------------------------------------------");
                    System.out.println("\t\t\t\t\t\t===============:::::::::::::::================");
                    System.out.println("\t\t\t\t\t\t----------------------------------------------");
                    System.out.println("\t\t\t\t\t\t\tHOPE TO SEE YOU AGAIN, GOODBYE!! (:");
                    System.out.println("\t\t\t\t\t\t----------------------------------------------");
                    System.out.println("\t\t\t\t\t\t===============:::::::::::::::================");
                    System.out.println("\t\t\t\t\t\t----------------------------------------------");
                }
                default -> System.out.println("\t\t\t\t\tINVALID INPUT, TRY AGAIN");
            }
        }
    }
    public static void clrscr() // to clear console screen. Neither works in INTELLIJ IDEA nor in ECLIPSE IDE
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}