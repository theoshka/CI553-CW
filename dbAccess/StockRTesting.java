package dbAccess;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import debug.DEBUG;
import middle.StockException;



class StockRTesting {

	@Test
	void databaseSizeTest() {	
		try 
		{
			StockR s = new StockR();	
			
			assertEquals(7 , s.getDatabaseSize(), "database size wrong"); // there are currently only 7 products in the database, i checked
		} 
		catch (StockException e) 
		{
			DEBUG.error("StockRTesting fail i suppose \n&s ",
			e.getMessage() );
		}
	}
	
	@Test 
	void testGetStockMap()
	{
		try 
		{
			StockR s = new StockR();
			
			assertEquals("0003" , s.getStockMap().get("toaster") , ".getStockMap() returning wrong prodNum for Toaster");  // the product number of the toaster is 0003
			assertEquals("0002" , s.getStockMap().get("dab radio"), ".getStockMap() returning wrong prodNum for dab radio"); // the product number of the dab radio is 0002
		} catch (StockException e)
		{
			DEBUG.error("stockmap fails supoose  " + e.getMessage());
		}
	}

}
