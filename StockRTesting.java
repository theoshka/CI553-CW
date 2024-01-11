package dbAccess;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import debug.DEBUG;
import middle.StockException;

class StockRTesting {

	@Test
	void test() {
		
		try 
		{
			StockR s = new StockR();	
			
			assertEquals(3 , s.getDatabaseSize(), "database size wrong");
		} 
		catch (StockException e) 
		{
			DEBUG.error("StockRTesting fail i suppose \n&s ",
			e.getMessage() );
		}
	}

}
