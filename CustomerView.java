package clients.customer;

import catalogue.BetterBasket;
import clients.Picture;
import middle.MiddleFactory;
import middle.StockReader;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Customer view.
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */

public class CustomerView implements Observer
{
  class Name                              // Names of buttons
  {
    public static final String CHECK  = "Check";
    public static final String CLEAR  = "Clear";
    public static final String SEARCH = "Search";
  }

  private static final int H = 300;       // Height of window pixels
  private static final int W = 400;       // Width  of window pixels

  private final JLabel      theAction  = new JLabel();
  private final JTextField  theInput   = new JTextField();
  
  private final JTextArea   theOutput  = new JTextArea();
  private final JScrollPane theSP      = new JScrollPane();
  private final JButton     theBtCheck = new JButton( Name.CHECK );
  private final JButton     theBtClear = new JButton( Name.CLEAR );
  
  private final JTextField  theInput2  = new JTextField();
  private final JButton     theBtSearch = new JButton(Name.SEARCH);
  private final JComboBox<String> theComboBox = new JComboBox<>();
  private final JPanel searchPanel = new JPanel();

  private Picture thePicture = new Picture(80,80);
  private StockReader theStock   = null;
  private CustomerController cont= null;
  private Map<String, String> possibleProducts;
  private String selectedProductNum;

  /**
   * Construct the view
   * @param rpc   Window in which to construct
   * @param mf    Factor to deliver order and stock objects
   * @param x     x-cordinate of position of window on screen 
   * @param y     y-cordinate of position of window on screen  
   */
  
  public CustomerView( RootPaneContainer rpc, MiddleFactory mf, int x, int y )
  {
    try                                             // 
    {      
      theStock  = mf.makeStockReader();             // Database Access
    } catch ( Exception e )
    {
      System.out.println("Exception: " + e.getMessage() );
    }
    Container cp         = rpc.getContentPane();    // Content Pane
    Container rootWindow = (Container) rpc;         // Root Window
    cp.setLayout(null);                             // No layout manager
    rootWindow.setSize( W, H );                     // Size of Window
    rootWindow.setLocation( x, y );
    
    theComboBox.setEditable(true); 	// making the combo box editable

    Font f = new Font("Monospaced",Font.PLAIN,12);  // Font f is

    theBtCheck.setBounds( 16, 50, 80, 40 );    // Check button
    theBtCheck.addActionListener(                   // Call back code
      e -> cont.doCheck( theInput.getText() ) );
    cp.add( theBtCheck );                           //  Add to canvas

    theBtClear.setBounds( 16, 30+60*1, 80, 40 );    // Clear button
    theBtClear.addActionListener(                   // Call back code
      e -> cont.doClear() );
    cp.add( theBtClear );                           //  Add to canvas

    theAction.setBounds( 110, 120 , 270, 20 );       // Message area
    theAction.setText( "" );                        //  Blank
    cp.add( theAction );                            //  Add to canvas

    theInput.setBounds( 110, 80, 270, 40 );         // Product no area
    theInput.setText("Check by prodNum");                           // Blank
    cp.add( theInput );                             //  Add to canvas
    
    theSP.setBounds( 110, 140, 270, 160 );          // Scrolling pane
    theOutput.setText( "" );                        //  Blank
    theOutput.setFont( f );                         //  Uses font  
    cp.add( theSP );                                //  Add to canvas
    theSP.getViewport().add( theOutput );           //  In TextArea

    thePicture.setBounds( 16, 25+60*2, 80, 80 );   // Picture area
    cp.add( thePicture );                           //  Add to canvas
    thePicture.clear();
    
    
    theInput2.setBounds(110, 10 , 270 , 40);
    theInput2.setText("Search by name / description");
    theInput2.addActionListener(
    		e -> updateComboBoxItems(theComboBox, theInput2.getText().toLowerCase()) );
    cp.add(theInput2);
    
    
    theComboBox.setBounds(110, 10, 270, 40);
    theComboBox.addActionListener(
    		e -> cont.doCheck(possibleProducts.get(theComboBox.getSelectedItem()))
    		);
    		
    cp.add(theComboBox);
      
    theBtSearch.setBounds(16, 10, 80, 40);
    theBtSearch.addActionListener(
    		e -> cont.doSearch(theInput2.getText()));
    cp.add(theBtSearch);
    
    searchPanel.add(theInput2);
    searchPanel.add(theComboBox);
    searchPanel.setBounds(110, 1, 270, 80);
    cp.add(searchPanel);
    
    rootWindow.setVisible( true );                  // Make visible);
    theInput.requestFocus();                        // Focus is here
    theInput2.requestFocus();
  }

   /**
   * The controller object, used so that an interaction can be passed to the controller
   * @param c   The controller
   */

  public void setController( CustomerController c )
  {
    cont = c;
  }

  /**
   * Update the view
   * @param modelC   The observed model
   * @param arg      Specific args 
   */
   
  public void update( Observable modelC, Object arg )
  {
    CustomerModel model  = (CustomerModel) modelC;
    String        message = (String) arg;
    theAction.setText( message );
    ImageIcon image = model.getPicture();  // Image of product
    if ( image == null )
    {
      thePicture.clear();                  // Clear picture
    } else {
      thePicture.set( image );             // Display picture
    }
    theOutput.setText( model.getBasket().getDetails() );
    theInput.requestFocus();               // Focus is here
    theInput2.requestFocus();
  }
  
  private void updateComboBoxItems(JComboBox<String> theComboBox, String filter) 
  {
	  if (possibleProducts != null) {
		  possibleProducts.clear();
	  }
	  
	  possibleProducts = cont.doSearch(filter);
	  
	  theComboBox.removeAllItems();
	  
	  for (Map.Entry<String, String> entry : possibleProducts.entrySet()) {
		  String key = entry.getKey();
		  theComboBox.addItem(key);
	  }
  }

}
