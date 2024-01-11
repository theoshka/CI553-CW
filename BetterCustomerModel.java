package clients.customer;

import javax.swing.ImageIcon;

import java.util.*;
import catalogue.BetterBasket;
import catalogue.Product;
import debug.DEBUG;
import middle.MiddleFactory;
import middle.OrderProcessing;
import middle.StockReader;

public class BetterCustomerModel extends CustomerModel
{
	private Product     theProduct = null;          // Current product
	private BetterBasket      theBasket  = null;          // Bought items

	private String      pn = "";                    // Product being processed

	private StockReader     theStock     = null;
	private OrderProcessing theOrder     = null;
	private ImageIcon       thePic       = null;
	
	
	public BetterCustomerModel(MiddleFactory mf) 
	{
		super(mf);
		try 
		{
			theStock = mf.makeStockReader();
		} catch(Exception e) 
		{
			DEBUG.error("BetterCustomerModel.constructor\n" + 
						"Database not created?\n%s\n", e.getMessage() );
		}
		theBasket = makeBasket();
	}
	

}
