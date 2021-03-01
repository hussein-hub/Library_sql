LIBRARY MANAGEMENT SYSTEM

Our project is a console based application which tries and uses almost all the oops concepts present in java.
We have implemented the code using java only and not any other language.
FEATURES OF OUR PROJECT:
Our project is mainly divided into 2 parts , that is Admin part and the User part

Features for Admin part:
 1. DISPLAY DETAILS OF ALL USERS : self explanatory
 2. VIEW ALL BOOKS : self explanatory
 3. VIEW ISSUED BOOKS : display all the issued books by all users
 4. ADD USER : self explanatory
 5. DELETE USER : self explanatory
 6. ADD BOOK : self explanatory
 7. DELETE BOOK : self explanatory
 8. NOTIFY USER : this feature is to notify that particular user which has not returned a particular book although the due date has passed
 9. ADD BALANCE : add some balance a particular user account on demand
 10. RETURN TO MAIN MENU : self explanatory

All the functions used for Admin :
 + displayAllUsers()
 + displayAllBooks()
 + displayIssuedBooks()
 + addUser()
 + deleteUser()
 + addBook()
 + addBalance()
 + deleteBook()
 + send_notifications()
 + main()
 + delay_op() { used for multithreading }
 + delay() { used for multithreading }


Features for User part:
  1. VIEW AVAILABLE BOOKS : display list of all the books that the library has for issuing purpose
  2. ISSUE A BOOK : self explanatory
  3. RETURN A BOOK : self explanatory
  4. VIEW ISSUED BOOKS : display list of all the books that the logged in user has issued
  5. CHECK BALANCE : display balance of logged in user
  6. NOTIFICATIONS : check for any notification regarding late returning of books
  7. RETURN TO MAIN MENU : self explanatory

All the functions used for User :
 + notifications(long uid_number_of_user)
 + check_issuedBook(long uid_number_of_user)
 + showBalance(long uid_number_of_user)
 + issueBook(ResultSet rs)
 + issuedBooks(ResultSet rs)
 + returnBook(ResultSet rs)
 + main(ResultSet rs,long uid_number_of_user)

User_defined Packages :
1. Connecton
    * connect
2. Login
    * adminLogin
    * userLogin


Classes :
1. Admin
2. User
3. Fine
4. Driver_Main_sql


Perks of our way of approach:
 * We have used object oriented programming approach
 * Use of user defined packages for more readability and convenience
 * Use of Exception Handling which avoids the program from terminating
 * Use of Inheritance
 *** We have used MySQL database for storing our user data and books records which allows the program to not lose data even if program is terminated


CONTRIBUTERS :
Kritharth.Jain
Nayan.Mandliya
Hussein.A.Motiwala