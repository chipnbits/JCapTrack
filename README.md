
# JCapTrack

## A Java ACB and Capital Gains Calculator

### Project Proposal

With the rise of fully online digital brokerages, access to trading in the equity markets has never been easier. Research shows that the Covid-19 pandemic has had a profound impact on the number of retail investors entering the stock market for the first time.  This flurry of new activity means that there is a demand for ways to track portfolio trading and performance, as well as important metrics for when it comes time to file taxes. 

Individual investment brokerages often track this information, but they do not share it between banks.  As a result, someone who is trading at multiple brokerages, or who has switched brokerages, will not have a clear consolidated financial picture.  Even worse, the details of information across brokerages such as the *adjusted cost base* (ACB) of a traded equity may not be correctly indicated in brokerage statements.  This could result in improper tax filing information leading to auditing and penalties from the tax authority.

**JCapTrack** aims to allow a retail investor to enter and consolidate their trades across all accounts.  Features that should be included at a minimum:

- Recording of transactions
  - Date of transaction
  - Trading account  
  - Buy/Sell
  - Name of equity
  - Transaction amount
  - USD/CAD
  - Number of shares
  - Commission
   

- Personal portfolio
  - Consolidated holdings across all accounts
  - Search for transactions
  - Tax information
    - Recording of capital gains
    - Tax year summary for reporting
    
**Wish List**
- Import/Export

This project is of interest to me because every year when I file my taxes I have to sift through piles of statements.  I would prefer to have an automatically generated report at the end of the year.  Canadian Brokerages do not provide good information on capital gains because of the tax structure in Canada.  The structure of the project is well suited for OOP like Java. 

## User Stories

### Phase 1
- As a user, I want to be able to add multiple securities to my portfolio.
- As a user, I want to be able to add transactions for past dates, not only today.  
- As a user, I want to have my capital gains automatically tracked and recorded when disposing of a security.  (This means comparing the average purchase price to the sell price in CAD).
- As a user, I want to be able to search for transactions by security name.

### Phase 2
- As a user, I want to be able to save my portfolio to file
- As a user, I want to be able to be able to load my portfolio from file
- As a user, when I select the quit option from the main portfolio menu, I want all my transactions to automatically save
- As a user, I would like to be able to import and consolidate data from AdjustedCostBase.ca export files into an existing portfolio

### Phase 3

A gui was implemented using Java Swing library.  Some additional ideas for functionality were implemented such as the ability to delete transactions from a security.  The structure of the gui is designed in such a way to maximize branched access to the data in a controlled manner, this means that:  

- Multiple portfolios can be open at once, but not more than one instance of a single portfolio at a time.
- Multiple securities can be open at once, but not more than one instance of a single security at a time.
- Closing a portfolio will close all of the open securities.
- JCapTrack will no close until all portfolios are closed, this is to ensure the proper selection of data persistence from the user.

The transaction entry menu makes good use of more robust data verification and is able to notify the user of all the different errors found.  The tax slip generator was left as a TODO item due to the time constraints on the project.

### Phase 4 : Task 2

I would like to bring some attention to the console menu class hierarchy that was implemented earlier in Phase 1 of the project cycle.
The superclass is of type MenuScreen and implements the functionality of a menu with a set list of options for the user to select from and a scanner to read and validate the user input.  This super class was incredibly useful because it has 7 different subclasses that extend it to form the entire console ui.  The use of a superclass for the menu also ensures that the menus have a consistent look and feel for the user.  You can find the classes as follows:

- Supertype: ui.console.MenuScreen
- Subtypes: ui.console.portfolio.* (6)
            ui.console.JCapTrack
  
Most classes contain an override for the menu display information, and then implement the abstract methods for showing selection options or menu quit behavior. 

### Phase 4 : Task 3
  







