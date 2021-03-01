package Login;
import java.util.*;

public class adminLogin 
{
	public boolean check(int password) throws InputMismatchException // verifies admin password with entered password
	{
		int pass = 0;
		@SuppressWarnings("resource")
		Scanner ob = new Scanner(System.in);
		System.out.print("\t\t\t\tEnter Admin Password To Continue : ");
		try
		{
			pass = ob.nextInt();
		}
		catch (InputMismatchException e)
		{
			System.out.println("\t\t\t----------------------------------------------");
			System.out.println("\n\n\t\t\tINPUT A INTEGER INSTEAD OF STRING OR DOUBLE\n\n");
		}
		return pass == password;
	}
}
