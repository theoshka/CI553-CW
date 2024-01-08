package clients.customer;

import catalogue.BetterBasket;
import catalogue.Product;
import debug.DEBUG;
import middle.MiddleFactory;
import middle.OrderProcessing;
import middle.StockException;
import middle.StockReader;

import javax.swing.*;
import java.util.Observable;
import java.util.*;

/**
 * Implements the Model of the customer client
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */
public class CustomerModel extends Observable
{
  private Product     theProduct = null;          // Current product
  private BetterBasket      theBasket  = null;          // Bought items

  private String      pn = "";                    // Product being processed

  private StockReader     theStock     = null;
  private OrderProcessing theOrder     = null;
  private ImageIcon       thePic       = null;

  /*
   * Construct the model of the Customer
   * @param mf The factory to create the connection objects
   */
  public CustomerModel(MiddleFactory mf)
  {
    try                                          // 
    {  
      theStock = mf.makeStockReader();           // Database access
    } catch ( Exception e )
    {
      DEBUG.error("CustomerModel.constructor\n" +
                  "Database not created?\n%s\n", e.getMessage() );
    }
    theBasket = makeBasket();                    // Initial Basket
  }
  
  /**
   * return the Basket of products
   * @return the basket of products
   */
  public BetterBasket getBasket()
  {
    return theBasket;
  }

  
  public void doSearch(String search) {
	  theBasket.clear();
	  String theAction = "";
	  ArrayList<Product> possibleProducts = new ArrayList<Product>();
	  
	  try 
	  {
		  Map<String, String> stockMap = theStock.getStockMap();
		  
		  for (int i = 0; i < stockMap.size(); i++) 
		  {
			  if (stockMap.get(Integer.toString(i)).contains(search)) 
			  {
				  possibleProducts.add(theStock.getDetails(Integer.toString(i)));
			  } 
			  else {
				  theAction =                             
				          "Unknown item " + search;
			  }
		  }
		  
		  ArrayList<String> possibleProductNumbers = new ArrayList<String>();
		  
		  for (Product product : possibleProducts) {
	            if (product.getDescription().contains(search)) {
	                possibleProductNumbers.add(product.getProductNum());
	            }
	        }
		  
		  

	  } catch (StockException e) 
	  {
		  DEBUG.error("CustomerModel.doSearch() ", e.getMessage());
	  }
	  
	  
  }
  /**
   * Check if the product is in Stock
   * @param productNum The product number
   */
  public void doCheck(String productNum )
  {	
	  
    theBasket.clear();                          // Clear s. list
    String theAction = "";
    
    pn = productNum.trim();                    // Product no.
   
    
    int    amount  = 1;                         //  & quantity
    try
    {
      if ( theStock.exists( pn ) )              // Stock Exists?
      {                                         // T
        Product pr = theStock.getDetails( pn ); //  Product
        if ( pr.getQuantity() >= amount )       //  In stock?
        { 
          theAction =                           //   Display 
            String.format( "%s : %7.2f (%2d) ", //
              pr.getDescription(),              //    description
              pr.getPrice(),                    //    price
              pr.getQuantity() );               //    quantity
          pr.setQuantity( amount );             //   Require 1
          theBasket.add( pr );                  //   Add to basket
          thePic = theStock.getImage( pn );     //    product
        } else {                                //  F
          theAction =                           //   Inform
            pr.getDescription() +               //    product not
            " not in stock" ;                   //    in stock
        }
      }else {                                  // F
        theAction =                             //  Inform Unknown
          "Unknown product number " + pn;       //  product number
      }
    } catch( StockException e )
    {
      DEBUG.error("CustomerClient.doCheck()\n%s",
      e.getMessage() );
    }
    setChanged(); notifyObservers(theAction);
  }

  /**
   * Clear the products from the basket
   */
  public void doClear()
  {
    String theAction = "";
    theBasket.clear();                        // Clear s. list
    theAction = "Enter Product Number";       // Set display
    thePic = null;                            // No picture
    setChanged(); notifyObservers(theAction);
  }
  
  /**
   * Return a picture of the product
   * @return An instance of an ImageIcon
   */ 
  public ImageIcon getPicture()
  {
    return thePic;
  }
  
  /**
   * ask for update of view callled at start
   */
  private void askForUpdate()
  {
    setChanged(); notifyObservers("START only"); // Notify
  }

  /**
   * Make a new Basket
   * @return an instance of a new Basket
   */
  protected BetterBasket makeBasket()
  {
    return new BetterBasket();
  }
}

